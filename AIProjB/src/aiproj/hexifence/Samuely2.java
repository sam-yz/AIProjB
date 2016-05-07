package aiproj.hexifence;

import java.io.PrintStream;
import java.util.HashMap;

public class Samuely2 implements Player, Piece {
	public GameBoard gameBoard;
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
			gameBoard.gameState = Piece.EMPTY;
			
			return 0;
		}
		catch(Exception e){
			return 1;
		}
	}

	@Override
	public Move makeMove() {
		Move m = new Move();
		m.P = this.pieceColor;
		
		//get all available moves
		int availableCount = 0;
		for (char cRow[] : gameBoard.gameBoard){
			for (char c : cRow){
				if (c == '+'){
					availableCount++;
				}
			}
		}
		
		//Get a random move from the set of available moves
		boolean flag = false;
		int rand = (int)(Math.random()*(availableCount));
		int row = 0;
		loop1: for (char[] cRow : gameBoard.gameBoard){
			int col = 0;
			for (char c : cRow){
				if (c == '+'){
					if (rand == 0){
						m.Row = row;
						m.Col = col;
						flag = true;
						break loop1;
					}
					rand--;
				}
				col++;
			}
			row++;
		}
		if (flag){
			gameBoard.update(m);
			gameBoard.checkCapture(m);
			return m;
		}
		else{
			System.out.println("Fked up");
			return m;
		}
	}

	@Override
	public int opponentMove(Move m) {

		//Check if move is valid
		if (!gameBoard.checkValid(m)){
			this.gameBoard.gameState = Piece.INVALID;
			return -1;
		}
	
		//Check if move m captures any hexagons
		//return 0 if none captured
		if (gameBoard.checkCapture(m) == false){
			this.gameBoard.update(m);
			return 0;
		}
		//return 1 if move is valid and a hexagon is captured by move m
		this.gameBoard.update(m);
		return 1;
	}

	@Override
	public int getWinner() {
		if (this.gameBoard.totalMovesLeft == 0 || this.gameBoard.gameState == Piece.INVALID){
			//Perform end game checks only if there are no more moves to be played or
			//the game ended due to an invalid move
			int oppCount = 0;
			int ourCount = 0;
			if (pieceColor == Piece.BLUE){
				ourCount = gameBoard.blueCap;
				oppCount = gameBoard.redCap;
			}else{
				ourCount = gameBoard.redCap;
				oppCount = gameBoard.blueCap;
			}
			if (oppCount > ourCount){
				return this.oppPieceColor;
			}
			else if (oppCount == ourCount){
				System.out.println(oppCount);
				System.out.println(ourCount);
				return Piece.DEAD;
			}
			else{
				return this.pieceColor;
			}
		}
		//Game still in progress
		return Piece.EMPTY;
	}

	
	@Override
	public void printBoard(PrintStream output) {
		gameBoard.printBoard(output);

	}

}
