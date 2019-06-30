



import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;
public class LZ77 {

	
	 public String substring ="";
	 public Vector<Byte> position = new Vector<Byte>(3);
	 public Vector<Byte> noOfLetters = new Vector<Byte>(3);
	 public int i;
	 public Vector<Character> nextChar= new Vector<Character>(3);
	 
	 	public void decompress(String path)
	 	throws IOException 
    		{
	 		String buff= readFile(path, StandardCharsets.UTF_8);
	        FileOutputStream fos=new FileOutputStream("decompress.txt");
	        DataOutputStream dos=new DataOutputStream(fos);
	 
	        int steps=0,len=0,k=0;
	        char c;
	        String s = "";
	        while(buff.length()>k+1)
	        {
	            steps=buff.charAt(k++);//steps back
	           // System.out.println(steps);
	            len=buff.charAt(k++);//length
	           // System.out.println(len);
	            c = buff.charAt(k++);//next character
	           // System.out.println(c);
	            if(steps==0)
	                s+=c;
	            else
	            {

	                s+=(s.substring((s.length()-steps),((s.length()-steps)+len))+c);
	            }
	        }
	        dos.writeChars(s);
	       // System.out.println(s);
	        dos.close();
    		
		}
	 	public void compress(String s)
	    {
	    	substring+=s.charAt(0); 
	    	position.add((byte)0); 
	    	noOfLetters.add((byte)0); 
	    	nextChar.add(s.charAt(0)); 
	        for ( i = 1; i < s.length(); i++)
	        {
	            int idx = 0, letters = 0;
	           // int substep=i;
	            String sub = s.substring(i, i);
	            String searchstr = s.substring(0, i );
	           // System.out.println(searchstr);
	            int end=i+letters+1, sz=s.length();
	            while (true) 
	            {
	            	
	            	int searchIdx = searchstr.lastIndexOf(sub);
	            	if (searchIdx != -1 && end < sz) {
	                idx = i - searchIdx;
	                letters++;
	                end=i+letters;
	                sub = s.substring(i, end);
	               // System.out.println(sub+i);
	           
	              //  i++;
	                }
	                else 
	                {
	                 // i--;
	                    break;
	                }
	            	   //searchstr = s.substring(0, i+letters);
	            	  // System.out.println(searchstr+"mmbreakawy");

	            }
	            
	            substring+=sub; 
	            position.add((byte)idx); 
	            noOfLetters.add((byte)(letters-1)); 
	            nextChar.add(s.charAt(i+letters-1));
	            
	            i += letters-1;
	        }
	    }
	    public void printobj()
	    {
	    	for(int j=0; j<nextChar.size(); j++ )
	    	{
	    		System.out.println("<"+position.get(j)+","+noOfLetters.get(j)+","+nextChar.get(j)+">");
	    	}
	    	
	    }
	     void writeFile()
	    {
	    	//System.out.println(noOfLetters.get(4).byteValue()+"--------");
	    	 File file = new File("compress.txt");
	    	         FileOutputStream fos = null;
	    	         try {
	    	             fos = new FileOutputStream(file);
	    	             // Writes bytes from the specified byte array to this file output stream
	    	            for(int i=0; i<position.size(); i++)
	    	            {
	    	            	fos.write(position.get(i));
	    	            	fos.write(noOfLetters.get(i));
	    	            	fos.write(nextChar.get(i));	    	         
	    	            }
	    	         }
	    	         catch (FileNotFoundException e) {
	    	             System.out.println("File not found" + e);
	    	         }
	    	         catch (IOException ioe) {
	    	             System.out.println("Exception while writing file " + ioe);
	    	         }
	    	         finally {
	    	           
	    	             try {
	    	                 if (fos != null) {
	    	                     fos.close();
	    	                 }
	    	             }
	    	             catch (IOException ioe) {
	    	                 System.out.println("Error while closing stream: " + ioe);
	    	             }
	    	         }
	    }
	    public static String readFile(String path, Charset encoding) 
	    		  throws IOException 
	    		{
	    		  byte[] encoded = Files.readAllBytes(Paths.get(path));
	    		  return new String(encoded, encoding);
	    		}
	   public static void main(String[] args) 
	   {
		   try{
		   LZ77 obj = new LZ77();
		   String content = readFile("test.txt", StandardCharsets.UTF_8);
		   obj.compress(content);
		   obj.printobj();
		   obj.writeFile();
		   obj.decompress("compress.txt");
		   } catch(IOException ie) {
	            ie.printStackTrace();
	        }   
//		   BufferedReader reader = null;
//		   String s=new String("abaababaabbbbbbbbbbbba");
//		   String s2=new String("abaababababababababa");
//		   obj.compress(s);
//		   obj.printobj();
	   }
	}

	

