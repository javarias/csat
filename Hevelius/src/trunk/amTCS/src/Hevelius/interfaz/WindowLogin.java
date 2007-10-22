package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

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
				//jTextField1_actionPerformed(e);
				/*					conf.setOption("user",jTextField1.getText());
									conf.store();
									hist.addHistoryLogin(jTextField1.getText());
									setVisible(false);
				 */					
				name = jTextField1.getText();
				name = name.trim();
				pat = Pattern.compile("(\\S*)(\\s*)(\\S)");
				System.out.println("NAME ="+name);
				mat = pat.matcher(name);
				System.out.println("Matcher ="+mat.find());
				if(!mat.find()){

				System.out.println("Nombre de Login =" + name);
				if(interfaz.getDrawingPanel().getCSATControl() == null && interfaz.getDrawingPanel().getCSATStatus() == null ){
				conf.setOption("user",jTextField1.getText());
				conf.store();
				hist.addHistoryLogin(jTextField1.getText());
				//                                        		setVisible(false);


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
				}
				else{
					System.out.println("NO CARGO ACS CTM !!");

					//init();

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
				public void actionPerformed(ActionEvent event) {



				name = jTextField1.getText();
				name = name.trim();
				pat = Pattern.compile("(\\S*)(\\s*)(\\S)");
				System.out.println("NAME ="+name);
				mat = pat.matcher(name);
				System.out.println("Matcher ="+mat.find());
				if(!mat.find()){

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
				else{
					System.out.println("NO CARGO ACS CTM !!");

					//init();

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
