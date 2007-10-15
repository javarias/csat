package Hevelius.interfaz;

import java.util.Properties;
import java.io.*;

public class Configuration extends Properties{
	
	static Properties propiedades;

      
      
//	static File file = new File("hevelius.properties");
//	static File dir = new File("~/.hevelius");
	static final public String configurationDir  = System.getProperty("user.home") + "/.hevelius";
	static final public String configurationFile = configurationDir + "/" + "hevelius.properties";

	static File dir = new File(configurationDir);
	static File file = new File(configurationFile);
	
	public Configuration(){
		
		if(!dir.exists()){
			dir.mkdir();
		}
				
		if(!file.exists()){
			try{
//				PrintWriter archivo = new PrintWriter(
//						new FileWriter("hevelius.properties",true));
				PrintWriter archivo = new PrintWriter(
						new FileWriter(configurationFile,true));
				
				archivo.println("### Hevelius Configuration File ###");
				archivo.println("");
				//archivo.println("#SYSTEM");
				archivo.println("coordinate = 0");
				archivo.println("location = (none)");
				archivo.println("city = (none)");
				archivo.println("tracking = 1");
				archivo.println("");
				//archivo.println("#INTERFACE");
				//archivo.println("#Modules");
				/*archivo.println("weather = 1");
				archivo.println("opengl = 1");
				archivo.println("compass = 1");
				archivo.println("background = 0");
				archivo.println("");*/
				archivo.println("user = ");

				archivo.close();
								
			}
			catch(IOException e)
			{
				System.out.println("Error al crear el archivo de Configuration");
			}
		}
		
		try{
		    //FileInputStream f = new FileInputStream("hevelius.properties");
		    FileInputStream f = new FileInputStream(configurationFile);
		    propiedades = new Properties();
		    propiedades.load(f);
		    f.close();
			}catch(Exception e){
				System.out.println("Error al cargar la Configuration");
		}
		
	}

	
	
	public String getOption(String a){
		return propiedades.getProperty(a).trim();
	}

	public void setOption(String a, String b){
		propiedades.setProperty(a, b);
	}

	public void store(){
		try{
			//OutputStream s = new FileOutputStream(new File("hevelius.properties"));
			OutputStream s = new FileOutputStream(new File(configurationFile));
			propiedades.store(s,"#HEVELIUS CONFIGURATION FILE");
			s.close();
		}catch(Exception e){
			System.out.println("Error al Guadar las configuration");
		}
	}

}
