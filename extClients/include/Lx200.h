#ifndef _LX200_H_
#define _LX200_H_

#include "Telescope.h"
#define MAX_PRECISE_ROTATION   4294967296.0
#define ENDCHAR 124

class Lx200: public virtual Telescope
{
public:
	Lx200();
	~Lx200();
	void parseInstructions();
	char *getAlignment();
	char *setAlignment();
	char *reticuleControl();
	char *sync();
	char *getDistance();
	char *fanControl();
	char *focuserControl();
	char *gpsControl();
	char *getInformation();
	char *homeControl();
	char *timeFormat();
	char *initialize();
	char *objectsControl();
	char *movement();
	char *togglePointingPrecision();
	char *haltMovement();
	char *fieldDerotatorControl();
	char *slewRate();
	char *telescopeSettings();
	char *trackingControl();
	char *togglePrecision();
	char *selectSite();
	char *moreCommands();
	char *helpCommands();
	char *badMessageResponse();
	char *defaultMessageResponse();
	int length(char *msg);
};

#endif