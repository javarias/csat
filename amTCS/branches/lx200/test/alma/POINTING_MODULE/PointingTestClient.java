package alma.POINTING_MODULE;

import java.io.*;
import alma.TYPES.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import alma.ACS.CBvoid;
import alma.ACS.CBDescIn;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.ComponentClient;

import Hevelius.interfaz.*;
import alma.acs.callbacks.RequesterUtil;
import alma.acs.callbacks.ResponseReceiver;

public class PointingTestClient extends ComponentClient
{
	private alma.POINTING_MODULE.Pointing pointing_comp;

	public PointingTestClient(Logger logger, String managerLoc, String clientName)
		throws Exception {
			super(logger, managerLoc, clientName);
		}

	public void loadComponent() throws AcsJContainerServicesEx {
		pointing_comp = alma.POINTING_MODULE.PointingHelper.narrow(getContainerServices().getDefaultComponent("IDL:alma/POINTING_MODULE/Pointing:1.0"));
	}

	public static PointingTestClient start()
	{
		String managerLoc = System.getProperty("ACS.manager");
		if (managerLoc == null) {
			System.out.println("Java property 'ACS.manager' must be set to the corbaloc of the ACS manager!");
			System.exit(-1);
		}
		String clientName = "Pointing Test Client";
		PointingTestClient pointingC = null;
		try 
		{
			pointingC = new PointingTestClient(null, managerLoc, clientName);
			pointingC.loadComponent();
			return pointingC;
		}
		catch (Exception e) 
		{
			System.out.println("ACS has been found on this system, but it is not started.");
			try 
			{
				Logger logger = pointingC.getContainerServices().getLogger();
				logger.log(Level.SEVERE, "Client application failure", e);
			} catch (Exception e2) 
			{
				//e.printStackTrace(System.err);
			}
		}
		return null;
	}

	public static void stop(PointingTestClient pointingC)
	{
		if (pointingC != null) 
		{
			try 
			{
				pointingC.tearDown();
			}
			catch (Exception e3) 
			{
				// bad luck
				e3.printStackTrace();
			}
		}
	}

	public Pointing getPointingComponent(){
		return pointing_comp;
	}

}
