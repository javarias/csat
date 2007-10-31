#include <maciSimpleClient.h>
#include <acsutil.h>
#include <math.h>

#include "CalculationsC.h"
#include "PrecessionTest.h"

using namespace maci;

extern CALCULATIONS_MODULE::Calculations_var calc_comp;

/* Taken from Astronmical Algorithms, page 135/136 */
void PrecessionTest::testPrecessionHR() {
	TYPES::RadecPos posInitial, posFinal;

	posInitial.ra  = 41.054063; posInitial.dec = 49.227750;
	posFinal = calc_comp->precessionHR(posInitial,2451545.0,2462088.69);
	CPPUNIT_ASSERT( fabs(posFinal.ra  - 41.547214F) < 0.00001 );
	CPPUNIT_ASSERT( fabs(posFinal.dec - 49.348483F) < 0.00001 );

	posInitial.ra  = 37.952937 + 0.19877*100*15/3600.; posInitial.dec = 89.264089 - 0.0152*100/3600.;
	posFinal = calc_comp->precessionHR(posInitial,2451545.0,2451545.0+100*365.25);
	CPPUNIT_ASSERT( fabs(posFinal.ra  - 88.371542F) < 0.00001 );
	CPPUNIT_ASSERT( fabs(posFinal.dec - 89.539496F) < 0.00001 );

	posInitial.ra  = 37.952937 + 0.19877*50*15/3600.;  posInitial.dec = 89.264089 - 0.0152*50/3600.;
	posFinal = calc_comp->precessionHR(posInitial,2451545.0,2451545.0+50*365.25);
	CPPUNIT_ASSERT( fabs(posFinal.ra  - 57.068458F) < 0.00001 );
	CPPUNIT_ASSERT( fabs(posFinal.dec - 89.454272F) < 0.00001 );

	// TODO: Write this test with a previous date
	//posInitial.ra  = 37.952937 + 0.19877*(-99)*15/3600.; posInitial.dec = 89.264089 - 0.0152*(-99)/3600.;
	//posFinal = calc_comp->precessionHR(posInitial,2451545.0,2415020.3135);
	//printf("\n%.6f\n",posFinal.ra);
	//printf("%.6f\n",posFinal.dec);
	//CPPUNIT_ASSERT( fabs(posFinal.ra  - 20.641250F) < 0.00001 );
	//CPPUNIT_ASSERT( fabs(posFinal.dec - 88.773939F) < 0.00001 );
}
