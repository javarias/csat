package Hevelius.utilities.historial;

import java.io.*;
import java.util.*;
import Hevelius.acsmodules.*;
import Hevelius.interfaz.*;
import alma.TYPES.*;

public class Historial //implements Runnable
{
	static final public String logDir  = System.getProperty("user.home") + "/.hevelius/history";
	static public String logFile = logDir + "/" + "history.log";

	static File dir = new File(logDir);
	static File file = new File(logFile);
	
	static Calendar calendario = new GregorianCalendar();
/*	int hora;
	int minuto;
	int segundo;*/

	//private static Configuration conf = new Configuration();

	public Historial(){
		static public String logFile = logDir + "/history.log";

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
		int hora = calendario.get(Calendar.HOUR_OF_DAY);
		int minutos = calendario.get(Calendar.MINUTE);
		int segundos = calendario.get(Calendar.SECOND);
		
		return hora+":"+minutos+":"+segundos;
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
		try {
			/**
			 * Creaciòn del objeto FileWriter dado un nombre de archivo determinado
			 * El segundo parametro (append) contiene un valore booleano que
			 * indica si la informacion recibida debe ser agregada el final del
			 * archivo o, en caso contrario, reemplazar la informaciòn ya
			 * existente.
			 */
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
			System.out.println(
					"Se ha producido un error durante la lectura del archivo " + logFile);
		}
	}







	//Preset
	public void addHistoryPreset(double c1, double c2, int i){
		if(i==0)
			saveFile("Preset to RA: "+c1+" ,Dec: "+c2+" at "+time());
		else
			saveFile("Preset to Alt: "+c1+" ,Az: "+c2+" at "+time());
	}

	public void addHistoryStop(){
		saveFile("Preset STOPED at "+time());
	}

	//Catalogue
	public void addHistoryCatalogue(double Ra, double Dec){
		saveFile("Go to RA: "+Ra+" ,Dec: "+Dec+" from Catalogue at "+time());
	}

	//Tracking
	public void addHistoryTracking(boolean status){
		if(status){
			saveFile("Tracking ENABLE at "+time());
		}
		else{
			saveFile("Tracking DISABLE at "+time());
		}	
	}

	//Zennith
	public void addHistoryZennith(){
		saveFile("Go to Zennith at "+time());
	}

	//Park
	public void addHistoryPark(){
		saveFile("Go to Park at "+time());
	}

	public void addHistoryLogin(String name){
		saveFile("\n\n"+name+" is Log In at "+time());
	}



}
