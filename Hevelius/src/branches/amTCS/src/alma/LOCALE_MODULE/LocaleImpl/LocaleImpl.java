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

package alma.LOCALE_MODULE.LocaleImpl;

import java.util.logging.Logger;
import java.util.*;

import alma.ACS.*;
import alma.TYPES.*;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ContainerServices;
import alma.LOCALE_MODULE.LocaleOperations;

public class LocaleImpl implements LocaleOperations, ComponentLifecycle {

	private ContainerServices m_containerServices;
	private Logger m_logger;

	private double longitude;
	private double latitude;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices) {
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		m_logger.info("initialize() called...");

		longitude = -71.53;
		//longitude = 0;
		latitude = -32.78;
		//longitude = -77.06555556;
		//latitude = 38.92138889;
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
	// Implementation of LocaleOperations
	/////////////////////////////////////////////////////////////

	public double siderealTime(){

		int hora;
		int min;
		int mes;
		int sec;
		int an_o;
		int dia;
		double jt;
		double jd;
		double  MST;
		double LMST;

                Calendar calendario = Calendar.getInstance();
                calendario.setTimeZone(TimeZone.getTimeZone("UTC"));
                hora =calendario.get(Calendar.HOUR_OF_DAY);
                min = calendario.get(Calendar.MINUTE);
                sec = calendario.get(Calendar.SECOND);
                an_o = calendario.get(Calendar.YEAR);
                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.MONTH)+ 1;

/*
                an_o=1987;
                dia = 10;
                mes = 4;
                hora = 19;
                min = 21;
                sec = 0;
*/

                if((mes == 1) || (mes == 2))
                {
                        mes += 12;
                        an_o -= 1;
                }
                jd = (int)(an_o/100.0); //Cada 100 años se resta 1 dia.


                jd = 2 - jd + (int)(jd/4.0); //Cada 4 años se suma 1 dia. La base no se xke.
                jd += (int)(365.25*(an_o+4716)); //Se cuentan los dias de cada año.
                jd += (int)(30.6001*(mes+1)); //Se le suman los dias de los meses que faltan.
                jd += dia - 1524.5; //Se suman los dias y se resta una constante.
                jd += (hora + min/60.0 + sec/3600.0)/24.0; //Se suman fracciones de dia.

/*
		Calendar calendario = Calendar.getInstance();
		calendario.setTimeZone(TimeZone.getTimeZone("UTC"));
		hora =calendario.get(Calendar.HOUR_OF_DAY);
		min = calendario.get(Calendar.MINUTE);
		sec = calendario.get(Calendar.SECOND);
		an_o = calendario.get(Calendar.YEAR);
		dia = calendario.get(Calendar.DAY_OF_MONTH);
		mes = calendario.get(Calendar.MONTH)+ 1;

		an_o=1987;
		dia = 10;
		mes = 4;
		hora = 19;
		min = 21;
		sec = 0;
		

		if((mes == 1) || (mes == 2))
		{
			mes += 12;
			an_o -= 1;
		}
		jd = (int)(an_o/100.0); //Cada 100 años se resta 1 dia.


		jd = 2 - jd + (int)(jd/4.0); //Cada 4 años se suma 1 dia. La base no se xke.
		jd += (int)(365.25*an_o); //Se cuentan los dias de cada año.
		jd += (int)(30.6001*(mes+1)); //Se le suman los dias de los meses que faltan.
		jd += dia - 730550.5; //Se suman los dias y se resta una constante.
		jd += (hora + min/60.0 + sec/3600.0)/24.0; //Se suman fracciones de dia.
*/	
	
		//Con esto se obtienen los Julian Days contando desde Epoch J2000.0
		jt = (jd-2451545)/36525; //Se obtienen Julian Centuries.


		MST = 280.46061837 + 360.98564736629*(jd-2451545); //Se obtiene una buena aprox.
		MST += 0.000387933*jt*jt - jt*jt*jt/38710000; //Mas exacto.
		//Esto es el Mean Sidereal Time... Formula.

		while(MST>360)
			MST -= 360;
		while(MST<0)
			MST += 360;

		LMST = MST + longitude; //Se centra el MST en el punto de observacion.

		while(LMST>360)
			LMST -= 360;
		while(LMST<0)
			LMST+=360;

		return LMST;
	}

	public EarthPos localPos(){
		EarthPos earthPos = new EarthPos();
		earthPos.latitude = latitude;
		earthPos.longitude = longitude;
		return earthPos;
	}

}
