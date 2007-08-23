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
import alma.acs.container.ContainerServices;
import alma.TELESCOPE_MODULE.TelescopeOperations;
import alma.TELESCOPE_MODULE.TelescopeImpl.TelescopeImpl;
import alma.acs.exceptions.AcsJException;
import alma.acs.component.ComponentLifecycleException;
//import si.ijs.maci.ComponentInfo;
import alma.acs.component.ComponentDescriptor;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;


public class TelescopeImpl implements TelescopeOperations, ComponentLifecycle {

	private ContainerServices m_containerServices;
	private Logger m_logger;
	private boolean working;
	private boolean stopping;

	private alma.TELESCOPE_MODULE.Telescope tele_comp;
	private alma.TRACKING_MODULE.Tracking trck_comp;
	private TelescopeWorker worker;

	org.omg.CORBA.Object obj = null;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices) throws ComponentLifecycleException {
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
                working = false;
                stopping = false;
		m_logger.info("initialize() called...");
		try {
			obj = m_containerServices.getDefaultComponent("IDL:alma/TRACKING_MODULE/Tracking:1.0");
			trck_comp = alma.TRACKING_MODULE.TrackingHelper.narrow(obj);
		}
		catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get Tracking component reference " + e);
			throw new ComponentLifecycleException("Failed to get Tracking component reference");
		}
	}

	public void execute() throws ComponentLifecycleException {
		m_logger.info("execute() called...");
	}

	public void cleanUp() {
		m_logger.info("cleanUp() called..., nothing to clean up.");
	}

	public void aboutToAbort() {
		if(working)
		{
			m_logger.fine("Trying to stop");
			//		try
			//		{
			stop();
			//		}
			//		catch(SchedulerAlreadyStoppedEx e)
			//		{
			//			m_logger.fine("Already Stopped");
			//		}
		}
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
	// Implementation of TelescopeOperations
	/////////////////////////////////////////////////////////////

	private double Alt = 0;
	private double Az = 0;
	private double Ra = 0;
	private double Dec = 0;
	private double ST = 0;

	public void goToAltAz(AltazPos p, AltazVel v, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc) {
		Alt = p.alt;
		Az = p.az;
		hor2radec(p);
	}

	public void getPos(RadecPosHolder p_rd, AltazPosHolder p_aa) {
		if(p_rd!=null)
		{
			p_rd.value = new RadecPos();
			p_rd.value.ra = Ra;
			p_rd.value.dec = Dec;
		}
		if(p_aa!=null)
		{
			p_aa.value = new AltazPos();
			p_aa.value.alt = Alt;
			p_aa.value.az = Az;
		}
		System.out.println("Llegue Tel Asdas by Mex");
		//System.out.println(p_aa.value.alt+" "+p_aa.value.az+" "+p_rd.value.ra+" "+p_rd.value.dec);
	}

	public void hor2radec(alma.TYPES.AltazPos p)
	{
		TelescopeConverter.altaz2radec(p.alt, p.az);
		Ra = TelescopeConverter.getRa();
		Dec = TelescopeConverter.getDec();
		ST = TelescopeConverter.getST();
	}

	public void radec2hor(alma.TYPES.RadecPos p)
	{
		TelescopeConverter.radec2altaz(p.ra, p.dec);
		Alt = TelescopeConverter.getAlt();
		Az = TelescopeConverter.getAz();
		ST = TelescopeConverter.getST();
	}

	public void preset(alma.TYPES.RadecPos p, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){

		Ra = p.ra;
		Dec = p.dec;
		radec2hor(p);

	}

	public double getSideralTime()
	{
		return ST;
	}

	public boolean isWorking()
	{	
		return working;
	}

	public void setWorking(boolean s)
	{
		working = s;
	}

	public void start() {//throws AcsJException{
		m_logger.info("Start called");

		if (!working && !stopping) {
			working = true;
/*
                try {
                        obj = m_containerServices.getDefaultComponent("IDL:alma/TELESCOPE_MODULE/Telescope:1.0");
                        tele_comp = alma.TELESCOPE_MODULE.TelescopeHelper.narrow(obj);
                } catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
                        m_logger.fine("Failed to get Telescope component reference " + e);
                        throw new ComponentLifecycleException("Failed to get Telescope component reference");
                }
*/
                try
                {
                        ComponentDescriptor s = m_containerServices.getComponentDescriptor("TELESCOPE");
                        obj = s.getComponent();
                        tele_comp = alma.TELESCOPE_MODULE.TelescopeHelper.narrow(obj);
                }
                catch(AcsJContainerServicesEx e)
                {
                        m_logger.fine("da");
                }

			worker = new TelescopeWorker(m_logger, trck_comp, tele_comp);
			worker.start();
		} else {
			m_logger.info("Already working");
			//AcsJException e = new AcsJException();
			//throw e;
		}
	}

	public void stop() {//throws AcsJException {
		m_logger.info("Stop called");

		if (!stopping)
			stopping = true;
		else {
			m_logger.info("Already trying to stop");

			//TODO: perhaps throw another exception here...
			return;
		}

		if (!working) {
			m_logger.info("Not yet working");
			stopping = false;
			//AcsJException e = new AcsJException();
			//throw e;
		}
		working = false;

		//block until last proposal finished
		if (worker != null) {
			try {
				m_logger.fine("Waiting for thread to die...");
				worker.join();
			} catch(Exception e) {
				m_logger.fine("Waiting for thread to die error");
			}
		}

		worker = null;
		stopping = false;

		m_logger.fine("Thread terminated");
	}
/*
	protected setRaDec(RadecPos p)
	{
		Ra = p.ra;
		Dec = p.dec;
	}

	protected setAltAz(AltazPos p)
	{
		Alt = p.alt;
		Az = p.az;
	}
*/
}
