#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include "telescope.h"

extern telescope_t *nexstar;

char* get_ra_dec(char *args) {
	return NULL;
}

char* get_azm_alt(char *args){
	return NULL;
}

char* goto_ra_dec(char *args){
	return NULL;
}

char* goto_azm_alt(char *args){

	/* This should be implemented as a new thread */
	/* Set the telescope to moving status */
	nexstar->gotoStatus = GOTO_MOVING;
	while( nexstar->gotoStatus != GOTO_STOPED ){
		usleep(50000);
	}

	/* and this should be returned at once */
	return "#";
}

char* get_version(char *args){
	char *version;
	version=(char *)malloc(3*sizeof(char));
	sprintf(version,"%c%c#",nexstar->version.major, nexstar->version.minor);
	return version;
}

char* echo(char *args){
	char *returnString;
	returnString = (char *)malloc(2*sizeof(char));
	returnString[0]=args[0];
	returnString[1]='#';

	printf("echo: %c\n",args[0]);

	return returnString;
}

char* alignment_complete(char *args){
	return NULL;
}

char* goto_in_progress(char *args){
	char *response;
	response = (char *)malloc(sizeof(char));
	sprintf(response,"%d",nexstar->gotoStatus);
	return response;
}

char* cancel_goto(char *args){
	nexstar->gotoStatus = GOTO_STOPED;
	return "#";
}
