package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import Hevelius.weather.*;
import Hevelius.utilities.historial.*;

import Hevelius.acsmodules.*;
import alma.acs.component.ComponentLifecycleException;

public class WindowLogin extends JDialog 
{
	private JLabel usser;
	private JTextField jTextField1;
	private JButton set;

	private int dx = 0;
	private int dy = 0;
	
	private static Configuration conf = new Configuration();
	private static Historial hist = new Historial();		

	public WindowLogin(JFrame f, String s)
	{
		super(f, s);
	}

	public void init()
	{

		//JDialog window = new JDialog(interfaz.getMainFrame(),"Hevelius - Login");
                setLayout(null);
                pack();

                getContentPane().setBackground(interfaz.getDrawingPanel().getBackground());

                setSize(335,141);
                setLocationRelativeTo(null);

                setResizable(false);

		//Usser Label
		usser = new JLabel("ussername:");
		usser.setSize(100,20);
		usser.setForeground(Color.WHITE);
		usser.setFont(usser.getFont().deriveFont(15.0f));
		add(usser);
		
		//text field
		
		jTextField1 = new JTextField();
		jTextField1.setForeground(new Color(0, 0, 0 ));
                jTextField1.setSelectedTextColor(new Color(0, 0, 0));
		jTextField1.setSize(130,20);
		jTextField1.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e)
                                {
                                //jTextField1_actionPerformed(e);
/*					conf.setOption("user",jTextField1.getText());
                                        conf.store();
					hist.addHistoryLogin(jTextField1.getText());
					setVisible(false);
*/
					if(interfaz.getDrawingPanel().getCSATControl() == null && interfaz.getDrawingPanel().getCSATStatus() == null){
					conf.setOption("user",jTextField1.getText());
                                        conf.store();
                                        hist.addHistoryLogin(jTextField1.getText());
//                                        setVisible(false);


						try
		                                {
		                                        interfaz.getDrawingPanel().setCSATControl(CSATControlClient.start());
		                                        interfaz.getDrawingPanel().setCSATStatus(CSATStatusClient.start());
		
		                                        if(interfaz.getDrawingPanel().getCSATControl() != null && interfaz.getDrawingPanel().getCSATStatus() != null){
		                                                interfaz.getDrawingPanel().getTelStatusPanel().setTrackingState(1);
		                                                interfaz.getDrawingPanel().getTelStatusPanel().setPointingState(1);
		                                                interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(1);
		                                        }
		                                }
		                                catch(Exception err)
		                                {
		                                        err.printStackTrace();
		                                }
					}

					setVisible(false);


                                }

                                });

		add(jTextField1);

		//More Button
		set = new JButton("Login");
		set.setSize(50,15);
		set.setMargin(new Insets(0,0,0,0));
		set.addActionListener(new ActionListener(  ) {
				public void actionPerformed(ActionEvent event) {
					
                                        if(interfaz.getDrawingPanel().getCSATControl() == null && interfaz.getDrawingPanel().getCSATStatus() == null){

	                                        conf.setOption("user",jTextField1.getText());
	                                        conf.store();
	                                        hist.addHistoryLogin(jTextField1.getText());

                                                try
                                                {
                                                        interfaz.getDrawingPanel().setCSATControl(CSATControlClient.start());
                                                        interfaz.getDrawingPanel().setCSATStatus(CSATStatusClient.start());

                                                        if(interfaz.getDrawingPanel().getCSATControl() != null && interfaz.getDrawingPanel().getCSATStatus() != null){
                                                                interfaz.getDrawingPanel().getTelStatusPanel().setTrackingState(1);
                                                                interfaz.getDrawingPanel().getTelStatusPanel().setPointingState(1);
                                                                interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(1);
                                                        }
                                                }
                                                catch(Exception err)
                                                {
                                                        err.printStackTrace();
                                                }
                                        }

					if(interfaz.getDrawingPanel().getCSATControl() != null && interfaz.getDrawingPanel().getCSATStatus() != null){

	                                        conf.setOption("user",jTextField1.getText());
	                                        conf.store();
	                                        hist.addHistoryLogin(jTextField1.getText());
                                        }

					setVisible(false);
				}
				});
		add(set);
		




		usser.setLocation(50,20);

               	jTextField1.setLocation(150,20);

              	set.setLocation(150,70);


		setVisible(true);

	}
/*
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

			usser.setLocation((int)(10*osize),(int)(40*osize));
			usser.setFont(usser.getFont().deriveFont(fsize));
			usser.setSize((int)(250*osize),(int)(20*osize));

			set.setLocation((int)(210*osize),(int)(190*osize));
			set.setFont(set.getFont().deriveFont(fsize));
			set.setSize((int)(50*osize),(int)(20*osize));
			
			jTextField1.setLocation((int)(10*osize),(int)(40*osize));
                        jTextField1.setFont(usser.getFont().deriveFont(fsize));
                        jTextField1.setSize((int)(250*osize),(int)(20*osize));
		}
	}
*/
	
/*	public void LoginScreen(){
		JDialog window = new JDialog(interfaz.getMainFrame(),"Hevelius - Login");
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
	}*/

}
