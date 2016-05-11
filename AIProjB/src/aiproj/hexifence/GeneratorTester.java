package aiproj.hexifence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GeneratorTester {
	
	public static void main(String[] args) throws IOException {
		FileWriter f = new FileWriter("game.txt", true);
		BufferedWriter out = null;
		out = new BufferedWriter(f);
		MoveMapper mapper = new MoveMapper(2);
		
		
		GameGenerator newGen = new GameGenerator();
		int gamesToPlay = 1;
		while (gamesToPlay > 0){
			
			ArrayList<Move> m = new ArrayList<Move>();
			//run game
			char winner = newGen.runGame(m);
			//get equiv rotations of game
			
			int[] line = mapper.convertToLines(m);
			
			//write to file
			for (int a: line){
				Integer j = new Integer(a);
				out.write(j.toString() + ",");
			}
			out.write(winner + "\n");
			gamesToPlay--;
		}
		out.close();
	}
	
}
