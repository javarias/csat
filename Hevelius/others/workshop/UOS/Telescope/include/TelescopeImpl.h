#ifndef TelescopeImpl_h
#define TelescopeImpl_h
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
* "@(#) $Id: TelescopeImpl.h,v 1.7 2007/07/11 21:09:13 wg3 Exp $"
*
* Header file developed by 
* Rodrigo Araya - rodrigo.araya@gmail.com
* Sebastian Caro - carosebastian@gmail.com
* Alejandro Barrientos - drlightspeed@msn.com
*/

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <acscomponentImpl.h>


#include <TelescopeS.h>
#include <TypesC.h>
#include <H3ES.h>
#include <InstrumentS.h>
#include <UOSErr.h>
using namespace acscomponent;


class TelescopeImpl: public virtual ACSComponentImpl,
		     public virtual POA_TELESCOPE_MODULE::Telescope
{
 public:
  TelescopeImpl(const ACE_CString &name, maci::ContainerServices *containerServices);

  /**
   * Destructor
   */
  virtual ~TelescopeImpl();
  
  virtual TYPES::ImageType * 
	observe (const TYPES::Position & coordinates, CORBA::Long exposureTime) throw(CORBA::SystemException, UOSErr::PositionOutOfLimitsEx);

  virtual void
    moveTo(const TYPES::Position & coordinates) throw(CORBA::SystemException, UOSErr::PositionOutOfLimitsEx);
  
  virtual TYPES::Position
    getCurrentPosition() throw(CORBA::SystemException);
 
 virtual void execute()
        throw (ACSErr::ACSbaseExImpl);


 
 private:

   H3E::LegoControl_var m_lego_p;
   INSTRUMENT_MODULE::Instrument_var m_Instrument_p;
    
  /**
   * ALMA C++ coding standards state copy operators should be disabled.
   */
  void operator=(const TelescopeImpl&);

};

#endif /*!TelescopeImpl_H*/
