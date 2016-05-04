package aiproj.hexifence;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		GameBoard gameBoard = new GameBoard(3);
		gameBoard.gameBoard = new char[][]{
			 {'B', 'B', '+', '+', '+', '+', '-', '-', '-', '-', '-'},
			 {'+', '-', '+', '-', '+', '-', '+', '-', '-', '-', '-'},
			 {'+', '+', 'B', 'R', '+', 'B', '+', '+', '-', '-', '-'},
			 {'+', '-', 'R', '-', 'B', '-', '+', '-', '+', '-', '-'},
			 {'R', 'B', 'R', 'B', 'B', 'B', 'R', 'R', '+', 'R', '-'},
			 {'+', '-', 'B', '-', 'B', '-', 'B', '-', 'B', '-', '+'},
			 {'-', 'B', '+', 'R', 'B', '+', 'R', 'B', 'B', '+', '+'},
			 {'-', '-', 'R', '-', 'R', '-', 'B', '-', '+', '-', '+'},
			 {'-', '-', '-', '+', '+', 'B', 'R', '+', '+', '+', '+'},
			 {'-', '-', '-', '-', 'R', '-', '+', '-', '+', '-', 'R'},
			 {'-', '-', '-', '-', '-', '+', '+', 'R', 'R', '+', '+'},
			};
		System.out.println(gameBoard.checkValid(new Move(6,5,Piece.BLUE)));
		gameBoard.update(new Move(6,5,Piece.BLUE), Piece.BLUE);
		gameBoard.printBoard(System.out);
		System.out.println(gameBoard.checkCapture(new Move(6,5,Piece.BLUE)));
	}

}
