/**
 * \file listener.c
 * Conatins code for listening on the serial port. This file implements 
 * the polling mechanism to read data from the serial port. It consists
 * of a reading procedure and a closing procedure for correctly closing
 * the serial port when a SIGINT signal is detected (e.g. a CTRL+C).
 * \author Rodrigo Tobar <rtobar@csrg.inf.utfsm.cl>
 * \author Jorge Valencia <jorjazo@labsd.inf.utfsm.cl>
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <termios.h>
#include <signal.h>

#include "telescope.h"
#include "listener.h"

/**
 * Operating baud rate (as said on Nexstar4's documentation).
 */
#define BAUDRATE B9600

static int fd;
static struct termios oldtio;

/**
 * Holds the simulated telescope's status.
 */
extern telescope_t nexstar;

/**
 * Implements the serial port polling thread. This function basically initializes
 * the serial port, registers the SIGINT handler function and starts polling the
 * serial port for commands. For each received command invokes the corresponding
 * handling function. As for version 0.1 a telescope.c file is included for
 * implementing those handler functions, but it also should work with any other
 * code implemmenting the functions listed in telescope.h.
 *
 * @param device a string with the full path to the serial port's device file.
 * @returns Nothing.
 */
void listen_serial(const char *device) {
	char* (*commands[256])(char*);
	char cmd, *args, *out, in[256];
	int i,res;
	struct termios newtio;

	for (i=0; i<255; i++)
		commands[i]=NULL;

	commands['E']=get_ra_dec;
	commands['Z']=get_azm_alt;
	commands['R']=goto_ra_dec;
	commands['B']=goto_azm_alt;
	commands['V']=get_version;
	commands['K']=echo;
	commands['J']=alignment_complete;
	commands['L']=goto_in_progress;
	commands['M']=cancel_goto;

	/* We open the serial port and get its attributes */
	fd = open(device, O_RDWR | O_NOCTTY);
	if( fd <= 0 ){
		fprintf(stderr,"Fatal: Can't open '%s' for read/write\n",device);
		exit(EXIT_FAILURE);
	}
	fsync(fd);

	/* Catch SIGINT signal for restoring the serial port and close the fd */
	signal(SIGINT,leave);
	tcgetattr(fd,&oldtio);
	bzero(&newtio,sizeof(newtio));
	
	newtio.c_cflag  = BAUDRATE | CREAD | CS8 | CLOCAL;
	newtio.c_iflag = IGNPAR | ICRNL;
	newtio.c_oflag = 0;
	newtio.c_lflag = ICANON;

	tcsetattr(fd,TCSANOW,&newtio);

	while(1){
		fsync(fd);
		res = read(fd,in,256);
		cmd=in[0];
		if( strlen(in)-1 != 0) {
			args=(char *)malloc(strlen(in)-1);
			strncpy(args,in+1,strlen(in)-1);
		} else
			args = NULL;

		if(commands[(int)cmd] != NULL) {
			out=commands[(int)cmd](args);
			write(fd,out,strlen(out));
			free(out);
		} else {
			verbosity("Error: Command '%c' not suported, ignoring it...\n",cmd);
		}
		if (args!=NULL) free(args);
	}

	return;
}

/**
 * SIGINT handler function. This function is used to properly close the serial
 * port and exiting when catching a SIGINT signal.
 * @param sig the signal's integer value.
 * @returns Nothing. However, it causes the simulator to exit with a return value
 * of zero (0).
 */
void leave(int sig){
	printf("Receiving SIGINT signal, closing port...\n");
	tcsetattr(fd,TCSANOW,&oldtio);
	close(fd);
	exit(EXIT_SUCCESS);
}

