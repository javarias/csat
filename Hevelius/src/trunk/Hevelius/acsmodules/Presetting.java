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

	public void move_to(){
		
		capturePos();

		//LLAMADA A METODO DE TOBAR COMPONENT PARA MOVER
		try{
			conection();
			preset(radecPos pos);
		}catch(ComponentLifecycleException e)
		{
			//VENTANA DE DESCONEXION U OTRA WEA POR DISCUTIR	
		}


	}

	public void altaz2radec(radecPos p){
	
		//FORMULA PARA CONVERTIR

	}
	
	public void capturePos(){
		
		radecPos tmp;
		int type_pos;

		//OBTENER DE PANTALLA LAS COORDENADA Y EL TIPO
		try{
			validatePos(tmp, type_pos);
		}catch(Exception e){
			//VENTANA DE ERROR EN INGRESO DE COORDENADAS
		}

		if(type_pos == 'ALTAZ')
			altaz2radec(tmp);

		pos = tmp;
		
	}

	public void validatePos(radecPos tmp, int type_pos){
		if(type_pos == 'ALTAZ'){
			//VALIDACION COORDENADAS ALTAZ (TIRAR EXCEPTION SI ESTA MAL)
		}
		else{
			if(type_pos == 'RADEC'){
				//VALIDACION COORDENADA RADEC (TIRAR EXCEPTION SI ESTA MAL)
			}
			else{
				//TIRAR EXCEPTION DE SOFTWARE
			}
		}
	}


}
