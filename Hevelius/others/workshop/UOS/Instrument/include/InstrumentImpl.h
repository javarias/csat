
#ifndef InstrumentImpl_h
#define InstrumentImpl_h

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
* "@(#) $Id: InstrumentImpl.h,v 1.5 2007/07/11 21:22:54 wg4 Exp $"
*
*       Joao  Lopez S.          jslopez_AT_csrg.inf.utfsm.cl
*       Jorge Castillo          jcastill_AT_ing.uchile.cl
*       Pablo Bugueño C.        pbugueno_AT_disc.ucn.cl
*       Mauricio Araya          maray_AT_csrg.inf.utfsm.cl
*
*/

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <acscomponentImpl.h>
#include <InstrumentS.h>
#include <TypesS.h>
#include <UOSErr.h>
#include <hptWebCamC.h>

using namespace acscomponent;


/** @file InstrumentImpl.h
 */

class InstrumentImpl: 	public virtual ACSComponentImpl,
		  	public virtual POA_INSTRUMENT_MODULE::Instrument{
  	public:

	     	/**
	     	* Constructor
	     	* @param name component's name. This is also the name that will be used to find the
	     	* @param containerServices the container services object for this component
	     	*/
	    	InstrumentImpl(const ACE_CString &name, maci::ContainerServices *containerServices);

	    	/**
	    	* Destructor
	     	*/
	    	virtual ~InstrumentImpl();

	    	virtual void cameraOn () throw (CORBA::SystemException);

	   	virtual void cameraOff () throw (CORBA::SystemException);

		virtual TYPES::ImageType * takeImage (
	        CORBA::Long exposureTime) throw (CORBA::SystemException, UOSErr::CameraIsOffEx);

		virtual void execute() throw (ACSErr::ACSbaseExImpl);

  	private:

		bool camState;

	    	/**
	     	* ALMA C++ coding standards state copy operators should be disabled.
	     	*/
	    	void operator=(const InstrumentImpl&);

		hptWebCam::WebCam_var m_cam_p;

};
	
#endif /*!InstrumentImpl_H*/

