#ifndef _NEXSTARALTDEVIO_H_
#define _NEXSTARALTDEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

class NexstarAltDevIO: public DevIO<CORBA::Double>
{

	public:

	LegoAltDevIO();
	LegoAltDevIO(void *data);
	virtual ~LegoAltDevIO();

	virtual bool initializeValue();

	CORBA::Double read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl);
	void          write(const CORBA::Double &value, ACS::Time &timestap)
	                                         throw (ACS::ACSbaseExImpl);

};

#endif /* _NEXSTARALTDEVIO_H_ */
