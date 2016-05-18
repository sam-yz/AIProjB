package aiproj.hexifence;
import java.util.ArrayList;

public class Hex {
	private final int x;
	private final int y;
	private ArrayList<Edgy> availableEdges;
	private String capturedBy;
	
	public Hex(int x, int y) {
		this.x = x;
		this.y = y;
		this.availableEdges = generateEdges();
		this.capturedBy = "-";
	}

	public ArrayList<Edgy> getAvailableEdges() {
		return availableEdges;
	}
	public int numberOfAvailableEdges() {
		return availableEdges.size();
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public String getCapturedBy() {
		return capturedBy;
	}

	public void setCapturedBy(String capturedBy) {
		this.capturedBy = capturedBy;
	}

	public void setAvailableEdges(ArrayList<Edgy> availableEdges) {
		this.availableEdges = availableEdges;
	}

	private ArrayList<Edgy> generateEdges() {
		ArrayList<Edgy> availableEdges = new ArrayList<Edgy>();
		Edgy edgeA = new Edgy(2*x, 2*y, this);
		availableEdges.add(edgeA);
		Edgy edgeB = new Edgy(2*x, 2*y+1, this);
		availableEdges.add(edgeB);
		Edgy edgeC = new Edgy(2*x+1, 2*y, this);
		availableEdges.add(edgeC);
		Edgy edgeD = new Edgy(2*x+1, 2*y+2, this);
		availableEdges.add(edgeD);
		Edgy edgeE = new Edgy(2*x+2, 2*y+1, this);
		availableEdges.add(edgeE);
		Edgy edgeF = new Edgy(2*x+2, 2*y+2, this);
		availableEdges.add(edgeF);
		
		return availableEdges;
	}
	@Override
	public String toString() {
		return "Hexagon [x=" + x + ", y=" + y + "]";
	}
}
