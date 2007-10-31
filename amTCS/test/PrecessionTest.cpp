#include <maciSimpleClient.h>
#include <acsutil.h>
#include <math.h>

#include "CalculationsC.h"
#include "PrecessionTest.h"

using namespace maci;

extern CALCULATIONS_MODULE::Calculations_var calc_comp;

void PrecessionTest::testPrecessionHR() {
	TYPES::RadecPos posInitial, posFinal;

	posInitial.ra  = 41.054063; posInitial.dec = 49.227750;
	posFinal = calc_comp->precessionHR(posInitial,2451545.0,2462088.69);
	CPPUNIT_ASSERT( fabs(posFinal.ra  - 41.547214F) < 0.00001 );
	CPPUNIT_ASSERT( fabs(posFinal.dec - 49.348483F) < 0.00001 );

	// TODO: Fill these tests with the correct data
	// See Astronomical Algorithms, exercise on page 136
	//posInitial.ra  = 37.952937; posInitial.dec = 89.264089;
	//posFinal = calc_comp->precessionHR(posInitial,2451545.0,);
	//CPPUNIT_ASSERT( fabs(posFinal.ra  - 20.641250F) < 0.00001 );
	//CPPUNIT_ASSERT( fabs(posFinal.dec - 88.773939F) < 0.00001 );

	//posInitial.ra  = 37.952937; posInitial.dec = 89.264089;
	//posFinal = calc_comp->precessionHR(posInitial,2451545.0,);
	//CPPUNIT_ASSERT( fabs(posFinal.ra  - 41.547214F) < 0.00001 );
	//CPPUNIT_ASSERT( fabs(posFinal.dec - 49.348483F) < 0.00001 );

	//posInitial.ra  = 37.952937; posInitial.dec = 89.264089;
	//posFinal = calc_comp->precessionHR(posInitial,2451545.0,);
	//CPPUNIT_ASSERT( fabs(posFinal.ra  - 41.547214F) < 0.00001 );
	//CPPUNIT_ASSERT( fabs(posFinal.dec - 49.348483F) < 0.00001 );
}
