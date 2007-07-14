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
	public static int gearDisplayList;
	private Dimension dim;
	private Dimension tam;
	private Image img = null;
	private Image rArrow = null;
	private Image lArrow = null;
	private Image tArrow = null;
	private Image bArrow = null;
	private Image hevelius = null;
	private Image screen = null;
	private JPanel tp;
	private GLCanvas canvas;
	private Listener list;

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

	public DrawingPanel(LayoutManager l)
	{
		super(l);
		tp = new JPanel();
		tp.setLayout(null);
		add(tp);
		GLCapabilities capabilities = new GLCapabilities();
		canvas = new GLCanvas(capabilities);
		list = new Listener();
		canvas.addGLEventListener(list);
		canvas.setLocation(0,0);
		canvas.setSize(100,100);
		tp.add(canvas);

		JDialog dialog = new JDialog(interfaz.frame,"Telescopio");
		dialog.getContentPane().setLayout(null);
		GLCanvas canvas2 = new GLCanvas(null,null,canvas.getContext(),null);
		canvas2.setSize(600,600);
		canvas2.setLocation(0,0);
		canvas2.addGLEventListener(list);
		dialog.getContentPane().add(canvas2);
		dialog.pack();
		dialog.setSize(600,600);
		dialog.setLocation(100,100);
		dialog.setResizable(false);
		final Animator animator = new Animator(canvas);
		final Animator animator2 = new Animator(canvas2);
		list.setDialog(dialog,animator2);
		WindowListener windowListener = new WindowAdapter()
		{
			public void windowClosing(WindowEvent e) 
			{
				new Thread(new Runnable() 
						{
							public void run() 
				{
					animator.stop();
					System.exit(0);
				}
				}).start();
			}
		};
		WindowListener windowListener2 = new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				new Thread(new Runnable()
						{
							public void run()
				{
					animator2.stop();
				}
				}).start();
			}
		};
		dialog.addWindowListener(windowListener2);
		interfaz.frame.addWindowListener(windowListener);
		animator.start();
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

		tp.setLocation(oGLx+25,15);
		tp.setSize(dx-oGLx-50,dx-oGLx-50);
		canvas.setSize(dx-oGLx-50,dx-oGLx-50);
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
				tp.setVisible(false); break;
			case 1: VTelescope = true;
				tp.setVisible(true); break;
			default:VTelescope = true; 
				tp.setVisible(true); break;
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

	public static synchronized void initializeDisplayList(GL gl) {
		gearDisplayList = gl.glGenLists(1);
		gl.glNewList(gearDisplayList, GL.GL_COMPILE);
		float red[] = { 0.8f, 0.1f, 0.0f, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, red, 0);
		gear(gl, 1.0f, 4.0f, 1.0f, 20, 0.7f);
		gl.glEndList();
	}

	private static void gear(GL gl,
			float inner_radius,
			float outer_radius,
			float width,
			int teeth,
			float tooth_depth)
	{
		int i;
		float r0, r1, r2;
		float angle, da;
		float u, v, len;
		
		float red[] = { 0.8f, 0.1f, 0.0f, 1.0f };
		float blue[] = { 0.5f, 0.8f, 0.4f, 1.0f };

		r0 = inner_radius;
		r1 = outer_radius - tooth_depth / 2.0f;
		r2 = outer_radius + tooth_depth / 2.0f;

		da = 2.0f * (float) Math.PI / teeth / 4.0f;

		gl.glShadeModel(GL.GL_FLAT);

		gl.glNormal3f(0.0f, 0.0f, 1.0f);

		/* draw front face */
		gl.glBegin(GL.GL_QUAD_STRIP);
		for (i = 0; i <= teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
			if(i < teeth)
			{
				gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
				gl.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
			}
		}
		gl.glEnd();

		/* draw front sides of teeth */
		gl.glBegin(GL.GL_QUADS);
		for (i = 0; i < teeth; i++)
		{
			if(i==0)
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, blue, 0);
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2.0f * da), r2 * (float)Math.sin(angle + 2.0f * da), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
			if(i==0)
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, red, 0);
		}
		gl.glEnd();

		/* draw back face */
		gl.glBegin(GL.GL_QUAD_STRIP);
		for (i = 0; i <= teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), -width * 0.5f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
		}
		gl.glEnd();

		/* draw back sides of teeth */
		gl.glBegin(GL.GL_QUADS);
		for (i = 0; i < teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), -width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2 * da), r2 * (float)Math.sin(angle + 2 * da), -width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), -width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
		}
		gl.glEnd();

		/* draw outward faces of teeth */
		gl.glBegin(GL.GL_QUAD_STRIP);
		for (i = 0; i < teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
			u = r2 * (float)Math.cos(angle + da) - r1 * (float)Math.cos(angle);
			v = r2 * (float)Math.sin(angle + da) - r1 * (float)Math.sin(angle);
			len = (float)Math.sqrt(u * u + v * v);
			u /= len;
			v /= len;
			gl.glNormal3f(v, -u, 0.0f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), -width * 0.5f);
			gl.glNormal3f((float)Math.cos(angle), (float)Math.sin(angle), 0.0f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2 * da), r2 * (float)Math.sin(angle + 2 * da), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2 * da), r2 * (float)Math.sin(angle + 2 * da), -width * 0.5f);
			u = r1 * (float)Math.cos(angle + 3 * da) - r2 * (float)Math.cos(angle + 2 * da);
			v = r1 * (float)Math.sin(angle + 3 * da) - r2 * (float)Math.sin(angle + 2 * da);
			gl.glNormal3f(v, -u, 0.0f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), -width * 0.5f);
			gl.glNormal3f((float)Math.cos(angle), (float)Math.sin(angle), 0.0f);
		}
		gl.glVertex3f(r1 * (float)Math.cos(0), r1 * (float)Math.sin(0), width * 0.5f);
		gl.glVertex3f(r1 * (float)Math.cos(0), r1 * (float)Math.sin(0), -width * 0.5f);
		gl.glEnd();

		gl.glShadeModel(GL.GL_SMOOTH);

		/* draw inside radius cylinder */
		gl.glBegin(GL.GL_QUAD_STRIP);
		for (i = 0; i <= teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glNormal3f(-(float)Math.cos(angle), -(float)Math.sin(angle), 0.0f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
		}
		gl.glEnd();
	}
}
