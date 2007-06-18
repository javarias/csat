/**
 * \file telescope.c
 * Telescope's protocol implementation and status simulation routines. This file
 * includes a function for every supported command of Celestron's communication
 * protocol. The implementation of these functions try to simulate the behavior
 * of a real Nexstar4 telescope.
 * @sa <a href="http://www.celestron.com/c2/images/files/downloads/1154108406_nexstarcommprot.pdf">
 *     NexStar Communication Protocol v 1.2</a>
 * \author Rodrigo Tobar <rtobar@csrg.inf.utfsm.cl>
 * \author Jorge Valencia <jorjazo@labsd.inf.utfsm.cl>
 * \warning All the functions implemented here responds to the <tt>char *funct(char *args)</tt>
 * prototype. For specific information about the received or returned string you are strongly
 * encouraged to read the
 * <a href="http://www.celestron.com/c2/images/files/downloads/1154108406_nexstarcommprot.pdf">
 * NexStar Communication Protocol v 1.2</a>.
 */

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdarg.h>
#include "telescope.h"

/** Simulated telescope's status. */
extern telescope_t nexstar;

/**
 * Returns a string with the simulated position with RA/DEC coordinates.
 * @param args should be an empty string or null pointer included just for prototype compatibility.
 * @returns A string with the correspondig coordinates.
 */
char* get_ra_dec(char *args) {
	return NULL;
}

/**
 * Returns a string with the simulated position with AZM/ALT coordinates.
 * @param args should be an empty string or null pointer included just for prototype compatibility.
 * @returns A string with the correspondig coordinates.
 */
char* get_azm_alt(char *args){
	char *res;
	res = (char *)malloc(sizeof(char)*10);
	sprintf(res,"%04X,%04X#",nexstar.azimuthRevolutions, nexstar.altitudeRevolutions);
	verbosity("Get AZM/ALT: %04X, %04X\n",nexstar.azimuthRevolutions, nexstar.altitudeRevolutions);
	return res;
}

/**
 * Causes the simulated telescope to move to a specific position in RA/DEC coordinates.
 * @param args string with the desired position.
 * @returns A "#" string.
 */
char* goto_ra_dec(char *args){
	return "#";
}

/**
 * Causes the simulated telescope to move to a specific position in AZM/ALT coordinates.
 * @param args string with the desired position.
 * @returns A "#" string.
 */
char* goto_azm_alt(char *args){

	/* This should be implemented as a new thread */
	/* Set the telescope to moving status */
	//nexstar.gotoStatus = GOTO_MOVING;
	//while( nexstar.gotoStatus != GOTO_STOPED ){
	//	usleep(50000);
	//}

	/* and this should be returned at once */
	return "#";
}

/**
 * Returns a string with the telescope's protocol version.
 * @param args should be an empty string or null pointer included just for prototype compatibility.
 * @returns A string with the simulated telescope's protocol version.
 */
char* get_version(char *args){
	char *versionRes;
	versionRes=(char *)malloc(3*sizeof(char));
	verbosity("version: %d.%d\n", nexstar.version.major, nexstar.version.minor);
	sprintf(versionRes,"%c%c#",nexstar.version.major, nexstar.version.minor);
	return versionRes;
}

/**
 * Echo function. Replies with the received message.
 * @param args any message string.
 * @returns The echo response.
 */
char* echo(char *args){
	char *returnString;
	returnString = (char *)malloc(2*sizeof(char));
	returnString[0]=args[0];
	returnString[1]='#';

	verbosity("echo: %c\n",args[0]);

	return returnString;
}

/**
 * Tells whether the telescope has completed its alignment or not .
 * @param args should be an empty string or null pointer included just for prototype compatibility.
 * @returns A string with the correspondig answer.
 */
char* alignment_complete(char *args){
	char *res;
	res = (char *)malloc(sizeof(char)*2);
	sprintf(res,"%c#",nexstar.alignmentStatus);
	verbosity("aligmentComplete: %d\n",nexstar.alignmentStatus);
	return res;
}

/**
 * Tells whether the telescope is moving or not.
 * @param args should be an empty string or null pointer included just for prototype compatibility.
 * @returns A string with the correspondig answer.
 */
char* goto_in_progress(char *args){
	char *response;
	response = (char *)malloc(sizeof(char)*2);
	verbosity("gotoStatus: %d\n",nexstar.gotoStatus);
	sprintf(response,"%d#",nexstar.gotoStatus);
	return response;
}

/**
 * Causes the simulated telescope to stop moving.
 * @param args should be an empty string or null pointer included just for prototype compatibility.
 * @returns A "#" string.
 */
char* cancel_goto(char *args){
	nexstar.gotoStatus = GOTO_STOPED;
	return "#";
}

/**
 * This is an auxiliary function and does not correspond to any telescope command. It is intended
 * to be a logging front-end to printf().
 * @param message a logging message format string (as used with \c printf()).
 * @param ... any value to be used with the \c message format string.
 * @returns Nothing.
 */
void verbosity(const char *message, ...) {
	va_list args;
	int verbose = 1;

	va_start(args,message);
	if(verbose){
		vprintf(message,args);
	}
	va_end(args);
	return;
}
