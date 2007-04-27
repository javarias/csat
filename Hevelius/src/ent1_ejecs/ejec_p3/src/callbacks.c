/*
** callbacks.c
*/

#ifdef HAVE_CONFIG_H
#  include <config.h>
#endif

#include <gtk/gtk.h>

#include "callbacks.h"
#include "interface.h"
#include "support.h"


/*
** delete_event
**
** The application's "delete_event" handler.
*/

gboolean delete_event(GtkWidget *widget, GdkEvent *event, gpointer data)
{
	return FALSE;
}

/*
** destroy
**
** The application's "destroy" handler.
*/

void destroy(GtkWidget *widget, gpointer data)
{
	gtk_main_quit();
}

