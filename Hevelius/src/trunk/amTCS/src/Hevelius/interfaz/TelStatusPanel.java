package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import Hevelius.acsmodules.*;
import Hevelius.heveliusmodules.*;
import Hevelius.interfaz.*;
import alma.TYPES.*;

public class TelStatusPanel extends JPanel
{
	//private JLabel telstate;
	private JLabel glstateL;
	private JLabel modeswL;
	private JLabel trkwsL;
	private JLabel autogL;
	private JLabel safetyL;
	private JLabel glstate;
	private JLabel modesw;
	private JLabel trkws;
	private JLabel autog;
	private JLabel safety;

	private int dx = 0;
	private int dy = 0;

	private int state = 0;
	private int tracking = 0;
	private int presetting = 0;
	private int pointing = 0;

	/**
	* Constructor de la clase
	*/
	public TelStatusPanel(LayoutManager l)
	{
		super(l);
	}
	/**
	* Metodo encargado de dibujar los label y botones
	*/
	public void init()
	{
		/*telstate = new JLabel("TELESCOPE STATE");
		telstate.setSize(200, 20);
		telstate.setForeground(Color.WHITE);
		add(telstate);*/

		glstateL = new JLabel("Global State");
		glstateL.setSize(200,20);
		glstateL.setForeground(Color.WHITE);
		add(glstateL);

		modeswL = new JLabel("Presetting");
		modeswL.setSize(200,20);
		modeswL.setForeground(Color.WHITE);
		add(modeswL);

		trkwsL = new JLabel("Tracking");
		trkwsL.setSize(100,20);
		trkwsL.setForeground(Color.WHITE);
		add(trkwsL);

		autogL = new JLabel("Pointing");
		autogL.setSize(100,20);
		autogL.setForeground(Color.WHITE);
		add(autogL);

		safetyL = new JLabel("Danger");
		safetyL.setSize(100,20);
                safetyL.setForeground(Color.WHITE);
                add(safetyL);

                glstate = new JLabel("Off");
		glstate.setSize(100,20);
		glstate.setForeground(Color.WHITE);
		add(glstate);

		modesw = new JLabel("Off");
		modesw.setSize(100,20);
		modesw.setForeground(Color.WHITE);
		add(modesw);

		trkws = new JLabel("Off");
		trkws.setSize(100,20);
		trkws.setForeground(Color.WHITE);
		add(trkws);

		autog = new JLabel("Off");
		autog.setSize(100,20);
		autog.setForeground(Color.WHITE);
		add(autog);

		safety = new JLabel("N/A");
                safety.setSize(100,20);
                safety.setForeground(Color.WHITE);
                add(safety);

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

                	/*telstate.setLocation((int)(100*osize),(int)(0*osize));
			telstate.setFont(telstate.getFont().deriveFont(fsize));
			telstate.setSize((int)(200*osize),(int)(20*osize));*/

	                glstateL.setLocation((int)(80*osize),(int)(25*osize));
			glstateL.setFont(glstateL.getFont().deriveFont(fsize));
			glstateL.setSize((int)(200*osize),(int)(20*osize));

        	        modeswL.setLocation((int)(80*osize),(int)(50*osize));
			modeswL.setFont(modeswL.getFont().deriveFont(fsize));
			modeswL.setSize((int)(200*osize),(int)(20*osize));

                	trkwsL.setLocation((int)(80*osize),(int)(75*osize));
			trkwsL.setFont(trkwsL.getFont().deriveFont(fsize));
			trkwsL.setSize((int)(100*osize),(int)(20*osize));

	                autogL.setLocation((int)(80*osize),(int)(100*osize));
			autogL.setFont(autogL.getFont().deriveFont(fsize));
			autogL.setSize((int)(100*osize),(int)(20*osize));
			
			safetyL.setLocation((int)(80*osize),(int)(125*osize));
                        safetyL.setFont(safetyL.getFont().deriveFont(fsize));
                        safetyL.setSize((int)(100*osize),(int)(20*osize));

        	        glstate.setLocation((int)(180*osize),(int)(25*osize));
			glstate.setFont(glstate.getFont().deriveFont(fsize));
			glstate.setSize((int)(100*osize),(int)(20*osize));

                	modesw.setLocation((int)(180*osize),(int)(50*osize));
			modesw.setFont(modesw.getFont().deriveFont(fsize));
			modesw.setSize((int)(100*osize),(int)(20*osize));

	                trkws.setLocation((int)(180*osize),(int)(75*osize));
			trkws.setFont(trkws.getFont().deriveFont(fsize));
			trkws.setSize((int)(100*osize),(int)(20*osize));

        	        autog.setLocation((int)(180*osize),(int)(100*osize));
			autog.setFont(autog.getFont().deriveFont(fsize));
			autog.setSize((int)(100*osize),(int)(20*osize));

			safety.setLocation((int)(180*osize),(int)(125*osize));
                        safety.setFont(safety.getFont().deriveFont(fsize));
                        safety.setSize((int)(100*osize),(int)(20*osize));

		}
	}
	/**
	* Metodo encargado de setar el tipo de estado en que se encuentra
	* alguna funcion
	* @param state	Int que contiene un numero correspondiente al estado
	*/
	
	public void setGlobalState(int state)
	{
		this.state = state;
		switch(state)
		{
			case 0: glstate.setText("Off"); break;
			case 1: glstate.setText("Idle"); break;
			case 2: glstate.setText("Presetting"); break;
			case 3: glstate.setText("Observing"); break;
			case 4: glstate.setText("Calibrating"); break;
			case 5: glstate.setText("Error"); break;
		}
	}
	/**
	* Metodo que setea el estado actual del tracking
	* @param state	Int contiene el numero correspondiente al estado.
	*/
	
	public void setTrackingState(int state)
	{
		this.tracking = state;
		switch(state)
                {
			case 0: trkws.setText("Off"); break;
			case 1: trkws.setText("Idle"); break;
			case 2: trkws.setText("On"); break;
			case 3: trkws.setText("Error"); break;
		}
		setState();
	}
	
	
	/**
        * Metodo que setea el estado actual del pointing
        * @param state  Int contiene el numero correspondiente al estado.
        */

	public void setPointingState(int state)
	{
		this.pointing = state;
                switch(state)
                {
			case 0: autog.setText("Off"); break;
			case 1: autog.setText("Idle"); break;
			case 2: autog.setText("Calibrating"); break;
                        case 3: autog.setText("Error"); break;
		}
		setState();
	}
	
	/**
        * Metodo que setea el estado actual del Presetting
        * @param state  Int contiene el numero correspondiente al estado.
        */

	public void setPresettingState(int state)
	{
		this.presetting = state;
		switch(state)
                {
                        case 0: modesw.setText("Off"); break;
			case 1: modesw.setText("Idle"); break;
			case 2: modesw.setText("Presetting"); break;
			case 3: modesw.setText("Error"); break;
		}
		setState();
	}

	public void setState()
	{
		if(tracking==0 && presetting==0 && pointing==0)
		{
			setGlobalState(0);
		}
		else if(tracking==1 && presetting==1 && pointing==1)
		{
			setGlobalState(1);
		}
		else if((tracking==1 || tracking==2) && presetting==0 && (pointing==1 || pointing==2))
		{
			setGlobalState(4);
		}
		else if(tracking==3 || presetting==3 || pointing==3)
		{
			tracking = 3;
			presetting = 3;
			pointing = 3;
			setGlobalState(5);
		}
                else if(tracking==2 && presetting==1 && pointing==1)
                {
			setGlobalState(3);
                }
                else if((tracking==1 || tracking==2) && presetting==2 && pointing==1)
                {
			setGlobalState(2);
                }
	}
	
	/**
	* Metodo encargado de setear el nivel de peligrosidad
	* segun el estado.
	* @param state	Int que contiene el estado
	*/
	
	public void setDangerState(int state)
	{
		switch(state)
		{
			case 0: safety.setText("N/A"); 
				safety.setForeground(Color.WHITE);
				break;

			case 1: safety.setText("Low");
				safety.setForeground(Color.GREEN);
				break;

			case 2: safety.setText("Moderate");
				safety.setForeground(Color.YELLOW);
				break;

			case 3: safety.setText("High");
				safety.setForeground(Color.ORANGE);
				break;
	
			case 4: safety.setText("Extreme");
				safety.setForeground(Color.RED);
				break;

			default: 	safety.setText("N/A"); 
					safety.setForeground(Color.WHITE);
					break;
		}
	}
}
