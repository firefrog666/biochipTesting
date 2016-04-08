package biochipTesting;

public class Wall extends Edge {
	
	public Wall(){
		super();
		this.on  = false;
	}
	
	
	
	public Wall(int a, int b, int c, int d){
		super(a , b, c, d);
	}
	public Wall(int i){
		this.number = i;
		SA1 = false;
		SA0 = false;
	}
	
	public void turnOn(){
		if(this.SA1)
			this.on = true;
		else
			this.on = false;
	}
	
	public void turnOff(){
		if(this.SA1)
			this.on = true;
		else
			this.on = false;
	}
	
	public void setSA0(){
		System.out.println("i am a wall. I cannot SA0");
	}
	
	
}
