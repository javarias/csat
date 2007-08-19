package Hevelius.acsmodules;

import java.io.*;
import java.util.logging.Logger;
import alma.ACS.ComponentStates;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ContainerServices;
import alma.acs.component.ComponentImplBase;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.exceptions.AcsJException;
import alma.TYPES.*;

public class CSATControl
{
	org.omg.CORBA.Object obj = null;
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
