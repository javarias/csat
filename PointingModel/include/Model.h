#ifndef _MODEL_H_
#define _MODEL_H_

#include <stdio.h>
#include <math.h>

#define PI (3.141592653589793)

double IHh(double h, double dec, double lat);
double IHd(double h, double dec, double lat);
double IDh(double h, double dec, double lat);
double IDd(double h, double dec, double lat);
double CHh(double h, double dec, double lat);
double CHd(double h, double dec, double lat);
double NPh(double h, double dec, double lat);
double NPd(double h, double dec, double lat);
double MAh(double h, double dec, double lat);
double MAd(double h, double dec, double lat);
double MEh(double h, double dec, double lat);
double MEd(double h, double dec, double lat);
double TFh(double h, double dec, double lat);
double TFd(double h, double dec, double lat);
double FOh(double h, double dec, double lat);
double FOd(double h, double dec, double lat);
double DAFh(double h, double dec, double lat);
double DAFd(double h, double dec, double lat);

double IAe(double alt, double az, double lat);
double IAa(double alt, double az, double lat);
double IEe(double alt, double az, double lat);
double IEa(double alt, double az, double lat);
double NPAEe(double alt, double az, double lat);
double NPAEa(double alt, double az, double lat);
double CAe(double alt, double az, double lat);
double CAa(double alt, double az, double lat);
double ANe(double alt, double az, double lat);
double ANa(double alt, double az, double lat);
double AWe(double alt, double az, double lat);
double AWa(double alt, double az, double lat);
double TFe(double alt, double az, double lat);
double TFa(double alt, double az, double lat);

double Ylm_eq(int l, int m, double h, double dec, double lat);
double Ylm_aa(int l, int m, double alt, double az, double lat);
double hzero_eq(int l, int m, double h, double dec, double lat);
double hzero_aa(int l, int m, double alt, double az, double lat);

int fact(int num);
int fact2(int num);

#endif
