/*
** coord.h
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

#ifndef COORD_H
#define COORD_H

#ifndef PI
#define PI 3.1415926535897932384626433832795028841971693993751058209749445923
#endif

#define DEG_TO_RAD(x) ((x) * (PI) / 180.0)
#define RAD_TO_DEG(x) ((x) * 180.0 / (PI))

double coord_wrap(double x);
void coord_a3_to_eq(double *ha, double *dec, double a1, double a2, double a3, double lat);
void coord_a3_to_altaz(double *az, double *alt, double a1, double a2, double a3, double lat);
void coord_camera_altaz_to_xyz(double *x, double *y, double *z, double caz, double calt);
void coord_a3_to_frot(double *frot, double a1, double a2, double a3, double lat);
void coord_eq_to_altaz(double *az, double *alt, double ha, double dec, double lat);
void coord_altaz_to_eq(double *ha, double *dec, double az, double alt, double lat);
void coord_altaz_to_a3(double *a1, double *a2, double *a3,
					   double az, double alt, double lat, double frot);
void coord_eq_to_a3(double *a1, double *a2, double *a3,
					double ha, double dec, double lat, double frot);

#endif /* COORD_H */
