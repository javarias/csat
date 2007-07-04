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
 * To use the client just go to the \c src directory and run \c make.
 * The \c cli_nex4 executable file should be created at the \c bin
 * directory. By default it uses the first serial port \c /dev/ttyS0. You
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
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <Communication.h>
#include <signal.h>

/** The default serial port's device file. */
#define DEFAULT_PORT "/dev/ttyS0"

/* Communication object */
Communication *com = NULL;

/**
 * Leaves the program at the SIGINT signal
 * @param sig The signal received
 */
void leave(int sig);

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

	com = new Communication(serialPort);

	/* With Ctrl-C restore the old terminal parameters */
	signal(SIGINT,leave);
	
	/* Probing echo */
	printf("echo \'c\': %s\n",com->echo('c'));
	
	/* Probing aligment complete */
	printf("alignmentComplete?: ");
	if( com->alignmentComplete() )
			  printf("true\n");
	else
			  printf("false\n");

	/* Probing slew rates */
	com->Slew(9,AZM_POS);
	printf("Sleeping 4 seconds...\n");
	sleep(4);
	com->Slew(0,AZM_POS);
	printf("Sleeping 2 seconds...\n");
	sleep(2);
	com->Slew(9,AZM_NEG);
	printf("Sleeping 1 seconds...\n");
	sleep(1);
	com->Slew(8,ALT_NEG);
	printf("Sleeping 4 seconds...\n");
	sleep(4);
	com->Slew(0,AZM_POS);
	printf("Sleeping 3 seconds...\n");
	sleep(3);
	com->Slew(0,ALT_POS);
	delete com;

	printf("Exiting aplication...\n");
	exit(EXIT_SUCCESS);
}

void leave(int signal){
	printf("Receiving the SIGINT signal... leaving now\n");
	delete com;
	printf("Exiting aplication...\n");
	exit(EXIT_SUCCESS);
}
