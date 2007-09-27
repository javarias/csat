package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;

import Hevelius.virtualview.*;
import alma.TYPES.*;

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
		buf = null;
		screen = null;
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int dx, dy;
		dx = getSize().width;
		dy = getSize().height;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,dx,dy);
		g.setColor(Color.BLACK);
		g.fillRect(2,2,dx-4,dy-4);
		screen = buf;
		if(screen != null)
			g.drawImage(screen,2,2,this);
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
				System.out.println("Error in ScreenPanel's Thread");
			}
		}
	}
	private void setScreen()
	{
		if(telescopeState)
		{
			//Code for obtaining image from ACS.
			ImageHolder imgh = null;
			interfaz.getDrawingPanel().getCSATControl().getPreviewImage(imgh);
			//buf = imgh.value;
		}
		else
		{
			buf = getScreenFromWeb();
		}
		if(getGraphics()!=null)
			paintComponent(getGraphics());
		else
			setScreen();
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
			x = (getSize().width-4)/35.31f;
			y = (getSize().height-4)/35.31f;
			buffer = VirtualTelescope.getScreen(RA,DEC,x,y);
			paintComponent(getGraphics());
			return buffer;
		}
		catch(NullPointerException e)
		{
			//System.out.println("There is a Null pointer being used.");
		}
		return null;
	}
	private Image getScreenFromCCD()
	{
		return null;
	}
}
