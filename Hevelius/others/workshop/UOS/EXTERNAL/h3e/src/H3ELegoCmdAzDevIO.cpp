/*
 * "@(#) $Id: H3ELegoCmdAzDevIO.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: H3ELegoCmdAzDevIO.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#include "H3ELegoCmdAzDevIO.h"
#include "SCTdefs.h"
#include "SCTlibs.h"

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include <ACSErrTypeCommon.h>

/*
 * LegoCmdAzDevIO base class.
 */


/*
 * Nothing to construct here
 */
LegoCmdAzDevIO::LegoCmdAzDevIO()
{
}


LegoCmdAzDevIO::LegoCmdAzDevIO(void *data)
{
}



/*
 * And nothing to destroy.
 */
LegoCmdAzDevIO::~LegoCmdAzDevIO()
{
   sct_close_lego();
}



/**
 * Sets the commanded azimuth to actual telescope's azimuth.
 * NOT IMPLEMENTED YET!
 */
bool LegoCmdAzDevIO::initializeValue()
{
	return true;
}



/**
 * Reads the commanded azimuth from Lego telescope.
 * NOT IMPLEMENTED
 */
CORBA::Double LegoCmdAzDevIO::read(ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl)
{
        CORBA::Double ret_val(0.0);
        
        if (sct_open_lego() < 0) {
	   ACS_SHORT_LOG((LM_ERROR, "LegoCmdAzDevIO::read: could not access Lego USB tower device file. Check RW permissions."));
	   throw new ACSErrTypeCommon::IOErrorExImpl("H3ELegoCmdAzDevIO.cpp",69,"read",DEFAULT_SEVERITY);
	   //throw new ACSErrTypeCommon::IOErrorExImpl();
	}
	
	ret_val=(CORBA::Double) sct_get_variable(RCX_COMM_AZIMUTH);

	timestamp=getTimeStamp();
        return ret_val;
}



/**
 * Sets the commanded azimuth to Lego telescope.
 * NOT IMPLEMENTED
 */
void LegoCmdAzDevIO::write(const CORBA::Double& value, ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl)
{
        if (sct_open_lego() < 0) {
	   ACS_SHORT_LOG((LM_ERROR, "LegoCmdAzDevIO::write: could not access Lego USB tower device file. Check RW permissions."));
	   throw new ACSErrTypeCommon::IOErrorExImpl("H3ELegoCmdAzDevIO.cpp",89,"write",DEFAULT_SEVERITY);
	   //throw new ACSErrTypeCommon::IOErrorExImpl();
	}

	if (sct_set_variable(RCX_COMM_AZIMUTH, (short int)value) != 0) {
	   ACS_SHORT_LOG((LM_ERROR, "LegoCmdAzDevIO::write: could not write RCX_COMM_AZIMUTH value. Please check RCX "
	   			    "communication and try again."));
	   throw new ACSErrTypeCommon::IOErrorExImpl("H3ELegoCmdAzDevIO",96,"write",DEFAULT_SEVERITY);
           //throw new ACSErrTypeCommon::IOErrorExImpl();
	}
        
        timestamp = getTimeStamp();
	return;
}

