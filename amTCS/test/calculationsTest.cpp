#include <cppunit/TestResult.h>
//#include <cppunit/TestResultCollector.h>
#include <cppunit/TestRunner.h>
#include <cppunit/ui/text/TextTestRunner.h>
//#include <cppunit/BriefTestProgressListener.h>

#include "JDCalculationsTest.h"

char **args;

int main (int argc, char *argv[]){

	args = argv;

	CPPUNIT_NS::TestResult result;

	//CPPUNIT_NS::TestResultCollector collectedResults;
	//result.addListener(&collectedResults);

	//CPPUNIT_NS::BriefTestProgressListener progress;
	//result.addListener(&progress);

	CPPUNIT_NS::TextTestRunner runner;
	runner.addTest( JDCalculationsTest::suite() );
	runner.run();
	//runner.run(result);

	//return (collectedResults.wasSuccessful() ? 0 : 1);
	return 0;
}
