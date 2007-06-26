package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IntroPanel extends JPanel
{
	Image imag;
	JProgressBar bar;
	public IntroPanel(LayoutManager l)
	{
		super(l);
		imag = Toolkit.getDefaultToolkit().getImage("Hevelius/images/hevelius.png");
		imag = Transparency.makeColorTransparent(imag, Color.WHITE);
		imag = imag.getScaledInstance(200,100,Image.SCALE_FAST);
		bar = new JProgressBar();
		bar.setSize(150,20);
		bar.setIndeterminate(true);
		add(bar);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(imag,getSize().width/2-100,40,this);
		bar.setLocation(getSize().width/2-75,160);
	}
}
