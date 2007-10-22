package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JComponent.*;
import javax.swing.JOptionPane;

import java.util.*;
import java.util.regex.*;
import Hevelius.weather.*;
import Hevelius.utilities.historial.*;

import Hevelius.acsmodules.*;
import alma.acs.component.ComponentLifecycleException;

public class WindowLogin extends JDialog 
{
	private JLabel usser;
	private JTextField jTextField1;
	private JButton set;


	Pattern pat;
	Matcher mat;

	private int dx = 0;
	private int dy = 0;
	private String name;
	private String message = "ussername no puede contener espacios, ingerese un usuario valido !";
	private static Configuration conf = new Configuration();
	private static Historial hist = new Historial();		

	public WindowLogin(JFrame f, String s)
	{
		super(f, s, true);
	}

	public void init()
	{

		//System.out.println("LOGIN COOL");
		//JDialog window = new JDialog(interfaz.getMainFrame(),"Hevelius - Login");
		setLayout(null);
		pack();

		getContentPane().setBackground(Color.BLACK);

		setSize(335,141);
		setLocationRelativeTo(null);
		//setBackground(Color.BLACK);

		setResizable(false);

		//Usser Label
		usser = new JLabel("username:");
		usser.setSize(100,20);
		usser.setForeground(Color.WHITE);
		usser.setFont(usser.getFont().deriveFont(15.0f));
		add(usser);

		//text field

		jTextField1 = new JTextField();
		jTextField1.setForeground(new Color(0, 0, 0 ));
		jTextField1.setSelectedTextColor(new Color(0, 0, 0));
		jTextField1.setSize(130,20);
		//jTextField1.setText("");
		jTextField1.addActionListener(new ActionListener() {


				public void actionPerformed(ActionEvent e)
				{
				name = jTextField1.getText();
				name = name.trim();
				//pat = Pattern.compile("(\\S+)(\\s+)(\\S+)");
				pat = Pattern.compile("(.+)(\\s)(.+)");
				mat = pat.matcher(name);
				System.out.println("NAME ="+name);
				if( name.equals(""))
				{
				JOptionPane.showMessageDialog(interfaz.getMainFrame(), "Ingrese un usuario valido !","Login - Error", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{

				mat = pat.matcher(name);
				if( mat.find() )
				{
					JOptionPane.showMessageDialog(interfaz.getMainFrame(), message,"Login - Error", JOptionPane.INFORMATION_MESSAGE);
				}
				else {


					if(interfaz.getDrawingPanel().getCSATControl() == null && interfaz.getDrawingPanel().getCSATStatus() == null){

						/* conf.setOption("user",jTextField1.getText());
						   conf.store();
						   hist.addHistoryLogin(jTextField1.getText());*/

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


					conf.setOption("user",jTextField1.getText());
					conf.store();
					hist.addHistoryLogin(jTextField1.getText());
					System.out.println(jTextField1.getText());
					//jTextField1.setText(" ");
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
		//add(set);
		jTextField1.setText("");
		set.addActionListener(new ActionListener(  ) {
				public void actionPerformed(ActionEvent e)
				{
				name = jTextField1.getText();
				name = name.trim();
				//pat = Pattern.compile("(\\S+)(\\s+)(\\S+)");
				pat = Pattern.compile("(.+)(\\s)(.+)");
				mat = pat.matcher(name);
				//System.out.println("NAME ="+name);
				if( name.equals(""))
				{
				JOptionPane.showMessageDialog(interfaz.getMainFrame(), "Ingrese un usuario valido !","Login - Error", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{

				mat = pat.matcher(name);
				if( mat.find() )
				{
				JOptionPane.showMessageDialog(interfaz.getMainFrame(), message,"Login - Error", JOptionPane.INFORMATION_MESSAGE);
				}
				else {


					if(interfaz.getDrawingPanel().getCSATControl() == null && interfaz.getDrawingPanel().getCSATStatus() == null){

						/* conf.setOption("user",jTextField1.getText());
						   conf.store();
						   hist.addHistoryLogin(jTextField1.getText());*/

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
							//err.printStackTrace();
						}
					}


					conf.setOption("user",jTextField1.getText());
					conf.store();
					hist.addHistoryLogin(jTextField1.getText());
					System.out.println(jTextField1.getText());
					//jTextField1.setText(" ");
				}
				}
		setVisible(false);
				}
		});

		add(set);


		usser.setLocation(50,20);

		jTextField1.setLocation(150,20);

		set.setLocation(150,70);


		//setVisible(true);

	}

	public void setLoginWindow(){
		setVisible(true);
		jTextField1.setText("");

	}

}
