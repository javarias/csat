package Hevelius.acsmodules;

import java.io.*;
import alma.TYPES.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import alma.ACS.CBvoid;
import alma.ACS.CBDescIn;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.ComponentClient;

public class CSATControlClient extends ComponentClient
{
	private alma.CSATCONTROL_MODULE.CSATControl cscontrol;

	public CSATControlClient(Logger logger, String managerLoc, String clientName)
		throws Exception {
			super(logger, managerLoc, clientName);
		}

	public void loadComponent() throws AcsJContainerServicesEx {
		cscontrol = alma.CSATCONTROL_MODULE.CSATControlHelper.narrow(getContainerServices().getDefaultComponent("IDL:alma/CSATCONTROL_MODULE/CSATControl:1.0"));
	}

	public static CSATControlClient start()
	{
		String managerLoc = System.getProperty("ACS.manager");
		if (managerLoc == null) {
			System.out.println("Java property 'ACS.manager' must be set to the corbaloc of the ACS manager!");
			System.exit(-1);
		}
		String clientName = "Manager";
		CSATControlClient csatcc = null;
		try 
		{
			csatcc = new CSATControlClient(null, managerLoc, clientName);
			csatcc.loadComponent();
			return csatcc;
		}
		catch (Exception e) 
		{
			System.out.println("ACS has been found on this system, but it is not started.");
			try 
			{
				Logger logger = csatcc.getContainerServices().getLogger();
				logger.log(Level.SEVERE, "Client application failure", e);
			} catch (Exception e2) 
			{
				//e.printStackTrace(System.err);
			}
		}
		return null;
	}

	public static void stop(CSATControlClient csatcc)
	{
		if (csatcc != null) 
		{
			try 
			{
				csatcc.tearDown();
			}
			catch (Exception e3) 
			{
				// bad luck
				e3.printStackTrace();
			}
		}
	}

	public void preset(RadecPos p)
	{
		if(cscontrol!=null)
		{
			//cscontrol.preset(p, new CBvoid(), new CBDescIn());
			//cscontrol.preset(p, null, new CBDescIn());
			//cscontrol.preset(p, new CBvoid(), new CBDescIn());
			CBDescIn desc = new CBDescIn(2000, 2000, 1);
			//alma.ACS.TimeInterval t1 = 2000;
			cscontrol.preset(p, null, desc);
		}
	}

	public void setTrackingStatus(boolean s)
	{
		if(cscontrol!=null)
		{
			cscontrol.setTrackingStatus(s);
		}
	}

	public void setTrackingRate(RadecVel v)
	{
		if(cscontrol!=null)
		{
			cscontrol.setTrackingRate(v);
		}
	}

	public void goToRadec(RadecPos p, RadecVel v)
	{
		if(cscontrol!=null)
		{
			cscontrol.goToRadec(p,v, null, null);
		}
	}

	public void goToAltAz(AltazPos p, AltazVel v)
	{
		if(cscontrol!=null)
		{
			CBDescIn desc = new CBDescIn(2000, 2000, 1);
			cscontrol.goToAltAz(p,v, null, desc);
		}
	}

	public void AltitudeOffSet(double degree)
	{
		if(cscontrol!=null)
		{
			cscontrol.AltitudeOffSet(degree);
		}
	}

	public void AzimuthOffSet(double degree)
	{
		if(cscontrol!=null)
		{
			cscontrol.AzimuthOffSet(degree);
		}
	}

	public void getPreviewImage(ImageHolder img)
	{
		if(cscontrol!=null)
		{
			cscontrol.getPreviewImage(img, null, null);
		}
	}

	public void stopTelescope()
	{
		if(cscontrol!=null)
		{
			cscontrol.stopTelescope();
		}
	}

	public void getProImage(ImageHolder img, int id, double exptime)
	{
		if(cscontrol!=null)
		{
			cscontrol.getProImage(img, id, exptime, null, null);
		}
	}
}
