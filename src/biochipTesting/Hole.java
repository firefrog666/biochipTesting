package biochipTesting;

public class Hole extends Edge{
	public Hole(){
		super();
		this.on  = true;
	}
	
	public Hole(int i){
		this.number = i;
		SA1 = false;
		SA0 = false;
	}
	
	public void turnOn(){
		if(this.SA0)
			this.on = false;
		else
			this.on = true;
	}
	
	public void turnOff(){
		if(this.SA0)
			this.on = false;
		else
			this.on = true;
	}
	
	public void setSA1(){
		System.out.println("i am a hole. I cannot SA1");
	}
}
