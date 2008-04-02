/**
 * \file Communication.cpp
 *
 * Implements the communication with the Celestron Nexstar 4 SE telescope at a
 * logical level; this means that this class is the responsible of formatting
 * the incoming and outcoming strings from/to the device, and convert then into
 * usefull information. The communication with the device itself is done by the
 * SerialRS232 class.
 *
 * \author Rodrigo Tobar <rtobar@csrg.inf.utfsm.cl>
 */

#include <stdio.h>
#include <string.h>

#include "Communication.h"
#include "verbosity.h"

Communication::Communication(char *deviceName){
	this->sp = new SerialRS232(deviceName,60);
	this->sp->flush_RS232();
}

Communication::Communication(char *deviceName, int verbose){
	this->sp = new SerialRS232(deviceName,60);
	this->sp->flush_RS232();
	this->verbose = verbose;
}

Communication::~Communication(){
	delete this->sp;
}

char Communication::trackingMode(){
	char *msg;

	this->sp->write_RS232("t",1);
	msg = this->sp->read_RS232();
	return msg[0];
}

char *Communication::echo(char c){
	char msg[3];
	msg[0] = 'K'; 	msg[1] = c;
	this->sp->write_RS232(msg,2);
	return (this->sp->read_RS232());
}

bool Communication::alignmentComplete(){
	char *msg;
	bool retValue = false;
	
	this->sp->write_RS232("J",1);
	msg = this->sp->read_RS232();
	if( msg[0] == 1 )
			  retValue = true;

	return retValue;
}

bool Communication::Slew(int rate, int direction){
	int movement = 0x0, axis = 0x0;
	if ( rate > 9 || rate < 0 ){
		fprintf(stderr,"ERROR: Rate limits are 1 <= rate <= 9\n");
		return false;
	}

	printf("Moving in ");
	switch ( direction ) {

		case AZM_POS:
			printf("AZM_POS");
			axis     = 0x10;
			movement = 0x24;
			break;
		case AZM_NEG:
			printf("AZM_NEG");
			axis     = 0x10;
			movement = 0x25;
			break;

		case ALT_POS:
			printf("ALT_POS");
			axis     = 0x11;
			movement = 0x24;
			break;
		case ALT_NEG:
			printf("ALT_NEG");
			axis     = 0x11;
			movement = 0x25;
			break;

	}
	printf(" at %d rate\n",rate);
	char command[8];
	command[0] = 'P';
	command[1] = 2;
	command[2] = axis;
	command[3] = movement;
	command[4] = rate;
	command[5] = 0;
	command[6] = 0;
	command[7] = 0;

	this->sp->write_RS232(command,8);
	this->sp->read_RS232();
	this->sp->flush_RS232();

	return true;
}

bool Communication::goToAltAzm(double alt, double azm){
//	if ( alt > 90 || alt < 0 ){
//		printf("Error: Altitud can't be > 90!\n");
//		return false;
//	}
	/* If azm is < 0, convert it into positive coordinates */
	while ( azm < 0 ){
		azm += 360.0;
	}

	azm /= 360;
	alt /= 360;

	char command[18];
	char *msg;
	sprintf(command,"b%08x,%08x",(unsigned int)(azm*MAX_PRECISE_ROTATION), (unsigned int)(alt*MAX_PRECISE_ROTATION));

	this->sp->write_RS232(command,18);
	this->sp->flush_RS232();
	msg = this->sp->read_RS232();

	return true;
}

double Communication::getAlt(){
	double alt;
	unsigned long read_alt, read_azm;
	char *msg;

	this->sp->write_RS232("z",1);
	msg = this->sp->read_RS232();
	VERBOSITY( printf("Received from the telescope: %s\n",msg); );
	sscanf(msg,"%08lX,%08lX#",&read_azm,&read_alt);

	alt = (double)(read_alt / MAX_PRECISE_ROTATION);
	alt *= 360.0;

	return alt;
}

double Communication::getAzm(){
	double azm;
	unsigned long read_alt, read_azm;
	char *msg;

	this->sp->write_RS232("z",1);
	msg = this->sp->read_RS232();
	VERBOSITY( printf("Received from the telescope: %s\n",msg); );
	sscanf(msg,"%08lX,%08lX#",&read_azm,&read_alt);

	azm = (double)(read_azm / MAX_PRECISE_ROTATION);
	azm *= 360.0;

	return azm;
}

void Communication::cancelGoto(){
	this->sp->write_RS232("M",1);
	this->sp->read_RS232();
	return;
}
