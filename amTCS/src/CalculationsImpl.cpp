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
#include "csatErrors.h"

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
CORBA::Double CalculationsImpl::date2JD(CORBA::Long year, CORBA::Long month, CORBA::Double day) throw(CORBA::SystemException,csatErrors::DateOutOfRangeEx){
	
	char *_METHOD_="CalculationsImpl::date2JD";
	int A, B;
	CORBA::Double jd;
	bool isJulian = false; /* The date is from Gregorian or Julian Calendar */
	
	if( month < 1  || 12 < month ||
	    day   < 1  || 32 < day){
		csatErrors::DateOutOfRangeExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason","Month or day is out of limits");
		throw ex.getDateOutOfRangeEx();
	}
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

TYPES::RadecPos CalculationsImpl::precessionHR(const TYPES::RadecPos & pos, CORBA::Double jd0, CORBA::Double jd1) throw (CORBA::SystemException){

	double T, t;
	double zeta, z, theta;
	double A, B, C;
	TYPES::RadecPos newPos;

	T = (jd0 - date2JD(2000,1,1.5))/36525;
	t = (jd1 - jd0)/36525;

	zeta  = (2306.2181/3600 + 1.39656/3600*T - 0.000139/3600*T*T)*t + (0.30188/3600 - 0.000344/3600*T)*t*t + 0.017998/3600*t*t*t;
	z     = (2306.2181/3600 + 1.39656/3600*T - 0.000139/3600*T*T)*t + (1.09468/3600 + 0.000066/3600*T)*t*t + 0.018203/3600*t*t*t;
	theta = (2004.3109/3600 - 0.85330/3600*T - 0.000217/3600*T*T)*t - (0.42665/3600 + 0.000217/3600*T)*t*t - 0.041833/3600*t*t*t;

	A = DCOS(pos.dec)*DSIN(pos.ra + zeta);
	B = DCOS(theta)*DCOS(pos.dec)*DCOS(pos.ra + zeta) - DSIN(theta)*DSIN(pos.dec);
	C = DSIN(theta)*DCOS(pos.dec)*DCOS(pos.ra + zeta) + DCOS(theta)*DSIN(pos.dec);

	newPos.ra  = DATAN2(A,B) + z;
	newPos.dec = DASIN(C);

	return newPos;
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(CalculationsImpl)
  /* ----------------------------------------------------------------*/
  
  
  /*___oOo___*/
