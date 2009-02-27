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

package alma.CSATCONTROL_MODULE.CSATControlImpl;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import alma.ACS.ComponentStates;
import alma.CSATCONTROL_MODULE.CSATControlOperations;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.ComponentLifecycle;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.container.ContainerServices;
import alma.csatErrors.TelescopeAlreadyMovingEx;

public class CSATControlImpl implements CSATControlOperations, ComponentLifecycle {

	private ContainerServices m_containerServices;
	private Logger m_logger;
	private alma.POINTING_MODULE.Pointing pointing;
	private alma.TELESCOPE_MODULE.Telescope telescope;
	private alma.TRACKING_MODULE.Tracking tracking;
	private alma.CCD_MODULE.CCD ccd;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices)  throws ComponentLifecycleException  {

		int m_mount = -1;
		String defaultTelescope = null;
		com.cosylab.CDB.DAL dal = null;
		com.cosylab.CDB.DAO dao = null;
		org.omg.CORBA.Object obj = null;

		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		m_logger.info("initialize() called...");

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
				if(dao.get_string( "/" + componentName + "/Type").equals("IDL:alma/DEVTELESCOPE_MODULE/DevTelescope:1.0"))
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
				telescope = alma.TELESCOPE_MODULE.TelescopeHelper.narrow(obj);
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
				telescope = alma.TELESCOPE_MODULE.EquatorialTelescopeHelper.narrow(obj);
			} catch (AcsJContainerServicesEx e) {
				m_logger.fine("Failed to get EquatorialTelescope default component reference");
				throw new ComponentLifecycleException("Failed to get EquatorialTelescope component reference");
			}
		}
		
		try {
			obj = m_containerServices.getDefaultComponent("IDL:alma/POINTING_MODULE/Pointing:1.0");
			pointing = alma.POINTING_MODULE.PointingHelper.narrow(obj);
		}
		catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Pointing component reference " + e);
			throw new ComponentLifecycleException("Failed to get Pointing component reference");
		}
		
		try {
		 	obj = m_containerServices.getDefaultComponent("IDL:alma/TRACKING_MODULE/Tracking:1.0");
			tracking = alma.TRACKING_MODULE.TrackingHelper.narrow(obj);
			tracking.setTelescope(telescope);
		}
			catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Tracking component reference " + e);
			throw new ComponentLifecycleException("Failed to get Tracking component reference");
		}

		try {
		 	obj = m_containerServices.getDefaultComponent("IDL:alma/CCD_MODULE/CCD:1.0");
			ccd = alma.CCD_MODULE.CCDHelper.narrow(obj);
		}
			catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get CCD component reference " + e);
			throw new ComponentLifecycleException("Failed to get CCD component reference");
		}
	}

	public void execute() {
		//telescope.start();
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
	// Implementation of DataBaseOperations
	/////////////////////////////////////////////////////////////

	public void preset(alma.TYPES.RadecPos p, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){
		/*	ResponseReceiver cb  =  new ResponseReceiver() {

			public void incomingResponse(Object x) {
			System.out.println("Incoming Response: "+x);
			}
			public void incomingException(Exception x) {
			System.out.println("Responding failed: "+x);}

			};	

			CBDescIn desc = new CBDescIn();	*/

		//telescope.presetting(p,cb,desc);
		telescope.presetting(p,cb,desc);
		//pointing.resetOffset();
	}

	public void setTrackingStatus(boolean s){
		tracking.setStatus(s);
	}

	public void setTrackingRate(alma.TYPES.RadecVel v){
	}

	public void setSlewRate(alma.TYPES.AltazVel v) throws TelescopeAlreadyMovingEx {
		telescope.setSlewRate(v);
	}

	public void goToRadec(alma.TYPES.RadecPos p, alma.TYPES.RadecVel v, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){
		preset(p,cb,desc);
	}

	public void goToAltAz(alma.TYPES.AltazPos p, alma.TYPES.AltazVel v, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){
		telescope.gotoAltAz(p,cb,desc);
	}

	public void AltitudeOffSet(double degrees){
		pointing.offSetAlt(degrees);
	}

	public void AzimuthOffSet(double degrees){
		pointing.offSetAzm(degrees);
	}

	public void getPreviewImage(alma.TYPES.ImageHolder img, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){
			ccd.getPreview(img,cb,desc);
	}

	public void stopTelescope(){
		telescope.stop();
	}

	public void getProImage(alma.TYPES.ImageHolder img, int id, double exptime, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){
	}

}
