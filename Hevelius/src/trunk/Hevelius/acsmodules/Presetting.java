package Hevelius.acsmodules;

import java.io.*;
import alma.ACS.ComponentStates;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ContainerServices;
import alma.acs.component.ComponentImplBase;
import alma.acs.component.ComponentLifecycleException;
import alma.acs.exceptions.AcsJException;
import Hevelius.utilities.converter.*;


/******************************
/ REVISAR LOS TRY Y CATCH
******************************/


public class Presetting {

	org.omg.CORBA.Object obj = null;
	radecPos pos;

	// SE SUPONE QUE ACA VA LA CONEXION AL COMPONENTE DE TOBAR
	public void connection(){

		try {
			obj = m_containerServices.getDefaultComponent("TOBAR COMPONENT");
		} catch (alma.JavaContainerError.wrappers.AcsJContainerServicesEx e) {
			m_logger.fine("Failed to get TOBAR component reference " + e);
			throw new ComponentLifecycleException("Failed to get TOBAR component reference");
		}

	}

	// HACER ESTO COMO HEBRA
	public static void move_to(double ra, double dec, int type){
		
		//capturePos();
		if(type == 'ALTAZ')
			Converter.altaz2radec(ra,dec);
		else
			Converter.radecValidate(ra,dec);

		pos = createRadecType(ra,dec);
		
		//LLAMADA A METODO DE TOBAR COMPONENT PARA MOVER
		try{
			conection();
			obj.preset(radecPos pos);
		}catch(ComponentLifecycleException e)
		{
			//VENTANA DE DESCONEXION U OTRA WEA POR DISCUTIR	
		}


	}

	public radecPos createRadecType(double ra, double dec){
		radecPos tmp;

		tmp.ra = ra;
		tmp.dec = dec;

		return tmp;
	}


}
