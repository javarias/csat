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
 * "@(#) $Id: TelescopeImpl.cpp,v 1.10 2007/07/11 21:09:13 wg3 Exp $"
 * 
 * Implementation file developed by
 * Rodrigo Araya - rodrigo.araya@gmail.com
 * Sebastian Caro - carosebastian@gmail.com
 * Alejandro Barrientos - drlightspeed@msn.com
 *
 */

#include <TelescopeImpl.h>
#include <iostream>
#include <unistd.h>

#define __RCX_COMMAND_DELAYTIME__	50000   // delay time in u_sec between RCX command invocations 

using namespace std;

/* ----------------------------------------------------------------*/
TelescopeImpl::TelescopeImpl(const ACE_CString &name, maci::ContainerServices *containerServices) :
  acscomponent::ACSComponentImpl(name, containerServices)
{
  const char *_METHOD_="Telescope::TelescopeImpl";
  // ACS_TRACE is used for debugging purposes
  ACS_TRACE(_METHOD_);
}
/* ----------------------------------------------------------------*/
TelescopeImpl::~TelescopeImpl()
{
  const char *_METHOD_="Telescope::~TelescopeImpl";
  // ACS_TRACE is used for debugging purposes
  ACS_TRACE(_METHOD_);
  ACS_DEBUG_PARAM(_METHOD_, "Destroying %s...", name());
}

void
TelescopeImpl::execute()
  throw (ACSErr::ACSbaseExImpl)
{
  const char *_METHOD_="Telescope::execute";
  ACS_TRACE(_METHOD_);
  
    // Use container to activate the door object
   ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_DEBUG, "Getting LegoComponent component."));
  
  //  Working Lego Stuff.
  m_lego_p = H3E::LegoControl::_nil();
  
  m_lego_p = getContainerServices()->getDefaultComponent<H3E::LegoControl>("IDL:alma/H3E/LegoControl:1.0");
  
  if (CORBA::is_nil(m_lego_p.in()))
    {
      throw acsErrTypeLifeCycle::LifeCycleExImpl(__FILE__, __LINE__, "::Telescope::execute");
    }
  
  
  // Get Instrument Component Reference Here.
  m_Instrument_p = INSTRUMENT_MODULE::Instrument::_nil();
  m_Instrument_p = getContainerServices()->getDefaultComponent<INSTRUMENT_MODULE::Instrument>("IDL:alma/INSTRUMENT_MODULE/Instrument:1.0");
  
  if (CORBA::is_nil(m_Instrument_p.in()))
    {
      throw acsErrTypeLifeCycle::LifeCycleExImpl(__FILE__, __LINE__, "::Telescope::execute");
    }
  
}



/* --------------------- [ CORBA interface ] ----------------------*/

TYPES::ImageType *
TelescopeImpl::observe (const TYPES::Position & coordinates, CORBA::Long exposureTime)
  throw(CORBA::SystemException, UOSErr::PositionOutOfLimitsEx)
{
  const char *_METHOD_="Telescope::observe";
  
  ACS_TRACE(_METHOD_);
  ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_DEBUG, "Received observe command. coordinates:  , exposureTime: %f ",exposureTime));  
  
  
  /* Verify if the coordinates are within allowed ranges
   * Azimuth     0 - 360
   * Elevation  10 - 90
   */
  
  if ( (coordinates.az < 0) || (coordinates.az > 360) || (coordinates.el < 10) || (coordinates.el > 90)){
    
    ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_ERROR, "Coordinates are out of limits, allowed ranges are el: 10 - 90 , az: 0 - 360."));
    UOSErr::PositionOutOfLimitsExImpl ex(__FILE__,__LINE__,_METHOD_);
    ex.addData("Reason","Coordinates are out of limits.");
    throw ex.getPositionOutOfLimitsEx();
  }
  
  
  
  /*   This method send the setTo command to the Lego component and once in position sends the takeImage command
   *   to the Intrument component. Then returns the obtauined image.
   */
  TYPES::Position currentCoordinates = getCurrentPosition();
  TYPES::ImageType * picture;
  
  
  TelescopeImpl::moveTo(coordinates);
  
  // This variables defines the encoders errors in degrees
  double az_delta = 4;
  double el_delta = 4;
  
  
  
  
  ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_DEBUG, "Waiting for telescope to get in position az: %f, el: %f",coordinates.az, coordinates.el));
  
  
  while( ((currentCoordinates.az - coordinates.az) >  az_delta) ||  \
	 ((currentCoordinates.az - coordinates.az) < -az_delta) ||  \
	 ((currentCoordinates.el - coordinates.el) >  el_delta) ||  \
	 ((currentCoordinates.el - coordinates.el) < -el_delta)){
    
    // waits until the telescope is in position
    usleep(__RCX_COMMAND_DELAYTIME__);
    currentCoordinates = getCurrentPosition();
  }
  
  ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_DEBUG, "Telescope in position az: %f, el: %f",currentCoordinates.az, currentCoordinates.el));
  
  
  // Take the picture
  picture = m_Instrument_p->takeImage (exposureTime);
  ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_DEBUG, "Returning the taken image")); 
  return picture;
}


void
TelescopeImpl::moveTo(const TYPES::Position & coordinates)
  throw(CORBA::SystemException, UOSErr::PositionOutOfLimitsEx)
{
  
  const char *_METHOD_="Telescope::moveTo";
  
  
  ACS_TRACE(_METHOD_);
  ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_DEBUG, "Received moveTo command. coordinates az: %f el: %f", coordinates.az, coordinates.el));
  
  /* Verify if the coordinates are within allowed ranges
   * Azimuth     0 - 360
   * Elevation  10 - 90
   */
  
  if ( (coordinates.az < 0) || (coordinates.az > 360) || (coordinates.el < 10) || (coordinates.el > 90)){
    ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_ERROR, "Coordinates are out of limits, allowed ranges are el: 10 - 90 , az: 0 - 360."));
    UOSErr::PositionOutOfLimitsExImpl ex(__FILE__,__LINE__,_METHOD_);
    ex.addData("Reason","Coordinates are out of limits.");
    throw ex.getPositionOutOfLimitsEx();
  }
  
  
  // Create the callback for the objfix method
  //AsyncMethodCBvoid setToCB("setTo");
  ACS::CBvoid_var setTo_CB;
  ACS::CBDescIn setToDescIn;
  
  CORBA::Double altitude = coordinates.el;
  CORBA::Double azimuth = coordinates.az;
  
  ACS_LOG(LM_SOURCE_INFO,_METHOD_,(LM_DEBUG, "Trying to set coordinates az: %f, el: %f", azimuth, altitude));
  m_lego_p->setTo( altitude, azimuth, setTo_CB.in(), setToDescIn);
}


TYPES::Position
TelescopeImpl::getCurrentPosition()
  throw(CORBA::SystemException)
{
  
  const char *_METHOD_="Telescope::getCurrentPosition";
  ACS_TRACE(_METHOD_);
  ACS_LOG(LM_SOURCE_INFO,_METHOD_, (LM_DEBUG, "Received getCurrentPosition command."));
  
  TYPES::Position currentCoordinates;
  ACSErr::Completion *comp = new ACSErr::Completion();
  
  
  currentCoordinates.az = (CORBA::Double)m_lego_p->actualAzimuth()->get_sync(comp);
  usleep(__RCX_COMMAND_DELAYTIME__);
  currentCoordinates.el = (CORBA::Double)m_lego_p->actualAltitude()->get_sync(comp);
  
  return currentCoordinates;
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(TelescopeImpl)
  /* ----------------------------------------------------------------*/
  
  
  /*___oOo___*/
