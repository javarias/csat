/*
 *    ALMA - Atacama Large Millimiter Array
 *    (c) European Southern Observatory, 2002
 *    Copyright by ESO (in the framework of the ALMA collaboration),
 *    All rights reserved
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, 
 *    MA 02111-1307  USA
 */

package alma.NEXSIM_MODULE.NexSimImpl;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.logging.Logger;

import alma.ACS.*;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ContainerServices;
import alma.NEXSIM_MODULE.NexSimOperations;

import cl.utfsm.acs.telescope.simulator.comm.Nexstar4MessageWrapper;
import cl.utfsm.acs.telescope.simulator.comm.SerialPortListener;

public class NexSimImpl implements NexSimOperations, ComponentLifecycle {

	private ContainerServices m_containerServices;
	private Logger m_logger;
	private Nexstar4MessageWrapper m_messageWrapper = null;
	private SerialPortListener m_serialPortListener = null;
	private Thread m_serialPortThread = null;
	private boolean telescopeOn = false;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices) {
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		m_logger.info("initialize() called...");
	}
    
	public void execute() {
		m_logger.info("execute() called...");
	}
    
	public void cleanUp() {
		m_logger.info("cleanUp() called..., nothing to clean up.");
	}
    
	public void aboutToAbort() {
		cleanUp();
		m_logger.info("managed to abort...");
	}
	
	/////////////////////////////////////////////////////////////
	// Implementation of ACSComponent
	/////////////////////////////////////////////////////////////
	
	public ComponentStates componentState() {
		return m_containerServices.getComponentStateManager().getCurrentState();
	}
	public String name() {
		return m_containerServices.getName();
	}

	/////////////////////////////////////////////////////////////
	// Implementation of NexSimOperations
	/////////////////////////////////////////////////////////////

	public void on(){
		if(m_messageWrapper==null)
			m_messageWrapper = new Nexstar4MessageWrapper();
		m_logger.info("Nexstar4 Simulator is on...");
		telescopeOn = true;
		return;
	}

	public void off(){
		if(m_serialPortListener!=null)
			m_serialPortListener.setTelescopeOn(false);
		if(m_serialPortListener!=null){
			m_messageWrapper.executeAction("M");
			m_messageWrapper.executeAction("P"+(char)2+(char)16+(char)36+(char)0+(char)0+(char)0+(char)0);
			m_messageWrapper.executeAction("P"+(char)2+(char)17+(char)36+(char)0+(char)0+(char)0+(char)0);
			m_messageWrapper.executeAction("T"+(char)0);		
		}
		m_logger.info("Nexstar4 Simulator is off...");
		telescopeOn = false;
		return;
	}

	public String executeAction(String message) {
		String response = "";
		if(telescopeOn){
			response = m_messageWrapper.executeAction(message);
			m_logger.info("message delivered to Nexstar4 Simulator: "+message);
		}
		else{
			m_logger.info("message not delivered to Nexstar4 Simulator because simulator is off; message: "+message);
		}
		return response;
	}

	public boolean onInPortDev(String portName) {
		boolean connected = false;
		if(m_serialPortListener==null){
			try {
				m_messageWrapper = new Nexstar4MessageWrapper();
				m_serialPortListener = new SerialPortListener(m_messageWrapper);
				m_serialPortListener.connect(portName);
				m_serialPortThread = new Thread(m_serialPortListener);
				m_serialPortThread.start();
				connected = m_serialPortListener.isConnected();
			} catch (NoSuchPortException e) {
				e.printStackTrace();
			} catch (PortInUseException e) {
				e.printStackTrace();
			} catch (UnsupportedCommOperationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			m_serialPortListener.setTelescopeOn(true);
			connected = m_serialPortListener.isConnected();
		}
		if(connected)
			m_logger.info("Nexstar4 Simulator is on and listening in port "+portName);
		else
			m_logger.info("Nexstar4 Simulator couldn't connect to port "+portName);
		return connected;
	}
	
	/////////////////////////////////////////////////////////////
	// Non Implementation Methods
	/////////////////////////////////////////////////////////////
	
	public String echo(String str){
		String res = m_messageWrapper.executeAction("K" + str.charAt(0));
		return res;
	}
}
