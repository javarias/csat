import java.io.*;
import java.util.regex.*;
import java.net.*;
import java.util.*;
public class Weather {

	public static Vector<WeatherCityId> vector_city;
	public static void main(String[] args) {
		BufferedReader lugar;
		BufferedReader id;
		String cadena;
		String tmp;
		Pattern pat;  
		Matcher mat;
		int i;
		//public static FileOutputStream file;
		try
		{
			System.out.println("Ingrese la ciudad que desea revisar");
			lugar=new BufferedReader(new InputStreamReader(System.in));
			tmp=lugar.readLine();
			WeatherCity weather=  new WeatherCity(tmp);
			vector_city = new Vector<WeatherCityId>();
			vector_city=weather.ListCity();
			int n= vector_city.length;
			for(i=1;i<n;i++)
			{
			System.out.println(vector_city.getNameCity()+" "+vector_city.getId());
			}
			//String var=tmp.trim();
			//pat = Pattern.compile("s// //\\+//");
			//mat = pat.matcher(var);
			//String finaly = var.replace(' ','+');
			//URL url = new URL("http://xoap.weather.com/search/search?where="+finaly );
			//BufferedReader paginaHtml =new BufferedReader( new InputStreamReader( url.openStream() ) );
			//while( (cadena = paginaHtml.readLine()) != null ) 
			//{
			//	cadena=cadena.trim();
			//	pat = Pattern.compile("^<loc.*?=\"(.*?)\".*?>(.*?)<.*?>");
			//	mat = pat.matcher(cadena);
			//	if(mat.find())
			//		System.out.println("ID= "+mat.group(1)+"     City= "+ mat.group(2));
			//}
			System.out.println("Ingrese el ID de la ciudad que desea");
			id=new BufferedReader(new InputStreamReader(System.in));
			tmp=id.readLine();
			tmp=tmp.trim();
			URL url2 = new URL("http://xoap.weather.com/weather/local/"+tmp+"?cc=*&prod=xoap&unit=m&par=wx_module_7075&key=wx_module_7075");
			BufferedReader searchhtml =new BufferedReader( new InputStreamReader( url2.openStream() ) );
			while( (cadena = searchhtml.readLine()) != null )
			{
				cadena=cadena.trim();
				pat = Pattern.compile("^<tm>(.*?)<.*?>");  //time
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					System.out.println("time= "+mat.group(1));
					String time=mat.group(1);	
				}
				pat = Pattern.compile("^<lat>(.*?)<.*?>"); //latitud
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					System.out.println("lat= "+mat.group(1));
					String lat=mat.group(1);
				}
				pat = Pattern.compile("^<lon>(.*?)<.*?>"); //longitud
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					System.out.println("lon= "+mat.group(1));
					String lon=mat.group(1);
				}
				pat = Pattern.compile("^<sunr.*?>(.*?)<.*?>"); //sunrice
				mat = pat.matcher(cadena);
				if(mat.find())              
				{
					System.out.println("sunr= "+mat.group(1));
					String sunr=mat.group(1);
				}
				pat = Pattern.compile("^<suns.*?>(.*?)<.*?>"); //sunset
				mat = pat.matcher(cadena);
				if(mat.find())                                                                
				{                            
					System.out.println("suns= "+mat.group(1));
					String suns=mat.group(1);
				}
				pat = Pattern.compile("^<zone.*?>(.*?)<.*?>"); //time zone
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					System.out.println("zone= "+mat.group(1));
					String zone=mat.group(1);
				}

				pat = Pattern.compile("^<cc>");//STOP 
				mat = pat.matcher(cadena);
				if(mat.find())
				{                              
					break;
					//System.out.println("zone= "+mat.group(1));
					//String zone=mat.group(1);
				}
			}
			while((cadena = searchhtml.readLine()) != null )
			{
				cadena=cadena.trim();
				pat = Pattern.compile("^<tmp>(.*?)<.*?>"); //temperature
				mat = pat.matcher(cadena);
				if(mat.find())              
				{
					System.out.println("temp= "+mat.group(1));
					String temp=mat.group(1);

				}
				pat = Pattern.compile("^<flik.*?>(.*?)<.*?>"); //feels like
				mat = pat.matcher(cadena);
				if(mat.find())              
				{
					System.out.println("flik= "+mat.group(1));
					String flik=mat.group(1);
				}
				pat = Pattern.compile("^<t>(.*?)<.*?>"); //wheather status
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					System.out.println("wt= "+mat.group(1));
					String wt=mat.group(1);
				}
				pat = Pattern.compile("^<bar>"); //STOP
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					break;
					//System.out.println("wt= "+mat.group(1));
					//String wt=mat.group(1);
				}
			}
			while((cadena = searchhtml.readLine()) != null )
			{

				cadena=cadena.trim();
				pat = Pattern.compile("^<r>(.*?)<.*?>"); //pressure
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					System.out.println("pressure= "+mat.group(1));
					String pres=mat.group(1);
				}
				pat = Pattern.compile("^<d>(.*?)<.*?>"); //status pressure
				mat = pat.matcher(cadena);                                                                                                                                   if(mat.find())
				{
					System.out.println("status presurre= "+mat.group(1));
					String stapres=mat.group(1);
				}

				pat = Pattern.compile("^<s.*?>(.*?)<.*?>"); //v. wind
				mat = pat.matcher(cadena);
				if(mat.find())
				{                  
					System.out.println("vwind= "+mat.group(1));
					String vwind=mat.group(1);
				}
				pat = Pattern.compile("^<gust.*?>(.*?)<.*?>"); 
				mat = pat.matcher(cadena);
				if(mat.find())
				{                
					System.out.println("gust= "+mat.group(1));
					String gust=mat.group(1);
				}
				pat = Pattern.compile("^<t>(.*?)<.*?>"); //direction
				mat = pat.matcher(cadena);
				if(mat.find())
				{              
					System.out.println("direc= "+mat.group(1));
					String direc=mat.group(1);
				}
				pat = Pattern.compile("^<hmid.*?>(.*?)<.*?>"); //humildity
				mat = pat.matcher(cadena);
				if(mat.find())
				{                            
					System.out.println("hmid= "+mat.group(1));
					String hmid=mat.group(1);
				}
				pat = Pattern.compile("^<vis>(.*?)<.*?>"); //visibility
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					System.out.println("vis= "+mat.group(1));
					String vis=mat.group(1);
				}
				pat = Pattern.compile("^<dewp.*?>(.*?)<.*?>"); //dew point
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					System.out.println("dewp= "+mat.group(1));
					String dewp=mat.group(1);
				}
				pat = Pattern.compile("^<moon>"); //STOP
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					break;
					//System.out.println("hmid= "+mat.group(1));
					//String hmid=mat.group(1);
				}
			}                      
			while((cadena = searchhtml.readLine()) != null )
			{

				cadena=cadena.trim();
				pat = Pattern.compile("^<t>(.*?)<.*?>"); //moon fase
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					System.out.println("moon= "+mat.group(1));
					String moon=mat.group(1);
					break;
				}

			}

			/*BufferedReader reader= new BufferedReader(new FileReader(args[0])); 
			  String linea= reader.readLine(); 
			  while(linea!=null) 
			  { 
			  System.out.println(linea); 
			  linea= reader.readLine();
			  pat = Pattern.compile("^<loc.*?>(.*?)<.*?>");
			  mat = pat.matcher(linea);*/



		}
		catch (Exception e) 
		{
			e.printStackTrace(); 
		}
		}


	}
