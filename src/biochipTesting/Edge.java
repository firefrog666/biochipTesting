package biochipTesting;

public class Edge extends Node{
	boolean on;
	boolean SA1 = false;
	boolean SA0 = false;
	public boolean isHorizontal;
	int weight;
	//public Int4 coordinate;
	
	public Edge(){
		super();
		//coordinate = new Int4();
	}
	
	
	public Edge(int i){
		this.number = i;
		SA1 = false;
		SA0 = false;
		weight = 0;
		coordinate = new Int4();
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
		coordinate.x = x;
		coordinate.y = y;
		coordinate.s = s;
		coordinate.t = t;
	}
	
	public int hashValue(){
		int hash = coordinate.x * 1000000 + coordinate.y * 10000 + coordinate.s * 100 + coordinate.t;
		return hash;
	}
	

}
