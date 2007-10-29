#include <maciSimpleClient.h>
#include <acsutil.h>

#include "CalculationsC.h"
#include "PrecessionTest.h"

using namespace maci;

extern CALCULATIONS_MODULE::Calculations_var calc_comp;

void PrecessionTest::testPrecessionHR() {
	TYPES::RadecPos posInitial;
	TYPES::RadecPos posFinal;

	/* This is because of precission issues */
	char finalRa[11]  = "41.547214";
	char finalDec[11] = "49.348483";
	char returnedRa[11];
	char returnedDec[11];

	posInitial.ra  = 41.054063;
	posInitial.dec = 49.227750;

	posFinal = calc_comp->precessionHR(posInitial,2451545.0,2462088.69);
	sprintf(returnedRa, "%.6f",posFinal.ra);
	sprintf(returnedDec,"%.6f",posFinal.dec);

	CPPUNIT_ASSERT( !strcmp(finalRa ,returnedRa)  );
	CPPUNIT_ASSERT( !strcmp(finalDec,returnedDec) );
}

