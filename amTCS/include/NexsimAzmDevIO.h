#ifndef _NEXSIM_AZM_DEVIO_H_
#define _NEXSIM_AZM_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include "SerialRS232.h"
#include "csatErrors.h"

#define MAX_PRECISE_ROTATION   4294967296.0

class NexsimAzmDevIO: public DevIO<CORBA::Double>
{

	public:

	NexsimAzmDevIO(char *deviceName) throw (csatErrors::CannotOpenDeviceEx);
	NexsimAzmDevIO(void *data);
	virtual ~NexsimAzmDevIO();

	virtual bool initializeValue();

	CORBA::Double read(ACS::Time &timestamp)
	              throw (ACSErr::ACSbaseExImpl);

	void write(const CORBA::Double &value, ACS::Time &timestap)
	              throw (ACSErr::ACSbaseExImpl);

	private:
	SerialRS232 *sp;
};

#endif /* _NEXSIM_AZM_DEVIO_H_ */
