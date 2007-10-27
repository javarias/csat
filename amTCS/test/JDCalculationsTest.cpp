#include <maciSimpleClient.h>
#include <acsutil.h>

#include "CalculationsC.h"
#include "JDCalculationsTest.h"

using namespace maci;

JDCalculationsTest::JDCalculationsTest(){
	comp_name =  "CALCULATIONS_CPP";
}

void JDCalculationsTest::setUp(){
	if( client.init(0,NULL) == 0 ){
		ACS_SHORT_LOG((LM_ERROR,"Cannot init client"));
		return;
	}

	client.login();
	calc_comp = client.get_object<CALCULATIONS_MODULE::Calculations>(comp_name,0,true);
}

void JDCalculationsTest::tearDown() {
	client.manager()->release_component(client.handle(),comp_name);
}

void JDCalculationsTest::testGregorian() {
	CPPUNIT_ASSERT( calc_comp->date2JD(2000,1,1) == (double)2451544.5 );
	CPPUNIT_ASSERT( calc_comp->date2JD(1999,1,1) == (double)2451179.5 );
	CPPUNIT_ASSERT( calc_comp->date2JD(1957,10,4.81) == (double)2463116.31);
	CPPUNIT_ASSERT( calc_comp->date2JD(1600,1,1) == (double)2305447.5);
	CPPUNIT_ASSERT( calc_comp->date2JD(1600,12,31) == (double)2305812.5);
}

void JDCalculationsTest::testJulian() {
	CPPUNIT_ASSERT( calc_comp->date2JD(837,4,10.3) == (double)2026871.8 );
	CPPUNIT_ASSERT( calc_comp->date2JD(-123,12,31) == (double)1676496.5);
	CPPUNIT_ASSERT( calc_comp->date2JD(-1000,7,12.5) == (double)1356001.0);
	CPPUNIT_ASSERT( calc_comp->date2JD(-1001,8,17.9) == (double)1355671.4);
	CPPUNIT_ASSERT( calc_comp->date2JD(-4712,1,1.5) == (double)0.0);
}
