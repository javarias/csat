package Hevelius.acsmodules.pointing;

import java.io.*;
import alma.ACS.ComponentStates;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ContainerServices;
import alma.acs.component.ComponentImplBase;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.exceptions.AcsJException;

public class PointingManual extends ComponentImplBase implements ComponentLifecycle{

	private double offsetAlt;
	private double offsetAz;
	private int alt;
	private int az;
	private static boolean working;
	private boolean stopping;

	private ContainerServices m_containerServices;
	//private Logger m_logger;

	//private alma.TELESCOPE_MODULE.Telescope tele_comp;

	//coneccion al modulo de tobar
	public void initialize(ContainerServices containerServices) throws ComponentLifecycleException {
		m_containerServices = containerServices;
		//m_logger = m_containerServices.getLogger();
		working = false;
		stopping = false;

		//m_logger.finer("Lifecycle initialize() called");

		// Get DataBase, Instrument, Telescope instances
		org.omg.CORBA.Object obj = null;
		try {
			obj = m_containerServices.getDefaultComponent("IDL:alma/TOBAR/tobi:1.0");
			//db_comp = alma.DATABASE_MODULE.DataBaseHelper.narrow(obj);
		} catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			//m_logger.fine("Failed to get DATABASE component reference " + e);
			throw new ComponentLifecycleException("Failed to get DATABASE component reference");
		}
	}

	public void execute() throws ComponentLifecycleException {
		//m_logger.finer("Lifecycle execute() called");

	}

	public void cleanUp() {
		//Cleanup component references
		//if (db_comp != null)
		//	m_containerServices.releaseComponent(db_comp.name());
	}

	public void aboutToAbort() {
		//m_logger.finer("Lifecycle aboutToAbort() called");

		if (working) {
			//m_logger.fine("Trying to stop");
			//try {
			//	stop();
			//} catch(SchedulerAlreadyStoppedEx e) {
				//m_logger.fine("Already stopped");
			//}
		}

		cleanUp();

		//m_logger.finer("Lifecycle finished");
	}

	/////////////////////////////////////////////////////////////
	// Implementation of AcsComponent
	/////////////////////////////////////////////////////////////

	public ComponentStates componentState() {
		return m_containerServices.getComponentStateManager().getCurrentState();
	}

	public String name() {
		return m_containerServices.getName();
	}

	//MODULOS PARA MOVER

	void AltitudeOffSet(double degree)
	{
		offsetAlt=degree;
	}
	void AzimuthOffSet(double degree)
	{
		offsetAz=degree;
	}

	//SACAR POSICIONES ACTUAL DEL TELESCOPIO
	//getPos(RadecPos r, AltAz a)
	//alt=component.getPos();
	//az=component.az+offsetAz;

}
