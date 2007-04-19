/*
** globject.h
**
** Functions for creating complex objects in OpenGL.
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

#ifndef GLOBJECT_H
#define GLOBJECT_H

#include <GL/gl.h>

#ifndef PI
#define PI 3.1415926535897932384626433832795028841971693993751f
#endif

typedef struct {
	GLfloat x;
	GLfloat y;
	GLfloat z;
} GlobVector;

void glob_vector_assemble(GlobVector *w, GLfloat x, GLfloat y, GLfloat z);
void glob_vector_scale(GlobVector *w, GlobVector *v, GLfloat scale);
void glob_vector_add(GlobVector *w, GlobVector *v1, GlobVector *v2);
void glob_vector_subtract(GlobVector *w, GlobVector *v1, GlobVector *v2);
void glob_vector_cross_product(GlobVector *w, GlobVector *v1, GlobVector *v2);
GLfloat glob_vector_dot_product(GlobVector *v1, GlobVector *v2);
void glob_vector_normalize(GlobVector *w, GlobVector *v);
void glob_compute_face_normal(GlobVector *w,
							  GlobVector *v1,
							  GlobVector *v2,
							  GlobVector *v3);
void glob_compute_cylinder_normal(GlobVector *w, GlobVector *v);
void glob_draw_polygon_with_face_normal(GlobVector *v, int num_vertices);
void glob_draw_polygon_with_cylinder_normal(GlobVector *v, int num_vertices);
void glob_extrude_polygon(GlobVector *v, int num_vertices,
						  GLfloat length, int side_type);
void glob_draw_regular_polygon(int num_sides, GLfloat radius);
void glob_extrude_regular_polygon(int num_sides, int num_sides_shown,
								  GLfloat radius, GLfloat thickness,
								  GLfloat length, int side_type);
void glob_draw_dome(int num_lat_div, int start_lat_div, int end_lat_div,
					int num_lon_div, int start_lon_div, int end_lon_div,
					GLfloat *start_color, GLfloat *end_color,
					GLfloat radius);
#endif /* GLOBJECT_H */
