package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Hevelius.acsmodules.*;

import alma.acs.component.ComponentLifecycleException;

public class SystemPanel extends JPanel
{
	private JLabel system;
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
		system = new JLabel("System");
		system.setSize(100,20);
		system.setForeground(Color.WHITE);
		add(system);

		//TCS Start Button
		start = new JButton("Start TCS");
		start.setSize(100,20);
		add(start);
		start.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
				try
				{
					interfaz.getDrawingPanel().setCSATControl(CSATControlClient.start());
					interfaz.getDrawingPanel().setCSATStatus(CSATStatusClient.start());
				}
				catch(Exception err)
				{
					err.printStackTrace();
				}
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
			if(dx/280.0f>dy/154.0f)
			{
				fsize = (12.0f*dy)/154.0f;
				osize = dy/154.0f;
			}
			else
			{
				fsize = (12.0f*dx)/280.0f;
				osize = dx/280.0f;
			}

			system.setLocation((int)(150*osize),(int)(5*osize));
			system.setFont(system.getFont().deriveFont(fsize));
			system.setSize((int)(100*osize),(int)(20*osize));

			start.setLocation((int)(150*osize),(int)(30*osize));
			start.setFont(start.getFont().deriveFont(fsize));
			start.setSize((int)(100*osize),(int)(20*osize));

			stop.setLocation((int)(150*osize),(int)(55*osize));
			stop.setFont(stop.getFont().deriveFont(fsize));
			stop.setSize((int)(100*osize),(int)(20*osize));

			shutdown.setLocation((int)(150*osize),(int)(80*osize));
			shutdown.setFont(shutdown.getFont().deriveFont(fsize));
			shutdown.setSize((int)(100*osize),(int)(20*osize));
		}
	}

	public void stopTCS()
	{
			CSATControlClient.stop(interfaz.getDrawingPanel().getCSATControl());
			interfaz.getDrawingPanel().setCSATControl(null);
			CSATStatusClient.stop(interfaz.getDrawingPanel().getCSATStatus());
			interfaz.getDrawingPanel().setCSATStatus(null);
	}
}
