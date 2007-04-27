/*
** globject.c
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

#include <math.h>
#include <stdlib.h>
#include <GL/gl.h>

#include "globject.h"

/*
** glob_vector_assemble
**
** This function assigns the x, y, and z values to the vector w.
*/

void glob_vector_assemble(GlobVector *w, GLfloat x, GLfloat y, GLfloat z)
{
	w->x = x;
	w->y = y;
	w->z = z;
}

/*
** glob_vector_scale
**
** This function computes the result of scaling the input vector v, by the
** scalar factor, scale.  The result is returned in the vector w.
*/

void glob_vector_scale(GlobVector *w, GlobVector *v, GLfloat scale)
{
	w->x = scale * v->x;
	w->y = scale * v->y;
	w->z = scale * v->z;
}

/*
** glob_vector_add
**
** This function adds the two input vectors v1 and v2, and returns the
** result in the vector w.
*/

void glob_vector_add(GlobVector *w, GlobVector *v1, GlobVector *v2)
{
	w->x = v1->x + v2->x;
	w->y = v1->y + v2->y;
	w->z = v1->z + v2->z;
}

/*
** glob_vector_subtract
**
** This function subtracts the vector v2 from the vector v1 and returns the
** result in the vector w.
*/

void glob_vector_subtract(GlobVector *w, GlobVector *v1, GlobVector *v2)
{
	w->x = v1->x - v2->x;
	w->y = v1->y - v2->y;
	w->z = v1->z - v2->z;
}

/*
** glob_vector_cross_product
**
** This function computes the cross product of vectors v1 and v2, and
** returns the result in the vector w.
*/

void glob_vector_cross_product(GlobVector *w, GlobVector *v1, GlobVector *v2)
{
	w->x = v1->y * v2->z - v1->z * v2->y;
	w->y = v1->z * v2->x - v1->x * v2->z;
	w->z = v1->x * v2->y - v1->y * v2->x;
}

/*
** glob_vector_dot_product
**
** This function returns the dot product of the two vectors v1 and v2.
*/

GLfloat glob_vector_dot_product(GlobVector *v1, GlobVector *v2)
{
	return v1->x * v2->x + v1->y * v2->y + v1->z * v2->z;
}

/*
** glob_vector normalize
**
** This function returns a normalized version of vector v in vector w.
*/

void glob_vector_normalize(GlobVector *w, GlobVector *v)
{
	GLfloat length;

	length = sqrt(glob_vector_dot_product(v, v));
	if (length == 0.0)
	{
		w->x = w->y = w->z = 0.0;
		return;
	}
	length = 1.0 / length;

	w->x = length * v->x;
	w->y = length * v->y;
	w->z = length * v->z;
}

/*
**  glob_compute_face_normal
**
**  This function computes the normal vector of the plane defined by the
**  three points v1, v2, and v3.  The order of the points defines the
**  direction of the vector according to the "right hand rule."  The result
**  is returned in vector w.
*/

void glob_compute_face_normal(GlobVector *w,
							  GlobVector *v1,
							  GlobVector *v2,
							  GlobVector *v3)
{
	GlobVector t1, t2;

	glob_vector_subtract(&t1, v3, v2);
	glob_vector_subtract(&t2, v1, v2);
	glob_vector_cross_product(w, &t1, &t2);
	glob_vector_normalize(w, w);
}

/*
** glob_compute_cylinder_normal
**
** This function computes the normal vector for a point on the surface of a
** cylinder whose primary axis is the z axis.  The result is returned in
** vector w.
*/

void glob_compute_cylinder_normal(GlobVector *w, GlobVector *v)
{
	w->x = v->x;
	w->y = v->y;
	w->z = 0;
	glob_vector_normalize(w, w);
}

/*
** glob_draw_polygon_with_face_normal
**
** This function draws a polygon with vertices defined by v.  The number of
** vertices is given by num_vertices.  There must be at least three
** vertices.
**
** It is assumed that all of the vertices of the polygon lie in the same
** plane.  The normal vector for all vertices is defined by the plane
** defined by the first three vertices according to the "right hand rule."
**
** glVertex commands are generated for the polygon, but the enclosing
** glBegin and glEnd statements are not.
*/

void glob_draw_polygon_with_face_normal(GlobVector *v, int num_vertices)
{
	int i;
	GlobVector n;

	glob_compute_face_normal(&n, &v[0], &v[1], &v[2]);
	glNormal3f(n.x, n.y, n.z);

	for (i = 0; i < num_vertices; i++)
	{
		glVertex3f(v[i].x, v[i].y, v[i].z);
	}
}

/*
** glob_draw_polygon_with_cylinder_normal
**
** This function draws a polygon with vertices defined by v.  The number of
** vertices is given by num_vertices.
**
** The normal vectors for each vertex in the polygon are defined as if the
** vertices were on the surface of a cylinder whose primary axis is the z
** axis.
**
** glVertex commands are generated for the polygon, but the enclosing
** glBegin and glEnd statements are not.
*/

void glob_draw_polygon_with_cylinder_normal(GlobVector *v, int num_vertices)
{
	int i;
	GlobVector n;

	for (i = 0; i < num_vertices; i++)
	{
		glob_compute_cylinder_normal(&n, &v[i]);
		glNormal3f(n.x, n.y, n.z);
		glVertex3f(v[i].x, v[i].y, v[i].z);
	}
}

/*
** glob_extrude_polygon
**
** Create an extruded solid with the cross section of an arbitrary
** polygon.  The argument v is an array of vertices defining the polygon.
** The num_vertices argument indicates the number of vertices in the
** array.  The vertices must be defined in a counter-clockwise fashion when
** viewing from the positive z axis.  The length argument indicates the
** length of the extrusion.  The extrusion is done along the z axis with
** half of the length on the positive side and half of the length on the
** negative side.  The side_type argument indicates whether the sides are
** drawn with face normals (0) or cylinder normals (1).
*/

void glob_extrude_polygon(GlobVector *v, int num_vertices,
						  GLfloat length, int side_type)
{
	int i, j;
	GLfloat z;
	GlobVector n;
	GlobVector quad[4];

	
	/*
	** TODO:  Can I use regular_polygon() here?  I may need to translate
	** first to get the z offset.
	*/

	z = 0.5 * length;
	
	/*
	** Front face.
	*/

	glBegin(GL_POLYGON);

	glob_compute_face_normal(&n, &v[0], &v[1], &v[2]);
	glNormal3f(n.x, n.y, n.z);

	for (i = 0; i < num_vertices; i++)
	{
		glVertex3f(v[i].x, v[i].y, v[i].z + z);
	}
	
	glEnd();

	/*
	** Rear face.
	*/

	glBegin(GL_POLYGON);

	glob_compute_face_normal(&n, &v[0], &v[1], &v[2]);
	glNormal3f(-n.x, -n.y, -n.z);

	for (i = num_vertices-1; i >= 0; i--)
	{
		glVertex3f(v[i].x, v[i].y, v[i].z - z);
	}
	
	glEnd();
	
	/*
	** Sides.
	*/

	glBegin(GL_QUADS);

	for (i = 0; i < num_vertices; i++)
	{
		j = i + 1; /* get index of next vertex */
		if (j >= num_vertices) j = 0;

		glob_vector_assemble(&quad[0], v[i].x, v[i].y, v[i].z - z);
		glob_vector_assemble(&quad[1], v[j].x, v[j].y, v[j].z - z);
		glob_vector_assemble(&quad[2], v[j].x, v[j].y, v[j].z + z);
		glob_vector_assemble(&quad[3], v[i].x, v[i].y, v[i].z + z);

		if (side_type)
		{
			glob_draw_polygon_with_cylinder_normal(quad, 4);
		}
		else
		{
			glob_draw_polygon_with_face_normal(quad, 4);
		}
	}
	
	glEnd();
}

/*
** glob_draw_regular_polygon
**
** Draws a two-dimensional regular polygon with given number of sides and
** radius.  The polygon is centered in the x-y plane with its normal in the
** positive z direction.
*/

void glob_draw_regular_polygon(int num_sides, GLfloat radius)
{
	GLfloat theta = 2.0 * PI / (GLfloat) num_sides;
	int i;
	
	glBegin(GL_POLYGON);

	glNormal3f(0.0, 0.0, 1.0);
	
	for (i = 0; i < num_sides; i++)
	{
		glVertex3f(radius * cos(i * theta), radius * sin(i * theta), 0.0);
	}
	
	glEnd();
}

/*
** glob_extrude_regular_polygon
**
** Create an extruded solid or tube with the cross section of a regular
** polygon.  num_sides defines the number of sides of the polygon.
** num_sides_shown defines the number of sides actually drawn -- this is
** for creating arcs, and is only valid if radius != thickness.  radius is
** the outer radius of the polygon.  thickness defines the outer wall
** thickness.  If thickness == radius, then the extrusion is a solid rather
** than a tube.  length defines the length of the extrusion, which is done
** half in the positive z direction and half in the negative z direction.
** The side_type argument indicates whether the inner and outer sides are
** drawn with face normals (0) or cylinder normals (1).
*/

void glob_extrude_regular_polygon(int num_sides, int num_sides_shown,
								  GLfloat radius, GLfloat thickness,
								  GLfloat length, int side_type)
{
	GLfloat theta = 2.0 * PI / (GLfloat) num_sides;
	GLfloat z = 0.5 * length;

	if (thickness == radius) /* solid, not tube */
	{
		GlobVector v1, v2;
		GlobVector quad[4];
		int i;
		
		/*
		** Sides.
		*/

		glBegin(GL_QUADS);

		for (i = 0; i < num_sides; i++)
		{
			glob_vector_assemble(&v1, radius * cos(i * theta),
								 radius * sin (i * theta), 0.0);
			glob_vector_assemble(&v2, radius * cos((i+1) * theta),
								 radius * sin ((i+1) * theta), 0.0);

			glob_vector_assemble(&quad[0], v1.x, v1.y, v1.z - z);
			glob_vector_assemble(&quad[1], v2.x, v2.y, v2.z - z);
			glob_vector_assemble(&quad[2], v2.x, v2.y, v2.z + z);
			glob_vector_assemble(&quad[3], v1.x, v1.y, v1.z + z);

			if (side_type)
			{
				glob_draw_polygon_with_cylinder_normal(quad, 4);
			}
			else
			{
				glob_draw_polygon_with_face_normal(quad, 4);
			}
		}

		glEnd();

		/*
		** Caps.
		*/

		glPushMatrix();
		glTranslatef(0.0, 0.0, z);
		glob_draw_regular_polygon(num_sides, radius);
		glPopMatrix();

		glPushMatrix();
		glTranslatef(0.0, 0.0, -z);
		glRotatef(180.0, 0.0, 1.0, 0.0); /* flip normal */
		glob_draw_regular_polygon(num_sides, radius);
		glPopMatrix();
	}
	else /* tube, not solid */
	{
		GLfloat inner_radius = radius - thickness;
		int start_side = (num_sides - num_sides_shown) / 2;
		int end_side = start_side + num_sides_shown;
		int i;
		
		glBegin(GL_QUADS);

		/*
		** Side and top segments.
		*/
		
		for (i = start_side; i < end_side; i++)
		{
			GlobVector out_first, out_next;
			GlobVector in_first, in_next;
			GlobVector outer_face[4];
			GlobVector inner_face[4];
			GlobVector top_face[4];
			GlobVector bottom_face[4];

			glob_vector_assemble(&out_first, radius * cos(i * theta), radius * sin(i * theta), 0.0);
			glob_vector_assemble(&out_next, radius * cos((i+1) * theta), radius * sin((i+1) * theta), 0.0);
			glob_vector_assemble(&in_first, inner_radius * cos(i * theta), inner_radius * sin(i * theta), 0.0);
			glob_vector_assemble(&in_next, inner_radius * cos((i+1) * theta), inner_radius * sin((i+1) * theta), 0.0);

			glob_vector_assemble(&outer_face[0], out_first.x, out_first.y, out_first.z - z);
			glob_vector_assemble(&outer_face[1], out_next.x, out_next.y, out_next.z - z);
			glob_vector_assemble(&outer_face[2], out_next.x, out_next.y, out_next.z + z);
			glob_vector_assemble(&outer_face[3], out_first.x, out_first.y, out_first.z + z);

			glob_vector_assemble(&inner_face[0], in_next.x, in_next.y, in_next.z + z);
			glob_vector_assemble(&inner_face[1], in_next.x, in_next.y, in_next.z - z);
			glob_vector_assemble(&inner_face[2], in_first.x, in_first.y, in_first.z - z);
			glob_vector_assemble(&inner_face[3], in_first.x, in_first.y, in_first.z + z);

			glob_vector_assemble(&top_face[0], out_next.x, out_next.y, out_next.z + z);
			glob_vector_assemble(&top_face[1], in_next.x, in_next.y, in_next.z + z);
			glob_vector_assemble(&top_face[2], in_first.x, in_first.y, in_first.z + z);
			glob_vector_assemble(&top_face[3], out_first.x, out_first.y, out_first.z + z);

			glob_vector_assemble(&bottom_face[0], in_first.x, in_first.y, in_first.z - z);
			glob_vector_assemble(&bottom_face[1], in_next.x, in_next.y, in_next.z - z);
			glob_vector_assemble(&bottom_face[2], out_next.x, out_next.y, out_next.z - z);
			glob_vector_assemble(&bottom_face[3], out_first.x, out_first.y, out_first.z - z);

			if (side_type)
			{
				glob_draw_polygon_with_cylinder_normal(outer_face, 4);
				glob_draw_polygon_with_cylinder_normal(inner_face, 4);
			}
			else
			{
				glob_draw_polygon_with_face_normal(outer_face, 4);
				glob_draw_polygon_with_face_normal(inner_face, 4);
			}

			glob_draw_polygon_with_face_normal(top_face, 4);
			glob_draw_polygon_with_face_normal(bottom_face, 4);
		}

		/*
		** End caps for partial extrusions.
		*/

		if (num_sides != num_sides_shown)
		{
			GlobVector start_outer, start_inner;
			GlobVector end_outer, end_inner;
			GlobVector start_cap[4];
			GlobVector end_cap[4];

			glob_vector_assemble(&start_outer,       radius * cos(start_side * theta),       radius * sin(start_side * theta), 0.0);
			glob_vector_assemble(&start_inner, inner_radius * cos(start_side * theta), inner_radius * sin(start_side * theta), 0.0);
			glob_vector_assemble(&end_outer,         radius * cos(end_side   * theta),       radius * sin(end_side   * theta), 0.0);
			glob_vector_assemble(&end_inner,   inner_radius * cos(end_side   * theta), inner_radius * sin(end_side   * theta), 0.0);

			glob_vector_assemble(&start_cap[0], start_outer.x, start_outer.y, start_outer.z + z);
			glob_vector_assemble(&start_cap[1], start_inner.x, start_inner.y, start_inner.z + z);
			glob_vector_assemble(&start_cap[2], start_inner.x, start_inner.y, start_inner.z - z);
			glob_vector_assemble(&start_cap[3], start_outer.x, start_outer.y, start_outer.z - z);

			glob_vector_assemble(&end_cap[0], end_outer.x, end_outer.y, end_outer.z + z);
			glob_vector_assemble(&end_cap[1], end_outer.x, end_outer.y, end_outer.z - z);
			glob_vector_assemble(&end_cap[2], end_inner.x, end_inner.y, end_inner.z - z);
			glob_vector_assemble(&end_cap[3], end_inner.x, end_inner.y, end_inner.z + z);

			glob_draw_polygon_with_face_normal(start_cap, 4);
			glob_draw_polygon_with_face_normal(end_cap, 4);
		}

		glEnd();
	}
}

/*
** glob_draw_dome
**
** This is an experimental function to draw a dome (basically, the inside
** of a sphere) for the purposes of simulating a sky.
*/

#define SIN_GRAD 1

#if SIN_GRAD
GLfloat get_color(GLfloat start, GLfloat end, GLfloat lat)
{
	return 0.5 * (start - end) * cos(lat - 0.5 * PI) + 0.5 * fabs(start - end);
}
#endif

void glob_draw_dome(int num_lat_div, int start_lat_div, int end_lat_div,
					int num_lon_div, int start_lon_div, int end_lon_div,
					GLfloat *start_color, GLfloat *end_color,
					GLfloat radius)
{
	GLfloat color[3];
	GLfloat next_color[3];
	GLfloat color_inc[3];
	GLfloat lat, next_lat, lat_inc;
	GLfloat lon, next_lon, lon_inc;
	int i, j;

	lat_inc = PI / (GLfloat) (num_lat_div - 1);
	lon_inc = 2.0 * PI / (GLfloat) num_lon_div;
	
	for (i = 0; i<3; i++)
	{
#if SIN_GRAD
		color[i] = get_color(start_color[i], end_color[i], 0.5 * PI);
		next_color[i] = get_color(start_color[i], end_color[i], 0.5 * PI - lat_inc);
#else		
		color[i] = start_color[i];
		color_inc[i] = (end_color[i] - start_color[i]) / (GLfloat) (num_lat_div - 1);
		next_color[i] = color[i] + color_inc[i];
#endif		
	}

	
	glBegin(GL_TRIANGLES);

	if (start_lat_div == 0)
	{
		for (j = 0; j < num_lon_div; j++)
		{
			next_lat = 0.5 * PI - lat_inc;

			if ((j >= start_lon_div) && (j <= end_lon_div))
			{
				lon = j * lon_inc;
				next_lon = (j + 1) * lon_inc;

				glColor3fv(color);
				glVertex3f(0.0, radius, 0.0);
				glColor3fv(next_color);
				glVertex3f(radius * cos(lon), radius * sin(next_lat), radius * sin(lon));
				glVertex3f(radius * cos(next_lon), radius * sin(next_lat), radius * sin(next_lon));
			}
		}
	}

	glEnd();

	glBegin(GL_QUADS);
	
	for (i = 1; i < (num_lat_div - 2); i++)
	{
#if !SIN_GRAD			
		for (j = 0; j < 3; j++)
		{
			color[j] += color_inc[j];
			next_color[j] += color_inc[j];
		}
#endif
		if ((i >= start_lat_div) && (i <= end_lat_div))
		{
			lat = 0.5 * PI - i * lat_inc;
			next_lat = 0.5 * PI - (i + 1) * lat_inc;

#if SIN_GRAD
			for (j = 0; j < 3; j++)
			{
				color[j] = get_color(start_color[j], end_color[j], lat);
				next_color[j] = get_color(start_color[j], end_color[j], next_lat);
			}
#endif			
			
			for (j = 0; j < num_lon_div; j++)
			{
				if ((j >= start_lon_div) && (j <= end_lon_div))
				{
					lon = j * lon_inc;
					next_lon = (j + 1) * lon_inc;
					
					glColor3fv(color);
					glVertex3f(radius * cos(next_lon), radius * sin(lat), radius * sin(next_lon));
					glVertex3f(radius * cos(lon), radius * sin(lat), radius * sin(lon));
					glColor3fv(next_color);
					glVertex3f(radius * cos(lon), radius * sin(next_lat), radius * sin(lon));
					glVertex3f(radius * cos(next_lon), radius * sin(next_lat), radius * sin(next_lon));
				}
			}
		}
	}

	glEnd();

	if (end_lat_div >= (num_lat_div - 1))
	{
		lat = 0.5 * PI - (num_lat_div - 2) * lat_inc;

		for (j = 0; j < 3; j++)
		{
#if SIN_GRAD
			color[j] = get_color(start_color[j], end_color[j], lat);
			next_color[j] = get_color(start_color[j], end_color[j], -0.5 * PI);
#else			
			color[j] += color_inc[j];
			next_color[j] += color_inc[j];
#endif			
		}

		glBegin(GL_TRIANGLES);
		
		for (j = 0; j < num_lon_div; j++)
		{
			/* next_lat = 0.5 * PI - num_lat_div * lat_inc; */

			if ((j >= start_lon_div) && (j <= end_lon_div))
			{
				lon = j * lon_inc;
				next_lon = (j + 1) * lon_inc;

				glColor3fv(color);
 				glVertex3f(radius * cos(next_lon), radius * sin(lat), radius * sin(next_lon));
				glVertex3f(radius * cos(lon), radius * sin(lat), radius * sin(lon));
				glColor3fv(next_color);
				glVertex3f(0.0, -radius, 0.0);
			}
		}

		glEnd();
	}
}
