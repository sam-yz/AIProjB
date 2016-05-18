package aiproj.hexifence;
import java.io.PrintStream;
import java.util.ArrayList;

import aiproj.hexifence.*;

public class klu2 implements Player, Piece {

	private int myPieceColor;
	private Board board;
	private int dimension;
	private MoveStrategy strategy;
	private int maxNumEdges;
	@Override
	public int init(int n, int p) {
		// TODO Auto-generated method stub
		this.myPieceColor = p;
		this.dimension = n;
		this.board = new Board(n);
		this.strategy = new ABPruningStrategy();
		this.maxNumEdges = board.numPossibleMoves();
		
//		strategy.bestMove(board, 1);
//		System.out.println(ABPruningStrategy.i);
//		System.exit(0);
//		
		
		if (board == null) {
			return -1;
		}
		else {
			return 0;
		}
	}
	public void recursivePrint(Node node, PrintStream output) {
		if (node.getChildren() == null) {
			return;
		}
		for (Node child : node.getChildren()) {
			recursivePrint(child, output);
		}
	}

	@Override
	public Move makeMove() {
		int depth = 4;
		
//		depth = board.numPossibleMoves() + board.maxNumCellsInOneMove();
		
		
		Move m = strategy.bestMove(board, myPieceColor, depth);

		// TODO Auto-generated method stub
//		Move m = board.makeMove(myPieceColor);
		ArrayList<Hex> hexagonsCaptured = board.checkNumberOfHexagonsCaptured(m);
		board.update(m);
		if (hexagonsCaptured.size() > 0) {
			// Then if there are hexagons capture then update the hexagon coordinates
			for(Hex hexagon : hexagonsCaptured) {
				Move hexagonMove = new Move();
				hexagonMove.P = m.P;
				hexagonMove.Col = hexagon.getY() * 2 + 1;
				hexagonMove.Row = hexagon.getX() * 2 + 1;
				board.update(hexagonMove);
			}
		}		
		return m;
		// call board to get the move
	}	

	@Override
	public int opponentMove(Move m) {
		// Check for validity of opponents move, and then update state of the game
		if (board.isValidMove(m)) {
			ArrayList<Hex> hexagonsOpponentCaptured = board.checkNumberOfHexagonsCaptured(m);
			// First input the edge
			board.update(m);
				if (hexagonsOpponentCaptured.size() > 0) {
					// Then if there are hexagons capture then update the hexagon coordinates
					for(Hex hexagon : hexagonsOpponentCaptured) {
						Move hexagonMove = new Move();
						hexagonMove.P = m.P;
						hexagonMove.Col = hexagon.getY() * 2 + 1;
						hexagonMove.Row = hexagon.getX() * 2 + 1;
						board.update(hexagonMove);
					}
				return 1;
			}
			else {
				return 0;
			}
		}
		else {
			return -1;
		}
	}

	@Override
	public int getWinner() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void printBoard(PrintStream output) {
		board.printBoard(output);
		// TODO Auto-generated method stub
		
	}
	
}
