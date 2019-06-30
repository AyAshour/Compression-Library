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
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;


public class Standard_huffman {
	public static HashMap<String,String> table= new HashMap<String,String>();
	public static HashMap<String,String> fileTable= new HashMap<String,String>();
	public Vector<String> vec=new Vector<String>();
	public static ArrayList<cell> tree= new ArrayList<cell>();
	Vector<Double> prob=new Vector<Double>();
	Vector<String> str=new Vector<String>();
	
	@SuppressWarnings("unchecked")
	public void calc_prob(String s)
	{
		int size=s.length();
		HashMap<String,Integer> count= new HashMap<String,Integer>();
			for(int i=0; i<size; i++)
		{
			if(!count.containsKey(s.charAt(i)+""))
			{
				count.put(s.charAt(i)+"", 1);//if char doesn't exists put it with count=1
				str.add(s.charAt(i)+"");
			}
			else
			{
				int n=count.get(s.charAt(i)+"");
				int m= n+1;
				count.replace(s.charAt(i)+"",n, m); //if char exists increment its count
			}
		}
			//System.out.println(count);
			//System.out.println(size);
			//System.out.println(str);
			for(int i=0; i<str.size(); i++)
			{
			
				Double f=(double)count.get(str.get(i));//prob or freq but i used prob
				//System.out.println(f);
				prob.add(f);
				cell temp=new cell(str.get(i),f,null,null,null);
				tree.add(temp);
//				System.out.println(temp.getStr());
//				System.out.println(temp.getProb());
				Collections.sort(tree);//sorting the Arraylist of struct(class)
			}
			
		//printList();
			
	}
	void printList(){
		for(int i=0; i<tree.size(); i++)
		{
			tree.get(i).print(tree.get(i));
			System.out.println();
		}
	}
	String readFile(String path, Charset encoding) 
	  		  throws IOException 
	  		{
	  		  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  		  return new String(encoded, encoding);
	  		}
	@SuppressWarnings("unchecked")
	public  void compress(String OriginalFilePath) throws IOException{
		String data=readFile(OriginalFilePath, StandardCharsets.UTF_8);
		System.out.println(data);
		calc_prob(data);
		while(tree.size()!=1){//loop until list size is 1 has only one cell (root of the tree)
			int n=tree.size();
			//System.out.println(n+"****");
			String s1s2=tree.get(0).getStr()+tree.get(1).getStr();
			Double d1d2=tree.get(0).getProb()+tree.get(1).getProb();
			cell c1c2=new cell(s1s2,d1d2,tree.get(1),tree.get(0),null);//create parent node for both cells
			tree.get(0).setParent(c1c2);//setting right and left nodes's parent
			tree.get(1).setParent(c1c2);//0-->right...1-->left
			//c1c2.print(c1c2);
			tree.add(c1c2);//remove the smallest two nodes and add one parent node instead
			tree.remove(0);//remove first item 
			tree.remove(0);//remove second item which is now first item
			//printList();//print list after its size should be size-1
			//System.out.println();
			Collections.sort(tree);//sorting list after every remove and add
			//printList();
		}
		tree.get(0).setCode(tree.get(0).left);
		tree.get(0).setCode(tree.get(0).right);
		fill(tree.get(0));
		System.out.println(table);
		//printList();
		//System.out.println(tree.get(0).getStr());
		//na2s add table to file then coded string then read it in decompress
		File file=new File("compress.txt");
		FileOutputStream f = new FileOutputStream(file);
		ObjectOutputStream s= new ObjectOutputStream (f);
		s.writeObject(table);
		s.flush();
		
		for(int i=0; i<data.length(); i++)
		{
			String charCode=table.get(data.charAt(i)+"");
			//System.out.println(charCode);
			vec.add(charCode);
			f.write(charCode.getBytes("UTF-8"));
		}
		
	}
	public void decompress(String file) throws ClassNotFoundException{
		
		try {
			FileInputStream f = new FileInputStream(file);
			ObjectInputStream s = new ObjectInputStream (f);
			//fileTable=(HashMap<String,String>)s.readObject();
			System.out.println(fileTable);
//			byte[] data = Files.readAllBytes(Paths.get(file));
//			String stringdata=new String(data, StandardCharsets.UTF_8);
//			System.out.println(stringdata);
			String output="";
			
			//System.out.println(fileTable);
			for(int i=0; i<vec.size(); i++)
			{
				output+=fileTable.get(vec.get(i));
			}
			System.out.println(output);
			@SuppressWarnings("resource")
			FileWriter decFile = new FileWriter("decompress.txt");
			decFile.write(output);
			//System.out.println(output);
			decFile.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	void fill(cell c){//fill table
		if(c==null)
			return;
		fill(c.left);
		if(c.getStr().length()==1)
		{
			table.put( c.getStr(),c.getCode());
			fileTable.put(c.getCode(), c.getStr());
		}
		fill(c.right);
	}
	
	
	public static void main(String [] args) throws IOException, ClassNotFoundException
	{
		String s="aabbcacdee";
		Standard_huffman sh= new Standard_huffman();
		//sh.calc_prob(s);
		sh.compress("original.txt");
		sh.decompress("compress.txt");
		
	}
}
