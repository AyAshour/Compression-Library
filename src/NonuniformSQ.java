import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.invoke.VolatileCallSite;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import static java.lang.Math.pow;

public class NonuniformSQ {

   static Vector<avgWithData> all_avgs=new Vector<>();
   static Vector<range> ranges=new Vector<>();
   static fileOperations fo=new fileOperations();
   static ImageClass IC=new ImageClass();
    static int x=0,y=0;
   static class range{
       int minRange=0;
       int maxRange=0;
       range(int min,int max){
           minRange=min;
           maxRange=max;
       }
       public int getMinRange() {
           return minRange;
       }
       public void setMinRange(int minRange) {
           this.minRange = minRange;
       }
       public int getMaxRange() {
           return maxRange;
       }
       public void setMaxRange(int maxRange) {
           this.maxRange = maxRange;
       }
   }
   static class avgWithData {
       int avg=0;
       Vector<Integer> group=new Vector<>();
       avgWithData(int a, Vector<Integer> g) {
           avg=a;
           group=g;
       }
       public void setGroup(Vector<Integer> group) {
           this.group = group;
       }
       public int getAvg() {
           return avg;
       }
       public void setAvg(int avg) { this.avg = avg; }
       public Vector<Integer> getGroup() {
           return group;
       }
    }
    public void printPixels(int pixels[][])
    {
        int n=pixels.length;
        int m=pixels[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++)

            {
                System.out.print(pixels[i][j]+" . ");
            }
            System.out.println();
        }
    }
    public static int getAvg(Vector vec)
    {
        int count=0;
        int result=0;
        int vec_size=vec.size();
        for (int i = 0; i < vec_size; i++) {

                 result+= (int)vec.get(i);
                count++;

        }
        System.out.println(result+" "+count);
        if(result==0)
            return 0;
        return result/count;
    }
    public static int getAvg(int pixels[][])
    {
         x=pixels.length;
        y=pixels[0].length;
        int count=0;
        int result=0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++)
            {
                result+=pixels[i][j];
                count++;
            }
        }
        System.out.println(result+" "+count);
        return result/count;
    }
    public void distribute( Vector<avgWithData> avgs,int pixels[][])
    {
        for(int i=0; i<avgs.size(); i++){ //first we need to clear the old groups
                avgs.get(i).setGroup(new Vector<Integer>());
        }
        int n=pixels.length;
        int m=pixels[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++)
            {
                int minDiff = Math.abs(avgs.get(0).getAvg() - pixels[i][j]);
                int indexOfMinDiff=0;
                for(int k=0; k<avgs.size(); k++) {
                    int diff= Math.abs(avgs.get(k).getAvg() - pixels[i][j]);
                    if (diff < minDiff) {
                        minDiff=diff;
                        indexOfMinDiff=k;
                    }
                }
                avgs.get(indexOfMinDiff).getGroup().add(pixels[i][j]);
            }
        }
    }
    public void compress(String path,int bit) throws IOException {
        if(bit==0)
        {
        return;
        }
        int arr[][]=IC.readImage(path);
        int sizeOfStruct = 0;
        Vector<Integer> copyOfAvgs=new Vector<>();
        double levels;
        //get avg of data
        avgWithData level= new avgWithData(0,null);
        level.setAvg(getAvg(arr));
        int avg=level.getAvg();
        all_avgs.add(level);
        Vector<Vector<Integer>> data=new Vector<Vector<Integer>>();
        for(int i=0; i<bit; i++) {
            levels = pow(2, i);
            for (int j = 0; j < (int) levels; j++) {
                level = all_avgs.get(0);
                avg=level.getAvg();
                all_avgs.remove(0);
                avgWithData less = new avgWithData(avg-1,null);  // left
                avgWithData more = new avgWithData(avg+1,null);  // right
                all_avgs.add(less);
                all_avgs.add(more);
            }
            distribute(all_avgs, arr); //distribute the data
            sizeOfStruct=all_avgs.size();
            copyOfAvgs.clear();
            for(int j=0; j<sizeOfStruct; j++) {  //get average of each group
                int newAvg = getAvg(all_avgs.get(j).getGroup());  //calc avg of each group
                all_avgs.get(j).setAvg(newAvg);
                copyOfAvgs.add(newAvg);
            }
        }
        boolean same=false;
        Vector<Integer> temp=new Vector<>();
        temp=copyOfAvgs;
        while (!same) {
            copyOfAvgs=temp;
            temp.clear();
            distribute(all_avgs, arr);
            for (int j = 0; j < sizeOfStruct; j++) {  //set average of each group
                int newAvg = getAvg(all_avgs.get(j).getGroup());  //calc avg of each group
                all_avgs.get(j).setAvg(newAvg);
                temp.add(newAvg);
            }
            for (int j = 0; j < sizeOfStruct; j++) {
                if(copyOfAvgs.get(j)!=all_avgs.get(j).getAvg()){
                    same=false;
                    break;
                }
                else
                    same=true;
            }
        }
        //reconstruct ranges
        ranges.add(new range(0,0));
        for(int i=1; i<copyOfAvgs.size(); i++){
            int mid=(copyOfAvgs.get(i-1)+copyOfAvgs.get(i))/2;
            ranges.add(new range(mid,255));
            ranges.get(i-1).setMaxRange(mid);
        }
        int[][] quantizedPixels= new int[x][y];
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                for(int k=0; k<ranges.size(); k++){
                    int pixel=arr[i][j];
                    if(pixel>=ranges.get(k).getMinRange() && pixel<ranges.get(k).getMaxRange()){
                        quantizedPixels[i][j]=k;
                        break;
                    }
                }
            }
        }
        //write quantized pixels to file
        fo.writeImg(quantizedPixels,copyOfAvgs);
    }

   public void decompress(File compressedFile) throws FileNotFoundException {

        int width=fo.readImg(compressedFile).length;
        int height=fo.readImg(compressedFile).width;
        int[][] dequantizedPixels=new int[width][height];
        dequantizedPixels=fo.readImg(compressedFile).dequantizedPixels;
        Vector<Integer> QInverse= new Vector<>();
        QInverse=fo.readImg(compressedFile).QInverse;

        //replace each pixel with its Qinverse(avg)
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                int index=dequantizedPixels[i][j];
                dequantizedPixels[i][j]=(int)(QInverse.get(index));
            }
        }
        //convert dequantized pixels to another image file
        String compressedImagePath = "NUSQ_decompressedImg.jpg";
        IC.writeImage(dequantizedPixels,compressedImagePath);
    }
    public static void main(String args[]) throws IOException {

        fileOperations fo=new fileOperations();
        String filePath= "C:\\Users\\Aya Essam\\workspace\\Compression-Algorithms\\cameraMan.jpg";
       // int arr[][]=fo.convertImg(f);
       int[][] arr={{6,15,17,60,100,90,66,59,18},{3,5,16,14,67,63,2,98,92}};
        NonuniformSQ nusq=new NonuniformSQ();
       // nusq.printPixels(arr);
        /*System.out.println(arr[1].length);
        System.out.println(arr.length);
        System.out.println(getAvg(arr));*/
        Scanner sc=new Scanner(System.in);
        int bit = sc.nextInt();
        nusq.compress(filePath,bit);
        File dec=new File("C:\\Users\\Aya Essam\\workspace\\Compression-Algorithms\\NUSQ_compressed.txt");
        nusq.decompress(dec);
    }

}
