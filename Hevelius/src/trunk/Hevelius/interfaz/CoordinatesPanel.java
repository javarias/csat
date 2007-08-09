package Hevelius.interfaz;

import Hevelius.utilities.converter.*;
import Hevelius.acsmodules.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CoordinatesPanel extends JPanel
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

	private boolean coortype;

	public CoordinatesPanel(LayoutManager l)
	{
		super(l);
	}
	public void init()
	{
		//Coordinate Label
		/*switch(Integer.parseInt(test.getOption("coordinate"))){
			case 0: coor = new JLabel("RaDec Coordinates"); break;
			case 1: coor = new JLabel("Horizontal Coordinates"); break;
			default: coor = new JLabel("RaDec Coordinates"); break;
		}*/
		coor = new JLabel("");
		coor.setSize(150,20);
		coor.setForeground(Color.WHITE);
		coor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		add(coor);

		//RA or ALT Label
		/*switch(Integer.parseInt(test.getOption("coordinate"))){
			case 0: coor1L = new JLabel("RA"); break;
			case 1: coor1L = new JLabel("Alt"); break;
			default: coor1L = new JLabel("RA"); break;
		}*/
		coor1L = new JLabel("");
		coor1L.setSize(30,20);
		coor1L.setForeground(Color.WHITE);
		add(coor1L);

		//RA or ALT Coordinate
		coor1 = new JTextField("0");
		coor1.setSize(80,20);
		add(coor1);

		//DEC or AZ Label
		/*switch(Integer.parseInt(test.getOption("coordinate"))){
			case 0: coor2L = new JLabel("Dec"); break;
			case 1: coor2L = new JLabel("Az"); break;
			default: coor2L = new JLabel("Dec"); break;
		}*/
		coor2L = new JLabel("");
		coor2L.setSize(30,20);
		coor2L.setForeground(Color.WHITE);
		add(coor2L);

		//DEC or AZ Coordinate
		coor2 = new JTextField("0");
		coor2.setSize(80,20);
		add(coor2);

		//Goto
		go = new JButton("Go");
		go.setSize(45,45);
		go.setMargin(new Insets(0,0,0,0));
		add(go);

		go.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Double Alt, Az;
				Alt = Double.parseDouble(coor1.getText());
				Az = Double.parseDouble(coor2.getText());
				//Converter.radec2altaz(Alt,Az);
				

				/* AGREGAR CAPTURA DE TIPO DE COORDENADA */
				Presetting.move_to(Alt,Az,type);				
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
		ccoorR = new JLabel("0.00000");
		ccoorR.setSize(80,20);
		ccoorR.setForeground(Color.WHITE);
		add(ccoorR);

		//Current DEC Label
		ccoorDL = new JLabel("Dec");
		ccoorDL.setSize(30,20);
		ccoorDL.setForeground(Color.WHITE);
		add(ccoorDL);

		//Current DEC Coordinate
		ccoorD = new JLabel("0.00000");
		ccoorD.setSize(80,20);
		ccoorD.setForeground(Color.WHITE);
		add(ccoorD);

		//Current Altitude Label
		ccoorAlL = new JLabel("Alt");
		ccoorAlL.setSize(30,20);
		ccoorAlL.setForeground(Color.WHITE);
		add(ccoorAlL);

		//Current Altitude Coordinate
		ccoorAl = new JLabel("0.00000");
		ccoorAl.setSize(80,20);
		ccoorAl.setForeground(Color.WHITE);
		add(ccoorAl);

		//Current Azimuth Label
		ccoorAzL = new JLabel("Az");
		ccoorAzL.setSize(30,20);
		ccoorAzL.setForeground(Color.WHITE);
		add(ccoorAzL);

		//Current Azimuth Coordinate
		ccoorAz = new JLabel("0.00000");
		ccoorAz.setSize(80,20);
		ccoorAz.setForeground(Color.WHITE);
		add(ccoorAz);

		//Catalogue Button
		catalogue = new JButton("Sel. from Catalogue");
		catalogue.setSize(130,20);
		catalogue.setMargin(new Insets(0,0,0,0));
		add(catalogue);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		//Interface Objects Positioning
		coor.setLocation(30,140);

		coor1L.setLocation(20,170);

		coor1.setLocation(50,170);

		coor2L.setLocation(20,195);

		coor2.setLocation(50,195);

		go.setLocation(135,170);

		ccoor.setLocation(30,0);

		radec.setLocation(20,40);
		
		horizontal.setLocation(130, 40);

		ccoorRL.setLocation(0,70);

		ccoorR.setLocation(40,70);

		ccoorDL.setLocation(0, 90);

		ccoorD.setLocation(40,90);

		ccoorAlL.setLocation(120,70);

		ccoorAl.setLocation(150,70);

		ccoorAzL.setLocation(120,90);

		ccoorAz.setLocation(150,90);

		catalogue.setLocation(50,220);
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

	public void setRa(double RA)
	{
		ccoorR.setText(Double.toString(RA));
	}

	public void setDec(double DEC)
	{
		ccoorD.setText(Double.toString(DEC));
	}

	public void setAlt(double ALT)
	{
		ccoorAl.setText(Double.toString(ALT));
	}

	public void setAz(double AZ)
	{
		ccoorAz.setText(Double.toString(AZ));
		interfaz.getDrawingPanel().getCompassPanel().setCompassPoints(AZ);
	}

}
