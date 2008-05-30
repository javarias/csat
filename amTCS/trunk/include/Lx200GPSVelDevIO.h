#ifndef _LX200GPS_VEL_DEVIO_H_
#define _LX200GPS_VEL_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include <SerialRS232.h>
#include "csatErrors.h"

#ifndef AZIMUTH_AXIS
	#define AZIMUTH_AXIS 0
#endif
#ifndef ALTITUDE_AXIS
	#define ALTITUDE_AXIS 1
#endif

class Lx200GPSVelDevIO: public DevIO<CORBA::Double>
{

	public:
		Lx200GPSVelDevIO(char *deviceName, int axist) 
			throw (csatErrors::CannotOpenDeviceEx);

		virtual ~Lx200GPSVelDevIO();

		CORBA::Double read(ACS::Time &timestamp) 
			throw (ACSErr::ACSbaseExImpl);

		void write(const CORBA::Double &value, ACS::Time &timestap) 
			throw (ACSErr::ACSbaseExImpl);

	private:
		SerialRS232 *sp;	
		int axis;
		double slewRateElevation;
		double slewRateAzimuth;
};

#endif /* _LX200GPS_VEL_DEVIO_H_ */
