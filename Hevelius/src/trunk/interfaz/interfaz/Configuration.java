import java.util.Properties;
import java.io.*;

public class Configuration extends Properties{
	
	static Properties propiedades;

      
      
	static File file = new File("hevelius.properties");
	static File dir = new File("~/.hevelius");
	
	public Configuration(){
		
		/*if(!dir.exists()){
			dir.mkdir();
		}*/
				
		if(!file.exists()){
			try{
				PrintWriter archivo = new PrintWriter(
						new FileWriter("hevelius.properties",true));
				
				archivo.println("### Hevelius Configuration File ###");
				archivo.println("");
				archivo.println("#SYSTEM");
				archivo.println("coordinate = 0");
				archivo.println("location = null");
				archivo.println("");
				archivo.println("#INTERFACE");
				archivo.println("#Modules");
				archivo.println("weather = 1");
				archivo.println("opengl = 1");
				archivo.println("compass = 1");
				archivo.println("background = 0");
				archivo.println("");
				
				archivo.close();
								
			}catch(IOException e){
				System.out.println("Error al crear el archivo de Configuration");
			}
		}
		
		try{
		    FileInputStream f = new FileInputStream("hevelius.properties");
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
			OutputStream s = new FileOutputStream(new File("hevelius.properties"));
			propiedades.store(s,"#HEVELIUS CONFIGURATION FILE");
			s.close();
		}catch(Exception e){
			System.out.println("Error al Guadar las configuration");
		}
	}

}
