package aiproj.hexifence;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GameBoard {

	char[][] gameBoard;
	int N;
	int[][] hexagons;
	int totalMovesLeft;
	int gameState;
	HashMap<ArrayList<Integer>,ArrayList<Hexagon>> hexagonMap;
	int blueCap = 0;
	int redCap = 0;
	
	//Constructor
	public GameBoard(int n) throws Exception{
		// Initialize game size
		this.N = n;
		// Generate a map, mapping every possible move to a hexagon
		hexagonMap = new HashMap<ArrayList<Integer>,ArrayList<Hexagon>>();
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
			// Decrement remaining edges
			hex.remainingEdges -= 1;
			// If hexagon caputed, update board and scores
			if (hex.remainingEdges == 0){
				updateBoardCap(hex, gameBoard, m.P);
				if (m.P == Piece.BLUE){
					blueCap += 1;
				}else{
					redCap += 1;
				}
			}
		}	
		// Decrement remainingMoves
		this.totalMovesLeft--;
		
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
	public Hexagon checkCapture(Move m){
		// Convert to ArrayList for key
		ArrayList<Integer> moveKey = new ArrayList<Integer>(Arrays.asList(m.Row, m.Col));
		// Decrement all hexagons with edge m
		for (Hexagon hex : hexagonMap.get(moveKey)){
			if (hex.remainingEdges <= 1){
				return hex;
			}
		}
		return null;
	}
	
	/**
	 * Update the board after capture
	 * @param hex
	 * @param gameBoard
	 * @param P
	 */
	public void updateBoardCap(Hexagon hex, char[][] gameBoard, int P){
		if (hex.x == 0 && hex.y == 0) gameBoard[1][1] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 0 && hex.y == 1) gameBoard[1][3] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 1 && hex.y == 0) gameBoard[3][1] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 1 && hex.y == 1) gameBoard[3][3] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 1 && hex.y == 2) gameBoard[3][5] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 2 && hex.y == 1) gameBoard[5][3] = (P == Piece.BLUE) ? 'b' : 'r';
		if (hex.x == 2 && hex.y == 2) gameBoard[5][5] = (P == Piece.BLUE) ? 'b' : 'r';
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
	
}
