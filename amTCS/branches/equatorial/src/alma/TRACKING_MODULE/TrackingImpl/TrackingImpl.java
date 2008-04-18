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

package alma.TRACKING_MODULE.TrackingImpl;

import java.util.logging.Logger;

import alma.ACS.*;
import alma.TYPES.*;
import alma.acs.component.ComponentLifecycle;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.container.ContainerServices;
import alma.TRACKING_MODULE.TrackingOperations;

public class TrackingImpl implements TrackingOperations, ComponentLifecycle, Runnable {

	private ContainerServices m_containerServices;
	private Logger m_logger;

	private boolean m_status;
	private RadecVel m_rate;

	private alma.TELESCOPE_MODULE.Telescope telescope_comp;
	private Thread trackingThread = null;

	private boolean doControl;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices) throws ComponentLifecycleException {
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		m_logger.info("initialize() called...");

		org.omg.CORBA.Object obj = null;
		try{
			obj = m_containerServices.getDefaultComponent("IDL:alma/TELESCOPE_MODULE/Telescope:1.0");
			telescope_comp = alma.TELESCOPE_MODULE.TelescopeHelper.narrow(obj);
		} catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Telescope default component reference");
			throw new ComponentLifecycleException("Failed to get Telescope component reference");
		}

		doControl = true;

		m_status = false;
		m_rate = new RadecVel();
	}

	public void execute() {
		m_logger.info("execute() called...");
	}

	public void cleanUp() {
		m_logger.info("cleanUp() called");
		doControl = false;
		if( trackingThread != null) {
			try {
         	trackingThread.join();
			} catch (InterruptedException e) {
				m_logger.info("Cannot end the tracking control thread");
			}
		}
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

	public RadecVel rate(){
			  return m_rate;
	}

	public boolean status(){
			  return m_status;
	}

	/////////////////////////////////////////////////////////////
	// Implementation of TrackingOperations
	/////////////////////////////////////////////////////////////

	public void setStatus(boolean status){
		this.m_status = status;
		if(status == true) {
			if( trackingThread == null)
			{
				if(telescope_comp.getAltAzVel().altVel == 0 && telescope_comp.getAltAzVel().azVel == 0)
					telescope_comp.gotoAltAz(telescope_comp.getAltAz(), null, null);
				trackingThread = m_containerServices.getThreadFactory().newThread(this);
				trackingThread.start();
			}
			else
				m_logger.info("Tracking already running");
		}
		else {
			if( trackingThread != null){
				try {
					trackingThread.join();
				} catch (InterruptedException e) {
					m_logger.info("Cannot end the tracking control thread");
				}
				trackingThread = null;
			}
		}
	}

	public void setRate(RadecVel rate){
		this.m_rate = rate;
	}

	public void run() {
		m_logger.info("Starting Tracking Control Thread");

		RadecPos pos = null;
		/* Tracking Control loop */
		while(true){
			if(m_status && doControl){
				if(telescope_comp != null) {
					m_logger.info("Adjusting position according to tracking");
					pos = telescope_comp.getRadec();
					telescope_comp.presetting(pos, null, null);
				}
			}
			else break;
		}
	}
}
