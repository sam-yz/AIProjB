package aiproj.hexifence;

import java.util.ArrayList;

public class Edge {

	ArrayList<Integer> pos;
	Hexagon endNode;
	
	Edge(ArrayList<Integer> pos, Hexagon u){
		this.pos = pos;
		this.endNode = u;
	}
	
	
}
