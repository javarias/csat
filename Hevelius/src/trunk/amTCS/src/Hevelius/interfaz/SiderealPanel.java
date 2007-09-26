package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import Hevelius.acsmodules.*;
import Hevelius.heveliusmodules.*;
import Hevelius.interfaz.*;
import alma.TYPES.*;

public class SiderealPanel extends JPanel
{
	//private JLabel telstate;
//	private JLabel stimeL;
        private JLabel stime;
        private JLabel stimeh;

	private int dx = 0;
	private int dy = 0;

	private int state = 0;
	private int tracking = 0;
	private int presetting = 0;
	private int pointing = 0;

	public SiderealPanel(LayoutManager l)
	{
		super(l);
	}
	public void init()
	{
		/*telstate = new JLabel("TELESCOPE STATE");
		telstate.setSize(200, 20);
		telstate.setForeground(Color.WHITE);
		add(telstate);*/

		/*stimeL = new JLabel("Sidereal Time");
                stimeL.setSize(120,20);
                stimeL.setForeground(Color.WHITE);
                add(stimeL);*/

                //Sidereal Time
                stime = new JLabel("0.000");
                stime.setSize(60,20);
                stime.setForeground(Color.WHITE);
                add(stime);

                //Sideral Time in Hour
                stimeh = new JLabel("00:00:00");
                stimeh.setSize(100,20);
                stimeh.setForeground(Color.WHITE);
                add(stimeh);

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
			if(dx/280.0f>dy/67.0f)
			{
				fsize = (12.0f*dy)/67.0f;
				osize = dy/67.0f;
			}
			else
			{
				fsize = (12.0f*dx)/280.0f;
				osize = dx/280.0f;
			}

//			System.out.println(dx+" "+dy);
                	/*telstate.setLocation((int)(100*osize),(int)(0*osize));
			telstate.setFont(telstate.getFont().deriveFont(fsize));
			telstate.setSize((int)(200*osize),(int)(20*osize));*/

			/*stimeL.setLocation(dx*4/7-60,dy/6-dy/40+dy/3);
                        stime.setLocation(dx*4/7-30,dy/6-dy/40+dy/3+20);
                        stimeh.setLocation(dx*4/7-40,dy/6-dy/40+dy/3+40);*/

		//	stimeL.setLocation((int)(100*osize),(int)(0*osize));
                        stime.setLocation((int)(100*osize),(int)(20*osize));
			stime.setFont(stime.getFont().deriveFont(fsize));
                        stime.setSize((int)(60*osize),(int)(20*osize));

                        stimeh.setLocation((int)(100*osize),(int)(40*osize));
			stimeh.setFont(stimeh.getFont().deriveFont(fsize));
                        stimeh.setSize((int)(100*osize),(int)(20*osize));


		}
	}

	public void setTime(String time1, String time2){
		stime.setText(time2);
		stimeh.setText(time1);
	}	

}
