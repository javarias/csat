#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdarg.h>
#include "telescope.h"

extern telescope_t nexstar;

char* get_ra_dec(char *args) {
	return NULL;
}

char* get_azm_alt(char *args){
	char *res;
	res = (char *)malloc(sizeof(char)*10);
	sprintf(res,"%04X,%04X#",nexstar.azimuthRevolutions, nexstar.altitudeRevolutions);
	verbosity("Get AZM/ALT: %04X, %04X\n",nexstar.azimuthRevolutions, nexstar.altitudeRevolutions);
	return res;
}

char* goto_ra_dec(char *args){
	return "#";
}

char* goto_azm_alt(char *args){

	/* This should be implemented as a new thread */
	/* Set the telescope to moving status */
	nexstar.gotoStatus = GOTO_MOVING;
	while( nexstar.gotoStatus != GOTO_STOPED ){
		usleep(50000);
	}

	/* and this should be returned at once */
	return "#";
}

char* get_version(char *args){
	char *versionRes;
	versionRes=(char *)malloc(3*sizeof(char));
	verbosity("version: %d.%d\n", nexstar.version.major, nexstar.version.minor);
	sprintf(versionRes,"%c%c#",nexstar.version.major, nexstar.version.minor);
	return versionRes;
}

char* echo(char *args){
	char *returnString;
	returnString = (char *)malloc(2*sizeof(char));
	returnString[0]=args[0];
	returnString[1]='#';

	verbosity("echo: %c\n",args[0]);

	return returnString;
}

char* alignment_complete(char *args){
	char *res;
	res = (char *)malloc(sizeof(char)*2);
	sprintf(res,"%c#",nexstar.alignmentStatus);
	verbosity("aligmentComplete: %d\n",nexstar.alignmentStatus);
	return res;
}

char* goto_in_progress(char *args){
	char *response;
	response = (char *)malloc(sizeof(char)*2);
	verbosity("gotoStatus: %d\n",nexstar.gotoStatus);
	sprintf(response,"%d#",nexstar.gotoStatus);
	return response;
}

char* cancel_goto(char *args){
	nexstar.gotoStatus = GOTO_STOPED;
	return "#";
}

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
