#ifndef _NEXSTAR_AZM_DEVIO_H_
#define _NEXSTAR_AZM_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include "SerialRS232.h"
#include "csatErrors.h"

#define MAX_PRECISE_ROTATION   4294967296.0

class NexstarAzmDevIO: public DevIO<CORBA::Double>
{

	public:

	NexstarAzmDevIO(char *deviceName) throw (csatErrors::CannotOpenDeviceEx);
	NexstarAzmDevIO(void *data);
	virtual ~NexstarAzmDevIO();

	virtual bool initializeValue();

	CORBA::Double read(ACS::Time &timestamp)
	              throw (ACSErr::ACSbaseExImpl);

	void write(const CORBA::Double &value, ACS::Time &timestap)
	              throw (ACSErr::ACSbaseExImpl);

	private:
	SerialRS232 *sp;
};

#endif /* _NEXSTAR_AZM_DEVIO_H_ */
