/** 
* Clase que contiene el panel correspondiente a las condiciones del clima
*/

package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import Hevelius.weather.*;

public class WeatherPanel extends JPanel implements Runnable
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
	private JButton more;

	private WeatherCondition clima = new WeatherCondition(test.getOption("location"));
	private Vector<WeatherCityCondition> vector = clima.ListCityCondition();

	private int dx = 0;
	private int dy = 0;

	/**
	* Constructor de la clase
	*/
	public WeatherPanel(LayoutManager l)
	{
		super(l);
	}
	/**
	* Metodo que dibuja los labels y botones del panel.
	*/
	public void init()
	{
		//Weather Status Label
		wLabel = new JLabel(vector.get(0).getCity()+" at "+vector.get(0).getTime());
		wLabel.setSize(250,20);
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

		Icon image;
		if(vector.get(0).getIconWt().compareTo("N/A")==0)
			image = new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/weather_images/na.png"));
		else
			image = new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/weather_images/"+vector.get(0).getIconWt()+".png"));

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

		//More Button
		more = new JButton("more");
		more.setSize(50,15);
		more.setMargin(new Insets(0,0,0,0));
		more.addActionListener(new ActionListener(  ) {
				public void actionPerformed(ActionEvent event) {
					try{
						fullWeatherWindow();
					}catch(ArrayIndexOutOfBoundsException e){
					System.out.println("Array Out of Bounds");	
					}
				}
				});
		add(more);

	}
	
	 /**
        * This method is extended in order to allow autoresizing
        * of widgets whenever window's size changes.
        * @param g      Graphics
        */
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		boolean updatePanel = false;
		float fsize, osize;
		if (dx != getSize().width || dy != getSize().height)
			updatePanel = true;
		dx = getSize().width;
		dy = getSize().height;
		if(updatePanel)
		{
			if(dx/280.0f>dy/263.0f)
			{
				fsize = (12.0f*dy)/263.0f;
				osize = dy/263.0f;
			}
			else
			{
				fsize = (12.0f*dx)/280.0f;
				osize = dx/280.0f;
			}

			wLabel.setLocation((int)(10*osize),(int)(40*osize));
			wLabel.setFont(wLabel.getFont().deriveFont(fsize));
			wLabel.setSize((int)(250*osize),(int)(20*osize));


			tempL.setLocation((int)(20*osize),(int)(65*osize));
			tempL.setFont(tempL.getFont().deriveFont(fsize));
			tempL.setSize((int)(100*osize),(int)(20*osize));

			wStatL.setLocation((int)(20*osize),(int)(90*osize));
			wStatL.setFont(wStatL.getFont().deriveFont(fsize));
			wStatL.setSize((int)(100*osize),(int)(20*osize));

			moonL.setLocation((int)(20*osize),(int)(115*osize));
			moonL.setFont(moonL.getFont().deriveFont(fsize));
			moonL.setSize((int)(100*osize),(int)(20*osize));

			windL.setLocation((int)(20*osize),(int)(140*osize));
			windL.setFont(windL.getFont().deriveFont(fsize));
			windL.setSize((int)(100*osize),(int)(20*osize));

			humL.setLocation((int)(20*osize),(int)(165*osize));
			humL.setFont(humL.getFont().deriveFont(fsize));
			humL.setSize((int)(100*osize),(int)(20*osize));

			tempB.setLocation((int)(140*osize),(int)(65*osize));
			tempB.setFont(tempB.getFont().deriveFont(fsize));
			tempB.setSize((int)(100*osize),(int)(20*osize));

			wStatB.setLocation((int)(140*osize),(int)(84*osize));
			wStatB.setFont(wStatB.getFont().deriveFont(fsize));
			wStatB.setSize((int)(200*osize),(int)(32*osize));

			moonB.setLocation((int)(140*osize),(int)(115*osize));
			moonB.setFont(moonB.getFont().deriveFont(fsize));
			moonB.setSize((int)(150*osize),(int)(20*osize));

			windB.setLocation((int)(140*osize),(int)(140*osize));
			windB.setFont(windB.getFont().deriveFont(fsize));
			windB.setSize((int)(150*osize),(int)(20*osize));

			humB.setLocation((int)(140*osize),(int)(165*osize));
			humB.setFont(humB.getFont().deriveFont(fsize));
			humB.setSize((int)(100*osize),(int)(20*osize));

			more.setLocation((int)(210*osize),(int)(190*osize));
			more.setFont(more.getFont().deriveFont(fsize));
			more.setSize((int)(50*osize),(int)(20*osize));
		}
	}

	/**
	* Metodo encargado de llamar al metodo de refresco de los datos del clima
	*/
	public void run(){
		long time1 = System.currentTimeMillis();
		long time2 = System.currentTimeMillis();
		while(true){
			//while(time2-time1<60000)
			//	time2 = System.currentTimeMillis();

			this.reloadWeather();
			try{
				Thread.sleep(20000);
			}catch(InterruptedException e){
				System.out.println("Error en Thread del Weather Panel");
			}
		}
	}
	
	/**
	* Metodo encargado de refrescar los datos del clima
	*/
	
	public void reloadWeather(){
		try{
			vector.removeAllElements();		
			clima = new WeatherCondition(test.getOption("location"));
			vector = clima.ListCityCondition();

			wLabel.setText(vector.get(0).getCity()+" at "+vector.get(0).getTime());
			tempB.setText(vector.get(0).getTm()+"°C");
			Icon image;
			if(vector.get(0).getIconWt().compareTo("N/A")==0)
				image = new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/weather_images/na.png"));
			else
				image = new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/weather_images/"+vector.get(0).getIconWt()+".png"));

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
		}catch(ArrayIndexOutOfBoundsException e){
			wLabel.setText("Weather Conditions at N/A");
			tempB.setText("N/A°C");
			Icon image = new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/weather_images/na.png"));
			wStatB.setIcon(image);
			wStatB.setText("N/A");
			windB.setText("N/A");
		}

	}
	
	/**
	* Metodo que dibuja en una nueva ventana los datos del clima
	*/
	
	public void fullWeatherWindow(){
		JDialog window = new JDialog(interfaz.getMainFrame(),"Hevelius - Weather");
		window.setLayout(null);
		window.pack();	
		
		window.getContentPane().setBackground(interfaz.getDrawingPanel().getBackground());

		window.setSize(500,350);
		window.setLocationRelativeTo(null);

		window.setResizable(false);

		//Weather Status Label
		final JLabel wLabel2 = new JLabel(vector.get(0).getCity()+" Weather Conditions at "
			+vector.get(0).getTime());
		wLabel2.setSize(500,20);
		wLabel2.setForeground(Color.WHITE);
		wLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		window.add(wLabel2);

		//Latitude Label
		final JLabel latL2 = new JLabel("Latitude:");
		latL2.setSize(100,20);
		latL2.setForeground(Color.WHITE);
		window.add(latL2);

		//Longitude Label
		final JLabel lonL2 = new JLabel("Longitude:");
		lonL2.setSize(100,20);
		lonL2.setForeground(Color.WHITE);
		window.add(lonL2);

		//Sunrise Label
		final JLabel sunrL2 = new JLabel("Sunrise:");
		sunrL2.setSize(100,20);
		sunrL2.setForeground(Color.WHITE);
		window.add(sunrL2);

		//Sunset Label
		final JLabel sunsL2 = new JLabel("Sunset:");
		sunsL2.setSize(100,20);
		sunsL2.setForeground(Color.WHITE);
		window.add(sunsL2);

		//Temperature Label
		final JLabel tempL2 = new JLabel("Temperature:");
		tempL2.setSize(100,20);
		tempL2.setForeground(Color.WHITE);
		window.add(tempL2);

		//Weather Status Label
		final JLabel wStatL2 = new JLabel("Weather:");
		wStatL2.setSize(100,20);
		wStatL2.setForeground(Color.WHITE);
		window.add(wStatL2);

		//Moon Label
		final JLabel moonL2 = new JLabel("Moon Phase:");
		moonL2.setSize(100,20);
		moonL2.setForeground(Color.WHITE);
		window.add(moonL2);

		//Wind Label
		final JLabel windL2 = new JLabel("Wind:");
		windL2.setSize(100,20);
		windL2.setForeground(Color.WHITE);
		window.add(windL2);

		//Humdity Label
		final JLabel humL2 = new JLabel("Humidity:");
		humL2.setSize(100,20);
		humL2.setForeground(Color.WHITE);
		window.add(humL2);

		//Visibility Label
		final JLabel visiL2 = new JLabel("Visibility:");
		visiL2.setSize(100,20);
		visiL2.setForeground(Color.WHITE);
		window.add(visiL2);

		//UV Index Label
		final JLabel uvL2 = new JLabel("UV Index:");
		uvL2.setSize(100,20);
		uvL2.setForeground(Color.WHITE);
		window.add(uvL2);



		//Latitude Bar
		final JLabel latB2 = new JLabel(vector.get(0).getLat()+"°");
		latB2.setSize(100,20);
		latB2.setForeground(Color.WHITE);
		window.add(latB2);

		//Longitude Bar
		final JLabel lonB2 = new JLabel(vector.get(0).getLon()+"°");
		lonB2.setSize(100,20);
		lonB2.setForeground(Color.WHITE);
		window.add(lonB2);

		//Sunrise Bar
		final JLabel sunrB2 = new JLabel(vector.get(0).getSunrice());
		sunrB2.setSize(100,20);
		sunrB2.setForeground(Color.WHITE);
		window.add(sunrB2);

		//Sunset Bar
		final JLabel sunsB2 = new JLabel(vector.get(0).getSunset());
		sunsB2.setSize(100,20);
		sunsB2.setForeground(Color.WHITE);
		window.add(sunsB2);

		//Temperature Bar
		final JLabel tempB2 = new JLabel(vector.get(0).getTm()+"°C");
		tempB2.setSize(100,20);
		tempB2.setForeground(Color.WHITE);
		window.add(tempB2);

		//Weather Status
		final Icon image2;
		if(vector.get(0).getIconWt().compareTo("N/A")==0)
			image2 = new ImageIcon("Hevelius/images/weather_images/na.png");
		else
			image2 = new ImageIcon("Hevelius/images/weather_images/"+vector.get(0).getIconWt()+".png");

		final JLabel wStatB2 = new JLabel(vector.get(0).getWt(),image2,JLabel.LEFT);
		wStatB2.setSize(200,32);
		wStatB2.setForeground(Color.WHITE);
		window.add(wStatB2);

		//Wind Bar
		final JLabel windB2;
		String aux;
		try{
			Integer.parseInt(vector.get(0).getVwind());
			aux = vector.get(0).getVwind()+" Km/Hr "+vector.get(0).getDirec();
		}catch(NumberFormatException e){
			aux = vector.get(0).getDirec();
		}
		windB2 = new JLabel(aux);
		windB2.setSize(150,20);
		windB2.setForeground(Color.WHITE);
		window.add(windB2);

		//Moon Bar
		final JLabel moonB2 = new JLabel(vector.get(0).getMoon());
		moonB2.setSize(150,20);
		moonB2.setForeground(Color.WHITE);
		window.add(moonB2);

		//Humdity Bar
		final JLabel humB2 = new JLabel(vector.get(0).getHumil()+"%");
		humB2.setSize(100,20);
		humB2.setForeground(Color.WHITE);
		window.add(humB2);

		//Visibility Bar
		final JLabel visiB2 = new JLabel(vector.get(0).getVisi()+" Kilometers");
		visiB2.setSize(100,20);
		visiB2.setForeground(Color.WHITE);
		window.add(visiB2);

		//UV Index Bar
		final JLabel uvB2 = new JLabel(vector.get(0).getUv());
		uvB2.setSize(100,20);
		uvB2.setForeground(Color.WHITE);
		window.add(uvB2);



		wLabel2.setLocation(10,10);

		latL2.setLocation(10,45);

		lonL2.setLocation(10,70);

		sunrL2.setLocation(270,45);

		sunsL2.setLocation(270,70);

		tempL2.setLocation(10,110);

		wStatL2.setLocation(10,135);

		moonL2.setLocation(10,160);

		windL2.setLocation(10,185);

		humL2.setLocation(10,210);

		visiL2.setLocation(10,235);

		uvL2.setLocation(10,260);


		latB2.setLocation(120,45);

		lonB2.setLocation(120,70);

		sunrB2.setLocation(380,45);

		sunsB2.setLocation(380,70);

		tempB2.setLocation(120,110);

		wStatB2.setLocation(120,129);

		moonB2.setLocation(120,160);

		windB2.setLocation(120,185);

		humB2.setLocation(120,210);

		visiB2.setLocation(120,235);

		uvB2.setLocation(120,260);

//		more.setLocation(200,160);



		//Weather Pictures Label
		final Icon image3;
		if(vector.get(0).getIconWt().compareTo("N/A")==0)
			image3 = new ImageIcon("Hevelius/images/big_weather_images/na.png");
		else
			image3 = new ImageIcon("Hevelius/images/big_weather_images/"+vector.get(0).getIconWt()+".png");

		final JLabel bigWt = new JLabel(image3);
		bigWt.setSize(150,150);
		bigWt.setLocation(305,125);
		bigWt.setHorizontalAlignment(SwingConstants.CENTER);
		bigWt.setVerticalAlignment(SwingConstants.CENTER);
		window.add(bigWt);



		JButton reload = new JButton("Reload Weather Conditions");
		reload.setSize(200,15);
		reload.setMargin(new Insets(0,0,0,0));
		reload.setLocation(150,290);
		reload.addActionListener(new ActionListener(  ) {
				public void actionPerformed(ActionEvent event) {
					try{		
						wLabel2.setText(vector.get(0).getCity()+" Weather Conditions at "+vector.get(0).getTime());

						latB2.setText(vector.get(0).getLat()+"°");

						lonB2.setText(vector.get(0).getLon()+"°");

						sunrB2.setText(vector.get(0).getSunrice());

						sunsB2.setText(vector.get(0).getSunset());

						tempB2.setText(vector.get(0).getTm()+"°C");

						Icon image2;
						if(vector.get(0).getIconWt().compareTo("N/A")==0)
							image2 = new ImageIcon("Hevelius/images/weather_images/na.png");
						else
							image2 = new ImageIcon("Hevelius/images/weather_images/"+vector.get(0).getIconWt()+".png");
						wStatB2.setIcon(image2);
						wStatB2.setText(vector.get(0).getWt());

						try{
							Integer.parseInt(vector.get(0).getVwind());
							windB2.setText(vector.get(0).getVwind()+" Km/Hr "+vector.get(0).getDirec());
						}catch(NumberFormatException e){
							windB2.setText(vector.get(0).getDirec());
						}

						moonB2.setText(vector.get(0).getMoon());

						humB2.setText(vector.get(0).getHumil()+"%");

						visiB2.setText(vector.get(0).getVisi()+" Kilometers");

						uvB2.setText(vector.get(0).getUv());

						Icon image3;
						if(vector.get(0).getIconWt().compareTo("N/A")==0)
							image3 = new ImageIcon("Hevelius/images/big_weather_images/na.png");
						else
							image3 = new ImageIcon("Hevelius/images/big_weather_images/"+vector.get(0).getIconWt()+".png");
						bigWt.setIcon(image3);
					}catch(ArrayIndexOutOfBoundsException e){
						System.out.println("Array out of bounds");						
					}

				}
				});
		window.add(reload);
		window.setVisible(true);
	}

}
