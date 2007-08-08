#ifndef LegoAzDevIO_h
#define LegoAzDevIO_h

/*
 * "@(#) $Id: H3ELegoAzDevIO.h,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 * 
 * $Log: H3ELegoAzDevIO.h,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#include <baciDevIO.h>
#include <acstime.h>

/*
 * Definition of LegoAzDevIO base class.
 */



class LegoAzDevIO: public DevIO<CORBA::Double>
{
	public:
		LegoAzDevIO();
		LegoAzDevIO(void *data);
		virtual ~LegoAzDevIO();

		virtual bool initializeValue();
		CORBA::Double   read(ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl);
		void         write(const CORBA::Double& value, ACS::Time& timestamp) 
			                                    throw (ACSErr::ACSbaseExImpl);

	private:
	
};
#endif

