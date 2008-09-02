#include "Model.h"


//Default Equatorial Terms
//
double IHh(double h, double dec, double lat)
{
	return 1.0;
}

double IHd(double h, double dec, double lat)
{
	return 0.0;
}

double IDh(double h, double dec, double lat)
{
	return 0.0;
}

double IDd(double h, double dec, double lat)
{
	return 1.0;
}

double CHh(double h, double dec, double lat)
{
	return 1/cos(dec);
}

double CHd(double h, double dec, double lat)
{
	return 0.0;
}

double NPh(double h, double dec, double lat)
{
	return tan(dec);
}

double NPd(double h, double dec, double lat)
{
	return 0.0;
}

double MAh(double h, double dec, double lat)
{
	return -cos(h)*tan(dec);
}

double MAd(double h, double dec, double lat)
{
	return sin(h);
}

double MEh(double h, double dec, double lat)
{
	return sin(h)*tan(dec);
}

double MEd(double h, double dec, double lat)
{
	return cos(h);
}

double TFh(double h, double dec, double lat)
{
	return cos(lat)*sin(h)/cos(dec);
}

double TFd(double h, double dec, double lat)
{
	return cos(lat)*cos(h)*sin(dec)-sin(lat)*cos(dec);
}

double FOh(double h, double dec, double lat)
{
	return 0.0;
}

double FOd(double h, double dec, double lat)
{
	return cos(h);
}

double DAFh(double h, double dec, double lat)
{
	return -(cos(lat)*cos(h)+sin(lat)*tan(dec));
}

double DAFd(double h, double dec, double lat)
{
	return 0.0;
}

//Default Horizontal Terms
//
/*
double IAe(double alt, double az, double lat)
{
	return 0.0;
}
double IAa(double alt, double az, double lat)
{
	return 1.0;
}
double IEe(double alt, double az, double lat)
{
	return 1.0;
}
double IEa(double alt, double az, double lat)
{
	return 0.0;
}
double NPAEe(double alt, double az, double lat)
{
	return 0.0;
}
double NPAEa(double alt, double az, double lat)
{
	return tan(alt);
}
double CAe(double alt, double az, double lat)
{
	return sin(az);
}
double CAa(double alt, double az, double lat)
{
	return cos(az)*tan(alt);
}
double ANe(double alt, double az, double lat)
{
	return cos(az);
}
double ANa(double alt, double az, double lat)
{
	return sin(az)*tan(alt);
}
double AWe(double alt, double az, double lat)
{
	return 0.0;
}
double AWa(double alt, double az, double lat)
{
	return 1/cos(alt);
}
double TFe(double alt, double az, double lat)
{
	return cos(alt);
}
double TFa(double alt, double az, double lat)
{
	return 0.0;
}
*/

double IAe(double alt, double az, double lat)
{
	return 0.0;
}
double IAa(double alt, double az, double lat)
{
	return cos(alt);
}
double IEe(double alt, double az, double lat)
{
	return -1.0;
}
double IEa(double alt, double az, double lat)
{
	return 0.0;
}
double NPAEe(double alt, double az, double lat)
{
	return 0.0;
}
double NPAEa(double alt, double az, double lat)
{
	return sin(alt);
}
double CAe(double alt, double az, double lat)
{
	return 0.0;
}
double CAa(double alt, double az, double lat)
{
	return -1.0;
}
double ANe(double alt, double az, double lat)
{
	return cos(az);
}
double ANa(double alt, double az, double lat)
{
	return -sin(az)*sin(alt);
}
double AWe(double alt, double az, double lat)
{
	return -sin(az);
}
double AWa(double alt, double az, double lat)
{
	return -cos(az)*sin(alt);
}
double TFe(double alt, double az, double lat)
{
	return cos(alt);
}
double TFa(double alt, double az, double lat)
{
	return 0.0;
}

double Ylm_eq(int l, int m, double h, double dec, double lat)
{
	double Nlm;
	double Plm, tmp1, tmp2;
	double phi,cosAz;
	double res;
	int absm;
	int i;
	//printf("AA\n\n");
	if(m<0)
		absm = -m;
	else
		absm = m;
	Nlm = sqrt((2*l+1)/(4*PI)*fact(l-absm)/((double)fact(l+absm)));
	phi = asin(sin(lat)*sin(dec)+cos(dec)*cos(lat)*cos(h));
	cosAz = cos(atan2(sin(h),cos(h)*sin(lat)-tan(dec)*cos(lat)));
	phi += PI/2.;
	cosAz = cos(h+PI);
	phi = dec+PI/2.;
	//Plm = pow(-1,absm)*fact(fact((2*absm - 1)))*pow(1-pow(cosAz,2),absm/2.);
	Plm = pow(-1,absm)*fact2(2*absm - 1)*pow(1-pow(cosAz,2),absm/2.);
	//printf("%d \n", fact2(2*absm - 1));
	if(l>absm)
	{
		tmp1 = Plm;
		Plm = cosAz*(2*absm + 1)*tmp1;
		i = 1;
		while(l>absm+i)
		{
			tmp2 = Plm;
			Plm = (cosAz*(2*(absm+i+1)-1)*tmp2 - ((absm+i+1)+absm-1)*tmp1)/((double)((absm+i+1)-absm));
			tmp1 = tmp2;
			i++;
		}
	}
	
	if(m > 0)
		res = sqrt(2)*Nlm*cos(m*phi)*Plm;
	else if (m == 0)
		res = Nlm*Plm;
	else
		res = sqrt(2)*Nlm*sin(absm*phi)*Plm;
	//printf("%lf\n", res);
	return res;
}

double Ylm_aa(int l, int m, double alt, double az, double lat)
{
	return Ylm_eq(l,m,alt,az,lat);
}

double hzero_eq(int l, int m, double h, double dec, double lat)
{
	return 0.0;
}

double hzero_aa(int l, int m, double alt, double az, double lat)
{
	return 0.0;
}

int fact(int num)
{
	if(num <= 1)
		return 1;
	return num*fact(num - 1);
}

int fact2(int num)
{
	int m;
	int res;
	if(num < 2)
		return 1;
	else
		return num*fact2(num-2);
	
}
