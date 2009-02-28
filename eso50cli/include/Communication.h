/** 
 * \file Communication.h
 * Headers to implement the communication with the ESO 50 telescope.
 *
 * \author Daniel Bustamante
 */

#ifndef _COMMUNICATION_H_
#define _COMMUNICATION_H_

#include <SerialRS232.h>

using namespace std;

typedef struct 
{
    char        Tm;
    char        MtrCtrl;
    unsigned short int   Tmr0;
    short int   Vfin;
    short int   Wref_Lo;
    short int   Wref_Hi;
    short int   Ki_Lo;
    short int   Ki_Hi;
    short int   Kp_Lo;
    short int   Kp_Hi;
} SlavePWM_t;

typedef struct 
{
     unsigned short int Target_HA_Lo;
     unsigned short int Target_HA_Hi;
     unsigned short int Target_Dec_Lo;
     unsigned short int Target_Dec_Hi;
     unsigned short int KpHA_Lo;
     unsigned short int KpHA_Hi;
     unsigned short int KiHA_Lo;
     unsigned short int KiHA_Hi;
     unsigned short int KdHA_Lo;
     unsigned short int KdHA_Hi;
     unsigned short int KpDec_Lo;
     unsigned short int KpDec_Hi;
     unsigned short int KiDec_Lo;
     unsigned short int KiDec_Hi;
     unsigned short int KdDec_Lo;
     unsigned short int KdDec_Hi;

} ESO50Prms_t;

typedef struct
{
     short int Current_HAAxis;
     short int Current_HAWorm;
     short int Current_DecAxis;
     short int Current_DecWorm;
     unsigned int Current_HA;
     unsigned int Current_Dec;

} ESO50Stat_t;

/**
 * Class that handles comunication.
 */
class Communication{

	private:
		SerialRS232 *sp;
		char *device;

	public:
		/** 
		 * Constructor
		 * Set AltAz alignment mode. 
		 * @param deviceName The serial port device
		 */
		Communication(char *deviceName);

		/** Destructor */
		~Communication();

		/** Check telescope connection */
		char *checkConnection();
		/** Get Telescope Altitude */
		

		/** Get Telescope Azimuth */
		double getAzimuth();

		/** Get Telescope Declination */
		void getDeclination(int *dec);

		/** Get Telescope RA */
		void getRA(int *ra);

		/** 
		 * Get the Sidereal Time 
		 * @param *stime Array for time values (HH:MM:SS)
		 */
	        
		char* read();
		//char* readGps();
		char* getTime(char *mensaje);

		double strtodou(char* msg);
                int write(float wref, int address, int msg_type, int run, int side, int pi, char* msg);
		void sendData(int option);
};

#endif
