package antidelma;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

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
		if (image != null) g.drawImage(image, 1, 1, null);
	}

	public void setImage(Image image) {
		this.image = image;
		repaint();
	}
	
	
	
}
