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

public class CSATStatus
{
	private org.omg.CORBA.Object obj = null;
	private alma.CSATSTATUS_MODULE.CSATStatus csstatus;
	private Logger m_logger;
	private ContainerServices m_containerServices;
	public void connection() throws ComponentLifecycleException
	{
		try
		{
			obj = m_containerServices.getDefaultComponent("IDL:alma/CSATSTATUS_MODULE/CSATStatusImpl:1.0");
			csstatus = alma.CSATSTATUS_MODULE.CSATStatusHelper.narrow(obj);

		}
		catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e)
		{
			//m_logger.fine("Failed to get STATUS component reference " + e);
			throw new ComponentLifecycleException("Failed to get STATUS component reference");
		}
	}

	public void on()
	{
		csstatus.on();
	}

	public void stop()
	{
//		csstatus.stop();
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
//		csstatus.initialize();
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
