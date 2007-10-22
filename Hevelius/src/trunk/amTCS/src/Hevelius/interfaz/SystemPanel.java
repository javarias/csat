package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Hevelius.acsmodules.*;

import alma.acs.component.ComponentLifecycleException;

public class SystemPanel extends JPanel
{
	//private JLabel system;
	private JButton start;
	private JButton stop;
	private JButton shutdown;

	private int dx = 0;
	private int dy = 0;

	public SystemPanel(LayoutManager l)
	{
		super(l);
	}
	public void init()
	{
		//System Label
	/*	system = new JLabel("System");
		system.setSize(100,20);
		system.setForeground(Color.WHITE);
		add(system);*/

		//TCS Start Button
		start = new JButton("Start TCS");
		start.setSize(100,20);
		add(start);
		start.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
				interfaz.getDrawingPanel().getMenuPanel().getWindowLogin().setLoginWindow();
				interfaz.getDrawingPanel().enablePanel(interfaz.getDrawingPanel().getWeatherPanel());
                                interfaz.getDrawingPanel().enablePanel(interfaz.getDrawingPanel().getTelStatusPanel());
                                interfaz.getDrawingPanel().enablePanel(interfaz.getDrawingPanel().getScreenPanel());
                                interfaz.getDrawingPanel().enablePanel(interfaz.getDrawingPanel().getCoordinatesPanel());
                                interfaz.getDrawingPanel().enablePanel(interfaz.getDrawingPanel().getVirtualTelescopePanel());
/*                                interfaz.getDrawingPanel().enablePanel(interfaz.getDrawingPanel().getWeatherPanel());
                                interfaz.getDrawingPanel().enablePanel(interfaz.getDrawingPanel().getWeatherPanel());*/
//				interfaz.getDrawingPanel().getMenuPanel().getWindowLogin().setVisible(true);
//			if(interfaz.getDrawingPanel().getMenuPanel().getWindowLogin().isVisible())
//				private static Configuration conf = new Configuration();
				
				

/*				try
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
				}*/
//				System.out.println("vincler la mazca");
                        }
                });

		//TCS Stop Button
		stop = new JButton("Stop TCS");
		stop.setSize(100,20);
		add(stop);
		stop.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
				stopTCS();
				interfaz.getDrawingPanel().getCoordinatesPanel().setAltOffset(0.0);
				interfaz.getDrawingPanel().getCoordinatesPanel().setAzOffset(0.0);
                                interfaz.getDrawingPanel().disablePanel(interfaz.getDrawingPanel().getWeatherPanel());
                                interfaz.getDrawingPanel().disablePanel(interfaz.getDrawingPanel().getTelStatusPanel());
                                interfaz.getDrawingPanel().disablePanel(interfaz.getDrawingPanel().getScreenPanel());
                                interfaz.getDrawingPanel().disablePanel(interfaz.getDrawingPanel().getCoordinatesPanel());
                                interfaz.getDrawingPanel().disablePanel(interfaz.getDrawingPanel().getVirtualTelescopePanel());
                        }
                });

		//Shutdown Button
		shutdown = new JButton("Shutdown");
		shutdown.setSize(100,20);
		add(shutdown);

		shutdown.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				stopTCS();
				System.exit(0);
			}
		});
	}
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
			if(dx/280.0f>dy/135.0f)
			{
				fsize = (12.0f*dy)/135.0f;
				osize = dy/135.0f;
			}
			else
			{
				fsize = (12.0f*dx)/280.0f;
				osize = dx/280.0f;
			}
		//	System.out.println(dx+" "+dy);
			/*system.setLocation((int)(150*osize),(int)(5*osize));
			system.setFont(system.getFont().deriveFont(fsize));
			system.setSize((int)(100*osize),(int)(20*osize));*/

			start.setLocation((int)(100*osize),(int)(30*osize));
			start.setFont(start.getFont().deriveFont(fsize));
			start.setSize((int)(100*osize),(int)(20*osize));

			stop.setLocation((int)(100*osize),(int)(55*osize));
			stop.setFont(stop.getFont().deriveFont(fsize));
			stop.setSize((int)(100*osize),(int)(20*osize));

			shutdown.setLocation((int)(100*osize),(int)(80*osize));
			shutdown.setFont(shutdown.getFont().deriveFont(fsize));
			shutdown.setSize((int)(100*osize),(int)(20*osize));
		}
	}

	public void stopTCS()
	{
			CSATControlClient temp1;
			CSATStatusClient temp2;
			if(interfaz.getDrawingPanel().getCSATControl()!=null)
				interfaz.getDrawingPanel().getCSATControl().stopTelescope();
			temp1 = interfaz.getDrawingPanel().getCSATControl();
			temp2 = interfaz.getDrawingPanel().getCSATStatus();
			interfaz.getDrawingPanel().setCSATControl(null);
			interfaz.getDrawingPanel().setCSATStatus(null);
			CSATControlClient.stop(temp1);
			CSATStatusClient.stop(temp2);
			interfaz.getDrawingPanel().getTelStatusPanel().setTrackingState(0);
			interfaz.getDrawingPanel().getTelStatusPanel().setPointingState(0);
			interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(0);
	}
}
