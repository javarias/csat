/**
 * \mainpage NexStar 4 SE Simulator v0.1
 * \section intro_sec Introduction
 *
 * This project intends to simulate a Celestron's NexStar4 SE telescope.
 * When run on a host machine, it keeps listening for instructions comming
 * from a serial port so another machine (a client machine) can be connected
 * to the host computer through a null-modem cable, making transparent to the
 * client that it is not really connected to a telescope.
 *
 * This application was developed for testing Telescope Control Systems (TCS)
 * at Universidad T&eacute;cnica Federico Santa Mar&iacute;a, in Valparaiso, Chile.
 *
 * \section usage-sec Usage
 *
 * To use the simulator just go to the \c src directory and run \c make.
 * The \c sim_nex4 executable file should be created at the \c bin
 * directory. By default it listens to the first serial port \c /dev/ttyS0. You
 * can change this behavior by passing the desired device file as a command-line argument:
 *
 * <center>\c ./sim_nex4 &lt;device_file&gt;</center>
 *
 * Be aware that you will need R/W permission on any device file to be used.
 *
 * \sa <a href="http://www.celestron.com/c2/images/files/downloads/1154108406_nexstarcommprot.pdf">
 * NexStar Communication Protocol v 1.2</a>.
 *
 * \bug Has erratic bahevior when used from a computer without a serial port.
 */

/**
 * \file main.c
 * Main routine implementation. This procedure proccess command-line arguments (if any) and
 * calls the listener routine with the correct device filename string.
 * \author Rodrigo Tobar <rtobar@csrg.inf.utfsm.cl>
 * \author Jorge Valencia <jorjazo@labsd.inf.utfsm.cl>
 */

#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include "telescope.h"
#include "listener.h"

/** The default serial port's device file. */
#define DEFAULT_PORT "/dev/ttyS0"

/** Nexstar4 status (using Celestron's protocol v1.2) */
telescope_t nexstar = {
	{ 1, 2 }, /* Version */
	ALIGNED,
	GOTO_STOPED,
	1000, /* Initial azmiuth  */
	10000  /* Initial altitude */
};

/**
 * Main routine. Starts the serial port listener, passing it the device
 * file received as argument or the default serial port device file
 * defined as DEFAULT_PORT.
 * @param args number of command-line arguments passed.
 * @param argv command-line arguments values.
 * @returns Always zero (0). However, a normal termination is caused by
 * a SIGINT signal handled by the listener routine's implementation so
 * on normal conditions this program will return any value specified there.
 * @sa telescope.c leave(int sig)
 */
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

