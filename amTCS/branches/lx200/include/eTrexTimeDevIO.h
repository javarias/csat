#ifndef _ETREX_TIME_DEVIO_H_
#define _ETREX_TIME_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

class eTrexTimeDevIO: public DevIO<CORBA::LongLong>
{

	public:

	virtual CORBA::LongLong read(ACS::Time &timestamp)
	              throw (ACSErr::ACSbaseExImpl);

	virtual void write(const CORBA::LongLong &value, ACS::Time &timestap)
	              throw (ACSErr::ACSbaseExImpl);

};

#endif /* _ETREX_TIME_DEVIO_H_ */
