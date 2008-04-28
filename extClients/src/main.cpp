#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <signal.h>

#include "TelTypes.h"
#include "Telescope.h"
#include "Nexstar.h"
#include "Lx200.h"

using namespace std;

Telescope *tel = NULL;

extern char telType[100][10];
extern int telNum;

void leave(int sig) {
	printf("Receiving SIGINT signal, leaving application...\n");
	delete tel;
}

int main(int argv, char **argc)
{
	bool isLocal = false;
	int i = 0;
	if(argv < 2 || argv > 3)
		return -1;

	if(argv == 3) {
		if( !strcmp(argc[1],"-l") ) {
			isLocal = true;
			argc++;
		}
		else if( !strcmp(argc[2],"-l") )
			isLocal = true;
	}
	
	initTelTypes();
	while(strcmp(argc[1],telType[i]) && i < telNum)
		i++;

	switch(i)
	{
		case 0:
			tel = (Telescope *)new Nexstar(isLocal);
			printf("%s Wrapper Acquired\n", argc[1]);
			break;
		case 1:
			tel = (Telescope *)new Lx200(isLocal);
			printf("%s Wrapper Acquired\n", argc[1]);
			break;
		default:
			tel = NULL;
			printf("There's no wrapper for the telescope %s\n", argc[1]);
			return 0;
	}

	/* Handle the Ctr-C signal */
	signal(SIGINT,leave);

	tel->start();
	delete tel;
	return 0;
}
