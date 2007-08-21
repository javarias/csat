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

package alma.POINTING_MODULE.PointingImpl;

import java.util.logging.Logger;

import alma.ACS.*;
import alma.TYPES.*;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ContainerServices;
import alma.POINTING_MODULE.PointingOperations;
import alma.POINTING_MODULE.PointingImpl.PointingImpl;
import alma.acs.component.ComponentLifecycleException;
import alma.TELESCOPE_MODULE.TelescopeImpl.TelescopeImpl;
import alma.CSATSTATUS_MODULE.CSATStatusImpl.CSATStatusImpl;
public class PointingImpl implements PointingOperations, ComponentLifecycle {

        private ContainerServices m_containerServices;
        private Logger m_logger;
	private alma.CSATSTATUS_MODULE.CSATStatus csatstatus;
	private alma.TELESCOPE_MODULE.Telescope tele_comp;
	private double alt;
	private double az;
	public double altoff = 0;
	public double azoff = 0;
		
	public void initialize(ContainerServices containerServices) throws ComponentLifecycleException{
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();

		m_logger.finer("Lifecycle initialize() called");

		// Get csatstatus instances
		org.omg.CORBA.Object obj = null;
//		try {
//			obj = m_containerServices.getDefaultComponent("IDL:alma/CSATSTATUS_MODULE/CSATStatus:1.0");
//			csatstatus = alma.CSATSTATUS_MODULE.CSATStatusHelper.narrow(obj);
//		} catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
//			m_logger.fine("Failed to get CSATStatus component reference " + e);
//			throw new ComponentLifecycleException("Failed to get CSATStatus component reference");
//		}
		try {
                        obj = m_containerServices.getDefaultComponent("IDL:alma/TELESCOPE_MODULE/Telescope:1.0");
                        tele_comp = alma.TELESCOPE_MODULE.TelescopeHelper.narrow(obj);
                } catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
                        m_logger.fine("Failed to get TELESCOPE component reference " + e);
                        throw new ComponentLifecycleException("Failed to get TELESCOPE component reference");
                }

		

	}

	public void execute() {
		m_logger.info("execute() called...");
	}

	public void cleanUp() {

//		if (csatstatus != null)
//			m_containerServices.releaseComponent(csatstatus.name());
		if (tele_comp != null)
                        m_containerServices.releaseComponent(tele_comp.name());
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
	public void AltitudeOffset(double degree){
		AltazPosHolder altazposh = new AltazPosHolder();	
		AltazPos altazpos = new AltazPos(); 
		RadecPosHolder r = null;
		tele_comp.getPos(r, altazposh);
		altazpos.alt = altazposh.value.alt+degree;
		altoff += degree;
		//tele_comp.goToAltAz(altazpos, null, null, null);
	}
	public void AzimuthOffset(double degree){
		AltazPosHolder altazposh = new AltazPosHolder();
		AltazPos altazpos = new AltazPos();
		RadecPosHolder r = null;
		tele_comp.getPos(r, altazposh);
		altazpos.az = altazposh.value.az+degree;
		azoff += degree;
		//tele_comp.goToAltAz(altazpos, null, null, null);

	}
	public void resetOffset(){
		azoff=0;
		altoff=0;
	}

}
