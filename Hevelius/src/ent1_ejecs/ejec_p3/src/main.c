/*
** main.c
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

