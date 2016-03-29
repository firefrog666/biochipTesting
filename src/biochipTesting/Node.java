package biochipTesting;

import java.util.ArrayList;

public class Node {
	int number;
	protected Int4 coord;
	//protected Edge[] adjEdges;
	private Node[] adjNodes;
	private ArrayList<Node> adjNodesList;
	
	public Node(Node node){
		this.coord = node.coord;
		this.number = node.number;
		this.adjNodesList = new ArrayList<Node>();
	}
	
	public Node(){
		this.number = 0;
		adjNodesList = new ArrayList<Node>();
		coord = new Int4();
	}
	
	public Node(int i)
	{
		adjNodesList = new ArrayList<Node>();
		coord = new Int4();
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
	public ArrayList<Node> getAdjNodes() {
		return adjNodesList;
		
	}
	
	public void setCoordinate(int x, int y){
		coord.x = x;
		coord.y = y;
	}
	
	public Int4 getCoordinate(){
		return coord;
	}
	public int hashValue(){
		int hash = coord.x * 100+ coord.y ;
		return hash;
	}
}
