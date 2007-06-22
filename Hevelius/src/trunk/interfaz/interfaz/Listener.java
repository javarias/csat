import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;

public class Listener implements GLEventListener, MouseListener {
	private JDialog dialog;
	private Animator animator;
	public float angle;
	private int gearDisplayList;
	public void init(GLAutoDrawable drawable) {
		gearDisplayList = DrawingPanel.gearDisplayList;
		//System.out.println("Listener.init()");
		drawable.setGL(new DebugGL(drawable.getGL()));

		GL gl = drawable.getGL();

		float pos[] = { 5.0f, 5.0f, 10.0f, 0.0f };
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, pos, 0);
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		DrawingPanel.initializeDisplayList(gl);

		gl.glEnable(GL.GL_NORMALIZE);

		reshape(drawable, 0, 0, drawable.getWidth(), drawable.getHeight());
		drawable.addMouseListener(this);
	}

	public void display(GLAutoDrawable drawable) {
		angle += 0.01f;
		interfaz.pane.setCompassPoints((double)angle);
		GL gl = drawable.getGL();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
		gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
		gearDisplayList = DrawingPanel.gearDisplayList;
		gl.glCallList(gearDisplayList);
		gl.glPopMatrix();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println("Listener.reshape()");
		GL gl = drawable.getGL();

		float h = (float)height / (float)width;

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 60.0f);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -40.0f);
	}

	public void destroy(GLAutoDrawable drawable) {
		System.out.println("Listener.destroy()");
		GL gl = drawable.getGL();
		gearDisplayList = DrawingPanel.gearDisplayList;
		gl.glDeleteLists(gearDisplayList, 1);
		gearDisplayList = 0;
	}

	// Unused routines
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}

	public void mousePressed(MouseEvent e){}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) 
	{
		if(!dialog.isVisible())
		{
			dialog.setVisible(true);
			animator.start();
		}
	}
	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}

	public void setDialog(JDialog diag, final Animator a)
	{
		dialog = diag;
		animator = a;
	}
}
