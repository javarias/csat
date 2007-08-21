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
//import alma.ACS.Callback;
import alma.acs.callbacks.ResponseReceiver;
import Hevelius.utilities.converter.*;
import Hevelius.acsmodules.*;


public class Presetting {

	//callback callback;
	CBDescIn reference;


	public static void preset(double ra, double dec, int type){
		
		RadecPos pos;

		if(type == 1)
			Converter.altaz2radec(ra,dec);
		else
			Converter.radecValidate(ra,dec);

		pos = createRadecType(ra,dec);
		
		ResponseReceiver callback  =  new ResponseReceiver() {

			public void incomingResponse(Object x) {
				System.out.println("Incoming Response: "+x);
			}
			public void incomingException(Exception x) {
				System.out.println("Responding failed: "+x);}

		};

		try{
			interfaz.getDrawingPanel().getCSATControl().preset(pos, callback, reference);
		}catch(ComponentLifecycleException e)
		{
			//VENTANA DE DESCONEXION U OTRA WEA POR DISCUTIR	
		}


	}

	public RadecPos createRadecType(double ra, double dec){
		RadecPos tmp = null;

		tmp.ra = ra;
		tmp.dec = dec;

		return tmp;
	}


}
