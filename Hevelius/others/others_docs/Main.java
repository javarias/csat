import java.io.*;
import java.util.regex.*;

public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	BufferedReader lugar;
	String search;
        String tmp;
	Pattern pat;                                          Matcher mat;
	public static FileOutputStream file;
	try
	{
		System.out.println("Ingrese la ciudad que desea revisar");
		lugar=new BufferedReader(new InputStreamReader(System.in));
		tmp=lugar.readLine();
		// FIXME esto no debe usar wget.
		search="wget http://xoap.weather.com/search/search?where="+tmp;
		//System.out.println(search);
   		final Process process = Runtime.getRuntime().exec(search); 
		InputStream is = process.getInputStream(); 
		BufferedReader br = new BufferedReader (new InputStreamReader (is));
		String aux=br.readLine();
		//System.out.println(aux);
		FileInputStream file = new FileInputStream("search?where="+tmp);
		BufferedReader reader= new BufferedReader(new FileReader(args[0])); 
		String linea= reader.readLine(); 
		while(linea!=null) 
		{ 
			System.out.println(linea); 
			linea= reader.readLine();
			pat = Pattern.compile("^<loc.*?>(.*?)<.*?>);
			mat = pat.matcher(linea);
								
		}
		
	}
	catch (Exception e) 
	{
		e.printStackTrace(); 
	}
}
    

}
