/*
 * "@(#) $Id: H3ELegoCmdAltDevIO.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: H3ELegoCmdAltDevIO.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#include "H3ELegoCmdAltDevIO.h"
#include "SCTdefs.h"
#include "SCTlibs.h"

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include <ACSErrTypeCommon.h>

/*
 * LegoCmdAltDevIO base class.
 */


/*
 * Nothing to construct here
 */
LegoCmdAltDevIO::LegoCmdAltDevIO()
{
}


LegoCmdAltDevIO::LegoCmdAltDevIO(void *data)
{
}


/*
 * And nothing to destroy.
 */
LegoCmdAltDevIO::~LegoCmdAltDevIO()
{
   sct_close_lego();
}


/**
 * Sets the commanded altitude to actual telescope's altitude.
 * NOT IMPLEMENTED YET!
 */
bool LegoCmdAltDevIO::initializeValue()
{
	return true;
}


/**
 * Reads the commanded altitude from Lego telescope.
 * NOT IMPLEMENTED
 */
CORBA::Double LegoCmdAltDevIO::read(ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl)
{
        CORBA::Double ret_val(0.0);
        
        if (sct_open_lego() < 0) {
	   ACS_SHORT_LOG((LM_ERROR, "LegoCmdAltDevIO::read: could not access Lego USB tower device file. Check RW permissions."));
	//   throw ACSErrTypeCommon::IOErrorExImpl(__FILE__, __LINE__, "LegoCmdAltDevIO::read");
	}
	
	ret_val=(CORBA::Double) sct_get_variable(RCX_COMM_ALTITUDE);
	timestamp=getTimeStamp();

        return ret_val;
}


/**
 * Sets the commanded altitude to Lego telescope.
 */
void LegoCmdAltDevIO::write(const CORBA::Double& value, ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl)
{
        if (sct_open_lego() < 0) {
	   ACS_SHORT_LOG((LM_ERROR, "LegoCmdAltDevIO::write: could not access Lego USB tower device file. Check RW permissions."));
	   //throw ACSErrTypeCommon::IOErrorExImpl(__FILE__, __LINE__, "LegoCmdAltDevIO::write");
	}

	if (sct_set_variable(RCX_COMM_ALTITUDE, (short int)value) != 0) {
	   ACS_SHORT_LOG((LM_ERROR, "LegoCmdAltDevIO::write: could not write RCX_COMM_ALTITUDE value. Please check RCX "
	   			    "communication and try again."));
	   //throw NEW aCSErrTypeCommon::IOErrorExImpl(__FILE__, __LINE__, "LegoCmdAltDevIO::write");
	}
        
        timestamp = getTimeStamp();
	return;
}

