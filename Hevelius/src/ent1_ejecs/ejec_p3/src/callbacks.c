/*
** callbacks.c
**
** Callback functions for some GTK widgets.
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

/*
** on_menu_quit_activate
*/

void on_menu_quit_activate(GtkMenuItem*menuitem, gpointer user_data)
{
	gtk_main_quit();
}

/*
** on_menu_about_activate
*/

void on_menu_about_activate(GtkMenuItem *menuitem, gpointer user_data)
{
	GtkWidget *about_dialog;
	GtkLabel *about_label;
	char *window_title;
	char *about_text;

	about_dialog = create_about_dialog();

	window_title = g_strdup_printf("About %s", PACKAGE);
	gtk_window_set_title((GtkWindow *) about_dialog, window_title);
	about_label = (GtkLabel*) lookup_widget(about_dialog, "about_label");
	gtk_label_set_justify(about_label, GTK_JUSTIFY_CENTER);
	about_text = g_strdup_printf("<span size=\"x-large\" weight=\"bold\">%s v%s</span>\n\
<span size=\"large\">A Simulation of Tracy Wilson's Three Axis Telescope Mount</span>\n\n\
Copyright (c) 2004 Steve Joiner (steve@daisyhill.net)", PACKAGE, VERSION);
	gtk_label_set_markup(about_label, about_text);
	gtk_widget_show(about_dialog);
	g_free(about_text);
	g_free(window_title);
}

/*
** on_about_ok_button_clicked
*/

void on_about_ok_button_clicked(GtkButton *button, gpointer user_data)
{
	gtk_widget_destroy(gtk_widget_get_toplevel((GtkWidget *)button));
}

/*
** on_menu_contents_activate
*/

void on_menu_contents_activate(GtkMenuItem *menuitem, gpointer user_data)
{
	GtkWidget *help_contents_dialog;

	help_contents_dialog = create_help_dialog();
	gtk_widget_show(help_contents_dialog);
}

/*
** on_help_ok_button_clicked
*/

void on_help_ok_button_clicked(GtkButton *button, gpointer user_data)
{
	gtk_widget_destroy(gtk_widget_get_toplevel((GtkWidget *)button));
}



