package Hevelius.acsmodules.pointing;

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

public class PointingManual extends ComponentImplBase implements SchedulerOperations, ComponentLifecycle{

	private int offset1;
	private int offset2;
	private int alt;
	private int az;

        private ContainerServices m_containerServices;
        private Logger m_logger;

        private alma.DATABASE_MODULE.DataBase db_comp;
        private alma.INSTRUMENT_MODULE.Instrument inst_comp;
        private alma.TELESCOPE_MODULE.Telescope tele_comp;

	public PointingManual(int offset1, int offset2){
		this.offset1=offset1;
		this.offset2=offset2;
	}
        //coneccion al modulo de tobar
	 public void initialize(ContainerServices containerServices) throws ComponentLifecycleException {
                m_containerServices = containerServices;
                m_logger = m_containerServices.getLogger();

                m_logger.finer("Lifecycle initialize() called");

                // Get DataBase, Instrument, Telescope instances
                org.omg.CORBA.Object obj = null;
                try {
                        obj = m_containerServices.getDefaultComponent("IDL:alma/DATABASE_MODULE/DataBase:1.0");
                        db_comp = alma.DATABASE_MODULE.DataBaseHelper.narrow(obj);
                } catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
                        m_logger.fine("Failed to get DATABASE component reference " + e);
                        throw new ComponentLifecycleException("Failed to get DATABASE component reference");
                }

                try {
                        obj = m_containerServices.getDefaultComponent("IDL:alma/INSTRUMENT_MODULE/Instrument:1.0");
                        inst_comp = alma.INSTRUMENT_MODULE.InstrumentHelper.narrow(obj);
                } catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
                        m_logger.fine("Failed to get INSTRUMENT component reference " + e);
                        throw new ComponentLifecycleException("Failed to get INSTRUMENT component reference");
                }

                try {
                        obj = m_containerServices.getDefaultComponent("IDL:alma/TELESCOPE_MODULE/Telescope:1.0");
                        tele_comp = alma.TELESCOPE_MODULE.TelescopeHelper.narrow(obj);
                } catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
                        m_logger.fine("Failed to get TELESCOPE component reference " + e);
                        throw new ComponentLifecycleException("Failed to get TELESCOPE component reference");
		}
	}


	public void execute() throws ComponentLifecycleException {
                m_logger.finer("Lifecycle execute() called");

        }

        public void cleanUp() {
                //Cleanup component references
                if (db_comp != null)
                        m_containerServices.releaseComponent(db_comp.name());
                if (inst_comp != null)
                        m_containerServices.releaseComponent(inst_comp.name());
                if (tele_comp != null)
                        m_containerServices.releaseComponent(tele_comp.name());
        }

        public void aboutToAbort() {
                m_logger.finer("Lifecycle aboutToAbort() called");

                if (working) {
                        m_logger.fine("Trying to stop");
                        try {
                                stop();
                        } catch(SchedulerAlreadyStoppedEx e) {
                                m_logger.fine("Already stopped");
                        }
                }

                cleanUp();

                m_logger.finer("Lifecycle finished");
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

	//tobar component
	//SACAR POSICIONES ACTUAL DEL TELESCOPIO
	alt=component.alt+offset1;
	az=component.az+offset2;
	component_tobar(alt, az)
	}
 
