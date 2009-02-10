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
	        
		char* readFrom();
		//char* readGps();
		char* getTime(char *mensaje);
		char* readTelescope();
		char* getGdata(char *mensaje, int option);
		char* getLongitude(char *mensaje);
		double strtodou(char* msg);
		double getAltitude();
                int writeTo(int direction, int msg_type);
};

#endif
