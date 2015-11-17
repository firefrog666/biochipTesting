package biochipTesting;

import java.util.ArrayList;

public class Node {
	int number;
	protected Int4 coordinate;
	//protected Edge[] adjEdges;
	private Node[] adjNodes;
	protected ArrayList<Node> adjNodesList;
	
	public Node(){
		this.number = 0;
		adjNodesList = new ArrayList<Node>();
		coordinate = new Int4();
	}
	
	public Node(int i)
	{
		adjNodesList = new ArrayList<Node>();
		coordinate = new Int4();
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
		//adjNodes = adjNodesList.toArray(new Node[adjNodesList.size()]);
	}
	public Node[] getAdjNodes() {
		return adjNodesList.toArray(new Node[adjNodesList.size()]);
		
	}
	
	public void setCoordinate(int x, int y){
		coordinate.x = x;
		coordinate.y = y;
	}
	
	public Int4 getCoordinate(){
		return coordinate;
	}
	public int hashValue(){
		int hash = coordinate.x * 100+ coordinate.y ;
		return hash;
	}
}
