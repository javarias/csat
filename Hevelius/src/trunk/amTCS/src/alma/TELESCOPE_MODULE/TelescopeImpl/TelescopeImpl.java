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

package alma.TELESCOPE_MODULE.TelescopeImpl;

import java.util.logging.Logger;

import alma.ACS.*;
import alma.TYPES.*;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ContainerServices;
import alma.TELESCOPE_MODULE.TelescopeOperations;
import alma.TELESCOPE_MODULE.TelescopeImpl.TelescopeImpl;

public class TelescopeImpl implements TelescopeOperations, ComponentLifecycle {

	private ContainerServices m_containerServices;
	private Logger m_logger;

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
	// Implementation of TelescopeOperations
	/////////////////////////////////////////////////////////////

	private double Alt = 0;
	private double Az = 0;
	private double Ra = 0;
	private double Dec = 0;

	public void goToAltAz(AltazPos p, AltazVel v, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc) {
		Alt = p.alt;
		Az = p.az;
		hor2radec();
	}

	public void getPos(RadecPosHolder p_rd, AltazPosHolder p_aa) {
		if(p_rd!=null)
		{
			p_rd.value = new RadecPos();
			p_rd.value.ra = Ra;
			p_rd.value.dec = Dec;
		}
		if(p_aa!=null)
		{
			p_aa.value = new AltazPos();
			p_aa.value.alt = Alt;
			p_aa.value.az = Az;
		}
		System.out.println(p_aa.value.alt+p_aa.value.az+"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
	}

	private void hor2radec()
	{
	}

	private void radec2hor(alma.TYPES.RadecPos p)
	{
	}

	public void preset(alma.TYPES.RadecPos p, alma.ACS.CBvoid cb, alma.ACS.CBDescIn desc){
		
		Ra = p.ra;
		Dec = p.dec;

		radec2hor(p);

	}
			
}
