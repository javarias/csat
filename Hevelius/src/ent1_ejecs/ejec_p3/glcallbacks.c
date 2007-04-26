/*
** glcallbacks.c
**
** Callback functions for GtkGLExt.
** (c) 2004 Steve Joiner (steve@daisyhill.net)
**
** This program is free software; you can redistribute it and/or modify
** it under the terms of the GNU General Public License as published by
** the Free Software Foundation; either version 2 of the License, or
** (at your option) any later version.
**
** This program is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
** GNU Library General Public License for more details.
**
** You should have received a copy of the GNU General Public License
** along with this program; if not, write to the Free Software
** Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/

#ifdef HAVE_CONFIG_H
#  include <config.h>
#endif

/* system headers */
#include <gtk/gtkgl.h>
#include <GL/gl.h>
#include <GL/glu.h>

/* glade headers */
#include "callbacks.h"
#include "interface.h"
#include "support.h"

/* application headers */
#include "coord.h"
#include "glcallbacks.h"
#include "model.h"

static void gl_realize(GtkWidget *widget, gpointer data);
static gboolean gl_configure_event(GtkWidget *widget,
								   GdkEventConfigure *event,
								   gpointer data);
static gboolean gl_expose_event(GtkWidget *widget,
								GdkEventExpose *event,
								gpointer data);

/*
** gl_realize
*/

static void gl_realize(GtkWidget *widget, gpointer data)
{
	AppData *app_data = (AppData*) data;
	GdkGLDrawable *gldrawable = gtk_widget_get_gl_drawable(widget);

	static GLfloat model_ambient[] = {0.5, 0.5, 0.5, 1.0};

	static GLfloat light0_position[] = {0.0, -20.0, 30.0, 0.0};
	static GLfloat light0_diffuse[] = {1.0, 1.0, 1.0, 1.0};
	static GLfloat light0_specular[] = {1.0, 1.0, 1.0, 1.0};

	static GLfloat material_diffuse[] = {1.0, 1.0, 1.0, 1.0};
	static GLfloat material_specular[] = {0.5, 0.5, 0.5, 0.5};
	static GLfloat material_shininess[] = {50.0};

	glClearColor(0.0/256.0, 9.0/256.0, 29.0/256.0, 1.0);
	
	glClearDepth(1.0);

	glLightModelfv(GL_LIGHT_MODEL_AMBIENT, model_ambient);
	
	glLightfv(GL_LIGHT0, GL_POSITION, light0_position);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, light0_diffuse);
	glLightfv(GL_LIGHT0, GL_SPECULAR, light0_specular);

	glEnable(GL_LIGHTING);
	glEnable(GL_LIGHT0);
	glEnable(GL_DEPTH_TEST);

	glEnable(GL_CULL_FACE);
	glShadeModel(GL_SMOOTH);
	
	glMaterialfv(GL_FRONT, GL_DIFFUSE, material_diffuse);
	glMaterialfv(GL_FRONT, GL_SPECULAR, material_specular);
	glMaterialfv(GL_FRONT, GL_SHININESS, material_shininess);
	glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
	glEnable(GL_COLOR_MATERIAL);

	app_data->model = model_init();

	gdk_gl_drawable_gl_end(gldrawable);

	app_data->gl_initialized = 1;
}

/*
** gl_configure_event
*/

static gboolean gl_configure_event(GtkWidget *widget,
								   GdkEventConfigure *event,
								   gpointer data)
{
	AppData *app_data = (AppData*) data;
	GdkGLContext *glcontext = gtk_widget_get_gl_context(widget);
	GdkGLDrawable *gldrawable = gtk_widget_get_gl_drawable(widget);
	gdouble x, y, z;
	
	app_data->width = widget->allocation.width;
	app_data->height = widget->allocation.height;
	app_data->aspect = ((gdouble) app_data->width) / ((gdouble) app_data->height);
	
	if (app_data->height == 0.0) app_data->height = 1.0; /* prevent divide by zero */
	
	if (!gdk_gl_drawable_gl_begin(gldrawable, glcontext))
	{
		return FALSE;
	}

	glViewport(0, 0, app_data->width, app_data->height);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	coord_camera_altaz_to_xyz(&x, &y, &z,
							  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->caz_scale))),
							  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->calt_scale))));
	gluPerspective(45.0, app_data->aspect, 0.1, 100.0);
	gluLookAt(x, y, z, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	glMatrixMode(GL_MODELVIEW);

	gdk_gl_drawable_gl_end(gldrawable);

	return TRUE;
}

/*
** gl_expose_event
*/

static gboolean gl_expose_event(GtkWidget *widget,
								GdkEventExpose *event,
								gpointer data)
{
	AppData *app_data = (AppData*) data;
	
	GdkGLContext *glcontext = gtk_widget_get_gl_context(widget);
	GdkGLDrawable *gldrawable = gtk_widget_get_gl_drawable(widget);

	if (!gdk_gl_drawable_gl_begin(gldrawable, glcontext))
	{
		return FALSE;
	}

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();

	model_draw(app_data->model,
			   gtk_range_get_value(GTK_RANGE(app_data->a1_scale)),
			   gtk_range_get_value(GTK_RANGE(app_data->a2_scale)),
			   gtk_range_get_value(GTK_RANGE(app_data->a3_scale)));

	if (gdk_gl_drawable_is_double_buffered(gldrawable))
	{
		gdk_gl_drawable_swap_buffers(gldrawable);
	}
	else
	{
		glFlush();
	}

	gdk_gl_drawable_gl_end(gldrawable);

	return TRUE;
}

/*
** gl_initialize_gtkglext
*/

void gl_init_gtkglext(AppData *app_data)
{
	gint major, minor;
	GdkGLConfig *glconfig;
	GtkWidget *mainhbox;
	
	gdk_gl_query_version(&major, &minor);
	/* g_print("\bOpenGL extension version - %d.%d\n", major, minor); */
	glconfig = gdk_gl_config_new_by_mode(GDK_GL_MODE_RGB |
										 GDK_GL_MODE_DEPTH |
										 GDK_GL_MODE_DOUBLE);
	if (glconfig == NULL)
	{
		g_print("*** Cannot find the double-buffered visual.\n");
		g_print("*** Trying single-buffered visual.\n");

		glconfig = gdk_gl_config_new_by_mode(GDK_GL_MODE_RGB |
											 GDK_GL_MODE_DEPTH);

		if (glconfig == NULL)
		{
			g_print("*** No appripriate OpenGL capable visual found.\n");
			exit(1);
		}
	}

	/*
	** Setup GL area and callbacks.
	*/

	app_data->gl_drawing_area = gtk_drawing_area_new();
	gtk_widget_set_size_request(app_data->gl_drawing_area, 640, 480);
	gtk_widget_set_gl_capability(app_data->gl_drawing_area, glconfig, NULL, TRUE, GDK_GL_RGBA_TYPE);
	gtk_widget_add_events(app_data->gl_drawing_area, GDK_VISIBILITY_NOTIFY_MASK);
	g_signal_connect_after(G_OBJECT(app_data->gl_drawing_area), "realize",
						   G_CALLBACK(gl_realize), app_data);
	g_signal_connect(G_OBJECT(app_data->gl_drawing_area), "configure_event",
					 G_CALLBACK(gl_configure_event), app_data);
	g_signal_connect(G_OBJECT(app_data->gl_drawing_area), "expose_event",
					 G_CALLBACK(gl_expose_event), app_data);

	mainhbox = lookup_widget(GTK_WIDGET(app_data->main_window), "mainhbox");
	
	gtk_box_pack_end(GTK_BOX(mainhbox), app_data->gl_drawing_area, TRUE, TRUE, 0);

	gtk_widget_show(app_data->gl_drawing_area);
}
