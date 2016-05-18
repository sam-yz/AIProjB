package aiproj.hexifence;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class minMax3 implements Player{
	ArrayList<Long> times;
	ArrayList<Move> moveSet;
	public GameBoard gameBoard;
	int pieceColor;
	int oppPieceColor;
	private static int MINIMAX_DEPTH = 10;
	
	@Override
	public int init(int n, int p) {
		try{
			gameBoard = new GameBoard(n);
			times = new ArrayList<Long>();
			moveSet = new ArrayList<Move>();
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
		Move move;
		
		if (gameBoard.totalMovesLeft > 23){
			// Use minimax
			
			long startTime = System.nanoTime();
			int[] moveDet = minimax(5, pieceColor, Integer.MIN_VALUE, Integer.MAX_VALUE, System.nanoTime());
			
			ArrayList<Integer> coords = new ArrayList<Integer>(Arrays.asList(moveDet[1], moveDet[2]));
			ArrayList<ArrayList<Integer>> noThreatMoves = gameBoard.safeMoves();
			
			if (!noThreatMoves.contains(coords) && noThreatMoves.size() > 0){
				coords = noThreatMoves.get((int)(Math.random()*noThreatMoves.size()));
			}
			
			move = new Move(coords.get(1), coords.get(0), pieceColor);
			gameBoard.update(move);
			long endTime = System.nanoTime();
			times.add((endTime - startTime)/1000000);
			System.out.println("MiniMax score: " + moveDet[0] + "time: " + ((endTime - startTime)/1000000));
			return move;
		}
		
		// When to start using minimax
		else if (gameBoard.totalMovesLeft > 10){
			// Use minimax
			
			long startTime = System.nanoTime();
			int[] moveDet = minimax(7, pieceColor, Integer.MIN_VALUE, Integer.MAX_VALUE, System.nanoTime());
			
			ArrayList<Integer> coords = new ArrayList<Integer>(Arrays.asList(moveDet[1], moveDet[2]));
			ArrayList<ArrayList<Integer>> noThreatMoves = gameBoard.safeMoves();
			
			if (!noThreatMoves.contains(coords) && noThreatMoves.size() > 0){
				coords = noThreatMoves.get((int)(Math.random()*noThreatMoves.size()));
			}
			else if (noThreatMoves.size() == 0){
				if (!gameBoard.getSmallestChainMoves().contains(coords)){
					coords = gameBoard.getSmallestChainMoves().get((int)(Math.random()*gameBoard.getSmallestChainMoves().size()));
				}
			}
			
			move = new Move(coords.get(1), coords.get(0), pieceColor);
			gameBoard.update(move);
			long endTime = System.nanoTime();
			times.add((endTime - startTime)/1000000);
			System.out.println("MiniMax score: " + moveDet[0] + "time: " + ((endTime - startTime)/1000000));
			return move;
		}
		
		// Use minimax
		
		long startTime = System.nanoTime();
		int[] moveDet = minimax1(10, pieceColor, Integer.MIN_VALUE, Integer.MAX_VALUE, startTime);
		long endTime = System.nanoTime();
		times.add((endTime - startTime)/1000000);
		
		
		System.out.println("MiniMax score: " + moveDet[0] + "time: " + ((endTime - startTime)/1000000));
		move = new Move(moveDet[2], moveDet[1], pieceColor);
		gameBoard.update(move);
		return move;
	}
	
	private int[] minimax(int depth, int currPieceColor, int alpha, int beta, long startTime) {
		// We are maximising, while opponent is minimising
		int currScore;
	    int bestRow = -1;
	    int bestCol = -1;
	    
	    long time = (System.nanoTime() - startTime)/1000000;
	    
	    // Gameover or depth reached, evaluate score
		if (gameBoard.totalMovesLeft == 0 || depth == 0 || time > 10000){
			currScore = evalBoard1000();
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
					if (captureHex){
						currScore = minimax(depth - 1, pieceColor, alpha, beta, startTime)[0];
					}
					else{
						currScore = minimax(depth - 1, oppPieceColor, alpha, beta, startTime)[0];
					}
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
					if (captureHex){
						currScore = minimax(depth - 1, oppPieceColor, alpha, beta, startTime)[0];
					}
					else{
						currScore = minimax(depth - 1, pieceColor, alpha, beta, startTime)[0];
					}
					
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

		return new int[]{(currPieceColor == pieceColor) ? alpha : beta, bestRow, bestCol};
	}
	
	private int[] minimax1(int depth, int currPieceColor, int alpha, int beta, long startTime) {
		// We are maximising, while opponent is minimising
		int currScore;
	    int bestRow = -1;
	    int bestCol = -1;
	    
	    long time = (System.nanoTime() - startTime)/1000000;
	    
	    // Gameover or depth reached, evaluate score
		if (gameBoard.totalMovesLeft == 0 || depth == 0 || time > 10000){
			currScore = evaluateBoard3();
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
					if (captureHex){
						currScore = minimax1(depth - 1, pieceColor, alpha, beta, startTime)[0];
					}
					else{
						currScore = minimax1(depth - 1, oppPieceColor, alpha, beta, startTime)[0];
					}
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
					if (captureHex){
						currScore = minimax1(depth - 1, oppPieceColor, alpha, beta, startTime)[0];
					}
					else{
						currScore = minimax1(depth - 1, pieceColor, alpha, beta, startTime)[0];
					}
					
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
					score -= 15;
					break;
				}
				case 2: {
					score += 3;
					break;
				}
				case 3: {
					score -= 1;
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
	
	private int evalBoard1000(){
		
		int score = 0;
		int numChains = gameBoard.numberOfLongChains();
		
		for (Hexagon hex : gameBoard.hexagonList){
			switch (hex.remainingEdges){
				case 0:{
					if (hex.capturedBy == pieceColor)score += 20;
					else score -= 20;
					break;
				}
				case 1:{
					score -= 15;
					break;
				}
				case 2: {
					score += 3;
					break;
				}
				case 3: {
					score -= 1;
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
		
		
		if (this.pieceColor == Piece.BLUE){
			if ((numChains % 2 == 0)){
				score = score + 7;//((4*gameBoard.N) - 1 - gameBoard.blueCap - gameBoard.redCap);
			}
			else if (numChains % 2 != 0){
				score = score - 7;//((4*gameBoard.N) - 1 - gameBoard.blueCap - gameBoard.redCap);
			}
		}
		else{
			if ((numChains % 2 == 0)){
				score = score - 7;//((4*gameBoard.N) - 1 - gameBoard.blueCap - gameBoard.redCap);
			}
			else if (numChains % 2 != 0){
				score = score + 7;//((4*gameBoard.N) - 1 - gameBoard.blueCap - gameBoard.redCap);
			}
		}
		
		
//		System.out.println("chain score: " + score);
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
		    public int compare(ArrayList o1, ArrayList o2) {
		        return o1.size() - o2.size();
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
