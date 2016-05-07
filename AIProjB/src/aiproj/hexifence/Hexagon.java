package aiproj.hexifence;

public class Hexagon{
	
	int remainingEdges;
	// Not needed, will add if necessary
	int x;
	int y;
	int capturedBy = -1;
	
	public Hexagon(int x, int y){
		this.x = x;
		this.y = y;
		this.remainingEdges = 6;
	}
}
