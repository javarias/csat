package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;

import Hevelius.weather.*;

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
	private JPanel tp;
	private GLCanvas canvas;
	private Listener list;

	private static Configuration test = new Configuration();
	private JLabel coor;
	private JLabel coor1L;
	private JTextField coor1;
	private JLabel coor2L;
	private JTextField coor2;
	private JButton go;
	private JButton change;
	private JLabel ccoor;
	private JLabel ccoor1L;
	private JLabel ccoor1C;
	private JLabel ccoor2L;
	private JLabel ccoor2C;
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
	private boolean VWeather;

	private CompassPanel cpane = null;
	private SystemPanel spane = null;
	private WeatherPanel wpane = null;
	private TelStatusPanel tspane = null;

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
		Image imag;
		imag = Toolkit.getDefaultToolkit().getImage(img);
		imag = Transparency.makeColorTransparent(imag, Color.BLACK);
		imag = imag.getScaledInstance(dim.width,dim.height,Image.SCALE_FAST);
		return imag;
	}
	public void setArrows(Dimension dim)
	{
		rArrow = Toolkit.getDefaultToolkit().getImage("Hevelius/images/rArrow.jpg");
		rArrow = Transparency.makeColorTransparent(rArrow, Color.BLACK);
		rArrow = rArrow.getScaledInstance(dim.width,dim.height,Image.SCALE_FAST);
		lArrow = Toolkit.getDefaultToolkit().getImage("Hevelius/images/lArrow.jpg");
		lArrow = Transparency.makeColorTransparent(lArrow, Color.BLACK);
		lArrow = lArrow.getScaledInstance(dim.width,dim.height,Image.SCALE_FAST);
		tArrow = Toolkit.getDefaultToolkit().getImage("Hevelius/images/tArrow.jpg");
		tArrow = Transparency.makeColorTransparent(tArrow, Color.BLACK);
		tArrow = tArrow.getScaledInstance(dim.width,dim.height,Image.SCALE_FAST);
		bArrow = Toolkit.getDefaultToolkit().getImage("Hevelius/images/bArrow.jpg");
		bArrow = Transparency.makeColorTransparent(bArrow, Color.BLACK);
		bArrow = bArrow.getScaledInstance(dim.width,dim.height,Image.SCALE_FAST);
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

		//Coordinate Label
		switch(Integer.parseInt(test.getOption("coordinate"))){
			case 0: coor = new JLabel("RaDec Coordinates"); break;
			case 1: coor = new JLabel("Horizontal Coordinates"); break;
			default: coor = new JLabel("RaDec Coordinates"); break;
		}
		coor.setSize(150,20);
		coor.setForeground(Color.WHITE);
		coor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		add(coor);

		//RA or ALT Label
		switch(Integer.parseInt(test.getOption("coordinate"))){
			case 0: coor1L = new JLabel("RA"); break;
			case 1: coor1L = new JLabel("Alt"); break;
			default: coor1L = new JLabel("RA"); break;
		}
		coor1L.setSize(30,20);
		coor1L.setForeground(Color.WHITE);
		add(coor1L);

		//RA or ALT Coordinate
		coor1 = new JTextField("0");
		coor1.setSize(80,20);
		add(coor1);

		//DEC or AZ Label
		switch(Integer.parseInt(test.getOption("coordinate"))){
			case 0: coor2L = new JLabel("Dec"); break;
			case 1: coor2L = new JLabel("Az"); break;
			default: coor2L = new JLabel("Dec"); break;
		}
		coor2L.setSize(30,20);
		coor2L.setForeground(Color.WHITE);
		add(coor2L);

		//DEC or AZ Coordinate
		coor2 = new JTextField("0");
		coor2.setSize(80,20);
		add(coor2);

		//Goto
		go = new JButton("Go");
		go.setSize(40,20);
		go.setMargin(new Insets(0,0,0,0));
		add(go);

		//Change RA-DEC <-> ALT-AZ
		change = new JButton("x");
		change.setSize(20,20);
		change.setMargin(new Insets(0,0,0,0));
		add(change);

		//Current coordinates
		switch(Integer.parseInt(test.getOption("coordinate"))){
			case 0: ccoor = new JLabel("RaDec Coordinates"); break;
			case 1: ccoor = new JLabel("Horizontal Coordinates"); break;
			default: ccoor = new JLabel("RaDec Coordinates"); break;
		}
		ccoor.setSize(150,20);
		ccoor.setForeground(Color.WHITE);
		ccoor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		add(ccoor);

		//Current RA or ALT Label
		switch(Integer.parseInt(test.getOption("coordinate"))){
			case 0: ccoor1L = new JLabel("RA"); break;
			case 1: ccoor1L = new JLabel("Alt"); break;
			default: ccoor1L = new JLabel("RA"); break;
		}
		ccoor1L.setSize(30,20);
		ccoor1L.setForeground(Color.WHITE);
		add(ccoor1L);

		//Current RA or ALT Coordinate
		ccoor1C = new JLabel("0.00000");
		ccoor1C.setSize(80,20);
		ccoor1C.setForeground(Color.WHITE);
		add(ccoor1C);

		//Current DEC or AZ Label
		switch(Integer.parseInt(test.getOption("coordinate"))){
			case 0: ccoor2L = new JLabel("Dec"); break;
			case 1: ccoor2L = new JLabel("Az"); break;
			default: ccoor2L = new JLabel("Dec"); break;
		}
		ccoor2L.setSize(30,20);
		ccoor2L.setForeground(Color.WHITE);
		add(ccoor2L);

		//Current DEC or AZ Coordinate
		ccoor2C = new JLabel("0.00000");
		ccoor2C.setSize(80,20);
		ccoor2C.setForeground(Color.WHITE);
		add(ccoor2C);
		
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

		//Telescope Status Panel
		tspane = new TelStatusPanel(null);
		add(tspane);

	//	wpane.autoReloadWeather().start();

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
			img = setImage("Hevelius/images/image.jpg",new Dimension((rect_x-10)*3/4,(rect_y-10)*3/4));
			stop = setImage("Hevelius/images/stop.png",new Dimension(80,80));
			hevelius = setImage("Hevelius/images/heveliusi.png",new Dimension(200,100));
			tam = new Dimension(dx,dy);
		}
		g.drawImage(stop, rect_x-40,dy - 140, this);
		g.drawImage(img,(dx-rect_x*3/4)/2+5,(dy-rect_y*3/4)/2+5,this);
		g.drawImage(hevelius,dx/2-100,40,this);

		tp.setLocation(oGLx+25,15);
		tp.setSize(dx-oGLx-50,dx-oGLx-50);
		canvas.setSize(dx-oGLx-50,dx-oGLx-50);
		cpane.setSize(((dy-rect_y)/2+50)*3/4,((dy-rect_y)/2+50)*3/4);
		cpane.setLocation(5,dy-cpane.getSize().height-50);

		spane.setSize(120,120);
		spane.setLocation(dx-120,dy-160);

		wpane.setSize(300,150);
		wpane.setLocation(0,0);

		tspane.setSize(300,200);
		tspane.setLocation(20,230);

		stimeL.setLocation(60,400);
		stime.setLocation(80,420);

		//Interface Objects Positioning
		coor.setLocation(dist-30,420);

		coor1L.setLocation(dist-10,450);

		coor1.setLocation(dist+20,450);

		coor2L.setLocation(dist-10,470);

		coor2.setLocation(dist + 20,470);

		go.setLocation(dist + 55,492);

		change.setLocation(dist-5,492);

		ccoor.setLocation(dist-30,240);

		ccoor1L.setLocation(dist-10,270);

		ccoor1C.setLocation(dist + 20,270);

		ccoor2L.setLocation(dist-10, 290);

		ccoor2C.setLocation(dist + 20,290);
		
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
			case 0: coor.setText("RaDec Coordinates"); 
				coor1L.setText("Ra");
				coor2L.setText("Dec");
				ccoor.setText("RaDec Coordinates");
				ccoor1L.setText("Ra");
				ccoor2L.setText("Dec");
				break;
			case 1: coor.setText("Horizontal Coordinates");  
				coor1L.setText("Alt");
				coor2L.setText("Az");
				ccoor.setText("Horizontal Coordinates");
				ccoor1L.setText("Alt");
				ccoor2L.setText("Az");
				break;
			default: coor.setText("RaDec Coordinates");  
				coor1L.setText("Ra");
				coor2L.setText("Dec");
				ccoor.setText("RaDec Coordinates");
				ccoor1L.setText("Ra");
				ccoor2L.setText("Dec");
				break;
		}

		//Weather
		
		wpane.reloadWeather();

		switch(Integer.parseInt(test.getOption("weather"))){
			case 0: wpane.setVisible(false);
				break;
			case 1: wpane.setVisible(true);
				break;
			default:wpane.setVisible(true);
				break;
		}

		//Compass
		switch(Integer.parseInt(test.getOption("compass"))){
			case 0: cpane.setVisible(false);
				break;
			case 1: cpane.setVisible(true);
				break;
			default:cpane.setVisible(true);
				break;
		}

		//OpenGL
		switch(Integer.parseInt(test.getOption("opengl"))){
			case 0: VTelescope = false;
				tp.setVisible(false);
				break;
			case 1: VTelescope = true;
				tp.setVisible(true);
				break;
			default:VTelescope = true; 
				tp.setVisible(true);
				break;
		}		
		paintImmediately(0,0,getSize().width,getSize().height);
	}
	public CompassPanel getPanel()
	{
		return cpane;
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
