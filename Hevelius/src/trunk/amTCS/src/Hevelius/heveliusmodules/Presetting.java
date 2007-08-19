package Hevelius.heveliusmodules;

import java.io.*;
//import alma.ACS.ComponentStates;
//import alma.acs.component.ComponentLifecycle;
//import alma.acs.container.ContainerServices;
//import alma.acs.component.ComponentImplBase;
//import alma.acs.component.ComponentLifecycleException;
import alma.acs.exceptions.AcsJException;
import alma.TYPES.*;
import Hevelius.utilities.converter.*;
import Hevelius.acsmodules.*;


public class Presetting {

	public static void preset(double ra, double dec, int type){
		
		//if(type == 'ALTAZ')
		//	Converter.altaz2radec(ra,dec);
		//else
		//	Converter.radecValidate(ra,dec);

		//pos = createRadecType(ra,dec);
		
		//try{
			//interfaz.getDrawingPanel().getCSATControl().preset(pos, callback, reference);
		//}catch(ComponentLifecycleException e)
		//{
			//VENTANA DE DESCONEXION U OTRA WEA POR DISCUTIR	
		//}


	}

	public RadecPos createRadecType(double ra, double dec){
		RadecPos tmp = null;

		tmp.ra = ra;
		tmp.dec = dec;

		return tmp;
	}

	//AVERIGUAR
	//public callback(){
//
//	}

}
