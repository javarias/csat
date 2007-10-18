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
import alma.acs.callbacks.RequesterUtil;
import alma.acs.callbacks.ResponseReceiver;
import alma.acs.container.ContainerServices;


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
//			ResponseReceiver rere  =  new ResponseReceiver() {
//				public void incomingResponse(Object x) {
//					System.out.println("Incoming Response: "+x);
//				}
//				public void incomingException(Exception x) {
//					System.out.println("Responding failed: "+x);}
//
//			};
/*			CBvoid cb = new CBvoid() {
				public void working(alma.ACSErr.Completion c, CBDescOut desc){
					System.out.println("A");
				}
				public void done(alma.ACSErr.Completion c, CBDescOut desc){
					System.out.println("B");
				}
				public boolean negotiate(long time, CBDescOut desc){
					System.out.println("C");
				}
				public org.omg.CORBA.Object _set_policy_override(org.omg.CORBA.Policy[] p, org.omg.CORBA.SetOverrideType oType){
					System.out.println("D");
				}
				public org.omg.CORBA.DomainManager[] _get_domain_managers(){
					System.out.println("E");
				}

			};
*/
			//ContainerServices cs = cscontrol.giveContainerServices();
			MovementCB cb = new MovementCB();
			//cscontrol.preset(p, null, desc);
			cscontrol.preset(p, /*RequesterUtil.giveCBvoid(cs, cb)*/ null, RequesterUtil.giveDescIn());
			interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(1);
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
