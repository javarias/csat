#ifndef ConsoleImpl_h
#define ConsoleImpl_h
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
* "@(#) $Id: ConsoleImpl.h,v 1.3 2007/07/10 21:20:26 wg8 Exp $"
*
*/

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <acscomponentImpl.h>

/**
 *  POA_acsexmplHelloWorld::HelloWorld is obtained from this header file and is
 *  automatically generated from HelloWorld's Interface Definition File 
 *  (i.e., acsexmplHelloWorld.idl) by CORBA.
 */

#include <TypesS.h>
#include <SchedulerC.h>
#include <TelescopeC.h>
#include <InstrumentC.h>
#include <UOSErr.h>
#include <ConsoleS.h>

using namespace acscomponent;


/** @file ConsoleImpl.h
 */

/** @class Console
 * Operator's interface to set automatic and manual modes.
 * Grants manual access to low-level components.
 * @version "@(#) $Id: ConsoleImpl.h,v 1.3 2007/07/10 21:20:26 wg8 Exp $"
 */
class ConsoleImpl: public virtual ACSComponentImpl,
		   public virtual POA_CONSOLE_MODULE::Console
{
  
  public:
     /**
     * Constructor
     * @param name component's name. This is also the name that will
     * be used to find the component
     * @param containerServices the container services object for this
     * component
     */
    ConsoleImpl(const ACE_CString &name, maci::ContainerServices *containerServices);
    
    /**
     * Destructor
     */
    virtual ~ConsoleImpl();
    
    /** 
     * Set the automatic / manual mode for the operator. Raises an exception 
     * if the automatic mode is asked twice.
     *
     *  @param mode if true then automatic mode otherwise manual mode.
     *  @return void
     */
    virtual void
    setMode (CORBA::Boolean mode)
      throw (CORBA::SystemException,UOSErr::AlreadyInAutomaticEx);

    /** 
     * Get the current operator's mode. 
     *
     *  @return current operator's mode
     */
    virtual CORBA::Boolean
    getMode()
      throw (CORBA::SystemException);

    /** 
     * Set the camera on.
     *
     *  @return void
     */
    virtual void
    cameraOn()
      throw (CORBA::SystemException,UOSErr::SystemInAutoModeEx);
	
    /** 
     * Set the camera off.
     *
     *  @return void
     */
    virtual void cameraOff()
      throw (CORBA::SystemException,UOSErr::SystemInAutoModeEx);
	
    /** 
     * Move telescope in synchronous mode. Raises an exception if the
     * requested position is out of limits.
     *
     *  @coordinates az, el coordinates
     *  @return void
     */    
    virtual void
    moveTelescope(const TYPES::Position &coordinates)
      throw (CORBA::SystemException,UOSErr::PositionOutOfLimitsEx,UOSErr::SystemInAutoModeEx);

    /** 
     * Current telescope position. 
     *
     *  @return Telescope position
     */    
    virtual TYPES::Position
    getTelescopePosition()
      throw (CORBA::SystemException);
	
    /** 
     * Get an image from the camera (from actual position of telescope).
     *
     *  @return Image from the camera
     */    
    virtual TYPES::ImageType *
    getCameraImage()
      throw (CORBA::SystemException,UOSErr::SystemInAutoModeEx,UOSErr::CameraIsOffEx);

    virtual void
    execute()
      throw (ACSErr::ACSbaseExImpl);

  private:
    const CORBA::Long exposure_time;
    enum { manual, automatic } status;
    SCHEDULER_MODULE::Scheduler_var sch_p;
    INSTRUMENT_MODULE::Instrument_var ch_p;
    TELESCOPE_MODULE::Telescope_var t_p;

    /**
     * ALMA C++ coding standards state copy operators should be disabled.
     */
    void operator=(const ConsoleImpl&);
};

#endif /*!ConsoleImpl_H*/
