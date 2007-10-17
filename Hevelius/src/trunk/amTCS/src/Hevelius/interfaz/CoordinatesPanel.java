package Hevelius.interfaz;
import java.util.*;

//import Hevelius.utilities.converter.*;
import Hevelius.acsmodules.*;
import Hevelius.heveliusmodules.*;
import Hevelius.interfaz.*;
import Hevelius.utilities.converter.*;
import Hevelius.utilities.historial.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import alma.TYPES.*;

import java.io.*;
import javax.imageio.*;

public class CoordinatesPanel extends JPanel //implements Runnable
{
	//private Configuration test = DrawingPanel.getConfig();

	private JLabel coor;
	private JLabel coor1L;
	private JTextField coor1;
	private JLabel coor2L;
	private JTextField coor2;
	private JButton go;
	private JButton change;
	private JLabel ccoor;
	private JLabel radec;
	private JLabel horizontal;
	private JLabel ccoorR;
	private JLabel ccoorRL;
	private JLabel ccoorD;
	private JLabel ccoorDL;
	private JLabel ccoorAl;
	private JLabel ccoorAlL;
	private JLabel ccoorAz;
	private JLabel ccoorAzL;
	private JButton catalogue;

	//Agregando...
	private JButton zenith;
	private JButton park;
	private JButton tracking;
	/*private JButton rbutton = null;
	private JButton lbutton = null;
	private JButton tbutton = null;
	private JButton bbutton = null;
	private JButton stopbutton = null;*/

	private JLabel rbutton;
        private JLabel lbutton;
        private JLabel tbutton;
        private JLabel bbutton;
        private JLabel stopbutton;

	private JLabel laltoffset;
	private JLabel lazoffset;
	private JLabel altoffset;
	private JLabel azoffset;
	private JLabel offsetL;
	//

	private boolean coortype;
	private RadecPosHolder rdPos;
	private AltazPosHolder aaPos;

	private int dx = 0;
	private int dy = 0;

	private static Historial hist = new Historial();

	float osize = 0;
	
	//Listener list = interfaz.getDrawingPanel().getVirtualTelescopePanel().getListener();

	public CoordinatesPanel(LayoutManager l)
	{
		super(l);
	}

	public Image setImage(String img, Dimension dim)
        {
                Image imag = null;
                try
                {
                        imag = ImageIO.read(getClass().getClassLoader().getResource(img));
                }
                catch(IOException e)
                {
                }
                imag = Transparency.makeColorTransparent(imag, Color.BLACK);
                imag = imag.getScaledInstance(dim.width,dim.height,Image.SCALE_FAST);
                return imag;
        }

	public void init()
	{

		//Coordinate Label
		coor = new JLabel("");
		coor.setSize(150,20);
		coor.setForeground(Color.WHITE);
		coor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		add(coor);

		//RA or ALT Label
		coor1L = new JLabel("");
		coor1L.setSize(30,20);
		coor1L.setForeground(Color.WHITE);
		add(coor1L);

		//RA or ALT Coordinate
		coor1 = new JTextField("90");
		coor1.setSize(80,20);
		coor1.setText("0");
		add(coor1);

		//DEC or AZ Label
		coor2L = new JLabel("");
		coor2L.setSize(30,20);
		coor2L.setForeground(Color.WHITE);
		add(coor2L);

		//DEC or AZ Coordinate
		coor2 = new JTextField("0");
		coor2.setSize(80,20);
		coor2.setText("0");
		add(coor2);

		//Goto
		go = new JButton("Go");
		go.setSize(45,45);
		go.setMargin(new Insets(0,0,0,0));
		add(go);

		go.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
				Double c1, c2;
				//float tmp1, tmp2;
				Configuration test = new Configuration();
				//Listener list = new Listener();

				c1 = Double.parseDouble(coor1.getText());
				c2 = Double.parseDouble(coor2.getText());
				
				//tmp1=Float.parseFloat(coor1.getText());
				//tmp2=Float.parseFloat(coor2.getText());

				Presetting.preset(c1,c2,Integer.parseInt(test.getOption("coordinate")));
				//list.setAltAzDest(tmp1,tmp2);		
				if(interfaz.getDrawingPanel().getCSATControl()!=null)
					hist.addHistoryPreset(c1,c2,Integer.parseInt(test.getOption("coordinate")));
				}
				});

		//Current coordinates
		ccoor = new JLabel("Current Coordinates");
		ccoor.setSize(150,20);
		ccoor.setForeground(Color.WHITE);
		ccoor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		add(ccoor);

		//Horizontal Label
		horizontal = new JLabel("Horizontal");
		horizontal.setSize(80,20);
		horizontal.setForeground(Color.WHITE);
		add(horizontal);

		//RaDec Label
		radec = new JLabel("RaDec");
		radec.setSize(80,20);
		radec.setForeground(Color.WHITE);
		add(radec);

		//Current RA Label
		ccoorRL = new JLabel("Ra");
		ccoorRL.setSize(30,20);
		ccoorRL.setForeground(Color.WHITE);
		add(ccoorRL);

		//Current RA Coordinate
		ccoorR = new JLabel("0.000");
		ccoorR.setSize(80,20);
		ccoorR.setForeground(Color.WHITE);
		add(ccoorR);

		//Current DEC Label
		ccoorDL = new JLabel("Dec");
		ccoorDL.setSize(30,20);
		ccoorDL.setForeground(Color.WHITE);
		add(ccoorDL);

		//Current DEC Coordinate
		ccoorD = new JLabel("0.000");
		ccoorD.setSize(80,20);
		ccoorD.setForeground(Color.WHITE);
		add(ccoorD);

		//Current Altitude Label
		ccoorAlL = new JLabel("Alt");
		ccoorAlL.setSize(30,20);
		ccoorAlL.setForeground(Color.WHITE);
		add(ccoorAlL);

		//Current Altitude Coordinate
		ccoorAl = new JLabel("0.000");
		ccoorAl.setSize(80,20);
		ccoorAl.setForeground(Color.WHITE);
		add(ccoorAl);

		//Current Azimuth Label
		ccoorAzL = new JLabel("Az");
		ccoorAzL.setSize(30,20);
		ccoorAzL.setForeground(Color.WHITE);
		add(ccoorAzL);

		//Current Azimuth Coordinate
		ccoorAz = new JLabel("0.000");
		ccoorAz.setSize(80,20);
		ccoorAz.setForeground(Color.WHITE);
		add(ccoorAz);

		//Catalogue Button
		catalogue = new JButton("Sel. from Catalogue");
		catalogue.setSize(130,20);
		catalogue.setMargin(new Insets(0,0,0,0));
		add(catalogue);

		//Agregando...
		//Go to R Button
		/*ImageIcon rArrow = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/rArrow.jpg")).getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH));
		rbutton = new JButton(rArrow);
		rbutton.setBackground(Color.WHITE);
		rbutton.setSize(50,50);
		//rbutton.setLocation();
		add(rbutton);

		rbutton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(interfaz.getDrawingPanel().getCSATControl()!=null){
						interfaz.getDrawingPanel().getCSATControl().AzimuthOffSet(1.0d);
						setAzOffset((double)(getAzOffset()+1));
					}
				}
				});
*/
		rbutton = new JLabel(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/rigth.png")).getImage().getScaledInstance(45,45,Image.SCALE_SMOOTH)));
                rbutton.setBackground(Color.BLACK);
                rbutton.setSize(50,50);
                add(rbutton);

		


		//Go to L Button
/*		ImageIcon lArrow = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/lArrow.jpg")).getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH));
		lbutton = new JButton(lArrow);
		lbutton.setBackground(Color.WHITE);
		lbutton.setSize(50,50);
		add(lbutton);

		lbutton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(interfaz.getDrawingPanel().getCSATControl()!=null){
						interfaz.getDrawingPanel().getCSATControl().AzimuthOffSet(-1.0d);
						setAzOffset((double)(getAzOffset()-1));
					}
				}
				});*/

		lbutton = new JLabel(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/left.png")).getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH)));
                lbutton.setBackground(Color.BLACK);
                lbutton.setSize(50,50);
                add(lbutton);




		//Go to T Botton
/*		ImageIcon tArrow = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/tArrow.jpg")).getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH));
		tbutton = new JButton(tArrow);
		tbutton.setBackground(Color.WHITE);
		tbutton.setSize(50,50);
		add(tbutton);

		tbutton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(interfaz.getDrawingPanel().getCSATControl()!=null){
						interfaz.getDrawingPanel().getCSATControl().AltitudeOffSet(1.0d);
						setAltOffset((double)(getAltOffset()+1));
					}	
				}
				});*/

		tbutton = new JLabel(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/up.png")).getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH)));
                tbutton.setBackground(Color.BLACK);
                tbutton.setSize(50,50);
                add(tbutton);



		//Go to B Button
/*		ImageIcon bArrow = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/bArrow.jpg")).getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH));
		bbutton = new JButton(bArrow);
		bbutton.setBackground(Color.WHITE);
		bbutton.setSize(50,50);
		add(bbutton);

		bbutton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(interfaz.getDrawingPanel().getCSATControl()!=null){
						interfaz.getDrawingPanel().getCSATControl().AltitudeOffSet(-1.0d);
						setAltOffset((double)(getAltOffset()-1));
					}
				}
				});*/

		bbutton = new JLabel(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/down.png")).getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH)));
                bbutton.setBackground(Color.BLACK);
                bbutton.setSize(50,50);
                add(bbutton);




/*		ImageIcon stop = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/stop.png")).getImage().getScaledInstance(80,80,Image.SCALE_SMOOTH));
		stopbutton = new JButton(stop);
		stopbutton.setBackground(Color.WHITE);
		stopbutton.setSize(80,80);
		add(stopbutton);

		stopbutton.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                if(interfaz.getDrawingPanel().getCSATControl()!=null){
                                	interfaz.getDrawingPanel().getCSATControl().stopTelescope();
                                	hist.addHistoryStop();
				}
                                }
                                });*/

		stopbutton = new JLabel(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/stop.png")).getImage().getScaledInstance(85,85,Image.SCALE_SMOOTH)));
                stopbutton.setBackground(Color.BLACK);
                stopbutton.setSize(85,85);
                add(stopbutton);





		//Activate/Deactivate Tracking
		tracking = new JButton("Tracking");
		tracking.setSize(100,20);
		add(tracking);
		tracking.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
				if(interfaz.getDrawingPanel().getCSATControl()!=null&&
					interfaz.getDrawingPanel().getCSATStatus()!=null)
				{
				interfaz.getDrawingPanel().getCSATControl().setTrackingStatus(
					!interfaz.getDrawingPanel().getCSATStatus().getTrackingStatus());
                                hist.addHistoryTracking(!interfaz.getDrawingPanel().getCSATStatus().getTrackingStatus());
				}
				}
				});


		//Go to Zenith Button
		zenith = new JButton("Zenith");
		zenith.setSize(100,20);
		add(zenith);
		zenith.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
				AltazPos p = new AltazPos();
				p.alt = 90;
				p.az = 0;
				if(interfaz.getDrawingPanel().getCSATControl()!=null)
					interfaz.getDrawingPanel().getCSATControl().goToAltAz(p, new AltazVel());
					hist.addHistoryZennith();
				}
				});

		//Go to Park Button
		park = new JButton("Park");
		park.setSize(100,20);
		add(park);

		//Offset Label
		offsetL = new JLabel("Offset");
		offsetL.setSize(80,20);
		offsetL.setForeground(Color.WHITE);
		add(offsetL);

		//Alt Offset Label
		laltoffset = new JLabel("Alt");
		laltoffset.setSize(30,20);
		laltoffset.setForeground(Color.WHITE);
		add(laltoffset);

		//Az Offset Label
		lazoffset = new JLabel("Az");
		lazoffset.setSize(30,20);
		lazoffset.setForeground(Color.WHITE);
		add(lazoffset);

		//Alt Offset Value
		altoffset = new JLabel("0");
		altoffset.setSize(30,20);
		altoffset.setForeground(Color.WHITE);
		add(altoffset);

		//Az Offset Value
		azoffset = new JLabel("0");
		azoffset.setSize(30,20);
		azoffset.setForeground(Color.WHITE);
		add(azoffset);
		//
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		boolean updatePanel = false;
		float fsize;
		if (dx != getSize().width || dy != getSize().height)
			updatePanel = true;
		dx = getSize().width;
		dy = getSize().height;

		if(updatePanel)
		{
			if(dx/463.0f>dy/418.0f)
			{
				fsize = (12.0f*dy)/418;
				osize = dy/418.0f;
			}
			else
			{
				fsize = (12.0f*dx)/463;
				osize = dx/463.0f;
			}

			//Interface Objects Positioning
			coor.setLocation((int)(30*osize),(int)(40*osize));
			coor.setFont(coor.getFont().deriveFont(fsize));
			coor.setSize((int)(150*osize),(int)(20*osize));
			

			coor1L.setLocation((int)(20*osize),(int)(70*osize));
			coor1L.setFont(coor1L.getFont().deriveFont(fsize));
			coor1L.setSize((int)(30*osize),(int)(20*osize));

			coor1.setLocation((int)(50*osize),(int)(70*osize));
			coor1.setFont(coor1.getFont().deriveFont(fsize));
			coor1.setSize((int)(80*osize),(int)(20*osize));

			coor2L.setLocation((int)(20*osize),(int)(95*osize));
			coor2L.setFont(coor2L.getFont().deriveFont(fsize));
			coor2L.setSize((int)(30*osize),(int)(20*osize));

			coor2.setLocation((int)(50*osize),(int)(95*osize));
			coor2.setFont(coor2.getFont().deriveFont(fsize));
			coor2.setSize((int)(80*osize),(int)(20*osize));

			go.setLocation((int)(135*osize),(int)(70*osize));
			go.setFont(go.getFont().deriveFont(fsize));
			go.setSize((int)(45*osize),(int)(45*osize));

			ccoor.setLocation((int)(250*osize),(int)(40*osize));
			ccoor.setFont(ccoor.getFont().deriveFont(fsize));
			ccoor.setSize((int)(150*osize),(int)(20*osize));

			radec.setLocation((int)(240*osize),(int)(80*osize));
			radec.setFont(radec.getFont().deriveFont(fsize));
			radec.setSize((int)(80*osize),(int)(20*osize));

			horizontal.setLocation((int)(350*osize), (int)(80*osize));
			horizontal.setFont(horizontal.getFont().deriveFont(fsize));
			horizontal.setSize((int)((int)(80*osize)*osize),(int)((int)(20*osize)*osize));

			ccoorRL.setLocation((int)(220*osize),(int)(110*osize));
			ccoorRL.setFont(ccoorRL.getFont().deriveFont(fsize));
			ccoorRL.setSize((int)(30*osize),(int)(20*osize));

			ccoorR.setLocation((int)(260*osize),(int)(110*osize));
			ccoorR.setFont(ccoorR.getFont().deriveFont(fsize));
			ccoorR.setSize((int)(80*osize),(int)(20*osize));

			ccoorDL.setLocation((int)(220*osize), (int)(130*osize));
			ccoorDL.setFont(ccoorDL.getFont().deriveFont(fsize));
			ccoorDL.setSize((int)(30*osize),(int)(20*osize));

			ccoorD.setLocation((int)(260*osize),(int)(130*osize));
			ccoorD.setFont(ccoorD.getFont().deriveFont(fsize));
			ccoorD.setSize((int)(80*osize),(int)(20*osize));

			ccoorAlL.setLocation((int)(340*osize),(int)(110*osize));
			ccoorAlL.setFont(ccoorAlL.getFont().deriveFont(fsize));
			ccoorAlL.setSize((int)(30*osize),(int)(20*osize));

			ccoorAl.setLocation((int)(380*osize),(int)(110*osize));
			ccoorAl.setFont(ccoorAl.getFont().deriveFont(fsize));
			ccoorAl.setSize((int)(80*osize),(int)(20*osize));

			ccoorAzL.setLocation((int)(340*osize),(int)(130*osize));
			ccoorAzL.setFont(ccoorAzL.getFont().deriveFont(fsize));
			ccoorAzL.setSize((int)(30*osize),(int)(20*osize));

			ccoorAz.setLocation((int)(380*osize),(int)(130*osize));
			ccoorAz.setFont(ccoorAz.getFont().deriveFont(fsize));
			ccoorAz.setSize((int)(80*osize),(int)(20*osize));

			catalogue.setLocation((int)(50*osize),(int)(120*osize));
			catalogue.setFont(catalogue.getFont().deriveFont(fsize));
			catalogue.setSize((int)(130*osize),(int)(20*osize));

			//Agregando...
			//Pointing buttons
			rbutton.setLocation((int)(145*osize),(int)(260*osize));
			rbutton.setSize((int)(50*osize),(int)(50*osize));
			rbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/rigth.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));

			rbutton.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        rbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/rigth.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        rbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/rigth-encima.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        rbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/rigth.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        rbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/rigth-encima.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        //config.setVisible(true);
                                        //setConfigWindow();
					if(interfaz.getDrawingPanel().getCSATControl()!=null){
                                                interfaz.getDrawingPanel().getCSATControl().AzimuthOffSet(1.0d);
                                                setAzOffset((double)(getAzOffset()+1));
                                        }
                                        }

                                        public void mousePressed(MouseEvent event){
                                        rbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/rigth-click.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                        });



			lbutton.setLocation((int)(25*osize),(int)(260*osize));
			lbutton.setSize((int)(50*osize),(int)(50*osize));
			lbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/left.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));

			lbutton.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        lbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/left.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        lbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/left-encima.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        lbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/left.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        lbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/left-encima.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        //config.setVisible(true);
                                        //setConfigWindow();
					if(interfaz.getDrawingPanel().getCSATControl()!=null){
						interfaz.getDrawingPanel().getCSATControl().AzimuthOffSet(-1.0d);
						setAzOffset((double)(getAzOffset()-1));
					}
                                        }

                                        public void mousePressed(MouseEvent event){
                                        lbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/left-click.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                        });




			tbutton.setLocation((int)(85*osize),(int)(200*osize));
			tbutton.setSize((int)(50*osize),(int)(50*osize));
			tbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/up.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));

			tbutton.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        tbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/up.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        tbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/up-encima.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        tbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/up.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        tbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/up-encima.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        //config.setVisible(true);
                                        //setConfigWindow();
					if(interfaz.getDrawingPanel().getCSATControl()!=null){
						interfaz.getDrawingPanel().getCSATControl().AltitudeOffSet(1.0d);
						setAltOffset((double)(getAltOffset()+1));
					}
                                        }

                                        public void mousePressed(MouseEvent event){
                                        tbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/up-click.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                        });





			bbutton.setLocation((int)(85*osize),(int)(320*osize));
			bbutton.setSize((int)(50*osize),(int)(50*osize));
			bbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/down.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));

			bbutton.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        bbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/down.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        bbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/down-encima.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        bbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/down.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        bbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/down-encima.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        //config.setVisible(true);
                                        //setConfigWindow();
					if(interfaz.getDrawingPanel().getCSATControl()!=null){
						interfaz.getDrawingPanel().getCSATControl().AltitudeOffSet(-1.0d);
						setAltOffset((double)(getAltOffset()-1));
					}
                                        }

                                        public void mousePressed(MouseEvent event){
                                        bbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/down-click.png")).getImage().getScaledInstance((int)(50*osize),(int)(50*osize),Image.SCALE_SMOOTH)));
                                        }

                        });




			//Offset Lables
			offsetL.setLocation((int)(240*osize),(int)(185*osize));
			offsetL.setFont(offsetL.getFont().deriveFont(fsize));
			offsetL.setSize((int)(80*osize),(int)(20*osize));

			laltoffset.setLocation((int)(230*osize),(int)(205*osize));
			laltoffset.setFont(laltoffset.getFont().deriveFont(fsize));
			laltoffset.setSize((int)(30*osize),(int)(20*osize));

			lazoffset.setLocation((int)(230*osize),(int)(225*osize));
			lazoffset.setFont(lazoffset.getFont().deriveFont(fsize));
			lazoffset.setSize((int)(30*osize),(int)(20*osize));

			altoffset.setLocation((int)(270*osize),(int)(205*osize));
			altoffset.setFont(altoffset.getFont().deriveFont(fsize));
			altoffset.setSize((int)(30*osize),(int)(20*osize));

			azoffset.setLocation((int)(270*osize),(int)(225*osize));
			azoffset.setFont(azoffset.getFont().deriveFont(fsize));
			azoffset.setSize((int)(30*osize),(int)(20*osize));

			//Park, Zenith & Tracking
			park.setLocation((int)(330*osize),(int)(180*osize));
			park.setFont(park.getFont().deriveFont(fsize));
			park.setSize((int)(100*osize),(int)(20*osize));

			zenith.setLocation((int)(330*osize),(int)(205*osize));
			zenith.setFont(zenith.getFont().deriveFont(fsize));
			zenith.setSize((int)(100*osize),(int)(20*osize));

			tracking.setLocation((int)(330*osize),(int)(230*osize));
			tracking.setFont(tracking.getFont().deriveFont(fsize));
			tracking.setSize((int)(100*osize),(int)(20*osize));

			stopbutton.setLocation((int)(340*osize),(int)(300*osize));
			stopbutton.setSize((int)(85*osize),(int)(85*osize));
			stopbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/stop.png")).getImage().getScaledInstance((int)(85*osize),(int)(85*osize),Image.SCALE_SMOOTH)));

			stopbutton.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        stopbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/stop.png")).getImage().getScaledInstance((int)(85*osize),(int)(85*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        stopbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/stop-encima.png")).getImage().getScaledInstance((int)(85*osize),(int)(85*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        stopbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/stop.png")).getImage().getScaledInstance((int)(85*osize),(int)(85*osize),Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        stopbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/stop-encima.png")).getImage().getScaledInstance((int)(85*osize),(int)(85*osize),Image.SCALE_SMOOTH)));
                                        //config.setVisible(true);
                                        //setConfigWindow();
                                	if(interfaz.getDrawingPanel().getCSATControl()!=null){
                                		interfaz.getDrawingPanel().getCSATControl().stopTelescope();
                                		hist.addHistoryStop();
					}
                                        }

                                        public void mousePressed(MouseEvent event){
                                        stopbutton.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/stop-click.png")).getImage().getScaledInstance((int)(85*osize),(int)(85*osize),Image.SCALE_SMOOTH)));
                                        }

                        });



		}
	}
	public void setCoorType(boolean type)
	{
		coortype = type;
		if(!type)
		{
			coor.setText("RaDec Coordinates");
			coor1L.setText("Ra");
			coor2L.setText("Dec");
		}
		else
		{
			coor.setText("Horizontal Coordinates");
			coor1L.setText("Alt");
			coor2L.setText("Az");
		}
	}

	public void initCoorType(boolean type)
	{
		coortype = type;
		if(!type)
		{
			coor.setText("RaDec Coordinates");
			coor1L.setText("Ra");
			coor2L.setText("Dec");
		}
		else
		{
			coor.setText("Horizontal Coordinates");
			coor1L.setText("Alt");
			coor2L.setText("Az");
		}
	}

	public double getRa()
	{
		return Double.parseDouble(ccoorR.getText());
	}

	public double getDec()
	{
		return Double.parseDouble(ccoorD.getText());
	}

	public double getAlt()
	{
		return Double.parseDouble(ccoorAl.getText());
	}

	public double getAz()
	{
		return Double.parseDouble(ccoorAz.getText());
	}

	public double getAltOffset()
	{
		return Double.parseDouble(altoffset.getText());
	}

	public double getAzOffset()
	{
		return Double.parseDouble(azoffset.getText());
	}

	public void setRa(double RA)
	{
		DecimalFormat df = new DecimalFormat("#.###");
		//ccoorR.setText(Double.toString(RA));
		ccoorR.setText(df.format(RA));
	}

	public void setDec(double DEC)
	{
		DecimalFormat df = new DecimalFormat("#.###");
		//ccoorD.setText(Double.toString(DEC));
		ccoorD.setText(df.format(DEC));
	}

	public void setAlt(double ALT)
	{
		DecimalFormat df = new DecimalFormat("#.###");
		//ccoorAl.setText(Double.toString(ALT));
		ccoorAl.setText(df.format(ALT));
	}

	public void setAz(double AZ)
	{
		DecimalFormat df = new DecimalFormat("#.###");
		//ccoorAz.setText(Double.toString(AZ));
		ccoorAz.setText(df.format(AZ));
		//interfaz.getDrawingPanel().getCompassPanel().setCompassPoints(AZ);
	}

	public void setAltOffset(double deg)
	{
		DecimalFormat df = new DecimalFormat("#.###");
		altoffset.setText(df.format(deg));
	}

	public void setAzOffset(double deg)
	{
		DecimalFormat df = new DecimalFormat("#.###");
		azoffset.setText(df.format(deg));
	}
}
