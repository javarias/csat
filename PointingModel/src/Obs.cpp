#include "Obs.h"

Obs::Obs(double raT, double decT, double raE, double decE, double st, double lat)
{
	bool da = true;
	double thE, thT;
	double taltT, tazmT, taltE, tazmE;
	double traT, tdecT, traE, tdecE;
	double r;
	//this->raT = raT;
	//this->decT = decT;
	//this->raE = raE;
	//this->decE = decE;
	this->state = true;
	this->st = st;
	this->lat = lat;
	lat = lat*PI/180.;

	thT = (st - raT)*PI/180.;
	thE = (st - raE)*PI/180.;
	if(thT < 0)
		thT += 2*PI;
	if(thE < 0)
		thE += 2*PI;
	tdecT = decT*PI/180.;
	tdecE = decE*PI/180.;
	traT = raT;
	traE = raE;

	//Diurnal Aberration::Just for testing..
	if(da)
	{
		printf("Ra: %lf Dec: %lf\n",traT, tdecT*180/PI);
		r = (21.4*pow(sin(lat),2)+6378.140)/6378.140;
		r = 1.0;
		traT = traT + 0.320/3600.*r*cos(lat)*cos(thT)/cos(tdecT);
		tdecT = tdecT + 0.320/3600.*r*cos(lat)*sin(thT)*sin(tdecT)*PI/180.;
		printf("dR: %lf dD: %lf\n",0.320*r*cos(lat)*cos(thT)/cos(tdecT), 0.320*r*cos(lat)*sin(thT)*sin(tdecT));
		thT = (st - traT)*PI/180.;
		if(thT < 0)
			thT += 2*PI;
		if(thE < 0)
			thE += 2*PI;
		printf("Ra: %lf Dec: %lf H: %lf\n",traT, tdecT*180/PI,thT*180/PI);
		printf("Ra: %lf Dec: %lf\n\n",traE, tdecE*180/PI);
	}
/////
	this->raT = traT;
	this->decT = tdecT*180/PI;
	this->raE = traE;
	this->decE = tdecE*180/PI;
	this->hT = thT*180/PI;
	this->hE = thE*180/PI;

	this->altT = asin(sin(lat)*sin(tdecT)+cos(tdecT)*cos(lat)*cos(thT));
	this->azmT = atan2(sin(thT),cos(thT)*sin(lat)-tan(tdecT)*cos(lat));
	this->altT *= 180/PI;
	this->azmT *= 180/PI;
	while(this->altT < 0)
		this->altT += 360;
	while(this->azmT < 0)
		this->azmT += 360;
	this->altE = asin(sin(lat)*sin(tdecE)+cos(tdecE)*cos(lat)*cos(thE));
	this->azmE = atan2(sin(thE),cos(thE)*sin(lat)-tan(tdecE)*cos(lat));
	this->altE *= 180/PI;
	this->azmE *= 180/PI;
	while(this->altE < 0)
		this->altE += 360;
	while(this->azmE < 0)
		this->azmE += 360;
	this->Z = 90 - this->altT;
	this->P = atan2(sin(this->hT*PI/180.),cos(this->decT*PI/180.)*tan(lat)-sin(this->decT*PI/180.)*cos(this->hT*PI/180.))*180/PI;
	this->dR = this->raT - this->raE;
	this->dH = this->hT - this->hE;
	this->dD = this->decT - this->decE;
	this->dA = this->azmT - this->azmE;
	this->dE = this->altT - this->altE;
	this->dZ = -this->dE;
	this->dP = this->P - atan2(sin(this->hE*PI/180.),cos(this->decE*PI/180.)*tan(lat)-sin(this->decE*PI/180.)*cos(this->hE*PI/180.))*180/PI;
}

Obs::~Obs()
{
}

double Obs::getRaT()
{
	return this->raT;
}
double Obs::getRaE()
{
	return this->raE;
}
double Obs::getDecT()
{
	return this->decT;
}
double Obs::getDecE()
{
	return this->decE;
}
double Obs::getAltT()
{
	return this->altT;
}
double Obs::getAltE()
{
	return this->altE;
}
double Obs::getAzmT()
{
	return this->azmT;
}
double Obs::getAzmE()
{
	return this->azmE;
}
double Obs::gethT()
{
	return this->hT;
}
double Obs::gethE()
{
	return this->hE;
}
double Obs::getSt()
{
	return this->st;
}
double Obs::getLat()
{
	return this->lat;
}

double Obs::getdR()
{
	return -this->dR;
}

double Obs::getdD()
{
	return -this->dD;
}

double Obs::getdE()
{
	return -this->dE;
}

double Obs::getdP()
{
	return -this->dP;
}

double Obs::getdZ()
{
	return -this->dZ;
}

bool Obs::getState(){
	return state;
}

void Obs::setState(bool state){
	this->state = state;
}
