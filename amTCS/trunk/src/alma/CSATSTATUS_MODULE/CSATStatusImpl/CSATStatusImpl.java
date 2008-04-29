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

import alma.ACS.CBDescIn;
import alma.ACS.CBvoid;
import alma.ACS.ComponentStates;

import alma.TYPES.RadecPos;
import alma.TYPES.RadecPosHolder;
import alma.TYPES.AltazPos;
import alma.TYPES.AltazPosHolder;
import alma.TYPES.RadecVel;
import alma.TYPES.EarthPos;

import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;

import alma.acs.component.ComponentLifecycle;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.container.ContainerServices;

import alma.CSATSTATUS_MODULE.CSATStatusOperations;
import alma.CSATSTATUS_MODULE.CSATStatusImpl.CSATStatusImpl;
import alma.CSATSTATUS_MODULE.TCSStatus;

public class CSATStatusImpl implements CSATStatusOperations, ComponentLifecycle {

	private ContainerServices m_containerServices;
	private Logger m_logger;

	private TCSStatus status;

	private alma.TELESCOPE_MODULE.Telescope telescope_comp;
	private alma.CALCULATIONS_MODULE.Calculations calculations_comp;
	private alma.TRACKING_MODULE.Tracking tracking_comp;
	private alma.SAFETY_MODULE.Safety safety_comp;
	private alma.POINTING_MODULE.Pointing pointing_comp;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices) throws ComponentLifecycleException {
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		m_logger.info("initialize() called...");

		status = TCSStatus.from_int(alma.CSATSTATUS_MODULE.TCSStatus._STOP);

		org.omg.CORBA.Object obj = null;

		/* We get the Telescope reference */
		try{
			obj = m_containerServices.getDefaultComponent("IDL:alma/TELESCOPE_MODULE/Telescope:1.0");
			telescope_comp = alma.TELESCOPE_MODULE.TelescopeHelper.narrow(obj);
		} catch (AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Telescope default component reference");
			throw new ComponentLifecycleException("Failed to get Telescope component reference");
		}

		/* We get the Calculations reference */
		try{
			obj = m_containerServices.getDefaultComponent("IDL:alma/CALCULATIONS_MODULE/Calculations:1.0");
			calculations_comp = alma.CALCULATIONS_MODULE.CalculationsHelper.narrow(obj);
		} catch (AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Calculations default component reference");
			throw new ComponentLifecycleException("Failed to get Calculations component reference");
		}

		/* We get the Tracking reference */
      try{
         obj = m_containerServices.getDefaultComponent("IDL:alma/TRACKING_MODULE/Tracking:1.0");
         tracking_comp = alma.TRACKING_MODULE.TrackingHelper.narrow(obj);
      } catch (AcsJContainerServicesEx e) {
         m_logger.fine("Failed to get Tracking default component reference");
         throw new ComponentLifecycleException("Failed to get Tracking component reference");
      }

		/* We get the Safety reference */
		try{
			obj = m_containerServices.getDefaultComponent("IDL:alma/SAFETY_MODULE/Safety:1.0");
			safety_comp = alma.SAFETY_MODULE.SafetyHelper.narrow(obj);
		} catch (AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Safety default component reference");
			throw new ComponentLifecycleException("Failed to get Safety component reference");
		}

		/* We get the Pointing reference */
		try{
			obj = m_containerServices.getDefaultComponent("IDL:alma/POINTING_MODULE/Pointing:1.0");
			pointing_comp = alma.POINTING_MODULE.PointingHelper.narrow(obj);
		} catch (AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Pointing default component reference");
			throw new ComponentLifecycleException("Failed to get Pointing component reference");
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

	public TCSStatus status(){
		return status;
	}
	
	/////////////////////////////////////////////////////////////
	// Implementation of CSATStatusOperations
	/////////////////////////////////////////////////////////////

	public void on(){
		status = TCSStatus.from_int(alma.CSATSTATUS_MODULE.TCSStatus._STAND_BY);
	}

	public void off(){
		status = TCSStatus.from_int(alma.CSATSTATUS_MODULE.TCSStatus._STOP);
	}

	public void setUncalibrated(){
		status = TCSStatus.from_int(alma.CSATSTATUS_MODULE.TCSStatus._CALIBRATING);
	}

	public void setCalibrated(AltazPos p){
		status = TCSStatus.from_int(alma.CSATSTATUS_MODULE.TCSStatus._STAND_BY);
	}

	public void initialize(CBvoid cb, CBDescIn desc){
		status = TCSStatus.from_int(alma.CSATSTATUS_MODULE.TCSStatus._READY);
	}

	public void stop(CBvoid cb,CBDescIn desc){
		telescope_comp.stop();
		status = TCSStatus.from_int(alma.CSATSTATUS_MODULE.TCSStatus._STAND_BY);
	}

	public void setMode(int mode){
		if( status.value() == alma.CSATSTATUS_MODULE.TCSStatus._READY)
			status = TCSStatus.from_int(alma.CSATSTATUS_MODULE.TCSStatus._AUTOMATIC);
		else if( status.value() == alma.CSATSTATUS_MODULE.TCSStatus._AUTOMATIC)
			status = TCSStatus.from_int(alma.CSATSTATUS_MODULE.TCSStatus._READY);
	}

	public void getPos(RadecPosHolder p_rd, AltazPosHolder p_aa){
		p_aa.value = telescope_comp.getAltAz();

		/* Correct back the pointing offsets */
		p_aa.value.az  -= pointing_comp.azmOffset();
		p_aa.value.alt -= pointing_comp.altOffset();

		/* Now convert to Ra/Dec coordinates */
		p_rd.value = calculations_comp.Altaz2Radec(p_aa.value);
	}

	public int getState(){
		return status.value();
	}

	public boolean getTrackingStatus(){
		return tracking_comp.status();
	}

	public RadecVel getTrackingRate(){
		return new RadecVel();
	}

	public int getSafety(RadecPos p){
		return safety_comp.getSafety(p);
	}

	public void EmergencyStop(){
	}

	public double getSiderealTime(){
		if( calculations_comp != null )
			return calculations_comp.siderealTime();
		return 0;
	}

	public EarthPos getLocalPos(){
		return new EarthPos();
	}

}
