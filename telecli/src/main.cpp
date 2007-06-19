/**
 * \mainpage NexStar 4 SE client v0.1
 * \section intro_sec Introduction
 *
 * This project intends to comunicate with a Celestron's NexStar4 SE telescope.
 * When run on a host machine, it opens a serial port to communicate with the
 * the telescope and pass it some commands.
 *
 * This application was developed for testing the telescope RS232 serial 
 * communication protocol. This is part of the Telescope Control System (TCS)
 * being developed at Universidad T&eacute;cnica Federico Santa Mar&iacute;a,
 * in Valparaiso, Chile, which is part of the CSAT project.
 *
 * \section usage-sec Usage
 *
 * To use the simulator just go to the \c src directory and run \c make.
 * The \c sim_nex4 executable file should be created at the \c bin
 * directory. By default it listens to the first serial port \c /dev/ttyS0. You
 * can change this behavior by passing the desired device file as a command-line argument:
 *
 * <center>\c ./cli_nex4 &lt;device_file&gt;</center>
 *
 * Be aware that you will need R/W permission on any device file to be used.
 *
 * \sa <a href="http://www.celestron.com/c2/images/files/downloads/1154108406_nexstarcommprot.pdf">
 * NexStar Communication Protocol v 1.2</a>.
 */

/**
 * \file main.cpp
 * Main routine implementation. This procedure proccess command-line arguments (if any) and
 * calls the serial routine with the correct device filename string.
 * \author Rodrigo Tobar <rtobar@csrg.inf.utfsm.cl>
 */

#include <stdio.h>
#include <string.h>
#include <unistd.h>

/** The default serial port's device file. */
#define DEFAULT_PORT "/dev/ttyS0"

/**
 * Main routine. Starts the serial port, passing it the device
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

	//open_serial(serialPort);
	return 0;
}

