#include <string>
#include <stdlib.h>
#include <stdio.h>
#include <time.h>

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


using namespace std;

class Communication{

	private:
	SerialRS232 *sp;
	bool verbose;

	void   request(char);
	char*  getResponse();
	int    checksum(char *, int);

	public:
	Communication(char *deviceName);
	Communication(char *deviceName, bool verbose);
	double getLatitude();
	double getLongitude();
	void getTime();
	
	~Communication();
};
