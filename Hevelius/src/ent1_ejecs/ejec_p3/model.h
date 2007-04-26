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

#ifndef MODEL_H
#define MODEL_H

#include <GL/gl.h>

enum {
	MODEL_DISPLAY_LIST_BASE,
	MODEL_DISPLAY_LIST_PLATFORM,
	MODEL_DISPLAY_LIST_HORSESHOE,
	MODEL_DISPLAY_LIST_TUBE,
	MODEL_NUM_DISPLAY_LIST
};

typedef struct {
	GLfloat eps;
	
	GLfloat tube_height;
	GLfloat tube_radius;
	GLfloat tube_thickness;
	GLint tube_num_sides;
	GLfloat tube_top_to_center;
	GLfloat tube_color[3];

	GLfloat mirror_height;
	GLfloat mirror_radius;
	GLint mirror_num_sides;
	GLfloat mirror_color[3];

	GLfloat sec_to_tube_top;
	GLfloat sec_height;
	GLfloat sec_radius;
	GLint sec_num_sides;
	GLint num_vanes;
	GLfloat vane_inner_height;
	GLfloat vane_outer_height;
	GLfloat vane_thickness;
	GLfloat sec_color[3];
	GLfloat vane_color[3];

	GLfloat focuser_height;
	GLfloat focuser_radius;
	GLfloat focuser_thickness;
	GLint focuser_num_sides;
	GLfloat focuser_angle;
	GLfloat focuser_color[3];

	/*
	** Horseshoe.
	*/
	
	GLint horseshoe_num_sides;
	GLint horseshoe_partial_sides;
	GLfloat horseshoe_radius;
	GLfloat horseshoe_thickness;
	GLfloat horseshoe_height;
	GLfloat horseshoe_separation;
	GLfloat horseshoe_color[3];

	/*
	** Platform.
	*/

	GLint platform_num_sides;
	GLfloat platform_radius;
	GLfloat platform_height;
	GLfloat platform_color[3];
	GLfloat platform_side_facet_length;
		
	GLint rocker_num_sides;
	GLint rocker_partial_sides;
	GLfloat rocker_thickness;
	GLfloat rocker_radius;
	GLfloat rocker_height;
	GLfloat rocker_color[3];

	GLfloat plate_thickness;
	GLfloat plate_right;
	GLfloat plate_left;
	GLfloat plate_top;
	GLfloat plate_bottom;
	GLfloat plate_offset;
	GLfloat plate_color[3];

	GLint holder_outer_num_sides;
	GLint holder_inner_num_sides;
	GLfloat holder_radius;
	GLfloat holder_thickness;
	GLfloat holder_height;
	GLfloat holder_offset;
	GLfloat iplate_thickness;
	GLfloat iplate_right;
	GLfloat iplate_left;
	GLfloat iplate_top;
	GLfloat iplate_bottom;
	GLfloat iplate_offset;
	GLfloat holder_color[3];

	/*
	** Base.
	*/

	GLint base_num_sides;
	GLfloat base_radius;
	GLfloat base_height;
	GLfloat base_color[3];

	GLint num_pads;
	GLint pad_num_sides;
	GLfloat pad_radius;
	GLfloat pad_height;
	GLfloat pad_color[3];

	/*
	** Display list.
	*/

	GLuint display_list[MODEL_NUM_DISPLAY_LIST];
	
} Model;

Model* model_init(void);
void model_draw(Model *model, GLfloat a1, GLfloat a2, GLfloat a3);

#endif /* MODEL_H */
