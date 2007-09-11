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

public class TelescopeImpl implements TelescopeOperations, ComponentLifecycle, Runnable {

	private static final double PRESITION = 0.5;
	private ContainerServices m_containerServices;
	private Logger m_logger;

	private AltazPos m_commandedPos;

	private alma.DEVTELESCOPE_MODULE.DevTelescope devTelescope_comp;
	private alma.POINTING_MODULE.Pointing pointing_comp;
	private Thread controlThread = null;
	
	private boolean doControl;

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

		/* We get the Pointing reference */
		try{
			obj = m_containerServices.getDefaultComponent("IDL:alma/POINTING_MODULE/Pointing:1.0");
			pointing_comp = alma.POINTING_MODULE.PointingHelper.narrow(obj);
		} catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Pointing default component reference");
			throw new ComponentLifecycleException("Failed to get Pointing component reference");
		}
		
		m_commandedPos = new AltazPos();
		
		doControl = true;
		
		controlThread = m_containerServices.getThreadFactory().newThread(this);
		controlThread.start();
	}
    
	public void execute() {
		m_logger.info("execute() called...");
	}
    
	public void cleanUp() {
		doControl = false;
		if( controlThread != null ) {
			try {
				controlThread.join();
			} catch (InterruptedException e) {
				m_logger.info("Cannot end the control thread");
			}
		}
		
		if( devTelescope_comp != null )
			m_containerServices.releaseComponent(devTelescope_comp.name());

		if( pointing_comp != null )
			m_containerServices.releaseComponent(pointing_comp.name());
		
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
		doControl = true;

		if( controlThread != null ){
			controlThread = m_containerServices.getThreadFactory().newThread(this);
			controlThread.start();
		}

		m_commandedPos = new AltazPos(0,0);
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

		doControl = true;

		if( controlThread != null ){
			controlThread = m_containerServices.getThreadFactory().newThread(this);
			controlThread.start();
		}
	}

	public void stop(){
		doControl = false;
		devTelescope_comp.setVel(new AltazVel(0,0));
		if( controlThread != null ){
			try {
				controlThread.join();
			} catch (InterruptedException e) {
				m_logger.info("Cannot end the control thread");
			}
		}
		controlThread = null;
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

	public void run() {
		m_logger.info("Starting Telescope control thread");
		
		CompletionHolder completionHolder = new CompletionHolder();
		double realAltitude;
		double realAzimuth;
		double commandedAltitude;
		double commandedAzimuth;
		double diffAlt;
		double diffAzm;
		
		
		/* Main loop control */
		while(true){
			
			/* Just control the telescope if doControl is true */
			if(doControl){
				
				/* We get the real values from the telescope */
				realAltitude = devTelescope_comp.realAlt().get_sync(completionHolder);
				realAzimuth  = devTelescope_comp.realAzm().get_sync(completionHolder);
				
				/* We add to the commanded position the pointing corrections */
				commandedAltitude = m_commandedPos.alt + pointing_comp.altOffset();
				commandedAzimuth  = m_commandedPos.az + pointing_comp.azmOffset();
				
				if(realAltitude - commandedAltitude > PRESITION ||
				   realAzimuth  - commandedAzimuth  > PRESITION ){

					AltazVel altazVel = new AltazVel();

					/* Diffs from PRESITION to 1º */
					diffAlt = commandedAltitude - realAltitude;
					diffAzm = commandedAzimuth  - realAzimuth ;

					/* Set the Alt velocity depending on the diffAlt */
					altazVel.altVel = 1;
					if( diffAlt < 0 ){
						altazVel.altVel =  -1;
						diffAlt        *= (-1);
					}

					     if( diffAlt <  1 )                altazVel.altVel *= 3;
					else if( diffAlt >= 1 && diffAlt < 3 ) altazVel.altVel *= 6;
					else if( diffAlt >= 3 && diffAlt < 7 ) altazVel.altVel *= 7;
					else if( diffAlt >= 7 && diffAlt < 10) altazVel.altVel *= 8;
					else if( diffAlt >= 10)                altazVel.altVel *= 9;

					/* Set the Azm velocity depending on the diffAzm */
					altazVel.azVel = 1;
					if( diffAzm < 0 ){
						altazVel.azVel =  -1;
						diffAzm       *= (-1);
					}

					     if( diffAzm <  1 )                altazVel.azVel *= 1;
					else if( diffAzm >= 1 && diffAzm < 3 ) altazVel.azVel *= 2;
					else if( diffAzm >= 3 && diffAzm < 5 ) altazVel.azVel *= 5;
					else if( diffAzm >= 5 && diffAzm < 7 ) altazVel.azVel *= 7;
					else if( diffAzm >= 7 && diffAzm < 10) altazVel.azVel *= 8;
					else if( diffAzm >= 10)                altazVel.azVel *= 9;

					devTelescope_comp.setVel(altazVel);
				}
				/* We stop if we're there */
				else{
					devTelescope_comp.setVel(new AltazVel(0,0));
				}
				
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			else break;
			
		}
		
	}
}
