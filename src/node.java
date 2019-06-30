import java.util.ArrayList;

public class node {
	private Character ch;
	private Double lower;
	private Double upper;
	private int symbol;
	
	private int number;
	private int count;



	private int avg = 0;
	private ArrayList<Integer> pxlsList = new ArrayList<Integer>();

	private node left;
	private node right;
	private node parent;
	private String weight;
	node(Character c, Double l, Double u)
	{
		ch=c;
		lower=l;
		upper=u;
	}
	public node()
	{
		symbol =0;
		weight="";
		number=0;
		count=0;
		left=null;
		right=null;
		parent=null;
		ch=null;
		lower=null;
		upper=null;
	}
	public int getAvg() {
		return avg;
	}

	public ArrayList<Integer> getPxlsList() {
		return pxlsList;
	}

	public void setAvg(int avg) {
		this.avg = avg;
	}

	public void setPxlsList(ArrayList<Integer> pxlsList) {
		this.pxlsList = pxlsList;
	}
	public Character getCh() {
		return ch;
	}
	public void setCh(Character ch) {
		this.ch = ch;
	}
	public Double getLower() {
		return lower;
	}
	public void setLower(Double lower) {
		this.lower = lower;
	}
	public Double getUpper() {
		return upper;
	}
	public void setUpper(Double upper) {
		this.upper = upper;
	}
	public int getSymbol() {
		return symbol;
	}
	public void setSymbol(int symbol) {
		this.symbol = symbol;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public node getLeft() {
		return left;
	}
	public void setLeft(node left) {
		this.left = left;
	}
	public node getRight() {
		return right;
	}
	public void setRight(node right) {
		this.right = right;
	}
	public node getParent() {
		return parent;
	}
	public void setParent(node parent) {
		this.parent = parent;
	}
	public node(int s,String w,int n, int c, node l, node r, node p)
	{
		symbol =s; //char
		weight=w;	//node code
		number=n;	//number of node (root is 100)
		count=c;	//number of children nodes (symbols)
		left=l;	
		right=r;
		parent=p;
				
	}
	
	
	void updateweight(node c)
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
	node search(node n,String code)
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
	node searchChar(node n,int ch)
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
