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
}

Communication::Communication(char *deviceName, bool verbose){
	this->sp = new SerialRS232(deviceName,300);
	this->verbose = verbose;
}

Communication::~Communication(){
	delete this->sp;
}

char *Communication::getResponse() {

	char *msg;
	int i;

	msg = this->sp->read_RS232();

	/* First, we have to handle the ACK stuff */
	if( msg[0] != SOM )
		return (char *)MSG_ERR;
	for(i=1; !(msg[i-1] == EOM_0 && msg[i] == EOM_1); i++);
	i++;

	if( checksum(msg,i) )
		return (char *)MSG_ERR;

	/* Now we check the other part of the message */
	msg = msg + i;

	if( msg[0] != SOM )
		return (char *)MSG_ERR;

	for(i=1; !(msg[i-1] == EOM_0 && msg[i] == EOM_1); i++);
	i++;

	if( checksum(msg,i) )
		return (char *)MSG_ERR;

	return msg;
}

void Communication::request(char what) {

	char message[8];

	message[0] = SOM;
	message[1] = CMDDAT;
	message[2] = 0x02;    /* number of data bytes */
	message[3] = what;
	message[4] = EMPTY_BYTE;
	/* checksum will be calculated at the end */
	message[6] = EOM_0;
	message[7] = EOM_1;

	unsigned char checksum = 0;
	int i;
	for(i=1; i < 5; i++) {
		checksum += message[i];
		if( message[i] == SOM )
			i++;
	}
	checksum = (unsigned char)0x100 - (checksum % 0x100);
	message[5] = checksum;

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

	if( checksum % 0x100 ) {
		VERBOSITY( fprintf(stderr,"Checksum error!") );
		return 1;
	}

	return 0;
}

double Communication::getLatitude() {

	double latitude = 0;
	char *msg;

	request(POSITION_XFER);
	msg = getResponse();

	if( !strcmp(msg,MSG_ERR) )	{
		VERBOSITY( fprintf(stderr,"Couldn't get the position from the GPS\n") );
		return ERR_VALUE;
	}

	memcpy(&latitude,msg + 4,8);

	return (latitude*180)/M_PI;
}

double Communication::getLongitude() {

	double longitude = 0;
	char *msg;

	request(POSITION_XFER);
	msg = getResponse();

	if( !strcmp(msg,MSG_ERR) )	{
		VERBOSITY( fprintf(stderr,"Couldn't get the position from the GPS\n") );
		return ERR_VALUE;
	}

	memcpy(&longitude, msg+12, 8);

	return (longitude*180)/M_PI;
}

void Communication::getTime() {

	char *msg;

	request(TIME_XFER);
	msg = getResponse();

	if( !strcmp(msg,MSG_ERR) ) {
		VERBOSITY( fprintf(stderr,"Couldn't get the time from the GPS\n") );
		return;
	}

	//int i;
	//for( i=0; i!=8; i++)
	//	printf("%02X ", (unsigned char)tmp[i]);
	//printf("\n");

	uint8_t  month  = msg[3];
	uint8_t  day    = msg[4];
	uint16_t year;    memcpy(&year, msg + 5, 2);
	uint16_t hour;    memcpy(&hour, msg + 7, 2);
	uint8_t  minute = msg[9];
	uint8_t  second = msg[10];

	printf("Today is %u/%u/%u, at %02u:%02u:%02u UTC\n", day, month, year, hour, minute, second);
}
