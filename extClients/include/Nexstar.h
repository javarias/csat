#ifndef _NEXSTAR_H_
#define _NEXSTAR_H_

#include "Telescope.h"
#define MAX_PRECISE_ROTATION   4294967296.0
#define ENDCHAR 124

class Nexstar: public virtual Telescope
{
public:
	Nexstar();
	~Nexstar();
	void parseInstructions();
	char *getRaDec();
	char *getPreciseRaDec();
	char *getAzmAlt();
	char *getPreciseAzmAlt();
	char *gotoRaDec();
	char *gotoPreciseRaDec();
	char *gotoAzmAlt();
	char *gotoPreciseAzmAlt();
	char *syncRaDec();
	char *syncPreciseRaDec();
	char *getTrackingMode();
	char *setTrackingMode();
	char *passThroughCommand();
	char *getLocation();
	char *setLocation();
	char *getTime();
	char *setTime();
	char *getVersion();
	char *getModel();
	char *echo();
	char *isAlignmentComplete();
	char *isGotoInProgress();
	char *cancelGoto();
	char *badMessageResponse();
	char *defaultMessageResponse();
	int length(char *msg);
};

#endif
