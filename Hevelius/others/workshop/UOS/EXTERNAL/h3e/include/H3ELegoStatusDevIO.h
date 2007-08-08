#ifndef LegoStatusDevIO_h
#define LegoStatusDevIO_h

/*
 * "@(#) $Id: H3ELegoStatusDevIO.h,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 * 
 * $Log: H3ELegoStatusDevIO.h,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#include <baciDevIO.h>
#include <acstime.h>

/*
 * Definition of LegoStatusDevIO base class.
 */



class LegoStatusDevIO: public DevIO<ACS::pattern>
{
	public:
		LegoStatusDevIO();
		LegoStatusDevIO(void *data);
		virtual ~LegoStatusDevIO();

		virtual bool initializeValue();
		ACS::pattern   read(ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl);
		void         write(const ACS::pattern& value, ACS::Time& timestamp) 
			                                    throw (ACSErr::ACSbaseExImpl);

	private:
	
};
#endif

