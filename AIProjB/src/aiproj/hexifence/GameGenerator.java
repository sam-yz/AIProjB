package aiproj.hexifence;

import java.util.ArrayList;

public class GameGenerator {
	
	private static Player P1;
	private static Player P2;
	private static Move lastPlayedMove;
	
	public char runGame(ArrayList<Move> moveSet, int n) {
		
		lastPlayedMove = new Move();
		int NumberofMoves = 0;

		int boardEmptyPieces=(n)*(9*n-3);
		System.out.println("Referee started !");
		P1 = new minMax3();
		P2 = new BasicAgent();
		
		P1.init(n, Piece.BLUE);
		P2.init(n, Piece.RED);
		
		int opponentResult=0;
		int turn=1;
		

        NumberofMoves++;
        
        lastPlayedMove=P1.makeMove();
        moveSet.add(lastPlayedMove);
        System.out.println("Placing to. "+lastPlayedMove.Row+":"+lastPlayedMove.Col+" by "+lastPlayedMove.P);
       
      P1.printBoard(System.out);
		boardEmptyPieces--;
		turn =2;

		while(boardEmptyPieces > 0 && P1.getWinner() == 0 && P2.getWinner() ==0)
		{
		if (turn == 2){			

			opponentResult = P2.opponentMove(lastPlayedMove);
			if(opponentResult<0)
			{
				System.out.println("Exception: Player 2 rejected the move of player 1.");
				P1.printBoard(System.out);
				P2.printBoard(System.out);
				System.exit(1);
			}			
			else if(P2.getWinner()==0  && P1.getWinner()==0 && boardEmptyPieces>0){
				NumberofMoves++;
				if (opponentResult>0){
					
					lastPlayedMove = P1.makeMove();
					moveSet.add(lastPlayedMove);
					System.out.println("Placing to. "+lastPlayedMove.Row+":"+lastPlayedMove.Col+" by "+lastPlayedMove.P);
					
					turn = 2;
					P1.printBoard(System.out);
				}	
				else{	
					lastPlayedMove = P2.makeMove();
					moveSet.add(lastPlayedMove);
					turn=1;
					System.out.println("Placing to. "+lastPlayedMove.Row+":"+lastPlayedMove.Col+" by "+lastPlayedMove.P);
					P2.printBoard(System.out);
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
                                        System.out.println("Placing to. "+lastPlayedMove.Row+":"+lastPlayedMove.Col+" by "+lastPlayedMove.P);
                                        turn = 1;
                                        P2.printBoard(System.out);
                                }
				else{
                                        lastPlayedMove = P1.makeMove();
                                        moveSet.add(lastPlayedMove);
                                        turn=2;
                                        System.out.println("Placing to. "+lastPlayedMove.Row+":"+lastPlayedMove.Col+" by "+lastPlayedMove.P);
                                        P1.printBoard(System.out);
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
//		
		System.out.println("--------------------------------------");
		System.out.println("P2 Board is :");
		P2.printBoard(System.out);
		System.out.println("P1 Board is :");
		P1.printBoard(System.out);
		System.out.println("--------------------------------------");


		System.out.println("--------------------------------------");
		System.out.println("Player one (BLUE) indicate winner as: "+ P1.getWinner());
		System.out.println("Player two (RED) indicate winner as: "+ P2.getWinner());
		System.out.println("Total Number of Moves Played in the Game: "+ NumberofMoves);
		System.out.println("Referee Finished !");
	
//		ArrayList<String> data = new ArrayList<String>();
//		MoveMapper mMap = new MoveMapper(n);
//		GameBoard gBoard = ((BasicAgent)P1).gameBoard;
//		if (P1.getWinner() == Piece.BLUE){
//			for (int j = 0; j < gBoard.movesThisGameM.size(); j++){
//				String str = "";
//				int lNum = mMap.getLineNumber(gBoard.movesThisGameM.get(j));
//				for (int i = 0; i < (n)*(9*n-3); i++){
//					if (i == lNum){
//						str += "1"
//					}
//				}
//				
//			}
//		}
		
		
		
//		minMax3 inst = (minMax3)P1;
//		long avg = 0;
//		for (long f : inst.times){
//			avg += f;
//			System.out.print(f + ", ");
//		}
//		System.out.println("\nAverage: " + avg/inst.times.size());
		
		return P1.getWinner() == Piece.BLUE ? 'T' : 'F';
	}

}
