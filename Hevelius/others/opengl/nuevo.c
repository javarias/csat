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


GLuint cylinder;
GLuint telescope;

#define DELTA 5
int x = 0;
int rotateX = 0;
int y = 0;
int rotateY = 0;
int z = 0;
int rotateZ = 0;
int speed = 0;
float angle =270;
float angle2 = 10;
int subir = 1;






GLvoid initGL()
{
	glClearColor(RED, GREEN, BLUE, ALPHA);
	glClearDepth(1.0);
	glDepthFunc(GL_LESS);
	glEnable(GL_DEPTH_TEST);
}

void init_scene()
{
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
	glTranslatef(-0.5, 0, 0);
	gluCylinder(quadric, 0.05, 0.05, 1.2 , 30, 30);
	glTranslatef(0.5, 0, 0);


	glColor3f(0, 0, 1);
        glTranslatef(0.5, 0, 0);
        gluCylinder(quadric, 0.05, 0.05, 1.2 , 30, 30);
	glTranslatef(-0.5, 0, 0);

	//Tapas pequenas de los pilares
	glColor3f(0, 0, 1);
        glTranslatef(0.5,0,1.2);
        gluDisk(quadric, 0, 0.05, 15, 15);
        glTranslatef(-0.5,0,-1.2);
	
	glColor3f(0, 0, 1);
        glTranslatef(-0.5,0,1.2);
        gluDisk(quadric, 0, 0.05, 15, 15);
        glTranslatef(0.5,0,-1.2);

	

	//Soportes pequeños del Telescopio
	glColor3f(0, 1, 0);
        glTranslatef(-0.4-0.05, 0, 1.143);
        glRotatef(90,0,1,0);
        gluCylinder(quadric, 0.05, 0.05, 0.140 , 30, 30);
	glRotatef(-90,0,1,0);
	glTranslatef(0.4+0.05, 0, -1.143);

	glColor3f(0, 1, 0);
        glTranslatef(0.4+0.05, 0, 1.145);
        glRotatef(-90,0,1,0);
        gluCylinder(quadric, 0.05, 0.05, 0.145 , 30, 30);
        glRotatef(90,0,1,0);
        glTranslatef(-0.4-0.05, 0, -1.143);


	
	gluDeleteQuadric(quadric);
	glEndList();

	//Lente del Telescopio
	telescope= glGenLists(1);

	glNewList(telescope, GL_COMPILE);
	quadric = gluNewQuadric();
        gluQuadricDrawStyle(quadric, GLU_FILL);

	 glColor3f(1, 1, 0);
        glTranslatef(0, 0.2, 0);
        gluCylinder(quadric, 0.4, 0.6, 2 , 30, 30);
	gluDeleteQuadric(quadric);
	glEndList();
}

GLvoid window_display()
{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();
	gluLookAt(0, 0, 5, 0, 0, 0, 0, 1, 0);
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
	gluPerspective(45, (GLdouble)width/(GLdouble)height, 1, 10);
	glMatrixMode(GL_MODELVIEW);
}


void render_scene()
{
	//GLUquadricObj *quadric;
	//quadric = gluNewQuadric();
	//gluQuadricDrawStyle(quadric, GLU_FILL);
	//glColor3f(0, 0, 1);
	//glTranslatef(-1, 0, 1);
	//gluDisk(quadric, 0.5, 0.75, 15, 15);
	glPushMatrix();
	//glRotatef(x, 1, 0, 0);
	//glRotatef(y, 0, 1, 0);
	//glRotatef(z, 0, 0, 1);
	glRotatef(270,1,0,0);
	//glRotatef(angle,0,0,0);
	glTranslatef(0, 0, -1.5);
	glScalef(1.5,1.5,1.5);
	glCallList(cylinder);
	glPopMatrix();
	
	//Telescopio
	glPushMatrix();
	//glRotatef(angle,1,0,0);
	glRotatef(angle,1,0,0);
	//glRotatef(angle2,1,0,0);
	glTranslatef(0,0,-0.5);
	glCallList(telescope);
	glPopMatrix();
}

GLvoid window_idle()
{
	/*angle += 0.18;
	if(angle2>75)
		subir = 1;
	if(angle2<10)
		subir = 0;
	if(subir==1)
		angle2 += 0.08;
	else
		angle2 -= 0.08;
	
	glutPostRedisplay();*/
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


