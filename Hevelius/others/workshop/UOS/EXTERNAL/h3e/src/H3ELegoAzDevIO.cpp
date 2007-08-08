/*
 * "@(#) $Id: H3ELegoAzDevIO.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: H3ELegoAzDevIO.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#include "H3ELegoAzDevIO.h"
#include "SCTdefs.h"
#include "SCTlibs.h"

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include <ACSErrTypeCommon.h>

/*
 * LegoAzDevIO base class.
 */


/**
 * Constructor. Nothing to de here.
 */
LegoAzDevIO::LegoAzDevIO()
{
}


/**
 * Constructor with some data. Nothing to do here.
 */
LegoAzDevIO::LegoAzDevIO(void *data)
{
}


/**
 * Nothing to destroy.
 */
LegoAzDevIO::~LegoAzDevIO()
{
   sct_close_lego();
}


/**
 * This value does not need initialization.
 */
bool LegoAzDevIO::initializeValue()
{
	return true;
}


/**
 * Reads actual telescope's azimuth encoder value and converts it to a degree value.
 * @param timestamp Time reference to store reading's timestamp.
 * @return Actual azimuth value, in degrees.
 */
CORBA::Double LegoAzDevIO::read(ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl)
{
        CORBA::Double ret_val(0.0);
        if (sct_open_lego() < 0) {
	   ACS_SHORT_LOG((LM_ERROR, "LegoAztDevIO::read: could not access Lego USB tower device file. Check RW permissions."));
//	   throw ACSErrTypeCommon::IOErrorExImpl(__FILE__, __LINE__, "LegoAzDevIO::read");
	}
	
	ret_val=(CORBA::Double) sct_get_variable(RCX_REAL_AZIMUTH);
	timestamp=getTimeStamp();

        return ret_val;
}


/**
 * This method should never be called.
 */
void LegoAzDevIO::write(const CORBA::Double& value, ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl)
{
        // Should an exception be thrown?
	ACS_SHORT_LOG((LM_ERROR, "LegoAzDevIO::write: This method should never be called!"));
	return;
}

