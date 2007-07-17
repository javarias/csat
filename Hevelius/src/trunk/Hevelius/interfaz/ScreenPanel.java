package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;

import Hevelius.virtualview.*;

public class ScreenPanel extends JPanel implements Runnable
{
	private float x, y, RAf,DECf;
	private Image screen, buf;
	public ScreenPanel(LayoutManager l)
	{
		super(l);
		init();
	}
	private void init()
	{
		screen = null;
		RAf = 0.0f;
		DECf = 0.0f;
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		screen = buf;
		if(screen != null)
			g.drawImage(screen,0,0,this);
	}
	public void setCoordsRaDec(float RA, float DEC)
	{
		RAf = RA;
		DECf = DEC;
	}
	public void run()
	{
		while(true)
		{
			try
			{
				setScreen();
				Thread.sleep(3000);
			}
			catch(InterruptedException e)
			{
				System.out.println("Error en Thread del ScreenPanel");
			}
		}
	}
	private void setScreen()
	{
		buf = getScreenFromWeb();
		paintComponent(getGraphics());
	}
	private Image getScreenFromWeb()
	{
		String RA, DEC;
		Image buffer;
		int hours, minutes;
		float seconds;
		try
		{
			hours = (int)RAf;
			minutes = (int)((RAf-hours)*60);
			seconds = ((RAf-hours)*60-minutes)*60;
			RA = new String(hours+"+"+minutes+"+"+seconds);
			hours = (int)DECf;
			minutes = Math.abs((int)((DECf-hours)*60));
			seconds = Math.abs(((DECf-hours)*60-minutes)*60);
			DEC = new String(hours+"+"+minutes+"+"+seconds);
			RAf = RAf+0.0002f;
			x = getSize().width/35.31f;
			y = getSize().height/35.31f;
			buffer = VirtualTelescope.getScreen(RA,DEC,x,y);
			paintComponent(getGraphics());
			return buffer;
		}
		catch(NullPointerException e)
		{
		}
		return null;
	}
	private Image getScreenFromCCD()
	{
		return null;
	}
}
