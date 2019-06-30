 
@SuppressWarnings("rawtypes")
public class cell implements Comparable{

	String s;
	Double prob;
	cell left;
	cell right;
	cell parent;
	String code="";
	cell(){
		code="";
	}
	cell(String s,Double d,cell l,cell r,cell p){
		this.s=s;
		prob=d;
		left=l;
		right=r;
		parent=p;
	}
	String getStr(){
		return this.s;
	}
	String getCode(){
		return this.code;
	}
	Double getProb(){
		return this.prob;
	}
	void setParent(cell p)
	{
		parent=p;
	}
	void print(cell c){
		if(c==null)
			return;
		print(c.left);
		System.out.println("------");
		System.out.print(c.s+" ");
		System.out.print(c.prob+" ");
		
		if(c.right!=null && c.left!=null && c.parent!=null)
		{System.out.print(c.left.s+" ");
		System.out.print(c.right.s+" ");
		System.out.print(c.parent.s+" ");
		}
		System.out.println(c.code+"<<");
		System.out.println("------------");
		print(c.right);
		
	}
	void setCode(cell c){
		if(c==null){
			return;
		}
		if(c.s==c.parent.left.s )
		{
			c.code=c.parent.code+"1";
		}
		if(c.s==c.parent.right.s )
		{
			c.code=c.parent.code+"0";
		}
		setCode(c.left);
		setCode(c.right);
	}
	
	
	public int compareTo(Object obj) {
		cell other = (cell)obj;

        if(other.prob > prob)
        return -1;

	        else if(other.prob== prob)
        return 0;

        else return 1;
	}
}

