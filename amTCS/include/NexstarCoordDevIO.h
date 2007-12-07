#ifndef _NEXSTAR_COORD_DEVIO_H_
#define _NEXSTAR_COORD_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include <SerialRS232.h>

#include "csatErrors.h"

#define MAX_PRECISE_ROTATION   4294967296.0

class NexstarCoordDevIO: public DevIO<CORBA::Double>
{

	public:

	NexstarCoordDevIO(char *deviceName, int axis) throw(csatErrors::CannotOpenDeviceEx);
	virtual ~NexstarCoordDevIO();

	virtual CORBA::Double read(ACS::Time &timestamp)
	              throw (ACSErr::ACSbaseExImpl);

	virtual void write(const CORBA::Double &value, ACS::Time &timestap)
	              throw (ACSErr::ACSbaseExImpl);

	static const int axisAzimuth  = 0;
	static const int axisAltitude = 1;

	private:
	SerialRS232 *sp;
	int axis;
};

#endif /* _NEXSTAR_COORD_DEVIO_H_ */
