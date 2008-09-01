#ifndef _LX200_GPSCOORD_DEVIO_H_
#define _LX200_GPSCOORD_DEVIO_H_

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

class Lx200GPSCoordDevIO: public DevIO<CORBA::Double>
{
	private:
		SerialRS232 *sp;
		int axis;

	public:

		Lx200GPSCoordDevIO(char *deviceName, int axis) 
			throw(csatErrors::CannotOpenDeviceEx);

		virtual ~Lx200GPSCoordDevIO();

		double sexa2double(const char *sexaStr);

		virtual CORBA::Double read(ACS::Time &timestamp) 
			throw (ACSErr::ACSbaseExImpl);

		virtual void write(const CORBA::Double &value, ACS::Time &timestap) 
			throw (ACSErr::ACSbaseExImpl);

};

#endif /* _LX200_GPSCOORD_DEVIO_H_ */
