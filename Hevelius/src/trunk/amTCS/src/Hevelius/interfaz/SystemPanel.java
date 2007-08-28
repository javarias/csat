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
					interfaz.getDrawingPanel().setCSATControl(CSATControlClient.start());
					interfaz.getDrawingPanel().setCSATStatus(CSATStatusClient.start());
                        }
                });

		//TCS Stop Button
		stop = new JButton("Stop TCS");
		stop.setSize(100,20);
		add(stop);
		stop.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
				stopTCS();
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
		system.setLocation(10,5);
		start.setLocation(10,30);
		stop.setLocation(10,55);
		shutdown.setLocation(10,80);
	}

	public void stopTCS()
	{
			CSATControlClient.stop(interfaz.getDrawingPanel().getCSATControl());
			interfaz.getDrawingPanel().setCSATControl(null);
			CSATStatusClient.stop(interfaz.getDrawingPanel().getCSATStatus());
			interfaz.getDrawingPanel().setCSATStatus(null);
	}
}
