#ifndef _ETREX_COORD_DEVIO_H_
#define _ETREX_COORD_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include "eTrexCommunication.h"
#include "csatErrors.h"

class eTrexCoordDevIO: public DevIO<CORBA::Double>
{

	public:

	eTrexCoordDevIO(char *deviceName, int axis) throw(csatErrors::CannotOpenDeviceEx);
	virtual ~eTrexCoordDevIO();

	virtual CORBA::Double read(ACS::Time &timestamp)
	              throw (ACSErr::ACSbaseExImpl);

	virtual void write(const CORBA::Double &value, ACS::Time &timestap)
	              throw (ACSErr::ACSbaseExImpl);

	private:
	eTrexCommunication *comm;
	int axis;
};

#endif /* _ETREX_COORD_DEVIO_H_ */
