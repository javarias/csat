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


	public static void preset(double c1, double c2, int type){
		
		RadecPos p_rd;
		AltazPos p_aa;
		CBDescIn reference;
		CBvoid callback;

		if(type == 1){
			p_aa = new AltazPos();
			p_aa.alt = c1;
			p_aa.az = c2;
			if(interfaz.getDrawingPanel().getCSATControl()!=null)
				interfaz.getDrawingPanel().getCSATControl().goToAltAz(p_aa, new AltazVel());
			//Converter.altaz2radec(c1,c2);
			//c1 = Converter.getRa();
			//c2 = Converter.getDec();
		}
		else
		{
			if(Converter.radecVerificate(c1,c2) == 1){


				p_rd = createRadecType(c1,c2);

				/*	ResponseReceiver callback  =  new ResponseReceiver() {

					public void incomingResponse(Object x) {
					System.out.println("Incoming Response: "+x);
					}
					public void incomingException(Exception x) {
					System.out.println("Responding failed: "+x);}

					};
				 */



				//try{
				if(interfaz.getDrawingPanel().getCSATControl()!=null)
				{
					interfaz.getDrawingPanel().getCSATControl().preset(p_rd);
				}
				//}catch(ComponentLifecycleException e)
				//{
				//VENTANA DE DESCONEXION U OTRA WEA POR DISCUTIR	
				//}
			}
			else{
				//System.out.println("RADEC MALO");
			}
		}

	}

	public static RadecPos createRadecType(double ra, double dec){
		RadecPos tmp = new RadecPos();
		tmp.ra = ra;
		tmp.dec = dec;
		return tmp;
	}


}
