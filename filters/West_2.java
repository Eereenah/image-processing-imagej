import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;
import ij.plugin.filter.*;

public class West_2 implements PlugIn{

	public void run(String arg) {
		ImagePlus imp = IJ.getImage();
		ImageProcessor proc = imp.getProcessor();

// Task 1
		for(int i=0; i < proc.getWidth(); i++){
			for(int j=0; j < proc.getHeight(); j++){
				if (proc.get(i, j) == 255){
					proc.set(i, j, 254);
				}
			}
		}
// Task 2
		proc.setLineWidth(2);
		proc.setColor(Color.white);
		proc.drawLine(273, 0, 273, 757);

// Task 3
		//there is no need to rotate the images as the line is already 
		//perpendicular to Ox axis
		// ImagePlus rotated = IJ.createImage("rotated", 533, 757, 1, 8);
		// ImageProcessor rot_proc = rotated.getProcessor().duplicate();
		// double theta = 45.0;
		// double radians = Math.toRadians(theta);
		// for(int i=0; i<proc.getWidth(); i++){
		// 	for(int j=0; j<proc.getHeight(); j++){
		// 		int xs = (int)(Math.cos(radians)*i - Math.sin(radians)*j);
		// 		int ys = (int)(Math.sin(radians)*i + Math.cos(radians)*j);
		// 		rot_proc.set(i, j, 255);
		// 		if(xs < rot_proc.getWidth() && xs > 0 && ys < rot_proc.getHeight() && ys > 0){
		// 			rot_proc.set(xs, ys, proc.get(i, j));
		// 		}
		// 	}
		// }
		// ImagePlus rotated_final = new ImagePlus("west03", rot_proc);
		// rotated_final.show();
		
// Task 4

		proc.setRoi(0, 239, 538, 338);
		ImagePlus cropped = new ImagePlus("west04", proc.crop());
		cropped.show();

// Task 5
// Sobel's operator
		Convolver con = new Convolver();	

		float[] sobelKernelX = {-1, 0, 1, -2, 0, 2, -1, 0, 1};
		float[] sobelKernelY = {-1, -2, -1, 0, 0, 0, 1, 2, 1};

		ImageProcessor sobelx_proc = cropped.getProcessor().duplicate();
		con.convolve(sobelx_proc, sobelKernelX, 3, 3);
		ImagePlus sobelX = new ImagePlus("sobelX", sobelx_proc);
		//sobelX.show();

		ImageProcessor sobely_proc = cropped.getProcessor().duplicate();
		con.convolve(sobely_proc, sobelKernelY, 3, 3);
		ImagePlus sobelY = new ImagePlus("sobelY", sobely_proc);
		//sobelY.show();

		ImageProcessor sobel = cropped.getProcessor().duplicate();
		for(int i=0; i<sobelx_proc.getWidth(); i++){
			for(int j=0; j<sobely_proc.getHeight(); j++){
				int newpixel = (int)Math.sqrt(sobely_proc.get(i, j)*sobely_proc.get(i, j) + sobelx_proc.get(i, j)*sobelx_proc.get(i, j));
				sobel.set(i, j, newpixel);
			}
		}
		ImagePlus sobel_edges = new ImagePlus("west051", sobel);
		sobel_edges.show();


// Improved Sobel's operator

		// float[] isobelKernelX = {-3, 0, 3, -10, 0, 10, -3, 0, 3};
		// float[] isobelKernelY = {-3, -10, -3, 0, 0, 0, 3, 10, 3};

		// ImageProcessor isobelx_proc = cropped.getProcessor().duplicate();
		// con.convolve(isobelx_proc, isobelKernelX, 3, 3);
		// ImagePlus isobelX = new ImagePlus("isobelX", isobelx_proc);
		// isobelX.show();

		// ImageProcessor isobely_proc = cropped.getProcessor().duplicate();
		// con.convolve(isobely_proc, isobelKernelY, 3, 3);
		// ImagePlus isobelY = new ImagePlus("isobelY", isobely_proc);
		// isobelY.show();

		// ImageProcessor isobel = cropped.getProcessor().duplicate();
		// for(int i=0; i<isobelx_proc.getWidth(); i++){
		// 	for(int j=0; j<isobely_proc.getHeight(); j++){
		// 		int newpixel = (int)Math.sqrt(isobely_proc.get(i, j)*isobely_proc.get(i, j) + isobelx_proc.get(i, j)*isobelx_proc.get(i, j));
		// 		isobel.set(i, j, newpixel);
		// 	}
		// }
		// ImagePlus isobel_edges = new ImagePlus("west052", isobel);
		// isobel_edges.show();

// Prewitt's operator

		float[] prKernelX = {-1, 0, 1, -1, 0, 1, -1, 0, 1};
		float[] prKernelY = {-1, -1, -1, 0, 0, 0,  1, 1, 1};

		ImageProcessor prx_proc = cropped.getProcessor().duplicate();
		con.convolve(prx_proc, prKernelX, 3, 3);
		ImagePlus prX = new ImagePlus("prX", prx_proc);
		//prX.show();

		ImageProcessor pry_proc = cropped.getProcessor().duplicate();
		con.convolve(pry_proc, prKernelY, 3, 3);
		ImagePlus prY = new ImagePlus("prY", pry_proc);
		//prY.show();

		ImageProcessor pr = cropped.getProcessor().duplicate();
		for(int i=0; i<prx_proc.getWidth(); i++){
			for(int j=0; j<pry_proc.getHeight(); j++){
				int newpixel = (int)Math.sqrt(pry_proc.get(i, j)*pry_proc.get(i, j) + prx_proc.get(i, j)*prx_proc.get(i, j));
				pr.set(i, j, newpixel);
			}
		}
		ImagePlus pr_edges = new ImagePlus("west053", pr);
		pr_edges.show();

// Robert's operator

		float[] robKernelX = {0, 0, 1, 0, -1, 0, 0, 0, 0};
		float[] robKernelY = {1, 0, 0, 0, -1, 0, 0, 0, 0};

		ImageProcessor robx_proc = cropped.getProcessor().duplicate();
		con.convolve(robx_proc, robKernelX, 3, 3);
		ImagePlus robX = new ImagePlus("robX", robx_proc);
		//robX.show();

		ImageProcessor roby_proc = cropped.getProcessor().duplicate();
		con.convolve(roby_proc, robKernelY, 3, 3);
		ImagePlus robY = new ImagePlus("robY", roby_proc);
		//robY.show();

		ImageProcessor rob = cropped.getProcessor().duplicate();
		for(int i=0; i<robx_proc.getWidth(); i++){
			for(int j=0; j<roby_proc.getHeight(); j++){
				int newpixel = (int)Math.sqrt(roby_proc.get(i, j)*roby_proc.get(i, j) + robx_proc.get(i, j)*robx_proc.get(i, j));
				rob.set(i, j, newpixel);
			}
		}
		ImagePlus rob_edges = new ImagePlus("west052", rob);
		rob_edges.show();

// Task 6
// the west side of the image is the same scale, scaling only affects the east side

// Task 7

		ImageProcessor westside = cropped.getProcessor().duplicate();
		westside.setRoi(0, 0, 266, 338);
		ImagePlus west07 = new ImagePlus("west07", westside.crop());
		west07.show();

// Task 8 - is implemented via Adjus Histogram and Combined via changing the paths and the names
// This is the case of normalized one
		// ImageProcessor eq = west07.getProcessor().duplicate();
		// int M = eq.getWidth();
		// int N = eq.getHeight();
		// int K = 256; // number of intensity values

		//  // compute the cumulative histogram:
		//  int[] H = eq.getHistogram();
		 
		//  for (int j = 1; j < H.length; j++) {
		//  	H[j] = H[j - 1] + H[j];
		//  }

		//  // equalize the image:
		//  for (int v = 0; v < N; v++) {
		//  for (int u = 0; u < M; u++) {
		// 	 int a = eq.get(u, v);
		// 	 int b = H[a] * (K - 1) / (M * N); // s. Equation (4.12)
		// 	 eq.set(u, v, b);
		// 	}
 	// 	}
 	// 	ImagePlus west08 = new ImagePlus("west08", eq);
		// west08.show();
	}

}
