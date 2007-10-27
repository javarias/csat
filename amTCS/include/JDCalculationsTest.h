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
	CPPUNIT_TEST( testGregorian );
	CPPUNIT_TEST( testJulian );
	CPPUNIT_TEST_SUITE_END();

	public:
	JDCalculationsTest();
	void setUp();
	void tearDown();

	protected:
	void testGregorian();
	void testJulian();

	private:
	SimpleClient client;
	CALCULATIONS_MODULE::Calculations_var calc_comp;
	static char *comp_name;
};

#endif
