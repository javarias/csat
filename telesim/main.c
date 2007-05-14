#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include "telescope.h"
#include "listener.h"

#define DEFAULT_PORT "/dev/ttyS0"

/* Create the nexStar telescope */
telescope_t nexstar = {
	{ 1, 2 }, /* Version */
	ALIGNED,
	GOTO_STOPED,
	1000, /* Initial azmiuth  */
	10000  /* Initial altitude */
};

int main(int args, char *argv[]) {
	char serialPort[256];

	/* Set the serial port to use */
	if( args < 2 ){
		printf("Using default port %s.\n", DEFAULT_PORT);
		strcpy(serialPort,DEFAULT_PORT);
	} else {
		strcpy(serialPort,argv[1]);
	}

	listen_serial(serialPort);
	return 0;
}
