package aiproj.hexifence;

import java.util.ArrayList;
import java.util.Arrays;

public class Tester {
	
	public static GameBoard gameBoard;
	
	public static void main(String[] args){
		try {
			gameBoard = new GameBoard(2);
			gameBoard.update(new Move(4, 3, 1));
			gameBoard.update(new Move(2, 2, 2));
			gameBoard.update(new Move(2, 3, 1));
			gameBoard.update(new Move(3, 4, 2));
			gameBoard.update(new Move(4, 5, 1));
			gameBoard.update(new Move(5, 6, 2));
			gameBoard.update(new Move(6, 6, 1));
			gameBoard.update(new Move(6, 5, 2));
			gameBoard.update(new Move(6, 4, 1));
			gameBoard.update(new Move(6, 3, 2));
			gameBoard.update(new Move(5, 2, 1));
			gameBoard.update(new Move(4, 1, 2));
			gameBoard.update(new Move(2, 0, 1));
			gameBoard.update(new Move(3, 0, 2));
			gameBoard.update(new Move(2, 1, 1));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameBoard.printBoard(System.out);
		System.out.println(gameBoard.numberOfChains());
		gameBoard.remove(new ArrayList<Integer>(Arrays.asList(2, 3)));
		gameBoard.printBoard(System.out);
		System.out.println(gameBoard.numberOfChains());
	}
}
