import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;




public class AdaptiveHuffman { 

	static int num=100;
	static Node root=new Node(-1,"",num--,0,null,null,null); ;
	static ArrayList<Node> leaves = new ArrayList<Node>();
	public static Map<String, String> dict = new HashMap<String , String>();
	public static Map<String, Integer> dict2 = new HashMap<String , Integer>();
	static Vector<String> output=new Vector<String>();
	static Vector<String> check=new Vector<String>();//contains letters without repetition
	public AdaptiveHuffman() {
	}
	public static String toBinary(int a, int bits) {
	    if (--bits > 0)
	        return toBinary(a>>1, bits)+((a&0x1)==0?"0":"1");
	    else 
	        return (a&0x1)==0?"0":"1";
	  }
	public static void buildtable(){
		int dictSize=123;
		
		for(int i=97; i<dictSize; i++)
		{
			dict.put(""+(char) i, toBinary((i-97), 5));
			dict2.put(toBinary((i-97), 5),  i);

		}
	}
	public static void compress(String s) throws IOException
	{
		buildtable();
		//to compress first character only 
		Character ch=s.charAt(0);
		root.right= new Node((int)ch,"1",num--,1,null,null,root);
		root.left=new Node(-1,"0",num--,0,null,null,root);
		String binaryString =dict.get(ch+"");
		output.add(binaryString);
		check.add(ch+"");
		leaves.add(root.right);
		//end first character
		for(int j=1; j<s.length();j++)
		{
			ch=s.charAt(j);
			//Node check=table.get(arg0)
			if(!check.contains(ch+"")) //first occurrence?
			{//if first occurrence
				
				check.add(ch + "");//add new char to vector
				Node temp_nyt= root;
				while(temp_nyt.symbol!=-1 || temp_nyt.left!=null)//stop when nyt->left is null
				{
					if(temp_nyt.left.symbol==-1)
						temp_nyt=temp_nyt.left;
					else if(temp_nyt.right.symbol==-1)
						temp_nyt= temp_nyt.right;
				}
				//split nyt
				Node r=new Node((int)ch,temp_nyt.weight+"1",num--,1,null,null,temp_nyt);
				temp_nyt.right=r;
				Node l= new Node(-1,temp_nyt.weight+"0",num--,0,null,null,temp_nyt);
				temp_nyt.left=l;
				//end of split and symbol count
				//add character and its short code to table ,then output nyt code and symbol short code from table 
				binaryString = dict.get(ch+"");
				output.add(temp_nyt.weight);
				output.add(binaryString);
				//add to leaves list
				leaves.add(r);
				checkSwap(temp_nyt.right);
				while(temp_nyt.parent!=null)
				{
					temp_nyt.count++;
					checkSwap(temp_nyt);
					temp_nyt=temp_nyt.parent;
				}//end of increment parent until root counter
			}
			//not first occurrence(repeated letter)
			else 
			{
				Node temp = root;
				temp=temp.searchChar(root,ch);
				Node itr=temp;
				output.add(temp.weight);
				//increment counter of parent until reach root 
				
				while(itr.parent!=null)
				{
					itr.count++;
					checkSwap(itr);
					itr=itr.parent;
				}//end of increment parent until root counter
				
			}
			
		}

		printtree(root);
		System.out.println(output);
		FileOutputStream f = new FileOutputStream(new File("compressed.txt"));
		ObjectOutputStream o = new ObjectOutputStream(f);
		o.writeObject(output);
		o.close();
		f.close();
		
	}
	public static void checkSwap(Node lower)
	{
		Node leaf=new Node();
		Node higher= lower;
		for(int i=0; i<leaves.size(); i++)//compare my current node to every letter (leaves)
		{
				leaf=leaves.get(i);
				if(lower.count>leaf.count && lower.number<leaf.number)
				{
					if(leaf.number>higher.number)
					{
						higher= leaf;
					}
				}	
		}
		if(higher!=lower)
		{
			swap(lower,higher);
			lower.updateweight(lower);
			higher.updateweight(higher);
		}
	}
	public static void swap(Node l, Node h)
	{
		Node temp=new Node(l.symbol,l.weight,l.number,l.count,l.left,l.right,l.parent);
		l.count=h.count;
		l.symbol=h.symbol;
		l.right=h.right;
		l.left=h.left;
		l.parent=h.parent;
		
		if(l.right!=null)
			l.right.parent=l;
		if(l.left!=null)
			l.left.parent=l;
		
		h.count=temp.count;
		h.symbol=temp.symbol;
		h.right=temp.right;
		h.left=temp.left;
		h.parent=temp.parent;
		
		if(h.right!=null)
			h.right.parent=h;
		if(h.left!=null)
			h.left.parent=h;
	
	}
	static void decompress(Vector<String> data) throws IOException
	{
		num=100;
		Node root2 =new Node(-1,"",num--,0,null,null,null);
		String code= data.get(0)+"";
		int ch=dict2.get(code);
		String dec=(char)ch+"";
		Node tmp=root2;
		root2.right= new Node(ch,"1",num--,1,null,null,root2);
		root2.left=new Node(-1,"0",num--,0,null,null,root2);
		tmp=tmp.left;
		for(int i=1; i<data.size(); i++)
		{
			code=data.get(i)+"";
			if(code.equals(tmp.weight))
			{
				code= data.get(++i)+"";
				ch=dict2.get(code);
				dec+=(char)ch;
				tmp.right= new Node(ch,tmp.weight+"1",num--,1,null,null,tmp);
				tmp.left=new Node(-1,tmp.weight+"0",num--,0,null,null,tmp);
				checkSwap(tmp.right);
				Node itr=tmp;
				while(itr.parent!=null)
				{
					itr.count++;//increment elparent w check elswap
					checkSwap(itr);
					itr=itr.parent;
				}//end of increment parent until root counter
				
				tmp=tmp.left;
			}
			else
			{
				System.out.println(dec);
				code = data.get(i)+"";
				Node cur=root2;
				cur=cur.search(root2,code);
				dec+=(char)cur.symbol;	
				while(cur.parent!=null)
				{
					cur.count++;//increment elparent w check elswap
					checkSwap(cur);
					cur=cur.parent;
				}//end of increment parent until root counter
			}
		}
		FileWriter fw = new FileWriter("decompressed.txt");
		fw.write(dec);
		fw.close();
		
	}
		
		public static void printtree(Node root)
	    {
	    if(root == null)
	      return;
	    printtree(root.left);
	    System.out.println((char)root.symbol+" |code:"+root.weight+" |number:"+root.number+" |count:"+root.count+" |left:"+root.left+" |right:"+root.right+" |parent:"+root.parent);
	    printtree(root.right);
	    }
		public static  String readFile(String path, Charset encoding) throws IOException 
		{
		    byte[] encoded = Files.readAllBytes(Paths.get(path));
		    return new String(encoded, encoding);
		}

	public static void main(String[] args) throws ClassNotFoundException
	{
		
		try {
			String content = readFile("data.txt", StandardCharsets.UTF_8);
			compress(content);
			FileInputStream fi = new FileInputStream(new File("compressed.txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			Vector<String> compressedData=(Vector<String>) oi.readObject();
			System.out.println("reading vector from file:"+compressedData);
			decompress(compressedData);
			fi.close();
			oi.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
