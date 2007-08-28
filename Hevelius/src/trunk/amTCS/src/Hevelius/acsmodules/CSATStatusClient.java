package Hevelius.acsmodules;

import java.io.*;
import alma.TYPES.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.ComponentClient;
//import alma.demo.HelloDemo;

public class CSATStatusClient extends ComponentClient
{
	private alma.CSATSTATUS_MODULE.CSATStatus csstatus;

	public CSATStatusClient(Logger logger, String managerLoc, String clientName)
		throws Exception {
			super(logger, managerLoc, clientName);
		}

	public void loadComponent() throws AcsJContainerServicesEx {
		csstatus = alma.CSATSTATUS_MODULE.CSATStatusHelper.narrow(getContainerServices().getDefaultComponent("IDL:alma/CSATSTATUS_MODULE/CSATStatus:1.0"));
	}

	public static CSATStatusClient start()
	{
		String managerLoc = System.getProperty("ACS.manager");
		if (managerLoc == null) 
		{
			System.out.println("Java property 'ACS.manager' must be set to the corbaloc of the ACS manager!");
			System.exit(-1);
		}
		String clientName = "Manager";
		CSATStatusClient csatsc = null;
		try 
		{
			csatsc = new CSATStatusClient(null, managerLoc, clientName);
			csatsc.loadComponent();
			return csatsc;
		}
		catch (Exception e) 
		{
			try 
			{
				Logger logger = csatsc.getContainerServices().getLogger();
				logger.log(Level.SEVERE, "Client application failure", e);
			} catch (Exception e2) 
			{
				e.printStackTrace(System.err);
			}
		}
		return null;
	}

	public static void stop(CSATStatusClient csatsc)
	{
		if (csatsc != null) 
		{
			try 
			{
				csatsc.tearDown();
			}
			catch (Exception e3) 
			{
				// bad luck
				e3.printStackTrace();
			}
		}
	}

	public void on()
	{
		if(csstatus!=null)
			csstatus.on();
	}

	public void stop()
	{
		if(csstatus!=null)
			csstatus.stop(null, null);
	}

	public void off()
	{
		if(csstatus!=null)
			csstatus.off();
	}

	public void setUncalibrated()
	{
		if(csstatus!=null)
			csstatus.setUncalibrated();
	}

	public void setCalibrated(AltazPos p)
	{
		if(csstatus!=null)
			csstatus.setCalibrated(p);
	}

	public void initialize()
	{
		if(csstatus!=null)
			csstatus.initialize(null, null);
	}

	public void getPos(RadecPosHolder r, AltazPosHolder a)
	{
		if(csstatus!=null)
			csstatus.getPos(r, a);
	}

	public int getState()
	{
		if(csstatus!=null)
			return csstatus.getState();
		return -1;
	}

	public boolean getTrackingStatus()
	{
		if(csstatus!=null)
			return csstatus.getTrackingStatus();
		return false;
	}

	public RadecVel getTrackingRate()
	{
		if(csstatus!=null)
			return csstatus.getTrackingRate();
		return null;
	}

	public int getSafety(RadecPos p)
	{
		if(csstatus!=null)
			return csstatus.getSafety(p);
		return -1;
	}

	public void EmergencyStop()
	{
		if(csstatus!=null)
			csstatus.EmergencyStop();
	}
	public void setMode(int s)
	{
		if(csstatus!=null)
			csstatus.setMode(s);
	}
	public double getSideralTime()
	{
		if(csstatus!=null)
			return csstatus.getSideralTime();
		return -1.0d;
	}

}
