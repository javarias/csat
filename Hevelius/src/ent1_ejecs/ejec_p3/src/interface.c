#ifdef HAVE_CONFIG_H
#  include <config.h>
#endif

#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <string.h>
#include <stdio.h>

#include <gdk/gdkkeysyms.h>
#include <gtk/gtk.h>

#include "callbacks.h"
#include "interface.h"
#include "support.h"

#define GLADE_HOOKUP_OBJECT(component,widget,name) \
  g_object_set_data_full (G_OBJECT (component), name, \
    gtk_widget_ref (widget), (GDestroyNotify) gtk_widget_unref)

#define GLADE_HOOKUP_OBJECT_NO_REF(component,widget,name) \
  g_object_set_data (G_OBJECT (component), name, widget)

GtkWidget*
create_main_window (void)
{
  GtkWidget *main_window;
  GtkWidget *vbox1;
  GtkWidget *mainhbox;
  GtkWidget *table2;
  GtkWidget *alt_scale;
  GtkWidget *az_scale;
  GtkWidget *ha_scale;
  GtkWidget *dec_scale;
  GtkWidget *frot_scale;
  GtkWidget *a1_scale;
  GtkWidget *a2_scale;
  GtkWidget *a3_scale;
  GtkWidget *lat_scale;
  GtkWidget *calt_scale;
  GtkWidget *caz_scale;
  GtkAccelGroup *accel_group;

  accel_group = gtk_accel_group_new ();

  main_window = gtk_window_new (GTK_WINDOW_TOPLEVEL);

  vbox1 = gtk_vbox_new (FALSE, 12);
  gtk_widget_show (vbox1);
  gtk_container_add (GTK_CONTAINER (main_window), vbox1);

  mainhbox = gtk_hbox_new (FALSE, 12);
  gtk_widget_show (mainhbox);
  gtk_box_pack_start (GTK_BOX (vbox1), mainhbox, TRUE, TRUE, 0);
  gtk_container_set_border_width (GTK_CONTAINER (mainhbox), 6);

  alt_scale = gtk_hscale_new (GTK_ADJUSTMENT (gtk_adjustment_new (0, 0, 0, 0, 0, 0)));
  gtk_widget_show (alt_scale);
  gtk_table_attach (GTK_TABLE (table2), alt_scale, 1, 2, 0, 1,
                    (GtkAttachOptions) (GTK_FILL),
                    (GtkAttachOptions) (GTK_FILL), 0, 0);
  gtk_widget_set_size_request (alt_scale, 260, -1);

  az_scale = gtk_hscale_new (GTK_ADJUSTMENT (gtk_adjustment_new (0, 0, 0, 0, 0, 0)));
  gtk_widget_show (az_scale);
  gtk_table_attach (GTK_TABLE (table2), az_scale, 1, 2, 1, 2,
                    (GtkAttachOptions) (GTK_FILL),
                    (GtkAttachOptions) (GTK_FILL), 0, 0);

  ha_scale = gtk_hscale_new (GTK_ADJUSTMENT (gtk_adjustment_new (0, 0, 0, 0, 0, 0)));
  gtk_widget_show (ha_scale);
  gtk_table_attach (GTK_TABLE (table2), ha_scale, 1, 2, 3, 4,
                    (GtkAttachOptions) (GTK_FILL),
                    (GtkAttachOptions) (GTK_FILL), 0, 0);

  dec_scale = gtk_hscale_new (GTK_ADJUSTMENT (gtk_adjustment_new (0, 0, 0, 0, 0, 0)));
  gtk_widget_show (dec_scale);
  gtk_table_attach (GTK_TABLE (table2), dec_scale, 1, 2, 4, 5,
                    (GtkAttachOptions) (GTK_FILL),
                    (GtkAttachOptions) (GTK_FILL), 0, 0);

  frot_scale = gtk_hscale_new (GTK_ADJUSTMENT (gtk_adjustment_new (0, 0, 0, 0, 0, 0)));
  gtk_widget_show (frot_scale);
  gtk_table_attach (GTK_TABLE (table2), frot_scale, 1, 2, 6, 7,
                    (GtkAttachOptions) (GTK_FILL),
                    (GtkAttachOptions) (GTK_FILL), 0, 0);

  a1_scale = gtk_hscale_new (GTK_ADJUSTMENT (gtk_adjustment_new (0, 0, 0, 0, 0, 0)));
  gtk_widget_show (a1_scale);
  gtk_table_attach (GTK_TABLE (table2), a1_scale, 1, 2, 8, 9,
                    (GtkAttachOptions) (GTK_FILL),
                    (GtkAttachOptions) (GTK_FILL), 0, 0);

  a2_scale = gtk_hscale_new (GTK_ADJUSTMENT (gtk_adjustment_new (0, 0, 0, 0, 0, 0)));
  gtk_widget_show (a2_scale);
  gtk_table_attach (GTK_TABLE (table2), a2_scale, 1, 2, 9, 10,
                    (GtkAttachOptions) (GTK_FILL),
                    (GtkAttachOptions) (GTK_FILL), 0, 0);

  a3_scale = gtk_hscale_new (GTK_ADJUSTMENT (gtk_adjustment_new (0, 0, 0, 0, 0, 0)));
  gtk_widget_show (a3_scale);
  gtk_table_attach (GTK_TABLE (table2), a3_scale, 1, 2, 10, 11,
                    (GtkAttachOptions) (GTK_FILL),
                    (GtkAttachOptions) (GTK_FILL), 0, 0);

  lat_scale = gtk_hscale_new (GTK_ADJUSTMENT (gtk_adjustment_new (0, 0, 0, 0, 0, 0)));
  gtk_widget_show (lat_scale);
  gtk_table_attach (GTK_TABLE (table2), lat_scale, 1, 2, 12, 13,
                    (GtkAttachOptions) (GTK_FILL),
                    (GtkAttachOptions) (GTK_FILL), 0, 0);

  calt_scale = gtk_hscale_new (GTK_ADJUSTMENT (gtk_adjustment_new (0, 0, 0, 0, 0, 0)));
  gtk_widget_show (calt_scale);
  gtk_table_attach (GTK_TABLE (table2), calt_scale, 1, 2, 14, 15,
                    (GtkAttachOptions) (GTK_FILL),
                    (GtkAttachOptions) (GTK_FILL), 0, 0);

  caz_scale = gtk_hscale_new (GTK_ADJUSTMENT (gtk_adjustment_new (0, 0, 0, 0, 0, 0)));
  gtk_widget_show (caz_scale);
  gtk_table_attach (GTK_TABLE (table2), caz_scale, 1, 2, 15, 16,
                    (GtkAttachOptions) (GTK_FILL),
                    (GtkAttachOptions) (GTK_FILL), 0, 0);

  /* Store pointers to all widgets, for use by lookup_widget(). */
  GLADE_HOOKUP_OBJECT_NO_REF (main_window, main_window, "main_window");
  GLADE_HOOKUP_OBJECT (main_window, vbox1, "vbox1");
  GLADE_HOOKUP_OBJECT (main_window, mainhbox, "mainhbox");
  GLADE_HOOKUP_OBJECT (main_window, alt_scale, "alt_scale");
  GLADE_HOOKUP_OBJECT (main_window, az_scale, "az_scale");
  GLADE_HOOKUP_OBJECT (main_window, ha_scale, "ha_scale");
  GLADE_HOOKUP_OBJECT (main_window, dec_scale, "dec_scale");
  GLADE_HOOKUP_OBJECT (main_window, frot_scale, "frot_scale");
  GLADE_HOOKUP_OBJECT (main_window, a1_scale, "a1_scale");
  GLADE_HOOKUP_OBJECT (main_window, a2_scale, "a2_scale");
  GLADE_HOOKUP_OBJECT (main_window, a3_scale, "a3_scale");
  GLADE_HOOKUP_OBJECT (main_window, lat_scale, "lat_scale");
  GLADE_HOOKUP_OBJECT (main_window, calt_scale, "calt_scale");
  GLADE_HOOKUP_OBJECT (main_window, caz_scale, "caz_scale");

  gtk_window_add_accel_group (GTK_WINDOW (main_window), accel_group);

  return main_window;
}
