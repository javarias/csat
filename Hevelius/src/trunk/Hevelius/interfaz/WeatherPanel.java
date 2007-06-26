package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import Hevelius.weather.*;

public class WeatherPanel extends JPanel
{
	private static Configuration test = new Configuration();	
	private JLabel wLabel;
	private JLabel tempL;
	private JLabel wStatL;
	private JLabel windL;
	private JLabel moonL;
	private JLabel humL;
	private JLabel tempB;
	private JLabel wStatB;
	private JLabel windB;
	private JLabel moonB;
	private JLabel humB;

	private WeatherCondition clima = new WeatherCondition(test.getOption("location"));
	private Vector<WeatherCityCondition> vector = clima.ListCityCondition();

	public WeatherPanel(LayoutManager l)
	{
		super(l);
		init();
	}
	public void init()
	{
		//Weather Status Label
		wLabel = new JLabel("Weather Conditions "+vector.get(0).getTime());
		wLabel.setSize(200,20);
		wLabel.setForeground(Color.WHITE);
		wLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
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

		//Moon Label
		moonL = new JLabel("Moon Phase:");
		moonL.setSize(100,20);
		moonL.setForeground(Color.WHITE);
		add(moonL);

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
		tempB = new JLabel(vector.get(0).getTm()+"°C");
		tempB.setSize(100,20);
		tempB.setForeground(Color.WHITE);
		add(tempB);

		//Weather Status

		Icon image = new ImageIcon("Hevelius/images/weather_images/"+vector.get(0).getIconWt()+".png");

		wStatB = new JLabel(vector.get(0).getWt(),image,JLabel.LEFT);
		wStatB.setSize(200,32);
		wStatB.setForeground(Color.WHITE);
		add(wStatB);

		//Wind Bar
		try{
			Integer.parseInt(vector.get(0).getVwind());
			windB = new JLabel(vector.get(0).getVwind()+" Km/Hr "+vector.get(0).getDirec());
		}catch(NumberFormatException e){
			windB = new JLabel(vector.get(0).getDirec());
		}
		//System.out.println(vector.get(0).getAll());
		windB.setSize(150,20);
		windB.setForeground(Color.WHITE);
		add(windB);

		//Moon Bar
		moonB = new JLabel(vector.get(0).getMoon());
		moonB.setSize(150,20);
		moonB.setForeground(Color.WHITE);
		add(moonB);

		//Humdity Bar
		humB = new JLabel(vector.get(0).getHumil()+"%");
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

		moonL.setLocation(10,85);

		windL.setLocation(10,110);

		humL.setLocation(10,135);

		tempB.setLocation(140,35);

		wStatB.setLocation(140,54);

		moonB.setLocation(140,85);

		windB.setLocation(140,110);

		humB.setLocation(140,135);
	}

/*	public void autoReloadWeather(){
		long time1 = System.currentTimeMillis();
		long time2 = System.currentTimeMillis();
		while(true){
			while(time2-time1<30000)
				time2 = System.currentTimeMillis();

			reloadWeather();
		}
	} */

	public void reloadWeather(){
		vector.removeAllElements();		
		clima = new WeatherCondition(test.getOption("location"));
		vector = clima.ListCityCondition();

		wLabel.setText("Weather Conditions "+vector.get(0).getTime());

		tempB.setText(vector.get(0).getTm()+"°C");

		Icon image = new ImageIcon("Hevelius/images/weather_images/"+vector.get(0).getIconWt()+".png");
		wStatB.setIcon(image);
		wStatB.setText(vector.get(0).getWt());

		try{
			Integer.parseInt(vector.get(0).getVwind());
			windB.setText(vector.get(0).getVwind()+" Km/Hr "+vector.get(0).getDirec());
		}catch(NumberFormatException e){
			windB.setText(vector.get(0).getDirec());
		}

		moonB.setText(vector.get(0).getMoon());

		humB.setText(vector.get(0).getHumil()+"%");

	}

}
