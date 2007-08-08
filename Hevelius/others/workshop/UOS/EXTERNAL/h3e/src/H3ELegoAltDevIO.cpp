/*
 * "@(#) $Id: H3ELegoAltDevIO.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: H3ELegoAltDevIO.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#include "H3ELegoAltDevIO.h"
#include "SCTdefs.h"
#include "SCTlibs.h"

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include <ACSErrTypeCommon.h>
#include <ACSErrTypeDevIO.h>

/*
 * LegoAltDevIO base class.
 */


/**
 * Constructor. Nothing to de here.
 */
LegoAltDevIO::LegoAltDevIO()
{
}

/**
 * Constructor with some data. Nothing to do here.
 */
LegoAltDevIO::LegoAltDevIO(void *data)
{
}

/**
 * Nothing to destroy.
 */
LegoAltDevIO::~LegoAltDevIO()
{
   sct_close_lego();
}


/**
 * This value does not need initialization.
 */
bool LegoAltDevIO::initializeValue()
{
	return true;
}


/**
 * Reads actual telescope's altitude encoder value and converts it to a degree value.
 * @param timestamp Time reference to store reading's timestamp.
 * @return Actual altitude value, in degrees.
 */
CORBA::Double LegoAltDevIO::read(ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl)
{
        CORBA::Double ret_val(0.0);
        
        if (sct_open_lego() < 0) {
	   ACS_SHORT_LOG((LM_ERROR, "LegoAltDevIO::read: could not access Lego USB tower device file. Check RW permissions."));
	//   throw ACSErrTypeCommon::IOErrorExImpl(__FILE__, __LINE__, "LegoAltDevIO::read");
	}
	
	ret_val=(CORBA::Double)sct_get_variable(RCX_REAL_ALTITUDE);
	timestamp=getTimeStamp();

	if (ret_val < -5.0 || ret_val > 185.0) {
		ACS_SHORT_LOG((LM_ERROR, "LegoAltDevIO::read: read value is out of bounds."));
	//	throw ACSErrTypeCommon::OutOfBoundsExImpl(__FILE__, __LINE__, "LegoAltDevIO::read");
	}

        return ret_val;
}


/**
 * This method should never be called.
 */
void LegoAltDevIO::write(const CORBA::Double& value, ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	ACS_SHORT_LOG((LM_ERROR, "LegoAltDevIO::write: This method should never be called!"));
	//throw ACSErrTypeDevIO::UnimplementedWriteExImpl(__FILE__, __LINE__, "LegoAltDevIO::write");
	return;
}

