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
 * "@(#) $Id: CalculationsImpl.cpp,v 1.10 2007/07/11 21:09:13 wg3 Exp $"
 * 
 * Implementation file developed by
 * Rodrigo Araya - rodrigo.araya@gmail.com
 * Sebastian Caro - carosebastian@gmail.com
 * Alejandro Barrientos - drlightspeed@msn.com
 *
 */

#include <iostream>
#include <unistd.h>
#include <math.h>

#include "CalculationsImpl.h"

using namespace acscomponent;

/* ----------------------------------------------------------------*/
CalculationsImpl::CalculationsImpl(const ACE_CString &name, maci::ContainerServices *containerServices) :
  acscomponent::ACSComponentImpl(name, containerServices)
{
  const char *_METHOD_="Calculations::CalculationsImpl";
  // ACS_TRACE is used for debugging purposes
  ACS_TRACE(_METHOD_);
}
/* ----------------------------------------------------------------*/
CalculationsImpl::~CalculationsImpl()
{
  const char *_METHOD_="Calculations::~CalculationsImpl";
  // ACS_TRACE is used for debugging purposes
  ACS_TRACE(_METHOD_);
  ACS_DEBUG_PARAM(_METHOD_, "Destroying %s...", name());
}


/* --------------------- [ CORBA interface ] ----------------------*/
TYPES::RadecPos CalculationsImpl::Altaz2Radec(const TYPES::AltazPos & pos) throw(CORBA::SystemException){
	return TYPES::RadecPos();
}

TYPES::AltazPos CalculationsImpl::Radec2Altaz(const TYPES::RadecPos & pos) throw(CORBA::SystemException){
	return TYPES::AltazPos();
}

/**
 * Returns the Julian Day for a given date.
 * (Astronomical Algorithms, second edition, Jean Meeus, 2005)
 */
CORBA::Double CalculationsImpl::date2JD(CORBA::Long year, CORBA::Long month, CORBA::Double day) throw(CORBA::SystemException){
	
	int A, B;
	CORBA::Double jd;
	bool isJulian = false; /* The date is from Gregorian or Julian Calendar */
	
	/* The last day of the Julian Calendar is on 1582, October 4th */
	if( year < 1582 || 
	   (year == 1582 && month < 10) ||
	   (year == 1582 && month == 10 && day < 5) )
		isJulian = true;

	if( month <= 2 ){
		year--;
		month += 12;
	}

	A = INT(year/100);
	if( isJulian )
		B = 0;
	else
		B = 2 - A + INT(A/4);

	jd = INT(365.25 * (year + 4716)) + INT(30.6001 * (month + 1)) + day + B - 1524.5;
	return jd;
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(CalculationsImpl)
  /* ----------------------------------------------------------------*/
  
  
  /*___oOo___*/
