#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <dirent.h>
#include <Communication.h>
#include <pthread.h>
#include <sys/types.h>

#include <SDL/SDL.h>
#include <SDL/SDL_ttf.h>


class Freemode
{
	public:
		Freemode(Communication *com);
		virtual ~Freemode();
		int start();
		virtual void run();
	private:
		Communication *com;
		static void * gate(void *);
		pthread_t _tid;
};
