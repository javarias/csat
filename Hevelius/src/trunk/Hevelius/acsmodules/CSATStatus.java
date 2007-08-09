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

public class CSATStatus
{
	org.omg.CORBA.Object obj = null;
	alma.CSATSTATUS_MODULE.CSATStatus csstatus;
	public void connection()
	{
		try
		{
			obj = m_containerServices.getDefaultComponent("IDL:alma/CSAT Status");
			csstatus = alma.CSATSTATUS_MODULE.CSATStatusHelper.narrow(obj);

		}
		catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e)
		{
			m_logger.fine("Failed to get STATUS component reference " + e);
			throw new ComponentLifecycleException("Failed to get STATUS component reference");
		}
	}

	public boolean getTrackingStatus()
	{
		return csstatus.getTrackingStatus();
	}

	public RadecVel getTrackingRate()
	{
		return csstatus.getTrackingRate();
	}
}
