import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;
import ij.plugin.filter.*;
import ij.io.*;
public class Combine implements PlugIn{

	public void run(String arg) {

		//change 08 to 07 to use this for task 9

		Opener opener = new Opener();  
		String imageeast = "C:/Users/User/Desktop/east08.jpg";
		String imagewest = "C:/Users/User/Desktop/west08.jpg";
		ImagePlus impe = opener.openImage(imageeast);
		ImagePlus impw = opener.openImage(imagewest);

		ImageProcessor ipe = impe.getProcessor();
		ImageProcessor ipw = impw.getProcessor();
		int totalw = ipe.getWidth() + ipw.getWidth();
		int totalh = ipe.getHeight();
		ImagePlus combined = IJ.createImage("combined", totalw, totalh, 1, 8);
		ImageProcessor p = combined.getProcessor().duplicate();

		for(int i=0; i<ipw.getWidth(); i++){
			for(int j=0; j<ipw.getHeight(); j++){
				p.set(i, j, ipw.get(i, j));
			}
		}
		for(int k = 0; k<ipe.getWidth(); k++){
			for(int l = 0; l<totalh; l++){
				p.set(k+ipw.getWidth(), l, ipe.get(k, l));
			}
		}
		ImagePlus face10 = new ImagePlus("face10", p);
		face10.show();


	}
}