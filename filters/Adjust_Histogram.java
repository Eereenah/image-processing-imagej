import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;
import ij.plugin.filter.*;
import ij.io.*;

public class Adjust_Histogram implements PlugIn {

	public void run(String arg) {
		Opener opener = new Opener();  
		String imageeast = "C:/Users/User/Desktop/east07.jpg";
		String imagewest = "C:/Users/User/Desktop/west07.jpg";
		ImagePlus impe = opener.openImage(imageeast);
		ImagePlus impw = opener.openImage(imagewest);

		ImageProcessor ipe = impe.getProcessor();
		ImageProcessor ipw = impw.getProcessor();

		 // compute the cumulative histogram:
		 int[] cumulHe = ipe.getHistogram();
		 int[] cumulHw = ipw.getHistogram();
		 
		 for (int j = 1; j < cumulHe.length; j++) {
		 	cumulHe[j] = cumulHe[j - 1] + cumulHe[j];
		 }

		 for (int i = 1; i < cumulHw.length; i++) {
		 	cumulHw[i] = cumulHw[i - 1] + cumulHw[i];
		 }

		 // compute average histogram
		 int[] avgH = ipe.getHistogram();
		 for(int h = 0; h < avgH.length; h++){
		 	avgH[h] = (int)((cumulHw[h] + cumulHe[h])/2);
		 }

		 for(int u = 0; u < ipe.getWidth(); u++){
		 	for(int v = 0; v < ipe.getHeight(); v++){
		 		ipe.set(u, v, avgH[ipe.get(u, v)]*255/(ipe.getHeight()*ipe.getWidth()));
		 	}
		 }
		ImagePlus east08 = new ImagePlus("east08", ipe);
		east08.show();

		 for(int u = 0; u < ipw.getWidth(); u++){
		 	for(int v = 0; v < ipw.getHeight(); v++){
		 		ipw.set(u, v, avgH[ipw.get(u, v)]*255/(ipw.getHeight()*ipw.getWidth()));
		 	}
		 }
		ImagePlus west08 = new ImagePlus("west08", ipw);
		west08.show();
	}

}
