/**
* CSATControlClient is a class that is used to connect with ACS component CSATControl.
* For this purpose it extends ComponentClient which implements most of the necessary methods
* to connect with an ACS component. Still is needed to implement some methods for this task
* because they are particular for each component. Also there is the need to implement most
* methods from CSATControlOperations interface which where generated from IDL interface. This
* is made in order to allow the use of component's methods by making calls to methods of
* objects of this client.
*/


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

	/**
        * This is a constructor for CSATSControlClient instance.
        * @param logger         Logger is a reference to a ACS Logger.
        * @param managerLoc     String System's managerLoc.
        * @param clientName     String clientName for ACS.
        * @throws Exception     Throws any exception that may ocurr.
        */
	public CSATControlClient(Logger logger, String managerLoc, String clientName)
		throws Exception {
			super(logger, managerLoc, clientName);
		}

	/**
        * This method is intended to retrieve component from ACS, as of now it retrieves
        * default CSATControl component.
        * @throws AcsJContainerServicesEx
        */
	public void loadComponent() throws AcsJContainerServicesEx {
		cscontrol = alma.CSATCONTROL_MODULE.CSATControlHelper.narrow(getContainerServices().getDefaultComponent("IDL:alma/CSATCONTROL_MODULE/CSATControl:1.0"));
	}

	/**
        * This static method is used to start the client and to create an instance of
        * this class if possible.
        * @return       CSATControlClient an instance of this class if possible, null in other case.
        */
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

	/**
        * This static method is used to stop the client and is recommended to destroy
        * instance reference when calling this method. Otherwise problems may arise.
        * @param csatcc         CSATControlClient instance to be destroyed.
        */
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

	/**
        * This method is used to move the telescope to desired position, giving a RadecPos structure 
        * with RA and Dec coordinates. This class also sets the presetting status to Presetting and 
	* creates a callback that will sets this status to Idle when the movement is done.
        * @param p         RadecPos with the direction where you'd like to move the telescope.
        */
	public void preset(RadecPos p)
	{
		if(cscontrol!=null)
		{
			interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(2);
			MyResponseReceiver rere  =  new MyResponseReceiver() {
				public void incomingResponse() {
					interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(1);
				}

			};

			ContainerServices cs = getContainerServices();
			CBvoid cb = null;
			try
			{
				cb = MyRequesterUtil.giveCBVoid(cs, rere);
			}
			catch(AcsJContainerServicesEx e)
			{
			}
			cscontrol.preset(p, cb, MyRequesterUtil.giveDescIn());
		}
	}

	/**
	* This method is used to activate/deactivate the tracking status of the system. It 
	* also sets the Tracking status to On/Off accordingly.
	* @param s	boolean with desired status for tracking.
	*/
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

	/**
	* This method sets the Tracking velocity receiving a RadecVel structure which is 
	* a vector with Alt and Az velocities.
	* @param v	RadecVel whith the desired velocities for Alt and Az tracking.
	*/
	public void setTrackingRate(RadecVel v)
	{
		if(cscontrol!=null)
		{
			cscontrol.setTrackingRate(v);
		}
	}

	/**
        * This method is used to move the telescope to desired position, giving a RadecPos structure 
        * with RA and Dec coordinates and a RadecVel structure. This class also sets the presetting status 
	* to Presetting and creates a callback that will sets this status to Idle when the movement is done.
        * @param p      RadecPos with the direction where you'd like to move the telescope.
	* @param v	RadecVel whith the desired velocities for Alt and Az movement.
        */
	public void goToRadec(RadecPos p, RadecVel v)
	{
		if(cscontrol!=null)
		{
			interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(2);
			CBDescIn desc = new CBDescIn(2000, 2000, 1);
			cscontrol.goToRadec(p,v, null, desc);
		}
	}

	/**
        * This method is used to move the telescope to desired position, giving a AltAzPos structure 
        * with Alt and Az coordinates and a RadecVel structure. This class also sets the presetting status 
        * to Presetting and creates a callback that will sets this status to Idle when the movement is done.
        * @param p      RadecPos with the direction where you'd like to move the telescope.
        * @param v      RadecVel whith the desired velocities for Alt and Az movement.
        */
	public void goToAltAz(AltazPos p, AltazVel v)
	{
		if(cscontrol!=null)
		{
			interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(2);
                        MyResponseReceiver rere  =  new MyResponseReceiver() {
                                public void incomingResponse() {
                                        interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(1);
                                }

                        };

                        ContainerServices cs = getContainerServices();
                        CBvoid cb = null;
                        try
                        {
                                cb = MyRequesterUtil.giveCBVoid(cs, rere);
                        }
                        catch(AcsJContainerServicesEx e)
                        {
                        }

			cscontrol.goToAltAz(p,v, cb, MyRequesterUtil.giveDescIn());
		}
	}

	/**
	* This method is used to set an Offset in the Alt coordinate, to correct errors 
	* in the direction that telescope is pointing.
	* @param degree		double that sets Altitude offset.
	*/
	public void AltitudeOffSet(double degree)
	{
		if(cscontrol!=null)
		{
			interfaz.getDrawingPanel().getTelStatusPanel().setPointingState(2);
			cscontrol.AltitudeOffSet(degree);
			interfaz.getDrawingPanel().getTelStatusPanel().setPointingState(1);
		}
	}

	/**
        * This method is used to set an Offset in the Az coordinate, to correct errors 
        * in the direction that telescope is pointing.
        * @param degree         double that sets Azimuth offset.
        */
	public void AzimuthOffSet(double degree)
	{
		if(cscontrol!=null)
		{
                        interfaz.getDrawingPanel().getTelStatusPanel().setPointingState(2);
			cscontrol.AzimuthOffSet(degree);
			interfaz.getDrawingPanel().getTelStatusPanel().setPointingState(1);
		}
	}

	/**
        * This method is used to get an Image of what CCD camera and Telescope are observing. It
	* just returns an "instant" image with no exposure time.
	* @param img		ImageHolder is a structure that stores image data.
	*/
	public void getPreviewImage(ImageHolder img)
	{
		if(cscontrol!=null)
		{

			MyResponseReceiver rere  =  new MyResponseReceiver() {
                                public void incomingResponse() {
					System.out.println("Image received");
                                }
                        };

			ContainerServices cs = getContainerServices();
                        CBvoid cb = null;
                        try
                        {
                                cb = MyRequesterUtil.giveCBVoid(cs, rere);
                        }
                        catch(AcsJContainerServicesEx e)
                        {
				e.printStackTrace();
                        }

			if(img==null)
				img = new ImageHolder();
			cscontrol.getPreviewImage(img, cb, MyRequesterUtil.giveDescIn());
		}
	}

	/**
	* This method is used to stop telescope movement when desired.
	*/
	public void stopTelescope()
	{
		if(cscontrol!=null)
		{
			setTrackingStatus(false);
			cscontrol.stopTelescope();
			interfaz.getDrawingPanel().getTelStatusPanel().setPresettingState(1);
		}
	}

	/**
        * This method is used to get an Image of what CCD camera and Telescope are observing. This 
        * image is obtained using exposure time and an Id.
        * @param img            ImageHolder is a structure that stores image data.
	* @param id		Int
	* @param exptime	double is the time that exposure will lasts.
        */
	public void getProImage(ImageHolder img, int id, double exptime)
	{
		if(cscontrol!=null)
		{
			cscontrol.getProImage(img, id, exptime, null, null);
		}
	}
}
