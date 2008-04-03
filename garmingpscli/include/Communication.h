#include <string>

#include <SerialRS232.h>

/* Defines start and end of a eTrex Legend serial message */
#define SOM      0x10
#define EOM_0    0x10
#define EOM_1    0x03

/* Type of messages */
#define ACK_MSG             0x06
#define TRACK_MSG_REQUEST   0x06
#define TRACK_MSG           0x22

/* Other constants */
#define EMPTY_BYTE  0x00
#define ERR_VALUE   -1000.0
#define MSG_ERR     "ERROR"
#define MAX_DEGREE  0x7FFFFFFF
#define PI (3.141592653589793)

using namespace std;

class Communication{

	private:
	SerialRS232 *sp;
	bool verbose;

	void   requestPosition();
	string getPosition();
	int    checksum(char *, int);

	public:
	Communication(char *deviceName);
	Communication(char *deviceName, bool verbose);
	double getLatitude();
	double getLongitude();
	
	~Communication();
};
