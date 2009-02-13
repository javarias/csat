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
#include "freemode.cpp"


/** The default serial port's device file */
#define DEFAULT_PORT "/dev/ttyACM0"

/** Communication object */
Communication *com = NULL;
Communication *gps = NULL;

char *gps_info;
char *telescope_info0,*la,*lo,*al;
char *telescope_info1;
char *info;
bool send = false;
bool gps_flag = false;

/**
 * Leaves the program at the SIGINT signal
 * @param sig The signal received
 */ 
void leave(int sig);

typedef struct 
{
     short int HAAxis;
     short int HAWorm;
     short int DecAxis;
     short int DecWorm;
} ESO50AbsEnc_t;
		
void* readGps(void* pcom)
{
	while(1)
	{
		gps_info = gps->readFrom();
	}
	pthread_exit(NULL);
}

int main(int args, char *argv[])
{
	char serialPort[NAME_MAX],*data,stime[15],slatitude[15],slongitude[15],saltitude[15],*orientation,sorientation[1];
	int option,i,checksum;
	pthread_t thread1, thread2;
	bool loop = true, free = true;
	char *gps_inf;
	int thread_id1, thread_id2;
	int address;
	char msg_aux1[32],msg_aux2[32],*mensaje;
	int time,msg_type;
	int deg, min;
	double seg;

	float wref;
	int run,side,pi,running=1;

	double latitude, longitude, altitude;

	printf("Using default port %s.\n", DEFAULT_PORT);
	printf("Using default port %s.\n", "/dev/ttyUSB0");
	strcpy(serialPort, DEFAULT_PORT);
	setbuf(stdout,NULL);
	com = new Communication(serialPort);
	//gps = new Communication("/dev/ttyUSB0");
	ESO50AbsEnc_t* test2;
	signal(SIGINT, leave);

	//thread_id1 = pthread_create(&thread1, NULL, readTelescope,(void *) serialPort);
	//thread_id2 = pthread_create(&thread2, NULL, readGps,(void *) serialPort);

	system("clear");
	printf("*************************************************\n");
	printf("################  client eso50  #################\n");
	printf("*************************************************\n");
	while(loop)
	{
		printf(" \n\n1.-	Send Message\n");
		printf("2.-	Get Message\n");
		printf("3.-	Absolute Encoders\n");
		printf("4.-	Get time(GPS)\n");
		printf("5.-	Get Latitude(GPS)\n");
		printf("6.-	Get Logitude(GPS)\n");
		printf("7.-	Get Altitud(GPS)\n");
		printf("8.-	**FREE MODE**\n");
		printf("9.-	Exit\n\n");
		printf("Seleccione su opcion: ");
		scanf("%i",&option);
		//endwin();
		switch(option)
		{
			case 1:
				printf("\nAddress: ");
				scanf("%i",&address);
				printf("\nmessage type (1/0): ");
				scanf("%i",&msg_type);

				printf("\nwref (>20): ");
				scanf("%f",&wref);
				while(wref<20)
				{
					printf("wred debe ser mayor que 20\n");
					scanf("%f",&wref);
				}
				printf("\nrun (1/0): ");
				scanf("%i",&run);
				printf("\nside  (1/0): ");
				scanf("%i",&side);
				printf("\npi (1/0): ");
				scanf("%i",&pi);

				checksum = com->writeTo(wref,address,msg_type,run,side,pi);

				break;
			case 3:
				mensaje = com->readFrom();

				for(i=6; i<38; i++) msg_aux1[i-6]=mensaje[i];
				test2 = (ESO50AbsEnc_t*) msg_aux1;
				printf("\nHAAxis : %d\n",test2->HAAxis);
				printf("HAWorm : %d\n",test2->HAWorm);
				printf("DecAxis : %d\n",test2->DecAxis);
				printf("DecWorm : %d\n",test2->DecWorm);
				break;
			case 4:
				data = gps->getGdata(gps_info,0);
				for(i=0;i<15;i++) stime[i] = data[i];
				printf("\n");
				for(i=0;i<6;i+=2)
				{
					printf("%c%c",stime[i],stime[i+1]);
					if(i!=4)printf(":");
				}
				printf("\n");
				break;
			case 5:
				data = gps->getGdata(gps_info,1);
				for(i=0;i<15;i++) slatitude[i] = data[i];
				latitude = gps->strtodou(slatitude);
				orientation = gps->getGdata(gps_info,4);
				for(i=0;i<15;i++) sorientation[i] = orientation[i];
				seg = (latitude-(double)(int)latitude)*60;
				min = (int)latitude%100;
				deg = ((int)latitude-min)/100;
				printf("\n%d° %d' %.2lf'' ",deg,min,seg);
				printf("%c\n",sorientation[0]);
				break;
			case 6:
				data = gps->getGdata(gps_info,2);
				for(i=0;i<15;i++) slongitude[i] = data[i];
				longitude = gps->strtodou(slongitude);
				orientation = gps->getGdata(gps_info,5);
				for(i=0;i<15;i++) sorientation[i] = orientation[i];
				seg = (longitude - (double)(int)longitude)*60;
				min = (int)longitude%100;
				deg = ((int)longitude-min)/100;
				printf("\n%d°%d'%.2lf''",deg,min,seg);
				printf("%s\n",sorientation);
				break;
			case 7:
				data = gps->getGdata(gps_info,3);
				for(i=0;i<15;i++) saltitude[i] = data[i];
				printf("\n%s M\n",saltitude);
				break;
		
			case 2:
				data = com->readFrom();
				printf("\n");
				for(i=0;i<40;i++)printf("%i ",data[i]);
				break;
			
			case 8:
				freemode(com);
				break;
			case 9:
				loop=false;
				break;
		}
	}

	delete com;
	printf("Exiting cli_eso50...\n");
	exit(EXIT_SUCCESS);
}

void leave(int sig)
{
	printf("Receiving SIGINT signal... leaving now\n");
	delete com;
	printf("Exiting cli_eso50...\n");
	exit(EXIT_SUCCESS);
}
