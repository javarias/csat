#ifndef CALCULATIONS_IMPL_H
#define CALCULATIONS_IMPL_H
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
* "@(#) $Id: CalculationsImpl.h,v 1.7 2007/07/11 21:09:13 wg3 Exp $"
*
*/

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <acscomponentImpl.h>

#define INT(X) (int)floor((double)X)

#include <CalculationsS.h>
#include <TypesC.h>
using namespace acscomponent;


class CalculationsImpl: public virtual ACSComponentImpl,
		     public virtual POA_CALCULATIONS_MODULE::Calculations
{
 public:
  CalculationsImpl(const ACE_CString &name, maci::ContainerServices *containerServices);

  /**
   * Destructor
   */
  virtual ~CalculationsImpl();
  virtual TYPES::RadecPos Altaz2Radec(const TYPES::AltazPos & pos) throw(CORBA::SystemException);
  virtual TYPES::AltazPos Radec2Altaz(const TYPES::RadecPos & pos) throw(CORBA::SystemException);
  virtual CORBA::Double greg2JD(CORBA::Long year, CORBA::Long month, CORBA::Double day) throw(CORBA::SystemException);
 
  /**
   * ALMA C++ coding standards state copy operators should be disabled.
   */
  void operator=(const CalculationsImpl&);

};

#endif /* !CALCULATIONS_IMPL_H */
