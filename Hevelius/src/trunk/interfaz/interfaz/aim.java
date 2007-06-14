import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class aim extends JComponent {
	private Icon icon;
	int pad;
	int preferredNumImages = 1;
	private Rectangle iconRect = new Rectangle();
	private Rectangle clipRect = new Rectangle();
	public aim() 
	{
		this.icon = icon;
		this.pad = 5;
		//Set a reasonable default border.
		if (this.pad > 0) 
		{
			setBorder(BorderFactory.createEmptyBorder(this.pad, this.pad, this.pad,	this.pad));
		}
	}

	/*public Dimension getPreferredSize() 
	{
		if (icon != null) {
			Insets insets = getInsets();
			return new Dimension(icon.getIconWidth() 
					* preferredNumImages
					+ pad
					* (preferredNumImages - 1)
					+ insets.left
					+ insets.right,
					icon.getIconHeight() 
					+ insets.top
					+ insets.bottom);
		} else {
			return new Dimension(200, 200);
		}
	}*/

	public Dimension getMinimumSize() {
		if (icon != null) {
			Insets insets = getInsets();

			//Return enough space for one icon.
			return new Dimension(icon.getIconWidth()
					+ insets.left
					+ insets.right,
					icon.getIconHeight() 
					+ insets.top
					+ insets.bottom);
		} else {
			return new Dimension(0,0);
		}
	}

	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = aim.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	protected void paintComponent(Graphics g) {
		if (isOpaque()) { //paint background
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		ImageIcon circle = createImageIcon("circle.jpg");
		Icon icon = circle;

		if (icon != null) {
			//Draw the icon over and over, right aligned.
			Insets insets = getInsets();
			int iconWidth = icon.getIconWidth();
			int iconX = 0;//getWidth() - insets.right - iconWidth+100;
			int iconY = insets.top;
			boolean faded = false;
			//We won't bother painting icons that are clipped.
			Graphics2D g2d = (Graphics2D)g.create();
			g.getClipBounds(clipRect);
			icon.paintIcon(this, g2d, iconX, iconY);
			g2d.dispose(); //clean up
		}
	}
}
