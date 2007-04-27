/*
** model.h
**
** OpenGL model for three axis telescope.
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
#include "model.h"


#define COPY_COLOR(dest, src) dest[0] = src[0]; dest[1] = src[1]; dest[2] = src[2];

static void model_set_default_parameters(Model *model);
static void model_draw_base(Model *model);
static void model_draw_platform(Model *model);
static void model_draw_horseshoe(Model *model);
static void model_draw_tube(Model *model);

Model* model_init(void)
{
	Model *model;
	GLuint list_start;

	model = malloc(sizeof(Model));

	if (model == NULL) return NULL;

	model_set_default_parameters(model);
	list_start = glGenLists(4);

	model->display_list[MODEL_DISPLAY_LIST_BASE] = list_start;
	glNewList(model->display_list[MODEL_DISPLAY_LIST_BASE], GL_COMPILE);
	model_draw_base(model);
	glEndList();

	model->display_list[MODEL_DISPLAY_LIST_PLATFORM] = list_start + 1;
	glNewList(model->display_list[MODEL_DISPLAY_LIST_PLATFORM], GL_COMPILE);
	model_draw_platform(model);
	glEndList();

	model->display_list[MODEL_DISPLAY_LIST_HORSESHOE] = list_start + 2;
	glNewList(model->display_list[MODEL_DISPLAY_LIST_HORSESHOE], GL_COMPILE);
	model_draw_horseshoe(model);
	glEndList();

	model->display_list[MODEL_DISPLAY_LIST_TUBE] = list_start + 3;
	glNewList(model->display_list[MODEL_DISPLAY_LIST_TUBE], GL_COMPILE);
	model_draw_tube(model);
	glEndList();

	return model;
}

void model_draw(Model *model, GLfloat a1, GLfloat a2, GLfloat a3)
{
	/*
	** Note the screen coordinate system is different from the alt-az
	** coordinate system.
	**
	** x_screen = y_altaz
	** y_screen = z_altaz
	** z_screen = x_altaz
	*/

#if 0
	/*
	** I was experimenting with drawing a sky around the scope.  Turn it
	** off for now.
	*/
	
	GLfloat start_color[3] = {255.0, 0.0, 0.0};
	GLfloat end_color[3] = {0.0, 255.0, 0.0};

	glob_draw_dome(21, 0, 20, 20, 0, 20, start_color, end_color, 30.0);
#endif
	
	glCallList(model->display_list[MODEL_DISPLAY_LIST_BASE]);

	glRotatef(-a3, 0.0, 1.0, 0.0);
	glCallList(model->display_list[MODEL_DISPLAY_LIST_PLATFORM]);

	glRotatef(a2, 0.0, 0.0, 1.0);
	glCallList(model->display_list[MODEL_DISPLAY_LIST_HORSESHOE]);

	glRotatef(a1, 1.0, 0.0, 0.0);
	glCallList(model->display_list[MODEL_DISPLAY_LIST_TUBE]);

}

static void model_set_default_parameters(Model *model)
{
	static GLfloat light_gray[] = {234.0/256.0, 232.0/256.0, 227.0/256.0};
	static GLfloat green[] = {62.0/256.0, 148.0/256.0, 0.0/256.0};
	static GLfloat blue[] = {0.0/256.0, 62.0/256.0, 148.0/256.0};
	static GLfloat purple[] = {85.0/256.0, 0.0/256.0, 148.0/256.0};
	static GLfloat brown[] = {148.0/256.0, 85.0/256.0, 0.0/256.0};

	model->eps = 0.01;

	static GLfloat *vane_and_spider_color = blue;
	static GLfloat *focuser_color = blue;
	static GLfloat *tube_color = green;
	static GLfloat *mirror_color = light_gray;
	static GLfloat *holder_color = blue;
	static GLfloat *horseshoe_color = brown;
	static GLfloat *rocker_color = purple;
	static GLfloat *base_color = blue;
	static GLfloat *pad_color = brown;
	
	/*
	** Tube.
	*/

	model->tube_height = 10.5;
	model->tube_radius = 1.75;
	model->tube_thickness = 0.1;
	model->tube_num_sides = 24;
	model->tube_top_to_center = 8.0;
	COPY_COLOR(model->tube_color, tube_color);

	model->mirror_height = 0.25;
	model->mirror_radius = 1.5;
	model->mirror_num_sides = 24;
	COPY_COLOR(model->mirror_color, mirror_color);

	model->sec_to_tube_top = 1.0;
	model->sec_height = 1.0;
	model->sec_radius = 0.5;
	model->sec_num_sides = 24;
	model->num_vanes = 4;
	model->vane_inner_height = 0.5;
	model->vane_outer_height = 0.25;
	model->vane_thickness = 0.05;
	COPY_COLOR(model->sec_color, vane_and_spider_color);
	COPY_COLOR(model->vane_color, vane_and_spider_color);

	model->focuser_height = 0.5;
	model->focuser_radius = 0.4;
	model->focuser_thickness = 0.05;
	model->focuser_num_sides = 24;
	model->focuser_angle = 45.0;
	COPY_COLOR(model->focuser_color, focuser_color);

	/*
	** Horseshoe.
	*/
	
	model->horseshoe_num_sides = 24;
	model->horseshoe_partial_sides = 18;
	model->horseshoe_radius = 3.5;
	model->horseshoe_thickness = 0.5;
	model->horseshoe_height = 0.5;
	model->horseshoe_separation = 5.0;
	COPY_COLOR(model->horseshoe_color, horseshoe_color);

	/*
	** Platform.
	*/

	model->platform_num_sides = 4;
	model->platform_radius = 0.707 * (model->horseshoe_separation
									  + 0.5 * model->horseshoe_height) + 0.707;
	model->platform_height = 0.5;
	model->platform_side_facet_length = 0.6;
	COPY_COLOR(model->platform_color, rocker_color);

		
	model->rocker_num_sides = 24;
	model->rocker_partial_sides = 6;
	model->rocker_thickness = 0.75;
	model->rocker_radius = (model->horseshoe_radius
							+ model->rocker_thickness
							+ model->eps);
	model->rocker_height = 0.75;
	COPY_COLOR(model->rocker_color, rocker_color);

	model->plate_thickness = 0.25;
	model->plate_right = 0.5 * (model->horseshoe_separation
								- model->horseshoe_height);
	model->plate_left = -model->plate_right;
	model->plate_top = sin(0.25 * PI) * (model->horseshoe_radius
										 - model->horseshoe_thickness);
	model->plate_bottom = -model->plate_top;
	model->plate_offset = cos(0.25 * PI) * (model->horseshoe_radius
											- model->horseshoe_thickness);
	COPY_COLOR(model->plate_color, horseshoe_color);

	model->holder_outer_num_sides = 8;
	model->holder_inner_num_sides = 24;
	model->holder_radius = (model->plate_offset
							/ cos(PI / (GLfloat) model->holder_outer_num_sides));
	model->holder_thickness = (model->holder_radius
							   - (model->tube_radius
								  / cos(PI / (GLfloat) model->holder_outer_num_sides)));
	model->holder_height = 0.5;
	model->holder_offset = model->plate_right - 1.5 * model->holder_height;
	model->iplate_thickness = 0.25;
	model->iplate_right = model->plate_right - 1.5 * model->holder_thickness;
	model->iplate_left = -model->iplate_right;
	model->iplate_top = (model->plate_offset
						 * sin(PI / (GLfloat) model->holder_outer_num_sides));
	model->iplate_bottom = -model->iplate_top;
	model->iplate_offset = (model->plate_offset
							- 0.5 * model->plate_thickness
							- 0.5 * model->iplate_thickness);
	COPY_COLOR(model->holder_color, holder_color);

	/*
	** Base.
	*/

	model->base_num_sides = 24;
	model->base_radius = 0.707 * model->platform_radius;
	model->base_height = 0.5;
	COPY_COLOR(model->base_color, base_color);

	model->num_pads = 3;
	model->pad_num_sides = 24;
	model->pad_radius = 0.2 * model->base_radius;
	model->pad_height = 0.25;
	COPY_COLOR(model->pad_color, pad_color);
}

static void model_draw_base(Model *model)
{
	GLfloat pad_offset_radius;
	GLfloat pad_offset_theta;
	int i;
	
	/*
	** Base.
	*/
	
	glColor3fv(model->base_color);
	glPushMatrix();
	glTranslatef(0.0, -(model->rocker_radius
						+ model->platform_height
						+ 0.5 * model->base_height
						+ 2.0 * model->eps), 0.0);
	glRotatef(90.0, 1.0, 0.0, 0.0);
	glob_extrude_regular_polygon(model->base_num_sides, /* num_sides */
								 model->base_num_sides, /* num_sides_shown */
								 model->base_radius, /* radius */
								 model->base_radius, /* thickness */
								 model->base_height, /* length */
								 1); /* cylinder normals */
	glPopMatrix();

	/*
	** Pads.
	*/

	glColor3fv(model->pad_color);
	pad_offset_radius = model->base_radius - model->pad_radius;
	pad_offset_theta = 2.0 * PI / (GLfloat) model->num_pads;

	for (i = 0; i < model->num_pads; i++)
	{
		glPushMatrix();
		glTranslatef(pad_offset_radius * cos(i * pad_offset_theta),
					 0.0,
					 pad_offset_radius * sin(i * pad_offset_theta));
		glTranslatef(0.0, -(model->rocker_radius
							+ model->platform_height
							+ model->base_height
							+ 0.5 * model->pad_height
							+ 3.0 * model->eps), 0.0);
		glRotatef(90.0, 1.0, 0.0, 0.0);
		glob_extrude_regular_polygon(model->pad_num_sides, /* num_sides */
									 model->pad_num_sides, /* num_sides_shown */
									 model->pad_radius, /* radius */
									 model->pad_radius, /* thickness */
									 model->pad_height, /* length */
									 1); /* cylinder normals */
		glPopMatrix();
	}
}

static void model_draw_platform(Model *model)
{
	GLfloat sign;
	GlobVector side[4];
	
	/*
	** Rockers.
	*/

	glColor3fv(model->rocker_color);

	for (sign = -1.0; sign <= 1.0; sign += 2.0)
	{
		glPushMatrix();
		glTranslatef(0.0, 0.0, sign * 0.5 * model->horseshoe_separation);
		glRotatef(90.0, 0.0, 0.0, 1.0);
		glob_extrude_regular_polygon(model->rocker_num_sides, /* num_sides */
									 model->rocker_partial_sides, /* num_sides_shown */
									 model->rocker_radius, /* radius */
									 model->rocker_thickness, /* thickness */
									 model->rocker_height, /* length */
									 1); /* cylinder normals */
		glPopMatrix();
	}

	/*
	** Platform Sides.
	*/

	
	glColor3fv(model->platform_color);

	glob_vector_assemble(&side[3], 0.0, 0.0, 0.0);
	glob_vector_assemble(&side[2], -0.7071 * model->platform_radius, 0.0, 0.0);
	glob_vector_assemble(&side[1], -0.7071 * model->platform_radius,
						 model->platform_side_facet_length, 0.0);
	glob_vector_assemble(&side[0],
						 -0.7071 * (model->platform_radius - model->platform_side_facet_length),
						 1.7071 * model->platform_side_facet_length,
						 0.0);

	for (sign = -1.0; sign <= 1.0; sign += 2.0)
	{
		glPushMatrix();
		glTranslatef(0.0, -model->rocker_radius, sign * 0.5 * model->horseshoe_separation);
		glob_extrude_polygon(side, 4, 0.8 * model->rocker_height, 0);
		glRotatef(180.0, 0.0, 1.0, 0.0);
		glob_extrude_polygon(side, 4, 0.8 * model->rocker_height, 0);
		glPopMatrix();
	}
	
	/*
	** Platform.
	*/

	glColor3fv(model->platform_color);
	glPushMatrix();
	glTranslatef(0.0, -(model->rocker_radius
						+ 0.5 * model->platform_height
						+ model->eps), 0.0);
	glRotatef(90.0, 1.0, 0.0, 0.0);
	glRotatef(45.0, 0.0, 0.0, 1.0);
	glob_extrude_regular_polygon(model->platform_num_sides, /* num_sides */
								 model->platform_num_sides, /* num_sides_shown */
								 model->platform_radius, /* radius */
								 model->platform_radius, /* thickness */
								 model->platform_height, /* length */
								 0); /* cylinder normals */
	glPopMatrix();
}

static void model_draw_horseshoe(Model *model)
{
	GLfloat sign;
	GlobVector plate[4];
	
	/*
	** Horseshoes.
	*/

	glColor3fv(model->horseshoe_color);

	for (sign = -1.0; sign <= 1.0; sign += 2.0)
	{
		glPushMatrix();
		glTranslatef(0.0, 0.0, sign * 0.5 * model->horseshoe_separation);
		glRotatef(90.0, 0.0, 0.0, 1.0);
		glob_extrude_regular_polygon(model->horseshoe_num_sides, /* num_sides */
									 model->horseshoe_partial_sides, /* num_sides_shown */
									 model->horseshoe_radius, /* radius */
									 model->horseshoe_thickness, /* thickness */
									 model->horseshoe_height, /* length */
									 1); /* cylinder normals */
		glPopMatrix();
	}

	/*
	** Plates.
	*/

	glColor3fv(model->plate_color);

	glob_vector_assemble(&plate[0], model->plate_left, model->plate_top, 0.0); /* top left */
	glob_vector_assemble(&plate[1], model->plate_left, model->plate_bottom, 0.0); /* bottom left */
	glob_vector_assemble(&plate[2], model->plate_right, model->plate_bottom, 0.0); /* bottom right */
	glob_vector_assemble(&plate[3], model->plate_right, model->plate_top, 0.0); /* top right */

	for (sign = -1.0; sign <= 1.0; sign += 2.0)
	{
		glPushMatrix();
		glTranslatef(sign * model->plate_offset, 0.0, 0.0);
		glRotatef(sign * 90.0, 0.0, 1.0, 0.0);
		glob_extrude_polygon(plate, 4, model->plate_thickness, 0);
		glPopMatrix();
	}
}

static void model_draw_tube(Model *model)
{
	GLfloat vane_outer_radius;
	GLfloat theta;
	GlobVector vane[4];
	GlobVector plate[4];
	int i;
	GLfloat sign;
	
	/*
	** Spider and spider vanes.
	*/

	glColor3fv(model->sec_color);

	glPushMatrix();
	glTranslatef(0.0, 0.0, -(model->tube_top_to_center - model->sec_to_tube_top));
	glob_extrude_regular_polygon(model->sec_num_sides, /* num_sides */
								 model->sec_num_sides, /* num_sides_shown */
								 model->sec_radius, /* radius */
								 model->sec_radius, /* thickness */
								 model->sec_height, /* length */
								 1); /* cylinder normals */

	vane_outer_radius = model->tube_radius - model->tube_thickness;

	glob_vector_assemble(&vane[0], -0.5 * model->vane_outer_height, vane_outer_radius, 0.0);
	glob_vector_assemble(&vane[1], -0.5 * model->vane_inner_height, model->sec_radius, 0.0);
	glob_vector_assemble(&vane[2], 0.5 * model->vane_inner_height, model->sec_radius, 0.0);
	glob_vector_assemble(&vane[3], 0.5 * model->vane_outer_height, vane_outer_radius, 0.0);

	theta = 360.0 / (GLfloat) model->num_vanes;
	
	glColor3fv(model->vane_color);

	for (i = 0; i < model->num_vanes; i++)
	{
		glPushMatrix();
		glRotatef(i * theta, 0.0, 0.0, 1.0);
		glRotatef(90.0, 0.0, 1.0, 0.0);
		glob_extrude_polygon(vane, 4, model->vane_thickness, 0);
		glPopMatrix();
	}

	glPopMatrix();

	/*
	** 	Focuser.
	*/

	glColor3fv(model->focuser_color);
	glPushMatrix();
	glTranslatef(0.0, 0.0, -(model->tube_top_to_center
							 - model->sec_to_tube_top
							 - 0.5 * model->sec_height
							 + model->focuser_radius));
	glRotatef(model->focuser_angle, 0.0, 0.0, 1.0);
	glTranslatef(model->tube_radius
				 + 0.5 * model->focuser_height
				 - 0.5 * model->tube_thickness, 0.0, 0.0);
	glRotatef(90.0, 0.0, 1.0, 0.0);
	glob_extrude_regular_polygon(model->focuser_num_sides, /* num_sides */
								 model->focuser_num_sides, /* num_sides_shown */
								 model->focuser_radius, /* radius */
								 model->focuser_thickness, /* thickness */
								 model->focuser_height, /* length */
								 1); /* cylinder normals */
	glPopMatrix();

	/*
	** Tube. 
	*/

	glColor3fv(model->tube_color);
	glPushMatrix();
	glTranslatef(0.0, 0.0, 0.5 * model->tube_height - model->tube_top_to_center);
	glob_extrude_regular_polygon(model->tube_num_sides, /* num_sides */
								 model->tube_num_sides, /* num_sides_shown */
								 model->tube_radius, /* radius */
								 model->tube_thickness, /* thickness */
								 model->tube_height, /* length */
								 1); /* cylinder normals */
	glPopMatrix();

	/*
	** Mirror.
	*/

	glColor3fv(model->mirror_color);
	glPushMatrix();
	glTranslatef(0.0, 0.0, (model->tube_height
							- model->tube_top_to_center
							- 0.5 * model->mirror_height));
	glob_extrude_regular_polygon(model->mirror_num_sides, /* num_sides */
								 model->mirror_num_sides, /* num_sides_shown */
								 model->mirror_radius, /* radius */
								 model->mirror_radius, /* thickness */
								 model->mirror_height, /* length */
								 1); /* cylinder normals */
	glPopMatrix();

	/*
	** Tube holder.
	*/

	glColor3fv(model->holder_color);
	
	for (sign = -1.0; sign <= 1.0; sign += 2.0)
	{
		glPushMatrix();
		glTranslatef(0.0, 0.0, sign * model->holder_offset);
		glob_extrude_regular_polygon(model->holder_inner_num_sides, /* num_sides */
									 model->holder_inner_num_sides, /* num_sides_shown */
									 model->plate_offset, /* radius */
									 model->plate_offset - model->tube_radius, /* thickness */
									 model->holder_height, /* length */
									 1); /* cylinder normals */
		glRotatef(180.0 / (GLfloat) model->holder_outer_num_sides, 0.0, 0.0, 1.0);
		glob_extrude_regular_polygon(model->holder_outer_num_sides, /* num_sides */
									 model->holder_outer_num_sides, /* num_sides_shown */
									 model->holder_radius, /* radius */
									 model->holder_thickness, /* thickness */
									 model->holder_height, /* length */
									 0); /* cylinder normals */
		glPopMatrix();
	}

	/*
	** Inner plates.
	*/

	glob_vector_assemble(&plate[0], model->iplate_left, model->iplate_top, 0.0); /* top left */
	glob_vector_assemble(&plate[1], model->iplate_left, model->iplate_bottom, 0.0); /* bottom left */
	glob_vector_assemble(&plate[2], model->iplate_right, model->iplate_bottom, 0.0); /* bottom right */
	glob_vector_assemble(&plate[3], model->iplate_right, model->iplate_top, 0.0); /* top right */

	for (sign = -1.0; sign <= 1.0; sign += 2.0)
	{
		glPushMatrix();
		glTranslatef(sign * model->iplate_offset, 0.0, 0.0);
		glRotatef(sign * 90.0, 0.0, 1.0, 0.0);
		glob_extrude_polygon(plate, 4, model->iplate_thickness, 0);
		glPopMatrix();
	}
}
