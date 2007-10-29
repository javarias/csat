#include <cppunit/TestResult.h>
#include <cppunit/TestRunner.h>
#include <cppunit/ui/text/TextTestRunner.h>

#include "JDCalculationsTest.h"
#include "PrecessionTest.h"

CALCULATIONS_MODULE::Calculations_var calc_comp;

int main (int argc, char *argv[]){

	CPPUNIT_NS::TestResult result;
	CPPUNIT_NS::TextTestRunner runner;

	char *comp_name = "CALCULATIONS_CPP";
	SimpleClient client;

	if( client.init(0,argv) == 0 ){
		ACS_SHORT_LOG((LM_ERROR,"Cannot init client"));
		return -1;
	}

	client.login();
	calc_comp = client.get_object<CALCULATIONS_MODULE::Calculations>(comp_name,0,true);

	runner.addTest( JDCalculationsTest::suite() );
	runner.addTest( PrecessionTest::suite() );
	runner.run();

	client.releaseComponent(comp_name);
	client.disconnect();
	client.logout();
	return 0;
}
