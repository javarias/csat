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

package alma.SAFETY_MODULE.SafetyImpl;

import java.util.logging.Logger;

import alma.ACS.*;
import alma.TYPES.*;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ContainerServices;
import alma.SAFETY_MODULE.SafetyOperations;
import java.util.*;

public class SafetyImpl implements SafetyOperations, ComponentLifecycle {

	private ContainerServices m_containerServices;
	private Logger m_logger;

	private Vector<Moon> moon_table;
	private Vector<Moon> moon_table2;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////

	public void initialize(ContainerServices containerServices) {
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		m_logger.info("initialize() called...");

		Moon temp;
		moon_table = new Vector<Moon>();
		moon_table2 = new Vector<Moon>();

		temp = new Moon(0,0,1,0,6288774,-20905355); //D, M, Mp, F, SumL, SumR
		moon_table.add(temp);

		temp = new Moon(2,0,-1,0,1274027,-3699111);
		moon_table.add(temp);

		temp = new Moon(2,0,0,0,658314,-2955968);
		moon_table.add(temp);

		temp = new Moon(0,0,2,0,213618,-569925);
		moon_table.add(temp);

		temp = new Moon(0,1,0,0,-185116, 48888);
		moon_table.add(temp);

		temp = new Moon(0,0,0,2,-114332,-3149);
		moon_table.add(temp);

		temp = new Moon(2,0,-2,0,58793,246158);
		moon_table.add(temp);

		temp = new Moon(2,-1,-1,0,57066,-152138);
		moon_table.add(temp);

		temp = new Moon(2,0,1,0,53322,-170733);
		moon_table.add(temp);

		temp = new Moon(2,-1,0,0,45758,-204586);
		moon_table.add(temp);

		temp = new Moon(0,1,-1,0,-40923,-129620);
		moon_table.add(temp);

		temp = new Moon(1,0,0,0,-34720,108743);
		moon_table.add(temp);

		temp = new Moon(0,1,1,0,-30383,104755);
		moon_table.add(temp);

		temp = new Moon(2,0,0,-2,15327,10321);
		moon_table.add(temp);

		temp = new Moon(0,0,1,2,-12528,0);
		moon_table.add(temp);

		temp = new Moon(0,0,1,-2,10980,79661);
		moon_table.add(temp);

		temp = new Moon(4,0,-1,0,10675,-34782);
		moon_table.add(temp);

		temp = new Moon(0,0,3,0,10034,-23210);
		moon_table.add(temp);

		temp = new Moon(4,0,-2,0,8548,-21636);
		moon_table.add(temp);

		temp = new Moon(2,1,-1,0,-7888,24208);
		moon_table.add(temp);

		temp = new Moon(2,1,0,0,-6766,30824);
		moon_table.add(temp);

		temp = new Moon(1,0,-1,0,-5163,-8379);
		moon_table.add(temp);

		temp = new Moon(1,1,0,0,4987,-16675);
		moon_table.add(temp);

		temp = new Moon(2,-1,1,0,4036,-12831);
		moon_table.add(temp);

		temp = new Moon(2,0,2,0,3994,-10445);
		moon_table.add(temp);

		temp = new Moon(4,0,0,0,3861,-11650);
		moon_table.add(temp);

		temp = new Moon(2,0,-3,0,3665,14403);
		moon_table.add(temp);

		temp = new Moon(0,1,-2,0,-2689,-7003);
		moon_table.add(temp);

		temp = new Moon(2,0,-1,2,-2602,0);
		moon_table.add(temp);

		temp = new Moon(2,-1,-2,0,2390,10056);
		moon_table.add(temp);

		temp = new Moon(1,0,1,0,-2348,6322);
		moon_table.add(temp);

		temp = new Moon(2,-2,0,0,2236,-9884);
		moon_table.add(temp);

		temp = new Moon(0,1,2,0,-2120,5751);
		moon_table.add(temp);

		temp = new Moon(0,2,0,0,-2069,0);
		moon_table.add(temp);

		temp = new Moon(2,-2,-1,0,2048,-4950);
		moon_table.add(temp);

		temp = new Moon(2,0,1,-2,-1773,4130);
		moon_table.add(temp);

		temp = new Moon(2,0,0,2,-1595,0);
		moon_table.add(temp);

		temp = new Moon(4,-1,-1,0,1215,-3958);
		moon_table.add(temp);

		temp = new Moon(0,0,2,2,-1110,0);
		moon_table.add(temp);

		temp = new Moon(3,0,-1,0,-892,3258);
		moon_table.add(temp);

		temp = new Moon(2,1,1,0,-810,2616);
		moon_table.add(temp);

		temp = new Moon(4,-1,-2,0,759,-1897);
		moon_table.add(temp);

		temp = new Moon(0,2,-1,0,-713,-2117);
		moon_table.add(temp);

		temp = new Moon(2,2,-1,0,-700,2354);
		moon_table.add(temp);

		temp = new Moon(2,1,-2,0,691,0);
		moon_table.add(temp);

		temp = new Moon(2,-1,0,-2,596,0);
		moon_table.add(temp);

		temp = new Moon(4,0,1,0,549,-1423);
		moon_table.add(temp);

		temp = new Moon(0,0,4,0,537,-1117);
		moon_table.add(temp);

		temp = new Moon(4,-1,0,0,520,-1571);
		moon_table.add(temp);

		temp = new Moon(1,0,-2,0,-487,-1739);
		moon_table.add(temp);

		temp = new Moon(2,1,0,-2,-399,0);
		moon_table.add(temp);

		temp = new Moon(0,0,2,-2,-381,-4421);
		moon_table.add(temp);

		temp = new Moon(1,1,1,0,351,0);
		moon_table.add(temp);

		temp = new Moon(3,0,-2,0,-340,0);
		moon_table.add(temp);

		temp = new Moon(4,0,-3,0,330,0);
		moon_table.add(temp);

		temp = new Moon(2,-1,2,0,327,0);
		moon_table.add(temp);

		temp = new Moon(0,2,1,0,-323,1165);
		moon_table.add(temp);

		temp = new Moon(1,1,-1,0,299,0);
		moon_table.add(temp);

		temp = new Moon(2,0,3,0,294,0);
		moon_table.add(temp);

		temp = new Moon(2,0,-1,-2,0,8752);
		moon_table.add(temp);

		//Table 2
		temp = new Moon(0,0,0,1,5128122);
		moon_table2.add(temp);

		temp = new Moon(0,0,1,1,280602);
		moon_table2.add(temp);

		temp = new Moon(0,0,1,-1,277693);
		moon_table2.add(temp);

		temp = new Moon(2,0,0,-1,173237);
		moon_table2.add(temp);

		temp = new Moon(2,0,-1,1,55413);
		moon_table2.add(temp);

		temp = new Moon(2,0,-1,-1,46271);
		moon_table2.add(temp);

		temp = new Moon(2,0,0,1,32573);
		moon_table2.add(temp);

		temp = new Moon(0,0,2,1,17198);
		moon_table2.add(temp);

		temp = new Moon(2,0,1,-1,9266);
		moon_table2.add(temp);

		temp = new Moon(0,0,2,-1,8822);
		moon_table2.add(temp);

		temp = new Moon(2,-1,0,-1,8216);
		moon_table2.add(temp);

		temp = new Moon(2,0,-2,-1,4324);
		moon_table2.add(temp);

		temp = new Moon(2,0,1,1,4200);
		moon_table2.add(temp);

		temp = new Moon(2,1,0,-1,-3359);
		moon_table2.add(temp);

		temp = new Moon(2,-1,-1,1,2463);
		moon_table2.add(temp);

		temp = new Moon(2,-1,0,1,2211);
		moon_table2.add(temp);

		temp = new Moon(2,-1,-1,-1,2065);
		moon_table2.add(temp);

		temp = new Moon(0,1,-1,-1,-1870);
		moon_table2.add(temp);

		temp = new Moon(4,0,-1,-1,1828);
		moon_table2.add(temp);

		temp = new Moon(0,1,0,1,-1794);
		moon_table2.add(temp);

		temp = new Moon(0,0,0,3,-1749);
		moon_table2.add(temp);

		temp = new Moon(0,1,-1,1,-1565);
		moon_table2.add(temp);

		temp = new Moon(1,0,0,1,-1491);
		moon_table2.add(temp);

		temp = new Moon(0,1,1,1,-1475);
		moon_table2.add(temp);

		temp = new Moon(0,1,1,-1,-1410);
		moon_table2.add(temp);

		temp = new Moon(0,1,0,-1,-1344);
		moon_table2.add(temp);

		temp = new Moon(1,0,0,-1,-1335);
		moon_table2.add(temp);

		temp = new Moon(0,0,3,1,1107);
		moon_table2.add(temp);

		temp = new Moon(4,0,0,-1,1107);
		moon_table2.add(temp);

		temp = new Moon(4,0,0,-1,1021);
		moon_table2.add(temp);

		temp = new Moon(4,0,-1,1,833);
		moon_table2.add(temp);

		temp = new Moon(0,0,1,-3,777);
		moon_table2.add(temp);

		temp = new Moon(4,0,-2,1,671);
		moon_table2.add(temp);

		temp = new Moon(2,0,0,-3,607);
		moon_table2.add(temp);

		temp = new Moon(2,0,2,-1,596);
		moon_table2.add(temp);

		temp = new Moon(2,-1,1,-1,491);
		moon_table2.add(temp);

		temp = new Moon(2,0,-2,1,-451);
		moon_table2.add(temp);

		temp = new Moon(0,0,3,-1,439);
		moon_table2.add(temp);

		temp = new Moon(2,0,2,1,422);
		moon_table2.add(temp);

		temp = new Moon(2,0,-3,-1,421);
		moon_table2.add(temp);

		temp = new Moon(2,1,-1,1,-366);
		moon_table2.add(temp);

		temp = new Moon(2,1,0,1,-351);
		moon_table2.add(temp);

		temp = new Moon(4,0,0,1,331);
		moon_table2.add(temp);

		temp = new Moon(2,-1,1,1,315);
		moon_table2.add(temp);

		temp = new Moon(2,-2,0,-1,302);
		moon_table2.add(temp);

		temp = new Moon(0,0,1,3,-283);
		moon_table2.add(temp);

		temp = new Moon(2,1,1,-1,-229);
		moon_table2.add(temp);

		temp = new Moon(1,1,0,-1,223);
		moon_table2.add(temp);

		temp = new Moon(1,1,0,1,223);
		moon_table2.add(temp);

		temp = new Moon(0,1,-2,-1,-220);
		moon_table2.add(temp);

		temp = new Moon(2,1,-1,-1,-220);
		moon_table2.add(temp);

		temp = new Moon(1,0,1,1,-185);
		moon_table2.add(temp);

		temp = new Moon(2,-1,-2,-1,181);
		moon_table2.add(temp);

		temp = new Moon(0,1,2,1,-177);
		moon_table2.add(temp);

		temp = new Moon(4,0,-2,-1,176);
		moon_table2.add(temp);

		temp = new Moon(4,-1,-1,-1,166);
		moon_table2.add(temp);

		temp = new Moon(1,0,1,-1,-164);
		moon_table2.add(temp);

		temp = new Moon(4,0,1,-1,132);
		moon_table2.add(temp);

		temp = new Moon(1,0,-1,-1,-119);
		moon_table2.add(temp);

		temp = new Moon(4,-1,0,-1,115);
		moon_table2.add(temp);

		temp = new Moon(2,-2,0,1,107);
		moon_table2.add(temp);
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
	// Implementation of SafetyOperations
	/////////////////////////////////////////////////////////////

	public int getSafety(RadecPos position){
		int danger = 0;
		danger = moonRestriction(position);
		
		return danger;
	}

	/////////////////////////////////////////////////////////////
	// Implementation of private methods
	/////////////////////////////////////////////////////////////

	private int moonRestriction(RadecPos rd)
	{
		double Lp, D, M, Mp, F, A1, A2, A3, E, T, T2, T3, T4, JDE, E2;
		double sumL = 0;
		double sumR = 0;
		double sumB = 0;
		double mR = 1738.0d;
		JDE = jd();
		T = (JDE - 2451545)/36525;
		T2 = T*T;
		T3 = T2*T;
		T4 = T3*T;
		Lp = 218.3164591d + 481267.881342d*T - 0.0013268d*T2 + T3/538841d - T4/65194000d;
		D = 297.8502042d + 445267.1115168d*T - 0.0016300d*T2 + T3/545868d - T4/11306500d;
		M = 357.5291092d + 35999.0502909d*T - 0.0001536d*T2 + T3/24490000d;
		Mp = 134.9634114d + 477198.8676313d*T + 0.0089970d*T2 + T3/69699d - T4/14712000;
		F = 93.2720993d + 483202.0175273d*T - 0.0034029*T2 - T3/3526000 + T4/863310000;
		A1 = 119.75d + 131.849d*T;
		A2 = 53.09d + 479264.290d*T;
		A3 = 313.45d + 481266.484d*T;
		E = 1 - 0.002516*T - 0.0000074*T2;
                E2 = E*E;

                Lp = Lp%360.0d;
                D = D%360.0d;
                M = M%360.0d;
                Mp = Mp%360.0d;
                F = F%360.0d;
                A1 = A1%360.0d;
                A2 = A2%360.0d;
                A3 = A3%360.0d;
                if(Lp<0)
                        Lp += 360.0d;
                if(D<0)
                        D += 360.0d;
                if(M<0)
                        M += 360.0d;
                if(Mp<0)
                        Mp += 360.0d;
                if(F<0)
                        F += 360.0d;
                if(A1<0)
                        A1 += 360.0d;
                if(A2<0)
                        A2 += 360.0d;
                if(A3<0)
                        A3 += 360.0d;

		int cD, cM, cMp, cF, cL, cR, cB;
		double gLat, gLon, gRad;

                for(int i = 0; i < moon_table.size(); i++)
                {
                        cD = moon_table.get(i).getD();
                        cM = moon_table.get(i).getM();
                        cMp = moon_table.get(i).getMp();
                        cF = moon_table.get(i).getF();
                        cL = moon_table.get(i).getL();
                        cR = moon_table.get(i).getR();
                        if(Math.abs(cM)==1)
                        {
                                sumL += Math.sin((cD*D+cM*M+cMp*Mp+cF*F)*Math.PI/180)*cL*E;
                                sumR += Math.cos((cD*D+cM*M+cMp*Mp+cF*F)*Math.PI/180)*cR*E;
                        }
                        else if(Math.abs(cM)==2)
                        {
                                sumL += Math.sin((cD*D+cM*M+cMp*Mp+cF*F)*Math.PI/180)*cL*E2;
                                sumR += Math.cos((cD*D+cM*M+cMp*Mp+cF*F)*Math.PI/180)*cR*E2;
                        }
                        else
                        {
                                sumL += Math.sin((cD*D+cM*M+cMp*Mp+cF*F)*Math.PI/180)*cL;
                                sumR += Math.cos((cD*D+cM*M+cMp*Mp+cF*F)*Math.PI/180)*cR;
                        }
                }

                for(int i = 0; i < moon_table2.size(); i++)
                {
                        cD = moon_table2.get(i).getD();
                        cM = moon_table2.get(i).getM();
                        cMp = moon_table2.get(i).getMp();
                        cF = moon_table2.get(i).getF();
                        cB = moon_table2.get(i).getB();
                        if(Math.abs(cM)==1)
                                sumB += Math.sin((cD*D+cM*M+cMp*Mp+cF*F)*Math.PI/180)*cB*E;
                        else if(Math.abs(cM)==2)
                                sumB += Math.sin((cD*D+cM*M+cMp*Mp+cF*F)*Math.PI/180)*cB*E2;
                        else
                                sumB += Math.sin((cD*D+cM*M+cMp*Mp+cF*F)*Math.PI/180)*cB;
                }

                sumL += 3958*Math.sin(A1*Math.PI/180) + 1962*Math.sin((Lp-F)*Math.PI/180) + 318*Math.sin(A2*Math.PI/180);
                sumB += -2235*Math.sin(Lp*Math.PI/180) + 382*Math.sin(A3*Math.PI/180) + 175*Math.sin((A1-F)*Math.PI/180);
                sumB += 175*Math.sin((A1+F)*Math.PI/180) + 127*Math.sin((Lp-Mp)*Math.PI/180);
                sumB -= 115*Math.sin((Lp+Mp)*Math.PI/180);

                gLon = Lp + sumL/1000000;
                gLat = sumB/1000000;
                gRad = 385000.56 + sumR/1000;

                System.out.println("Lon="+gLon+"Lat="+gLat+"Rad="+gRad);

                double eps, ra, dec, gLatR, gLonR, epsR, lambda, beta, londiff, latdiff;

                eps = 23.43929111d - 0.013004166d*T - 0.000000163d*T2 + 0.000000503d*T3;
                epsR = eps*Math.PI/180;
                gLonR = gLon*Math.PI/180;
                gLatR = gLat*Math.PI/180;
                ra = Math.atan2(Math.sin(gLonR)*Math.cos(epsR)-Math.tan(gLatR)*Math.sin(epsR),Math.cos(gLonR))*180/Math.PI;
                dec = Math.asin(Math.sin(gLatR)*Math.cos(epsR)+Math.cos(gLatR)*Math.sin(epsR)*Math.sin(gLonR))*180/Math.PI;

                if(ra<0)
                        ra += 360.0d;

		lambda = Math.sin(rd.ra*Math.PI/180)*Math.cos(epsR)+Math.tan(rd.dec*Math.PI/180)*Math.sin(epsR);
		lambda = Math.atan2(lambda,Math.cos(rd.ra*Math.PI/180));

                beta = Math.sin(rd.dec*Math.PI/180)*Math.cos(epsR);
                beta -= Math.cos(rd.dec*Math.PI/180)*Math.sin(epsR)*Math.sin(rd.ra*Math.PI/180);
                beta = Math.asin(beta);

		if(lambda < 0)
			lambda += Math.PI*2;
		if(beta < 0)
			beta += Math.PI*2;

		m_logger.info("lambda"+(lambda*180/Math.PI));
		m_logger.info("beta"+(beta*180/Math.PI));

		m_logger.info("diflon"+(gLonR-lambda));
		m_logger.info("diflat"+(gLatR-beta));

                if(gLonR-lambda < 0 && gLonR-lambda > -Math.PI/2)
                        londiff = Math.tan(-(gLonR-lambda))*gRad;
                else if (gLonR-lambda >= 0 && gLonR-lambda < Math.PI/2)
                        londiff = Math.tan(gLonR-lambda)*gRad;
                else if (gLonR-lambda > 3/2*Math.PI)
                        londiff = Math.tan(2*Math.PI-(gLonR-lambda))*gRad;
                else if (gLonR-lambda < -3/2*Math.PI)
                        londiff = Math.tan(2*Math.PI+(gLonR-lambda))*gRad;
                else
                        londiff = 10*mR;

                if(gLatR-beta < 0 && gLatR-beta > -Math.PI/2)
                        latdiff = Math.tan(-(gLatR-beta))*gRad;
                else if (gLatR-beta >= 0 && gLatR-beta < Math.PI/2)
                        latdiff = Math.tan(gLatR-beta)*gRad;
                else if (gLatR-beta > 3/2*Math.PI)
                        latdiff = Math.tan(2*Math.PI-(gLatR-beta))*gRad;
                else if (gLatR-beta < -3/2*Math.PI)
                        latdiff = Math.tan(2*Math.PI+(gLatR-beta))*gRad;
                else
                        latdiff = 10*mR;


		System.out.println(londiff+"");
		System.out.println(latdiff+"");

		int lonDanger, latDanger;

		lonDanger = 1;
		if(londiff < 4*mR)
			lonDanger = 2;
		if(londiff < 5/2*mR)
			lonDanger = 3;
		if(londiff < 3/2*mR)
			lonDanger = 4;

		latDanger = 1;
                if(latdiff < 4*mR)
                        latDanger = 2;
                if(latdiff < 5/2*mR)
                        latDanger = 3;
                if(latdiff < 3/2*mR)
                        latDanger = 4;

		System.out.println("LonD "+lonDanger+" LatD "+latDanger);
		if (lonDanger > latDanger)
			return lonDanger;
		else
			return latDanger;
	}

	private double jd()
	{
                int hora;
                int min;
                int mes;
                int sec;
                int an_o;
                int dia;
                double jd;

                Calendar calendario = Calendar.getInstance();
                calendario.setTimeZone(TimeZone.getTimeZone("UTC"));
                hora =calendario.get(Calendar.HOUR_OF_DAY);
                min = calendario.get(Calendar.MINUTE);
                sec = calendario.get(Calendar.SECOND);
                an_o = calendario.get(Calendar.YEAR);
                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.MONTH)+ 1;

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

                return jd;
	}
}
