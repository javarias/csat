#include <maciSimpleClient.h>
#include <acsutil.h>

#include "CalculationsC.h"
#include "csatErrors.h"
#include "JDCalculationsTest.h"

using namespace maci;

extern CALCULATIONS_MODULE::Calculations_var calc_comp;

JDCalculationsTest::JDCalculationsTest(){
//	this->calc_comp = calc_comp;
}

void JDCalculationsTest::testJulian() {
	CPPUNIT_ASSERT( calc_comp->date2JD(837,4,10.3) == (double)2026871.8   );
	CPPUNIT_ASSERT( calc_comp->date2JD(-123,12,31) == (double)1676496.5   );
	CPPUNIT_ASSERT( calc_comp->date2JD(-1000,7,12.5) == (double)1356001.0 );
	CPPUNIT_ASSERT( calc_comp->date2JD(-1001,8,17.9) == (double)1355671.4 );
	CPPUNIT_ASSERT( calc_comp->date2JD(-4712,1,1.5) == (double)0.0        );
}

void JDCalculationsTest::testGregorian() {
	CPPUNIT_ASSERT( calc_comp->date2JD(2000,1,1) == (double)2451544.5      );
	CPPUNIT_ASSERT( calc_comp->date2JD(1999,1,1) == (double)2451179.5      );
	CPPUNIT_ASSERT( calc_comp->date2JD(1957,10,4.81) == (double)2436116.31 );
	CPPUNIT_ASSERT( calc_comp->date2JD(1600,1,1) == (double)2305447.5      );
	CPPUNIT_ASSERT( calc_comp->date2JD(1600,12,31) == (double)2305812.5    );
}

void JDCalculationsTest::testExceptions() {
	CPPUNIT_ASSERT_THROW( calc_comp->date2JD(1600,0 ,23.3) , csatErrors::DateOutOfRangeEx );
	CPPUNIT_ASSERT_THROW( calc_comp->date2JD(1600,14,23.3) , csatErrors::DateOutOfRangeEx );
	CPPUNIT_ASSERT_THROW( calc_comp->date2JD(1600,11,-3)   , csatErrors::DateOutOfRangeEx );
	CPPUNIT_ASSERT_THROW( calc_comp->date2JD(1600,11,34)   , csatErrors::DateOutOfRangeEx );
}
