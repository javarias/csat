package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;

public class VirtualTelescopePanel extends JPanel
{
	public static int gearDisplayList;
	private GLCanvas canvas;
	private GLCanvas canvas2;
	private JDialog dialog;
	private Listener list;
	public VirtualTelescopePanel(LayoutManager l)
	{
		super(l);
	}
	public void init()
	{
		GLCapabilities capabilities = new GLCapabilities();
		canvas = new GLCanvas(capabilities);
		list = new Listener();
		canvas.addGLEventListener(list);
		canvas.setLocation(0,0);
		canvas.setSize(100,100);
		add(canvas);

		dialog = new JDialog(interfaz.getMainFrame(),"Telescopio");
		dialog.getContentPane().setLayout(null);
		canvas2 = new GLCanvas(null,null,canvas.getContext(),null);
		canvas2.setSize(600,600);
		canvas2.setLocation(0,0);
		canvas2.addGLEventListener(list);
		dialog.getContentPane().add(canvas2);
		dialog.pack();
		dialog.setSize(600,600);
		dialog.setLocation(100,100);
		dialog.setResizable(false);
		final Animator animator = new Animator(canvas);
		final Animator animator2 = new Animator(canvas2);
		list.setDialog(dialog,animator2);
		WindowListener windowListener = new WindowAdapter()
		{
			public void windowClosing(WindowEvent e) 
			{
				new Thread(new Runnable() 
						{
							public void run() 
				{
					animator.stop();
					System.exit(0);
				}
				}).start();
			}
		};
		WindowListener windowListener2 = new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				new Thread(new Runnable()
						{
							public void run()
				{
					animator2.stop();
				}
				}).start();
			}
		};
		dialog.addWindowListener(windowListener2);
		interfaz.getMainFrame().addWindowListener(windowListener);
		animator.start();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int dx, dy;
		Dimension d = getSize();
		Dimension d2 = getSize();
		d2.width = d2.width*2;
		d2.height = d2.height*2;
		dx = d.width;
		dy = d.height;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,dx,dy);
		g.setColor(Color.BLACK);
		g.fillRect(2,2,dx-4,dy-4);
		d.width -= 10;
		d.height -= 10;
		canvas.setSize(d);
		dialog.setSize(d2.width, d2.height+30);
		canvas2.setSize(d2);
		canvas.setLocation(2,2);
	}

	public Listener getListener(){
		return this.list;
	}	
}
