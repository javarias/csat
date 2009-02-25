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

package alma.TELESCOPE_MODULE.EquatorialTelescopeImpl;

import java.util.logging.Logger;

import alma.ACS.*;
import alma.TYPES.*;
import alma.acs.component.ComponentLifecycle;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.container.ContainerServices;
import alma.ACSErr.CompletionHolder;

import alma.csatErrors.TelescopeAlreadyMovingEx;
import alma.csatErrors.wrappers.AcsJTelescopeAlreadyMovingEx;
import alma.acs.callbacks.*;
import alma.TELESCOPE_MODULE.EquatorialTelescopeOperations;

public class EquatorialTelescopeImpl implements EquatorialTelescopeOperations, ComponentLifecycle, Runnable {

	private static final double PRESITION = 0.01;
	private ContainerServices m_containerServices;
	private Logger m_logger;

	private AltazPos m_commandedPos;
	private RadecPos m_softRealPos;
	private RadecPos m_commandedRadecPos;

	private alma.DEVTELESCOPE_MODULE.DevTelescope devTelescope_comp;
	private alma.POINTING_MODULE.Pointing pointing_comp;
	private alma.CALCULATIONS_MODULE.Calculations calculations_comp;
	private Thread controlThread = null;
	
	private boolean doControl;
	private boolean moving;

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

		/* We get the Calculations reference */
		try{
			obj = m_containerServices.getDefaultComponent("IDL:alma/CALCULATIONS_MODULE/Calculations:1.0");
			calculations_comp = alma.CALCULATIONS_MODULE.CalculationsHelper.narrow(obj);
		} catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Calculations default component reference");
			throw new ComponentLifecycleException("Failed to get Calculations component reference");
		}

		doControl = true;
		moving = false;

		CompletionHolder completionHolder = new CompletionHolder();
		m_commandedPos = new AltazPos();
		m_softRealPos  = new RadecPos();
		m_commandedRadecPos = new RadecPos();

		m_commandedPos.az  = devTelescope_comp.realAzm().get_sync(completionHolder);
		m_commandedPos.alt = devTelescope_comp.realAlt().get_sync(completionHolder);

		m_commandedRadecPos.ra  = m_commandedPos.az + calculations_comp.siderealTime();
		m_commandedRadecPos.dec = m_commandedPos.alt;

		m_softRealPos = m_commandedRadecPos;

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
	// Implementation of EquatorialTelescopeOperations
	/////////////////////////////////////////////////////////////

	private alma.ACS.CBvoid cb = null;
	private alma.ACS.CBDescIn descIn = null;

	public void presetting(RadecPos position, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){
		doControl = true;

		if( controlThread == null ){
			controlThread = m_containerServices.getThreadFactory().newThread(this);
			controlThread.start();
		}

		if(moving)
			return;
		m_commandedPos.az  = position.ra - calculations_comp.siderealTime();
		m_commandedPos.alt = position.dec;
		m_commandedRadecPos.ra = position.ra;
		m_commandedRadecPos.dec = position.dec;
		this.cb = cb;
		descIn = desc;
	}

	public RadecPos getRadec(){
//		AltazPos p = new AltazPos();
//		CompletionHolder completionHolder = new CompletionHolder();
//		p.alt = devTelescope_comp.realAlt().get_sync(completionHolder);
//		p.az  = devTelescope_comp.realAzm().get_sync(completionHolder);
		
//		return calculations_comp.Altaz2Radec(p);	
		//return new RadecPos();
		return m_commandedRadecPos;
	}

	public void offSet(AltazPos offset){
		m_commandedPos.alt += offset.alt;
		m_commandedPos.az += offset.az;
	}

	public void gotoAltAz(AltazPos position, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){
		moving = true;
		doControl = true;

		if( controlThread == null ){
			controlThread = m_containerServices.getThreadFactory().newThread(this);
			controlThread.start();
		}

		m_commandedRadecPos = calculations_comp.Altaz2Radec(position);
		m_commandedPos.az  = m_commandedRadecPos.ra - calculations_comp.siderealTime();
		m_commandedPos.alt = m_commandedRadecPos.dec;

		moving = false;

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
			m_softRealPos.dec = devTelescope_comp.realAlt().get_sync(completionHolder);
			m_softRealPos.ra  = devTelescope_comp.realAzm().get_sync(completionHolder) - calculations_comp.siderealTime();
		}

		return calculations_comp.Radec2Altaz(m_softRealPos);
	}

	public void setCurrentAltAz(AltazPos position){
		m_commandedPos = position;
		m_commandedRadecPos = calculations_comp.Altaz2Radec(m_commandedPos);
	}

	public AltazVel getAltAzVel(){
		AltazVel altazVel = new AltazVel();
		CompletionHolder completionHolder = new CompletionHolder();
		altazVel.altVel = devTelescope_comp.altVel().get_sync(completionHolder);
		altazVel.azVel = devTelescope_comp.azmVel().get_sync(completionHolder);
		return altazVel;
	}

	public void setSlewRate(AltazVel vel) throws TelescopeAlreadyMovingEx {
		if(doControl && (getAltAzVel().altVel == 0 && getAltAzVel().azVel == 0))
		{
			doControl = false;
			if( controlThread != null ){
				try {
					controlThread.join();
				} catch (InterruptedException e) {
					m_logger.info("Cannot end the control thread");
				}
				controlThread = null;
			}
		}
		else if (doControl && (getAltAzVel().altVel != 0 || getAltAzVel().azVel != 0)) {
			AcsJTelescopeAlreadyMovingEx e = new AcsJTelescopeAlreadyMovingEx(new IllegalStateException("Telescope moving when trying to set Slew Rate"));
			throw e.toTelescopeAlreadyMovingEx();
		}

		devTelescope_comp.setVel(vel);
	}

	public void run() {
		m_logger.info("Starting Telescope control thread");
		
		CompletionHolder completionHolder = new CompletionHolder();
		AltazVel altazVel = new AltazVel();
		RadecPos tmprd = new RadecPos();
		RadecPos delta_rd;
		AltazPos tmpaa;
		double st;
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
				
				st = calculations_comp.siderealTime();
				/* We get the real values from the telescope */
				realAltitude = devTelescope_comp.realAlt().get_sync(completionHolder);
				realAzimuth  = devTelescope_comp.realAzm().get_sync(completionHolder);

				m_softRealPos.dec = realAltitude;
				m_softRealPos.ra  = realAzimuth + st;

				/* We add to the commanded position the Automatic pointing corrections */
				tmprd.ra = m_commandedPos.az + st;
				tmprd.dec = m_commandedPos.alt;
				delta_rd = pointing_comp.offSet(tmprd,st);
				if( delta_rd.ra != 0 || delta_rd.dec != 0){
					commandedAzimuth = tmprd.ra + delta_rd.ra - st;
					commandedAltitude = tmprd.dec + delta_rd.dec;
				}
				else{
					commandedAzimuth = m_commandedPos.az;
					commandedAltitude = m_commandedPos.alt;
				}
				
				/* We add to the commanded position the manual pointing corrections */
				if( pointing_comp.azmOffset() != 0 || pointing_comp.altOffset() != 0) {
					tmprd.ra  = commandedAzimuth + st;
					tmprd.dec = commandedAltitude;
					tmpaa = calculations_comp.Radec2Altaz(tmprd);
					tmpaa.alt += pointing_comp.altOffset();
					tmpaa.az  += pointing_comp.azmOffset();
					tmprd = calculations_comp.Altaz2Radec(tmpaa);

					commandedAzimuth  = tmprd.ra - st;
					commandedAltitude = tmprd.dec;
				}

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

					     if( diffAlt <  0.1 )                  altazVel.altVel *= 0.004;
					else if( diffAlt >= 0.1 && diffAlt < 0.4 ) altazVel.altVel *= 0.008;
					else if( diffAlt >= 0.4 && diffAlt < 0.5 ) altazVel.altVel *= 0.01;
					else if( diffAlt >= 0.5 && diffAlt < 1 )   altazVel.altVel *= 0.5;
					else if( diffAlt >= 1   && diffAlt < 3 )   altazVel.altVel *= 0.8;
					else if( diffAlt >= 3   && diffAlt < 5 )   altazVel.altVel *= 1;
					else if( diffAlt >= 5   && diffAlt < 10)   altazVel.altVel *= 2;
					else if( diffAlt >= 10  && diffAlt < 15)   altazVel.altVel *= 3;
					else if( diffAlt >= 15)                    altazVel.altVel *= 8;

					
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

					     if( diffAzm <  0.1 )                  altazVel.azVel *= 0.004;
					else if( diffAzm >= 0.1 && diffAzm < 0.4 ) altazVel.azVel *= 0.008;
					else if( diffAzm >= 0.4 && diffAzm < 0.5 ) altazVel.azVel *= 0.01;
					else if( diffAzm >= 0.5 && diffAzm < 1 )   altazVel.azVel *= 0.5;
					else if( diffAzm >= 1   && diffAzm < 3 )   altazVel.azVel *= 0.8;
					else if( diffAzm >= 3   && diffAzm < 5 )   altazVel.azVel *= 1;
					else if( diffAzm >= 5   && diffAzm < 10)   altazVel.azVel *= 2;
					else if( diffAzm >= 10  && diffAzm < 15)   altazVel.azVel *= 3;
					else if( diffAzm >= 15)                    altazVel.azVel *= 8;

				} else {
					altazVel.azVel = 0;
				}

				/* Send the velocity to the telescope */
				devTelescope_comp.setVel(altazVel);

				if(altazVel.azVel == 0 && altazVel.altVel == 0)
				{
					if(cb!=null && descIn != null)
					{
						m_logger.info("Presetting done");
						MyResponderUtil.respond(cb, descIn);
						cb=null;
						descIn = null;
					}
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			else break;
		}
		
	}
}
