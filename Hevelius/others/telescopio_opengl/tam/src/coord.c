/*
** coord.c
**
** Astronomical and telescope coordinate transformations.
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

#include "coord.h"

typedef struct {
	double x;
	double y;
	double z;
} CoordVector;

static void coord_vector_scale(CoordVector *w, CoordVector *v, double s);
static void coord_vector_add(CoordVector *w, CoordVector *v1, CoordVector *v2);
static void coord_vector_subtract(CoordVector *w, CoordVector *v1, CoordVector *v2);
static void coord_vector_cross_product(CoordVector *w, CoordVector *v1, CoordVector *v2);
static double coord_vector_dot_product(CoordVector *v1, CoordVector *v2);
static void coord_vector_normalize(CoordVector *w, CoordVector *v);

/*
** coord_vector_scale
**
** This function computes the result of scaling the input vector v, by the
** scalar factor, s.  The result is returned in the vector w.  The
** operation may be performed in place (i.e., w and v may point to the
** same vector).
*/

static void coord_vector_scale(CoordVector *w, CoordVector *v, double s)
{
	w->x = s * v->x;
	w->y = s * v->y;
	w->z = s * v->z;
}

/*
** coord_vector_add
**
** This function adds the two input vectors v1 and v2, and returns the
** result in the vector w.
*/

static void coord_vector_add(CoordVector *w, CoordVector *v1, CoordVector *v2)
{
	w->x = v1->x + v2->x;
	w->y = v1->y + v2->y;
	w->z = v1->z + v2->z;
}

/*
** coord_vector_subtract
**
** This function subtracts the vector v2 from the vector v1 and returns the
** result in the vector w.
*/

static void coord_vector_subtract(CoordVector *w, CoordVector *v1, CoordVector *v2)
{
	w->x = v1->x - v2->x;
	w->y = v1->y - v2->y;
	w->z = v1->z - v2->z;
}

/*
** coord_vector_cross_product
**
** This function computes the cross product of vectors v1 and v2, and
** returns the result in the vector w (i.e., w = v1 x v2).
*/

static void coord_vector_cross_product(CoordVector *w, CoordVector *v1, CoordVector *v2)
{
	w->x = v1->y * v2->z - v1->z * v2->y;
	w->y = v1->z * v2->x - v1->x * v2->z;
	w->z = v1->x * v2->y - v1->y * v2->x;
}

/*
** coord_vector_dot_product
**
** This function returns the dot product of the two vectors v1 and v2.
*/

static double coord_vector_dot_product(CoordVector *v1, CoordVector *v2)
{
	return v1->x * v2->x + v1->y * v2->y + v1->z * v2->z;
}

/*
** coord_vector_normalize
**
** This function returns a normalized version of vector v in vector w.  The
** operation may be done in place (i.e., w and v may point to the same
** vector). 
*/

static void coord_vector_normalize(CoordVector *w, CoordVector *v)
{
	double length;

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
** coord_wrap
**
** Return wrapped version of input so that  0 <= x > 2 PI.
*/

double coord_wrap(double x)
{
	while (x >= (2.0 * PI))
	{
		x -= 2.0 * PI;
	}

	while (x < 0.0)
	{
		x += 2.0 * PI;
	}

	return x;
}

/*
** coord_a3_to_eq
**
** Convert alt-alt-az coordinates to local equatorial coordinates.
*/

void coord_a3_to_eq(double *ha, double *dec, double a1, double a2, double a3, double lat)
{
	*ha = atan2(-sin(a3) * cos(a1) + cos(a3) * sin(a2) * sin(a1),
				(-sin(lat) * cos(a3) * cos(a1)
				 - sin(lat) * sin(a3) * sin(a2) * sin(a1)
				 + cos(lat) * cos(a2) * sin(a1)));
	*dec = asin(cos(lat) * cos(a3) * cos(a1)
				+ cos(lat) * sin(a3) * sin(a2) * sin(a1)
				+ sin(lat) * cos(a2) * sin(a1));
}

/*
** coord_a3_to_altaz
**
** Convert alt-alt-az coordinates to altazimuth coordinates.
*/

void coord_a3_to_altaz(double *az, double *alt, double a1, double a2, double a3, double lat)
{
	*az = coord_wrap(atan2(sin(a3) * cos(a1) - cos(a3) * sin(a2) * sin(a1),
						   cos(a3) * cos(a1) + sin(a3) * sin(a2) * sin(a1)));
	*alt = asin(cos(a2) * sin(a1));
}

/*
** coord_camera_altaz_to_xyz
**
** Convert camera altitude and azimuth to screen cartesian coordinates.
*/

void coord_camera_altaz_to_xyz(double *x, double *y, double *z, double caz, double calt)
{
	*x = 23.0 * cos(calt) * sin(caz);
	*y = 23.0 * sin(calt);
	*z = -23.0 * cos(calt) * cos(caz);
}

/*
** coord_a3_to_frot
*/

void coord_a3_to_frot(double *frot, double a1, double a2, double a3, double lat)
{
	CoordVector f_aaa;
	double ha, dec;
	CoordVector f_eq;
	CoordVector r;
	CoordVector p;
	
	/*
	** Compute field rotation vector for alt-alt-az mount.
	*/
	
	f_aaa.x = cos(a3) * sin(a1) - sin(a3) * sin(a2) * cos(a1);
	f_aaa.y = -sin(a3) * sin(a1) - cos(a3) * sin(a2) * cos(a1);
	f_aaa.z = cos(a2) * cos(a1);

	/*
	** Compute local equatorial coordinates.
	*/
	
 	coord_a3_to_eq(&ha, &dec, a1, a2, a3, lat);

	/*
	** Compute field rotation vector for equatorial mount.
	*/

	f_eq.x = -sin(lat) * cos(ha) * sin(dec) - cos(lat) * cos(dec);
	f_eq.y = sin(ha) * sin(dec);
	f_eq.z = -cos(lat) * cos(ha) * sin(dec) + sin(lat) * cos(dec);

	/*
	** Compute angle between the two field rotation vectors.
	*/
	
	*frot = acos(coord_vector_dot_product(&f_aaa, &f_eq));

	/*
	** This gives us 0 <= frot <= pi.  In order to determine the full
	** angle, we see if f_eq x f_aaa is parallel or antiparallel to the
	** direction vector r.
	*/

	r.x = -cos(a3) * cos(a1) - sin(a3) * sin(a2) * sin(a1);
	r.y = sin(a3) * cos(a1) - cos(a3) * sin(a2) * sin(a1);
	r.z = cos(a2) * sin(a1);
	
	coord_vector_cross_product(&p, &f_eq, &f_aaa); 

	if (coord_vector_dot_product(&p, &r) < 0.0) *frot = 2.0 * PI - *frot;
}

/*
** coord_eq_to_altaz
**
** Convert local equatorial coordinates to altazimuth coordinates.
*/

void coord_eq_to_altaz(double *az, double *alt, double ha, double dec, double lat)
{
	*az = coord_wrap(atan2(-cos(dec) * sin(ha),
						   sin(dec) * cos(lat) - cos(dec) * cos(ha) * sin(lat)));
	*alt = asin(cos(dec) * cos(ha) * cos(lat) + sin(dec) * sin(lat));
}

/*
** coord_altaz_to_eq
**
** Convert altazimuth coordinates to local equatorial coordinates.
*/

void coord_altaz_to_eq(double *ha, double *dec, double az, double alt, double lat)
{
	*ha = atan2(-cos(alt) * sin(az),
				sin(alt) * cos(lat) - cos(alt) * cos(az) * sin(lat));
	*dec = asin(cos(alt) * cos(az) * cos(lat) + sin(alt) * sin(lat));
}

/*
** coord_altaz_to_a3
**
** Convert altazimuth coordinates to alt-alt-az coordinates.
*/

void coord_altaz_to_a3(double *a1, double *a2, double *a3,
					   double az, double alt, double lat, double frot)
{
	double ha, dec;

	coord_altaz_to_eq(&ha, &dec, az, alt, lat);
	coord_eq_to_a3(a1, a2, a3, ha, dec, lat, frot);

}

/*
** coord_eq_to_a3
**
** Convert local equatorial coordinates to alt-alt-az coordinates.
*/

void coord_eq_to_a3(double *a1, double *a2, double *a3,
					double ha, double dec, double lat, double frot)
{
	double fz;
	double vx, vy, vz;

	/*
	** Compute needed components of r, f, and v vectors of alt-alt-az scope
	** by using equations for r, f, and v vectors of equatorial scope that
	** has been pre-rotated by the field rotation.
	*/

	fz = -cos(lat) * sin(ha) * sin(frot) - cos(lat) * cos(ha) * sin(dec) * cos(frot) + sin(lat) * cos(dec) * cos(frot);
	vx = -sin(lat) * sin(ha) * cos(frot) + sin(lat) * cos(ha) * sin(dec) * sin(frot) + cos(lat) * cos(dec) * sin(frot);
	vy = -cos(ha) * cos(frot) - sin(ha) * sin(dec) * sin(frot);
	vz = -cos(lat) * sin(ha) * cos(frot) + cos(lat) * cos(ha) * sin(dec) * sin(frot) - sin(lat) * cos(dec) * sin(frot);

	*a2 = asin(vz);
	*a1 = acos(fz / cos(*a2));
	*a3 = coord_wrap(atan2(vx / cos(*a2), vy / cos(*a2)));
}
