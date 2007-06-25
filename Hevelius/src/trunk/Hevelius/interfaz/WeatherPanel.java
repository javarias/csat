package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WeatherPanel extends JPanel
{
	private JLabel wLabel;
	private JLabel tempL;
	private JLabel wStatL;
	private JLabel windL;
	private JLabel humL;
	private JLabel tempB;
	private JLabel wStatB;
	private JLabel windB;
	private JLabel humB;
	public WeatherPanel(LayoutManager l)
	{
		super(l);
		init();
	}
	public void init()
	{
		//Weather Status Label
		wLabel = new JLabel("Weather Conditions");
		wLabel.setSize(200,20);
		wLabel.setForeground(Color.WHITE);
		add(wLabel);

		//Temperature Label
		tempL = new JLabel("Temperature:");
		tempL.setSize(100,20);
		tempL.setForeground(Color.WHITE);
		add(tempL);

		//Weather Status Label
		wStatL = new JLabel("Weather:");
		wStatL.setSize(100,20);
		wStatL.setForeground(Color.WHITE);
		add(wStatL);

		//Wind Label
		windL = new JLabel("Wind:");
		windL.setSize(100,20);
		windL.setForeground(Color.WHITE);
		add(windL);

		//Humdity Label
		humL = new JLabel("Humidity:");
		humL.setSize(100,20);
		humL.setForeground(Color.WHITE);
		add(humL);

		//Temperature Bar
		tempB = new JLabel("0");
		tempB.setSize(100,20);
		tempB.setForeground(Color.WHITE);
		add(tempB);

		//Weather Status
		wStatB = new JLabel("Sunny");
		wStatB.setSize(100,20);
		wStatB.setForeground(Color.WHITE);
		add(wStatB);

		//Wind Bar
		windB = new JLabel("0");
		windB.setSize(100,20);
		windB.setForeground(Color.WHITE);
		add(windB);

		//Humdity Bar
		humB = new JLabel("0");
		humB.setSize(100,20);
		humB.setForeground(Color.WHITE);
		add(humB);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		wLabel.setLocation(10,10);

		tempL.setLocation(10,35);

		wStatL.setLocation(10,60);

		windL.setLocation(10,85);

		humL.setLocation(10,110);

		tempB.setLocation(140,35);

		wStatB.setLocation(140,60);

		windB.setLocation(140,85);

		humB.setLocation(140,110);
	}
}
