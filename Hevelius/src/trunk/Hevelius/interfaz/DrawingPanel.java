package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;

import java.io.*;
import javax.imageio.*;

import Hevelius.weather.*;
import Hevelius.virtualview.*;

public class DrawingPanel extends JPanel
{
	//public static int gearDisplayList;
	private Dimension dim;
	private Dimension tam;
	private Image img = null;
	private Image rArrow = null;
	private Image lArrow = null;
	private Image tArrow = null;
	private Image bArrow = null;
	private Image hevelius = null;
	private Image screen = null;

	private static Configuration test = new Configuration();
	
	private Image stop=null;
	private JButton zenith;
	private JButton park;
	private JLabel stimeL;
	private JLabel stime;

	private int cx;
	private int cy;
	private int dist;
	private int dy;
	private int dx;
	private int rect_x;
	private int rect_y;
	private int r;

	private boolean VTelescope;

	private CompassPanel cpane = null;
	private SystemPanel spane = null;
	private WeatherPanel wpane = null;
	private TelStatusPanel tspane = null;
	private ScreenPanel scpane = null;
	private CoordinatesPanel coorpane = null;
	private VirtualTelescopePanel vtpane = null;

	public DrawingPanel(LayoutManager l)
	{
		super(l);
		init();
	}
	public Image setImage(String img, Dimension dim)
	{
		Image imag = null;
		try
		{
			imag = ImageIO.read(new File(img));
		}
		catch(IOException e)
		{
		}
		imag = Transparency.makeColorTransparent(imag, Color.BLACK);
		imag = imag.getScaledInstance(dim.width,dim.height,Image.SCALE_FAST);
		return imag;
	}
	public void setArrows(Dimension dim)
	{
		rArrow = setImage("Hevelius/images/rArrow.jpg",dim);
		lArrow = setImage("Hevelius/images/lArrow.jpg",dim);
		tArrow = setImage("Hevelius/images/tArrow.jpg",dim);
		bArrow = setImage("Hevelius/images/bArrow.jpg",dim);
	}
	private void init()
	{
		tam = new Dimension(0,0);
		dy = getSize().height;
		dx = getSize().width;
		rect_x = dx/2;
		rect_y = dy/2;
		dist = (3*dx + rect_x)/4 -40;
		cx = 30+(dy-rect_y)/4;
		cy = (dy-dy/3)+(dy-rect_y)/4-20;
		r = (dy-rect_y)/4;

		//Interface Initialization
		setArrows(new Dimension(40,40));
		img = setImage("Hevelius/images/image.jpg",new Dimension(200,200));

		//Go to Zenith Button
		zenith = new JButton("Zenith");
		zenith.setSize(100,20);
		add(zenith);

		//Go to Park Button
		park = new JButton("Park");
		park.setSize(100,20);
		add(park);

		//Sidereal Time Label
		stimeL = new JLabel("Sidereal Time");
		stimeL.setSize(120,20);
		stimeL.setForeground(Color.WHITE);
		add(stimeL);

		//Sidereal Time
		stime = new JLabel("0.000");
		stime.setSize(100,20);
		stime.setForeground(Color.WHITE);
		add(stime);

		//CompassPanel
		cpane = new CompassPanel(null);
		add(cpane);

		//SystemPanel
		spane = new SystemPanel(null);
		add(spane);

		//WeatherPanel
		wpane = new WeatherPanel(null);
		add(wpane);
		new Thread(wpane).start();

		//Telescope Status Panel
		tspane = new TelStatusPanel(null);
		add(tspane);

		//ScreenPanel
		scpane = new ScreenPanel(null);
		add(scpane);
		new Thread(scpane).start();

		//CoordinatesPanel
		coorpane = new CoordinatesPanel(null);
		switch(Integer.parseInt(test.getOption("coordinate"))){
			case 0:	coorpane.setCoorType(1==0); break;
			case 1: coorpane.setCoorType(1==1); break;
			default:coorpane.setCoorType(1==0); break;
		}
		add(coorpane);

		//VirtualTelescopePanel
		vtpane = new VirtualTelescopePanel(null);
		add(vtpane);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		dy = getSize().height;
		dx = getSize().width;
		rect_x = dx/2;
		rect_y = dy/2;
		cx = 30+(dy-rect_y)/4;
		cy = (dy-dy/3)+(dy-rect_y)/4-20;
		dist = (3*dx + rect_x)/4 -40;
		r = (dy-rect_y)/4;

		g.setColor(Color.GRAY);

		int oGLx = (dx-rect_x)/2+rect_x+20;
		if(VTelescope)
		{
			g.drawRect(oGLx+20,10,dx-oGLx-40,dx-oGLx-40);
			g.fillRect(oGLx+20,10,dx-oGLx-40,dx-oGLx-40);
		}

		g.drawRect((dx-rect_x*3/4)/2,(dy-rect_y*3/4)/2,rect_x*3/4,rect_y*3/4);
		g.fillRect((dx-rect_x*3/4)/2,(dy-rect_y*3/4)/2,rect_x*3/4,rect_y*3/4);

		g.setColor(Color.BLACK);

		g.fillRect((dx-rect_x*3/4)/2+5,(dy-rect_y*3/4)/2+5,rect_x*3/4-10,rect_y*3/4-10);
		if(VTelescope)
			g.fillRect(oGLx+25,15,dx-oGLx-50,dx-oGLx-50);

		g.drawImage(lArrow, (dx-rect_x*3/4)/2-40, dy/2-20,this);
		g.drawImage(rArrow, (dx+rect_x*3/4)/2+0, dy/2-20,this);
		g.drawImage(tArrow, dx/2-20, (dy-rect_y*3/4)/2-40,this);
		g.drawImage(bArrow, dx/2-20, (dy+rect_y*3/4)/2+0,this);
		if(tam.width != dx || tam.height != dy)
		{
			//img = setImage("Hevelius/images/image.jpg",new Dimension((rect_x-10)*3/4,(rect_y-10)*3/4));
			stop = setImage("Hevelius/images/stop.png",new Dimension(80,80));
			hevelius = setImage("Hevelius/images/heveliusi.png",new Dimension(200,100));
			tam = new Dimension(dx,dy);
		}
		g.drawImage(stop, rect_x-40,dy - 140, this);
		//g.drawImage(img,(dx-rect_x*3/4)/2+5,(dy-rect_y*3/4)/2+5,this);
		g.drawImage(hevelius,dx/2-100,40,this);

		vtpane.setLocation(oGLx+25,15);
		vtpane.setSize(dx-oGLx-50,dx-oGLx-50);
		cpane.setSize(((dy-rect_y)/2+50)*3/4,((dy-rect_y)/2+50)*3/4);
		cpane.setLocation(5,dy-cpane.getSize().height-50);

		spane.setSize(120,120);
		spane.setLocation(dx-120,dy-160);

		wpane.setSize(300,200);
		wpane.setLocation(0,0);

		tspane.setSize(300,200);
		tspane.setLocation(0,230);

		scpane.setSize(rect_x*3/4-10,rect_y*3/4-10);
		scpane.setLocation((dx-rect_x*3/4)/2+5,(dy-rect_y*3/4)/2+5);

		coorpane.setSize(200,250);
		coorpane.setLocation(oGLx+10,dx-oGLx);

		stimeL.setLocation(60,400);
		stime.setLocation(80,420);

		zenith.setLocation(rect_x*3/4-50,3*dy/4-20);

		park.setLocation(rect_x*5/4-50,3*dy/4-20);
	}
	public Dimension getDim()
	{
		return getSize();
	}
	public void setBackground(Color c)
	{
		super.setBackground(c);
		if(cpane!=null)
			cpane.setBackground(c);
		if(spane!=null)
			spane.setBackground(c);
		if(wpane!=null)
			wpane.setBackground(c);
		if(tspane!=null)
			tspane.setBackground(c);
		if(scpane!=null)
			scpane.setBackground(c);
		if(coorpane!=null)
			coorpane.setBackground(c);
		if(vtpane!=null)
			vtpane.setBackground(c);
	}
	public void updateWindow(){
		//Cambiar Background
		switch(Integer.parseInt(test.getOption("background"))){
			case 0: setBackground(Color.BLACK); break;
			case 1: setBackground(Color.BLUE); break;
			case 2: setBackground(Color.CYAN); break;
			case 3: setBackground(Color.DARK_GRAY); break;
			case 4: setBackground(Color.GRAY); break;
			case 5: setBackground(Color.GREEN); break;
			case 6: setBackground(Color.LIGHT_GRAY); break;
			case 7: setBackground(Color.MAGENTA); break;
			case 8: setBackground(Color.ORANGE); break;
			case 9: setBackground(Color.PINK); break;
			case 10: setBackground(Color.RED); break;
			case 11: setBackground(Color.YELLOW); break;
			default: setBackground(Color.BLACK); break;
		}

		//Modificar Tipos de Coordenadas
		switch(Integer.parseInt(test.getOption("coordinate"))){
			case 0: coorpane.setCoorType(1==0); break;
			case 1: coorpane.setCoorType(1==1); break;
			default:coorpane.setCoorType(1==0); break;
		}

		//Weather
		
		wpane.reloadWeather();

		switch(Integer.parseInt(test.getOption("weather"))){
			case 0: wpane.setVisible(false); break;
			case 1: wpane.setVisible(true); break;
			default:wpane.setVisible(true); break;
		}

		//Compass
		switch(Integer.parseInt(test.getOption("compass"))){
			case 0: cpane.setVisible(false); break;
			case 1: cpane.setVisible(true); break;
			default:cpane.setVisible(true); break;
		}

		//OpenGL
		switch(Integer.parseInt(test.getOption("opengl"))){
			case 0: VTelescope = false;
				vtpane.setVisible(false); break;
			case 1: VTelescope = true;
				vtpane.setVisible(true); break;
			default:VTelescope = true; 
				vtpane.setVisible(true); break;
		}		
		paintImmediately(0,0,getSize().width,getSize().height);
	}
	public CompassPanel getPanel()
	{
		return cpane;
	}

	public static Configuration getConfig()
	{
		return test;
	}
}
