package aiproj.hexifence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BenchMark {
	
	public static void main(String[] args) throws Exception {
		GameGeneratorNoPrint gg = new GameGeneratorNoPrint();
		double blueWin = 0;
		double totalGames = 50;
		
		// training games
		for (int i = 1; i <= totalGames; i++) {
			ArrayList<Move> m = new ArrayList<Move>();
			//run game
			long startTime = System.nanoTime();
			char winner = gg.runGame(m);
			long endTime = System.nanoTime();
			if (winner == 'T') blueWin += 1;
			if (winner == 'X') totalGames -= 1;

			double duration = (endTime - startTime)/1000000000.0;
			System.out.println(i + ", duration: " + duration + ", score: " + blueWin/i);
		}
		
		System.out.println("End score: " + blueWin/totalGames);
	}
	
}
