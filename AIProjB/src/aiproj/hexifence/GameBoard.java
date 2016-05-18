package aiproj.hexifence;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

public class GameBoard {

	char[][] gameBoard;
	int N;
	int[][] hexagons;
	int totalMovesLeft;
	int gameState;
	HashMap<ArrayList<Integer>,ArrayList<Hexagon>> hexagonMap;
	ArrayList<ArrayList<Hexagon>> chainList;
	ArrayList<Hexagon> hexagonList;
	int blueCap = 0;
	int redCap = 0;
	
	//Constructor
	public GameBoard(int n) throws Exception{
		// Initialize game size
		this.N = n;
		// Generate a map, mapping every possible move to a hexagon
		chainList = new ArrayList<ArrayList<Hexagon>>();
		hexagonMap = new HashMap<ArrayList<Integer>,ArrayList<Hexagon>>();
		hexagonList = new ArrayList<Hexagon>();
		generateHexagonMap();
//		System.out.println(hexagonList.size());
		for (Hexagon hex : hexagonList){
			hex.generateGraphConnections(hexagonMap);
		}

		
		if (n == 3){
			gameBoard = new char[][]{
									 {'+', '+', '+', '+', '+', '+', '-', '-', '-', '-', '-'},
									 {'+', '-', '+', '-', '+', '-', '+', '-', '-', '-', '-'},
									 {'+', '+', '+', '+', '+', '+', '+', '+', '-', '-', '-'},
									 {'+', '-', '+', '-', '+', '-', '+', '-', '+', '-', '-'},
									 {'+', '+', '+', '+', '+', '+', '+', '+', '+', '+', '-'},
									 {'+', '-', '+', '-', '+', '-', '+', '-', '+', '-', '+'},
									 {'-', '+', '+', '+', '+', '+', '+', '+', '+', '+', '+'},
									 {'-', '-', '+', '-', '+', '-', '+', '-', '+', '-', '+'},
									 {'-', '-', '-', '+', '+', '+', '+', '+', '+', '+', '+'},
									 {'-', '-', '-', '-', '+', '-', '+', '-', '+', '-', '+'},
									 {'-', '-', '-', '-', '-', '+', '+', '+', '+', '+', '+'},
									};
									
			this.totalMovesLeft = 72;
		}
		else if (n == 2){
			gameBoard = new char[][]{
									 {'+', '+', '+', '+', '-', '-', '-'},
									 {'+', '-', '+', '-', '+', '-', '-'},
									 {'+', '+', '+', '+', '+', '+', '-'},
									 {'+', '-', '+', '-', '+', '-', '+'},
									 {'-', '+', '+', '+', '+', '+', '+'},
									 {'-', '-', '+', '-', '+', '-', '+'},
									 {'-', '-', '-', '+', '+', '+', '+'},
									};
			this.totalMovesLeft = 30;
		}
		else{
			throw new Exception("Board size not valid, must be 2 or 3");
		}
	}
	
	/**
	 * Print the board
	 * @param output
	 */
	public void printBoard(PrintStream output){
		for (char[] row : gameBoard){
			for (char column : row){
				output.print(column + " ");
			}
			output.print('\n');
		}
	}
	
	/**
	 * Generate hexagons according to board size
	 * @param n Board size
	 */
	private void generateHexagons(int n){
		// Hexagons for board size 2
		if (n == 2){
			hexagons = new int[][] {{0,0},{0,1},
									{1,0},{1,1},{1,2},
									{2,1},{2,2}};
		}
		// Hexagons for board size 3
		else if (n == 3){
			hexagons = new int[][] {{0,0},{0,1},{0,2},
									{1,0},{1,1},{1,2},{1,3},
									{2,0},{2,1},{2,2},{2,3},{2,4},
									{3,1},{3,2},{3,3},{3,4},
									{4,2},{4,3},{4,4}};
		}
	}
	
	/**
	 * Update the gameboard
	 * @param m Move
	 * @param p Piece color
	 */
	public void update(Move m){
		
		if (this.totalMovesLeft <= 0){
			return;
		}
		// Update Board for piece
		if (m.P == Piece.BLUE){
			gameBoard[m.Row][m.Col] = 'B';
		}
		else{
			gameBoard[m.Row][m.Col] = 'R';
		}
		// Update all hexagons with edge m
		ArrayList<Integer> moveKey = new ArrayList<Integer>(Arrays.asList(m.Row, m.Col));
		for (Hexagon hex : hexagonMap.get(moveKey)){
			
			// Decrement remaining edges
			hex.updateEdge(moveKey);
			
			// If hexagon captured, update board and scores
			if (hex.remainingEdges == 0){
				updateBoardCap(hex, m.P);
				
				if (m.P == Piece.BLUE){
					hex.capturedBy = Piece.BLUE;
					blueCap += 1;
				}
				else{
					hex.capturedBy = Piece.RED;
					redCap += 1;
				}
			}
		}	
		// Decrement remainingMoves & update Chains
		updateChains();
		this.totalMovesLeft--;
	}
	
	
	
	/**
	 * Get all possible moves
	 * @return  ArrayList of possible moves
	 */
	public ArrayList<ArrayList<Integer>> getMoves(){
		ArrayList<ArrayList<Integer>> returnList = new ArrayList<ArrayList<Integer>>();
		for (ArrayList<Integer> move : hexagonMap.keySet()){
			if (gameBoard[move.get(0)][move.get(1)] == '+'){
				returnList.add((int)(Math.random()*returnList.size()), move);
			}
		}
		return returnList;
	}
	
	/**
	 * Remove move played (Used in minimax)
	 * @throws Exception 
	 */
	public void remove(ArrayList<Integer> key){
		// Get col/row values
		int row = key.get(0);
		int col = key.get(1);
		// Check if board is occupied, remove move
		if (gameBoard[row][col] == 'B' || gameBoard[row][col] == 'R'){
			gameBoard[row][col] = '+';
		}else{
			System.out.println(gameBoard[row][col]);
		}
		// Add back to hexagonMap
		addKeyBack(key);
		
		updateChains();
		totalMovesLeft += 1;
	}

	private void addKeyBack(ArrayList<Integer> key){
		ArrayList<Hexagon> mappedHexes = hexagonMap.get(key);
		for (Hexagon hex : mappedHexes){
			if (hex.remainingEdges == 0){
				if (hex.capturedBy == Piece.BLUE){
					blueCap -= 1;
				}else if (hex.capturedBy == Piece.RED){
					redCap -= 1;
				}
			}
			
			hex.remainingEdges += 1;
			removeBoardCap(hex);
		}
		
		if (mappedHexes.size() == 2){
			mappedHexes.get(0).edges.add(new Edge(key, mappedHexes.get(1)));
			mappedHexes.get(1).edges.add(new Edge(key, mappedHexes.get(0)));
		}
		
		hexagonMap.put(key, hexagonMap.get(key));
	}
	
	
	/**
	 * Checks if hexagon is found in hexagon list
	 * @param hexList List of hexagon chains
	 * @param hex Hexagon to check
	 * @return
	 */
	private boolean findHexInList(ArrayList<ArrayList<Hexagon>> hexList, Hexagon hex){
		// Check all lists
		for (ArrayList<Hexagon> hexList2 : hexList){
			if (hexList2.contains(hex)){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	/**
	 * Checks if move is valid in current GameBoard state
	 * @param m Move
	 * @return boolean value if valid
	 */
	public boolean checkValid(Move m){
		// Check if space is open
		if (gameBoard[m.Row][m.Col] == '+'){
			// If open, move is valid
			return true;
		}
		// If space is not open or taken, move is invalid
		return false;
	}
	
	/**
	 * Check if move captures a hexagon
	 * @param m Move
	 * @return boolean true if capturable
	 */
	public boolean checkCapture(Move m){
		// Convert to ArrayList for key
		ArrayList<Integer> moveKey = new ArrayList<Integer>(Arrays.asList(m.Row, m.Col));
		// Decrement all hexagons with edge m
		for (Hexagon hex : hexagonMap.get(moveKey)){
			if (hex.remainingEdges <= 1){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Update the board after capture
	 * @param hex
	 * @param gameBoard
	 * @param P
	 */
	public void updateBoardCap(Hexagon hex, int P){
		hex.capturedBy = -1;
		//Board 2
		if (hex.x == 0 && hex.y == 0) gameBoard[1][1] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 0 && hex.y == 1) gameBoard[1][3] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 1 && hex.y == 0) gameBoard[3][1] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 1 && hex.y == 1) gameBoard[3][3] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 1 && hex.y == 2) gameBoard[3][5] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 2 && hex.y == 1) gameBoard[5][3] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 2 && hex.y == 2) gameBoard[5][5] = (P == Piece.BLUE) ? 'b' : 'r';
		//Board 3 extra hexagons
		if (hex.x == 0 && hex.y == 2) gameBoard[1][5] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 1 && hex.y == 3) gameBoard[3][7] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 2 && hex.y == 0) gameBoard[5][1] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 2 && hex.y == 3) gameBoard[5][7] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 2 && hex.y == 4) gameBoard[5][9] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 3 && hex.y == 1) gameBoard[7][3] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 3 && hex.y == 2) gameBoard[7][5] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 3 && hex.y == 3) gameBoard[7][7] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 3 && hex.y == 4) gameBoard[7][9] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 4 && hex.y == 2) gameBoard[9][5] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 4 && hex.y == 3) gameBoard[9][7] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 4 && hex.y == 4) gameBoard[9][9] = (P == Piece.BLUE) ? 'b' : 'r';
	}
	
	/**
	 * Update the board after removal of Capture
	 * @param hex
	 * @param gameBoard
	 * @param P
	 */
	public void removeBoardCap(Hexagon hex){
		//Board 2
		if (hex.x == 0 && hex.y == 0) gameBoard[1][1] = '-';
		if (hex.x == 0 && hex.y == 1) gameBoard[1][3] = '-';
		if (hex.x == 1 && hex.y == 0) gameBoard[3][1] = '-';
		if (hex.x == 1 && hex.y == 1) gameBoard[3][3] = '-';
		if (hex.x == 1 && hex.y == 2) gameBoard[3][5] = '-';
		if (hex.x == 2 && hex.y == 1) gameBoard[5][3] = '-';
		if (hex.x == 2 && hex.y == 2) gameBoard[5][5] = '-';
		
		//Board 3 Extras
		if (hex.x == 0 && hex.y == 2) gameBoard[1][5] = '-';
		if (hex.x == 1 && hex.y == 3) gameBoard[3][7] = '-';
		if (hex.x == 2 && hex.y == 0) gameBoard[5][1] = '-';
		if (hex.x == 2 && hex.y == 3) gameBoard[5][7] = '-';
		if (hex.x == 2 && hex.y == 4) gameBoard[5][9] = '-';
		if (hex.x == 3 && hex.y == 1) gameBoard[7][3] = '-';
		if (hex.x == 3 && hex.y == 2) gameBoard[7][5] = '-';
		if (hex.x == 3 && hex.y == 3) gameBoard[7][7] = '-';
		if (hex.x == 3 && hex.y == 4) gameBoard[7][9] = '-';
		if (hex.x == 4 && hex.y == 2) gameBoard[9][5] = '-';
		if (hex.x == 4 && hex.y == 3) gameBoard[9][7] = '-';
		if (hex.x == 4 && hex.y == 4) gameBoard[9][9] = '-';
	}
	
	/**
	 * Build hashmap of all possible hexagons
	 */
	private void generateHexagonMap(){
		// Generate all possible hexagons
		generateHexagons(this.N);
		ArrayList<Integer> alKey;
		for (int[] hexagon : hexagons){
			int x = hexagon[0];
			int y = hexagon[1];
			Hexagon newHex = new Hexagon(x,y);
			hexagonList.add(newHex);
			// Hashmap needs key as ArrayList because the .equals function
			// does not check the object's hashcode, and instead checks
			// the actual values inside. Therefore, two different objects
			// can be used as the key provided the integers inside are identical
			
			// Right Top
			alKey = new ArrayList<Integer>(Arrays.asList(2*x, 2*y+1));
			addHexagon(alKey, newHex);
			// Left Top
			alKey = new ArrayList<Integer>(Arrays.asList(2*x, 2*y));
			addHexagon(alKey, newHex);
			// Right
			alKey = new ArrayList<Integer>(Arrays.asList(2*x+1, 2*y+2));
			addHexagon(alKey, newHex);
			// Left
			alKey = new ArrayList<Integer>(Arrays.asList(2*x+1, 2*y));
			addHexagon(alKey, newHex);
			// Left Bottom
			alKey = new ArrayList<Integer>(Arrays.asList(2*x+2, 2*y+1));
			addHexagon(alKey, newHex);
			// Right Bottom
			alKey = new ArrayList<Integer>(Arrays.asList(2*x+2, 2*y+2));
			addHexagon(alKey, newHex);
		}
	}
	
	
	/**
	 * Add hexagon to hexagonMap
	 * @param moveKey Key for hashmap
	 * @param newHex Hexagon to add
	 */
	private void addHexagon(ArrayList<Integer> moveKey, Hexagon newHex){
		if (hexagonMap.containsKey(moveKey)){
			hexagonMap.get(moveKey).add(newHex);
		}else{
			ArrayList<Hexagon> hexList = new ArrayList<>();
			hexList.add(newHex);
			hexagonMap.put(moveKey, hexList);
		}
	}
	
	
	
	//###########################################################################################
	//Stuff for Eval Function	
	
	
	/**
	 * Method to find the number of long chains and total number of chains in the graph.
	 * A long chain is defined to be size 3 or greater
	 * @return
	 */
	public void updateChains(){

		ArrayList<ArrayList<Hexagon>> returnList = new ArrayList<ArrayList<Hexagon>>();
		
		for (Hexagon hex : this.hexagonList){
			if (!hex.visited){
				ArrayList<Hexagon> newChain = new ArrayList<Hexagon>();
				//find connected components in the chain
				dfs(hex, hex, null, 0, newChain);
				
				returnList.add(newChain);
			}
		}

		for (Hexagon hex : this.hexagonList){
			hex.visited = false;
		}
		
		Collections.sort(returnList, new Comparator<ArrayList>(){
			public int compare(ArrayList o1, ArrayList o2){
				return o1.size() - o2.size();
			}
		});
		
		this.chainList = returnList;
	}
	
	
	
	public int numberOfLongChains(){
		int numLongChains = 0;
		for (ArrayList<Hexagon> chain : chainList){
			if (chain.size() > 2){
				numLongChains++;
			}
		}
		
		return numLongChains;
	}
	
	
	
	/**
	 * DFS through the hexagons that returns the length of the DFS. 
	 * Runs it from the start node, incrementing the length count each level of recursion.
	 * CycleTest is to determine whether or not we have a cycle. Cycles count as 2 long
	 * chains in theory, so we flag it by returning a -ve value.
	 * @param startNode
	 * @param cycleTest
	 * @param count
	 * @return
	 */
	private int dfs(Hexagon startNode, Hexagon cycleTest, Hexagon prevNode, int count, ArrayList<Hexagon> chain){
		count++;
		startNode.visited = true;
		chain.add(startNode);
		
		for (Edge e : startNode.edges){
			//Don't check edge leading to previous Node=
			if (e.endNode.equals(prevNode)){
				continue;
			}
			//recurse down if we can continue
			if (!e.endNode.visited){
				dfs(e.endNode, cycleTest, startNode, count, chain);
			}
			//Return a -ve value if we find a cycle
			else if (e.endNode.visited && e.endNode.equals(cycleTest)){
				count-=100;
			}
			
		}
		return count;
	}
	
	
	//###########################################################################################
	//fin
	
	//###########################################################################################
	//###############################    MOVE ORDERING    #######################################
	
	public ArrayList<ArrayList<Integer>> getOrderedMoves(){
		ArrayList<ArrayList<Integer>> possibleMoves = getMoves();
		ArrayList<ArrayList<Integer>> returnList = new ArrayList<ArrayList<Integer>>();
		LinkedList<Hexagon> queue = new LinkedList<Hexagon>();
		
		//Use bfs to expand outward from the hex with the smallest num edges
		queue.add(findLeastEdgeHexagon());
		
		while(!queue.isEmpty()){
			Hexagon u = queue.removeFirst();
			u.visited = true;
			for (Edge e : u.edges){
				returnList.add(e.pos);
				if (!e.endNode.visited){
					queue.add(e.endNode);
				}
			}
		}
		
		//Add all the edges related to just one hex
		for (ArrayList<Integer> move : possibleMoves){
			if(!returnList.contains(move)){
				returnList.add(move);
			}
		}
		
		return returnList;
	}
	
	public ArrayList<ArrayList<Integer>> getSmallestChainMoves(){
		ArrayList<ArrayList<Integer>> returnList = new ArrayList<ArrayList<Integer>>();

		for (Hexagon hex : chainList.get(0)){
			int x = hex.x;
			int y = hex.y;
				//top left
			if (gameBoard[2*x][2*y] == '+'){
				ArrayList<Integer> moveCoord = new ArrayList<Integer>(Arrays.asList(2*x, 2*y));
				if (!returnList.contains(moveCoord)){
					returnList.add(moveCoord);
				}
			}
			//top right
			if (gameBoard[2*x][2*y+1] == '+'){
				ArrayList<Integer> moveCoord = new ArrayList<Integer>(Arrays.asList(2*x, 2*y+1));
				if (!returnList.contains(moveCoord)){
					returnList.add(moveCoord);
				}
			}
			//left
			if (gameBoard[2*x+1][2*y] == '+'){
				ArrayList<Integer> moveCoord = new ArrayList<Integer>(Arrays.asList(2*x+1, 2*y));
				if (!returnList.contains(moveCoord)){
					returnList.add(moveCoord);
				}
			}
			//right
			if (gameBoard[2*x+1][2*y+2] == '+'){
				ArrayList<Integer> moveCoord = new ArrayList<Integer>(Arrays.asList(2*x+1, 2*y+2));
				if (!returnList.contains(moveCoord)){
					returnList.add(moveCoord);
				}
			}
			//bottom left
			if (gameBoard[2*x+2][2*y+1] == '+'){
				ArrayList<Integer> moveCoord = new ArrayList<Integer>(Arrays.asList(2*x+2, 2*y+1));
				if (!returnList.contains(moveCoord)){
					returnList.add(moveCoord);
				}
			}
			//bottom right
			if (gameBoard[2*x+2][2*y+2] == '+'){
				ArrayList<Integer> moveCoord = new ArrayList<Integer>(Arrays.asList(2*x+2, 2*y+2));
				if (!returnList.contains(moveCoord)){
					returnList.add(moveCoord);
				}
			}
			
		}
		return returnList;
	}
	
	
	
	public ArrayList<ArrayList<Integer>> safeMoves(){
		ArrayList<ArrayList<Integer>> noThreatMoves = new ArrayList<ArrayList<Integer>>();
		
		for (ArrayList<Integer> move : hexagonMap.keySet()){
			if (gameBoard[move.get(0)][move.get(1)] == '+'){
				int count = 0;
				for (Hexagon hex : hexagonMap.get(move)){
					if (hex.remainingEdges == 2){
						count++;
						break;
					}
				}
				if (count == 0){
					noThreatMoves.add(move);
				}
			}
		}
		
		return noThreatMoves;
	}
	
	
	
	
	private Hexagon findLeastEdgeHexagon(){
		int minEdges = 7;
		Hexagon returnHex = null;
		for (Hexagon h : hexagonList){
			if (h.remainingEdges < minEdges){
				returnHex = h;
			}
		}
		
		return returnHex;
	}

	
	
}
