/*
** main.c
**
** Main program for three axis telescope.
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
#include <gtk/gtk.h>
#include <gdk/gdkkeysyms.h>
#include <gtk/gtkgl.h>
#include <GL/gl.h>
#include <GL/glu.h>

/* glade headers */
#include "callbacks.h"
#include "interface.h"
#include "support.h"

/* application headers */
#include "app.h"
#include "controls.h"
#include "glcallbacks.h"

int main (int argc, char *argv[])
{
	AppData *app_data;
	GtkWidget *menu_about;
	
	gtk_set_locale();
	gtk_init(&argc, &argv);

	add_pixmap_directory(PACKAGE_DATA_DIR "/" PACKAGE "/pixmaps");

	gtk_gl_init(&argc, &argv);

	app_data = g_malloc(sizeof(AppData));
	app_data->gl_initialized = FALSE;
	app_data->servicing_value_changed = FALSE;
	app_data->alt_limit = 1.0;
	
	app_data->main_window = create_main_window();
	gtk_container_set_reallocate_redraws(GTK_CONTAINER(app_data->main_window), TRUE);

	g_signal_connect(G_OBJECT(app_data->main_window), "delete_event",
					 G_CALLBACK(delete_event), NULL);
	g_signal_connect(G_OBJECT(app_data->main_window), "destroy",
					 G_CALLBACK(destroy), NULL);

	ctrl_init_scales(app_data);

	gl_init_gtkglext(app_data);
	
	gtk_widget_show(app_data->main_window);
	g_timeout_add_full(G_PRIORITY_DEFAULT_IDLE,100,(GSourceFunc)poll_file,app_data,NULL);
	gtk_main();

	return 0;
}

