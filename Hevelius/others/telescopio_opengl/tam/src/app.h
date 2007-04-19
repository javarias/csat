/*
** app.h
**
** Application specific data for the three axis telescope simulation.
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

#ifndef APP_H
#define APP_H

#include <gtk/gtk.h>

#include "model.h"

typedef struct {
	Model *model;
	GtkWidget *main_window;
	GtkWidget *gl_drawing_area;
	gboolean gl_initialized;
	gint width;
	gint height;
	gdouble aspect;
	GtkHScale *alt_scale;
	GtkHScale *az_scale;
	GtkHScale *ha_scale;
	GtkHScale *dec_scale;
	GtkHScale *frot_scale;
	GtkHScale *a1_scale;
	GtkHScale *a2_scale;
	GtkHScale *a3_scale;
	GtkHScale *lat_scale;
	GtkHScale *calt_scale;
	GtkHScale *caz_scale;
	gboolean servicing_value_changed;
	gdouble alt_limit;
} AppData;

#endif /* APP_H */
