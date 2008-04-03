/**
 * \file Communication.cpp
 *
 * Implements the communication with the Garmin Legend eTrex GPS at a
 * logical level; this means that this class is the responsible of formatting
 * the incoming and outcoming strings from/to the device, and convert then into
 * usefull information. The communication with the device itself is done by the
 * SerialRS232 class.
 *
 * \author Rodrigo Tobar <rtobar@csrg.inf.utfsm.cl>
 */
#include <iostream>
#include <math.h>

#include "Communication.h"
#include "verbosity.h"

Communication::Communication(char *deviceName){
	this->sp = new SerialRS232(deviceName,300);
	this->sp->flush_RS232();
}

Communication::Communication(char *deviceName, bool verbose){
	this->sp = new SerialRS232(deviceName,300);
	this->sp->flush_RS232();
	this->verbose = verbose;
}

Communication::~Communication(){
	delete this->sp;
}

string Communication::getPosition() {

	char *msg;

	msg = this->sp->read_RS232();

	if( msg[0] != SOM || msg[29] != EOM_0 || msg[30] != EOM_1 )
		return string(MSG_ERR);

	strncpy(msg,(msg+8),23);

	if( checksum(msg,23) )
		return string(MSG_ERR);

	return string(msg);
}

void Communication::requestPosition() {

	char message[8];

	message[0] = SOM;
	message[1] = 0x0a;
	message[2] = 0x02;    /* number of data bytes */
	message[3] = 0x02;
	message[4] = EMPTY_BYTE;
	message[5] = 0xF2;  /* Checksum = 100 */
	message[6] = EOM_0;
	message[7] = EOM_1;

	this->sp->write_RS232(message, 8);
}

int Communication::checksum(char *msg, int size) {

	int i;
	int checksum = 0;

	/* If there's a SOM byte, then will be another next to it
	 * which mustn't be counted in the checksum */
	for(i=1; i<size-2; i++) {
		checksum += (unsigned char)msg[i];
		if( (unsigned char)msg[i] == SOM )
			i++;
	}

	if( checksum % 0x100 )
		return 1;

	return 0;
}

double Communication::getLatitude() {

	double latitude = 0;

	requestPosition();
	string msg = getPosition();

	if( msg == MSG_ERR )	{
		VERBOSITY( fprintf(stderr,"Couldn't get the position from the GPS\n") );
		return ERR_VALUE;
	}

	const char *tmp = msg.substr(4,8).c_str();
	memcpy(&latitude,tmp,8);

	return (latitude*180)/PI;
}

double Communication::getLongitude() {

	double longitude = 0;

	requestPosition();
	string msg = getPosition();

	if( msg == MSG_ERR )	{
		VERBOSITY( fprintf(stderr,"Couldn't get the position from the GPS\n") );
		return ERR_VALUE;
	}

	const char *tmp = msg.substr(12,8).c_str();
	memcpy(&longitude,tmp,8);

	return (longitude*180)/PI;
}
