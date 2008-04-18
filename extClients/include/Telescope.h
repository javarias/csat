#ifndef _TELESCOPE_H_
#define _TELESCOPE_H_

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <termios.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <stropts.h>
#include <sys/stropts.h>
#include <signal.h>
#include <sgtty.h>
#include <string.h>
#include "CSATClient.h"

class Telescope
{
public:
	Telescope();
	virtual ~Telescope();
	int start();
	virtual void parseInstructions();
protected:
	bool cscRun;
	bool cssRun;
	CSATClient *csatC;
	int fdm;
};

#endif
