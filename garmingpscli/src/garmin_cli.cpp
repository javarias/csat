#include "Communication.h"

int main(int args, char *argv[]) {

	char serial_port[256] = "/dev/ttyS0";

	Communication *comm = new Communication(serial_port,true);
	printf("%lf, %lf\n",comm->getLatitude(), comm->getLongitude());
	
	delete comm;
	return 0;
}
