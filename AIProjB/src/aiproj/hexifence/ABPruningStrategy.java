package aiproj.hexifence;

import java.util.ArrayList;

import aiproj.hexifence.Move;
import aiproj.hexifence.Piece;

public class ABPruningStrategy implements MoveStrategy {

	public ABPruningStrategy() {

	}
	

	@Override
	public Move bestMove(Board board, int p, int depth) {
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		
		Node rootNode = new Node(null, board);
		int bestMoveEvaluationValue = recursiveBestMove(rootNode, alpha, beta, true, p, depth);
		Move bestMove = null;
		if (rootNode.getChildren() != null) {
			for (Node n : rootNode.getChildren()) {
				int evaluationValue = n.getValue();
				System.out.println("value: " + evaluationValue);
				System.out.println("best value: " + bestMoveEvaluationValue);
				
				if (evaluationValue == bestMoveEvaluationValue) {
					bestMove = n.getMove();
					break;
				} 
			}
		}
		if (rootNode.getChildren().size() == 1) {
			Board rootBoard = rootNode.getBoard();
			for (Edgy e : rootBoard.edgesInGame.values()) {
				Move m = new Move();
				m.P = p;
			    m.Row = e.getX();
				m.Col = e.getY();
				if (rootBoard.isValidMove(m)) {
					return m;
				}
			}
		}
		
		return bestMove;
	}
	public static int i = 0;
	public int recursiveBestMove(Node node, int alpha, int beta, boolean maximisingPlayer, int p, int depth) {
		
		// Generate all children (possible board states)
		if (depth != 0) {
			if (maximisingPlayer) {
				node.generateChildren(p);
			} else {
				if (p == 1) {
					node.generateChildren(Piece.RED);
				} else {
					node.generateChildren(Piece.BLUE);
				}
			}
//			System.out.println(node.getChildren().size());
		}
		
//		System.out.println("----------");
//		System.out.println(depth);
//		
//		
//		i++;
//		node.getBoard().printBoard(System.out);
		
		int bestValue;
		if (node.getChildren() == null) {
			bestValue = node.getBoard().calcEvaluationValue(p);
			
		} else if (maximisingPlayer) {
			// Given that we are at a maximizing node, we know that we are guaranteed to be able to get this state of the board
			// hence we are able to update alpha
			bestValue = alpha;
			for (Node n : node.getChildren()) {
				ArrayList<Hex> hexagonsCaptured = node.getBoard().checkNumberOfHexagonsCaptured(n.getMove());
				int childValue = 0;
//				System.out.println("size: " + hexagonsCaptured.size());
				if (hexagonsCaptured.size() > 1) {
					childValue = recursiveBestMove(n, bestValue, beta, true, p, depth - 1);
				} else {
					childValue = recursiveBestMove(n, bestValue, beta, false, p, depth - 1);
				}
				 
				// if we find a better board state which is equivalent to a higher evaluation value then update the value of the node
				// (note that this is not the same value as alpha, it is the value that the node currently is)
				bestValue = Math.max(bestValue, childValue);
				node.setValue(bestValue);
//				System.out.println("value: " + node.getValue());
				// To see if we can prune compare the value of the maximising node with the beta value which is also the guaranteed value
				// for the minimising player
				
				if (bestValue >= beta) {
//					System.out.println("Number of child nodes is: " + node.getChildren().size());
					break;
				}
			}
		} else {
			// Given that we are at a minimizing node, we know that we are guaranteed to be abel to get this state of the board
			// hence we update beta to this value
			bestValue = beta;
			for (Node n : node.getChildren()) {
				
				ArrayList<Hex> hexagonsCaptured = node.getBoard().checkNumberOfHexagonsCaptured(n.getMove());
				int childValue = 0;
				if (hexagonsCaptured.size() > 1) {
					childValue = recursiveBestMove(n, alpha, bestValue, true, p, depth - 1);
				} else {
					childValue = recursiveBestMove(n, alpha, bestValue, false, p, depth - 1);
				}
				
				// if we find a better board state which is equivalent to a lower evaluation value then update the value of the node
				// (note that this is not the same value as alpha, it is the value that the node currently is)
				bestValue = Math.min(bestValue, childValue);
				node.setValue(bestValue);
//				System.out.println("value: " + node.getValue());
				
				// To see if we can prune compare the value of the maximising node with the beta value which is also the guaranteed value
				// for the minimising player
				
				if (bestValue <= alpha) {
					break;
				}
			}
			
		}
		return bestValue;
	}

	public int calcUtilityValue(Board board) {
		return 1;
	}

}
