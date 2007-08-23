package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Image;

import java.io.*;
import javax.imageio.*;

import Hevelius.heveliusmodules.*;
import Hevelius.acsmodules.*;

import alma.acs.exceptions.AcsJException;
import alma.TYPES.*;
import alma.ACS.CBDescIn;
import alma.ACS.CBvoid;
import alma.acs.callbacks.ResponseReceiver;
import Hevelius.utilities.converter.*;
import Hevelius.utilities.sideralupdate.*;
import Hevelius.interfaz.interfaz;

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
	private JButton rbutton;
	private JButton lbutton;
	private JButton tbutton;
	private JButton bbutton;
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

	private Tracking trck = null;
	private SideralUpdate sdrl = null;

	private CSATControlClient csatc = null;
	private CSATStatusClient csats = null;

	public DrawingPanel(LayoutManager l)
	{
		super(l);
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
	/*private void setArrows(Dimension dim)
	  {

	//ImageIcon rArrow = new ImageIcon("../images/rArrow.jpg");
	//rbutton = new JButton(rArrow);
	//rbutton.setBackground(Color.WHITE);
	//rbutton.setSize(200,200);
	//rbutton.setLocation();
	//add(rbutton);

	//ImageIcon lArrow = new ImageIcon("../images/lArrow.jpg");
	//lbutton = new JButton(lArrow);
	//lbutton.setBackground(Color.WHITE);
	//lbutton.setSize(50,20);
	//add(lbutton);

	//ImageIcon tArrow = new ImageIcon("../images/tArrow.jpg");
	//tbutton = new JButton(tArrow);
	tbutton.setBackground(Color.WHITE);
	tbutton.setSize(50,20);
	add(tbutton);

	ImageIcon bArrow = new ImageIcon("../images/bArrow.jpg");
	bbutton = new JButton(bArrow);
	bbutton.setBackground(Color.WHITE);
	bbutton.setSize(50,20);
	add(bbutton);

	rArrow = setImage("Hevelius/images/rArrow.jpg",dim);
	lArrow = setImage("Hevelius/images/lArrow.jpg",dim);
	tArrow = setImage("Hevelius/images/tArrow.jpg",dim);
	bArrow = setImage("Hevelius/images/bArrow.jpg",dim);
	}*/
	public void init()
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
		//setArrows(new Dimension(40,40));
		img = setImage("Hevelius/images/image.jpg",new Dimension(200,200));

		/*ImageIcon rarrow = new ImageIcon("../images/rArrow.jpg");
		  JButton rbutton = new JButton(rarrow);
		  rbutton.setBackground(Color.BLACK);

		  ImageIcon larrow = new ImageIcon("../images/lArrow.jpg");
		  JButton lbutton = new JButton(larrow);
		  lbutton.setBackground(Color.BLACK);

		  ImageIcon tarrow = new ImageIcon("../images/tArrow.jpg");
		  JButton tbutton = new JButton(tarrow);
		  tbutton.setBackground(Color.BLACK);

		  ImageIcon barrow = new ImageIcon("../images/bArrow.jpg");
		  JButton bbutton = new JButton(barrow);
		  bbutton.setBackground(Color.BLACK);



		 */
		//Go to Zenith Button
		zenith = new JButton("Zenith");
		zenith.setSize(100,20);
		add(zenith);

		//Go to R Button
		ImageIcon rArrow = new ImageIcon(new ImageIcon("Hevelius/images/rArrow.jpg").getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH));
		rbutton = new JButton(rArrow);
		rbutton.setBackground(Color.WHITE);
		rbutton.setSize(50,50);
		//rbutton.setLocation();
		add(rbutton);

		rbutton.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                                interfaz.getDrawingPanel().getCSATControl().AzimuthOffSet(1.0d);
                        }
                });
		

		//Go to L Button
		ImageIcon lArrow = new ImageIcon(new ImageIcon("Hevelius/images/lArrow.jpg").getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH));
		lbutton = new JButton(lArrow);
		lbutton.setBackground(Color.WHITE);
		lbutton.setSize(50,50);
		add(lbutton);

		lbutton.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
				if(interfaz.getDrawingPanel().getCSATControl()==null)
					System.out.println("Alo");
                                interfaz.getDrawingPanel().getCSATControl().AzimuthOffSet(-1.0d);
                        }
                });


		//Go to T Botton
		ImageIcon tArrow = new ImageIcon(new ImageIcon("Hevelius/images/tArrow.jpg").getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH));
		tbutton = new JButton(tArrow);
		tbutton.setBackground(Color.WHITE);
		tbutton.setSize(50,50);
		add(tbutton);

		tbutton.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                                interfaz.getDrawingPanel().getCSATControl().AltitudeOffSet(1.0d);
                        }
                });

		//Go to B Button
		ImageIcon bArrow = new ImageIcon(new ImageIcon("Hevelius/images/bArrow.jpg").getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH));
		bbutton = new JButton(bArrow);
		bbutton.setBackground(Color.WHITE);
		bbutton.setSize(50,50);
		add(bbutton);

		bbutton.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                                interfaz.getDrawingPanel().getCSATControl().AltitudeOffSet(-1.0d);
                        }
                });

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

		//CoordinatesPanel
		coorpane = new CoordinatesPanel(null);
		coorpane.init();
		switch(Integer.parseInt(test.getOption("coordinate"))){
			case 0: coorpane.initCoorType(false); break;
			case 1: coorpane.initCoorType(true); break;
			default:coorpane.initCoorType(false); break;
		}
		add(coorpane);

		//CompassPanel
		cpane = new CompassPanel(null);
		cpane.init();
		add(cpane);

		//SystemPanel
		spane = new SystemPanel(null);
		spane.init();
		add(spane);

		//WeatherPanel
		wpane = new WeatherPanel(null);
		wpane.init();
		add(wpane);
		new Thread(wpane).start();

		//Telescope Status Panel
		tspane = new TelStatusPanel(null);
		tspane.init();
		add(tspane);

		//ScreenPanel
		scpane = new ScreenPanel(null);
		scpane.init();
		add(scpane);
		new Thread(scpane).start();

		//VirtualTelescopePanel
		vtpane = new VirtualTelescopePanel(null);
		vtpane.init();
		add(vtpane);

		//TrackingModule
		trck = new Tracking();
		trck.setACSTracking(false);
		trck.setTrackingState(true);

		//SideralUpdate
		sdrl = new SideralUpdate();
                //sdrl.init();
                new Thread(sdrl).start();
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
			stop = setImage("Hevelius/images/stop.png",new Dimension(80,80));
			hevelius = setImage("Hevelius/images/heveliusi.png",new Dimension(200,100));
			tam = new Dimension(dx,dy);
		}
		g.drawImage(stop, rect_x-40,dy - 140, this);
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

		//rbutton.setSize(200,200);

		// Positions buttons
		rbutton.setLocation(((dx+rect_x*3/4)/2+0)+12, dy/2-20);
		lbutton.setLocation(((dx-rect_x*3/4)/2-40)-22, dy/2-20);

		tbutton.setLocation(dx/2-20, ((dy-rect_y*3/4)/2-40)-22);
		bbutton.setLocation(dx/2-20, ((dy+rect_y*3/4)/2+0)+12);



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
	public void updateWindow(boolean init){
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
		if(!init){
			switch(Integer.parseInt(test.getOption("coordinate"))){
				case 0: coorpane.setCoorType(1==0); break;
				case 1: coorpane.setCoorType(1==1); break;
				default:coorpane.setCoorType(1==0); break;
			}
		}
		else{
			switch(Integer.parseInt(test.getOption("coordinate"))){
                                case 0: coorpane.initCoorType(1==0); break;
                                case 1: coorpane.initCoorType(1==1); break;
                                default:coorpane.initCoorType(1==0); break;
                        }
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

		//Tracking
		switch(Integer.parseInt(test.getOption("tracking"))){
                        case 0: interfaz.getDrawingPanel().getCSATControl().setTrackingStatus(false); break;
                        case 1: interfaz.getDrawingPanel().getCSATControl().setTrackingStatus(true); break;
                        default: interfaz.getDrawingPanel().getCSATControl().setTrackingStatus(true); break;
                }

	}

	public static Configuration getConfig()
	{
		return test;
	}

	public CompassPanel getCompassPanel()
	{
		return cpane;
	}

	public SystemPanel getSystemPanel()
	{
		return spane;
	}

	public WeatherPanel getWeatherPanel()
	{
		return wpane;
	}

	public TelStatusPanel getTelStatusPanel()
	{
		return tspane;
	}

	public ScreenPanel getScreenPanel()
	{
		return scpane;
	}

	public CoordinatesPanel getCoordinatesPanel()
	{
		return coorpane;
	}

	public VirtualTelescopePanel getVirtualTelescopePanel()
	{
		return vtpane;
	}

	public CSATControlClient getCSATControl()
	{
		return csatc;
	}

	public CSATStatusClient getCSATStatus()
	{
		return csats;
	}

	public void setCSATControl(CSATControlClient csatc)
	{
		this.csatc = csatc;
	}

	public void setCSATStatus(CSATStatusClient csats)
	{
		this.csats = csats;
	}
}
