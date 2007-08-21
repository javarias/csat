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

package alma.CSATSTATUS_MODULE.CSATStatusImpl;

import java.util.logging.Logger;

import alma.ACS.*;
import alma.TYPES.*;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ContainerServices;
import alma.CSATSTATUS_MODULE.CSATStatusOperations;
import alma.CSATSTATUS_MODULE.CSATStatusImpl.CSATStatusImpl;
import alma.CSATSTATUS_MODULE.*;

public class CSATStatusImpl implements CSATStatusOperations, ComponentLifecycle {

	private ContainerServices m_containerServices;
	private Logger m_logger;
	private RWTCSStatus m_status;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices) {
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		m_logger.info("initialize() called...");
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
	// Implementation of CSATStatusOperations
	/////////////////////////////////////////////////////////////

	public void on(){
	}

	public void off(){
	}

	public void setUncalibrated(){
	}

	public void setCalibrated(AltazPos p){
	}

	public void initialize(CBvoid cb, CBDescIn desc){
	}

	public void stop(CBvoid cb,CBDescIn desc){
	}

	public void setMode(int mode){
	}

	public void getPos(RadecPosHolder p_rd, AltazPosHolder p_aa){
		p_rd = new RadecPosHolder();
		p_aa = new AltazPosHolder();
	}

	public int getState(){
		return 0;
	}

	public boolean getTrackingStatus(){
		return false;
	}

	public RadecVel getTrackingRate(){
		return new RadecVel();
	}

	public int getSafety(RadecPos p){
		return 0;
	}

	public void EmergencyStop(){
	}

	public RWTCSStatus status(){
		return m_status;
	}
}
