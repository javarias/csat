#include <stdio.h>
#include <string.h>
#include "telescope.h"
#include "listener.h"

#define DEFAULT_PORT "/dev/ttyS0"

telescope_t *nexstar;
int main(int args, char *argv[]) {
	char serialPort[256];

	/* Create the nexStar telescope */
	telescope_t nexstar_telescope = {
		.message = "",
		.version = { .major = 4, .minor = 1 },
		.alignmentStatus = ALIGNED,
		.gotoStatus = GOTO_STOPED
	};
	nexstar = &nexstar_telescope;

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
