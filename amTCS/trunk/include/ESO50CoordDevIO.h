#ifndef _ESO50_COORD_DEVIO_H_
#define _ESO50_COORD_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include <SerialRS232.h>

#include "csatErrors.h"
#include "BufferThread.h"

#ifndef AZIMUTH_AXIS
	#define AZIMUTH_AXIS 164
#endif
#ifndef ALTITUDE_AXIS
	#define ALTITUDE_AXIS 162
#endif

typedef struct 
{
     unsigned short int Target_HAAxis;
     unsigned short int Target_HAWorm;
     unsigned short int Target_DecAxis;
     unsigned short int Target_DecWorm;
     unsigned short int KpHA;
     unsigned short int KiHA;
     unsigned short int KdHA_Lo;
     unsigned short int KdHA_Hi;
     unsigned short int KpDec;
     unsigned short int KiDec;
     unsigned short int KdDec_Lo;
     unsigned short int KdDec_Hi;
} ESO50Prms_t;

class ESO50CoordDevIO: public DevIO<CORBA::Double>
{
	private:
		SerialRS232 *sp;
		ESO50Prms_t *ESO50Prms;
		int axis;
		BufferThread *thread_p;
	public:

		ESO50CoordDevIO(char *deviceName, int axis, BufferThread *thread_p)
			throw(csatErrors::CannotOpenDeviceEx);

		virtual ~ESO50CoordDevIO();

		void msg2send(int msg_type, int option);

		virtual CORBA::Double read(ACS::Time &timestamp) 
			throw (ACSErr::ACSbaseExImpl);

		virtual void write(const CORBA::Double &value, ACS::Time &timestap) 
			throw (ACSErr::ACSbaseExImpl);

};

#endif /* _ESO50_COORD_DEVIO_H_ */
