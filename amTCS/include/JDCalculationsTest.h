#ifndef _JDCALCULATIONS_TEST_H
#define _JDCALCULATIONS_TEST_H

#include <maciSimpleClient.h>
#include <acsutil.h>
#include <cppunit/TestFixture.h>
#include <cppunit/extensions/HelperMacros.h>

#include <CalculationsC.h>

using namespace maci;


class JDCalculationsTest: public CppUnit::TestFixture{

	CPPUNIT_TEST_SUITE( JDCalculationsTest );
	CPPUNIT_TEST( testJulian );
	CPPUNIT_TEST( testGregorian );
	CPPUNIT_TEST( testExceptions );
	CPPUNIT_TEST_SUITE_END( );

	public:
	JDCalculationsTest();

	protected:
	void testJulian();
	void testGregorian();
	void testExceptions();

	//private:
	//CALCULATIONS_MODULE::Calculations_var calc_comp;
};

#endif
