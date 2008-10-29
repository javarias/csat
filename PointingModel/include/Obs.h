#ifndef _OBS_H_
#define _OBS_H_

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define PI (3.141592653589793)

class Obs
{
	public:
		Obs(double raT, double decT, double raE, double decE, double st, double lat);
		double getRaT();
		double getRaE();
		double getDecT();
		double getDecE();
		double getAltT();
		double getAltE();
		double getAzmT();
		double getAzmE();
		double gethT();
		double gethE();
		double getSt();
		double getLat();
		double getdR();
		double getdD();
		double getdE();
		double getdP();
		double getdZ();
		bool getState();
		void setState(bool state);
		~Obs();
	private:
		double raE, hE, decE, raT, hT, decT;
		double altE, azmE, altT, azmT;
		double st, lat;
		double Z,P,n;
		double dR, dH, dD, dA, dE;
		double dHDNP,dZ, dAANP, dTR;
		double dU, dL, dP, dS, dN, dW, dV;
		bool state;
};

#endif
