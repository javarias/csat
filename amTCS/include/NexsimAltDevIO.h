#ifndef _NEXSIM_ALT_DEVIO_H_
#define _NEXSIM_ALT_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include "SerialRS232.h"
#include "csatErrors.h"

#define MAX_PRECISE_ROTATION   4294967296.0

class NexsimAltDevIO: public DevIO<CORBA::Double>
{

	public:

	NexsimAltDevIO(char *deviceName) throw(csatErrors::CannotOpenDeviceEx);
	virtual ~NexsimAltDevIO();

	virtual bool initializeValue();

	virtual CORBA::Double read(ACS::Time &timestamp)
	              throw (ACSErr::ACSbaseExImpl);

	virtual void write(const CORBA::Double &value, ACS::Time &timestap)
	              throw (ACSErr::ACSbaseExImpl);

	private:
	SerialRS232 *sp;
};

#endif /* _NEXSIM_ALT_DEVIO_H_ */