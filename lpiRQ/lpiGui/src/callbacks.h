#include <gtk/gtk.h>


void
on_nuevo1_activate                     (GtkMenuItem     *menuitem,
                                        gpointer         user_data);

void
on_abrir1_activate                     (GtkMenuItem     *menuitem,
                                        gpointer         user_data);

void
on_guardar1_activate                   (GtkMenuItem     *menuitem,
                                        gpointer         user_data);

void
on_guardar_como1_activate              (GtkMenuItem     *menuitem,
                                        gpointer         user_data);

void
on_salir1_activate                     (GtkMenuItem     *menuitem,
                                        gpointer         user_data);

void
on_cortar1_activate                    (GtkMenuItem     *menuitem,
                                        gpointer         user_data);

void
on_copiar1_activate                    (GtkMenuItem     *menuitem,
                                        gpointer         user_data);

void
on_pegar1_activate                     (GtkMenuItem     *menuitem,
                                        gpointer         user_data);

void
on_borrar1_activate                    (GtkMenuItem     *menuitem,
                                        gpointer         user_data);

void
on_acerca_de1_activate                 (GtkMenuItem     *menuitem,
                                        gpointer         user_data);

void
on_exposureScale_value_changed         (GtkRange        *range,
                                        gpointer         user_data);

void
on_exposureEntry_changed               (GtkEditable     *editable,
                                        gpointer         user_data);

void
on_resetScale_value_changed            (GtkRange        *range,
                                        gpointer         user_data);

void
on_resetEntry_changed                  (GtkEditable     *editable,
                                        gpointer         user_data);

void
on_PixelScale_value_changed            (GtkRange        *range,
                                        gpointer         user_data);

void
on_pixelEntry_changed                  (GtkEditable     *editable,
                                        gpointer         user_data);

void
on_contrastScale_value_changed         (GtkRange        *range,
                                        gpointer         user_data);

void
on_contrastEntry_changed               (GtkEditable     *editable,
                                        gpointer         user_data);

void
on_redScale_value_changed              (GtkRange        *range,
                                        gpointer         user_data);

void
on_redEntry_changed                    (GtkEditable     *editable,
                                        gpointer         user_data);

void
on_greenScale_value_changed            (GtkRange        *range,
                                        gpointer         user_data);

void
on_greenEntry_changed                  (GtkEditable     *editable,
                                        gpointer         user_data);

void
on_blueScale_value_changed             (GtkRange        *range,
                                        gpointer         user_data);

void
on_blueEntry_changed                   (GtkEditable     *editable,
                                        gpointer         user_data);

void
on_image_update (GtkWidget *widget,
                GdkEventExpose *event,
                gpointer user_data);

gboolean update_image(gpointer user_data);
