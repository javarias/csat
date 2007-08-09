package Hevelius.acsmodules;

import java.io.*;
import alma.ACS.ComponentStates;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ContainerServices;
import alma.acs.component.ComponentImplBase;
import alma.SCHEDULER_MODULE.SchedulerOperations;
import alma.acs.component.ComponentLifecycleException;
import alma.UOSErr.*;
import alma.UOSErr.wrappers.*;
import alma.acs.exceptions.AcsJException;
import alma.acs.TYPES.*;

public class CSATControl
{
	org.omg.CORBA.Object obj = null;
	alma.CSATCONTROL_MODULE.CSATControl cscontrol;
	public void connection()
	{
		try
		{
			obj = m_containerServices.getDefaultComponent("IDL:alma/CSAT Control");
			cscontrol = alma.CSATCONTROL_MODULE.CSATControlHelper.narrow(obj);

		}
		catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e)
		{
			m_logger.fine("Failed to get CONTROL component reference " + e);
			throw new ComponentLifecycleException("Failed to get CONTROL component reference");
		}
	}

	public void setTrackingStatus(boolean s)
	{
		cscontrol.setTrackingStatus(s);
	}

	public void setTrackingRate(RadecVel v)
	{
		cscontrol.setTrackingRate(v);
	}
}
