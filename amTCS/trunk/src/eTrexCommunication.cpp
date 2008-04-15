#include "eTrexCommunication.h"

eTrexCommunication::eTrexCommunication(char *deviceName, bool verbose) throw (SerialRS232::SerialRS232Exception) {

	this->sp = new SerialRS232(deviceName, 100);
	this->verbose = verbose;

}

eTrexCommunication::~eTrexCommunication() {
	delete this->sp;
}

void eTrexCommunication::request(char what) {

	char message[8];

	message[0] = SOM;
	message[1] = CMDDAT;
	message[2] = 0x02;   /* number of data bytes */
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

char *eTrexCommunication::getResponse() {

	char *msg;
	int i=0;
	int j=0;
	int k=0;

	msg = this->sp->read_RS232();

	/* First, we have to handle the ACK stuff */
	if( msg[0] != SOM )
		return (char *)MSG_ERR;
	for(i=1; !(msg[i-1] == EOM_0 && msg[i] == EOM_1); i++);  i++;

	if( checksum(msg,i) )
		return (char *)MSG_ERR;

	/* Now we check the other part of the message */
	msg = msg + i;

	if( msg[0] != SOM )
		return (char *)MSG_ERR;

	for(i=1; !(msg[i-1] == EOM_0 && msg[i] == EOM_1); i++);  i++;
	if( checksum(msg,i) )
		return (char *)MSG_ERR;

	/* Finally, remove all the doubled SOM in the body of the message */
	for(j=1; j!=i; j++)
		if( msg[j] == SOM && msg[j+1] == SOM )
			for(k=j; k!=i; k++)
				msg[k] = msg[k+1];
				
	return msg;
}

int eTrexCommunication::checksum(char *msg, int size) {

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

		VERBOSITY(
			for(i=0; i != size ; i++)
				printf("%02X ", (unsigned char)msg[i]);
			printf("\n");
		);

		VERBOSITY( fprintf(stderr,"Checksum error!\n") );
		return 1;
	}

	return 0;
}
