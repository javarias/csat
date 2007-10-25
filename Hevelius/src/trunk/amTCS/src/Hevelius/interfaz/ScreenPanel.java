package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import java.util.*;

import Hevelius.virtualview.*;
import alma.TYPES.*;
import javax.imageio.*;
import java.io.*;
//import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.awt.image.WritableRaster;

public class ScreenPanel extends JPanel implements Runnable
{
	private Image screen, buf;
	private boolean telescopeState = false;
	private boolean working = false;
	private int dx, dy;
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
		boolean update = false;
		super.paintComponent(g);
		if(dx == getSize().width || dy == getSize().height)
			update = true;
		dx = getSize().width;
		dy = getSize().height;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,dx,dy);
		g.setColor(Color.BLACK);
		g.fillRect(2,2,dx-4,dy-4);
		if(buf!=null)
		{
			buf = buf.getScaledInstance(dx-4,dy-4,Image.SCALE_SMOOTH);
			screen = buf;
			buf = null;
		}
		if(screen != null)
		{
			//if(update)
			//	screen = screen.getScaledInstance(dx-4,dy-4,Image.SCALE_SMOOTH);
			g.drawImage(screen,2,2,this);
		}
	}
	public void run()
	{
		while(true)
		{
			try
			{
				if(interfaz.getDrawingPanel().getCSATControl() != null && interfaz.getDrawingPanel().getCSATStatus() != null)
				{
					working = true;
					setScreen();
					working = false;
				}
				Thread.sleep(2000);
			}
			catch(InterruptedException e)
			{
				System.out.println("Error in ScreenPanel's Thread");
			}
		}
	}
	private void setScreen()
	{
		if(buf == null)
		{
			if(telescopeState)
				buf = getScreenFromCCD();
			else
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
		int[] img = new int[921600];
		BufferedImage image = null;
		ImageHolder imgh = new ImageHolder();
		if(interfaz.getDrawingPanel().getCSATControl()!=null)
		{
			try
			{
				interfaz.getDrawingPanel().getCSATControl().getPreviewImage(imgh);
				image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
			}
			catch(Exception e)
			{
			}
			if(imgh.value!=null)
			{
				for(int i=0; i!=640*480; i++)
				{
					int r = imgh.value[3*i];
					int g = imgh.value[3*i+1];
					int b = imgh.value[3*i+2];
					img[i] = (0<<24) + (r<<16) + (g<<8) + (b);
				}
				image.setRGB(0,0,640,480,img,0,640);
				return image;
			}
		}
		return null;
	}

	public boolean getThreadState()
	{
		return working;
	}
}
