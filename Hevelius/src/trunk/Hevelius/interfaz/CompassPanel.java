package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CompassPanel extends JPanel
{
	private JLabel northL;
	private JLabel southL;
	private JLabel eastL;
	private JLabel westL;
	
	private Image compassRose;	

	private int dx;
	private int dy;
	private int r;
	private int pointx;
	private int pointy;
	public CompassPanel(LayoutManager l)
	{
		super(l);
		init();
	}
	private void init()
	{
		//North Label
		northL = new JLabel("N");
		northL.setSize(20,20);
		northL.setForeground(Color.WHITE);
		add(northL);

		//South Label
		southL = new JLabel("S");
		southL.setSize(20,20);
		southL.setForeground(Color.WHITE);
		add(southL);

		//East Label
		eastL = new JLabel("E");
		eastL.setSize(20,20);
		eastL.setForeground(Color.WHITE);
		add(eastL);

		//West Label
		westL = new JLabel("W");
		westL.setSize(20,20);
		westL.setForeground(Color.WHITE);
		add(westL);
		
		compassRose = Toolkit.getDefaultToolkit().getImage("Hevelius/images/compass_rose2.jpg");
		compassRose = Transparency.makeColorTransparent(compassRose, Color.WHITE);
		compassRose = compassRose.getScaledInstance(140,140,Image.SCALE_FAST);

		setBackground(Color.BLACK);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int i;
		dx = getSize().width;
		dy = getSize().height;
		r = 5*dx/12;
		g.setColor(Color.GRAY);
		g.drawOval(dx/12,dy/12,2*r,2*r);
		g.fillOval(dx/12,dy/12,2*r,2*r);

		g.setColor(Color.WHITE);
		g.fillOval(dx/12+5,dy/12+5,2*r-10,2*r-10);

		northL.setLocation(dx/2-2,dy/2-r-20);
		southL.setLocation(dx/2-8,dy/2+r+2);
		eastL.setLocation(dx/2+r+8,dy/2-10);
		westL.setLocation(dx/2-r-15,dy/2-10);

		g.drawImage(compassRose,dx/2-70,dy/2-70,this);
		
		g.setColor(Color.DARK_GRAY);

		g.drawLine(dx/2,dy/2,pointx,pointy);
		g.drawLine(dx/2+1,dy/2+1,pointx+1,pointy+1);
		g.drawLine(dx/2-1,dy/2-1,pointx-1,pointy-1);
		g.setColor(Color.BLACK);
		g.drawOval(dx/2-5,dy/2-5,10,10);
		g.fillOval(dx/2-5,dy/2-5,10,10);


	}
	public void setCompassPoints(double dec)
	{
		double theta = dec*Math.PI/180;
		pointx = (int)(((double)r-10)*Math.cos(theta)+(double)dx/2);
		pointy = (int)(-((double)r-10)*Math.sin(theta)+(double)dy/2);
		Rectangle re = new Rectangle(0,0,dx,dy);
		paintImmediately(re);
	}
}
