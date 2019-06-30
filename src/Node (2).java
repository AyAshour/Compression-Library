
public class Node {

	 int symbol;
	String weight;
	 int number;
	 int count;
	
	 Node left;
	Node right;
	Node parent;
	
	String getweight(){
		return this.weight;
	}
	
	public Node(int s,String w,int n, int c, Node l, Node r, Node p)
	{
		symbol =s; //char
		weight=w;	//node code
		number=n;	//number of node (root is 100)
		count=c;	//number of children nodes (symbols)
		left=l;	
		right=r;
		parent=p;
				
	}
	public Node()
	{
		symbol =0;
		weight="";
		number=0;
		count=0;
		left=null;
		right=null;
		parent=null;
	}
	
	void updateweight(Node c)
    {
		if(c==null){
			return;
		}
		
		if(c==c.parent.left )
		{
			c.weight=c.parent.weight+"0";
		}
		if(c==c.parent.right )
		{
			c.weight=c.parent.weight+"1";
		}
		updateweight(c.left);
		updateweight(c.right);
    }
	Node search(Node n,String code)
	{
		if(n!=null){
		if(n.weight==code ){
			return n;
		}
		if(search(n.left,code)==null)
		{
			return	search(n.right,code);
		}
		return search(n.left,code);
		}
		return null;
	}
	Node searchChar(Node n,int ch)
	{
		if(n!=null){
		if(n.symbol==ch ){
			return n;
		}
		if(searchChar(n.left,ch)==null)
		{
			return	searchChar(n.right,ch);
		}
		return searchChar(n.left,ch);

		}
		return null;
	}
	
}
