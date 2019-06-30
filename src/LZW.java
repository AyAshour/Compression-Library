import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class LZW {
	
	public  void compress(String s) throws IOException
	{
		int dictSize =128;
		ArrayList<Integer> compList = new ArrayList<Integer>();
		Map<String, Integer> dict = new HashMap<String , Integer>();
		for(int i=0; i<dictSize; i++)
		{
			dict.put(""+(char) i, i);
		}
		//System.out.println(dict);
		String curr = s.charAt(0)+""; // to make it string not char
		String next= s.charAt(1)+"";
		for(int i=0; i<s.length()-1; i++)
		{	next=s.charAt(i+1)+"";
			String temp = curr + next;
			if(dict.containsKey(temp))
			{
				//compList.add(dict.get(temp));
				curr= temp;
				//next=s.charAt(i+1)+"";
			}
			else
			{
				System.out.println(curr);
				compList.add(dict.get(curr));
				dict.put(temp, dictSize++);
				curr= next;
				
			}
			
		}
		compList.add(dict.get(next));
		//System.out.println(compList.size());
		//File fos = new File("compressed.txt");
		FileWriter fw = new FileWriter("compressed.txt");
		for(int i=0; i<compList.size(); i++){
			fw.write(compList.get(i)+",");
			System.out.println(compList.get(i));
		}
		fw.close();
//		FileOutputStream fos = new FileOutputStream("Compressed.txt");
//		ObjectOutputStream oos = new ObjectOutputStream(fos);
//		oos.writeObject(compList);
//		oos.close();
//		
	}
	public  void decompress(String fileName) throws IOException
	{
		Scanner read = new Scanner (new File(fileName));
		   read.useDelimiter(",");
		   int num;
		   ArrayList<Integer> dec = new ArrayList <Integer>();
		   while(read.hasNext())
		   {
			   num = read.nextInt();
			   dec.add(num);
			 //  System.out.println(num);
		   }
		   read.close();
		   String s="";
		   int dictSize=128;
		   HashMap<Integer,String> dec_dic = new HashMap<Integer,String>();
		   for(int i=0; i<dictSize; i++)
			   dec_dic.put(i, (char)i+"");
		   String prev=dec_dic.get(dec.get(0)), cur =dec_dic.get(dec.get(1)),tempp="";
		   s=dec_dic.get(dec.get(0));
		   for(int i=1; i<dec.size(); i++)
		   {

			   prev=dec_dic.get(dec.get(i-1));
			  if(dec_dic.containsKey(dec.get(i)))
			  {
				  cur=dec_dic.get(dec.get(i));
				  s+=cur;
				  //if cur in dict then continue
				 dec_dic.put(dictSize++, (prev+cur.charAt(0)));
			
			  }
			  else
			  {
				  cur = prev + prev.charAt(0);
				  s+=cur;
				  dec_dic.put(dictSize++, cur);
				//  System.out.println(cur);
			  }
			  
		   }
		  // s+=cur;
		  
		   
		   FileWriter fw = new FileWriter("decompressed.txt");
		   fw.write(s);
		   fw.close();
		   System.out.println(s);
		 
	}
	public static  String readFile(String path, Charset encoding) 
  		  throws IOException 
  		{
  		  byte[] encoded = Files.readAllBytes(Paths.get(path));
  		  return new String(encoded, encoding);
  		}
	public static void main(String[] args) throws IOException
	{
		LZW obj = new LZW();
		String content = readFile("original.txt", StandardCharsets.UTF_8);
		   obj.compress(content);
		   obj.decompress("compressed.txt");
	}
}
