import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class ArithmeticCoding {

	static int size;
	static HashMap<Character,Double> count= new HashMap<Character,Double>();
	static Vector<Character> ch=new Vector<Character>();
	HashMap<Character,letter> original_interval = new HashMap<Character,letter>();
	public static void calc_prob(String s)
	{
		size=s.length();
		for(int i=0; i<size; i++)
		{
			if(!count.containsKey(s.charAt(i)))
			{
				Double d=1.0/size;
				count.put(s.charAt(i), d);//if char doesn't exists put it with count=1
				ch.add(s.charAt(i));
			}
			else
			{
				Double n= count.get(s.charAt(i));
				double m= n+(1.0/size);
				count.replace(s.charAt(i),n, m); //if char exists increment its count
			}
		}
			System.out.println(count);
	}
	void compress(String s) throws IOException
	{
		calc_prob(s);
		Double low=0.0, high=0.0;
		int i;
		for( i=0; i<ch.size(); i++)//building first interval only
		{
			low=high;
			high+=count.get(ch.get(i));
			letter a=new letter(ch.get(i),low,high);
			original_interval.put(ch.get(i),a);
		}
		for( i=0; i<original_interval.size(); i++)
			System.out.print(original_interval.get(s.charAt(i)).getLower() +"/"+s.charAt(i)+ " ** ");
		System.out.println(original_interval);
		Double low_range=0.0,high_range=1.0;
		for(i=0; i<s.length(); i++)
		{
			Character c=s.charAt(i);
			Double my_low=original_interval.get(c).getLower();
			Double my_high=original_interval.get(c).getUpper();
			low=low_range+(high_range-low_range)*my_low;
			high=low_range+(high_range-low_range)*my_high;
			low_range=low;
			high_range=high;
		}
		System.out.println(low+"  "+high);
		Double val=ThreadLocalRandom.current().nextDouble(low, high);
		System.out.println(val);
		FileWriter comFile = new FileWriter("compress.txt");
		comFile.write(val.toString());
		comFile.close();
		
	}
	void decompress(Double d) throws IOException
	{
		Double code;
		String output="";
		Double low=0.0,high=0.0;
		Double low_range=0.0,high_range=1.0;
		for(int i=0; i<size; i++)
		{
			letter l=new letter();
			code=(d-low_range)/(high_range-low_range);
			System.out.println(code);
			for (Map.Entry<Character, letter> entry : original_interval.entrySet()) 
			{
				Double lower=entry.getValue().getLower();
				Double higher=entry.getValue().getUpper();
				if(code<higher && code>lower)
				{
					l=entry.getValue();
					break;
				}
			}
			output+=l.getCh();
			
				Character c=l.getCh();
				System.out.println(c);
				Double my_low=original_interval.get(c).getLower();
				Double my_high=original_interval.get(c).getUpper();
				low=low_range+(high_range-low_range)*my_low;
				high=low_range+(high_range-low_range)*my_high;
				low_range=low;
				high_range=high;
		}
		FileWriter decFile = new FileWriter("decompress.txt");
		decFile.write(output);
		decFile.close();
		System.out.println(output);
	}
	static String readFile(String path, Charset encoding) 
	  		  throws IOException 
	  		{
	  		  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  		  return new String(encoded, encoding);
	  		}
	
	public static void main(String args[]) throws IOException
	{
		ArithmeticCoding ac=new ArithmeticCoding();
		String data=readFile("data.txt", StandardCharsets.UTF_8);
		ac.compress(data);
		String value=readFile("compress.txt", StandardCharsets.UTF_8);
		ac.decompress(Double.parseDouble(value));
		
		
	}
}
