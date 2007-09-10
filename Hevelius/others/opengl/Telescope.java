//import java.util.*;
//import java.lang.Object.*;
//import javax.*;
import javax.media.opengl.*;
//import com.sun.opengl.util.*;
import javax.media.opengl.glu.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.sun.opengl.util.GLUT;
import javax.swing.JFrame;
import java.awt.BorderLayout;

public class Telescope implements GLEventListener {
	
	public static final int WIDTH = 480;
	public static final int HEIGHT = 480;
	
	public static final int RED = 0;
	public static final int GREEN = 0;
	public static final int BLUE = 0;
	public static final int ALPHA = 1;
    private int cylinder;
    private int telescope;
    private float mat_ambient[] = { 0.7f, 0.7f, 0.7f, 1.0f };
    private float mat_diffuse[] = { 0.1f, 0.5f, 0.8f, 1.0f };
    private float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    private float mat_shininess[] = { 100.0f };
	
    //#define DELTA 5
    private float angle =0;
    private float angle2 = 0;
    private float az=90;
    private float alt=90;
    //public static int gearDisplayList;
	//private GLCanvas canvas;
	//private Listener list;
    //GLAutoDrawable drawable;
	 //GL gl = drawable.getGL();
     GLU glu;
     GLUT glut= new GLUT();
	public Telescope() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Telescope");
		GLCanvas lienzo= new GLCanvas();
		lienzo.addGLEventListener(new Telescope());
		frame.getContentPane().add(lienzo, BorderLayout.CENTER);
		frame.setSize(WIDTH, HEIGHT);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowAdapter e){
				System.exit(0);
			}
		});
		
				
		//init();
		//init_scene();
		frame.setVisible(true);
	
		//glutInit(argc, argv);
		//glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);
		/*glutInitWindowSize(WIDTH, HEIGHT);
		glutInitWindowPosition(0, 0);
		glutCreateWindow("Rotating Cylinder");*/
		//initGL();
		//init_scene();

		//glutDisplayFunc(window_display);
		//glutReshapeFunc(window_reshape);
		/*glutIdleFunc(window_idle);
		glutMainLoop();*/
	}

	public void IncreaseAngleAz(float az, GLAutoDrawable drawable){
		
		GL gl =drawable.getGL();
		if (angle2 < az){
			angle2 = angle2 + 0.5f;
		}
//		printf("Angulo de entrada : %g \n",angle);	
		MoveToAz(angle2, drawable);
	}
	public void IncreaseAngleAlt(float alt, GLAutoDrawable drawable){
		GL gl = drawable.getGL();
	    if (angle2==az && angle < alt ){
	                //printf("lali:%g\n",angle);
	                //MoveToAlt(angle);
	                angle = angle + 0.5f;
	        }
		MoveToAlt(angle,drawable );
	}
	
	public void MoveToAlt(float alt, GLAutoDrawable drawable)
	{
		GL gl = drawable.getGL();
	
			System.out.println("lali "+alt);
	      	gl.glPushMatrix();
	       	gl.glRotatef(0,0,0,1);
	        if(angle<=alt && angle2==az)
	        gl.glRotatef(alt,0,0,1);
	    	gl.glRotatef(angle2,0,1,0);
	        gl.glCallList(telescope);
			gl.glPopMatrix();
		

	}
	public void MoveToAz(float az, GLAutoDrawable drawable)
	{
		GL gl = drawable.getGL();
	
		
		System.out.println("lolo "+az);
	    gl.glPushMatrix();
	    gl.glScalef(1.5f,1.5f,1.5f);
		gl.glRotatef( az,0f,1f,0f);
		gl.glRotatef(270,1,0,0);
		gl.glTranslatef(0,0,-1);
		gl.glCallList(cylinder);
		gl.glPopMatrix();
	      
	}
	public void init(GLAutoDrawable drawable)
	{
		//drawable.setGL(new DebugGL(drawable.getGL()));

		GL gl = drawable.getGL();
		gl.glClearColor(RED, GREEN, BLUE, ALPHA);
		gl.glClearDepth(1.0);
		gl.glDepthFunc(GL.GL_LESS);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}
	public void init_scene(GLAutoDrawable drawable)
	{
		GL gl = drawable.getGL();
	
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
		cylinder = gl.glGenLists(1);

		gl.glNewList(cylinder, GL.GL_COMPILE);
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
		telescope= gl.glGenLists(1);

		gl.glNewList(telescope, GL.GL_COMPILE);
		quadric = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		gl.glEnable(GL.GL_DEPTH_TEST); 
		gl.glColor3f(1, 1, 0);
		gl.glTranslatef(0, 0 ,-0.4f);
		glu.gluCylinder(quadric, 0.4, 0.6, 2, 30, 30); 
		gl.glTranslatef(0, 0, 0.4f);
		glu.gluDeleteQuadric(quadric);
		
		gl.glEndList();
	}
	public void display(GLAutoDrawable drawable)
	{
		GL gl = drawable.getGL();
		// Propiedades del material
       	 
	    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		glu.gluLookAt(0, 0, 5, 0, 0, 0, 0, 1, 0);
		//gl.glMaterialfv(arg0, arg1, arg2)
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient,0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse,0);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular,0);
		gl.glMaterialfv(GL.GL_FRONT,  GL.GL_SHININESS, mat_shininess,0);
		render_scene(drawable);
		//glut.glutSwapBuffers();
		
	}
	public void reshape(GLAutoDrawable drawable, int x,int y, int width, int height)
	{
		GL gl = drawable.getGL();
		if (height == 0)
			height = 1;

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		//glOrtho(-2, 2, -2, 2, -2, 2);
		glu.gluPerspective(45, (double)width/(double)height, 1, 10);
		gl.glMatrixMode(GL.GL_MODELVIEW);
	}
	public void displayChanged(GLAutoDrawable drawable, boolean x, boolean y)
	{
	}
	 public void render_scene(GLAutoDrawable drawable)
	{
	    IncreaseAngleAz(az, drawable);	
	    IncreaseAngleAlt(alt, drawable);
	}
	public void window_idle()
	 {
	 	//glutPostRedisplay();
	 }
}

