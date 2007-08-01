package Hevelius.acsmodules;

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

public class Presetting {

	org.omg.CORBA.Object obj = null;

	// SE SUPONE QUE ACA VA LA CONEXION AL COMPONENTE DE TOBAR
	public void connection(){

		try {
			obj = m_containerServices.getDefaultComponent("TOBAR COMPONENT");
		} catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get TOBAR component reference " + e);
			throw new ComponentLifecycleException("Failed to get TOBAR component reference");
		}

	}

	public void move_to(double alt, double az){
		
		//LLAMADA A METODO DE TOBAR COMPONENT PARA MOVER
		try{
			conection();
			preset(radecPos p);
		}catch(ComponentLifecycleException e)
		{
			//VENTANA DE DESCONECCION U OTRA WEA POR DISCUTIR	
		}


	}

	public radecPos altaz2radec(radecPos P){
	
		//FORMULA PARA CONVERTIR

		return p;
	}
	


}
