/*******************************************************************************
*    ALMA - Atacama Large Millimiter Array
*    (c) Associated Universities Inc., 2002 *
*    (c) European Southern Observatory, 2002
*    Copyright by ESO (in the framework of the ALMA collaboration)
*    and Cosylab 2002, All rights reserved
*
*    This library is free software; you can redistribute it and/or
*    modify it under the terms of the GNU Lesser General Public
*    License as published by the Free Software Foundation; either
*    version 2.1 of the License, or (at your option) any later version.
*
*    This library is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*    Lesser General Public License for more details.
*
*    You should have received a copy of the GNU Lesser General Public
*    License along with this library; if not, write to the Free Software
*    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
*
*
*
* "@(#) $Id: ConsoleImpl.cpp,v 1.5 2007/07/11 16:53:51 wg8 Exp $"
*
*/
 
#include <ConsoleImpl.h>
#include <iostream>

using namespace std;


/* ----------------------------------------------------------------*/
ConsoleImpl::ConsoleImpl(const ACE_CString &name, maci::ContainerServices *containerServices) :
  acscomponent::ACSComponentImpl(name, containerServices),exposure_time(1)
{

	const char *_METHOD_="ConsoleImpl::ConsoleImpl";
	ACS_TRACE(_METHOD_);
}
/* ----------------------------------------------------------------*/
ConsoleImpl::~ConsoleImpl()
{
	const char *_METHOD_="ConsoleImpl::~ConsoleImpl";
	ACS_TRACE(_METHOD_);
}


/* --------------------- [ CORBA interface ] ----------------------*/
	void
	ConsoleImpl::setMode (CORBA::Boolean mode)
throw (CORBA::SystemException,UOSErr::AlreadyInAutomaticEx)
{
	const char *_METHOD_="ConsoleImpl::setMode";
	ACS_TRACE(_METHOD_);

	if (CORBA::is_nil(sch_p)) 
		ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_WARNING,"sch_p is nil"));

	if (mode) {
		if (status == automatic) {
			ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_ERROR,"try to set automatic, but status already in automatic"));
			UOSErr::AlreadyInAutomaticExImpl ex(__FILE__,__LINE__,_METHOD_);
			ex.addData("Reason","try to set automatic, but status already in automatic");
			throw ex.getAlreadyInAutomaticEx();
		} else {
			try
			{
				sch_p->start();
			} catch (UOSErr::SchedulerAlreadyRunningEx &ex) {
				ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_WARNING,"try to set automatic, but scheduler already running"));
			}
		}
		status = automatic;
	} else {
		if (status == manual) {
			ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_DEBUG,"set to manual, but status already manual"));
		} else {
			try
			{
				sch_p->stop();
			} catch (UOSErr::SchedulerAlreadyStoppedEx &ex) {
				ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_WARNING,"try to set manual, but scheduler already stopped"));
			}

		}
		status = manual;
	}
	ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_INFO,"Received setMode command. mode: %d", mode));
}

CORBA::Boolean
ConsoleImpl::getMode ()
  throw (CORBA::SystemException)
{
	
	const char *_METHOD_="ConsoleImpl::getMode";
	ACS_TRACE(_METHOD_);
	ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_INFO,"Received getMode mode: %d", status));
	return status == automatic;
}

void
ConsoleImpl::cameraOn ()
  throw (CORBA::SystemException,UOSErr::SystemInAutoModeEx)
{

	const char *_METHOD_="ConsoleImpl::cameraOn";
	ACS_TRACE(_METHOD_);
	if (status == automatic) {
		ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_DEBUG,"Try to use camera in automatic mode"));
		UOSErr::SystemInAutoModeExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason","Try to use camera in automatic mode");
		throw ex.getSystemInAutoModeEx();
	}
	ch_p->cameraOn();
	ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_INFO,"Set camera on"));
}

void
ConsoleImpl::cameraOff ()
  throw (CORBA::SystemException,UOSErr::SystemInAutoModeEx)
{
	const char *_METHOD_="ConsoleImpl::cameraOff";
	ACS_TRACE(_METHOD_);
	if (status == automatic) {
		ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_DEBUG,"Try to use camera in automatic mode"));
		UOSErr::SystemInAutoModeExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason","Try to use camera in automatic mode");
		throw ex.getSystemInAutoModeEx();
	}
	ch_p->cameraOff();
	ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_INFO,"Set camera off"));
}

void
ConsoleImpl::moveTelescope (const TYPES::Position &position)
  throw (CORBA::SystemException,UOSErr::PositionOutOfLimitsEx,UOSErr::SystemInAutoModeEx)
{
	const char *_METHOD_="ConsoleImpl::moveTelescope";
	ACS_TRACE(_METHOD_);
	if (status == automatic) {
		ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_DEBUG,"Try to move telescope in automatic mode"));
		UOSErr::SystemInAutoModeExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason","Try to move telescope in automatic mode");
		throw ex.getSystemInAutoModeEx();
	}
	t_p->moveTo(position);
	ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_INFO,"Received new telescope position."
				" az: %f el %f",
				position.az, position.el));
}

TYPES::Position
ConsoleImpl::getTelescopePosition ()
  throw (CORBA::SystemException)
{
	TYPES::Position position;
        const char *_METHOD_="ConsoleImpl::getTelescopePosition";
        ACS_TRACE(_METHOD_);
	position=t_p->getCurrentPosition();
	ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_INFO,"Current telescope position: "
				" az: %f el %f",
				position.az, position.el));
	return position;
}

TYPES::ImageType *
ConsoleImpl::getCameraImage ()
  throw (CORBA::SystemException,UOSErr::SystemInAutoModeEx,UOSErr::CameraIsOffEx)
{
	TYPES::ImageType *image;
	const char *_METHOD_="ConsoleImpl::getCameraImage";
	ACS_TRACE(_METHOD_);
	if (status == automatic) {
		ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_DEBUG,"Try to use camera in automatic mode"));
		UOSErr::SystemInAutoModeExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason","Try to use camera in automatic mode");
		throw ex.getSystemInAutoModeEx();
	}
	image=ch_p->takeImage(exposure_time);
	ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_INFO,"Recieved new image"));
	return image;
}

void
ConsoleImpl::execute()
  throw (ACSErr::ACSbaseExImpl)
{
	const char *_METHOD_="ConsoleImpl::execute";
	ACS_TRACE(_METHOD_);
	// TODO: Put pointers init a initialize()
	sch_p = SCHEDULER_MODULE::Scheduler::_nil();
	ch_p  = INSTRUMENT_MODULE::Instrument::_nil();
	t_p   = TELESCOPE_MODULE::Telescope::_nil();
	status = manual;

	sch_p = getContainerServices()->getDefaultComponent<SCHEDULER_MODULE::Scheduler>("IDL:alma/SCHEDULER_MODULE/Scheduler:1.0");
	if (CORBA::is_nil(sch_p)) 
		ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_WARNING,"Scheduler Component not found: sch_p is nil"));
	ch_p = getContainerServices()->getDefaultComponent<INSTRUMENT_MODULE::Instrument>("IDL:alma/INSTRUMENT_MODULE/Instrument:1.0");
	if (CORBA::is_nil(ch_p)) 
		ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_WARNING,"Instrument Component not found: ch_p is nil"));
	t_p = getContainerServices()->getDefaultComponent<TELESCOPE_MODULE::Telescope>("IDL:alma/TELESCOPE_MODULE/Telescope:1.0");
	if (CORBA::is_nil(t_p)) 
		ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_WARNING,"Telescope Component not found: t_p is nil"));

}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(ConsoleImpl)
/* ----------------------------------------------------------------*/


/*___oOo___*/


