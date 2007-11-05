package Hevelius.utilities.historial;

import java.io.*;
import java.util.*;
import Hevelius.acsmodules.*;
import Hevelius.interfaz.*;
import alma.TYPES.*;

public class Historial //implements Runnable
{
	static final public String logDir  = System.getProperty("user.home") + "/.hevelius/history";
	//static public String logFile = logDir + "/" + "history.log";

	private static Configuration test = new Configuration();


	static File dir = new File(logDir);
	//static File file = new File(logFile);
	
//	static Calendar calendario = new GregorianCalendar();
/*	int hora;
	int minuto;
	int segundo;*/

	//private static Configuration conf = new Configuration();

	public Historial(){
		String logFile = logDir + "/" + test.getOption("user") + ".log";
		File file = new File(logFile);	

		if(!dir.exists()){
			dir.mkdir();
		}

		if(!file.exists()){
			try{
				PrintWriter archivo = new PrintWriter(
						new FileWriter(logFile,true));

				archivo.close();

			}
			catch(IOException e)
			{
				System.out.println("Error al crear el archivo de Logs");
			}
		}


	}


	public String time(){
		Calendar calendario = new GregorianCalendar();
		int hora = calendario.get(Calendar.HOUR_OF_DAY);
		int minutos = calendario.get(Calendar.MINUTE);
		int segundos = calendario.get(Calendar.SECOND);
		int dia = calendario.get(Calendar.DAY_OF_MONTH);
		int mes = calendario.get(Calendar.MONTH);
		int ano = calendario.get(Calendar.YEAR);
		String hrs;
		String min;
		String sec;
		String d;
		String m;
		String a;
	

		if(hora < 10)
                        hrs = new String("0"+hora);
		else
			hrs = new String(""+hora);

                if(minutos < 10)
                        min = new String("0"+minutos);
		else
			min = new String(""+minutos);

                if(segundos < 10)
                        sec = new String("0"+segundos);
		else
			sec = new String(""+segundos);

		if(dia < 10)
                        d = new String("0"+dia);
                else
                        d = new String(""+dia);

		if(mes < 10)
                        m = new String("0"+mes);
                else
                        m = new String(""+mes);

	
		return d+"-"+m+"-"+ano+" "+hrs+":"+min+":"+sec;
	}
	

	/*
	   import java.io.File;
	   import java.io.FileReader;
	   import java.io.FileWriter;
	   import java.io.BufferedReader;
	   import java.io.BufferedWriter;
	   import java.io.IOException;
	   import java.io.FileNotFoundException;
	 */

	/**
	 * Este metodo permite, dada una cadena de caracteres determinada,
	 * salvar la misma como un archivo de texto, o agregarla a un archivo ya existente
	 *
	 * @param filename String
	 * @param dataToWrite String
	 * @param append boolean
	 */
	public void saveFile(/*String filename, */String dataToWrite/*, boolean append*/) {
		String logFile = logDir + "/" + test.getOption("user") + ".log";
		try {
			/**
			 * Creaciòn del objeto FileWriter dado un nombre de archivo determinado
			 * El segundo parametro (append) contiene un valore booleano que
			 * indica si la informacion recibida debe ser agregada el final del
			 * archivo o, en caso contrario, reemplazar la informaciòn ya
			 * existente.
			 */
			
		//	String logFile = logDir + "/" + test.getOption("user") + ".log";

			FileWriter fw = new FileWriter(logFile, true);

			/**
			 * Escritura de la informacion en el archivo
			 */
			fw.write(dataToWrite+"\n");

			/**
			 * Se cierra el archivo
			 */
			fw.close();
		} catch (IOException ioe) {
			/**
			 * Se ha producido un error durante la lectura/escritura del archivo
			 */
			System.out.println("Se ha producido un error durante la lectura del archivo " + logFile);
		}
	}







	//Preset
	public void addHistoryPreset(double c1, double c2, int i){
		if(i==0)
			saveFile("["+time()+"] Preset to RA: "+c1+" ,Dec: "+c2);
		else
			saveFile("["+time()+"] Preset to Alt: "+c1+" ,Az: "+c2);
	}

	public void addHistoryStop(){
		saveFile("["+time()+"] Preset STOPPED");
	}

	//Catalogue
	public void addHistoryCatalogue(double Ra, double Dec){
		saveFile("["+time()+"] Go to RA: "+Ra+" ,Dec: "+Dec+" from Catalogue");
	}

	//Tracking
	public void addHistoryTracking(boolean status){
		if(status){
			saveFile("["+time()+"] Tracking DISABLE");
		}
		else{
			saveFile("["+time()+"] Tracking ENABLE");
		}	
	}

	//Zennith
	public void addHistoryZennith(){
		saveFile("["+time()+"] Go to Zennith");
	}

	//Park
	public void addHistoryPark(){
		saveFile("["+time()+"] Go to Park");
	}

	public void addHistoryLogin(String name){
		saveFile("------------------------------------------------\n["+time()+"] "+name+" is Log In");
	}



}
