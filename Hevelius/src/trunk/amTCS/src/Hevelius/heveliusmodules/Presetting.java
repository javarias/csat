package Hevelius.heveliusmodules;

import java.io.*;
//import alma.ACS.ComponentStates;
//import alma.acs.component.ComponentLifecycle;
//import alma.acs.container.ContainerServices;
//import alma.acs.component.ComponentImplBase;
//import alma.acs.component.ComponentLifecycleException;

import alma.acs.exceptions.AcsJException;
import alma.TYPES.*;
import alma.ACS.CBDescIn;
import alma.ACS.CBvoid;
import alma.acs.callbacks.ResponseReceiver;
import Hevelius.utilities.converter.*;
import Hevelius.acsmodules.*;
import Hevelius.interfaz.interfaz;

public class Presetting {

	//callback callback;
	//CBDescIn reference;
	//CDvoid callback;


	public static void preset(double ra, double dec, int type){
		
		RadecPos pos;
		CBDescIn reference;
        	CBvoid callback;

		if(type == 1){
			Converter.altaz2radec(ra,dec);
			ra = Converter.getRa();
			dec = Converter.getDec();
		}
			
		if(Converter.radecVerificate(ra,dec) == 1){


			pos = createRadecType(ra,dec);

		/*	ResponseReceiver callback  =  new ResponseReceiver() {

				public void incomingResponse(Object x) {
					System.out.println("Incoming Response: "+x);
				}
				public void incomingException(Exception x) {
					System.out.println("Responding failed: "+x);}

			};
		*/

			
			
			//try{
			interfaz.getDrawingPanel().getCSATControl().preset(pos);
			//}catch(ComponentLifecycleException e)
			//{
			//VENTANA DE DESCONEXION U OTRA WEA POR DISCUTIR	
			//}
		}
		else{
			System.out.println("RADEC MALO");
		}

	}

	public static RadecPos createRadecType(double ra, double dec){
		RadecPos tmp = new RadecPos();
		

		tmp.ra = ra;
		tmp.dec = dec;

		return tmp;
	}


}
