package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;
import javax.media.opengl.glu.*;

public class Listener implements GLEventListener, MouseListener {
        public static final int RED = 0;
        public static final int GREEN = 0;
        public static final int BLUE = 0;
	public static final int ALPHA = 1;

	private float mat_ambient[] = { 0.7f, 0.7f, 0.7f, 1.0f };
	private float mat_diffuse[] = { 0.1f, 0.5f, 0.8f, 1.0f };
	private float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float mat_shininess[] = { 100.0f };

	//#define DELTA 5
	private float angle = 0;
	private float angle2 = 0;
	private float az=0;
	private float alt=0;
	private JDialog dialog;
	private Animator animator;
	private int BaseDisplayList;
	private int TelescopeDisplayList;

	public synchronized void initializeDisplayList(GL gl) {
		BaseDisplayList = gl.glGenLists(1);
		TelescopeDisplayList = gl.glGenLists(1);
		init_scene(gl);
	}

	public void init(GLAutoDrawable drawable) {
		//System.out.println("Listener.init()");
		drawable.setGL(new DebugGL(drawable.getGL()));

		GL gl = drawable.getGL();

		gl.glClearColor(RED, GREEN, BLUE, ALPHA);
		gl.glClearDepth(1.0);
		gl.glDepthFunc(GL.GL_LESS);
		gl.glEnable(GL.GL_DEPTH_TEST);

		initializeDisplayList(gl);

		reshape(drawable, 0, 0, drawable.getWidth(), drawable.getHeight());
		drawable.addMouseListener(this);
	}

	public void display(GLAutoDrawable drawable) {
		//interfaz.getDrawingPanel().getCompassPanel().setCompassPoints((double)angle);
		GL gl = drawable.getGL();
		GLU glu = new GLU();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		glu.gluLookAt(0, 0, 5, 0, 0, 0, 0, 1, 0);

		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient,0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse,0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular,0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, mat_shininess,0);
		prepare_scene(drawable);	
		render_scene(drawable);
		gl.glFlush();

	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		if (height == 0)
			height = 1;

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		//gl.glOrtho(-2, 2, -2, 2, -2, 2);
		glu.gluPerspective(45, (double)width/(double)height, 1, 10);
		gl.glMatrixMode(GL.GL_MODELVIEW);
	}

	public void destroy(GLAutoDrawable drawable) {
		System.out.println("Listener.destroy()");
		GL gl = drawable.getGL();
		gl.glDeleteLists(BaseDisplayList, 1);
		gl.glDeleteLists(TelescopeDisplayList, 1);
		BaseDisplayList = 0;
		TelescopeDisplayList = 0;
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

	public void init_scene(GL gl)
	{
		GLU glu = new GLU();
		//float light_position[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		GLUquadric quadric;
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glDepthFunc(GL.GL_LESS);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glPolygonMode(GL.GL_FRONT, GL.GL_FILL);
		//Base Telescopio       

		//GLUquadricObj *quadric;
		//GLUquadricObj ;
		BaseDisplayList = gl.glGenLists(1);

		gl.glNewList(BaseDisplayList, GL.GL_COMPILE);
		quadric = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		gl.glColor3f(1, 0, 0);
		glu.gluCylinder(quadric, 0.8, 0.6, 0.3, 30, 30);

		//Disco de base
		gl.glColor3f(0, 1, 0);
		gl.glTranslatef(0f,0f,0.3f);
		glu.gluDisk(quadric, 0, 0.6, 15, 15);
		gl.glTranslatef(0,0,-0.3f);

		//Soportes grandes del telescopio

		gl.glColor3f(0, 0, 1f);
		gl.glRotatef(90,0,0,1);
		gl.glTranslatef(0, 0.5f ,0.3f);
		glu.gluCylinder(quadric, 0.06, 0.06, 0.8 , 60, 60);
		gl.glTranslatef(0, -0.5f,-0.3f);
		gl.glRotatef(-90,0,0,1);

		gl.glColor3f(0, 0, 1);
		gl.glRotatef(90,0,0,1);
		gl.glTranslatef(0, -0.5f ,0.3f);
		glu.gluCylinder(quadric, 0.06, 0.06, 0.8 , 60, 60);
		gl.glTranslatef(0, 0.5f, -0.3f);
		gl.glRotatef(-90,0,0,1);
		//Tapas pequenas de los pilares


		gl.glColor3f(1, 1, 1);
		gl.glRotatef(90,0,0,1);
		gl.glTranslatef(0, 0.5f,1.1f);
		glu.gluDisk(quadric, 0, 0.06, 15, 15);
		gl.glTranslatef(0, -0.5f,-1.1f);
		gl.glRotatef(-90,0,0,1);

		gl.glColor3f(1, 1, 1);
		gl.glRotatef(90,0,0,1);
		gl.glTranslatef(0, -0.5f ,1.1f);
		glu.gluDisk(quadric, 0, 0.06, 15, 15);
		gl.glTranslatef(0, 0.5f,-1.1f);
		gl.glRotatef(-90,0,0,1);

		//Soportes pequeños del Telescopio

		gl.glColor3f(1, 1, 0);
		gl.glTranslatef(-0.45f, 0, 1.06f);
		gl.glRotatef(90,0,1,0);
		glu.gluCylinder(quadric, 0.05, 0.05, 0.17 , 30, 30);
		gl.glRotatef(-90,0,1,0);
		gl.glTranslatef(0.45f, 0, -1.06f);


		gl.glColor3f(1, 1, 0);
		gl.glTranslatef(0.45f, 0, 1.06f);
		gl.glRotatef(-90,0,1,0);
		glu.gluCylinder(quadric, 0.05, 0.05, 0.17 , 30, 30);
		gl.glRotatef(90,0,1,0);
		gl.glTranslatef(-0.45f, 0, -1.06f);

		/*Tapas de cilindros pequeñ*/

		gl.glColor3f(1, 0, 0);
		gl.glTranslatef(-0.3f, 0, 1.06f);
		gl.glRotatef(90,0,1,0);
		glu.gluDisk(quadric, 0, 0.05, 15, 15);
		gl.glRotatef(-90,0,1,0);
		gl.glTranslatef(0.3f, 0, -1.06f);

		gl.glColor3f(1, 0, 0);
		gl.glTranslatef(0.3f, 0, 1.06f);
		gl.glRotatef(-90,0,1,0);
		glu.gluDisk(quadric, 0, 0.05, 15, 15);
		gl.glRotatef(90,0,1,0);
		gl.glTranslatef(-0.3f, 0, -1.06f);

		glu.gluDeleteQuadric(quadric);
		gl.glEndList();

		//Lente del Telescopio
		TelescopeDisplayList = gl.glGenLists(1);

		gl.glNewList(TelescopeDisplayList, GL.GL_COMPILE);
		quadric = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glColor3f(1, 1, 0);
		 gl.glRotatef(-90,1,0,0);
		gl.glTranslatef(0, 0 ,-0.4f);
		//gl.glRotatef(-90,1,0,0);
		glu.gluCylinder(quadric, 0.4, 0.6, 2, 30, 30);
		//gl.glRotatef(90,1,0,0);
		gl.glTranslatef(0, 0, 0.4f);
		gl.glRotatef(90,1,0,0);
		glu.gluDeleteQuadric(quadric);
		
		
	/*	//lente delantero
		gl.glColor4f(1, 1, 1,0);
		//gl.
		gl.glDepthMask(false);
		gl.glRotatef(0,1,0,0);
                gl.glTranslatef(0, 0, 1.36f);
                //gl.glRotatef(-90,0,1,0);
                glu.gluDisk(quadric, 0, 0.6, 15, 15);
                //gl.glRotatef(90,0,1,0);
                gl.glTranslatef(0, 0, -1.36f);
		gl.glRotatef(0,1,0,0);
		gl.glDepthMask(true);*/
		
		gl.glEndList();
	}

	public void prepare_scene(GLAutoDrawable drawable)
	{
		if(Math.abs(alt -angle) >= 0.1f || Math.abs(az-angle2) >= 0.1f )
		{
			if(az >= angle2)
			{
				//System.out.println("az :"+az);
				IncreaseAngleAz(drawable);
			}
			else //(az <= angle)
				DecreaseAngleAz(drawable);
			if(alt >= angle)
				IncreaseAngleAlt(drawable);
			else //if(alt<= angle)
				DecreaseAngleAlt(drawable);
		}
	}

	public void render_scene(GLAutoDrawable drawable)
	{
		MoveToAlt(drawable);
		MoveToAz(drawable);
		
	}

	public void IncreaseAngleAz(GLAutoDrawable drawable){

		//System.out.println("angle2 :"+angle2+ "   az :"+this.az);
		GL gl =drawable.getGL();
		if (angle2 < this.az){
			angle2 = angle2 + 2f;
			//System.out.println("increaseaz "+angle2);
		}
		//    System.out.println("increaseaz "+angle2);      
	}
	public void IncreaseAngleAlt(GLAutoDrawable drawable){
		GL gl = drawable.getGL();
		if ( angle < alt ){
			//System.out.println("ENTRO increase alt");
			//MoveToAlt(angle);
			angle = angle + 2f;
		}
	}

	public void DecreaseAngleAz(GLAutoDrawable drawable){

		//System.out.println("decreaseaz"+ angle2);
		GL gl =drawable.getGL();
		if (angle2 > az){
			angle2 = angle2 - 2f;
		}
		//              printf("Angulo de entrada : %g \n",angle);      
		MoveToAz(drawable);
	}
	public void DecreaseAngleAlt(GLAutoDrawable drawable){

		//System.out.println("decreasealt "+angle);
		GL gl = drawable.getGL();
		if ( angle > alt ){
			//System.out.println("ENTRO");
			//MoveToAlt(angle);
			angle = angle - 2f;
		}
		MoveToAlt(drawable );
	}


	public void MoveToAlt(GLAutoDrawable drawable)
	{
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glRotatef(0,0,0,1);
		gl.glRotatef(-angle2,0,1,0);
		//if(angle2==az)
		gl.glRotatef(angle,-1,0,0);
		//System.out.println(angle);
		//gl.glRotatef(angle2,0,1,0);
		gl.glCallList(TelescopeDisplayList);
		gl.glPopMatrix();


	}
	public void MoveToAz(GLAutoDrawable drawable)
	{
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glScalef(1.5f,1.5f,1.5f);
		gl.glRotatef(-angle2,0f,1f,0f);
		gl.glRotatef(270,1,0,0);
		gl.glTranslatef(0,0,-1);
		gl.glCallList(BaseDisplayList);
		gl.glPopMatrix();
	}

	public void setAltAzDest(float Alt, float Az)
	{
		//System.out.println(Alt + " -- " + Az );

		this.az= Az;
		this.alt = 90 - Alt;
	}
}
