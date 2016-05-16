package aiproj.hexifence;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class BasicAgent implements Player, Piece {
	GameBoard gameBoard;
	int pieceColor;
	int oppPieceColor;
	ArrayList<Move> movesLeft;
	
	
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
			
			movesLeft = new ArrayList<Move>();
			genMoves();
			
			return 0;
		}
		catch(Exception e){
			return 1;
		}
	}
	
	public void genMoves(){
		int row = 0;
		// Iterate through row
		for (char[] mRow : gameBoard.gameBoard){
			int col = 0;
			// Iterate through column
			for (char move : mRow){
				// Add move if capturable
				if (move == '+'){
					movesLeft.add(new Move(col, row, pieceColor));
				}
				col += 1;
			}
			row += 1;
		}
	}

	@Override
	public Move makeMove() {
		ArrayList<ArrayList<Integer>> noThreatMoves = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> noChoice = null;
		// Return move if capturable
		int rand = (int) (Math.random()*(gameBoard.totalMovesLeft));
		Move randMove = null;
		int row = 0;
		for (char[] mRow : gameBoard.gameBoard){
			int col = 0;
			for (char move : mRow){
				if (move == '+'){
					Move m = new Move(col, row, this.pieceColor);
					if (gameBoard.checkCapture(m)){
						gameBoard.update(m);
						return m;
					}
					
					int count = 0;
					ArrayList<Integer> key = new ArrayList<Integer>(Arrays.asList(row, col));
					for (Hexagon hex : gameBoard.hexagonMap.get(key)){
						if (hex.remainingEdges == 2){
							count++;
						}
					}
					if (rand == 0){
						noChoice = key;
					}

					//No hexs connected to this move so it is a safe move, it just isnt our initial random
					if (count == 0 && rand == 0){
						randMove = m;
					}
					else if (count == 0 && rand != 0){
						noThreatMoves.add(key);
					}

					rand -= 1;
				}
				col += 1;
			}
			row += 1;
		}
		
		if (randMove == null && noThreatMoves.size() > 0){
			int index = (int)(Math.random() * (noThreatMoves.size()));
			randMove = new Move(noThreatMoves.get(index).get(1), noThreatMoves.get(index).get(0), this.pieceColor);
		}
		else{
			randMove = new Move(noChoice.get(1), noChoice.get(0), this.pieceColor);
		}

		gameBoard.update(randMove);
		return randMove;
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
		if (!gameBoard.checkCapture(m)){
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