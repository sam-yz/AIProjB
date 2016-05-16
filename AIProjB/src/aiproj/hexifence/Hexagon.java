package aiproj.hexifence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Hexagon{
	
	ArrayList<Edge> edges;
	ArrayList<Integer> singletonEdges;
	// Not needed, will add if necessary
	int x;
	int y;
	int remainingEdges;
	int capturedBy = -1;
	boolean visited;
	
	public Hexagon(int x, int y){
		this.x = x;
		this.y = y;
		this.remainingEdges = 6;
		this.visited = false;
		this.edges = new ArrayList<Edge>();
		
	}
	
	public void removeEdge(ArrayList<Integer> edge){
		Edge removeEdge = null;

		for (Edge e : edges){
			if (e.pos.equals(edge)){
				removeEdge = e;
				break;
			}
		}
		
		if (!edges.remove(removeEdge)){
//			singletonEdges.remove(removeEdge);
		}
	}
	
	/**
	 * Method to encapsulate decreasing edge count and
	 * removing an edge from the graph
	 * @param edge
	 */
	public void updateEdge(ArrayList<Integer> edge){
		this.remainingEdges--;
		removeEdge(edge);
		
	}
	
	/**
	 * Method generates graph connections for this hexagon. Any edge that
	 * connects two adjacent hexagons is considered an undirected edge in a graph.
	 * The edges that are only related to one hexagon are not included in the graph.
	 * @param hexagonMap A map of edges to hexagons. Used to generate the graph.
	 */
	public void generateGraphConnections(HashMap<ArrayList<Integer>,ArrayList<Hexagon>> hexagonMap){

		int[][] edgeVals = {{2*x, 2*y+1}, {2*x, 2*y},
						  {2*x+1, 2*y+2}, {2*x+1, 2*y},
						  {(x*2)+2, (y*2)+1}, {(x*2)+2, (y*2)+2}
						 };
		
		for (int[] coord : edgeVals){
//			int count = 0;
			ArrayList<Integer> edge;
			edge = new ArrayList<Integer>(Arrays.asList(coord[0], coord[1]));
			
//			System.out.printf("Num hexes for current edge %d, %d: %d\n", coord[0], coord[1], hexagonMap.get(edge).size());
			
			for (Hexagon h : hexagonMap.get(edge)){
//				count++;
//				System.out.printf("This hex: %d, %d | h: %d, %d | edge: %d, %d| count: %d\n", this.x, this.y, h.x, h.y, coord[0], coord[1], count);
				if (h.x != this.x || h.y != this.y){
//					System.out.println("\t" + count);
					Edge newEdge = new Edge(edge, h);
					edges.add(newEdge);
				}
			}
		}
//		System.out.println("Size: " + edges.size() + "\n");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hexagon other = (Hexagon) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	
	
	
	
	
}






