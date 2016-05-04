package aiproj.hexifence;

import java.io.PrintStream;

public class Samuely2 implements Player, Piece {
	GameBoard gameBoard;
	int pieceColor;
	int oppPieceColor;
	
	@Override
	public int init(int n, int p) {
		try{
			gameBoard = new GameBoard(n);
			this.pieceColor = p;
			if (p == Piece.BLUE){
				oppPieceColor = Piece.RED;
			}else{
				oppPieceColor = Piece.BLUE;
			}
			return 0;
		}
		catch(Exception e){
			return 1;
		}
	}

	@Override
	public Move makeMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int opponentMove(Move m) {
		if (!gameBoard.checkValid(m)){
			return -1;
		}
		gameBoard.update(m, oppPieceColor);
		if (!gameBoard.checkCapture(m)){
			return 0;
		}
		return 1;
	}

	@Override
	public int getWinner() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void printBoard(PrintStream output) {
		// TODO Auto-generated method stub

	}

}
