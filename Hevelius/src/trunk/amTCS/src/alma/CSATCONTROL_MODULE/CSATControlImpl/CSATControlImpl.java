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

import java.util.logging.Logger;

import alma.ACS.*;
import alma.TYPES.*;
import alma.acs.component.ComponentLifecycle;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.container.ContainerServices;
//import alma.acs.callbacks.ResponseReceiver;
//import alma.ACS.CBDescIn;
import alma.CSATCONTROL_MODULE.CSATControlOperations;
import alma.CSATCONTROL_MODULE.CSATControlImpl.CSATControlImpl;

public class CSATControlImpl implements CSATControlOperations, ComponentLifecycle {

	private ContainerServices m_containerServices;
	private Logger m_logger;
	private alma.POINTING_MODULE.Pointing pointing;
	private alma.TELESCOPE_MODULE.Telescope telescope;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices) throws ComponentLifecycleException {
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		m_logger.info("initialize() called...");

		// Get pointing instances
		org.omg.CORBA.Object obj = null;
		try {
			obj = m_containerServices.getDefaultComponent("IDL:alma/POINTING_MODULE/Pointing:1.0");
			pointing = alma.POINTING_MODULE.PointingHelper.narrow(obj);
		} catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Pointing component reference " + e);
			throw new ComponentLifecycleException("Failed to get Pointing component reference");
		}
		
		try {
                        obj = m_containerServices.getDefaultComponent("IDL:alma/TELESCOPE_MODULE/Telescope:1.0");
                        telescope = alma.TELESCOPE_MODULE.TelescopeHelper.narrow(obj);
                } catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
                        m_logger.fine("Failed to get Telescope component reference " + e);
                        throw new ComponentLifecycleException("Failed to get Telescope component reference");
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

		telescope.preset(p,cb,desc);
		pointing.resetOffset();
	}

	public void setTrackingStatus(boolean s){
	}

	public void setTrackingRate(alma.TYPES.RadecVel v){
	}

	public void goToRadec(alma.TYPES.RadecPos p, alma.TYPES.RadecVel v, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){
	}

	public void goToAltAz(alma.TYPES.AltazPos p, alma.TYPES.AltazVel v, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){

	}

	public void AltitudeOffSet(double degrees){
		pointing.AltitudeOffset(degrees);
	}

	public void AzimuthOffSet(double degrees){
		pointing.AzimuthOffset(degrees);
		//System.out.println("Alo");
	}

	public void getPreviewImage(alma.TYPES.ImageHolder img, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){
	}

	public void stopTelescope(){
	}

	public void getProImage(alma.TYPES.ImageHolder img, int id, double exptime, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){
	}

}
