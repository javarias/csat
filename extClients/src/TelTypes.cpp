#include "TelTypes.h"

using namespace std;

char telType[100][10];
int telNum = 2;

void initTelTypes()
{
	sprintf(telType[0], "Nexstar");
	sprintf(telType[1], "Lx200");
}
