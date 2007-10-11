package Hevelius.utilities.historial;

import java.util.*;
import Hevelius.acsmodules.*;
import Hevelius.interfaz.*;
import alma.TYPES.*;

public class Historial //implements Runnable
{
	static final public String logDir  = System.getProperty("user.home") + "/.hevelius/logs";
	static final public String logFile = configurationDir + "/" + "user.log";

	static File dir = new File(logDir);
	static File file = new File(logFile);

	public Historial(){

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

	//Preset, Zennith, Park, Catalogue
	public void addLogPreset(double Ra, double Dec){
		
	}

	//Catalogue
	public void addLogCatalogue(double Ra, double Dec){
		
	}

	//Tracking
	public void addLogTracking(boolean status){
		
	}

	//Zennith
	public void addLogZennith(double Ra, double Dec){
		
	}

	//Park
	public void addLogPark(double Ra, double Dec){
		
	}




}
