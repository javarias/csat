#ifndef _NEXSTAR_ALT_DEVIO_H_
#define _NEXSTAR_ALT_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include "SerialRS232.h"

#define MAX_PRECISE_ROTATION   4294967296.0

class NexstarAltDevIO: public DevIO<CORBA::Double>
{

	public:

	NexstarAltDevIO(char *deviceName);
	NexstarAltDevIO(void *data);
	virtual ~NexstarAltDevIO();

	virtual bool initializeValue();

	virtual CORBA::Double read(ACS::Time &timestamp)
	              throw (ACSErr::ACSbaseExImpl);

	virtual void write(const CORBA::Double &value, ACS::Time &timestap)
	              throw (ACSErr::ACSbaseExImpl);

	private:
	SerialRS232 *sp;
};

#endif /* _NEXSTAR_ALT_DEVIO_H_ */
