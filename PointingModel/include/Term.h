#ifndef _TERM_H_
#define _TERM_H_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Model.h"

typedef double(*term)(double, double, double);
typedef double(*hterm)(int, int, double, double, double);

class Term
{
	public:
		Term();
		Term(const char *n, term f1, term f2, bool harmonic);
		~Term();
		char *getName();
		void setlm(int l, int m, bool first, int mount);
		double Off1(double a1, double a2, double lat);
		double Off2(double a1, double a2, double lat);
		int getl();
		int getm();
		void setState(bool used);
		bool getState();
		bool getHarmState();
	private:
		char *name; //Name of term.
		term t1; //Hour Angle offset term.
		term t2; //Declination offset term.
		bool harmonic; //Decides whether it is a harmonic function.
		hterm ht1; //Harmonic offset term (Both HA and Dec).
		hterm ht2; //Harmonic offset term (Both HA and Dec).
		int l, m; //Numbers for the harmonic model.
		bool hstate; //Decides if it affects h or dec (true if h).
		bool used; //Decides whether this term is considered or not.
};

#endif
