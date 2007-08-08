/*
 * "@(#) $Id: H3ELegoStatusDevIO.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: H3ELegoStatusDevIO.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#include "H3ELegoStatusDevIO.h"
#include "SCTdefs.h"
#include "SCTlibs.h"

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include <ACSErrTypeCommon.h>

/*
 * LegoStatusDevIO base class.
 */


/*
 * nothing to construct.
 */
LegoStatusDevIO::LegoStatusDevIO()
{
}


LegoStatusDevIO::LegoStatusDevIO(void *data)
{
}


/*
 * Nothing to destroy.
 */
LegoStatusDevIO::~LegoStatusDevIO()
{
   sct_close_lego();
}


/**
 * Resets status bits.
 * NOT IMPLEMENTED.
 */
bool LegoStatusDevIO::initializeValue()
{
	return true;
}


/**
 * Reads status from telescope.
 * NOT IMPLEMENTED.
 */
ACS::pattern LegoStatusDevIO::read(ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	ACS::pattern ret_val=STAT_COMM_ERROR;
        
        if (sct_open_lego() < 0) {
	   ACS_SHORT_LOG((LM_ERROR, "LegoSatusDevIO::read: could not access Lego USB tower device file. Check RW permissions."));
	   throw new ACSErrTypeCommon::IOErrorExImpl("H3ELegoStatusDevIO.cpp",66,"read",DEFAULT_SEVERITY);
	   //throw new ACSErrTypeCommon::IOErrorExImpl();
	}
	
	ret_val=(ACS::pattern) sct_get_variable(RCX_STATUS);

	timestamp=getTimeStamp();
	return ret_val;
}


/*
 * Change lego status.
 */
void LegoStatusDevIO::write(const ACS::pattern& value, ACS::Time& timestamp) throw (ACSErr::ACSbaseExImpl)
{
        /* bitmask with writable status bits */
	ACS::pattern mask = STAT_CALIBRATED | STAT_CALIBRATE;

        short int old_val=0, new_val=0;

        if (sct_open_lego() < 0) {
	   ACS_SHORT_LOG((LM_ERROR, "LegoStatusDevIO::write: could not access Lego USB tower device file. Check RW permissions."));
	   throw new ACSErrTypeCommon::IOErrorExImpl("H3ELegoStatusDevIO.cpp",89,"write",DEFAULT_SEVERITY);
           //throw new ACSErrTypeCommon::IOErrorExImpl();
	}

        if ((old_val=sct_get_variable(RCX_STATUS))==-1) {
	   ACS_SHORT_LOG((LM_ERROR, "LegoStatusDevIO::write: could not read RCX_STATUS value. Please check RCX"
	                            "communication and try again."));
	   throw new ACSErrTypeCommon::IOErrorExImpl("H3ELegoStatusDevIO.cpp",96,"write",DEFAULT_SEVERITY);
	   //throw new ACSErrTypeCommon::IOErrorExImpl();
	}

        //old_val |= (value & mask);
        new_val = (old_val & ~mask) | (value & mask);
	if (sct_set_variable(RCX_STATUS, new_val) != 0) {
	   ACS_SHORT_LOG((LM_ERROR, "LegoStatusDevIO::write: could not write RCX_STATUS value. Please check RCX "
	   			    "communication and try again."));
	   throw new ACSErrTypeCommon::IOErrorExImpl("H3ELegoStatusDevIO.cpp",104,"write",DEFAULT_SEVERITY);
           //throw new ACSErrTypeCommon::IOErrorExImpl();
	}
        
        timestamp = getTimeStamp();
	return;
}

