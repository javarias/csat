package Hevelius.interfaz;

import java.io.*;
import javax.imageio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class IntroPanel extends JPanel
{
	Image imag = null;
	JProgressBar bar;
	public IntroPanel(LayoutManager l)
	{
		super(l);
		try
		{
			imag = ImageIO.read(getClass().getClassLoader().getResource("Hevelius/images/hevelius.png"));
		}
		catch(IOException e)
		{
			System.out.println("There was a problem retrieving an Image");
		}
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
