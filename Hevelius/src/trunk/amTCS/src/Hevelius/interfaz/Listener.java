package Hevelius.interfaz;

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

        public synchronized void initializeDisplayList(GL gl) {
                gearDisplayList = gl.glGenLists(1);
                gl.glNewList(gearDisplayList, GL.GL_COMPILE);
                float red[] = { 0.8f, 0.1f, 0.0f, 1.0f };
                gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, red, 0);
                gear(gl, 1.0f, 4.0f, 1.0f, 20, 0.7f);
                gl.glEndList();
        }

	public void init(GLAutoDrawable drawable) {
		gearDisplayList = gearDisplayList;
		//System.out.println("Listener.init()");
		drawable.setGL(new DebugGL(drawable.getGL()));

		GL gl = drawable.getGL();

		float pos[] = { 5.0f, 5.0f, 10.0f, 0.0f };
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, pos, 0);
		gl.glEnable(GL.GL_CULL_FACE);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		initializeDisplayList(gl);

		gl.glEnable(GL.GL_NORMALIZE);

		reshape(drawable, 0, 0, drawable.getWidth(), drawable.getHeight());
		drawable.addMouseListener(this);
	}

	public void display(GLAutoDrawable drawable) {
		angle += 0.01f;
		//interfaz.getDrawingPanel().getCompassPanel().setCompassPoints((double)angle);
		GL gl = drawable.getGL();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
		gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
		gearDisplayList = gearDisplayList;
		gl.glCallList(gearDisplayList);
		gl.glPopMatrix();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		//System.out.println("Listener.reshape()");
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

                // draw front face
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

		// draw front sides of teeth
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

                // draw back face
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

                // draw back sides of teeth
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

		// draw outward faces of teeth
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

                // draw inside radius cylinder
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
