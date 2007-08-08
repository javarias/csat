/*
 * "@(#) $Id: MOUNT2Impl.cpp,v 1.5 2007/07/11 15:17:11 wg2 Exp $"
 *
 * $Log: MOUNT2Impl.cpp,v $
 * Revision 1.5  2007/07/11 15:17:11  wg2
 *  - Cleaned MOUNT2Impl.cpp and SerialRS232.cpp
 *
 * Revision 1.4  2007/07/10 20:43:41  wg2
 *  - Total functional methods in MOUNT2
 *
 * Revision 1.3  2007/07/10 20:21:20  wg2
 *  - Added Communication class.
 *  - Implemented offSet, zenith and park methods in MOUNT2Impl
 *
 * Revision 1.2  2007/07/10 18:50:28  wg2
 *  - First DevIO functional implementation (some TODO)
 *
 * Revision 1.1  2007/07/09 20:30:15  wg2
 *  - Included tests and several more files
 *
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#include <MOUNT2.h>
//#include <H3EImplIncludes.h>

//#include <H3EHelperFunctions.cpp>

#include "Communication.h"
#include "Mount2RefAzDevIO.h"
#include "Mount2RefAltDevIO.h"
//#include "Mount2CmdAzDevIO.h"
//#include "Mount2CmdAltDevIO.h"
#include "SerialRS232.h"
//#include "SCTdefs.h"

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




MOUNT2Impl::MOUNT2Impl(const ACE_CString& name, 
		                             maci::ContainerServices* containerServices)
	: CharacteristicComponentImpl(name, containerServices)

	,commandedAltitude_m(this)
	,commandedAzimuth_m(this)
	,actualAltitude_m(this)
	,actualAzimuth_m(this)
	,status_m(this)

{
	component_name = name.c_str();

	//#include <H3EImplInit.body.cpp>
	component_running = true;

	//#include <H3EThreadInit.body.cpp>
}


//#include <H3EThreadImpl.body.cpp>


MOUNT2Impl::~MOUNT2Impl()
{
}


void MOUNT2Impl::initialize(void)
	throw (acsErrTypeLifeCycle::acsErrTypeLifeCycleExImpl)
{
	if (getComponent() != 0)
	{
		//#include <H3EImplInitialize.body.cpp>
 		commandedAltitude_m = new RWdouble((component_name + std::string(":commandedAltitude")).c_str(), 
 				                  getComponent()/*, new Mount2CmdAltDevIO*/);
 		commandedAzimuth_m = new RWdouble((component_name + std::string(":commandedAzimuth")).c_str(), 
 				                  getComponent()/*, new Mount2CmdAzDevIO*/);
 		actualAltitude_m = new ROdouble((component_name + std::string(":actualAltitude")).c_str(), 
 				                  getComponent(), new Mount2RefAltDevIO);
 		actualAzimuth_m = new ROdouble((component_name + std::string(":actualAzimuth")).c_str(), 
 				                  getComponent(),new Mount2RefAzDevIO);
 		status_m = new RWpattern((component_name + std::string(":status")).c_str(), 
 				                  getComponent());
	}

}


void MOUNT2Impl::execute(void)
	throw (acsErrTypeLifeCycle::acsErrTypeLifeCycleExImpl)
{
//	if (getComponent() != 0)
//	{
//		//#include <H3EImplExecute.body.cpp>
//	}

}


void MOUNT2Impl::cleanUp(void)
{
//	if (getComponent() != 0)
//	{
//		//#include <H3EImplCleanUp.body.cpp>
//		sct_close_lego();
//
//		getComponent()->stopAllThreads();
//		ACE_OS::sleep(2);
//	}
//
}


void MOUNT2Impl::aboutToAbort(void)
{
//	if (getComponent() != 0)
//	{
//		//#include <H3EImplAboutToAbort.body.cpp>
//	}
//
}

/* --------------- [ Action implementator interface ] -------------- */

ActionRequest MOUNT2Impl::invokeAction(int function,
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

ActionRequest MOUNT2Impl::setToAction(BACIComponent* cob,
	const int&       callbackID,
	const CBDescIn&  descIn,
	BACIValue*       value,
	Completion&      completion,
	CBDescOut&       descOut)
{
	completion.type = ACSErr::ACSErrTypeOK;
	completion.code = ACSErr::ACSErrOK;
	ACS_SHORT_LOG((LM_INFO, "MOUNT2Impl::setToAction: Moving the telescope with setTo"));
	if (component_running == true)
	{
		__setTo_action *param_p = static_cast<__setTo_action *>
			                         (const_cast<void *>(value->pointerValue()));
		if (param_p != 0)
		{	
			commandedAltitude_m->set_sync(param_p->altitude);
			commandedAzimuth_m->set_sync(param_p->azimuth);
			
			ACSErr::Completion *comp = new ACSErr::Completion();
			Communication *comm = new Communication("/dev/ttyS0");
			comm->goToAltAzm((double)commandedAltitude_m->get_sync(comp),(double) commandedAzimuth_m->get_sync(comp));
			delete comm;
			delete comp;
			delete param_p;
		}
		else
		{
			ACS_SHORT_LOG((LM_INFO, "setToAction: paramenter pointer is 0!"));
		}
	}
	else
	{
		ACS_SHORT_LOG((LM_ERROR, "MOUNT2Impl::setToAction: Action is not "
					                   "executed. Check if component is running!"));
	}
	completion.timeStamp = getTimeStamp();
	//return reqInvokeDone;
	return (ActionRequest)NULL;//reqInvokeDone;
}

ActionRequest MOUNT2Impl::offSetAction(BACIComponent* cob,
	const int&       callbackID,
	const CBDescIn&  descIn,
	BACIValue*       value,
	Completion&      completion,
	CBDescOut&       descOut)
{

	ACS_SHORT_LOG((LM_INFO, "MOUNT2Impl::offSetAction: doing an offSet"));

	if (component_running == true)
	{
		__offSet_action *param_p = static_cast<__offSet_action *>
			                         (const_cast<void *>(value->pointerValue()));
		if (param_p != 0)
		{	
			ACSErr::Completion *comp = new ACSErr::Completion();

			double altitude = (double) actualAltitude_m->get_sync(comp) + param_p->altOffset;
			double azimuth = (double) actualAzimuth_m->get_sync(comp) + param_p->azOffset;

			Communication *comm = new Communication("/dev/ttyS0");
			comm->goToAltAzm(altitude, azimuth);
			delete comm;
			delete comp;
			delete param_p;
		}
		else
		{
			ACS_SHORT_LOG((LM_INFO, "offSetAction: paramenter pointer is 0!"));
		}
	}
	else
	{
		ACS_SHORT_LOG((LM_ERROR, "MOUNT2Impl::offSetAction: Action is not "
					                   "executed. Check if component is running!"));
	}
	completion.timeStamp = getTimeStamp();
	//return reqInvokeDone;
	return (ActionRequest) NULL;
}

ActionRequest MOUNT2Impl::zenithAction(BACIComponent* cob,
	const int&       callbackID,
	const CBDescIn&  descIn,
	BACIValue*       value,
	Completion&      completion,
	CBDescOut&       descOut)
{
	ACS_SHORT_LOG((LM_INFO, "MOUNT2Impl::zenithAction: Moving telescope to zenith position" ));

	completion.type = ACSErr::ACSErrTypeOK;
	completion.code = ACSErr::ACSErrOK;
	if (component_running == true)
	{
		ACSErr::Completion *comp = new ACSErr::Completion();
		Communication *comm = new Communication("/dev/ttyS0");
		comm->goToAltAzm(90,(double) actualAzimuth_m->get_sync(comp));
		delete comm;
		delete comp;
	}
	else
	{
		ACS_SHORT_LOG((LM_ERROR, "MOUNT2Impl::zenithAction: Action is not "
					                   "executed. Check if component is running!"));
	}
	completion.timeStamp = getTimeStamp();
	//return reqInvokeDone;
	return (ActionRequest)NULL;
}

ActionRequest MOUNT2Impl::parkAction(BACIComponent* cob,
	const int&       callbackID,
	const CBDescIn&  descIn,
	BACIValue*       value,
	Completion&      completion,
	CBDescOut&       descOut)
{
	completion.type = ACSErr::ACSErrTypeOK;
	completion.code = ACSErr::ACSErrOK;
	ACS_SHORT_LOG((LM_INFO, "MOUNT2Impl::parkAction: Parking telescope to (0,0)"));
	if (component_running == true)
	{
		ACSErr::Completion *comp = new ACSErr::Completion();
		Communication *comm = new Communication("/dev/ttyS0");
		comm->goToAltAzm(0,0);
		delete comm;
		delete comp;
		
	}
	else
	{
		ACS_SHORT_LOG((LM_ERROR, "MOUNT2Impl::parkAction: Action is not "
					                   "executed. Check if component is running!"));
	}
	completion.timeStamp = getTimeStamp();
	//return reqInvokeDone;
	return (ActionRequest)NULL;
}



/* ------------------ [ Functions ] ----------------- */

void MOUNT2Impl::setUncalibrated() throw(CORBA::SystemException)
{
	ACS_SHORT_LOG((LM_INFO, "MOUNT2Impl::setUncalibrated: Setting status to 0"));
	if (component_running == true)
	{
		status_m->set_sync(0);
	}
	else
	{
		ACS_SHORT_LOG((LM_ERROR,"MOUNT2Impl::setUncalibrated: Function is not "
					                  "executed. Check if component is running!"));
	}
}



void MOUNT2Impl::calibrateEncoders() throw(CORBA::SystemException)
{
	ACS_SHORT_LOG((LM_INFO, "MOUNT2Impl::calibrateEncoders: Setting status to 1"));
	if (component_running == true)
	{
		status_m->set_sync(1);
	}
	else
	{
		ACS_SHORT_LOG((LM_ERROR,"MOUNT2Impl::calibrateEncoders: Function is not "
					                  "executed. Check if component is running!"));
	}
}




/* --------------------- [ CORBA interface ] ----------------------*/

void MOUNT2Impl::setTo(CORBA::Double altitude, CORBA::Double azimuth, ACS::CBvoid_ptr cb, const ACS::CBDescIn& desc)
  throw(CORBA::SystemException)
{
	__setTo_action *param_p = new __setTo_action();
	param_p->altitude = (double) altitude;
	param_p->azimuth = (double) azimuth;

	getComponent()->registerAction(BACIValue::type_null, cb, desc,
			                           this, setTo_ACTION, BACIValue(param_p));
}

void MOUNT2Impl::offSet(CORBA::Double altOffset, CORBA::Double azOffset, ACS::CBvoid_ptr cb, const ACS::CBDescIn& desc)
  throw(CORBA::SystemException)
{
	__offSet_action *param_p = new __offSet_action();
	param_p->altOffset = (double) altOffset;
	param_p->azOffset = (double) azOffset;

	getComponent()->registerAction(BACIValue::type_null, cb, desc,
			                           this, offSet_ACTION, BACIValue(param_p));
}

void MOUNT2Impl::zenith(ACS::CBvoid_ptr cb, const ACS::CBDescIn& desc)
	throw(CORBA::SystemException)
{
	ACS_SHORT_LOG((LM_INFO, "MOUNT2Impl::zenith: Entering zenith" ));
	getComponent()->registerAction(BACIValue::type_null, cb, desc, 
			                           this, zenith_ACTION);
}


void MOUNT2Impl::park(ACS::CBvoid_ptr cb, const ACS::CBDescIn& desc)
	throw(CORBA::SystemException)
{
	getComponent()->registerAction(BACIValue::type_null, cb, desc, 
			                           this, park_ACTION);
}




ACS::RWdouble_ptr MOUNT2Impl::commandedAltitude() throw(CORBA::SystemException)
{
	if (commandedAltitude_m == 0)
	{
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(commandedAltitude_m->getCORBAReference());
	return prop._retn();
}


ACS::RWdouble_ptr MOUNT2Impl::commandedAzimuth() throw(CORBA::SystemException)
{
	if (commandedAzimuth_m == 0)
	{
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(commandedAzimuth_m->getCORBAReference());
	return prop._retn();
}


ACS::ROdouble_ptr MOUNT2Impl::actualAltitude() throw(CORBA::SystemException)
{
	if (actualAltitude_m == 0)
	{
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(actualAltitude_m->getCORBAReference());
	return prop._retn();
}


ACS::ROdouble_ptr MOUNT2Impl::actualAzimuth() throw(CORBA::SystemException)
{
	if (actualAzimuth_m == 0)
	{
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(actualAzimuth_m->getCORBAReference());
	return prop._retn();
}


ACS::RWpattern_ptr MOUNT2Impl::status() throw(CORBA::SystemException)
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
MACI_DLL_SUPPORT_FUNCTIONS(MOUNT2Impl)
