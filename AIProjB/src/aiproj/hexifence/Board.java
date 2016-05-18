package aiproj.hexifence;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import aiproj.hexifence.*;
public class Board implements Piece{
	public static final String FREE_STATE = "+";
	public static final String NON_EXIST = "-";
	public HashMap<Key, Hex> hexagonsInGame;
	protected HashMap<Key, Edgy> edgesInGame;
	protected String stateOfGame[][];
	protected int dimensionOfBoard;
	
	public Board(int n){
		System.out.println("Created new PartA");	
		// Create the hexagons 
		this.hexagonsInGame = generateHexagons(n);
		
		// Use the created hexagons to create the edges
		this.edgesInGame = generateEdges(hexagonsInGame);
		
		// Treating j as the column and i as the row	
		setupBoard(n);
		
	}
	private void setupBoard(int n) {
		this.dimensionOfBoard = 4*n - 1;
		this.stateOfGame = new String[dimensionOfBoard][dimensionOfBoard];
		for(int i = 0 ; i < dimensionOfBoard; i ++) {
			for(int j = 0 ; j < dimensionOfBoard; j ++) {
				Key key = new Key(i, j);
				if(edgesInGame.containsKey(key)) {
					this.stateOfGame[i][j] = FREE_STATE;
				}
				else {
					this.stateOfGame[i][j] = NON_EXIST;
				}
			}
		}
	}
	public Board(Board board) {
		this.edgesInGame = new HashMap<Key, Edgy>();
		for (Map.Entry<Key, Edgy> entry : board.edgesInGame.entrySet()) {
			Key key = entry.getKey();
			Edgy edge = entry.getValue();
			Edgy newEdge = new Edgy(edge.getX(), edge.getY(), null);
			newEdge.setState(edge.getState());
			this.edgesInGame.put(key, newEdge);
		}
		this.hexagonsInGame = new HashMap<Key, Hex>();
		for (Map.Entry<Key, Hex> entry : board.hexagonsInGame.entrySet()) {
			Key key = entry.getKey();
			Hex hexagon = entry.getValue();
			Hex newHexagon = new Hex(hexagon.getX(), hexagon.getY());
			newHexagon.setCapturedBy(hexagon.getCapturedBy());
			this.hexagonsInGame.put(key, newHexagon);
		}
		this.stateOfGame = new String[board.dimensionOfBoard][board.dimensionOfBoard];
		for (int i = 0; i < board.dimensionOfBoard; i++) {
			for (int j = 0; j < board.dimensionOfBoard; j++) {
				this.stateOfGame[i][j] = board.stateOfGame[i][j];
			}
		}
		this.dimensionOfBoard = board.dimensionOfBoard;
		// TODO Auto-generated constructor stub
	}


	public void update(Move move) {
		int p = move.P;
		int x = move.Row;
		int y = move.Col;

		if (p == Piece.BLUE) {
			//  Check if it is a hexagon coordinate
			if (x%2 == 1 && y%2 ==1) {
				this.stateOfGame[x][y] = "b";

				// Scale the key so that it is consistent with the coordinates that were 
				// used to calculate the edge coordinates
				
				Key key = new Key((x-1)/2, (y-1)/2);

				Hex hexagon = hexagonsInGame.get(key);
				hexagon.setCapturedBy("b");
			}
			else {
				Key key = new Key(x, y);
				Edgy edge = edgesInGame.get(key);
				this.stateOfGame[x][y] = "B";
				edge.setState("B");
			}
		}
		else if (p == Piece.RED) {
			if (x%2 == 1 && y%2 ==1) {
				this.stateOfGame[x][y] = "r";
				
				Key key = new Key((x-1)/2, (y-1)/2);
				Hex hexagon = hexagonsInGame.get(key);
				hexagon.setCapturedBy("r");


			}
			else {
				Key key = new Key(x, y);
				Edgy edge = edgesInGame.get(key);
				this.stateOfGame[x][y] = "R";
				edge.setState("R");
			}
		}
		
	}
	
	public ArrayList<Hex> checkNumberOfHexagonsCaptured(Move m) {
		int x = m.Row;
		int y = m.Col;
		ArrayList<Hex> hexagonsCaptured = new ArrayList<Hex>();
		
		// Iterate through all the edges and 
		for (Hex hexagon : hexagonsInGame.values()) {
			ArrayList<Edgy> freeEdges = new ArrayList<Edgy>();
			for(Edgy edge : hexagon.getAvailableEdges()) {
				Key key = new Key(edge.getX(), edge.getY());
				Edgy edgeInGame = edgesInGame.get(key);
				if(edgeInGame.getState() == FREE_STATE) {
					freeEdges.add(edge);
				}
			}
			if (freeEdges.size() == 1) {
				Edgy edge = freeEdges.get(0);
				if (edge.getX() == x && edge.getY() == y) {
					hexagonsCaptured.add(hexagon);
				}
			}
		}
		return hexagonsCaptured;
	}
	
	
	public boolean isValidMove(Move m) {
		Key key = new Key(m.Row, m.Col);
		if (edgesInGame.containsKey(key)) {
			Edgy edge = edgesInGame.get(key);
			if (edge.getState() == FREE_STATE) {
				return true;
			}
			else {
				return false;
			}
		}
		
		return false;
	}
	public static int k = 0;
	public Move makeMove(int p) {
		k++;
		int random = new Random().nextInt(edgesInGame.values().size());
		Edgy edge = (Edgy) edgesInGame.values().toArray()[random];
		Move move = new Move();
		move.Row = edge.getX();
		move.Col = edge.getY();
		move.P = p;
		
		if (isValidMove(move)) {
//			return move;
			if (this.checkNumberOfHexagonsCaptured(move).size() != 0) {
				return move;
			} else {
				if (k > 30) {
					return move;
				} else {
					return makeMove(p);
				}
			}
		} else {
			return makeMove(p);
		}
//		for (Edge edge : edgesInGame.values()) {
//			
//			if (isValidMove(move)) {
//				return move;
//			}
//		}
	}

	private HashMap<Key, Hex> generateHexagons(int n) {
		HashMap<Key, Hex> hexagonsInGame = new HashMap<Key, Hex>();

		boolean halfWayReached = false;
		int halfWay = (int) Math.ceil((double) n / 2);
		for (int i = 0; i < 2 * n - 1; i++) {
			if (!halfWayReached) {
				int j = 0;
				while (j <= halfWay + i) {
					Hex hexagon = new Hex(i, j);
					Key key = new Key(i, j);
					hexagonsInGame.put(key, hexagon);
					j += 1;
				}
			} else {
				int j = i - halfWay;
				while (j < 2 * n - 1) {
					Hex hexagon = new Hex(i, j);
					Key key = new Key(i, j);
					hexagonsInGame.put(key, hexagon);
					j += 1;
				}
			}

			if (i == halfWay) {
				halfWayReached = true;
			}
		}
		return hexagonsInGame;
	}

	public HashMap<Key, Edgy> generateEdges(
			HashMap<Key, Hex> hexagonsInGame) {
		HashMap<Key, Edgy> edgesInGame = new HashMap<Key, Edgy>();
		for (Hex hexagon : hexagonsInGame.values()) {
			for (Edgy edge : hexagon.getAvailableEdges()) {
				Key key = new Key(edge.getX(), edge.getY());
				edgesInGame.put(key, edge);
			}
		}
		return edgesInGame;
	}
	/*
	 * Calculate the number of free edges in game
	 */
	public int numPossibleMoves() {
		int numPossibleMoves = 0;
		for (Edgy edge : edgesInGame.values()) {
			if (edge.getState().equals(FREE_STATE)) {
				numPossibleMoves++;
			}
		}
		return numPossibleMoves;
	}
	
	/*
	 * Calculate the largest number of cells that can be capture in 1 move
	 */
	public int maxNumCellsInOneMove() {
		int maxNumCells = 0;
		ArrayList<Edgy> freeEdges = new ArrayList<Edgy>();
		for (Hex hexagon : hexagonsInGame.values()) {
			int numFreeEdges = 0;
			Edgy freeEdge = null;
			
			ArrayList<Edgy> availableEdges = hexagon.getAvailableEdges();
			// check every available edge and count how many free edges
			for (Edgy edge : availableEdges) {
				Key key = new Key(edge.getX(), edge.getY());
				Edgy edgeInGame = edgesInGame.get(key);
				if (edgeInGame.getState().equals(FREE_STATE)) {
					freeEdge = edgeInGame;
					numFreeEdges++;
				}
			}
			// if only 1 free edge then hexagon can be completed in 1 move
			if (numFreeEdges == 1) {
				maxNumCells = 1;
				// check if the free edge has previously appeared, if it does
				// then the two hexagon shares one free edge
				if (freeEdges.contains(freeEdge)) {
					// since maximum number of edge that can be captured is 2
					// we can break the loop and return
					maxNumCells = 2;
					break;
				} else {
					freeEdges.add(freeEdge);
				}
			}
		}
		
		return maxNumCells;
	}
	
	/*
	 * Calculate number of edges that will complete a hexagon
	 */
	public int numCellsAvailableInOneMove() {
		int numCellsAvailable = 0;
		for (Hex hexagon : hexagonsInGame.values()) {
			int numFreeEdges = 0;
			ArrayList<Edgy> availableEdges = hexagon.getAvailableEdges();
			// check every available edge and count how many free edges
			for (Edgy edge : availableEdges) {
				Key key = new Key(edge.getX(), edge.getY());
				Edgy edgeInGame = edgesInGame.get(key);
				if (edgeInGame.getState().equals(FREE_STATE)) {
					numFreeEdges++;
				}
			}
			// if only 1 free edge then hexagon can be completed in 1 move
			if (numFreeEdges == 1) {
				numCellsAvailable++;
			}
		}
		return numCellsAvailable;
	}
	
	public int calcEvaluationValue(int p) {
		int max = 0;
		int min = 0;
		for (int i = 0; i < this.dimensionOfBoard; i++) {
			for (int j = 0; j < this.dimensionOfBoard; j++) {
				if (p == Piece.BLUE) {
					if (this.stateOfGame[i][j].equals("b")) {
						max++;
					} else if (this.stateOfGame[i][j].equals("r")) {
						min++;
					}
				} else if (p == Piece.RED) {
					if (this.stateOfGame[i][j].equals("r")) {
						max++;
					} else if (this.stateOfGame[i][j].equals("b")) {
						min++;
					}
				}
				
			}
		}

		return max-min;
	}
	
	public void printBoard(PrintStream output) {
		for(int i = 0; i < dimensionOfBoard; i++) {
			for(int j = 0; j < dimensionOfBoard; j++) {
				output.print(stateOfGame[i][j]);
				output.print(" ");
			}
			output.println();
		}
		// TODO Auto-generated method stub
		
	}

	
}
