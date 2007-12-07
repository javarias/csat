#ifndef _NEXSIM_COORD_DEVIO_H_
#define _NEXSIM_COORD_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include <NexSimS.h>

#include "csatErrors.h"

#define MAX_PRECISE_ROTATION   4294967296.0

class NexsimCoordDevIO: public DevIO<CORBA::Double>
{

	public:

	NexsimCoordDevIO(NEXSIM_MODULE::NexSim_var simulator, int axis);
	virtual ~NexsimCoordDevIO();

	virtual bool initializeValue();

	CORBA::Double read(ACS::Time &timestamp)
	              throw (ACSErr::ACSbaseExImpl);

	void write(const CORBA::Double &value, ACS::Time &timestap)
	              throw (ACSErr::ACSbaseExImpl);

	/* Constants for konw wich axis will this DevIO controlling */
	static const int axisAzimuth  = 0;
	static const int axisAltitude = 1;

	private:
	NEXSIM_MODULE::NexSim_var m_simulator;
	int axis;
};

#endif /* _NEXSIM_COORD_DEVIO_H_ */
