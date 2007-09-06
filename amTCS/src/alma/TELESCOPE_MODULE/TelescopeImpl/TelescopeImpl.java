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

package alma.TELESCOPE_MODULE.TelescopeImpl;

import java.util.logging.Logger;

import alma.ACS.*;
import alma.TYPES.*;
import alma.acs.component.ComponentLifecycle;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.container.ContainerServices;
import alma.ACSErr.CompletionHolder;

import alma.TELESCOPE_MODULE.TelescopeOperations;

public class TelescopeImpl implements TelescopeOperations, ComponentLifecycle {

	private ContainerServices m_containerServices;
	private Logger m_logger;

	private AltazPos m_commandedPos;

	private alma.DEVTELESCOPE_MODULE.DevTelescope devTelescope_comp;

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
			obj = m_containerServices.getDefaultComponent("IDL:alma/DEVTELESCOPE_MODULE/DevTelescope:1.0");
			devTelescope_comp = alma.DEVTELESCOPE_MODULE.DevTelescopeHelper.narrow(obj);
		} catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get DevTelescope default component reference");
			throw new ComponentLifecycleException("Failed to get DevTelescope component reference");
		}

		m_commandedPos = new AltazPos();
	}
    
	public void execute() {
		m_logger.info("execute() called...");
	}
    
	public void cleanUp() {
		if( devTelescope_comp != null )
			m_containerServices.releaseComponent(devTelescope_comp.name());

		m_logger.info("cleanUp() called");
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

	public AltazPos commandedPos(){
		return m_commandedPos;
	}
	
	/////////////////////////////////////////////////////////////
	// Implementation of TelescopeOperations
	/////////////////////////////////////////////////////////////

	public void preseting(RadecPos position){
	}

	public RadecPos getRadec(){
		return new RadecPos();
	}

	public void offSet(AltazPos offset){
		m_commandedPos.alt += offset.alt;
		m_commandedPos.az += offset.az;
	}

	public void gotoAltAz(AltazPos position){
		m_commandedPos = position;
	}

	public void stop(){
	}

	public AltazPos getAltAz(){
		if( devTelescope_comp != null ){

			AltazPos position = new AltazPos();
			CompletionHolder completionHolder = new CompletionHolder();

			position.alt = devTelescope_comp.realAlt().get_sync(completionHolder);
			position.az  = devTelescope_comp.realAzm().get_sync(completionHolder);

			return position;
		}

		return null;
	}

	public void setCurrentAltAz(AltazPos position){
		m_commandedPos = position;
	}
}
