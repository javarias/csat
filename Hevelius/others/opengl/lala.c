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

#define KEY_ESC 27
#define KEY_UP 101
#define KEY_DOWN 103
#define KEY_X 120
#define KEY_Y 121
#define KEY_Z 122

GLuint cylinder;

#define DELTA 5
int x = 0;
int rotateX = 0;
int y = 0;
int rotateY = 0;
int z = 0;
int rotateZ = 0;
int speed = 0;

void init_scene();
void render_scene();
GLvoid initGL();
GLvoid window_display();
GLvoid window_reshape(GLsizei width, GLsizei height);
GLvoid window_idle();
GLvoid window_key(unsigned char key, int x, int y); 
GLvoid window_special_key(int key, int x, int y);
float angle = 90;

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
  glutKeyboardFunc(&window_key);
  glutSpecialFunc(&window_special_key);

  glutMainLoop();  

  return 1;
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
  GLUquadricObj *quadric;

  cylinder = glGenLists(1);

  glNewList(cylinder, GL_COMPILE);
    quadric = gluNewQuadric();
    gluQuadricDrawStyle(quadric, GLU_FILL);

    glColor3f(1, 0, 0);
    gluCylinder(quadric, 1, 0.75, 1, 15, 15); 

    glColor3f(0, 1, 0);
    gluDisk(quadric, 0.5, 1, 15, 15); 

    glColor3f(0, 0, 1);
    glTranslatef(0, 0, 1);
    gluDisk(quadric, 0.5, 0.75, 15, 15); 

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

GLvoid window_key(unsigned char key, int x, int y) 
{
  switch (key) {    
  case KEY_ESC:  
    exit(1);                   	
    break; 
  case KEY_X:
    rotateX = !rotateX;
    glutPostRedisplay();
    break;
  case KEY_Y:
    rotateY = !rotateY;
    glutPostRedisplay();
    break;
  case KEY_Z:
    rotateZ = !rotateZ;
    glutPostRedisplay();
    break;
  default:
    printf ("Pressing %d doesn't do anything.\n", key);
    break;
  }	
}

GLvoid window_special_key(int key, int x, int y) 
{   
  switch (key) {    
  case KEY_UP: 
    speed = (speed + DELTA + 360) % 360;
    glutPostRedisplay();
    break;
    
  case KEY_DOWN: 
    speed = (speed - DELTA + 360) % 360;
    glutPostRedisplay();
    break;
    
  default:
    printf ("Pressing %d doesn't do anything.\n", key);
    break;
  }
}

GLvoid window_idle()
{
  if (rotateX) x = (x + speed + 360) % 360;
  if (rotateY) y = (y + speed + 360) % 360;
  if (rotateZ) z = (z + speed + 360) % 360;
  if (speed > 0 && (rotateX || rotateY || rotateZ)) 
    glutPostRedisplay();
  //angle += 1;
  glutPostRedisplay();
}

void render_scene()
{
  glPushMatrix();
  //glRotatef(x, 1, 0, 0);
  //glRotatef(y, 0, 1, 0);
  //glRotatef(z, 0, 0, 1);
  glRotatef(angle,1,0,0);
  glCallList(cylinder);
}



