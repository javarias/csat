#ifndef LegoCmdAltDevIO_h
#define LegoCmdAltDevIO_h

/*
 * "@(#) $Id: H3ELegoCmdAltDevIO.h,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 * 
 * $Log: H3ELegoCmdAltDevIO.h,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#include <baciDevIO.h>
#include <acstime.h>

/*
 * Definition of LegoCmdAltDevIO base class.
 */



class LegoCmdAltDevIO: public DevIO<CORBA::Double>
{
	public:
		LegoCmdAltDevIO();
		LegoCmdAltDevIO(void *data);
		virtual ~LegoCmdAltDevIO();

		virtual bool initializeValue();
		CORBA::Double   read(ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl);
		void         write(const CORBA::Double& value, ACS::Time& timestamp) 
			                                    throw (ACSErr::ACSbaseExImpl);

	private:
	
};
#endif

