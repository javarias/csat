#ifndef _ESO50_VEL_DEVIO_H_
#define _ESO50_VEL_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include <SerialRS232.h>
#include "csatErrors.h"

#ifndef AZIMUTH_AXIS
	#define AZIMUTH_AXIS 164
#endif
#ifndef ALTITUDE_AXIS
	#define ALTITUDE_AXIS 162
#endif

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

class ESO50VelDevIO: public DevIO<CORBA::Double>
{

	public:
		ESO50VelDevIO(char *deviceName, int axist) 
			throw (csatErrors::CannotOpenDeviceEx);

		virtual ~ESO50VelDevIO();

		CORBA::Double read(ACS::Time &timestamp) 
			throw (ACSErr::ACSbaseExImpl);

		unsigned short bytefix(float data,int i);

		void write(const CORBA::Double &value, ACS::Time &timestap) 
			throw (ACSErr::ACSbaseExImpl);

	private:
		SerialRS232 *sp;	
		int axis;
		double slewRateDeclination;
		double slewRateHA;
};

#endif /* _ESO50_VEL_DEVIO_H_ */
