package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;

import Hevelius.virtualview.*;

public class ScreenPanel extends JPanel implements Runnable
{
	private Image screen, buf;
	private boolean telescopeState = false;
	public ScreenPanel(LayoutManager l)
	{
		super(l);
	}
	public void init()
	{
		screen = null;
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		screen = buf;
		if(screen != null)
			g.drawImage(screen,0,0,this);
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
		if(telescopeState)
		{
			//Code for obtaining image from ACS.
		}
		else
		{
			buf = getScreenFromWeb();
		}
		paintComponent(getGraphics());
	}
	private Image getScreenFromWeb()
	{
		String RA, DEC;
		double RAd, DECd;
		Image buffer;
		int hours, minutes;
		double x, y;
		double seconds;
		RAd = interfaz.getDrawingPanel().getCoordinatesPanel().getRa();
		DECd = interfaz.getDrawingPanel().getCoordinatesPanel().getDec();
		try
		{
			hours = (int)RAd;
			minutes = (int)((RAd-hours)*60);
			seconds = ((RAd-hours)*60-minutes)*60;
			RA = new String(hours+"+"+minutes+"+"+seconds);
			hours = (int)DECd;
			minutes = Math.abs((int)((DECd-hours)*60));
			seconds = Math.abs(((DECd-hours)*60-minutes)*60);
			DEC = new String(hours+"+"+minutes+"+"+seconds);
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
