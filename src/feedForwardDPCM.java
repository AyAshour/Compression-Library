import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class feedForwardDPCM {
    private fileOperations fo= new fileOperations();
    private ImageClass IC=new ImageClass();
    public void  compress(String filePath,int bits) throws IOException {
        int arr[][]=IC.readImage(filePath);
        int k=0;
        int length=arr.length;
        int width=arr[0].length;
        int totalSize=length*width;
        int[] oneD=new int[totalSize];
        for(int i=0; i<length; i++){
            for(int j=0; j<width; j++){
                oneD[k++]=arr[i][j];
            }
        }
        int[] diff=new int[totalSize];
        diff[0]=oneD[0];
        int max=diff[0];
        int min=0;
        for(int i=1; i<totalSize; i++){
            diff[i]=oneD[i]-oneD[i-1];
            if(diff[i]>max)
                max=diff[i];
            if(diff[i]<min)
                min=diff[i];
        }
        double levels=Math.pow(2.0,(double)bits);
        double step = Math.ceil((double)(max-min)/levels);
        int[] ranges=new int[(int)levels+1];
        Vector<Integer> QInverse=new Vector<>();
        ranges[0]=min;
        for(int i=1; i<levels+1; i++){
            ranges[i]=ranges[i-1]+(int)step;
            QInverse.add((ranges[i-1]+ranges[i])/2);
        }
        int[] quantized= new int[totalSize];
        quantized[0]=oneD[0];
        for(int i=1; i<totalSize; i++){
            int temp=diff[i];
            for(int j=0; j<levels ; j++){
                if(temp>=ranges[j] && temp<ranges[j+1]){
                    quantized[i]=j;
                    break;
                }
            }
        }
        int[][] quantized2D=new int[length][width];
        k=0;
        for(int i=0; i<length; i++){
            for(int j=0; j<width; j++){
                quantized2D[i][j]=quantized[k++];
            }
        }

        fo.writeImg(quantized2D,QInverse);
    }

    public void decompress(File compressedFile) throws FileNotFoundException {
        int width=fo.readImg(compressedFile).length;
        int height=fo.readImg(compressedFile).width;
        int[][] dequantizedPixels2D=new int[width][height];
        dequantizedPixels2D=fo.readImg(compressedFile).dequantizedPixels;
        Vector<Integer> QInverse= new Vector<>();
        QInverse=fo.readImg(compressedFile).QInverse;

        Vector<Integer> dequantizedPixels=new Vector<>();
        //replace each pixel with its Qinverse(avg)
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                if(i==0 && j==0){
                    dequantizedPixels.add((dequantizedPixels2D[i][j]));
                }
                else {
                    int index = dequantizedPixels2D[i][j];
                    dequantizedPixels.add(QInverse.get(index));
                }
            }
        }

        int [] decode=new int[width*height];
        decode[0]=(int)dequantizedPixels.get(0);
        for(int i=1; i<dequantizedPixels.size(); i++){
            decode[i]=decode[i-1]+(int)dequantizedPixels.get(i);
            if(decode[i]<0){
                decode[i]=0;
            }
            else if(decode[i]>255){
                decode[i]=255;
            }
        }
        int k=0;
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++) {
                    dequantizedPixels2D[i][j] = decode[k++];
            }
        }

        //convert dequantized pixels to another image file
        String compressedImagePath = "ffDPCM_decompressedImg.jpg";
        IC.writeImage(dequantizedPixels2D,compressedImagePath);
    }

    public static void main(String args[]) throws IOException {

       // ImageClass =new ImageClass();
        String filePath= "C:\\Users\\Aya Essam\\workspace\\Compression-Algorithms\\cameraMan.jpg";
        // int arr[][]=fo.readImage(filePath);
       // int[][] arr={{15,16,24},{33,44,68}};
        feedForwardDPCM ffDPCM=new feedForwardDPCM();
        Scanner sc=new Scanner(System.in);
        int bits=sc.nextInt();
        ffDPCM.compress(filePath,bits);

        File dec=new File("C:\\Users\\Aya Essam\\workspace\\Compression-Algorithms\\NUSQ_or_ffDPCM_compressed.txt");
        ffDPCM.decompress(dec);
    }

}
