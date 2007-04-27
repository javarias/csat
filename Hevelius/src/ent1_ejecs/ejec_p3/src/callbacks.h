/*
** callbacks.h
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

#include <gtk/gtk.h>

gboolean delete_event(GtkWidget *widget, GdkEvent *event, gpointer data);
void destroy(GtkWidget *widget, gpointer data);
void on_menu_quit_activate(GtkMenuItem*menuitem, gpointer user_data);
void on_menu_about_activate(GtkMenuItem *menuitem, gpointer user_data);
void on_about_ok_button_clicked(GtkButton *button, gpointer user_data);
void on_menu_contents_activate(GtkMenuItem *menuitem, gpointer user_data);
void on_help_ok_button_clicked(GtkButton *button, gpointer user_data);
