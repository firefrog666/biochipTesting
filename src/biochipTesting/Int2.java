package biochipTesting;

public class Int2 {
	int x,y;
	
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
}
