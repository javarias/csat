/*
** controls.c
**
** Control handlers for the three axis telescope simulation.
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

/* system headers */
#include <gtk/gtk.h>
#include <gtk/gtkgl.h>
#include <GL/gl.h>
#include <GL/glu.h>

/* glade headers */
#include "support.h"

/* application headers */
#include "app.h"
#include "coord.h"

#include <stdio.h>

static void ctrl_init_scale(AppData *app_data, gchar *name,
							gdouble value, gdouble lower,
							gdouble upper, gdouble step_increment,
							gdouble page_increment, gdouble page_size,
							GtkHScale **adj,
							void (*on_adj_changed)(GtkAdjustment *adj, gpointer data));
static void ctrl_on_alt_adj_value_changed(GtkAdjustment *adjustment, gpointer data);
static void ctrl_on_az_adj_value_changed(GtkAdjustment *adjustment, gpointer data);
static void ctrl_on_ha_adj_value_changed(GtkAdjustment *adjustment, gpointer data);
static void ctrl_on_dec_adj_value_changed(GtkAdjustment *adjustment, gpointer data);
static void ctrl_on_frot_adj_value_changed(GtkAdjustment *adjustment, gpointer data);
static void ctrl_on_a1_adj_value_changed(GtkAdjustment *adjustment, gpointer data);
static void ctrl_on_a2_adj_value_changed(GtkAdjustment *adjustment, gpointer data);
static void ctrl_on_a3_adj_value_changed(GtkAdjustment *adjustment, gpointer data);
static void ctrl_on_lat_adj_value_changed(GtkAdjustment *adjustment, gpointer data);
static void ctrl_on_calt_adj_value_changed(GtkAdjustment *adjustment, gpointer data);
static void ctrl_on_caz_adj_value_changed(GtkAdjustment *adjustment, gpointer data);
static void ctrl_on_a3_coords_changed(AppData *app_data);
static void ctrl_on_eq_coords_changed(AppData *app_data);
static void ctrl_on_altaz_coords_changed(AppData *app_data);
static void ctrl_on_frot_coord_changed(AppData *app_data);
static void ctrl_on_camera_coords_changed(AppData *app_data);

/*
** ctrl_init_scales
*/

void ctrl_init_scales(AppData *app_data)
{
	ctrl_init_scale(app_data, "alt_scale",   45.0,    0.0,  90.0, 1.0, 10.0, 0.0, &app_data->alt_scale, ctrl_on_alt_adj_value_changed);
	ctrl_init_scale(app_data, "az_scale",   180.0,    0.0, 360.0, 1.0, 10.0, 0.0, &app_data->az_scale, ctrl_on_az_adj_value_changed);
	ctrl_init_scale(app_data, "ha_scale",     0.0, -180.0, 180.0, 1.0, 10.0, 0.0, &app_data->ha_scale, ctrl_on_ha_adj_value_changed);
	ctrl_init_scale(app_data, "dec_scale",    0.0,  -90.0,  90.0, 1.0, 10.0, 0.0, &app_data->dec_scale, ctrl_on_dec_adj_value_changed);
	ctrl_init_scale(app_data, "frot_scale",   0.0,    0.0, 360.0, 1.0, 10.0, 0.0, &app_data->frot_scale, ctrl_on_frot_adj_value_changed);
	ctrl_init_scale(app_data, "a1_scale",    45.0,    0.0, 180.0, 1.0, 10.0, 0.0, &app_data->a1_scale, ctrl_on_a1_adj_value_changed);
	ctrl_init_scale(app_data, "a2_scale",     0.0,  -90.0,  90.0, 1.0, 10.0, 0.0, &app_data->a2_scale, ctrl_on_a2_adj_value_changed);
	ctrl_init_scale(app_data, "a3_scale",   180.0,    0.0, 360.0, 1.0, 10.0, 0.0, &app_data->a3_scale, ctrl_on_a3_adj_value_changed);
	ctrl_init_scale(app_data, "lat_scale",   45.0,  -90.0,  90.0, 1.0, 10.0, 0.0, &app_data->lat_scale, ctrl_on_lat_adj_value_changed);
	ctrl_init_scale(app_data, "calt_scale",   0.0,  -90.0,  90.0, 1.0, 10.0, 0.0, &app_data->calt_scale, ctrl_on_calt_adj_value_changed);
	ctrl_init_scale(app_data, "caz_scale",  180.0,    0.0, 360.0, 1.0, 10.0, 0.0, &app_data->caz_scale, ctrl_on_caz_adj_value_changed);

	
}

/*
** ctrl_init_scale
**
** Helper function for initializing scales.
*/

static void ctrl_init_scale(AppData *app_data, gchar *name,
							gdouble value, gdouble lower,
							gdouble upper, gdouble step_increment,
							gdouble page_increment, gdouble page_size,
							GtkHScale **hscale,
							void (*on_adj_changed)(GtkAdjustment *adj, gpointer data))
{
	GtkAdjustment *adj;

	*hscale = GTK_HSCALE(lookup_widget(GTK_WIDGET(app_data->main_window), name));
	adj = gtk_range_get_adjustment(GTK_RANGE(*hscale));
	adj->upper = upper;
	adj->lower = lower;
	adj->value = value;
	adj->step_increment = step_increment;
	adj->page_increment = page_increment;
	adj->page_size = page_size;

#if 1	
	gtk_signal_connect(GTK_OBJECT((*hscale)), "value-changed",
					   GTK_SIGNAL_FUNC(on_adj_changed), app_data);
#else	
	gtk_signal_connect(GTK_OBJECT((adj)), "value-changed",
					   GTK_SIGNAL_FUNC(on_adj_changed), app_data);
#endif
}

/*
** ctrl_on_alt_adj_value_changed
*/

static void ctrl_on_alt_adj_value_changed(GtkAdjustment *adjustment, gpointer data)
{
/*	FILE *refAzF,*refAltF;
        double refAz,refAlt;
        refAzF=fopen("/tmp/refAzDevIO","w");
        refAltF=fopen("/tmp/refAltDevIO","w");
	refAz=(double)gtk_range_get_value(GTK_RANGE(((AppData*)data)->az_scale));
	refAlt=(double)gtk_range_get_value(GTK_RANGE(((AppData*)data)->alt_scale));
        fprintf(refAzF,"%lf",refAz);
        fprintf(refAltF,"%lf",refAlt);
	

        fclose(refAzF);
        fclose(refAltF);
*/	
	ctrl_on_altaz_coords_changed((AppData*) data);
}

/*
** ctrl_on_az_adj_value_changed
*/

static void ctrl_on_az_adj_value_changed(GtkAdjustment *adjustment, gpointer data)
{
	ctrl_on_altaz_coords_changed((AppData*) data);
}

/*
** ctrl_on_ha_adj_value_changed
*/

static void ctrl_on_ha_adj_value_changed(GtkAdjustment *adjustment, gpointer data)
{
	ctrl_on_eq_coords_changed((AppData*) data);
}

/*
** ctrl_on_dec_adj_value_changed
*/

static void ctrl_on_dec_adj_value_changed(GtkAdjustment *adjustment, gpointer data)
{
	ctrl_on_eq_coords_changed((AppData*) data);
}

/*
** ctrl_on_frot_adj_value_changed
*/

static void ctrl_on_frot_adj_value_changed(GtkAdjustment *adjustment, gpointer data)
{
	ctrl_on_frot_coord_changed((AppData*) data);
}

/*
** ctrl_on_a1_adj_value_changed
*/

static void ctrl_on_a1_adj_value_changed(GtkAdjustment *adjustment, gpointer data)
{
	ctrl_on_a3_coords_changed((AppData*) data);
}

/*
** ctrl_on_a2_adj_value_changed
*/

static void ctrl_on_a2_adj_value_changed(GtkAdjustment *adjustment, gpointer data)
{
	ctrl_on_a3_coords_changed((AppData*) data);
}

/*
** ctrl_on_a3_adj_value_changed
*/

static void ctrl_on_a3_adj_value_changed(GtkAdjustment *adjustment, gpointer data)
{
	ctrl_on_a3_coords_changed((AppData*) data);
}

/*
** ctrl_on_lat_adj_value_changed
*/

static void ctrl_on_lat_adj_value_changed(GtkAdjustment *adjustment, gpointer data)
{
	ctrl_on_a3_coords_changed((AppData*) data);
}

/*
** ctrl_on_calt_adj_value_changed
*/

static void ctrl_on_calt_adj_value_changed(GtkAdjustment *adjustment, gpointer data)
{
	ctrl_on_camera_coords_changed((AppData*) data);
}

/*
** ctrl_on_caz_adj_value_changed
*/

static void ctrl_on_caz_adj_value_changed(GtkAdjustment *adjustment, gpointer data)
{
	ctrl_on_camera_coords_changed((AppData*) data);
}

/*
** ctrl_on_a3_coords_changed
*/

static void ctrl_on_a3_coords_changed(AppData *app_data)

{
	gdouble alt, az;
	gdouble ha, dec;
	gdouble a1, a2, a3;
	gdouble frot;
	
	if (app_data->servicing_value_changed) return;
	app_data->servicing_value_changed = TRUE;

	coord_a3_to_altaz(&az, &alt,
					  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->a1_scale))),
					  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->a2_scale))),
					  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->a3_scale))),
					  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->lat_scale))));

	if (alt < DEG_TO_RAD(app_data->alt_limit))
	{
		/*
		** If the new coordinates would get us too close to the horizon,
		** then revert to the previous coordinates.
		*/

		coord_eq_to_a3(&a1, &a2, &a3,
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->ha_scale))),
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->dec_scale))),
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->lat_scale))),
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->frot_scale))));

		gtk_range_set_value(GTK_RANGE(app_data->a1_scale), RAD_TO_DEG(a1));
		gtk_range_set_value(GTK_RANGE(app_data->a2_scale), RAD_TO_DEG(a2));
		gtk_range_set_value(GTK_RANGE(app_data->a3_scale), RAD_TO_DEG(a3));
	}
	else
	{
		coord_a3_to_eq(&ha, &dec,
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->a1_scale))),
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->a2_scale))),
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->a3_scale))),
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->lat_scale))));
		coord_a3_to_frot(&frot,
						 DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->a1_scale))),
						 DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->a2_scale))),
						 DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->a3_scale))),
						 DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->lat_scale))));

		gtk_range_set_value(GTK_RANGE(app_data->ha_scale), RAD_TO_DEG(ha));
		gtk_range_set_value(GTK_RANGE(app_data->dec_scale), RAD_TO_DEG(dec));
		gtk_range_set_value(GTK_RANGE(app_data->az_scale), RAD_TO_DEG(az));
		gtk_range_set_value(GTK_RANGE(app_data->alt_scale), RAD_TO_DEG(alt));
		gtk_range_set_value(GTK_RANGE(app_data->frot_scale), RAD_TO_DEG(frot));
	}
#if 0
	gdk_window_invalidate_rect(app_data->gl_drawing_area->window,
							   &(app_data->gl_drawing_area->allocation),
							   FALSE);
#else	
	gtk_widget_queue_draw(GTK_WIDGET(app_data->gl_drawing_area));
	/* gtk_widget_queue_draw(app_data->main_window); */
#endif	
	app_data->servicing_value_changed = FALSE;
}

/*
** ctrl_on_eq_cords_changed
*/

static void ctrl_on_eq_coords_changed(AppData *app_data)
{
	gdouble alt, az;
	gdouble ha, dec;
	gdouble a1, a2, a3;
	
	if (app_data->servicing_value_changed) return;
	app_data->servicing_value_changed = TRUE;

	coord_eq_to_altaz(&az, &alt,
					  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->ha_scale))),
					  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->dec_scale))),
					  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->lat_scale))));

	if (alt < DEG_TO_RAD(app_data->alt_limit))
	{
		/*
		** If the new equatorial coordinates would get us too close to the
		** horizon, then revert to the previous coordinates.
		*/

		coord_altaz_to_eq(&ha, &dec,
						  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->az_scale))),
						  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->alt_scale))),
						  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->lat_scale))));

		gtk_range_set_value(GTK_RANGE(app_data->ha_scale), RAD_TO_DEG(ha));
		gtk_range_set_value(GTK_RANGE(app_data->dec_scale), RAD_TO_DEG(dec));
	}
	else
	{
		coord_eq_to_a3(&a1, &a2, &a3,
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->ha_scale))),
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->dec_scale))),
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->lat_scale))),
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->frot_scale))));

 		gtk_range_set_value(GTK_RANGE(app_data->az_scale), RAD_TO_DEG(az));
		gtk_range_set_value(GTK_RANGE(app_data->alt_scale), RAD_TO_DEG(alt));
		gtk_range_set_value(GTK_RANGE(app_data->a1_scale), RAD_TO_DEG(a1));
		gtk_range_set_value(GTK_RANGE(app_data->a2_scale), RAD_TO_DEG(a2));
		gtk_range_set_value(GTK_RANGE(app_data->a3_scale), RAD_TO_DEG(a3));
	}

	gdk_window_invalidate_rect(app_data->gl_drawing_area->window,
							   &(app_data->gl_drawing_area->allocation),
							   FALSE);

	app_data->servicing_value_changed = FALSE;
}

/*
** ctrl_on_altaz_coords_changed
*/

static void ctrl_on_altaz_coords_changed(AppData *app_data)
{
	if (app_data->servicing_value_changed) return;
	app_data->servicing_value_changed = TRUE;

	if (gtk_range_get_value(GTK_RANGE(app_data->alt_scale)) < app_data->alt_limit)
	{
		gdouble az, alt;

		/*
		** If the new coordinates would get us too close to the horizon,
		** then revert to the previous coordinates.
		*/

		coord_eq_to_altaz(&az, &alt,
						  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->ha_scale))),
						  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->dec_scale))),
						  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->lat_scale))));

		gtk_range_set_value(GTK_RANGE(app_data->az_scale), RAD_TO_DEG(az));
		gtk_range_set_value(GTK_RANGE(app_data->alt_scale), RAD_TO_DEG(alt));
	}
	else
	{
		gdouble ha, dec;
		gdouble a1, a2, a3;
		gdouble prev_a3;
		gdouble prev_ha;

		prev_ha = DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->ha_scale)));
		prev_a3 = DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->a3_scale)));
		
		coord_altaz_to_eq(&ha, &dec,
						  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->az_scale))),
						  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->alt_scale))),
						  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->lat_scale))));

		/*
		** If we are pointing directly at the pole, then the conversion
		** from az, alt to ha is undefined.  In this case, we use the
		** previous value of ha in order to prevent discontinuities in
		** ha. (0.000277 degrees is 1 arc second, so we use that precision
		** for determining when we're pointing at the pole).
		*/

		if ((fabs(gtk_range_get_value(GTK_RANGE(app_data->alt_scale))
				  - gtk_range_get_value(GTK_RANGE(app_data->lat_scale))) < 0.00027)
			&& (gtk_range_get_value(GTK_RANGE(app_data->az_scale)) < 0.00027))
		{
			ha = prev_ha;
		}
		
		coord_eq_to_a3(&a1, &a2, &a3, ha, dec,
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->lat_scale))),
					   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->frot_scale))));

		/*
		** If we move through the pole, this causes a discontinuity in the
		** coordinates due to the field rotation flip at the pole.  We
		** detect this by looking for a3 to change by approximately 180
		** degrees.  If this happens, we add 180 degrees to the field
		** rotation and recompute the coordinates.
		*/

		if (fabs(prev_a3 - a3) > (0.95 * PI))
		{
			gdouble new_frot;
			
			new_frot = DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->frot_scale)));
			new_frot = coord_wrap(new_frot + PI);

			gtk_range_set_value(GTK_RANGE(app_data->frot_scale), RAD_TO_DEG(new_frot));
			
			coord_eq_to_a3(&a1, &a2, &a3, ha, dec,
						   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->lat_scale))),
						   new_frot);
		}
		
		gtk_range_set_value(GTK_RANGE(app_data->ha_scale), RAD_TO_DEG(ha));
		gtk_range_set_value(GTK_RANGE(app_data->dec_scale), RAD_TO_DEG(dec));
		gtk_range_set_value(GTK_RANGE(app_data->a1_scale), RAD_TO_DEG(a1));
		gtk_range_set_value(GTK_RANGE(app_data->a2_scale), RAD_TO_DEG(a2));
		gtk_range_set_value(GTK_RANGE(app_data->a3_scale), RAD_TO_DEG(a3));
	}

	gdk_window_invalidate_rect(app_data->gl_drawing_area->window,
							   &(app_data->gl_drawing_area->allocation),
							   FALSE);

	app_data->servicing_value_changed = FALSE;
}

/*
** ctrl_on_frot_coord_changed
*/

static void ctrl_on_frot_coord_changed(AppData *app_data)
{
	gdouble a1, a2, a3;
	
	if (app_data->servicing_value_changed) return;
	app_data->servicing_value_changed = TRUE;

	coord_eq_to_a3(&a1, &a2, &a3,
				   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->ha_scale))),
				   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->dec_scale))),
				   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->lat_scale))),
				   DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->frot_scale))));

	gtk_range_set_value(GTK_RANGE(app_data->a1_scale), RAD_TO_DEG(a1));
	gtk_range_set_value(GTK_RANGE(app_data->a2_scale), RAD_TO_DEG(a2));
	gtk_range_set_value(GTK_RANGE(app_data->a3_scale), RAD_TO_DEG(a3));

	gdk_window_invalidate_rect(app_data->gl_drawing_area->window,
							   &(app_data->gl_drawing_area->allocation),
							   FALSE);

	app_data->servicing_value_changed = FALSE;
}

/*
** ctrl_on_camera_coords_changed
*/

static void ctrl_on_camera_coords_changed(AppData *app_data)
{
	gdouble x, y, z;
	GLfloat light0_position[4];
	gdouble laz, lalt;
	
	if (!app_data->gl_initialized) return;

	
	coord_camera_altaz_to_xyz(&x, &y, &z,
							  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->caz_scale))),
							  DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->calt_scale))));

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();

	gluPerspective(45.0, app_data->aspect, 0.1, 100.0);
	gluLookAt(x, y, z, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

	glMatrixMode(GL_MODELVIEW);

	/* Adjust light to follow camera */
	laz = DEG_TO_RAD(gtk_range_get_value(GTK_RANGE(app_data->caz_scale)));
	lalt = DEG_TO_RAD(-20.0 + gtk_range_get_value(GTK_RANGE(app_data->calt_scale)));

	light0_position[0] = cos(lalt) * sin(laz);
	light0_position[1] = sin(lalt);
	light0_position[2] = -cos(lalt) * cos(laz);
	light0_position[3] = 0.0;

	glLoadIdentity();
	glLightfv(GL_LIGHT0, GL_POSITION, light0_position);
	
	gdk_window_invalidate_rect(app_data->gl_drawing_area->window,
							   &(app_data->gl_drawing_area->allocation),
							   FALSE);
}
gboolean poll_file(AppData *app_data){
	FILE *refAzF,*refAltF,*realAzF,*realAltF;
	double realAz,realAlt,azStep=0,altStep=0;
	//refAzF=fopen("/tmp/refAzDevIO","rw");
	//refAltF=fopen("/tmp/refAltDevIO","rw");
	realAzF=fopen("/tmp/realAzDevIO","r");
	realAltF=fopen("/tmp/realAltDevIO","r");
	fscanf(realAzF,"%lf",&realAz);
	fscanf(realAltF,"%lf",&realAlt);
 	if(realAlt==0)realAlt=1;
	if(realAz<0)realAz=360+realAz;
	realAz=360-realAz;
	if(realAz!=(double)gtk_range_get_value(GTK_RANGE(app_data->az_scale))){
		azStep=realAz-(double)gtk_range_get_value(GTK_RANGE(app_data->az_scale));
		if(azStep>2)azStep=2;
		if(azStep<-2)azStep=-2;
		gtk_range_set_value(GTK_RANGE(app_data->az_scale), (double)gtk_range_get_value(GTK_RANGE(app_data->az_scale))+azStep);		
		ctrl_on_altaz_coords_changed(app_data);
	}
        if(realAlt!=(double)gtk_range_get_value(GTK_RANGE(app_data->alt_scale))){
                altStep=realAlt-(double)gtk_range_get_value(GTK_RANGE(app_data->alt_scale));
                if(altStep>2)altStep=2;
                if(altStep<-2)altStep=-2;
                gtk_range_set_value(GTK_RANGE(app_data->alt_scale), (double)gtk_range_get_value(GTK_RANGE(app_data->alt_scale))+altStep);
                ctrl_on_altaz_coords_changed(app_data);
        }
	//fprintf(realAzF,"%lf",(double)gtk_range_get_value(GTK_RANGE(app_data->az_scale))+azStep);
	//fprintf(realAltF,"%lf",(double)gtk_range_get_value(GTK_RANGE(app_data->alt_scale))+altStep);
	
	//fclose(refAzF);
	//fclose(refAltF);
	fclose(realAzF);
	fclose(realAltF);
	return TRUE;
}
