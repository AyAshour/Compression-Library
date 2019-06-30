import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import static java.lang.Math.pow;

public class vectorQuantization {

    static int n,m;
    vectorQuantization(int w,int h){
        n=w;
        m=h;
    }
    static ArrayList<avgWithData> all_avgs=new ArrayList<>();
    static Vector<int[][]> codeBook=new Vector<>();
    static fileOperations fo=new fileOperations();
    static ImageClass IC=new ImageClass();
    static int x=0,y=0;
    static int noOfBlocks=0;

    static class avgWithData {
        int[][] avg=new int[n][m];
        ArrayList<int[][]> group= new ArrayList<>();
        avgWithData(){}
        public int[][] getAvg() {
            return avg;
        }

        public void setAvg(int[][] avg) {
            this.avg = avg;
        }

        public ArrayList<int[][]> getGroup() {
            return group;
        }

        public void setGroup(ArrayList<int[][]> group) {
            this.group = group;
        }

        avgWithData(int[][] a, ArrayList<int[][]> g) {
            avg=a;
            group=g;
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
    public static int[][] getAvg(ArrayList<int[][]> vec)
    {
        int count=0;
        int[][] result=new int[n][m];
        int[][] temp= new int [n][m];
        int vec_size=vec.size();
        for(int x=0; x<n; x++){
            for(int y=0; y<m; y++){
                for (int i = 0; i < vec_size; i++) {
                    temp=vec.get(i);
                    result[x][y]+=temp[x][y];

                }
                if(vec_size>0)
                    result[x][y]/=vec_size;
            }
        }

        return result;
    }
    public static int[][] getAvg(int pixels[][],int n,int m)
    {
        x=pixels.length;
        y=pixels[0].length;
        int[][] result= new int[n][m];
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                result[i][j]=0;
            }
        }
        int count=(x*y)/(n*m);
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++)
            {
                result[i%n][j%m]+=pixels[i][j];
            }
        }
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                result[i][j]/=count;
            }
        }
        return result;
    }
    int[][] minusAvg(int[][] avg){
        int[][] temp=new int[n][m];
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                temp[i][j]=avg[i][j]-1;
            }
        }
        return temp;
    }
    int[][] plusAvg(int[][] avg){
        int[][] temp=new int[n][m];
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                temp[i][j]=avg[i][j]+1;
            }
        }
        return temp;
    }
    int[][] copyArray(int[][] block){
        int[][] temp=new int[n][m];
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                temp[i][j]=block[i][j];
            }
        }
        return temp;
    }
    int[] buildQuantizer(Vector<int[][]> codeBook,int[][] imgData){
        int[] compressedImgData= new int[noOfBlocks];
        int c=0;
        int l = imgData.length;
        int w = imgData[0].length;
        boolean b = true;
        int[][] block = new int[n][m];
        int i=0, j=0;
        for (i=0; i < n; i++) {
            for (j=0; j < m; j++) {
                block[i][j] = imgData[i][j];
            }
        }
        while (b) {
            int minMSE = MSE(codeBook.get(0), block);
            int indexOfMinMSE = 0;
            for (int k = 0; k < codeBook.size(); k++) {
                int diff = MSE(codeBook.get(k), block);
                if (diff < minMSE) {
                    minMSE = diff;
                    indexOfMinMSE = k;
                }
            }
            System.out.println(indexOfMinMSE+" ** "+c);
            if(j!=w){
                i-=m;
            }
            if(i==l && j==w){
                break;
            }
            compressedImgData[c++]=indexOfMinMSE; //index out of bound
            for (int x=0; x < n; x++) {
                for (int y=0; y < m; y++) {
                    if(j==w){
                        j=0;
                    }
                    if(i==l && j<w){
                        i=0;
                    }
                    block[x][y]=imgData[i][j];
                    j=(j+1)%(w+1);
                }
                i=(i+1)%(l+1);
                j-=m;
            }
            j+=m;
        }
      return compressedImgData;
    }
    public void distribute( ArrayList<avgWithData> avgs,int pixels[][]) {
        for (int i = 0; i < avgs.size(); i++) { //first we need to clear the old groups
            avgs.get(i).setGroup(new ArrayList<>());
        }
        int l = pixels.length;
        int w = pixels[0].length;
        boolean b = true;
        int[][] block = new int[n][m];
        int i=0, j=0;
        for (i=0; i < n; i++) {
            for (j=0; j < m; j++) {
                System.out.println(i+"* "+j+" "+i+" "+j+" ");
                block[i][j] = pixels[i][j];
            }
        }

        while (b) {
            int minMSE = MSE(avgs.get(0).getAvg(), block);
            int indexOfMinDiff = 0;
            for (int k = 0; k < avgs.size(); k++) {
                int diff = MSE(avgs.get(k).getAvg(), block);
                if (diff < minMSE) {
                    minMSE = diff;
                    indexOfMinDiff = k;
                }
            }
            avgs.get(indexOfMinDiff).getGroup().add(copyArray(block));
            if(j!=w){
                i-=n;
            }
            if(i==l && j==w){
                b=false;
                break;
            }
            for (int x=0; x < n; x++) {
                for (int y=0; y < m; y++) {
                    if(j==w){
                        j=0;
                    }
                    if(i==l && j<w){
                        i=0;
                    }

                    System.out.println(x+" "+y+" "+i+" "+j+" ");
                    block[x][y]=pixels[i][j];

                    j=(j+1)%(w+1);
                }
                i=(i+1)%(l+1);
                j-=m;
            }
            j+=m;
        }
    }
   int MSE(int[][]avg,int[][]block){
        int res=0;
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                res+=(avg[i][j]-block[i][j])*(avg[i][j]-block[i][j]);
            }
        }
        return res;
   }
   int[][] padding (int[][] arr, int block_n, int block_m){
       int img_n=arr.length , img_m=arr[0].length;
       int rows=block_n-(img_n%block_n);
       int cols=block_m-(img_m%block_m);
       if(cols>=block_m)
           cols-=block_m;
       if(rows>=block_n)
           rows-=block_n;
       int [][] updated_arr= new int[rows+img_n][cols+img_m];
       for(int i=0; i<img_n; i++){
           for(int j=0; j<img_m; j++){
               updated_arr[i][j]=arr[i][j];
           }
       }
       return updated_arr;
   }
    public void compress( String path,int n,int m, int codeBookSize) throws IOException {
        if(codeBookSize==0)
        {
            return;
        }
        int arr1[][]=IC.readImage(path);
        int arr[][]=padding(arr1,n,m);
        int height=arr.length/n;
        int width=arr[0].length/m;
        noOfBlocks=(arr.length*arr[0].length)/(n*m);
        int sizeOfStruct = 0;
        Vector<int[][]> copyOfAvgs=new Vector<>();
        double levels;
        //get avg of data
        avgWithData level= new avgWithData();
        level.setAvg(getAvg(arr,n,m));
        int[][] avg=level.getAvg();
        all_avgs.add(level);
        for(int i=0; i<codeBookSize; i++) {
            levels = pow(2, i);
            for (int j = 0; j < (int) levels; j++) {
                level = all_avgs.get(0);
                avg=level.getAvg();
                all_avgs.remove(0);
                avgWithData less = new avgWithData(minusAvg(avg),null);  // left
                avgWithData more = new avgWithData(plusAvg(avg),null);  // right
                all_avgs.add(less);
                all_avgs.add(more);
            }
            distribute(all_avgs, arr); //distribute the data
            sizeOfStruct=all_avgs.size();
            copyOfAvgs.clear();
            for(int j=0; j<sizeOfStruct; j++) {  //get average of each group
                int[][] newAvg = getAvg(all_avgs.get(j).getGroup());  //calc avg of each group
                all_avgs.get(j).setAvg(newAvg);
                copyOfAvgs.add(newAvg);
            }
        }
        boolean same=false;
        Vector<int[][]> temp=new Vector<>();
        temp=copyOfAvgs;
        while (!same) {
            copyOfAvgs=temp;
            temp.clear();
            distribute(all_avgs, arr);
            for (int j = 0; j < sizeOfStruct; j++) {  //set average of each group
                int[][] newAvg = getAvg(all_avgs.get(j).getGroup());  //calc avg of each group
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
        codeBook=copyOfAvgs;

        int[] quantizedPixels= new int[noOfBlocks];
        quantizedPixels=buildQuantizer(codeBook,arr);
        int[][] compressedData=new int[height][width];
        int k=0;
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                compressedData[i][j]=quantizedPixels[k++];
            }
        }


        //write quantized pixels to file
        fo.writeVectorImg(compressedData,codeBook);
    }

    static void DequantizeData(int[][] deCompressedData, int[] dequantizedPixels,Vector<int[][]>codeBook){
        int a=0;
        int l = deCompressedData.length;
        int w = deCompressedData[0].length;
        int n=codeBook.get(0).length, m=codeBook.get(0)[0].length;
        int[][] block = new int[n][m];
        int i=0, j=0;
        block=codeBook.get(dequantizedPixels[a++]);
        for (i=0; i < n; i++) {
            for (j=0; j < m; j++) {
                deCompressedData[i][j] = block[i][j];
            }
        }
        while (true) {
            if(i==l && j==w){
                break;
            }
            block=codeBook.get(dequantizedPixels[a++]);
            if(j!=w){
                i-=m;
            }
            for (int x=0; x < n; x++) {
                for (int y=0; y < m; y++) {
                    if(j==w){
                        j=0;
                    }
                    deCompressedData[i][j] = block[x][y];
                    j=(j+1)%(w+1);
                }
                i=(i+1)%(l+1);
                j-=m;
            }
            j+=m;
        }
    }
    void decompress(File compressedFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(compressedFile);
        int width=scanner.nextInt();
        int height=scanner.nextInt();
        int[] dequantizedPixels=new int[width*height];
        for(int i=0; i<width*height; i++){
                dequantizedPixels[i]=scanner.nextInt();
        }
        int CBsize=scanner.nextInt();
        int l=scanner.nextInt();
        int w=scanner.nextInt();
        Vector<int[][]> codeBook=new Vector<>();
        for(int i=0; i<CBsize; i++){
            codeBook.add(new int[l][w]);
            for(int j=0; j<l; j++){
                for(int k=0; k<w; k++){
                    codeBook.get(i)[j][k]=scanner.nextInt();
                }
            }
        }
        //replace each pixel with its vector from codeBook(avg)
        int[][] deCompressedData=new int[width*l][height*w];
        DequantizeData(deCompressedData,dequantizedPixels,codeBook);
        String compressedImagePath = "NUVQ_decompressedImg.jpg";
        IC.writeImage(deCompressedData,compressedImagePath);
    }
    public static void main(String args[]) throws IOException {

        ImageClass fo=new ImageClass();
        String filePath= "C:\\Users\\Aya Essam\\workspace\\Compression-Algorithms\\cameraMan.jpg";
       // int arr[][]=fo.readImage(filePath);
        //int[][] arr={{1,2,7,9,4,11},{3,4,6,6,12,12},{4,9,15,14,9,9},{10,10,20,18,8,8},{4,3,17,16,1,4},{4,5,18,18,5,6}};
        Scanner sc=new Scanner(System.in);
        int n = sc.nextInt();
        int m=sc.nextInt();
        vectorQuantization nuvq=new vectorQuantization(n,m);
        int codeBookSize=sc.nextInt();
        nuvq.compress(filePath,n,m,codeBookSize);
        File dec=new File("C:\\Users\\Aya Essam\\workspace\\Compression-Algorithms\\NUVQ_compressed.txt");
        nuvq.decompress(dec);
    }

}
