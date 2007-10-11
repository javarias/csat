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
		super.paintComponent(g);

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
