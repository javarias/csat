package Hevelius.acsmodules;

import java.io.*;
//import alma.ACS.ComponentStates;
//import alma.acs.component.ComponentLifecycle;
//import alma.acs.container.ContainerServices;
//import alma.acs.component.ComponentImplBase;
//import alma.acs.component.ComponentLifecycleException;
//import alma.acs.exceptions.AcsJException;
import alma.TYPES.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.ComponentClient;
//import alma.demo.HelloDemo;

public class CSATControlClient extends ComponentClient
{
	/*	org.omg.CORBA.Object obj = null;
		alma.CSATCONTROL_MODULE.CSATControl cscontrol;
		Logger m_logger;
		private ContainerServices m_containerServices;
		public void connection() throws ComponentLifecycleException
		{
		try
		{
		obj = m_containerServices.getDefaultComponent("IDL:alma/CSATCONTROL_MODULE/CSATControlImpl:1.0");
		cscontrol = alma.CSATCONTROL_MODULE.CSATControlHelper.narrow(obj);

		}
		catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e)
		{
		m_logger.fine("Failed to get CONTROL component reference " + e);
		throw new ComponentLifecycleException("Failed to get CONTROL component reference");
		}
		}
		*/

	private alma.CSATCONTROL_MODULE.CSATControl cscontrol;

	/**
	 * @param logger
	 * @param managerLoc
	 * @param clientName
	 * @throws Exception
	 */
	public CSATControlClient(Logger logger, String managerLoc, String clientName)
		throws Exception {
		super(logger, managerLoc, clientName);
	}

	public void loadComponent() throws AcsJContainerServicesEx {
		cscontrol = alma.CSATCONTROL_MODULE.CSATControlHelper.narrow(getContainerServices().getDefaultComponent("IDL:alma/CSATCONTROL_MODULE/CSATControlImpl:1.0"));
	}

	public static CSATControlClient start()
	{
		String managerLoc = System.getProperty("ACS.manager");
		if (managerLoc == null) {
			System.out.println("Java property 'ACS.manager' must be set to the corbaloc of the ACS manager!");
			System.exit(-1);
		}
		String clientName = "CSATControlStatus";
		CSATControlClient csatcc = null;
		try {
			csatcc = new CSATControlClient(null, managerLoc, clientName);
			csatcc.loadComponent();
			return csatcc;
		}
		catch (Exception e) {
			try {
				Logger logger = csatcc.getContainerServices().getLogger();
				logger.log(Level.SEVERE, "Client application failure", e);
			} catch (Exception e2) {
				e.printStackTrace(System.err);
			}
		}
		finally {
			if (csatcc != null) {
				try {
					csatcc.tearDown();
				}
				catch (Exception e3) {
					// bad luck
					e3.printStackTrace();
				}
			}
			return null;
		}
	}

	public void preset(RadecPos p)
	{
		//		cscontrol.preset(p);
	}

	public void setTrackingStatus(boolean s)
	{
		cscontrol.setTrackingStatus(s);
	}

	public void setTrackingRate(RadecVel v)
	{
		cscontrol.setTrackingRate(v);
	}

	public void goToRadec(RadecPos p, RadecVel v)
	{
		//		cscontrol.goToRadec(p,v);
	}

	public void goToAltAz(AltazPos p, AltazVel v)
	{
		//		cscontrol.goToAltAz(p,v);
	}

	public void AltitudeOffSet(double degree)
	{
		cscontrol.AltitudeOffSet(degree);
	}

	public void AzimuthOffSet(double degree)
	{
		cscontrol.AzimuthOffSet(degree);
	}

	public void getPreviewImage(ImageHolder img)
	{
		//		cscontrol.getPreviewImage(img);
	}

	public void stopTelescope()
	{
		cscontrol.stopTelescope();
	}

	//	public void getProImage(int id, double exptime)
	//	{
	//		cscontrol.getProImage(id, exptime);
	//	}
}
