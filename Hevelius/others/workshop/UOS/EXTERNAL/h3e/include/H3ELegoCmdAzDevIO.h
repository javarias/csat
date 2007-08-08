#ifndef LegoCmdAzDevIO_h
#define LegoCmdAzDevIO_h

/*
 * "@(#) $Id: H3ELegoCmdAzDevIO.h,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 * 
 * $Log: H3ELegoCmdAzDevIO.h,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#include <baciDevIO.h>
#include <acstime.h>

/*
 * Definition of LegoCmdAzDevIO base class.
 */



class LegoCmdAzDevIO: public DevIO<CORBA::Double>
{
	public:
		LegoCmdAzDevIO();
		LegoCmdAzDevIO(void *data);
		virtual ~LegoCmdAzDevIO();

		virtual bool initializeValue();
		CORBA::Double   read(ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl);
		void         write(const CORBA::Double& value, ACS::Time& timestamp) 
			                                    throw (ACSErr::ACSbaseExImpl);

	private:
	
};
#endif

