/*
 * ALMA - Atacama Large Millimiter Array (c) European Southern Observatory,
 * 2002 Copyright by ESO (in the framework of the ALMA collaboration), All
 * rights reserved
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Authors:
 * Jorge Valencia F. <jorjazo@labmc.inf.utfsm.cl>
 * Marcelo Zuniga L. <azuniga@alumnos.inf.utfsm.cl>
 * Matias Mora K. <mmora@alumnos.inf.utfsm.cl>
 * Ruben Soto T. <rsoto@alma.cl>
 */
package alma.SCHEDULER_MODULE.SchedulerImpl;
import java.util.logging.Logger;

import alma.ACS.ComponentStates;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ContainerServices;
import alma.acs.component.ComponentImplBase;
import alma.SCHEDULER_MODULE.SchedulerOperations;
import alma.acs.component.ComponentLifecycleException;
import alma.UOSErr.*;
import alma.UOSErr.wrappers.*;
import alma.acs.exceptions.AcsJException;

public class SchedulerImpl extends ComponentImplBase implements SchedulerOperations, ComponentLifecycle
{
	private ContainerServices m_containerServices;
	private Logger m_logger;
	private static boolean working;
	private boolean stopping;
	private SchedulerWorker worker;

	private alma.DATABASE_MODULE.DataBase db_comp;
	private alma.INSTRUMENT_MODULE.Instrument inst_comp;
	private alma.TELESCOPE_MODULE.Telescope tele_comp;

	
	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices) throws ComponentLifecycleException {
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		working = false;
		stopping = false;
		
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


	/////////////////////////////////////////////////////////////
	// Implementation of SchedulerOperations
	/////////////////////////////////////////////////////////////

	public void start() throws SchedulerAlreadyRunningEx{
		m_logger.info("Start called");
		
		if (!working && !stopping) {
			working = true;
			worker = new SchedulerWorker(m_logger, db_comp, inst_comp, tele_comp);
			worker.start();
		} else {
			m_logger.info("Already working");
			AcsJSchedulerAlreadyRunningEx ex = new AcsJSchedulerAlreadyRunningEx();
			throw ex.toSchedulerAlreadyRunningEx();
		}
	}

	public void stop() throws SchedulerAlreadyStoppedEx {
		m_logger.info("Stop called");
		
		if (!stopping)
			stopping = true;
		else {
			m_logger.info("Already trying to stop");
			
			//TODO: perhaps throw another exception here...
			return;
		}
		
		if (!working) {
			m_logger.info("Not yet working");
			stopping = false;
			
			AcsJSchedulerAlreadyStoppedEx ex = new AcsJSchedulerAlreadyStoppedEx();
			throw ex.toSchedulerAlreadyStoppedEx();
		}
		working = false;
		
		//block until last proposal finished
		if (worker != null) {
			try {
				m_logger.fine("Waiting for thread to die...");
				worker.join();
			} catch(Exception e) {
				m_logger.fine("Waiting for thread to die error");
			}
		}
		
		worker = null;
		stopping = false;
		
		m_logger.fine("Thread terminated");
	}

	public int proposalUnderExecution() throws NoProposalExecutingEx {
		m_logger.info("proposalUnderExecution called");
		
		
		if (worker==null || worker.getProposalID() == -1) {
			AcsJNoProposalExecutingEx ex = new AcsJNoProposalExecutingEx();
			throw ex.toNoProposalExecutingEx();
		}
		else
			return worker.getProposalID();
	}


	/////////////////////////////////////////////////////////////
	// Other Methods
	/////////////////////////////////////////////////////////////

	public static boolean isWorking() {
		return working;
	}

}

