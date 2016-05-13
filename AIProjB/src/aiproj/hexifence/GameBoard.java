package aiproj.hexifence;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

public class GameBoard {

	char[][] gameBoard;
	int N;
	int[][] hexagons;
	int totalMovesLeft;
	int gameState;
	ArrayList<ArrayList<Hexagon>> chainList = new ArrayList<ArrayList<Hexagon>>();
	HashMap<ArrayList<Integer>,ArrayList<Hexagon>> hexagonMap;
	ArrayList<Hexagon> hexagonList;
	int blueCap = 0;
	int redCap = 0;
	
	//Constructor
	public GameBoard(int n) throws Exception{
		// Initialize game size
		this.N = n;
		// Generate a map, mapping every possible move to a hexagon
		hexagonMap = new HashMap<ArrayList<Integer>,ArrayList<Hexagon>>();
		hexagonList = new ArrayList<Hexagon>();
		generateHexagonMap();
		
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
			hex.remainingEdgeList.remove(moveKey);
			// Decrement remaining edges
			hex.remainingEdges -= 1;
			// If hexagon caputed, update board and scores
			if (hex.remainingEdges == 0){
				updateBoardCap(hex, m.P);
				if (m.P == Piece.BLUE){
					hex.capturedBy = Piece.BLUE;
					blueCap += 1;
				}else{
					hex.capturedBy = Piece.RED;
					redCap += 1;
				}
			}
		}	
		// Decrement remainingMoves
		updateChainList();
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
				returnList.add(move);
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
		for (Hexagon hex : hexagonMap.get(key)){
			hex.remainingEdgeList.add(key);
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
		hexagonMap.put(key, hexagonMap.get(key));
		updateChainList();
		totalMovesLeft += 1;
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
		if (hex.x == 0 && hex.y == 0) gameBoard[1][1] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 0 && hex.y == 1) gameBoard[1][3] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 1 && hex.y == 0) gameBoard[3][1] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 1 && hex.y == 1) gameBoard[3][3] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 1 && hex.y == 2) gameBoard[3][5] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 2 && hex.y == 1) gameBoard[5][3] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 2 && hex.y == 2) gameBoard[5][5] = (P == Piece.BLUE) ? 'b' : 'r';
	}
	
	/**
	 * Update the board after removal of Capture
	 * @param hex
	 * @param gameBoard
	 * @param P
	 */
	public void removeBoardCap(Hexagon hex){
		if (hex.x == 0 && hex.y == 0) gameBoard[1][1] = '-';
		if (hex.x == 0 && hex.y == 1) gameBoard[1][3] = '-';
		if (hex.x == 1 && hex.y == 0) gameBoard[3][1] = '-';
		if (hex.x == 1 && hex.y == 1) gameBoard[3][3] = '-';
		if (hex.x == 1 && hex.y == 2) gameBoard[3][5] = '-';
		if (hex.x == 2 && hex.y == 1) gameBoard[5][3] = '-';
		if (hex.x == 2 && hex.y == 2) gameBoard[5][5] = '-';
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
		// Add edge/move to remaining edges in hexagon
		newHex.remainingEdgeList.add(moveKey);
		// If hexagon is already present, add to it
		if (hexagonMap.containsKey(moveKey)){
			hexagonMap.get(moveKey).add(newHex);
		}
		// Otherwise, create a new list
		else{
			ArrayList<Hexagon> hexList = new ArrayList<>();
			hexList.add(newHex);
			hexagonMap.put(moveKey, hexList);
		}
	}
	
	/**
	 * Update arraylist containing all chains
	 * Uses depth first search
	 */
	public void updateChainList(){
		// Create a new list containing list of chains
		ArrayList<ArrayList<Hexagon>> chainList = new ArrayList<ArrayList<Hexagon>>();
		// Use stack for dfs
		Stack<Hexagon> stack = new Stack<Hexagon>();
		// Iterate through hexagons
		for (Hexagon hex : hexagonList){
			// If hexagon had already been added, skip
			if (findHexInList(chainList,hex)) continue;
			// Start dfs are hexagon
			stack.push(hex);
			ArrayList<Hexagon> chain = new ArrayList<Hexagon>();
			// Continue until all hexagon links are processed
			while (stack.size() > 0){
				// Pop and add to chain
				Hexagon hexagon = stack.pop();
				chain.add(hexagon);
				// Push all connecting hexagons into stack
				for (ArrayList<Integer> edges : hexagon.remainingEdgeList){
					for (Hexagon connectingHex : hexagonMap.get(edges)){
						if (!chain.contains(connectingHex)){
							stack.push(connectingHex);
						}
					}
				}
			}
			// Add finished cahin to chain list
			chainList.add(chain);
		}
		// Update chain list
		this.chainList = chainList;
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
}
