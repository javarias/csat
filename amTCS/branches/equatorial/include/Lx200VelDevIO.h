#ifndef _LX200_VEL_DEVIO_H_
#define _LX200_VEL_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include <SerialRS232.h>
#include "csatErrors.h"

#ifndef AZIMUTH_AXIS
	#define AZIMUTH_AXIS 0
#endif
#ifndef ALTITUDE_AXIS
	#define ALTITUDE_AXIS 1
#endif

class Lx200VelDevIO: public DevIO<CORBA::Double>
{

	public:
		Lx200VelDevIO(char *deviceName, int axis, bool reversed) 
			throw (csatErrors::CannotOpenDeviceEx);

		virtual ~Lx200VelDevIO();

		CORBA::Double read(ACS::Time &timestamp) 
			throw (ACSErr::ACSbaseExImpl);

		void write(const CORBA::Double &value, ACS::Time &timestap) 
			throw (ACSErr::ACSbaseExImpl);

	private:
		SerialRS232 *sp;	
		int axis;
		double slewRateElevation;
		double slewRateAzimuth;
		bool reversed;
};

#endif /* _LX200_VEL_DEVIO_H_ */
