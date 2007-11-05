/**
* CSATStatusClient is a class that is used to connect with ACS component CSATStatus.
* For this purpose it extends ComponentClient which implements most of the necessary methods
* to connect with an ACS component. Still is needed to implement some methods for this task 
* because they are particular for each component. Also there is the need to implement most
* methods from CSATStatusOperations interface which where generated from IDL interface. This 
* is made in order to allow the use of component's methods by making calls to methods of 
* objects of this client.
*/

package Hevelius.acsmodules;

import java.io.*;
import alma.TYPES.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.ComponentClient;

import Hevelius.interfaz.*;
//import alma.demo.HelloDemo;

public class CSATStatusClient extends ComponentClient
{
	private alma.CSATSTATUS_MODULE.CSATStatus csstatus;

	/**
	* This is a constructor for CSATStatusClient instance. 
        * @param logger   	Logger is a reference to a ACS Logger.
	* @param managerLoc 	String System's managerLoc.
	* @param clientName	String clientName for ACS.
	* @throws Exception	Throws any exception that may ocurr.
	*/
	public CSATStatusClient(Logger logger, String managerLoc, String clientName)
		throws Exception {
			super(logger, managerLoc, clientName);
		}

	/**
	* This method is intended to retrieve component from ACS, as of now it retrieves 
	* default CSATStatus component.
	* @throws AcsJContainerServicesEx
	*/
	public void loadComponent() throws AcsJContainerServicesEx {
		csstatus = alma.CSATSTATUS_MODULE.CSATStatusHelper.narrow(getContainerServices().getDefaultComponent("IDL:alma/CSATSTATUS_MODULE/CSATStatus:1.0"));
	}

	/**
	* This static method is used to start the client and to create an instance of 
	* this class if possible.
	* @return	CSATStatusClient an instance of this class if possible, null in other case.
	*/
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
			System.out.println("ACS has been found on this system, but it is not started.");
			try 
			{
				Logger logger = csatsc.getContainerServices().getLogger();
				logger.log(Level.SEVERE, "Client application failure", e);
			} catch (Exception e2) 
			{
				//e.printStackTrace(System.err);
			}
		}
		return null;
	}

	/**
        * This static method is used to stop the client and is recommended to destroy 
	* instance reference when calling this method. Otherwise problems may arise.
        * @param csatsc 	CSATStatusClient instance to be destroyed.
        */
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
		{
			if(csstatus.getTrackingStatus())
                                interfaz.getDrawingPanel().getTelStatusPanel().setTrackingState(2);
                        else
                                interfaz.getDrawingPanel().getTelStatusPanel().setTrackingState(1);
			return csstatus.getTrackingStatus();
		}
		interfaz.getDrawingPanel().getTelStatusPanel().setTrackingState(0);
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
	public double getSiderealTime()
	{
		if(csstatus!=null)
			return csstatus.getSiderealTime();
		return -1.0d;
	}

}
