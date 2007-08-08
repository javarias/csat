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
import java.util.Random;

import alma.ACS.ComponentStates;
import alma.acs.container.ContainerServices;
import alma.UOSErr.*;
//import alma.DATABASE_MODULE.DataBaseImpl;


public class SchedulerWorker extends Thread
{
	private Logger m_logger;
	private alma.DATABASE_MODULE.DataBase db_comp;
	private alma.INSTRUMENT_MODULE.Instrument inst_comp;
	private alma.TELESCOPE_MODULE.Telescope tele_comp;

	private alma.TYPES.Proposal currentProposal;

	public SchedulerWorker(Logger m_logger, alma.DATABASE_MODULE.DataBase db_comp, alma.INSTRUMENT_MODULE.Instrument inst_comp, alma.TELESCOPE_MODULE.Telescope tele_comp) {
		this.m_logger = m_logger;
		this.db_comp = db_comp;
		this.inst_comp = inst_comp;
		this.tele_comp = tele_comp;

		currentProposal=null;
		m_logger.finer("Worker initialized.");
	}
	
	public void run() {
		m_logger.finer("Worker thread started.");
		
		while (SchedulerImpl.isWorking()) {
			
			//Get Proposals from DataBase and select which to run
			alma.TYPES.Proposal[] proposals = db_comp.getProposals();
			if (proposals==null || proposals.length==0) {
				m_logger.finer("No proposal to start. Sleeping...");
				try {
					Thread.sleep(2000);
				} catch(Exception e) {
					m_logger.fine("Failed to sleep. Trying to get more proposals...");
				}
				continue;
			}
			
			//Select a proposal from the DB
			currentProposal = proposals[selectProposal(proposals.length)];
			try {
				db_comp.setProposalStatus(currentProposal.pid, 1);
			} catch (InvalidProposalStatusTransitionEx e) {
				//TODO: handle exception
			}
			
			m_logger.info("Scheduling proposal " + currentProposal.pid + " for observation (status changed to 1).");
			
			inst_comp.cameraOn();
			m_logger.finer("Camera turned on.");

			for (alma.TYPES.Target target : currentProposal.targets) {
				try{
					m_logger.finer("Starting observation for target "+target.tid+".");
					int[] img = tele_comp.observe(target.coordinates, target.expTime);
					m_logger.finer("Image length: "+img.length+".");
					
					db_comp.storeImage(currentProposal.pid, target.tid, img);
					m_logger.info("Observation for target " + target.tid + " finished and stored.");
				}catch(PositionOutOfLimitsEx e){
					m_logger.warning("Positon out of limits: proposal " + currentProposal.pid + ", target " + target.tid);
				}catch(ImageAlreadyStoredEx e){
					m_logger.warning("Image already stored: proposal " + currentProposal.pid + ", target " + target.tid);
				}
			}

			inst_comp.cameraOff();
			try {
				db_comp.setProposalStatus(currentProposal.pid, 2);
			} catch (InvalidProposalStatusTransitionEx e) {
				//TODO: handle exception
			}
			m_logger.fine("Camera is now off and observation for proposal " + currentProposal.pid + " is done (status changed to 2)");
		}
		m_logger.fine("Ending Scheduler worker thread.");
	}

	public int selectProposal(int max) {
		//return selected proposal (random)
		int selected;
		
		Random rand = new Random();
		selected = rand.nextInt() % max;
		if (selected < 0)
			selected = -selected;
		
		m_logger.finer("Proposal " + selected + " selected (total " + max + ")");
		
		return selected;
	}
	
	public int getProposalID(){
		if (currentProposal != null)
			return currentProposal.pid;
		return -1;
	}

}

