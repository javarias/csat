/*
 *    ALMA - Atacama Large Millimiter Array
 *    (c) European Southern Observatory, 2002
 *    Copyright by ESO (in the framework of the ALMA collaboration),
 *    All rights reserved
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
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, 
 *    MA 02111-1307  USA
 */

package alma.CALCULATIONS_MODULE.CalculationsImpl;

import java.util.logging.Logger;
import java.util.*;

import alma.ACS.*;
import alma.TYPES.*;
import alma.acs.component.ComponentLifecycle;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.container.ContainerServices;
import alma.CALCULATIONS_MODULE.CalculationsOperations;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;

public class CalculationsImpl implements CalculationsOperations, ComponentLifecycle {

	private ContainerServices m_containerServices;
	private Logger m_logger;

	private double m_altOffset;
	private double m_azmOffset;

	private alma.LOCALE_MODULE.Locale locale_comp;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices) throws ComponentLifecycleException {
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		m_logger.info("initialize() called...");

		org.omg.CORBA.Object obj = null;

		/* We get the Locale referece */
		try{
			obj = m_containerServices.getDefaultComponent("IDL:alma/LOCALE_MODULE/Locale:1.0");
			locale_comp = alma.LOCALE_MODULE.LocaleHelper.narrow(obj);
		} catch (AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Locale default component reference");
			throw new ComponentLifecycleException("Failed to get Locale component reference");
		}
	}
    
	public void execute() {
		m_logger.info("execute() called...");
	}
    
	public void cleanUp() {
		m_logger.info("cleanUp() called..., nothing to clean up.");
	}
    
	public void aboutToAbort() {
		cleanUp();
		m_logger.info("managed to abort...");
	}
	
	/////////////////////////////////////////////////////////////
	// Implementation of ACSComponent
	/////////////////////////////////////////////////////////////
	
	public ComponentStates componentState() {
		return m_containerServices.getComponentStateManager().getCurrentState();
	}
	public String name() {
		return m_containerServices.getName();
	}

	/////////////////////////////////////////////////////////////
	// Implementation of CalculationsOperations
	/////////////////////////////////////////////////////////////

	public AltazPos Radec2Altaz(RadecPos position){
		
		double LAT;
		double HA;
		double LMST;
		double DEC;
		double RA;
		double ALT;
		double AZ;

		RA  = position.ra;
		DEC = position.dec;

		LMST = locale_comp.siderealTime();
		LAT  = locale_comp.localPos().latitude;

		HA = LMST - RA; //Se obtiene Hour Angle.
/*
		//Se obtiene la altitud en grados.
		ALT = Math.sin(DEC*Math.PI/180)*Math.sin(LAT*Math.PI/180);
		ALT += Math.cos(DEC*Math.PI/180)*Math.cos(LAT*Math.PI/180)*Math.cos(HA*Math.PI/180);
		ALT = Math.asin(ALT)*180/Math.PI;

		//Se obtiene el Azimuth en grados
		AZ = Math.sin(DEC*Math.PI/180) -Math.sin(ALT*Math.PI/180)*Math.sin(LAT*Math.PI/180);
		AZ /= Math.cos(ALT*Math.PI/180)*Math.cos(LAT*Math.PI/180);
		AZ = Math.acos(AZ)*180/Math.PI;
*/


		ALT = Math.sin(DEC*Math.PI/180)*Math.sin(LAT*Math.PI/180);
		ALT += Math.cos(DEC*Math.PI/180)*Math.cos(LAT*Math.PI/180)*Math.cos(HA*Math.PI/180);
		ALT = Math.asin(ALT)*180/Math.PI;


		AZ = Math.sin(LAT*Math.PI/180)*Math.cos(HA*Math.PI/180);
		AZ -= Math.tan(DEC*Math.PI/180)*Math.cos(LAT*Math.PI/180);
		AZ = Math.atan2(Math.sin(HA*Math.PI/180), AZ)*180/Math.PI;
		//AZ += 180;

/*
		AZ = Math.sin(DEC*Math.PI/180) -Math.sin(ALT*Math.PI/180)*Math.sin(LAT*Math.PI/180);
                AZ /= Math.cos(ALT*Math.PI/180)*Math.cos(LAT*Math.PI/180);
                AZ = Math.acos(AZ)*180/Math.PI;
*/

/*		while(ALT>360)
			ALT -= 360;
		while(ALT<0)
			ALT += 0;*/

		while(AZ>360)
			AZ -= 360;
		while(AZ<0)
			AZ += 360;

                AltazPos altazPos = new AltazPos();
                altazPos.alt = ALT;
                altazPos.az  = AZ;

		return altazPos;
	}

	public RadecPos Altaz2Radec(AltazPos position){

		double DEC;
		double HA;
		double RA;
		double LMST;
		double LAT;
		double ALT;
		double AZ;

		ALT = position.alt;
		AZ  = position.az;

		LAT  = locale_comp.localPos().latitude;
		LMST = locale_comp.siderealTime();

/*
		DEC = (180/Math.PI)*Math.asin((Math.cos(Math.PI*AZ/180)*Math.cos(Math.PI*ALT/180)*Math.cos(Math.PI*LAT/180)) + (Math.sin(Math.PI*ALT/180)*Math.sin(Math.PI*LAT/180)));

		HA = (180/Math.PI)*Math.acos((Math.sin(Math.PI*ALT/180) - (Math.sin(Math.PI*DEC/180)*Math.sin(Math.PI*LAT/180))) / (Math.cos(Math.PI*DEC/180)*Math.cos(Math.PI*LAT/180)));
*/

		//AZ = AZ-180;
		HA = Math.cos(Math.PI*AZ/180)*Math.sin(Math.PI*LAT/180);
		HA += Math.tan(Math.PI*ALT/180)*Math.cos(Math.PI*LAT/180);
		HA = Math.atan2(Math.sin(Math.PI*AZ/180),HA)*180/Math.PI;


		DEC = Math.sin(Math.PI*LAT/180)*Math.sin(Math.PI*ALT/180);
		DEC -= Math.cos(Math.PI*LAT/180)*Math.cos(Math.PI*ALT/180)*Math.cos(Math.PI*AZ/180);
		DEC = Math.asin(DEC)*180/Math.PI;

		RA = LMST - HA; //Se obtiene Hour Angle.

		while( RA < 0 )
			RA += 360;
		while( RA > 360 )
			RA -= 360;

/*
		while( DEC < 0 )
			DEC += 360;
		while( DEC > 360 )
			DEC -= 360;
*/
		RadecPos radecPos = new RadecPos();
		radecPos.ra  = RA;
		radecPos.dec = DEC;

		return radecPos;
	}
}
