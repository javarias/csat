package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelStatusPanel extends JPanel
{
	private JLabel telstate;
	private JLabel glstateL;
	private JLabel modeswL;
	private JLabel trkwsL;
	private JLabel autogL;
	private JLabel glstate;
	private JLabel modesw;
	private JLabel trkws;
	private JLabel autog;

	private int dx = 0;
	private int dy = 0;

	public TelStatusPanel(LayoutManager l)
	{
		super(l);
	}
	public void init()
	{
		telstate = new JLabel("TELESCOPE STATE");
		telstate.setSize(200, 20);
		telstate.setForeground(Color.WHITE);
		add(telstate);

		glstateL = new JLabel("Global State");
		glstateL.setSize(200,20);
		glstateL.setForeground(Color.WHITE);
		add(glstateL);

		modeswL = new JLabel("Mode Switch");
		modeswL.setSize(200,20);
		modeswL.setForeground(Color.WHITE);
		add(modeswL);

		trkwsL = new JLabel("Trk.WS");
		trkwsL.setSize(100,20);
		trkwsL.setForeground(Color.WHITE);
		add(trkwsL);

		autogL = new JLabel("Autog");
		autogL.setSize(100,20);
		autogL.setForeground(Color.WHITE);
		add(autogL);

		glstate = new JLabel("ONLINE");
		glstate.setSize(100,20);
		glstate.setForeground(Color.WHITE);
		add(glstate);

		modesw = new JLabel("IDLE");
		modesw.setSize(100,20);
		modesw.setForeground(Color.WHITE);
		add(modesw);

		trkws = new JLabel("IDLE");
		trkws.setSize(100,20);
		trkws.setForeground(Color.WHITE);
		add(trkws);

		autog = new JLabel("Ag Idle");
		autog.setSize(100,20);
		autog.setForeground(Color.WHITE);
		add(autog);
	}
	public void paintComponent(Graphics g)
	{
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

                	telstate.setLocation((int)(100*osize),(int)(0*osize));
			telstate.setFont(telstate.getFont().deriveFont(fsize));
			telstate.setSize((int)(200*osize),(int)(20*osize));

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
		}
	}

	public void setGlobalState(int state)
	{
		switch(state)
		{
			case 0: glstate.setText("OFF");
		}
	}

	public void setTrackingState()
	{

	}

	public void setPointingState()
	{
		
	}

	public void setPresettingState()
	{

	}
}
