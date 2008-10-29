#include "Pointing.h"

ObsList *oList;
bool start = false;
bool Calibrating = false;

void init(int mount)
{
	if(!start)
	{
		oList = new ObsList(mount,19+49/60.+33/3600.);
		start = true;
	}
}

void calibrate(bool reset)
{
	if(start)
	{
		Calibrating = true;
		if(reset)
			oList->resetObsList();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
		//oList->addHarmonicTerm();
	}
}

void endCalibrate()
{
	if(start)
		Calibrating = false;
}

void addObs(double raT, double decT, double raE, double decE, double st)
{
	printf("A\n");
	if(start)
		oList->add(raT,decT,raE,decE, st);
	printf("B\n");
}

void calculateCoeffs()
{
	oList->cCoeffs();
}

void calculateOffsets()
{
	oList->cOffs();
}

void calculateOffset(double inra, double indec, double st)
{
	double rad, decd;
	double &ra = (double &) rad;
	double &dec = (double &) decd;;
	oList->cOff(inra,indec,st, ra, dec);
}

int getNumCoeffs()
{
	if(start)
		return oList->getNumTerms();
	return 0;
}

void reset()
{
	if(start)
		oList->resetAll();
}

void end()
{
	if(start)
		delete oList;
	start = false;
}
