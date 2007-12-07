#ifndef _NEXSTAR_VEL_DEVIO_H_
#define _NEXSTAR_VEL_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include "SerialRS232.h"
#include "csatErrors.h"

#define MAX_PRECISE_ROTATION   4294967296.0

class NexstarVelDevIO: public DevIO<CORBA::Double>
{

	public:

	NexstarVelDevIO(char *deviceName, int axist) throw (csatErrors::CannotOpenDeviceEx);
	NexstarVelDevIO(void *data);
	virtual ~NexstarVelDevIO();

	virtual bool initializeValue();

	CORBA::Double read(ACS::Time &timestamp)
	              throw (ACSErr::ACSbaseExImpl);

	void write(const CORBA::Double &value, ACS::Time &timestap)
	              throw (ACSErr::ACSbaseExImpl);

	static const int axisAzimuth  = 0;
	static const int axisAltitude = 1;

	private:
	SerialRS232 *sp;
	int axis;
};

#endif /* _NEXSTAR_VEL_DEVIO_H_ */
