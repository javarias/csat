#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <dirent.h>
#include <Communication.h>
#include <Freemode.h>
#include <Telescope.h>
#include <Gps.h>
#include <pthread.h>
#include <sys/types.h>
#include <SDL/SDL.h>
#include <SDL/SDL_ttf.h>



/** The default serial port's device file */
#define DEFAULT_PORT "/dev/ttyACM0"

/** Communication object */
Communication *com = NULL;
Gps *gps = NULL;

char *gps_info;
char *tlc_info;
bool send = false;
bool gps_flag = false;
bool encoder = false;
char answer[40];
int ChkSum;

/**
 * Leaves the program at the SIGINT signal
 * @param sig The signal received
 */ 
void leave(int sig);

ESO50Prms_t *absenc;

int main(int args, char *argv[])
{
	char serialPort[NAME_MAX],*data,stime[15],slatitude[15],slongitude[15],saltitude[15],*orientation,sorientation[1];
	int option,i,checksum,optionHADec;
	pthread_t thread1, thread2;
	bool loop = true;
	char *gps_inf,encodoption;
	int thread_id1, thread_id2;
	int address;
	char msg_aux[32],msg_aux2[32],*msg;
	char message[32];
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
	//gps = new Gps((char *)"/dev/ttyUSB0",true);

	Freemode *freemode = new Freemode(com);
	Telescope *tlc = new Telescope(com);
	tlc->start();

	signal(SIGINT, leave);

	system("clear");
	printf("*************************************************\n");
	printf("################  client eso50  #################\n");
	printf("*************************************************\n");

	while(loop)
	{
		printf("\n\n1.-	Send Message\n");
		printf("2.-	Get Message\n");
		printf("3.-	Get absolute enconders\n");
		printf("4.-	Absolute Encoders\n");
		printf("5.-	Get time(GPS)\n");
		printf("6.-	Get Latitude(GPS)\n");
		printf("7.-	Get Logitude(GPS)\n");
		printf("8.-	Get Altitud(GPS)\n");
		printf("9.-	**FREE MODE**\n");
		printf("10.-	Exit\n\n");
		printf("Seleccione su opcion: ");
		scanf("%i",&option);

		switch(option)
		{
			case 1:
				printf("\nAddress: ");
				scanf("%i",&address);
				printf("\nmessage type (1/0): ");
				scanf("%i",&msg_type);
				if(msg_type == 1)
				{
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

				checksum = com->write(wref,address,msg_type,run,side,pi,"hola");
				}
				else if(msg_type == 0)
				{
					printf("\nESCRIBA EL MENSAJE A ENVIAR\n");
					scanf("%s",msg);
					checksum = com->write(0,address,msg_type,0,0,0,msg);
				}

				break;	
			case 2:
				if(send)
				{
					printf("\n");
					if(answer[5] == 1)
					for(i=0;i<40;i++) printf("%i ",answer[i]);
					else 
					{
						for(i=6;i<38;i++) message[i-6] = answer[i];
						printf("%s ",message);
					}
				}
				else printf("\nno message\n");
				send=false;
				break;
			case 3:
				printf("\n(r) Receive absolute enconders");
				printf("\n(s) Stop absolute enconders\n");
				scanf("%i",&option);
				if(option == 1) 
				{
					com->sendData(1);	
				}
				else com->sendData(0);

				break;
			case 4:
				printf("\nHAAxis : %lf\n",tlc->getCoordRa());
				printf("DecAxis : %lf\n",tlc->getCoordDec());
				break;
			case 5:
		
				
				/*data = gps->getGdata(gps_info,0);
				for(i=0;i<15;i++) stime[i] = data[i];
				printf("\n");
				for(i=0;i<6;i+=2)
				{
					printf("%c%c",stime[i],stime[i+1]);
					if(i!=4)printf(":");
				}
				printf("\n");*/
				break;
			case 6:
				//printf("latitude : %lf\n",gps->getLatitude());
				/*data = gps->getGdata(gps_info,1);
				for(i=0;i<15;i++) slatitude[i] = data[i];
				latitude = gps->strtodou(slatitude);
				orientation = gps->getGdata(gps_info,4);
				for(i=0;i<15;i++) sorientation[i] = orientation[i];
				seg = (latitude-(double)(int)latitude)*60;
				min = (int)latitude%100;
				deg = ((int)latitude-min)/100;
				printf("\n%d° %d' %.2lf'' ",deg,min,seg);
				printf("%c\n",sorientation[0]);*/
				break;
			case 7:
				//printf("longitude : %lf\n",gps->getLongitude());
				/*
				data = gps->getGdata(gps_info,2);
				for(i=0;i<15;i++) slongitude[i] = data[i];
				longitude = gps->strtodou(slongitude);
				orientation = gps->getGdata(gps_info,5);
				for(i=0;i<15;i++) sorientation[i] = orientation[i];
				seg = (longitude - (double)(int)longitude)*60;
				min = (int)longitude%100;
				deg = ((int)longitude-min)/100;
				printf("\n%d°%d'%.2lf''",deg,min,seg);
				printf("%s\n",sorientation);*/
				break;
			case 8:
				//printf("altitude : %ld\n",gps->getAltitude());
				/*
				data = gps->getGdata(gps_info,3);
				for(i=0;i<15;i++) saltitude[i] = data[i];
				printf("\n%s M\n",saltitude);*/
				break;
			case 9:
				freemode->start();
				break;
			case 10:
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
