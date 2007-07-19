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
		int dx = getSize().width;
		int dy = getSize().height;
		telstate.setLocation(30,0);
		//telstate.setHorizontalAlignment(SwingConstants.CENTER);
		glstateL.setLocation(10,25);
		modeswL.setLocation(10,50);
		trkwsL.setLocation(10,75);
		autogL.setLocation(10,100);
		glstate.setLocation(110,25);
		modesw.setLocation(110,50);
		trkws.setLocation(110,75);
		autog.setLocation(110,100);
	}
}
