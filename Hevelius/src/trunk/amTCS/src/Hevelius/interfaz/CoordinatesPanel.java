package Hevelius.interfaz;

//import Hevelius.utilities.converter.*;
import Hevelius.acsmodules.*;
import Hevelius.heveliusmodules.*;
import Hevelius.interfaz.*;
import Hevelius.utilities.converter.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import alma.TYPES.*;

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
        private JButton rbutton = null;
        private JButton lbutton = null;
        private JButton tbutton = null;
        private JButton bbutton = null;
	private JLabel laltoffset;
	private JLabel lazoffset;
	private JLabel altoffset;
	private JLabel azoffset;
	private JLabel offsetL;
//

	private boolean coortype;
	private RadecPosHolder rdPos;
	private AltazPosHolder aaPos;

	public CoordinatesPanel(LayoutManager l)
	{
		super(l);
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
		coor1 = new JTextField("0");
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
				Configuration test = new Configuration();

				c1 = Double.parseDouble(coor1.getText());
				c2 = Double.parseDouble(coor2.getText());
				
				Presetting.preset(c1,c2,Integer.parseInt(test.getOption("coordinate")));		
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
                ImageIcon rArrow = new ImageIcon(new ImageIcon("Hevelius/images/rArrow.jpg").getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH));
                rbutton = new JButton(rArrow);
                rbutton.setBackground(Color.WHITE);
                rbutton.setSize(50,50);
                //rbutton.setLocation();
                add(rbutton);

                rbutton.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                if(interfaz.getDrawingPanel().getCSATControl()!=null)
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
                                if(interfaz.getDrawingPanel().getCSATControl()!=null)
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
                                if(interfaz.getDrawingPanel().getCSATControl()!=null)
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
                                if(interfaz.getDrawingPanel().getCSATControl()!=null)
                                interfaz.getDrawingPanel().getCSATControl().AltitudeOffSet(-1.0d);
                                }
                                });

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
                                interfaz.getDrawingPanel().getCSATControl().goToAltAz(p, null);
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
		altoffset = new JLabel("0.0");
		altoffset.setSize(30,20);
		altoffset.setForeground(Color.WHITE);
		add(altoffset);

		//Az Offset Value
		azoffset = new JLabel("0.0");
		azoffset.setSize(30,20);
		azoffset.setForeground(Color.WHITE);
		add(azoffset);
//
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		//Interface Objects Positioning
		coor.setLocation(30,0);

		coor1L.setLocation(20,30);

		coor1.setLocation(50,30);

		coor2L.setLocation(20,55);

		coor2.setLocation(50,55);

		go.setLocation(135,30);

		ccoor.setLocation(250,0);

		radec.setLocation(240,40);
		
		horizontal.setLocation(350, 40);

		ccoorRL.setLocation(220,70);

		ccoorR.setLocation(260,70);

		ccoorDL.setLocation(220, 90);

		ccoorD.setLocation(260,90);

		ccoorAlL.setLocation(340,70);

		ccoorAl.setLocation(380,70);

		ccoorAzL.setLocation(340,90);

		ccoorAz.setLocation(380,90);

		catalogue.setLocation(50,80);

//Agregando...
                //Pointing buttons
                rbutton.setLocation(145,220);
                lbutton.setLocation(25,220);
                tbutton.setLocation(85,160);
                bbutton.setLocation(85,280);

		//Offset Lables
		offsetL.setLocation(240,135);
		laltoffset.setLocation(230,155);
		lazoffset.setLocation(230,175);
		altoffset.setLocation(270,155);
		azoffset.setLocation(270,175);

		//Park, Zenith & Tracking
		park.setLocation(340,130);
		zenith.setLocation(340,155);
		tracking.setLocation(340,180);
//
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
		interfaz.getDrawingPanel().getCompassPanel().setCompassPoints(AZ);
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
