#ifndef _POINTING_H_
#define _POINTING_H_

#include "ObsList.h"

void init(int mount);
void calibrate(bool reset);
void endCalibrate();
void addObs(double raT, double decT, double raE, double decE, double st);
void calculateCoeffs();
void calculateOffsets();
void calculateOffset(double ra, double dec, double st);
int getNumCoeffs();
void reset();
void end();

#endif
