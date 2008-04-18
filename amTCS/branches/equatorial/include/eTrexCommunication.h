#ifndef ETREX_COMMUNICATION_H_
#define ETREX_COMMUNICATION_H_

#include <string>

#include <SerialRS232.h>

/* Defines start and end of a eTrex Legend serial message */
#define SOM      0x10
#define EOM_0    0x10
#define EOM_1    0x03

/* Type of messages */
#define POSITION_XFER     0x02
#define TIME_XFER         0x05
#define CMDDAT            0x0A

/* Other constants */
#define EMPTY_BYTE  0x00
#define ERR_VALUE   -1000.0
#define MSG_ERR     "ERROR"
#define MAX_DEGREE  0x7FFFFFFF

#define VERBOSITY(X)   if( this->verbose ) X;

using namespace std;

class eTrexCommunication{

	private:
	SerialRS232 *sp;
	bool verbose;
	int  checksum(char *, int);

	public:
	eTrexCommunication(char *deviceName, bool verbose) throw (SerialRS232::SerialRS232Exception);
	~eTrexCommunication();
	void   request(char);
	char*  getResponse();


};

#endif /* ETREX_COMMUNICATION_H_ */
