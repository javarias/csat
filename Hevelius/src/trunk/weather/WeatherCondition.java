import java.io.*;
import java.util.regex.*;
import java.net.*;
public class WeatherCondition
{
	//BufferedReader lugar;
	//BufferedReader id;
	String cadena;
	//String tmp;
	Pattern pat;
	Matcher mat;
	public static Vector<WeatherCityCondition> vector_condition;
	private String id;
	public WeatherCityCondition(String id)
	{
		this.city = city;
	}
	public Vector ListCityCondition()
	{
		try
		{
			WeatherCityCondition weather=  new WeatherCityCondition();
			vector_condition = new Vector<WeatherCitycondition>();
			id = id.trim();
			URL url2 = new URL("http://xoap.weather.com/weather/local/"+tmp+"?cc=*&prod=xoap&unit=m&par=wx_module_7075&key=wx_module_7075");	
			BufferedReader searchhtml =new BufferedReader( new InputStreamReader( url2.openStream() ) );
			while( (cadena = searchhtml.readLine()) != null )
			{
				cadena=cadena.trim();
				pat = Pattern.compile("^<tm>(.*?)<.*?>");  //time
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					
					String time = new WeatherCityCondition();
					time.setTime(mat);
				}
				pat = Pattern.compile("^<lat>(.*?)<.*?>"); //latitud
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					String lat = new WeatherCityCondition();
					lat.setLat(mat);
				}
				pat = Pattern.compile("^<lon>(.*?)<.*?>"); //longitud
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					String lon=  new WeatherCityCondition();
					lon.setLon(mat);	
				}
				pat = Pattern.compile("^<sunr.*?>(.*?)<.*?>"); //sunrice
				mat = pat.matcher(cadena);
				if(mat.find())              
				{
					String sunr =  new WeatherCityCondition();
					sunr.setSunrice(mat);
				}
				pat = Pattern.compile("^<suns.*?>(.*?)<.*?>"); //sunset
				mat = pat.matcher(cadena);
				if(mat.find())                                                                
				{                            
					String suns = new WeatherCityCondition();
					suns.setSunset(mat);
				}
				pat = Pattern.compile("^<zone.*?>(.*?)<.*?>"); //time zone
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					String zone =  new WeatherCityCondition();
					zone.setZone(mat);
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
					String tem p= new WeatherCityCondition();
					tem.setTm(mat);

				}
				pat = Pattern.compile("^<flik.*?>(.*?)<.*?>"); //feels like
				mat = pat.matcher(cadena);
				if(mat.find())              
				{
					String flik = new WeatherCityCondition();
					flik.setFlik(mat);
				}
				pat = Pattern.compile("^<t>(.*?)<.*?>"); //wheather status
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					String wt = new WeatherCityCondition();
					wt.setWt(mat);
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
					String press= new WeatherCityCondition();
					press.setPres(mat);
				}
				pat = Pattern.compile("^<d>(.*?)<.*?>"); //status pressure
				mat = pat.matcher(cadena);                                                                                                                                   if(mat.find())
				{
					String stapres = new WeatherCityCondition();
					stapres.setStapres(mat);
				}

				pat = Pattern.compile("^<s.*?>(.*?)<.*?>"); //v. wind
				mat = pat.matcher(cadena);
				if(mat.find())
				{
				 	String vwind = new WeatherCityCondition();
					vwind.setVwind(mat);
				}
				pat = Pattern.compile("^<gust.*?>(.*?)<.*?>"); 
				mat = pat.matcher(cadena);
				if(mat.find())
				{                
					String gust = new WeatherCityCondition();
					gust.setGust(mat);
				}
				pat = Pattern.compile("^<t>(.*?)<.*?>"); //direction
				mat = pat.matcher(cadena);
				if(mat.find())
				{              
					String direc = new WeatherCityCondition();
					direc.setDirec(mat);
				}
				pat = Pattern.compile("^<hmid.*?>(.*?)<.*?>"); //humildity
				mat = pat.matcher(cadena);
				if(mat.find())
				{                            
					String hmid = new WeatherCityCondition();
					hmid.setHumil(mat);
				}
				pat = Pattern.compile("^<vis>(.*?)<.*?>"); //visibility
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					String vis = new WeatherCityCondition();
					vis.setVisi(mat);
				}
				pat = Pattern.compile("^<dewp.*?>(.*?)<.*?>"); //dew point
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					String dewp = new WeatherCityCondition();
					dewp.setDpoint(match);
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
					String moon = new WeatherCityCondition();
					moon.setMoon(mat);
					break;
				}

			}

			
		
				















			/*	while( (cadena = paginaHtml.readLine()) != null )
			{
				cadena=cadena.trim();
				pat = Pattern.compile("^<loc.*?=\"(.*?)\".*?>(.*?)<.*?>");
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					//System.out.println("ID= "+mat.group(1)+"     City= "+ mat.group(2));
					weather.setNameCity=mat.group(2);
					weather.setId=mat.group(1);
					vector_city.add(weather);
				}

			}*/
			return vector_condition;
		}
		catch(Exception e)
		{
			e.printStackTrace();

		}
	}
}
