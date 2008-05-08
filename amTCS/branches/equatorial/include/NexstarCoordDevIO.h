#ifndef _NEXSTAR_COORD_DEVIO_H_
#define _NEXSTAR_COORD_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include <SerialRS232.h>

#include "csatErrors.h"

#define MAX_PRECISE_ROTATION   4294967296.0

#ifndef AZIMUTH_AXIS
	#define AZIMUTH_AXIS 0
#endif
#ifndef ALTITUDE_AXIS
	#define ALTITUDE_AXIS 1
#endif

class NexstarCoordDevIO: public DevIO<CORBA::Double>
{

	public:

	NexstarCoordDevIO(char *deviceName, int axis, bool reversed) throw(csatErrors::CannotOpenDeviceEx);
	virtual ~NexstarCoordDevIO();

	virtual CORBA::Double read(ACS::Time &timestamp)
	              throw (ACSErr::ACSbaseExImpl);

	virtual void write(const CORBA::Double &value, ACS::Time &timestap)
	              throw (ACSErr::ACSbaseExImpl);

	private:
	SerialRS232 *sp;
	int axis;
	bool reversed;
};

#endif /* _NEXSTAR_COORD_DEVIO_H_ */
