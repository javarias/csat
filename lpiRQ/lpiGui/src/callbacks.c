#ifdef HAVE_CONFIG_H
#  include <config.h>
#endif

#include <gtk/gtk.h>

#include "callbacks.h"
#include "interface.h"
#include "support.h"
#include "ccd.h"
#include <math.h>
#include <string.h>
#include <stdio.h>

void
on_nuevo1_activate                     (GtkMenuItem     *menuitem,
                                        gpointer         user_data)
{

}


void
on_abrir1_activate                     (GtkMenuItem     *menuitem,
                                        gpointer         user_data)
{

}


void
on_guardar1_activate                   (GtkMenuItem     *menuitem,
                                        gpointer         user_data)
{

}


void
on_guardar_como1_activate              (GtkMenuItem     *menuitem,
                                        gpointer         user_data)
{

}


void
on_salir1_activate                     (GtkMenuItem     *menuitem,
                                        gpointer         user_data)
{
	//to store the selected values
	FILE *archConf;
	GtkWidget *temp;
	int num;
	float value;
	archConf=fopen("/tmp/values.tdm", "w");

	temp = lookup_widget(GTK_WIDGET(menuitem), "exposureScale");
	num = gtk_range_get_value(GTK_RANGE(temp));
	fprintf(archConf, "exposure: %d\n", num);

	temp = lookup_widget(GTK_WIDGET(menuitem), "resetScale");
	num = gtk_range_get_value(GTK_RANGE(temp));
	fprintf(archConf, "reset: %d\n", num);

	temp = lookup_widget(GTK_WIDGET(menuitem), "PixelScale");
	value = gtk_range_get_value(GTK_RANGE(temp));
	fprintf(archConf, "pixel: %f\n", value);

	temp = lookup_widget(GTK_WIDGET(menuitem), "redScale");
	value = gtk_range_get_value(GTK_RANGE(temp));
	fprintf(archConf, "red: %f\n", value);

	temp = lookup_widget(GTK_WIDGET(menuitem), "greenScale");
	value = gtk_range_get_value(GTK_RANGE(temp));
	fprintf(archConf, "green: %f\n", value);

	temp = lookup_widget(GTK_WIDGET(menuitem), "blueScale");
	value = gtk_range_get_value(GTK_RANGE(temp));
	fprintf(archConf, "blue: %f\n", value);
	

	fclose(archConf);
	gtk_main_quit();
}


void
on_cortar1_activate                    (GtkMenuItem     *menuitem,
                                        gpointer         user_data)
{

}


void
on_copiar1_activate                    (GtkMenuItem     *menuitem,
                                        gpointer         user_data)
{

}


void
on_pegar1_activate                     (GtkMenuItem     *menuitem,
                                        gpointer         user_data)
{

}


void
on_borrar1_activate                    (GtkMenuItem     *menuitem,
                                        gpointer         user_data)
{

}


void
on_acerca_de1_activate                 (GtkMenuItem     *menuitem,
                                        gpointer         user_data)
{

}


void
on_exposureScale_value_changed         (GtkRange        *range,
                                        gpointer         user_data)
{
	gint num;
	gchar number[3];
	struct ccd *cam;
	cam = user_data;
	GtkWidget *entry = lookup_widget(GTK_WIDGET(range), "exposureEntry");
	num = gtk_range_get_value(GTK_RANGE(range));
	//sprintf(number, "%d", num);
	//gtk_entry_set_text(GTK_ENTRY(entry), number);
	change_control(cam,V4L2_CID_EXPOSURE, num);
	sleep(0.01);
}


void
on_exposureEntry_changed               (GtkEditable     *editable,
                                        gpointer         user_data)
{
	gint num = 0;
	GtkWidget *range = lookup_widget(GTK_WIDGET(editable), "exposureScale");
	gchar *entry_text = gtk_entry_get_text(GTK_ENTRY(editable));
	num = atoi(entry_text);
	gtk_range_set_value(GTK_RANGE(range),num);
}


void
on_resetScale_value_changed            (GtkRange        *range,
                                        gpointer         user_data)
{
	gint num;
        gchar number[3];
	struct ccd *cam;
        cam = user_data;
        GtkWidget *entry = lookup_widget(GTK_WIDGET(range), "resetEntry");
        num = gtk_range_get_value(GTK_RANGE(range));
        //sprintf(number, "%d", num);
        //gtk_entry_set_text(GTK_ENTRY(entry), number);
	change_control(cam,SN9C102_V4L2_CID_RESET_LEVEL, num);
	sleep(0.01);
}


void
on_resetEntry_changed                  (GtkEditable     *editable,
                                        gpointer         user_data)
{
	gint num = 0;
        GtkWidget *range = lookup_widget(GTK_WIDGET(editable), "resetScale");
        gchar *entry_text = gtk_entry_get_text(GTK_ENTRY(editable));
        num = atoi(entry_text);
        gtk_range_set_value(GTK_RANGE(range),num);
}


void
on_PixelScale_value_changed            (GtkRange        *range,
                                        gpointer         user_data)
{
        gfloat num;
        //gchar number[4];
	struct ccd *cam;
        cam = user_data;
        GtkWidget *entry = lookup_widget(GTK_WIDGET(range), "pixelEntry");
        //num = gtk_range_get_value(GTK_RANGE(range));
        //sprintf(number, "%f", num);
	//number[3]='\0';
        //gtk_entry_set_text(GTK_ENTRY(entry), number);
	change_control(cam,SN9C102_V4L2_CID_PIXEL_BIAS_VOLTAGE, (int)(num*7));
	sleep(0.01);
}


void
on_pixelEntry_changed                  (GtkEditable     *editable,
                                        gpointer         user_data)
{
        gfloat num;
	char *p;
        GtkWidget *range = lookup_widget(GTK_WIDGET(editable), "PixelScale");
        gchar *entry_text = gtk_entry_get_text(GTK_ENTRY(editable));
	num = (entry_text[0]-'0') + (entry_text[2]-'0')/10.0;
        gtk_range_set_value(GTK_RANGE(range),num);
}


void
on_contrastScale_value_changed         (GtkRange        *range,
                                        gpointer         user_data)
{
        gfloat num;
        //gchar number[4];
        GtkWidget *entry = lookup_widget(GTK_WIDGET(range), "contrastEntry");
        num = gtk_range_get_value(GTK_RANGE(range));
        //sprintf(number, "%f", num);
        //number[3]='\0';
        //gtk_entry_set_text(GTK_ENTRY(entry), number);
	sleep(0.01);
}


void
on_contrastEntry_changed               (GtkEditable     *editable,
                                        gpointer         user_data)
{
        gfloat num;
        char *p;
        GtkWidget *range = lookup_widget(GTK_WIDGET(editable), "contrastScale");
        gchar *entry_text = gtk_entry_get_text(GTK_ENTRY(editable));
        num = (entry_text[0]-'0') + (entry_text[2]-'0')/10.0;
        gtk_range_set_value(GTK_RANGE(range),num);
}


void
on_redScale_value_changed              (GtkRange        *range,
                                        gpointer         user_data)
{
        gfloat num;
        //gchar number[4];
	struct ccd *cam;
        cam = user_data;
        GtkWidget *entry = lookup_widget(GTK_WIDGET(range), "redEntry");
        //num = gtk_range_get_value(GTK_RANGE(range));
        //sprintf(number, "%f", num);
        //number[3]='\0';
        //gtk_entry_set_text(GTK_ENTRY(entry), number);
	change_control(cam,V4L2_CID_RED_BALANCE, (int)(63*num));
	sleep(0.01);
}


void
on_redEntry_changed                    (GtkEditable     *editable,
                                        gpointer         user_data)
{
        gfloat num;
        char *p;
        GtkWidget *range = lookup_widget(GTK_WIDGET(editable), "redScale");
        gchar *entry_text = gtk_entry_get_text(GTK_ENTRY(editable));
        num = (entry_text[0]-'0') + (entry_text[2]-'0')/10.0;
        gtk_range_set_value(GTK_RANGE(range),num);
}


void
on_greenScale_value_changed            (GtkRange        *range,
                                        gpointer         user_data)
{
        gfloat num;
        //gchar number[4];
	struct ccd *cam;
        cam = user_data;
        GtkWidget *entry = lookup_widget(GTK_WIDGET(range), "greenEntry");
        num = gtk_range_get_value(GTK_RANGE(range));
        //sprintf(number, "%f", num);
        //number[3]='\0';
        //gtk_entry_set_text(GTK_ENTRY(entry), number);
	change_control(cam,SN9C102_V4L2_CID_GREEN_BALANCE, (int)(63*num));
	sleep(0.01);
}


void
on_greenEntry_changed                  (GtkEditable     *editable,
                                        gpointer         user_data)
{
        gfloat num;
        char *p;
        GtkWidget *range = lookup_widget(GTK_WIDGET(editable), "greenScale");
        gchar *entry_text = gtk_entry_get_text(GTK_ENTRY(editable));
        num = (entry_text[0]-'0') + (entry_text[2]-'0')/10.0;
        gtk_range_set_value(GTK_RANGE(range),num);
}


void
on_blueScale_value_changed             (GtkRange        *range,
                                        gpointer         user_data)
{
        gfloat num;
        //gchar number[4];
	struct ccd *cam;
        cam = user_data;
        GtkWidget *entry = lookup_widget(GTK_WIDGET(range), "blueEntry");
        num = gtk_range_get_value(GTK_RANGE(range));
        //sprintf(number, "%f", num);
        //number[3]='\0';
        //gtk_entry_set_text(GTK_ENTRY(entry), number);
	change_control(cam,V4L2_CID_BLUE_BALANCE, (int)(63*num));
	sleep(0.01);
}


void
on_blueEntry_changed                   (GtkEditable     *editable,
                                        gpointer         user_data)
{
        gfloat num;
        char *p;
        GtkWidget *range = lookup_widget(GTK_WIDGET(editable), "blueScale");
        gchar *entry_text = gtk_entry_get_text(GTK_ENTRY(editable));
        num = (entry_text[0]-'0') + (entry_text[2]-'0')/10.0;
        gtk_range_set_value(GTK_RANGE(range),num);
}

void
on_image_update (GtkWidget *widget,
		GdkEventExpose *event,
		gpointer user_data)
{
}

gboolean update_image(gpointer user_data)
{

	unsigned char *img;
        img = (unsigned char*)malloc(640*480*3);
	struct ccd *cam; 
        cam = user_data;
	read_frame(cam);
        process_image(cam->buffers[0].start, img);
	gdk_draw_rgb_image (LpiImageG->window, LpiImageG->style->fg_gc[GTK_STATE_NORMAL],0, 110, 640, 480,GDK_RGB_DITHER_MAX, img, 640*3);
	free(img);
	return TRUE;
}
