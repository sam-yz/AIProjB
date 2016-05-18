package aiproj.hexifence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GeneratorTester {

	public static void main(String[] args) throws IOException {
		FileWriter f = new FileWriter("game1.txt", true);
		BufferedWriter out = null;
		out = new BufferedWriter(f);
		MoveMapper mapper = new MoveMapper(2);
		
		GameGenerator newGen = new GameGenerator();
		
		int winTally = 0;
		
		int gamesToPlay = 100;
		while (gamesToPlay > 0){
			
			ArrayList<Move> m = new ArrayList<Move>();
			//run game
			char winner = newGen.runGame(m, 2);
			//get equiv rotations of game
			
			int[] line = mapper.convertToLines(m);
			
			//write to file
			for (int a: line){
				Integer j = new Integer(a);
				out.write(j.toString() + ",");
			}
			
			if (winner == 'T'){
				winTally++;
			}
			
			out.write(winner + "\n");
			gamesToPlay--;
			System.out.println("Running win percentage: " + winTally/(float)(100 - gamesToPlay) + " " + (100 - gamesToPlay));
		}
		out.close();
	}
	
}
