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

#include "Communication.h"
#include "verbosity.h"

Communication::Communication(char *deviceName){
	this->sp = new SerialRS232(deviceName, 0x00);
	this->sp->flush_RS232();
}

Communication::Communication(char *deviceName, bool verbose){
	this->sp = new SerialRS232(deviceName, 0x00);
	this->sp->flush_RS232();
	this->verbose = verbose;
}

Communication::~Communication(){
	delete this->sp;
}

string Communication::getTrackPoint() {

	char *msg;
	msg = this->sp->read_RS232();
	printf("OK :D\n");

	if( msg[0] != SOM || msg[17] != EOM_0 || msg[18] != EOM_1 )
		return string(MSG_ERR);

	if( checksum(msg,19) )
		return string(MSG_ERR);

	return string(msg);
}

void Communication::requestTrackPoint() {

	char message[8];

	message[0] = SOM;
	message[1] = ACK_MSG;
	message[2] = 0x02;    /* number of data bytes */
	message[3] = TRACK_MSG;
	message[4] = EMPTY_BYTE;
	message[5] = 0xD6;  /* Checksum = 100 */
	message[6] = EOM_0;
	message[7] = EOM_1;

	this->sp->write_RS232(message, 8);
}

int Communication::checksum(char *msg, int size) {

	int i;
	int checksum = 0;

	for(i=1; i!=size-1; i++)
		checksum += msg[i];

	if( checksum % 0x100 )
		return 1;

	return 0;
}

double Communication::getLatitude() {

	int i;
	double latitude;
	long tmp = 0;

	requestTrackPoint();
	string msg = getTrackPoint();

	if( msg == MSG_ERR )	{
		VERBOSITY( fprintf(stderr,"Couldn't get a track point from the GPS") );
		return ERR_VALUE;
	}

	for(i=0 ;i!=4; i++) {
		tmp += msg[3 + i] << i;
	}

	VERBOSITY( printf("%ld",tmp) );

	latitude = (180/MAX_DEGREE)*tmp;

	return latitude;
}
