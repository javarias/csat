#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <dirent.h>
#include <Communication.h>
#include <pthread.h>
#include <sys/types.h>

class Telescope
{
	public:
		Telescope(Communication *com);
		virtual ~Telescope();
		int start();
		virtual void run();
		double getCoordRa();
		double getCoordDec();

	private:
		Communication *com;
		double coordRa;
		double coordDec;
		static void * gate(void *);
		pthread_t _tid;
};
