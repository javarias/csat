package Hevelius.acsmodules;

import java.io.*;
import alma.TYPES.*;

import java.util.logging.Level;
import java.util.logging.Logger;
//import alma.ACS.CBvoid;
import alma.ACS.*;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.ComponentClient;

import Hevelius.interfaz.*;
import alma.acs.container.ContainerServices;

import alma.acs.callbacks.*;

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
			interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(2);
			MyResponseReceiver rere  =  new MyResponseReceiver() {
				public void incomingResponse(Object x) {
					System.out.println("Incoming Response: "+x);
				}
				public void incomingResponse() {
					interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(1);
				}
				public void incomingException(Exception e) {
					System.out.println("Responding failed: "+e);}

			};

			ContainerServices cs = getContainerServices();
			//MovementCB cb = new MovementCB();
			//cscontrol.preset(p, null, desc);
			CBstring cbs = null;
			CBvoid cb = null;
			try
			{
				cb = MyRequesterUtil.giveCBVoid(cs, rere);;
			}
			catch(AcsJContainerServicesEx e)
			{
			}
			cscontrol.preset(p, cb, MyRequesterUtil.giveDescIn());
//			interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(1);
		}
	}

	public void setTrackingStatus(boolean s)
	{
		if(cscontrol!=null)
		{
			cscontrol.setTrackingStatus(s);
			if(s)
				interfaz.getDrawingPanel().getTelStatusPanel().setTrackingState(2);
			else
				interfaz.getDrawingPanel().getTelStatusPanel().setTrackingState(1);
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
			interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(2);
			CBDescIn desc = new CBDescIn(2000, 2000, 1);
			cscontrol.goToRadec(p,v, null, desc);
		}
	}

	public void goToAltAz(AltazPos p, AltazVel v)
	{
		if(cscontrol!=null)
		{
			interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(2);
                        MyResponseReceiver rere  =  new MyResponseReceiver() {
                                public void incomingResponse(Object x) {
                                        System.out.println("Incoming Response: "+x);
                                }
                                public void incomingResponse() {
                                        interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(1);
                                }
                                public void incomingException(Exception e) {
                                        System.out.println("Responding failed: "+e);}

                        };

                        ContainerServices cs = getContainerServices();
                        //MovementCB cb = new MovementCB();
                        //cscontrol.preset(p, null, desc);
                        CBstring cbs = null;
                        CBvoid cb = null;
                        try
                        {
                                cb = MyRequesterUtil.giveCBVoid(cs, rere);;
                        }
                        catch(AcsJContainerServicesEx e)
                        {
                        }

			cscontrol.goToAltAz(p,v, cb, MyRequesterUtil.giveDescIn());
		}
	}

	public void AltitudeOffSet(double degree)
	{
		if(cscontrol!=null)
		{
			//long time1 = System.currentTimeMillis();
			//long time2 = System.currentTimeMillis();
			interfaz.getDrawingPanel().getTelStatusPanel().setPointingState(2);
			cscontrol.AltitudeOffSet(degree);
			interfaz.getDrawingPanel().getTelStatusPanel().setPointingState(1);
			//while(time2-time1<3000)
                        //	time2 = System.currentTimeMillis();
			//interfaz.getDrawingPanel().getTelStatusPanel().setPointingState(1);
		}
	}

	public void AzimuthOffSet(double degree)
	{
		if(cscontrol!=null)
		{
			//long time1 = System.currentTimeMillis();
                        //long time2 = System.currentTimeMillis();
                        interfaz.getDrawingPanel().getTelStatusPanel().setPointingState(2);
			cscontrol.AzimuthOffSet(degree);
			interfaz.getDrawingPanel().getTelStatusPanel().setPointingState(1);
			//while(time2-time1<3000)
                        //        time2 = System.currentTimeMillis();
                        //interfaz.getDrawingPanel().getTelStatusPanel().setPointingState(1);
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
			interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(1);
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
