package aiproj.hexifence;
import java.util.ArrayList;

import aiproj.hexifence.Move;

public class Node {
	private Board board;
	private ArrayList<Node> children;
	private int value;
	private Move move;
	
	public Node(Move moveMade, Board board) {
		super();
		this.board = board;
		this.setMove(moveMade);
	}
	
	public void generateChildren(int piece) {
		children = new ArrayList<Node>();
		for (Edgy e : this.board.edgesInGame.values()) {
			// Create the move 
			Move m = new Move();
			m.P = piece;
			m.Row = e.getX();
			m.Col = e.getY();
			if (board.isValidMove(m)) {
				// Create a new board when move is made
				Board newBoard = new Board(board);
				ArrayList<Hex> hexagonsOpponentCaptured = newBoard.checkNumberOfHexagonsCaptured(m);
				// First input the edge
				if (hexagonsOpponentCaptured.size() > 0) {
					// Then if there are hexagons capture then update the hexagon coordinates
					for(Hex hexagon : hexagonsOpponentCaptured) {
						Move hexagonMove = new Move();
						hexagonMove.P = m.P;
						hexagonMove.Col = hexagon.getY() * 2 + 1;
						hexagonMove.Row = hexagon.getX() * 2 + 1;
						newBoard.update(hexagonMove);
					}
				}
				newBoard.update(m);
				Node newNode = new Node(m, newBoard);
				children.add(newNode);
			}
		}
	}
	public Board getBoard() {
		return this.board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public ArrayList<Node> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}
	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
