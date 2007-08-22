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
		csstatus = alma.CSATSTATUS_MODULE.CSATStatusHelper.narrow(getContainerServices().getDefaultComponent("IDL:alma/CSATSTATUS_MODULE/CSATStatusImpl:1.0"));
	}

	public static CSATStatusClient start()
	{
		String managerLoc = System.getProperty("ACS.manager");
		if (managerLoc == null) {
			System.out.println("Java property 'ACS.manager' must be set to the corbaloc of the ACS manager!");
			System.exit(-1);
		}
		String clientName = "CSATStatusClient";
		CSATStatusClient csatsc = null;
		try {
			csatsc = new CSATStatusClient(null, managerLoc, clientName);
			csatsc.loadComponent();
			return csatsc;
		}
		catch (Exception e) {
			try {
				Logger logger = csatsc.getContainerServices().getLogger();
				logger.log(Level.SEVERE, "Client application failure", e);
			} catch (Exception e2) {
				e.printStackTrace(System.err);
			}
		}
		return null;
	}

        public static void stop(CSATStatusClient csatsc)
        {
                if (csatsc != null) {
                        try {
                                csatsc.tearDown();
                        }
                        catch (Exception e3) {
                                // bad luck
                                e3.printStackTrace();
                        }
                }
        }

	public void on()
	{
		csstatus.on();
	}

	public void stop()
	{
		csstatus.stop(null, null);
	}

	public void off()
	{
		csstatus.off();
	}

	public void setUncalibrated()
	{
		csstatus.setUncalibrated();
	}

	public void setCalibrated(AltazPos p)
	{
		csstatus.setCalibrated(p);
	}

	public void initialize()
	{
		csstatus.initialize(null, null);
	}

	public void getPos(RadecPosHolder r, AltazPosHolder a)
	{
		csstatus.getPos(r, a);
	}

	public int getState()
	{
		return csstatus.getState();
	}

	public boolean getTrackingStatus()
	{
		return csstatus.getTrackingStatus();
	}

	public RadecVel getTrackingRate()
	{
		return csstatus.getTrackingRate();
	}

        public int getSafety(RadecPos p)
	{
		return csstatus.getSafety(p);
	}
	
	public void EmergencyStop()
	{
		csstatus.EmergencyStop();
	}
	public void setMode(int s)
	{
		csstatus.setMode(s);
	}

}
