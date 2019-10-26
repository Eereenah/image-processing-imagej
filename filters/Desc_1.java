import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;
import ij.plugin.filter.*;
import imagingbook.lib.image.ImageMath;
import imagingbook.lib.image.Filter;
import imagingbook.lib.image.*;

public class Desc_1 implements PlugIn{

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

// This is a copy of an already implemented code from the book
// The jar library is also downloaded from there and will be included
// in the final submission

		ImageProcessor cornerp = proc.duplicate();
		FloatProcessor Ix = cornerp.convertToFloatProcessor(); 
		FloatProcessor Iy = cornerp.convertToFloatProcessor();
		int width = cornerp.getWidth();
		int height = cornerp.getHeight();

		float[] hp = {2f/9, 5f/9, 2f/9};
		float[] hd = {0.5f, 0, -0.5f};
		float[] hb = {1f/64, 6f/64, 15f/64, 20f/64, 15f/64, 6f/64, 1f/64};
		
		Filter.convolveX(Ix, hp);				// pre-filter Ix horizontally
		Filter.convolveX(Ix, hd);				// get horizontal derivative 
		
		Filter.convolveY(Iy, hp);				// pre-filter Iy vertically
		Filter.convolveY(Iy, hd);				// get vertical derivative
		
		FloatProcessor a = ImageMath.sqr(Ix);					// A <- Ix^2
		FloatProcessor b = ImageMath.sqr(Iy);					// B <- Iy^2
		FloatProcessor c = ImageMath.mult(Ix, Iy);				// C <- Ix * Iy
		
		Filter.convolveXY(a, hb);				// blur A in x/y
		Filter.convolveXY(b, hb);				// blur B in x/y
		Filter.convolveXY(c, hb);               // blur C in x/y

		FloatProcessor fp1 = makeCrf(0.01f, width, height, a, b, c);
		ImagePlus fp1a = new ImagePlus("desc021", fp1);
		fp1a.show();

		FloatProcessor fp2 = makeCrf(0.05f, width, height, a, b, c);
		ImagePlus fp2a = new ImagePlus("desc022", fp2);		
		fp2a.show();

		FloatProcessor fp3 = makeCrf(0.005f, width, height, a, b, c);
		ImagePlus fp3a = new ImagePlus("desc023", fp3);
		fp3a.show();

// Task 3
// Since this image is the smaller one, it doesn't need to be scaled
// Anyway, both methods are implemented in Asc part of the code

// Task 4
		ImageProcessor drawp = proc.duplicate();
		drawp.setLineWidth(1);
		drawp.setColor(Color.white);
		drawp.drawLine(284, 0, 284, 570);
		ImagePlus whiteline = new ImagePlus("desc04", drawp);
		whiteline.show();

// Task 5
		ImageProcessor descside = whiteline.getProcessor().duplicate();
		descside.setRoi(285, 0, 282, 570);
		ImagePlus desc05 = new ImagePlus("desc05", descside.crop());
		desc05.show();
	}

		private FloatProcessor makeCrf(float alpha, int M, int N, FloatProcessor a, FloatProcessor b, FloatProcessor c) { //corner response function (CRF)
		FloatProcessor q = new FloatProcessor(M, N);
		final float[] pA = (float[]) a.getPixels();
		final float[] pB = (float[]) b.getPixels();
		final float[] pC = (float[]) c.getPixels();
		final float[] pQ = (float[]) q.getPixels();
		for (int i = 0; i < M * N; i++) {
			float aa = pA[i];
			float bb = pB[i];
			float cc = pC[i];
			float det = aa * bb - cc * cc;
			float trace = aa + bb;
			pQ[i] = det - alpha * (trace * trace);
		}
		return q;
// The rest of the task is implemented in Adjust_Histogram and Combine scripts
// For running them change the path and 

}
}