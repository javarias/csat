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

package alma.POINTING_MODULE.PointingImpl;

import java.util.logging.Logger;

import alma.ACS.*;
import alma.TYPES.*;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ContainerServices;
import alma.POINTING_MODULE.PointingOperations;
import alma.POINTING_MODULE.PointingImpl.PointingImpl;

public class PointingImpl implements PointingOperations, ComponentLifecycle {

        private ContainerServices m_containerServices;
        private Logger m_logger;

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

	public void AltitudeOffset(double degree){

	}
	public void AzimuthOffset(double degree){
	}

}
