package biochipTesting;
//this is a test line

import java.util.ArrayList;

public class Node {
	int number;
	protected Int4 coordinate;
	//protected Edge[] adjEdges;
	protected Node[] adjNodes;
	protected ArrayList<Node> adjNodesList;
	
	public Node(){
		this.number = 0;
		adjNodesList = new ArrayList<Node>();
		coordinate = new Int4();
	}
	
	public Node(int i)
	{
		adjNodesList = new ArrayList<Node>();
		this.number = i;
	}
	
	public void setNumber(int i) {
		this.number = i;
	}
	
	public void setAdjNodes(Node... nodes) {
		adjNodes = new Node[nodes.length];
		for(int i=0; i < nodes.length; i++){
			adjNodes[i] = nodes[i];			
		}
		
	}
	
	
	public void setAdjNodes(Node node) {
		adjNodesList.add(node);
		adjNodes = adjNodesList.toArray(new Node[adjNodesList.size()]);
	}
	public Node[] getAdjNodes() {
		return this.adjNodes;
		
	}
	
	public void setCoordinate(int x, int y){
		coordinate.x = x;
		coordinate.y = y;
	}
	
	public Int4 getCoordinate(){
		return coordinate;
	}
}
