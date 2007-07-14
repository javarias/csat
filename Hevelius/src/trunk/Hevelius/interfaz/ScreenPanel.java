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
	public void init()
	{
		screen = null;
		RAf = 0.0f;
		DECf = 0.0f;
		x = 10;
		y = 6.5f;
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		screen = buf;
		if(screen != null)
			g.drawImage(screen,0,0,this);
	}
	public void setParameters(float RA, float DEC)
	{
		this.RAf = RA;
		this.DECf = DEC;
	}
	public void run()
	{
		String RA, DEC;
		int hours, minutes;
		float seconds;
		while(true)
		{
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
				buf = VirtualTelescope.getScreen(RA,DEC,x,y);
				paintComponent(getGraphics());
				Thread.sleep(3000);
			}
			catch(InterruptedException e)
			{
				System.out.println("Error en Thread del ScreenPanel");
			}
			catch(NullPointerException e)
			{
			}
		}
	}
}
