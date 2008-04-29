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
	Telescope(bool isLocal);
	virtual ~Telescope();
	virtual void parseInstructions();
	int start();
	void stop();
	void configPort();

protected:
	bool cscRun;
	bool cssRun;
	bool isLocal;
	bool run;
	bool move;
	CSATClient *csatC;
	int fdm;
   int fds;
	const char *serial;
	const char *serialbk;
};

#endif
