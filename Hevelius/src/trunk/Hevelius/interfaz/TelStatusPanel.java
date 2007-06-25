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
		init();
	}
	public void init()
	{
		telstate = new JLabel("TELESCOPE STATE");

		glstateL = new JLabel("Global State");

		modeswL = new JLabel("Mode Switch");

		trkwsL = new JLabel("Trk.WS");

		autogL = new JLabel("Autog");

		glstate = new JLabel("ONLINE");

		modesw = new JLabel("IDLE");

		trkws = new JLabel("IDLE");

		autog = new JLabel("Ag Idle");
	}
}
