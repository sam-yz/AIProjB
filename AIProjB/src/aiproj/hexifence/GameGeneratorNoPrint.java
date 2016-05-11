package aiproj.hexifence;

import java.util.ArrayList;

public class GameGeneratorNoPrint {
	
	private static Player P1;
	private static Player P2;
	private static Move lastPlayedMove;
	
	public char runGame(ArrayList<Move> moveSet) {
		
		lastPlayedMove = new Move();
		int NumberofMoves = 0;
		int dimension = 2;
		int boardEmptyPieces=(dimension)*(9*dimension-3);
		P1 = new MinimaxAgent();
		P2 = new BasicAgent();
		
		P1.init(2, Piece.BLUE);
		P2.init(2, Piece.RED);
		
		int opponentResult=0;
		int turn=1;
		

        NumberofMoves++;
        
        lastPlayedMove=P1.makeMove();
        moveSet.add(lastPlayedMove);
       
		boardEmptyPieces--;
		turn =2;

		while(boardEmptyPieces > 0 && P1.getWinner() == 0 && P2.getWinner() ==0)
		{
		if (turn == 2){			

			opponentResult = P2.opponentMove(lastPlayedMove);
			if(opponentResult<0)
			{
				System.exit(1);
			}			
			else if(P2.getWinner()==0  && P1.getWinner()==0 && boardEmptyPieces>0){
				NumberofMoves++;
				if (opponentResult>0){
					
					lastPlayedMove = P1.makeMove();
					moveSet.add(lastPlayedMove);
					turn = 2;
				}	
				else{	
					lastPlayedMove = P2.makeMove();
					moveSet.add(lastPlayedMove);
					turn=1;
				}
				boardEmptyPieces--;
			}
		}
		else{	
			
			opponentResult = P1.opponentMove(lastPlayedMove);
			if(opponentResult<0)
			{
				System.out.println("Exception: Player 1 rejected the move of player 2.");
				P2.printBoard(System.out);
				P1.printBoard(System.out);
				System.exit(1);
			}
			else if(P2.getWinner()==0  && P1.getWinner()==0 && boardEmptyPieces>0){
                                NumberofMoves++;
                                if (opponentResult>0){
                                        lastPlayedMove = P2.makeMove();
                                        moveSet.add(lastPlayedMove);                                       
                                        turn = 1;                                        
                                }
				else{
                                        lastPlayedMove = P1.makeMove();
                                        moveSet.add(lastPlayedMove);
                                        turn=2;                                        
                                }
                                boardEmptyPieces--;
			}	
		}
			
		}
		
		
		if(turn == 2){
		    opponentResult = P2.opponentMove(lastPlayedMove);
		    if(opponentResult < 0) {
			System.out.println("Exception: Player 2 rejected the move of player 1.");
			P1.printBoard(System.out);
			P2.printBoard(System.out);
			System.exit(1);
		    }
		} else {
		    opponentResult = P1.opponentMove(lastPlayedMove);
		    if(opponentResult < 0) {
			System.out.println("Exception: Player 1 rejected the move of player 2.");
			P2.printBoard(System.out);
			P1.printBoard(System.out);
			System.exit(1);
		    }
		}	
		return P1.getWinner() == Piece.BLUE ? 'T' : 'F';
	}

}
