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

		JDialog dialog = new JDialog(interfaz.getMainFrame(),"Telescopio");
		dialog.getContentPane().setLayout(null);
		GLCanvas canvas2 = new GLCanvas(null,null,canvas.getContext(),null);
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
		canvas.setSize(getSize());
	}	

	public static synchronized void initializeDisplayList(GL gl) {
		gearDisplayList = gl.glGenLists(1);
		gl.glNewList(gearDisplayList, GL.GL_COMPILE);
		float red[] = { 0.8f, 0.1f, 0.0f, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, red, 0);
		gear(gl, 1.0f, 4.0f, 1.0f, 20, 0.7f);
		gl.glEndList();
	}

	private static void gear(GL gl,
			float inner_radius,
			float outer_radius,
			float width,
			int teeth,
			float tooth_depth)
	{
		int i;
		float r0, r1, r2;
		float angle, da;
		float u, v, len;

		float red[] = { 0.8f, 0.1f, 0.0f, 1.0f };
		float blue[] = { 0.5f, 0.8f, 0.4f, 1.0f };

		r0 = inner_radius;
		r1 = outer_radius - tooth_depth / 2.0f;
		r2 = outer_radius + tooth_depth / 2.0f;

		da = 2.0f * (float) Math.PI / teeth / 4.0f;

		gl.glShadeModel(GL.GL_FLAT);

		gl.glNormal3f(0.0f, 0.0f, 1.0f);

		/* draw front face */
		gl.glBegin(GL.GL_QUAD_STRIP);
		for (i = 0; i <= teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
			if(i < teeth)
			{
				gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
				gl.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
			}
		}
		gl.glEnd();

		/* draw front sides of teeth */
		gl.glBegin(GL.GL_QUADS);
		for (i = 0; i < teeth; i++)
		{
			if(i==0)
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, blue, 0);
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2.0f * da), r2 * (float)Math.sin(angle + 2.0f * da), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3.0f * da), r1 * (float)Math.sin(angle + 3.0f * da), width * 0.5f);
			if(i==0)
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, red, 0);
		}
		gl.glEnd();

		/* draw back face */
		gl.glBegin(GL.GL_QUAD_STRIP);
		for (i = 0; i <= teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), -width * 0.5f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
		}
		gl.glEnd();

		/* draw back sides of teeth */
		gl.glBegin(GL.GL_QUADS);
		for (i = 0; i < teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), -width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2 * da), r2 * (float)Math.sin(angle + 2 * da), -width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), -width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
		}
		gl.glEnd();

		/* draw outward faces of teeth */
		gl.glBegin(GL.GL_QUAD_STRIP);
		for (i = 0; i < teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle), r1 * (float)Math.sin(angle), -width * 0.5f);
			u = r2 * (float)Math.cos(angle + da) - r1 * (float)Math.cos(angle);
			v = r2 * (float)Math.sin(angle + da) - r1 * (float)Math.sin(angle);
			len = (float)Math.sqrt(u * u + v * v);
			u /= len;
			v /= len;
			gl.glNormal3f(v, -u, 0.0f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + da), r2 * (float)Math.sin(angle + da), -width * 0.5f);
			gl.glNormal3f((float)Math.cos(angle), (float)Math.sin(angle), 0.0f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2 * da), r2 * (float)Math.sin(angle + 2 * da), width * 0.5f);
			gl.glVertex3f(r2 * (float)Math.cos(angle + 2 * da), r2 * (float)Math.sin(angle + 2 * da), -width * 0.5f);
			u = r1 * (float)Math.cos(angle + 3 * da) - r2 * (float)Math.cos(angle + 2 * da);
			v = r1 * (float)Math.sin(angle + 3 * da) - r2 * (float)Math.sin(angle + 2 * da);
			gl.glNormal3f(v, -u, 0.0f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), width * 0.5f);
			gl.glVertex3f(r1 * (float)Math.cos(angle + 3 * da), r1 * (float)Math.sin(angle + 3 * da), -width * 0.5f);
			gl.glNormal3f((float)Math.cos(angle), (float)Math.sin(angle), 0.0f);
		}
		gl.glVertex3f(r1 * (float)Math.cos(0), r1 * (float)Math.sin(0), width * 0.5f);
		gl.glVertex3f(r1 * (float)Math.cos(0), r1 * (float)Math.sin(0), -width * 0.5f);
		gl.glEnd();

		gl.glShadeModel(GL.GL_SMOOTH);

		/* draw inside radius cylinder */
		gl.glBegin(GL.GL_QUAD_STRIP);
		for (i = 0; i <= teeth; i++)
		{
			angle = i * 2.0f * (float) Math.PI / teeth;
			gl.glNormal3f(-(float)Math.cos(angle), -(float)Math.sin(angle), 0.0f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), -width * 0.5f);
			gl.glVertex3f(r0 * (float)Math.cos(angle), r0 * (float)Math.sin(angle), width * 0.5f);
		}
		gl.glEnd();
	}
}
