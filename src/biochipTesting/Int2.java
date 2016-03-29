package biochipTesting;

public class Int2 {
	int x,y;
	
	
	public Int2(){
		x = 0;
		y = 0;
	}
	public Int2(int i, int j){
		x = i;
		y = j;
	}
	
	 @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof Int2)
        {
            sameSame = (this.x == ((Int2) object).x)&&(this.y == ((Int2)object).y);
        }

        return sameSame;
    }
	 
	public boolean leftUnder(Int2 b){
		if(x < b.x || y < b.y)
			return true;
		else 
			return false;
	}
	
	//with a int2 point, see if its on a int4 segment
	public boolean pointOnSegment(Int4 segment){
		boolean en = false;
		
		if(x == segment.x && x == segment.s){
			if((segment.y <y && y < segment.t) ||(segment.y > y && y > segment.t) ){
				en = true;
			}
		}
		else if(y == segment.y && y == segment.t){
			if((segment.x <x && x < segment.s) ||(segment.x > x && x > segment.s) ){
				en = true;
			}
		}
		
		return en;
	}
	
	public boolean pointIsSegVertex(Int4 segment){
		boolean en = false;
		
		if(x == segment.x && y == segment.y )
			en = true;
		else if( x == segment.s && y == segment.t)
			en = true;
		return en;
	}
}
