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

package alma.CCD_MODULE.CCDImpl;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import alma.ACS.*;
import alma.ACSErr.Completion;
import alma.TYPES.*;
import alma.acs.component.ComponentLifecycle;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.container.ContainerServices;
import alma.CCD_MODULE.CCDOperations;
import alma.DEVCCD_MODULE.DevCCD;
import alma.acs.callbacks.*;
//import alma.ACS.CBDescIn;

public class CCDImpl implements CCDOperations, ComponentLifecycle {

	private ContainerServices m_containerServices;
	private Logger m_logger;
	private DevCCD devCCD_comp;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices) throws ComponentLifecycleException {
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		m_logger.info("initialize() called...");

		org.omg.CORBA.Object obj = null;
		/* We get the DevCCD reference */
		try{
			obj = m_containerServices.getDefaultComponent("IDL:alma/DEVCCD_MODULE/DevCCD:1.0");
			devCCD_comp = alma.DEVCCD_MODULE.DevCCDHelper.narrow(obj);
		} catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get DevCCD default component reference");
			throw new ComponentLifecycleException("Failed to get DevCCD component reference");
		}

	}
    
	public void execute() {
		m_logger.info("execute() called...");
	}
    
	public void cleanUp() {

		if( devCCD_comp != null ){
			m_logger.info("Releasing " + devCCD_comp.name() + " component.");
			m_containerServices.releaseComponent(devCCD_comp.name());
		}

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
	// Implementation of CCDOperations
	/////////////////////////////////////////////////////////////

	public void getPreview(ImageHolder img, CBvoid cb, CBDescIn desc){
		try
		{
		if( devCCD_comp != null ){
			int image[] = devCCD_comp.image(0);
			img.value = new int[921600];
			if(image!=null)
				for(int i=0;i<img.value.length;i++)
					img.value[i]=image[i];
			//img.value = image;
			m_logger.info("Got preview image. Size: " + image.length);
			MyResponderUtil.respond(cb, desc);
		}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void getImage(ImageHolder img, double exposure, CBvoid cb, CBDescIn desc){

		if( devCCD_comp != null ){
			int image[] = devCCD_comp.image(0);
			img.value = image;
			m_logger.info("Got pro image with exposure " + exposure + " sec. Size: " + image.length);
//			BufferedImage bi = new BufferedImage(640,480,BufferedImage.TYPE_INT_RGB);
//			for(int i=0;i!=480;i++){
//				for(int j=0;j!=640;j++){
//					int rgb = image[3*(i*640+j)] << 8;
//					rgb |= image[3*(i*640+j) + 1] << 16;
//					rgb |= image[3*(i*640+j) + 2] << 24;
//					bi.setRGB(j, i, rgb);
//				}
//			}
//			
//			try {
//				ImageIO.write(bi,"png",new File("/home/rtobar/image.png"));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		
	}
}
