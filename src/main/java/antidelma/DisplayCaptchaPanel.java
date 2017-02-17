package antidelma;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DisplayCaptchaPanel extends JPanel {

	private Image image;

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(154,35);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor( Color.white );
		g.fillRect(0, 0, getWidth(), getHeight());
		if (image != null && image.getWidth(null)>0 ) {
			BufferedImage bImage = new BufferedImage( image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB );
			bImage.getGraphics().drawImage(image, 0, 0, null);
//			for (int x = 0;x<bImage.getWidth();x++) {
//				for (int y = 0;y<bImage.getHeight();y++) {
//					int rgb = bImage.getRGB(x, y);
//					int r_ = (rgb >> 16) & 0xFF;
//					int g_ = (rgb >> 8) & 0xFF;
//					int b_ = (rgb & 0xFF);
//					if ((r_ > 240) && (g_ > 150) & (b_ < 110)) {
//						bImage.setRGB(x, y, 0xff000000);
//					} else {
//						System.out.println( "r ="+r_+" g_ = "+g_+" b_ = "+b_ );
//					}
//				}				
//			}
			g.drawImage(bImage, 0, 0, null);
		}
	}

	public void setImage(Image image) {
		this.image = image;
		repaint();
	}

	public Image getImage() {
		return image;
	}

	public void method() {
		for (int x=0;x<1;x++) {
			int z = 0;
			z = 1;
			z = x >> 8;
		z = 2;
		}
	}

}
