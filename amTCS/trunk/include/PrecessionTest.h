#ifndef _PRECESSION_TEST_H
#define _PRECESSION_TEST_H

#include <maciSimpleClient.h>
#include <acsutil.h>
#include <cppunit/TestFixture.h>
#include <cppunit/extensions/HelperMacros.h>

#include <CalculationsC.h>

using namespace maci;


class PrecessionTest: public CppUnit::TestFixture{

	CPPUNIT_TEST_SUITE( PrecessionTest );
	CPPUNIT_TEST( testPrecessionHR );
	CPPUNIT_TEST_SUITE_END( );

	protected:
	void testPrecessionHR();

};

#endif
