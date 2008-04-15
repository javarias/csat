#ifndef _CSATCONTROLCLIENT_H_
#define _CSATCONTROLCLIENT_H_

#include <maciSimpleClient.h>
#include <CSATControlC.h>
#include <CSATStatusC.h>
#include <ACSErrTypeCommon.h>
#include <acsutilTimeStamp.h>
#include <string.h>

using namespace maci;

class CSATClient
{
public:
	CSATClient();
	~CSATClient();
	CSATCONTROL_MODULE::CSATControl_var getcscClient();
	CSATSTATUS_MODULE::CSATStatus_var getcssClient();
	int startCSC();
	int startCSS();
	void stop();
private:
	SimpleClient client;
	CSATCONTROL_MODULE::CSATControl_var csc;
	CSATSTATUS_MODULE::CSATStatus_var css;
};

#endif
