package aiproj.hexifence;

import java.util.ArrayList;
import java.util.HashMap;

public class MoveMapper {
	HashMap<String, Integer> mapper;
	
	MoveMapper(int n){
		mapper = new HashMap<String, Integer>();
		int count = 0;
		if (n==2){
			try{
				GameBoard g = new GameBoard(2);
				for (int i = 0; i < g.gameBoard.length; i++){
					for (int j = 0; j < g.gameBoard[i].length; j++){
						if (g.gameBoard[i][j] == '+'){
							String str = new String(i + " " + j);
							mapper.put(str, ++count);
						}
					}
				}
			}
			catch(Exception e){
				e.printStackTrace(System.out);
				System.exit(-1);
			}
			
		}
		else if (n==3){
			try{
				GameBoard g = new GameBoard(3);
				for (int i = 0; i < g.gameBoard.length; i++){
					for(int j = 0; j < g.gameBoard[i].length; j++){
						if (g.gameBoard[i][j] == '+'){
							String str = new String(i + " " + j);
							mapper.put(str, ++count);
						}
					}
				}
			}
			catch(Exception e){
				e.printStackTrace(System.out);
				System.exit(-1);
			}
		}
	}
	
	public int getLineNumber(Move m){
		String str = new String(m.Row + " " + m.Col);
		return mapper.get(str);
	}
	
	public int[] convertToLines(ArrayList<Move> moveSet){
		int[] lines = new int[mapper.values().size()];
		int count = 0;
		for(Move m : moveSet){
			lines[count++] = getLineNumber(m);
		}
		
		return lines;
	}
	
	
	
}
