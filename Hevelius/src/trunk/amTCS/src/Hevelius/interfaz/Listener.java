package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;
import javax.media.opengl.glu.*;
import java.text.DecimalFormat;


public class Listener implements GLEventListener, MouseListener {
        public static final float RED = 0;
        public static final float GREEN = 0;
        public static final float BLUE = 0.05f;
	public static final float ALPHA = 0.05f;

	private float mat_ambient[] = { 0.44f, 0.44f, 0.44f, 1.0f };
	private float mat_ambient1[] = { 0.8f, 0.4f, 0f, 1.0f };
	private float mat_ambient2[] = { 0.22f, 0.22f, 0.22f, 1.0f };
	private float mat_ambient3[] = { 0f, 0f, 0f, 1f };
	private float mat_diffuse[] = { 0.1f, 0.5f, 0.8f, 1.0f };
	private float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float mat_shininess[] = { 100.0f };

	private float luzdifusa[]= {0.7f,1.0f,0.7f,1.0f};
	private float luzambiente[]={0.7f,0.5f,0.7f,1.0f};
	private float luzspecular[]={0.5f,0,0.5f,0};
	private float posicion[]={1f,1f,1f,0};

	private float angle = 0;
	private float angle2 =0;
	private float az=0;
	private float alt=0;
	private float vel=1f;
	private JDialog dialog;
	private Animator animator;
	private int BaseDisplayList;
	private int TelescopeDisplayList;

	public synchronized void initializeDisplayList(GLAutoDrawable drawable) {
		GL gl =drawable.getGL();
		BaseDisplayList = gl.glGenLists(1);
		TelescopeDisplayList = gl.glGenLists(1);
		init_scene(drawable);

		
	}

	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearColor(RED, GREEN, BLUE, ALPHA);
		gl.glClearDepth(1.0);
		gl.glDepthFunc(GL.GL_LESS);
		gl.glEnable(GL.GL_DEPTH_TEST);
		//gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		//gl.glLightModel(GL.GL_LIGHT_MODEL_TWO_SIDE,1);
		gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE, luzdifusa,0);
		gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT, luzambiente,0);
		gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR, luzspecular,0);
		gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION, posicion,0);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);

		initializeDisplayList(drawable);

		reshape(drawable, 0, 0, drawable.getWidth(), drawable.getHeight());
		drawable.addMouseListener(this);
	}

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		glu.gluLookAt(0, 0, 5, 0, 0, 0, 0, 1, 0);

		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient,0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse,0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular,0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, mat_shininess,0);
		prepare_scene();
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
	public void init_scene(GLAutoDrawable drawable)
        {
                GL gl =drawable.getGL();
		GLU glu = new GLU();
		//float light_position[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		GLUquadric quadric;
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glDepthFunc(GL.GL_LESS);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glPolygonMode(GL.GL_FRONT, GL.GL_FILL);
		
		//Base Telescopio       

		BaseDisplayList = gl.glGenLists(1);
		gl.glNewList(BaseDisplayList, GL.GL_COMPILE);
		quadric = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		
		gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient3,0);
		glu.gluCylinder(quadric, 0.8, 0.6, 0.3, 30, 30);
		gl.glPopMatrix();
		//Disco de base

		 gl.glPushMatrix();
                gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient2,0);
                //gl.glTranslatef(0,0,-0.3f);
		glu.gluCylinder(quadric, 0.81, 0.71, 0.11, 30, 30);
                gl.glPopMatrix();


		gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient,0);
		gl.glTranslatef(0.02f,0f,0.28f);
		glu.gluDisk(quadric, 0, 0.6, 15, 15);
		gl.glPopMatrix();

		//Soportes grandes del telescopio
		gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient2,0);
		gl.glRotatef(90,0,0,1);
		gl.glTranslatef(0, 0.5f ,0.3f);
		glu.gluCylinder(quadric, 0.06, 0.06, 0.8 , 60, 60);
		gl.glPopMatrix();
	
		gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient2,0);
		gl.glRotatef(90,0,0,1);
		gl.glTranslatef(0, -0.5f ,0.3f);
		glu.gluCylinder(quadric, 0.06, 0.06, 0.8 , 60, 60);
		gl.glPopMatrix();
		//Tapas pequenas de los pilares*/

		gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient2,0);
		gl.glRotatef(90,0,0,1);
		gl.glTranslatef(0, 0.5f,1.1f);
		glu.gluDisk(quadric, 0, 0.06, 15, 15);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient2,0);
		gl.glRotatef(90,0,0,1);
		gl.glTranslatef(0, -0.5f ,1.1f);
		glu.gluDisk(quadric, 0, 0.06, 15, 15);
		gl.glPopMatrix();
		//Soportes pequeños del Telescopio

		gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient2,0);
		gl.glTranslatef(-0.45f, 0, 1.06f);
		gl.glRotatef(90,0,1,0);
		glu.gluCylinder(quadric, 0.05, 0.05, 0.17 , 30, 30);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient2,0);
		gl.glTranslatef(0.45f, 0, 1.06f);
		gl.glRotatef(-90,0,1,0);
		glu.gluCylinder(quadric, 0.05, 0.05, 0.17 , 30, 30);
		gl.glPopMatrix();
		
		//Tapas de cilindros pequeñ

		gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient2,0);
		gl.glTranslatef(-0.3f, 0, 1.06f);
		gl.glRotatef(90,0,1,0);
		glu.gluDisk(quadric, 0, 0.05, 15, 15);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient2,0);
		gl.glTranslatef(0.3f, 0, 1.06f);
		gl.glRotatef(-90,0,1,0);
		glu.gluDisk(quadric, 0, 0.05, 15, 15);
		gl.glPopMatrix();

		glu.gluDeleteQuadric(quadric);
		gl.glEndList();

		//Lente del Telescopio
		TelescopeDisplayList = gl.glGenLists(1);

		gl.glNewList(TelescopeDisplayList, GL.GL_COMPILE);
		quadric = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient1,0);
		gl.glTranslatef(0, 0 ,-0.4f);
		glu.gluCylinder(quadric, 0.4, 0.5, 2, 30, 30);
		gl.glPopMatrix();
	
		//lente trasero

		gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient3,0);		
		gl.glRotatef(-90,1,0,0);
                gl.glTranslatef(0, -1.55f, -0.5f);
                glu.gluCylinder(quadric, 0.01, 0.01, 1 , 30, 30);
		gl.glPopMatrix();

		
                gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient3,0);
		gl.glRotatef(-90,0,1,0);
                gl.glTranslatef(1.6f, 0f, -0.5f);
                glu.gluCylinder(quadric, 0.01, 0.01, 1 , 30, 30);
		gl.glPopMatrix();
		
		//lente delantero
		
		gl.glPushMatrix();
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient3,0);
		gl.glDepthMask(false);
		gl.glRotatef(0,1,0,0);
                gl.glTranslatef(0,0, -0.4f);
                glu.gluDisk(quadric, 0, 0.4, 15, 15);
		gl.glDepthMask(true);
		gl.glPopMatrix();

		//adorno
		gl.glPushMatrix();
                gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient3,0);
                gl.glTranslatef(0, 0 ,-0.4f);
                glu.gluCylinder(quadric, 0.42, 0.42, 0.3, 30, 30);
                gl.glPopMatrix();

		gl.glPushMatrix();
                gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient3,0);
                gl.glTranslatef(0f, 0f ,1.45f);
                glu.gluCylinder(quadric, 0.5, 0.52, 0.3, 30, 30);
                gl.glPopMatrix();

		
		glu.gluDeleteQuadric(quadric);
		gl.glEndList();
	}

	public void prepare_scene()
	{
		if(Math.abs(alt -angle) >= 1f || Math.abs(az-angle2) >= 1f )
		{
			if(az > angle2) 
			{
				if(360-az+angle2 < az - angle2)
					DecreaseAngleAz();
				else
					IncreaseAngleAz();
			}
			else if (az < angle2)
			{
				if(360-angle2+az < angle2 - az)
					IncreaseAngleAz();
				else
					DecreaseAngleAz();
			}

			if(alt > angle)
			{
				if(360-alt+angle < alt - angle)
                                        DecreaseAngleAlt();
                                else
                                        IncreaseAngleAlt();
			}
			else if(alt < angle)
			{
				if(360-angle+alt < angle - alt)
					IncreaseAngleAlt();
				else
					DecreaseAngleAlt();
			}
			if(angle>=360)
				angle=angle-360;
			if(angle2>=360)
				angle2=angle2-360;
			if(angle<0)
				angle += 360;
			if(angle2<0)
				angle2 += 360;

		}
	}

	public void render_scene(GLAutoDrawable drawable)
	{
		MoveToAlt(drawable);
		MoveToAz(drawable);
	}

	public void IncreaseAngleAz(){
			angle2 = angle2 + vel;
	}
	public void IncreaseAngleAlt(){
			angle = angle + vel;
	}

	public void DecreaseAngleAz(){
			angle2 = angle2 - vel;
	}
	public void DecreaseAngleAlt(){
			angle = angle - vel;
	}


	public void MoveToAlt(GLAutoDrawable drawable)
	{
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glRotatef(0,0,0,1);
		gl.glRotatef(angle2,0,1,0);
		gl.glRotatef(-angle+90,-1,0,0);
		gl.glCallList(TelescopeDisplayList);
		gl.glPopMatrix();


	}
	public void MoveToAz(GLAutoDrawable drawable)
	{
		GL gl = drawable.getGL();

		gl.glPushMatrix();
		gl.glScalef(1.5f,1.5f,1.5f);
		gl.glRotatef(angle2,0f,1f,0f);
		gl.glRotatef(270,1,0,0);
		gl.glTranslatef(0,0,-1);
		gl.glCallList(BaseDisplayList);
		gl.glPopMatrix();
	}

	public void setAltAzDest(float Alt, float Az)
	{

		az= 180 - Az;
		alt = 90 - Alt;
		if(alt < 0)
			alt += 360;
		if(az < 0)
			az += 360;
		DecimalFormat df = new DecimalFormat("#.#");
		az = Float.parseFloat(df.format(az));
		alt = Float.parseFloat(df.format(alt));
	}
}
