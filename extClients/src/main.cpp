#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "TelTypes.h"
#include "Telescope.h"
#include "Nexstar.h"

using namespace std;

extern char telType[100][10];

int main(int argv, char **argc)
{
	int i = 0;
	if(argv < 2)
		return -1;
	initTelTypes();
	while(strcmp(argc[1],telType[i]) && i < 2)
		i++;
	Telescope *tel = NULL;
	switch(i)
	{
		case 0:
			tel = (Telescope *)new Nexstar();
			printf("%s Wrapper Acquired\n", argc[1]);
			break;
		case 1:
			//tel = new Lx200();
			printf("%s Wrapper Acquired\n", argc[1]);
			break;
		default:
			tel = NULL;
			printf("There's no wrapper for the telescope %s\n", argc[1]);
			return 0;
	}

	tel->start();
	delete tel;
	return 0;
}
