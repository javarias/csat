#ifndef _NEXSIM_COORD_DEVIO_H_
#define _NEXSIM_COORD_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include <NexSimS.h>

#include "csatErrors.h"

#define MAX_PRECISE_ROTATION   4294967296.0

#ifndef AZIMUTH_AXIS
	#define AZIMUTH_AXIS 0
#endif
#ifndef ALTITUDE_AXIS
	#define ALTITUDE_AXIS 1
#endif

class NexsimCoordDevIO: public DevIO<CORBA::Double>
{

	public:

	NexsimCoordDevIO(NEXSIM_MODULE::NexSim_var simulator, int axis);

	virtual CORBA::Double read(ACS::Time &timestamp)
	              throw (ACSErr::ACSbaseExImpl);

	virtual void write(const CORBA::Double &value, ACS::Time &timestap)
	              throw (ACSErr::ACSbaseExImpl);

	private:
	NEXSIM_MODULE::NexSim_var m_simulator;
	int axis;
};

#endif /* _NEXSIM_COORD_DEVIO_H_ */
