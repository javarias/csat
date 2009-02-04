#ifndef _ESO50_COORD_DEVIO_H_
#define _ESO50_COORD_DEVIO_H_

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

class ESO50CoordDevIO: public DevIO<CORBA::Double>
{
	private:
		SerialRS232 *sp;
		int axis;

	public:

		ESO50CoordDevIO(char *deviceName, int axis)
			throw(csatErrors::CannotOpenDeviceEx);

		virtual ~ESO50CoordDevIO();

		double sexa2double(const char *sexaStr);

		virtual CORBA::Double read(ACS::Time &timestamp) 
			throw (ACSErr::ACSbaseExImpl);

		virtual void write(const CORBA::Double &value, ACS::Time &timestap) 
			throw (ACSErr::ACSbaseExImpl);

};

#endif /* _ESO50_COORD_DEVIO_H_ */
