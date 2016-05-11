package aiproj.hexifence;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MinimaxAgent implements Player{
	public GameBoard gameBoard;
	int pieceColor;
	int oppPieceColor;
	private static int MINIMAX_DEPTH = 10;
	private static ArrayList<ArrayList<Integer>> connectingEdges = new ArrayList<ArrayList<Integer>>();
	
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
	public Move makeMove(){
//		// Return move if capturable
//		int row = 0;
//		for (ArrayList<Integer> move : gameBoard.getMoves()){
//			Move mv = new Move(move.get(1),move.get(0),pieceColor);
//			if (gameBoard.checkCapture(mv)) {
//				gameBoard.update(mv);
//				return mv;
//			}
//		}
		// When to start using minimax
		if (gameBoard.totalMovesLeft > -1){
			// Use minimax
			int[] moveDet = minimax(7, pieceColor, Integer.MIN_VALUE, Integer.MAX_VALUE);
			Move move = new Move(moveDet[2], moveDet[1], pieceColor);
			gameBoard.update(move);
			return move;
		}
		// Use minimax
		int[] moveDet = minimax(100, pieceColor, Integer.MIN_VALUE, Integer.MAX_VALUE);
		Move move = new Move(moveDet[2], moveDet[1], pieceColor);
		gameBoard.update(move);
		return move;
	}
	
	private int[] minimax(int depth, int currPieceColor, int alpha, int beta) {
		// We are maximising, while opponent is minimising
		int currScore;
	    int bestRow = -1;
	    int bestCol = -1;
	    
	    // Gameover or depth reached, evaluate score
		if (gameBoard.totalMovesLeft == 0 || depth == 0){
			if (gameBoard.totalMovesLeft == 0) currScore = evaluateBoard3();
			else currScore = evaluateBoard3();
			return new int[]{currScore, bestRow, bestCol};
		}
		else{
			// Iterate through all moves
			for (ArrayList<Integer> move : gameBoard.getMoves()){
				// Update the board for the current move
				Move move_2 = new Move(move.get(1),move.get(0), currPieceColor);
				boolean captureHex = gameBoard.checkCapture(move_2);
				gameBoard.update(move_2);
				// Maximizing score
				if (currPieceColor == pieceColor){
					// If move captures hexagon, maximize again
					if (captureHex) currScore = minimax(depth - 1, pieceColor, alpha, beta)[0];
					else currScore = minimax(depth - 1, oppPieceColor, alpha, beta)[0];
					// Update score values
					if (currScore > alpha){
						alpha = currScore;
						bestRow = move.get(0);
						bestCol = move.get(1);
					}
				}
				// Minimizing score
				else if (currPieceColor == oppPieceColor){
					// If move captures hexagon, minimize again
					if (captureHex)currScore = minimax(depth - 1, oppPieceColor, alpha, beta)[0];
					else currScore = minimax(depth - 1, pieceColor, alpha, beta)[0];
					// Update score values
					if (currScore < beta){
						beta = currScore;
						bestRow = move.get(0);
						bestCol = move.get(1);
					}
				}
				// Revert board back to original state
				gameBoard.remove(move);
	            // A/B pruning
	            if (alpha >= beta) break;
			}
		}
		// Return score values
		return new int[]{(currPieceColor == pieceColor) ? alpha : beta, bestRow, bestCol};
	}
	
	/**
	 * Evaluation function without reaching end
	 * @return
	 */
	private int evaluateBoard3(){
		int score = 0;
		for (Hexagon hex : gameBoard.hexagonList){
			switch (hex.remainingEdges){
				case 0:{
					if (hex.capturedBy == pieceColor)score += 20;
					else score -= 20;
					break;
				}
				case 1:{
					score += 10;
					break;
				}
				case 2: {
					score -= 1;
					break;
				}
				case 3: {
					score += 1;
					break;
				}
				case 4: {
					score -= 0;
					break;
				}
				case 5: {
					score += 0;
					break;
				}
			}
		}
		return score;
	}
	
	/**
	 * Evaluation at the end of the game
	 * @return
	 */
	private int evaluateBoard(){
		if (gameBoard.blueCap > gameBoard.redCap)
			return 1;
		return -1;
	}

	/**
	 * Ignore this
	 * @return
	 */
	private int evaluateBoard2(){		
		ArrayList<ArrayList<Hexagon>> links = new ArrayList<ArrayList<Hexagon>>();
		ArrayList<Hexagon> tempLink = null;
		// Iterate through moves
		for (ArrayList<Integer> move : gameBoard.getMoves()){
			if (gameBoard.hexagonMap.get(move).size() < 2) continue;
			// Iterate through hexagons mapping to move
			for (Hexagon hex : gameBoard.hexagonMap.get(move)){
				// Iterate through links
				linkloop : for (ArrayList<Hexagon> link : links){
					// Iterate though hexagon in links
					for (Hexagon hex2 : link){
						if (hex.equals(hex2)){
							tempLink = link;
							break linkloop;
						}
					}
				}
			}
			if (tempLink != null){
				tempLink.removeAll(gameBoard.hexagonMap.get(move));
				tempLink.addAll(gameBoard.hexagonMap.get(move));
				tempLink = null;
			}else{
				links.add(new ArrayList<Hexagon>(gameBoard.hexagonMap.get(move)));
			}

		}
		Collections.sort(links, new Comparator<ArrayList>(){
		    public int compare(ArrayList a1, ArrayList a2) {
		        return a1.size() - a2.size(); // assumes you want biggest to smallest
		    }
		});
		boolean add = false;
		int score = 0;
		for (ArrayList<Hexagon> link : links){
			if (add) score += link.size();
			else score -= link.size();
			add = !add;
		}
		return score;
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
		if (gameBoard.checkCapture(m)){
			this.gameBoard.update(m);
			return 1;
		}
		//return 1 if move is valid and a hexagon is captured by move m
		this.gameBoard.update(m);
		return 0;
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
