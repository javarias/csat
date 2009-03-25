#ifndef _ESO50_COORD_DEVIO_H_
#define _ESO50_COORD_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#include <SerialRS232.h>

#include "csatErrors.h"
#include <BufferThread.h>

#ifndef AZIMUTH_AXIS
	#define AZIMUTH_AXIS 164
#endif
#ifndef ALTITUDE_AXIS
	#define ALTITUDE_AXIS 162
#endif

/*typedef struct
{
     short int Current_HAAxis;
     short int Current_HAWorm;
     short int Current_DecAxis;
     short int Current_DecWorm;
     unsigned int Current_HA;
     unsigned int Current_Dec;

} ESO50Stat_t;*/

class ESO50CoordDevIO: public DevIO<CORBA::Double>
{
	private:
		SerialRS232 *sp;
		//ESO50Stat_t *ESO50Stat;
		int axis;
		//BufferThread *thread_p;
		bool receiving;
		bool reversed;
		double value;
		BufferThread *thread_p;
	public:
		ESO50CoordDevIO(char *deviceName, int axis, BufferThread *thread_p, bool reversed = true)
			throw(csatErrors::CannotOpenDeviceEx);

		virtual ~ESO50CoordDevIO();

		void msg2send(int msg_type, int option);

		virtual CORBA::Double read(ACS::Time &timestamp) 
			throw (ACSErr::ACSbaseExImpl);

		virtual void write(const CORBA::Double &value, ACS::Time &timestap) 
			throw (ACSErr::ACSbaseExImpl);

};

#endif /* _ESO50_COORD_DEVIO_H_ */
