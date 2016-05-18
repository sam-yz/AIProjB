package aiproj.hexifence;
import aiproj.hexifence.Move;

public interface MoveStrategy {
	public Move bestMove(Board board, int p, int depth);
}
