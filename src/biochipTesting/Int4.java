package biochipTesting;

public class Int4 {
	int x;
	int y;
	int s;
	int t;
	public Int4(){
		x = 0;
		y = 0;
		s = 0;
		t = 0;
	}
	
	public Int4(int a, int b, int c, int d){
		x = a;
		y = b;
		s = c;
		t = d;
	}
	
	public Int4(Int4 b){
		x = b.x;
		y = b.y;
		s = b.s;
		t = b.t;
	}
	
	public Int4(Int2 a, Int2 b){
		x = a.x;
		y = a.y;
		s = b.x;
		t = b.y;
	}
	
	public void set(int a,int b, int c, int d){
		x = a;
		y = b;
		s = c;
		t = d;
	}
	
	public void set(Int4 b){
		set(b.x,b.y,b.s,b.t);
	}

	public boolean isVertical(){
		assert(!(x == s && y ==t ));
		assert(x == s || y == t);
		boolean en = false;
		
		if(y == t)
			en = true;
		
		return en;
		
	}
	
	public boolean isHorizontal(){
		assert(!(x == s && y ==t ));
		assert(x == s || y == t);
		boolean en = false;
		
		if(x == s)
			en = true;
		
		return en;
		
	}
}

