package alma.TELESCOPE_MODULE.TelescopeImpl;
import java.util.logging.Logger;
import java.util.Random;

import alma.ACS.ComponentStates;
import alma.acs.container.ContainerServices;
import alma.TYPES.*;
//import alma.DATABASE_MODULE.DataBaseImpl;


public class TelescopeWorker extends Thread
{
	private Logger m_logger;
	private alma.TRACKING_MODULE.Tracking trck_comp;
	private alma.TELESCOPE_MODULE.Telescope tele_comp;

	public TelescopeWorker(Logger m_logger, alma.TRACKING_MODULE.Tracking trck_comp, alma.TELESCOPE_MODULE.Telescope tele_comp) 
	{
		this.m_logger = m_logger;
		this.trck_comp = trck_comp;
		this.tele_comp = tele_comp;
		m_logger.finer("Worker initialized.");
	}

	public void run() 
	{
		m_logger.finer("Worker thread started.");

		while (tele_comp.isWorking()) 
		{
			//Update Sidereal Time
			//tele_comp.st();
			AltazPosHolder ph_aa = new AltazPosHolder();
			RadecPosHolder ph_rd = new RadecPosHolder();
			tele_comp.getPos(ph_rd,ph_aa);
			
			if(trck_comp.getTrackingStatus())
			{
				//Update Horizontal Coords
				tele_comp.radec2hor(ph_rd.value);
			}
			else
			{
				//Update Radec Coords
				tele_comp.hor2radec(ph_aa.value);
			}
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				m_logger.fine("Failed to sleep.");
			}	
		}
	}
}
