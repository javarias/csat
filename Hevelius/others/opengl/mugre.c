#include <GL/gl.h>
#include <GL/glu.h>
#include <GL/glut.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define WIDTH  480
#define HEIGHT 480

#define RED   0
#define GREEN 0
#define BLUE  0
#define ALPHA 1

GLvoid initGL();
void init_scene();
GLvoid window_display();
void render_scene();
void MoveToAz(double az);
void MoveToAlt(double alt);

GLuint cylinder;
GLuint telescope;
GLfloat mat_ambient[] = { 0.7f, 0.7f, 0.7f, 1.0f };
GLfloat mat_diffuse[] = { 0.1f, 0.5f, 0.8f, 1.0f };
GLfloat mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
GLfloat mat_shininess[] = { 100.0f };

#define DELTA 5
int x = 0;
int rotateX = 0;
int y = 0;
int rotateY = 0;
int z = 0;
int rotateZ = 0;
int speed = 0;
float angle =0;
int fla =0;
float angle2 = 0;
int subir = 1;
double az=90;

double alt=90;


void IncreaseAngleAz(double az){
	
	if (angle2< az){
		angle2 = angle2 + 0.5;
	}
//	printf("Angulo de entrada : %g \n",angle);	
	MoveToAz(angle2);
}

void IncreaseAngleAlt(double alt){
	
    if (angle2==az && angle < alt ){
                //printf("lali:%g\n",angle);
                //MoveToAlt(angle);
                angle = angle + 0.5;
        }
	MoveToAlt(angle);
}



void MoveToAlt(double alt)
{
		printf("lali %g\n",alt);
        glPushMatrix();
       	glRotatef(0,0,0,1);
        if(angle<=alt && angle2==az)
        glRotatef(alt,0,0,1);

    	glRotatef(angle2,0,1,0);

        glCallList(telescope);
		glPopMatrix();
	

}
void MoveToAz(double az)
{
	printf("lolo%g\n",az);
    glPushMatrix();
	glScalef(1.5,1.5,1.5);
	glRotatef(az,0,1,0);
	glRotatef(270,1,0,0);
	glTranslatef(0,0,-1);
	glCallList(cylinder);
	glPopMatrix();
	

}


GLvoid initGL()
{
	glClearColor(RED, GREEN, BLUE, ALPHA);
	glClearDepth(1.0);
	glDepthFunc(GL_LESS);
	glEnable(GL_DEPTH_TEST);
}

void init_scene()
{
	 GLfloat light_position[] = { 1.0, 1.0, 1.0, 1.0 };
	 
     glEnable(GL_LIGHTING);
     glEnable(GL_LIGHT0);
     glDepthFunc(GL_LESS);
     glEnable(GL_DEPTH_TEST);
     glPolygonMode(GL_FRONT, GL_FILL); 
    //Base Telescopio	
	GLUquadricObj *quadric;

	cylinder = glGenLists(1);

	glNewList(cylinder, GL_COMPILE);
	quadric = gluNewQuadric();
	gluQuadricDrawStyle(quadric, GLU_FILL);

   	
    glColor3f(1, 0, 0);
	gluCylinder(quadric, 0.8, 0.6, 0.3, 30, 30); 
    
	//Disco de base
	glColor3f(0, 1, 0);
	glTranslatef(0,0,0.3);
	gluDisk(quadric, 0, 0.6, 15, 15);
	glTranslatef(0,0,-0.3);
     
	//Soportes grandes del telescopio

    glColor3f(0, 0, 1);
	glRotatef(90,0,0,1);
	glTranslatef(0, 0.5 ,0.3);
	gluCylinder(quadric, 0.06, 0.06, 0.8 , 60, 60);
	glTranslatef(0, -0.5,-0.3);
	glRotatef(-90,0,0,1);
  
    glColor3f(0, 0, 1);
	glRotatef(90,0,0,1);
	glTranslatef(0, -0.5 ,0.3);
	gluCylinder(quadric, 0.06, 0.06, 0.8 , 60, 60);
	glTranslatef(0, 0.5, -0.3);
	glRotatef(-90,0,0,1);
    //Tapas pequenas de los pilares
 
   
    glColor3f(1, 1, 1);
	glRotatef(90,0,0,1);
	glTranslatef(0, 0.5 ,1.1);
	gluDisk(quadric, 0, 0.06, 15, 15);
	glTranslatef(0, -0.5,-1.1);
	glRotatef(-90,0,0,1);
  
    glColor3f(1, 1, 1);
    glRotatef(90,0,0,1);
	glTranslatef(0, -0.5 ,1.1);
	gluDisk(quadric, 0, 0.06, 15, 15);
	glTranslatef(0, 0.5,-1.1);
	glRotatef(-90,0,0,1);
   
	//Soportes pequeÃ±os del Telescopio

    glColor3f(1, 1, 0);
	glTranslatef(-0.45, 0, 1.06);
	glRotatef(90,0,1,0);
	gluCylinder(quadric, 0.05, 0.05, 0.17 , 30, 30);
	glRotatef(-90,0,1,0);
    glTranslatef(0.45, 0, -1.06);
     
    
	glColor3f(1, 1, 0);
	glTranslatef(0.45, 0, 1.06);
	glRotatef(-90,0,1,0);
	gluCylinder(quadric, 0.05, 0.05, 0.17 , 30, 30);
	glRotatef(90,0,1,0);
	glTranslatef(-0.45, 0, -1.06);


/*Tapas de cilindros pequeñsos*/

    glColor3f(1, 0, 0);
	glTranslatef(-0.3, 0, 1.06);
	glRotatef(90,0,1,0);
	gluDisk(quadric, 0, 0.05, 15, 15);
	glRotatef(-90,0,1,0);
    glTranslatef(0.3, 0, -1.06);
       
	glColor3f(1, 0, 0);
	glTranslatef(0.3, 0, 1.06); 
	glRotatef(-90,0,1,0);
	gluDisk(quadric, 0, 0.05, 15, 15);
	glRotatef(90,0,1,0);
	glTranslatef(-0.3, 0, -1.06);
	
	gluDeleteQuadric(quadric);
	glEndList();

	//Lente del Telescopio
	telescope= glGenLists(1);

	glNewList(telescope, GL_COMPILE);
	quadric = gluNewQuadric();
	gluQuadricDrawStyle(quadric, GLU_FILL);
	glEnable(GL_DEPTH_TEST); 
	glColor3f(1, 1, 0);
	glTranslatef(0, 0 ,-0.4);
	gluCylinder(quadric, 0.4, 0.6, 2, 30, 30); 
	glTranslatef(0, 0, 0.4);
 	gluDeleteQuadric(quadric);
	
	glEndList();
}

GLvoid window_display()
{
	// Propiedades del material


 
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();
	gluLookAt(0, 0, 5, 0, 0, 0, 0, 1, 0);
	glMaterialfv(GL_FRONT, GL_AMBIENT, mat_ambient);
    glMaterialfv(GL_FRONT, GL_DIFFUSE, mat_diffuse);
    glMaterialfv(GL_FRONT, GL_SPECULAR, mat_specular);
 glMaterialfv(GL_FRONT, GL_SHININESS, mat_shininess);
	render_scene();
	glutSwapBuffers();
}

GLvoid window_reshape(GLsizei width, GLsizei height)
{
	if (height == 0)
		height = 1;

	glViewport(0, 0, width, height);

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	//glOrtho(-2, 2, -2, 2, -2, 2);
	gluPerspective(45, (GLdouble)width/(GLdouble)height, 1, 10);
	glMatrixMode(GL_MODELVIEW);
}


void render_scene()
{
    IncreaseAngleAz(az);	
    IncreaseAngleAlt(alt);
}

GLvoid window_idle()
{
	glutPostRedisplay();
}

int main(int argc, char **argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);
	glutInitWindowSize(WIDTH, HEIGHT);
	glutInitWindowPosition(0, 0);
	glutCreateWindow("Rotating Cylinder");

	initGL();
	init_scene();

	glutDisplayFunc(&window_display);
	glutReshapeFunc(&window_reshape);
	glutIdleFunc(&window_idle);
	glutMainLoop();

	return 1;
}


