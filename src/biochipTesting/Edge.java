package biochipTesting;

public class Edge extends Node{
	boolean on;
	boolean SA1 = false;
	boolean SA0 = false;
	public boolean isHorizontal;
	int weight;
	//public Int4 coordinate;
	
	public Edge(Edge e){
		on = e.on;
		SA1 = e.SA1;
		SA0 = e.SA0;
		isHorizontal = e.isHorizontal;
		coord = e.coord;
	}
	
	public Edge(){
		super();
		//coordinate = new Int4();
	}
	
	
	public Edge(int i){
		this.number = i;
		SA1 = false;
		SA0 = false;
		weight = 0;
		coord = new Int4();
		isHorizontal = true;
		
	}
	
	public void turnOn(){
		if (SA0)
			on = false;
		else
			on = true;
	}
	
	public void turnOff(){
		if (SA1)
			on = true;
		else
			on = false;
	}
	
	public void setSA1(){
		SA1 = true;
	}
	
	public void setSA0(){
		SA0 = true;
	}
	
	public void setCoordinate(int x, int y, int s, int t  ){
		coord.x = x;
		coord.y = y;
		coord.s = s;
		coord.t = t;
	}
	
	public int hashValue(){
		int hash = coord.x * 1000000 + coord.y * 10000 + coord.s * 100 + coord.t;
		return hash;
	}
	

}
