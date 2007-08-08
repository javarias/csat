#ifndef LegoAltDevIO_h
#define LegoAltDevIO_h

/*
 * "@(#) $Id: H3ELegoAltDevIO.h,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 * 
 * $Log: H3ELegoAltDevIO.h,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#include <baciDevIO.h>
#include <acstime.h>

/*
 * Definition of LegoAltDevIO base class.
 */



class LegoAltDevIO: public DevIO<CORBA::Double>
{
	public:
		LegoAltDevIO();
		LegoAltDevIO(void *data);
		virtual ~LegoAltDevIO();

		virtual bool initializeValue();
		CORBA::Double   read(ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl);
		void         write(const CORBA::Double& value, ACS::Time& timestamp) 
			                                    throw (ACSErr::ACSbaseExImpl);

	private:
	
};
#endif

