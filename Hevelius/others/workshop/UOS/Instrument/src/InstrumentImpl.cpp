/*******************************************************************************
*    ALMA - Atacama Large Millimiter Array
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
* "@(#) $Id: InstrumentImpl.cpp,v 1.11 2007/07/11 21:22:36 wg4 Exp $"
*	
*	Joao  Lopez S. 		jslopez_AT_csrg.inf.utfsm.cl
*	Jorge Castillo 		jcastill_AT_ing.uchile.cl
*	Pablo Bugueño C.	pbugueno_AT_disc.ucn.cl
*	Mauricio Araya		maray_AT_csrg.inf.utfsm.cl
*	
*/

#include <InstrumentImpl.h>
#include <iostream>
#include <unistd.h>
using namespace std;



/* ----------------------------------------------------------------*/
InstrumentImpl::InstrumentImpl(
	const ACE_CString &name, maci::ContainerServices *containerServices) :
    	acscomponent::ACSComponentImpl(name, containerServices)
{
	// ACS_TRACE is used for debugging purposes
	ACS_TRACE("::InstrumentImpl::InstrumentImpl");
}


/* ----------------------------------------------------------------*/
InstrumentImpl::~InstrumentImpl()
{
	// ACS_TRACE is used for debugging purposes
	ACS_TRACE("::InstrumentImpl::~InstrumentImpl");
}


/* ----------------------------------------------------------------*/
void 
InstrumentImpl::execute() throw (ACSErr::ACSbaseExImpl)
{
	const char *_METHOD_="InstrumentImpl::execute";
        ACS_TRACE(_METHOD_);
	
	ACSComponentImpl::execute();
	camState=false;
	m_cam_p = hptWebCam::WebCam::_nil();
//	m_cam_p = getContainerServices()->getComponent<hptWebCam::WebCam>(camName.c_str());
	m_cam_p = getContainerServices()->getDefaultComponent<hptWebCam::WebCam>("IDL:alma/hptWebCam/WebCam:1.0");
	if (CORBA::is_nil(m_cam_p)){
		ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_WARNING,"WebCam Component not found: m_cam_p is nil"));
	}
	else
		cameraOff();
	ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_INFO,"Instrument Component its up and running."));
}


/* --------------------- [ CORBA interface ] ----------------------*/
void 
InstrumentImpl::cameraOn () throw (CORBA::SystemException)
{
	const char *_METHOD_="InstrumentImpl::cameraOn";
        ACS_TRACE(_METHOD_);
	try
	{
		m_cam_p->on();
		camState=true;
	}
	catch(CORBA::SystemException &cse)
	{
		ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_WARNING,"CORBA::System Exception, cannot turn the camera on, check if is already running"));
		return;
	}
	ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_INFO,"The Camera is on."));

}

void 
InstrumentImpl::cameraOff () throw (CORBA::SystemException)
{
	const char *_METHOD_="InstrumentImpl::cameraOff";
        ACS_TRACE(_METHOD_);
	try
	{
		m_cam_p->off();
		camState=false;
	}
	catch(CORBA::SystemException &cse)
	{
		ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_WARNING,"CORBA::System Exception, cannot turn the camera off, check if it's already turned on"));
		return;
	}
	ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_INFO,"The Camera is off."));
}

TYPES::ImageType *
InstrumentImpl::takeImage (CORBA::Long exposureTime) throw (CORBA::SystemException, UOSErr::CameraIsOffEx)
{
	const char *_METHOD_="InstrumentImpl::takeImage";
	ACS_TRACE(_METHOD_);
	//Creating an instance of ImageType
	TYPES::ImageType *image = new TYPES::ImageType();

	if (camState==false){
		ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_ERROR,"The Camera is off,turn it on before take image"));
		UOSErr::CameraIsOffExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason","Camera is off, turn it on before take image");
		throw ex.getCameraIsOffEx();
	}
	//Settings necessary to take frames
	m_cam_p->brightness()->set_sync(20000);
	m_cam_p->gamma()->set_sync(10000);
	m_cam_p->setFrameSize(640,480);

	//Getting an image frame from the webcam
	ACS::longSeq *frame=m_cam_p->getFrame(false);
	sleep((unsigned int)exposureTime);	

	//Setting the length of the ImageType instance
	image->length(frame->length());

	//Filling the ImageType instance with image data
	for (unsigned long i=0; i<frame->length(); i++){
		(*image)[i]=(*frame)[i];
	}

	//returning the image, yay!
	ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_INFO,"Image taken without problems."));
	return image;
}


/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(InstrumentImpl)
/* ----------------------------------------------------------------*/

