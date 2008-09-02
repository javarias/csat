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

package alma.LOCALE_MODULE.LocaleImpl;

import java.util.logging.Logger;

import alma.ACS.ComponentStates;
import alma.ACSErr.CompletionHolder;
import alma.DEVGPS_MODULE.DevGPS;
import alma.LOCALE_MODULE.LocaleOperations;
import alma.TYPES.EarthPos;
import alma.TYPES.TimeVal;
import alma.acs.component.ComponentLifecycle;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.container.ContainerServices;

public class LocaleImpl implements LocaleOperations, ComponentLifecycle {

	private ContainerServices m_containerServices;
	private Logger m_logger;

	private DevGPS devGPS_comp;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices) throws ComponentLifecycleException {
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		m_logger.info("initialize() called...");

		org.omg.CORBA.Object obj = null;

		/* We get the DevTelescope reference */
		try{
			obj = m_containerServices.getDefaultComponent("IDL:alma/DEVGPS_MODULE/DevGPS:1.0");
			devGPS_comp = alma.DEVGPS_MODULE.DevGPSHelper.narrow(obj);
		} catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get DevGPS default component reference");
			throw new ComponentLifecycleException("Failed to get DevGPS component reference");
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
	// Implementation of LocaleOperations
	/////////////////////////////////////////////////////////////
	//
	public EarthPos localPos(){
		CompletionHolder completionHolder = new CompletionHolder();
		EarthPos earthPos  = new EarthPos();
		earthPos.latitude  = devGPS_comp.latitude().get_sync(completionHolder);
		earthPos.longitude = devGPS_comp.longitude().get_sync(completionHolder);
		return earthPos;
	}

	public TimeVal time() {
		TimeVal timeVal = new TimeVal();
		timeVal = devGPS_comp.time();
		return timeVal;
	}
}
