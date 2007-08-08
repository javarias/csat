/*
 * "@(#) $Id: H3EImpl.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: H3EImpl.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#include <H3EImpl.h>
#include <H3EImplIncludes.h>
#include <H3EHelperFunctions.cpp>

#include "H3ELegoCmdAltDevIO.h"
#include "H3ELegoCmdAzDevIO.h"
#include "H3ELegoAltDevIO.h"
#include "H3ELegoAzDevIO.h"
#include "H3ELegoStatusDevIO.h"

struct __setTo_action
{
	double  altitude;
	double  azimuth;

};

struct __offSet_action
{
	double  altOffset;
	double  azOffset;

};




LegoControlImpl::LegoControlImpl(const ACE_CString& name, 
		                             maci::ContainerServices* containerServices)
	: CharacteristicComponentImpl(name, containerServices)

	,commandedAltitude_m(this)
	,commandedAzimuth_m(this)
	,actualAltitude_m(this)
	,actualAzimuth_m(this)
	,status_m(this)

{
	component_name = name.c_str();

	#include <H3EImplInit.body.cpp>
	#include <H3EThreadInit.body.cpp>
}


#include <H3EThreadImpl.body.cpp>


LegoControlImpl::~LegoControlImpl()
{
}


void LegoControlImpl::initialize(void)
	throw (acsErrTypeLifeCycle::acsErrTypeLifeCycleExImpl)
{
	if (getComponent() != 0)
	{
		#include <H3EImplInitialize.body.cpp>
	}

}


void LegoControlImpl::execute(void)
	throw (acsErrTypeLifeCycle::acsErrTypeLifeCycleExImpl)
{
	if (getComponent() != 0)
	{
		#include <H3EImplExecute.body.cpp>
	}

}


void LegoControlImpl::cleanUp(void)
{
	if (getComponent() != 0)
	{
		#include <H3EImplCleanUp.body.cpp>

		getComponent()->stopAllThreads();
		ACE_OS::sleep(2);
	}

}


void LegoControlImpl::aboutToAbort(void)
{
	if (getComponent() != 0)
	{
		#include <H3EImplAboutToAbort.body.cpp>
	}

}

/* --------------- [ Action implementator interface ] -------------- */

ActionRequest LegoControlImpl::invokeAction(int function,
	BACIComponent*   cob,
	const int&       callbackID,
	const CBDescIn&  descIn,
	BACIValue*       value,
	Completion&      completion,
	CBDescOut&       descOut)
{
	switch (function)
	{
		case setTo_ACTION:
		{
			return setToAction(cob, callbackID, descIn, value, completion, descOut);
		}
		case offSet_ACTION:
		{
			return offSetAction(cob, callbackID, descIn, value, completion, descOut);
		}
		case zenith_ACTION:
		{
			return zenithAction(cob, callbackID, descIn, value, completion, descOut);
		}
		case park_ACTION:
		{
			return parkAction(cob, callbackID, descIn, value, completion, descOut);
		}

		default:
		{
			return reqDestroy;
		}
	}
}


/* ------------------ [ Action implementations ] ----------------- */

ActionRequest LegoControlImpl::setToAction(BACIComponent* cob,
	const int&       callbackID,
	const CBDescIn&  descIn,
	BACIValue*       value,
	Completion&      completion,
	CBDescOut&       descOut)
{
	completion.type = ACSErr::ACSErrTypeOK;
	completion.code = ACSErr::ACSErrOK;
	if (component_running == true)
	{
		__setTo_action *param_p = static_cast<__setTo_action *>
			                         (const_cast<void *>(value->pointerValue()));
		if (param_p != 0)
		{	
			#include "H3EsetToAction.body.cpp"
			delete param_p;
		}
		else
		{
			ACS_SHORT_LOG((LM_INFO, "setToAction: paramenter pointer is 0!"));
		}
	}
	else
	{
		ACS_SHORT_LOG((LM_ERROR, "LegoControlImpl::setToAction: Action is not "
					                   "executed. Check if component is running!"));
	}
	completion.timeStamp = getTimeStamp();
	return reqInvokeDone;
}

ActionRequest LegoControlImpl::offSetAction(BACIComponent* cob,
	const int&       callbackID,
	const CBDescIn&  descIn,
	BACIValue*       value,
	Completion&      completion,
	CBDescOut&       descOut)
{
	completion.type = ACSErr::ACSErrTypeOK;
	completion.code = ACSErr::ACSErrOK;
	if (component_running == true)
	{
		__offSet_action *param_p = static_cast<__offSet_action *>
			                         (const_cast<void *>(value->pointerValue()));
		if (param_p != 0)
		{	
			#include "H3EoffSetAction.body.cpp"
			delete param_p;
		}
		else
		{
			ACS_SHORT_LOG((LM_INFO, "offSetAction: paramenter pointer is 0!"));
		}
	}
	else
	{
		ACS_SHORT_LOG((LM_ERROR, "LegoControlImpl::offSetAction: Action is not "
					                   "executed. Check if component is running!"));
	}
	completion.timeStamp = getTimeStamp();
	return reqInvokeDone;
}

ActionRequest LegoControlImpl::zenithAction(BACIComponent* cob,
	const int&       callbackID,
	const CBDescIn&  descIn,
	BACIValue*       value,
	Completion&      completion,
	CBDescOut&       descOut)
{
	completion.type = ACSErr::ACSErrTypeOK;
	completion.code = ACSErr::ACSErrOK;
	if (component_running == true)
	{
		#include "H3EzenithAction.body.cpp"
	}
	else
	{
		ACS_SHORT_LOG((LM_ERROR, "LegoControlImpl::zenithAction: Action is not "
					                   "executed. Check if component is running!"));
	}
	completion.timeStamp = getTimeStamp();
	return reqInvokeDone;
}

ActionRequest LegoControlImpl::parkAction(BACIComponent* cob,
	const int&       callbackID,
	const CBDescIn&  descIn,
	BACIValue*       value,
	Completion&      completion,
	CBDescOut&       descOut)
{
	completion.type = ACSErr::ACSErrTypeOK;
	completion.code = ACSErr::ACSErrOK;
	if (component_running == true)
	{
		#include "H3EparkAction.body.cpp"
	}
	else
	{
		ACS_SHORT_LOG((LM_ERROR, "LegoControlImpl::parkAction: Action is not "
					                   "executed. Check if component is running!"));
	}
	completion.timeStamp = getTimeStamp();
	return reqInvokeDone;
}



/* ------------------ [ Functions ] ----------------- */

void LegoControlImpl::setUncalibrated() throw(CORBA::SystemException)
{
	if (component_running == true)
	{
		#include "H3EsetUncalibratedFunct.body.cpp"
	}
	else
	{
		ACS_SHORT_LOG((LM_ERROR,"LegoControlImpl::setUncalibrated: Function is not "
					                  "executed. Check if component is running!"));
	}
}



void LegoControlImpl::calibrateEncoders() throw(CORBA::SystemException)
{
	if (component_running == true)
	{
		#include "H3EcalibrateEncodersFunct.body.cpp"
	}
	else
	{
		ACS_SHORT_LOG((LM_ERROR,"LegoControlImpl::calibrateEncoders: Function is not "
					                  "executed. Check if component is running!"));
	}
}




/* --------------------- [ CORBA interface ] ----------------------*/

void LegoControlImpl::setTo(CORBA::Double altitude, CORBA::Double azimuth, ACS::CBvoid_ptr cb, const ACS::CBDescIn& desc)
  throw(CORBA::SystemException)
{
	__setTo_action *param_p = new __setTo_action();
	param_p->altitude = (double) altitude;
	param_p->azimuth = (double) azimuth;

	getComponent()->registerAction(BACIValue::type_null, cb, desc,
			                           this, setTo_ACTION, BACIValue(param_p));
}

void LegoControlImpl::offSet(CORBA::Double altOffset, CORBA::Double azOffset, ACS::CBvoid_ptr cb, const ACS::CBDescIn& desc)
  throw(CORBA::SystemException)
{
	__offSet_action *param_p = new __offSet_action();
	param_p->altOffset = (double) altOffset;
	param_p->azOffset = (double) azOffset;

	getComponent()->registerAction(BACIValue::type_null, cb, desc,
			                           this, offSet_ACTION, BACIValue(param_p));
}

void LegoControlImpl::zenith(ACS::CBvoid_ptr cb, const ACS::CBDescIn& desc)
	throw(CORBA::SystemException)
{
	getComponent()->registerAction(BACIValue::type_null, cb, desc, 
			                           this, zenith_ACTION);
}


void LegoControlImpl::park(ACS::CBvoid_ptr cb, const ACS::CBDescIn& desc)
	throw(CORBA::SystemException)
{
	getComponent()->registerAction(BACIValue::type_null, cb, desc, 
			                           this, park_ACTION);
}




ACS::RWdouble_ptr LegoControlImpl::commandedAltitude() throw(CORBA::SystemException)
{
	if (commandedAltitude_m == 0)
	{
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(commandedAltitude_m->getCORBAReference());
	return prop._retn();
}


ACS::RWdouble_ptr LegoControlImpl::commandedAzimuth() throw(CORBA::SystemException)
{
	if (commandedAzimuth_m == 0)
	{
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(commandedAzimuth_m->getCORBAReference());
	return prop._retn();
}


ACS::ROdouble_ptr LegoControlImpl::actualAltitude() throw(CORBA::SystemException)
{
	if (actualAltitude_m == 0)
	{
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(actualAltitude_m->getCORBAReference());
	return prop._retn();
}


ACS::ROdouble_ptr LegoControlImpl::actualAzimuth() throw(CORBA::SystemException)
{
	if (actualAzimuth_m == 0)
	{
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(actualAzimuth_m->getCORBAReference());
	return prop._retn();
}


ACS::RWpattern_ptr LegoControlImpl::status() throw(CORBA::SystemException)
{
	if (status_m == 0)
	{
		return ACS::RWpattern::_nil();
	}
	ACS::RWpattern_var prop = ACS::RWpattern::_narrow(status_m->getCORBAReference());
	return prop._retn();
}



/* --------------- [ MACI DLL support functions ] -----------------*/

#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(LegoControlImpl)
