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

import java.util.StringTokenizer;
import java.util.logging.Logger;

import alma.ACS.CBDescIn;
import alma.ACS.CBvoid;
import alma.ACS.ComponentStates;

import alma.TYPES.RadecPos;
import alma.TYPES.RadecPosHolder;
import alma.TYPES.AltazPos;
import alma.TYPES.AltazPosHolder;
import alma.TYPES.RadecVel;
import alma.TYPES.AltazVel;
import alma.TYPES.EarthPos;
import alma.TYPES.PointingModel;

import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;

import alma.acs.component.ComponentLifecycle;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.container.ContainerServices;

import alma.acsErrTypeLifeCycle.*;
import alma.acsErrTypeLifeCycle.wrappers.*;

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
	private alma.LOCALE_MODULE.Locale locale_comp;

	private RadecPos p_theorical;
	private boolean observing_for_pointing;
	
	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices) throws ComponentLifecycleException {

		int m_mount = -1;
		observing_for_pointing = false;
		String defaultTelescope = null;
		com.cosylab.CDB.DAL dal = null;
		com.cosylab.CDB.DAO dao = null;
		org.omg.CORBA.Object obj = null;

		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		m_logger.info("initialize() called...");

		status = TCSStatus.STOP;

		/* Search across the CDB for the default DevTelescope component */
		try {
			dal = m_containerServices.getCDB();
			dao = dal.get_DAO_Servant("MACI/Components");
			String components = dao.get_string("/_characteristics");
			StringTokenizer tokenizer = new StringTokenizer(components, ",");
			while (tokenizer.hasMoreTokens())
			{
				String subname = tokenizer.nextToken().toString();
				String componentName = subname;
				if(dao.get_string("/" + componentName + "/Type").equals("IDL:alma/DEVTELESCOPE_MODULE/DevTelescope:1.0"))
					if( dao.get_string( "/" + componentName + "/Default").equals("true") ) {
						defaultTelescope = componentName;
						break;
					}
			}
			//dao.destroy();
		} catch(Exception e) {
			m_logger.fine("Couldn't retrieve CDB information for components");
			throw new ComponentLifecycleException("Failed to get the componets list from the CDB");
		}

		/* Get the mount type for the default telescope */
		try {
			dal = m_containerServices.getCDB();
			dao = dal.get_DAO_Servant("alma/" + defaultTelescope);
			m_mount = dao.get_long("mount/default_value");
			//dao.destroy();
		} catch (Exception e) {
			m_logger.fine("Couldn't retrieve mount type for the telescope");
			throw new ComponentLifecycleException("Failed to get the mount type for the default physical telescope");
		}

		/* Depending on the mount type we get the telescope reference */
		if( m_mount == alma.DEVTELESCOPE_MODULE.mountType._ALTAZ ) {
			m_logger.info("Starting Telescope (we have an Alt/Az telescope here)");
			/* We get the Telescope reference */
			try{
				obj = m_containerServices.getDefaultComponent("IDL:alma/TELESCOPE_MODULE/Telescope:1.0");
				telescope_comp = alma.TELESCOPE_MODULE.TelescopeHelper.narrow(obj);
			} catch (AcsJContainerServicesEx e) {
				m_logger.fine("Failed to get Telescope default component reference");
				throw new ComponentLifecycleException("Failed to get Telescope component reference");
			}
		}
		else if( m_mount == alma.DEVTELESCOPE_MODULE.mountType._EQUATORIAL) {
			m_logger.info("Starting EquatorialTelescope (we have an equatorial telescope here)");
			/* We get the EquatorialTelescope reference */
			try{
				obj = m_containerServices.getDefaultComponent("IDL:alma/TELESCOPE_MODULE/EquatorialTelescope:1.0");
				telescope_comp = alma.TELESCOPE_MODULE.EquatorialTelescopeHelper.narrow(obj);
			} catch (AcsJContainerServicesEx e) {
				m_logger.fine("Failed to get EquatorialTelescope default component reference");
				throw new ComponentLifecycleException("Failed to get EquatorialTelescope component reference");
			}
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
			tracking_comp.setTelescope(telescope_comp);
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

		/* We get the Locale reference */
		try{
			obj = m_containerServices.getDefaultComponent("IDL:alma/LOCALE_MODULE/Locale:1.0");
			locale_comp = alma.LOCALE_MODULE.LocaleHelper.narrow(obj);
		} catch (AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Lointing default component reference");
			throw new ComponentLifecycleException("Failed to get Lointing component reference");
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

	public void on() throws LifeCycleEx {
		
		if( status != TCSStatus.STOP ) {
			AcsJLifeCycleEx ex = new AcsJLifeCycleEx(new IllegalStateException("TCS is in " + status + " state"));
			throw ex.toLifeCycleEx();
		}
		status = TCSStatus.STAND_BY;
	}

	public void off() throws LifeCycleEx {
		
		if( status != TCSStatus.STAND_BY ) {
			AcsJLifeCycleEx ex = new AcsJLifeCycleEx(new IllegalStateException("TCS is in " + status + " state"));
			throw ex.toLifeCycleEx();
		}
		status = TCSStatus.STOP;
	}

	public void setUncalibrated() throws LifeCycleEx {
		
		if( status != TCSStatus.STAND_BY ) {
			AcsJLifeCycleEx ex = new AcsJLifeCycleEx(new IllegalStateException("TCS is in " + status + " state"));
			throw ex.toLifeCycleEx();
		}
		status = TCSStatus.CALIBRATING;
		pointing_comp.resetAdjusts();
		pointing_comp.setState(true, PointingModel.MANUAL);
		pointing_comp.setState(false, PointingModel.AUTOMATIC);
		tracking_comp.setStatus(true);
	}

	public void setCalibrated() throws LifeCycleEx {
		
		if( status != TCSStatus.CALIBRATING ) {
			AcsJLifeCycleEx ex = new AcsJLifeCycleEx(new IllegalStateException("TCS is in " + status + " state"));
			throw ex.toLifeCycleEx();
		}
		pointing_comp.calculateCoeffs();
		pointing_comp.setState(false, PointingModel.MANUAL);
		pointing_comp.setState(true, PointingModel.AUTOMATIC);
		status = TCSStatus.STAND_BY;
	}

	public void initialize(CBvoid cb, CBDescIn desc) throws LifeCycleEx {
		
		if( status != TCSStatus.STAND_BY ) {
			AcsJLifeCycleEx ex = new AcsJLifeCycleEx(new IllegalStateException("TCS is in " + status + " state"));
			throw ex.toLifeCycleEx();
		}
		status = TCSStatus.READY;
	}

	public void stop(CBvoid cb,CBDescIn desc) throws LifeCycleEx {
		
		if( status != TCSStatus.READY ) {
			AcsJLifeCycleEx ex = new AcsJLifeCycleEx(new IllegalStateException("TCS is in " + status + " state"));
			throw ex.toLifeCycleEx();
		}
		telescope_comp.stop();
		status = TCSStatus.STAND_BY;
	}

	public void setMode(int mode) throws LifeCycleEx {
		
		if( status != TCSStatus.READY && status != TCSStatus.AUTOMATIC ) {
			AcsJLifeCycleEx ex = new AcsJLifeCycleEx(new IllegalStateException("TCS is in " + status + " state"));
			throw ex.toLifeCycleEx();
		}
		
		if( status.value() == alma.CSATSTATUS_MODULE.TCSStatus._READY)
			status = TCSStatus.from_int(alma.CSATSTATUS_MODULE.TCSStatus._AUTOMATIC);
		else if( status.value() == alma.CSATSTATUS_MODULE.TCSStatus._AUTOMATIC)
			status = TCSStatus.from_int(alma.CSATSTATUS_MODULE.TCSStatus._READY);
	}

	public void getPos(RadecPosHolder p_rd, AltazPosHolder p_aa){
		RadecPos delta_rd, tmp_rd;
		p_aa.value = telescope_comp.getAltAz();

		/* Correct back the manual pointing offsets */
		if(pointing_comp.getState(PointingModel.MANUAL)){
			p_aa.value.az  -= pointing_comp.azmOffset();
			p_aa.value.alt -= pointing_comp.altOffset();
		}

		/* Now convert to Ra/Dec coordinates */
		p_rd.value = calculations_comp.Altaz2Radec(p_aa.value);

		/* Correct back the automatic pointing offsets */
		if(pointing_comp.getState(PointingModel.AUTOMATIC)){
			tmp_rd = telescope_comp.getRadec();
			delta_rd = pointing_comp.offSet(tmp_rd,getSiderealTime());
			p_rd.value.ra -= delta_rd.ra;
			p_rd.value.dec -= delta_rd.dec;
			p_aa.value = calculations_comp.Radec2Altaz(p_rd.value);
		}
	}

	public TCSStatus getState(){
		return status;
	}

	public boolean getTrackingStatus(){
		return tracking_comp.status();
	}

	public AltazVel getSlewRate(){
		return telescope_comp.getAltAzVel();
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
		if( locale_comp != null )
			return locale_comp.localPos();
		return new EarthPos(-1,-1);
	}

	public void addPointingObs() throws LifeCycleEx {
		
		if( status != TCSStatus.CALIBRATING ) {
			AcsJLifeCycleEx ex = new AcsJLifeCycleEx(new IllegalStateException("TCS is in " + status + " state"));
			throw ex.toLifeCycleEx();
		}
		
		if( observing_for_pointing )
			return;
		
		pointing_comp.resetAdjusts();
		p_theorical = telescope_comp.getRadec();
		observing_for_pointing = true;
	}
	
	public void acceptPointingObs(boolean acceptance) throws LifeCycleEx {

		if( status != TCSStatus.CALIBRATING ) {
			AcsJLifeCycleEx ex = new AcsJLifeCycleEx(new IllegalStateException("TCS is in " + status + " state"));
			throw ex.toLifeCycleEx();
		}

		if( !observing_for_pointing )
			return;
		observing_for_pointing = false;
		if( !acceptance )		
			return;
		
		RadecPos p_experimental = telescope_comp.getRadec();
		pointing_comp.addObs(p_theorical, p_experimental, getSiderealTime());
	}
}
