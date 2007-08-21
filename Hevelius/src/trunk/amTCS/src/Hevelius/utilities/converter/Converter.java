package Hevelius.utilities.converter;

import Hevelius.interfaz.*;
import Hevelius.weather.*;
import java.io.*;
import java.util.regex.*;
import java.net.*;
import java.util.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Converter {
	
	private static double RA;
	private static double DEC;
	
	private static double ALT;
	private static double AZ;

	private static Configuration test = new Configuration();

	public static void radec2altaz(double RA, double DEC) //usarlo como clase
	{		
		Calendar calendario = Calendar.getInstance();

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
		double LON;
		double LAT;
		double HA;
		double ALT;
		double AZ;

		WeatherCondition clima = new WeatherCondition(test.getOption("location"));
        	Vector<WeatherCityCondition> vector = clima.ListCityCondition();

		if(!(vector.get(0).getLon()=="N/A" || vector.get(0).getLat()=="N/A"))	
		{
		//dates of the time
		hora =calendario.get(Calendar.HOUR_OF_DAY);
		min = calendario.get(Calendar.MINUTE);
		sec = calendario.get(Calendar.SECOND);
		an_o = calendario.get(Calendar.YEAR);
		dia = calendario.get(Calendar.DAY_OF_MONTH);
		mes = calendario.get(Calendar.MONTH)+ 1;
		//System.out.println(hora + ":" + min + ":" + sec + "_"+ dia + "_" + an_o + "_" + mes) ;
		
		//posicion fisicas del telescopio de acuerdo a la ciudad elegida
		LON = Double.parseDouble(vector.get(0).getLon()); //llamar al cuadrado q tiene ese dato
		LAT = Double.parseDouble(vector.get(0).getLat()); //llama1;r posicion que tuene ese dato 


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
	
	
		//Con esto se obtienen los Julian Days contando desde Epoch J2000.0
		jt = jd/36525; //Se obtienen Julian Centuries.


		MST = 280.46061837 + 360.98564736629*jd; //Se obtiene una buena aprox.
		MST += 0.000387933*jt*jt - jt*jt*jt/38710000; //Mas exacto.
		//Esto es el Mean Sidereal Time... Formula.


		while(MST>360)
			MST -= 360;
		while(MST<0)
			MST += 360;
		//MST se deja entre 0 y 360

		LMST = MST + LON; //Se centra el MST en el punto de observacion.
		HA = LMST - RA; //Se obtiene Hour Angle.
		if(HA < 0)
			HA += 360;//Tiene que ser positivo.


		//Se obtiene la altitud en grados.
		ALT = Math.sin(DEC*Math.PI/180)*Math.sin(LAT*Math.PI/180);
		ALT += Math.cos(DEC*Math.PI/180)*Math.cos(LAT*Math.PI/180)*Math.cos(HA*Math.PI/180);
		ALT = Math.asin(ALT)*180/Math.PI;

		//Se obtiene el Azimuth en grados
		AZ = Math.sin(DEC*Math.PI/180) -Math.sin(ALT*Math.PI/180)*Math.sin(LAT*Math.PI/180);
		AZ /= Math.cos(ALT*Math.PI/180)*Math.cos(LAT*Math.PI/180);
		AZ = Math.acos(AZ)*180/Math.PI;

		//Si es que el Sin de Hour Angle es mayor que 0, entonces Azimuth cambia.
		if (Math.sin(HA*Math.PI/180) > 0)
			AZ = 360 - AZ;
		RA = ALT;
		DEC = AZ;
		Converter.ALT = ALT;
		Converter.AZ = AZ;
		}

	}

	public static void altaz2radec(double ALT, double AZ){

	}


	public static int radecVerificate(double RA, double DEC)
	{
		if((0<= RA && RA<= 24*15) &&(-90 <= DEC && DEC <= 90))
			return 1;
		return 0;
	}

	public static int radecValidate(double ALT, double AZ)
	{
		while(ALT > 360)
			ALT -= 360;
		while(AZ > 360)
			AZ -= 360;
		while(ALT < 0)
			ALT += 360;
		while(AZ < 0)
			AZ += 360;
		if(1<= ALT && ALT <= 179)
		{
			if (ALT > 90)
			{
				AZ = 180 + AZ;
				ALT = 180 - ALT;
			}
			return 1;
		}
		return 0;
	}

	public static int altazVerificate(double RA, double DEC)
	{
		return 0;
	}

	public static int altazValidate(double ALT, double AZ)
	{
		return 0;
	}


	public static double getAlt()
	{
		return ALT;
	}

	public static double getAz()
	{
		return AZ;
	}

	public static double getRa()
	{
		return RA;
	}

	public static double getDec()
	{
		return DEC;
	}
}

