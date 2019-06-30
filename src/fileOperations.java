import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;

public class fileOperations {

	public class imgWithQInverse {
		int length;
		int width;
		int[][] dequantizedPixels;
		Vector<Integer> QInverse;
		imgWithQInverse(int l, int w){
			length=l;
			width=w;
			dequantizedPixels=new int[length][width];
			QInverse=new Vector<>();

		}
	}
	fileOperations()
	{
	}
	int w;

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	int h;
	public int[][] convertImg(File file)
	{
		try {
			BufferedImage img = ImageIO.read(file);
			Raster raster = img.getData();
			setW(raster.getWidth());
			setH(raster.getHeight());
			int pixels[][] = new int[w][h];
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					pixels[x][y] = raster.getSample(x, y, 0);
				}
			}

			return pixels;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void convertPixels(int[][] pixels) {
		BufferedImage image = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_BYTE_GRAY);
		for (int x = 0; x < pixels.length; x++) {
			for (int y = 0; y < pixels[0].length; y++) {
				image.setRGB(x, y, pixels[x][y]);
			}
		}

		File ImageFile = new File("decompressedImg.png");
		try {
			ImageIO.write(image, "jpg", ImageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void writeImg(int[][] quantizedPixels, Vector<Integer> QInverse) throws IOException {
		int w=quantizedPixels.length;
		int h=quantizedPixels[0].length;
		FileWriter fwriter = new FileWriter("NUSQ_or_ffDPCM_compressed.txt");
		fwriter.write(w +" "+h+" ");
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				fwriter.write(quantizedPixels[i][j] + " ");  // quantizedPixels
			}
		}
		for(int i=0; i<QInverse.size(); i++){
			fwriter.write(((int)QInverse.get(i)) + " ");
		}
		fwriter.close();
	}
	public imgWithQInverse readImg(File compressedFile) throws FileNotFoundException {
		Scanner scanner = new Scanner(compressedFile);
		int length=scanner.nextInt();
		int width=scanner.nextInt();
		imgWithQInverse temp=new imgWithQInverse(length,width);
		for(int i=0; i<length; i++){
			for(int j=0; j<width; j++){
				temp.dequantizedPixels[i][j]=scanner.nextInt();
			}
		}
		while(scanner.hasNextInt()){
			temp.QInverse.add((Integer)scanner.nextInt());
		}
		return temp;
	}
	public void writeVectorImg(int[][] quantizedPixels, Vector<int[][]> codeBook) throws IOException {
		int w=quantizedPixels.length;
		int h=quantizedPixels[0].length;
		FileWriter fwriter = new FileWriter("NUVQ_compressed.txt");
		fwriter.write(w +" "+h+" ");
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				fwriter.write(quantizedPixels[i][j] + " ");  // quantizedPixels
			}
		}
		int n=codeBook.get(0).length;
		int m=codeBook.get(0)[0].length;
		fwriter.write(codeBook.size()+" "+n+" "+m+" ");
		for(int i=0; i<codeBook.size(); i++){
			for(int j=0; j<n; j++){
				for(int k=0; k<m; k++){
					fwriter.write(codeBook.get(i)[j][k] + " ");
				}
			}
		}
		fwriter.close();
	}

}