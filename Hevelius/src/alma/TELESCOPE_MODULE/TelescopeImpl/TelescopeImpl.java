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
import alma.acs.callbacks.*;
import alma.ACS.CBDescIn;

import alma.TELESCOPE_MODULE.TelescopeOperations;

public class TelescopeImpl implements TelescopeOperations, ComponentLifecycle, Runnable {

	private static final double PRESITION = 0.1;
	private ContainerServices m_containerServices;
	private Logger m_logger;

	private AltazPos m_commandedPos;
	private AltazPos m_softRealPos;

	private RadecPos m_resultPos;

	private alma.DEVTELESCOPE_MODULE.DevTelescope devTelescope_comp;
	private alma.POINTING_MODULE.Pointing pointing_comp;
	private alma.CALCULATIONS_MODULE.Calculations calculations_comp;
	private alma.TRACKING_MODULE.Tracking tracking_comp;
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

		/* We get the Pointing reference */
		try{
			obj = m_containerServices.getDefaultComponent("IDL:alma/CALCULATIONS_MODULE/Calculations:1.0");
			calculations_comp = alma.CALCULATIONS_MODULE.CalculationsHelper.narrow(obj);
		} catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Calculations default component reference");
			throw new ComponentLifecycleException("Failed to get Calculations component reference");
		}

                /* We get the Tracking referece */
                try{
                        obj = m_containerServices.getDefaultComponent("IDL:alma/TRACKING_MODULE/Tracking:1.0");
                        tracking_comp = alma.TRACKING_MODULE.TrackingHelper.narrow(obj);
                } catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
                        m_logger.fine("Failed to get Tracking default component reference");
                        throw new ComponentLifecycleException("Failed to get Tracking component reference");
                }
		
		doControl = true;

		CompletionHolder completionHolder = new CompletionHolder();
		m_commandedPos = new AltazPos();
		m_softRealPos  = new AltazPos();

		m_resultPos = new RadecPos();

		m_commandedPos.alt = devTelescope_comp.realAlt().get_sync(completionHolder);
		m_commandedPos.az  = devTelescope_comp.realAzm().get_sync(completionHolder);

		m_softRealPos.alt = m_commandedPos.alt;
		m_softRealPos.az  = m_commandedPos.az;

		m_resultPos = calculations_comp.Altaz2Radec(m_softRealPos);
		
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

	private alma.ACS.CBvoid cb = null;
	private alma.ACS.CBDescIn descIn = null;

        private void preseting(RadecPos position){
                doControl = true;

                if( controlThread == null ){
                        controlThread = m_containerServices.getThreadFactory().newThread(this);
                        controlThread.start();
                }

                m_resultPos = new RadecPos();
                m_resultPos.ra = position.ra;
                m_resultPos.dec = position.dec;
                m_commandedPos = calculations_comp.Radec2Altaz(position);
        }

	public void preseting(RadecPos position, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){
		doControl = true;

		if( controlThread == null ){
			controlThread = m_containerServices.getThreadFactory().newThread(this);
			controlThread.start();
		}

		m_resultPos = new RadecPos();
		m_resultPos.ra = position.ra;
		m_resultPos.dec = position.dec;
		m_commandedPos = calculations_comp.Radec2Altaz(position);
		this.cb = cb;
		descIn = desc;
	}

	public RadecPos getRadec(){
		RadecPos p = new RadecPos();
		p.ra = m_resultPos.ra;
		p.dec = m_resultPos.dec;
		return (p);
	}

	public void offSet(AltazPos offset){
		m_commandedPos.alt += offset.alt;
		m_commandedPos.az += offset.az;
	}

	public void gotoAltAz(AltazPos position, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){
		m_commandedPos.alt = position.alt;
		m_commandedPos.az  = position.az;

		m_resultPos = calculations_comp.Altaz2Radec(position);

		doControl = true;

		if( controlThread == null ){
			controlThread = m_containerServices.getThreadFactory().newThread(this);
			controlThread.start();
		}
		this.cb = cb;
                descIn = desc;
	}

	public void stop(){
		doControl = false;
		
		if( controlThread != null ){
			try {
				controlThread.join();
			} catch (InterruptedException e) {
				m_logger.info("Cannot end the control thread");
			}
			controlThread = null;
		}
		
		AltazVel altazVel = new AltazVel(0,0);
		devTelescope_comp.setVel(altazVel);
	}

	public AltazPos getAltAz(){

		if( doControl == false && controlThread != null ){
			CompletionHolder completionHolder = new CompletionHolder();
			m_softRealPos.alt = devTelescope_comp.realAlt().get_sync(completionHolder);
			m_softRealPos.az  = devTelescope_comp.realAzm().get_sync(completionHolder);
		}

		return m_softRealPos;
	}

	public void setCurrentAltAz(AltazPos position){
		m_commandedPos = position;
		m_resultPos = calculations_comp.Altaz2Radec(position);
	}

	public void run() {
		m_logger.info("Starting Telescope control thread");
		
		CompletionHolder completionHolder = new CompletionHolder();
		AltazVel altazVel = new AltazVel();
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

				m_softRealPos.alt = realAltitude;
				m_softRealPos.az  = realAzimuth;
				
				/* We add to the commanded position the pointing corrections */
				commandedAltitude = m_commandedPos.alt + pointing_comp.altOffset();
				commandedAzimuth  = m_commandedPos.az + pointing_comp.azmOffset();
				
				/* We search which movement is shorter in azimuth (left or right) */
				if( commandedAzimuth > realAzimuth ){
					if( commandedAzimuth - realAzimuth > (realAzimuth + 360) - commandedAzimuth ){
						realAzimuth += 360;
					}
				}
				else if( commandedAzimuth < realAzimuth) {
					if( commandedAzimuth - (realAzimuth - 360) < realAzimuth - commandedAzimuth )
						realAzimuth -= 360;
				}
				
				/* We search which movement is shorter in altitude (up or down) */
				if( commandedAltitude > realAltitude ){
					if( commandedAltitude - realAltitude > (realAltitude + 360) - commandedAltitude ){
						realAltitude += 360;
					}
				}
				else if( commandedAltitude < realAltitude) {
					if( commandedAltitude - (realAltitude - 360) < realAltitude- commandedAltitude )
						realAltitude -= 360;
				}
				
				if(Math.abs(realAltitude - commandedAltitude) > PRESITION ){		

					diffAlt = commandedAltitude - realAltitude;

					/* Set the Alt velocity depending on the diffAlt */
					altazVel.altVel = 1;
					if( diffAlt < 0 ){
						altazVel.altVel =  -1;
						diffAlt        *= (-1);
					}

					     if( diffAlt <  0.1 )                  altazVel.altVel *= 2;
					else if( diffAlt >= 0.1 && diffAlt < 0.5 ) altazVel.altVel *= 3;
					else if( diffAlt >= 0.5 && diffAlt < 3 )   altazVel.altVel *= 6;
					else if( diffAlt >= 3   && diffAlt < 7 )   altazVel.altVel *= 7;
					else if( diffAlt >= 7   && diffAlt < 10)   altazVel.altVel *= 8;
					else if( diffAlt >= 10)                    altazVel.altVel *= 9;					     

					
				} else {
					altazVel.altVel = 0;
				}
				
				if(Math.abs(realAzimuth  - commandedAzimuth)  > PRESITION ){

					diffAzm = commandedAzimuth  - realAzimuth;
					/* Set the Azm velocity depending on the diffAzm */
					altazVel.azVel = 1;
					if( diffAzm < 0 ){
						altazVel.azVel =  -1;
						diffAzm       *= (-1);
					}

					     if( diffAzm <  0.1 )                  altazVel.azVel *= 2;
					else if( diffAzm >= 0.1 && diffAzm < 0.5 ) altazVel.azVel *= 3;
					else if( diffAzm >= 0.5 && diffAzm < 3 )   altazVel.azVel *= 6;
					else if( diffAzm >= 3   && diffAzm < 7 )   altazVel.azVel *= 7;
					else if( diffAzm >= 7   && diffAzm < 10)   altazVel.azVel *= 8;
					else if( diffAzm >= 10)                    altazVel.azVel *= 9;

				} else {
					altazVel.azVel = 0;
				}

				/* Send the velocity to the telescope */
				devTelescope_comp.setVel(altazVel);

				if(altazVel.azVel == 0 && altazVel.altVel == 0 && tracking_comp.status())
				{
					//m_logger.info("azVel = 0 y tracking=true");
					//preseting(m_resultPos);
					m_commandedPos = calculations_comp.Radec2Altaz(m_resultPos);
					m_logger.info(m_commandedPos.alt + "_" + m_commandedPos.az);
					if(cb!=null && descIn != null)
					{
						m_logger.info("azVel = 0 y tracking=true enviando callback");
						MyResponderUtil.respond(cb, descIn);
						cb=null;
						descIn = null;
					}
				}
				else if (altazVel.azVel == 0 && altazVel.altVel == 0)
				{
					//m_logger.info("azVel = 0 y tracking=false");
					m_resultPos = calculations_comp.Altaz2Radec(m_softRealPos);
					if(cb!=null && descIn != null)
                                        {
						m_logger.info("azVel = 0 y tracking=false enviando callback");
                                                MyResponderUtil.respond(cb, descIn);
                                                cb=null;
                                                descIn = null;
                                        }
				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			else break;
		}
		
	}
}
