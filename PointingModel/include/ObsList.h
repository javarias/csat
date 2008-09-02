#ifndef _OBSLIST_H_
#define _OBSLIST_H_

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "Term.h"
#include "Obs.h"

class ObsList
{
	public:
		ObsList(int mount, double lat);
		~ObsList();
		void add(double raT,double decT,double raE,double decE, double st);
		void createMatrix();
		void createVector();
		void resetObsList();
		void resetAll();
		void initDefaultTerms();
		void addHarmonicTerm();
		void selectedTerms();
		double *getMatrix();
		double *getVector();
		double Off1(int i,double a1,double a2,double lat);
		double Off2(int i,double a1,double a2,double lat);
		int getNumObs();
		int getNumTerms();
		void cCoeffs();
		void cOff(double ra, double dec, double st);
		void cOffs();
	private:
		Obs **obs;
		double *mat, *vec;
		double *coeffs;
		double offsets[2];
		int mount;
		double lat;
		int n; //Number of observations.
		Term *defaultTerms[9];
		Term **harmonicTerms;
		Term **customTerms;
		Term **inuseTerms;
		int dn; //Default Terms.
		int hn; //Harmonic Terms.
		int cn; //Custom Terms.
		int nterms; //Number of terms used.
};

#endif
