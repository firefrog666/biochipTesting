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
		coord = new Int4();
		coord.x = e.coord.x;
		coord.y = e.coord.y;
		coord.s = e.coord.s;
		coord.t = e.coord.t;
		ifHorizantol();
	}
	
	public Edge(){
		super();
		//coordinate = new Int4();
		ifHorizantol();
	}
	
	public Edge(int a,int b, int c, int d){
		coord = new Int4();
		coord.x = a;
		coord.y = b;
		coord.s = c;
		coord.t = d;
		ifHorizantol();
	}
	
	public Edge(Node a, Node b){
		assert(!(a.coord.x == b.coord.x && a.coord.y == b.coord.y) );
		assert(a.coord.x == b.coord.x || a.coord.y == b.coord.y);
		Node temp;
		if(a.coord.x > b.coord.x || a.coord.y > b.coord.y){
			temp = a;
			a = b;
			b = temp;
		}
		coord = new Int4();
		coord.x = a.coord.x;
		coord.y = a.coord.y;
		coord.s = b.coord.x;
		coord.t = b.coord.y;
			
		ifHorizantol();
		
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
	
	private void ifHorizantol(){
		if(coord.x != coord.s)
			isHorizontal = false;
		if(coord.y != coord.t)
			isHorizontal = true;
	}
	
	
	public int hashValue(){
		int hash = coord.x * 1000000 + coord.y * 10000 + coord.s * 100 + coord.t;
		return hash;
	}
	

}
