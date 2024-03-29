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
 * Also, in this package, is included a little program that tests the
 * connection with the telescope, so many movements are done automatically to
 * see if the telescope responses OK. This application is called \c test_nex4 ,
 * and to use it, just go into the \c bin directory, and type
 *
 * <center>\c ./test_nex4 &lt;device_file&gt;</center>
 *
 * \sa <a href="http://www.celestron.com/c2/images/files/downloads/1154108406_nexstarcommprot.pdf">
 * NexStar Communication Protocol v 1.2</a>.
 */

/**
 * \file client.cpp
 * Main routine implementation. This procedure proccess command-line arguments (if any) and
 * calls the serial routine with the correct device filename string.
 * \author Rodrigo Tobar <rtobar@csrg.inf.utfsm.cl>
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <Communication.h>

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
 * Enables verbosity
 */
int verbose = 0;

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
	int rate = 0;

	setbuf(stdout,NULL);

	/* Set the serial port to use */
	if( args < 2 ){
		printf("\nUsage: %s [-v] var [value]\n\n",argv[0]);
		printf("Variables supported are:\n");
		printf("      [slal | slaz] rate    (slew in altitud and azimuth direction)\n");
		printf("      echo char             (to test connection to telescope)\n");
		printf("      [gtal | gtaz] degrees (go to altitude and azimuth coordinates)\n");
		printf("      gtalaz alt az         (go to cetain altitude/azimuth coordinate)\n");
		printf("      [getalt | getazm]     (gets the actuals altitud/azimuth coords)\n");
		printf("      track                 (returns the tracking mode)\n");
		printf("      cancel                (Cancels a GOTO)\n\n");
		exit(EXIT_FAILURE);
	}

	if(argv[1][0] == '-' && argv[1][1] == 'v'){
		verbose = 1;
		argv++;
	}

//	if( args < 4 ){
	printf("Using default port %s.\n", DEFAULT_PORT);
	strcpy(serialPort,DEFAULT_PORT);
//	} else {
//		strcpy(serialPort,argv[1]);
//		argv++;
//	}

	com = new Communication(serialPort,verbose);

	/* With Ctrl-C restore the old terminal parameters */
	signal(SIGINT,leave);

	if( !strcmp("slaz",argv[1])){
		rate = atoi(argv[2]);
		if( rate >= 0 )
			com->Slew(rate,AZM_POS);
		else
			com->Slew(-rate,AZM_NEG);
	}

	else if( !strcmp("slal",argv[1])){
		rate = atoi(argv[2]);
		if( rate >= 0 )
			com->Slew(rate,ALT_POS);
		else
			com->Slew(-rate,ALT_NEG);
	}

	else if( !strcmp("echo",argv[1]))
		printf("echo: %c\n",com->echo(argv[2][0])[0]);

	else if( !strcmp("gtal",argv[1]))
		com->goToAltAzm(atof(argv[2]),com->getAzm());

	else if( !strcmp("gtaz",argv[1]))
		com->goToAltAzm(com->getAlt(),atof(argv[2]));

	else if( !strcmp("gtalaz",argv[1]))
		com->goToAltAzm(atof(argv[2]),atof(argv[3]));

	else if( !strcmp("getalt",argv[1]))
		printf("Actual Alt: %lf\n",com->getAlt());

	else if( !strcmp("getazm",argv[1]))
		printf("Actual Azm: %lf\n",com->getAzm());

	else if( !strcmp("cancel",argv[1]))
		com->cancelGoto();
	else if( !strcmp("track",argv[1]))
		printf("Tracking mode: %d\n",com->trackingMode());

	else{
		printf("Variable not supported: %s\n",argv[1]);
		delete com;
		exit(EXIT_FAILURE);
	}

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
