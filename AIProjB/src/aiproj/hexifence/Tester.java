package aiproj.hexifence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

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
			gameBoard.update(new Move(2, 4, 2));
			gameBoard.update(new Move(2, 5, 1));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameBoard.printBoard(System.out);
		
		System.out.println("num long chains: " + gameBoard.numberOfLongChains());
		System.out.println("Sizes: ");
		for (ArrayList<Hexagon> h : gameBoard.chainList){
			System.out.println(h.size());
		}
		
		for (ArrayList<Integer> move : gameBoard.getSmallestChainMoves()){
			System.out.printf("(%d, %d) \n", move.get(0), move.get(1));
		}
		
		
		gameBoard.remove(new ArrayList<Integer>(Arrays.asList(1, 2)));
		
		gameBoard.printBoard(System.out);
		
		
		System.out.println("num long chains: " + gameBoard.numberOfLongChains());
		System.out.println("Sizes: ");
		for (ArrayList<Hexagon> h : gameBoard.chainList){
			System.out.println(h.size());
		}
		
		
	}
}
