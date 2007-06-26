package Hevelius.weather;

import java.io.*;
import java.util.regex.*;
import java.util.*;
import java.net.*;
public class WeatherCondition
{
	//BufferedReader lugar;
	//BufferedReader id;
	String cadena;
	//String id;
	//String tmp;
	Pattern pat;
	Matcher mat;
	//WeatherCityCondition weather;
	public static Vector<WeatherCityCondition> vector_condition;
	private String id;
	public WeatherCondition(String id)
	{
		this.id = id;
	}
	public Vector ListCityCondition()
	{
		try
		{
			WeatherCityCondition weather;
			weather = new WeatherCityCondition();
			vector_condition = new Vector<WeatherCityCondition>();
			id = id.trim();
			URL url2 = new URL("http://xoap.weather.com/weather/local/"+id+"?cc=*&prod=xoap&unit=m&par=wx_module_7075&key=wx_module_7075");	
			BufferedReader searchhtml =new BufferedReader( new InputStreamReader( url2.openStream() ) );
			while( (cadena = searchhtml.readLine()) != null )
			{
				cadena=cadena.trim();
				pat = Pattern.compile("^<tm>(.*?)<.*?>");  //time
				mat = pat.matcher(cadena);
				if(mat.find())
				{

					//String time = new WeatherCityCondition();
					//weather = new WeatherCityCondition();
					weather.setTime(mat);
				}
				pat = Pattern.compile("^<lat>(.*?)<.*?>"); //latitud
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					//String lat = new WeatherCityCondition();
					weather.setLat(mat);
				}
				pat = Pattern.compile("^<lon>(.*?)<.*?>"); //longitud
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					//String lon=  new WeatherCityCondition();
					weather.setLon(mat);	
				}
				pat = Pattern.compile("^<sunr.*?>(.*?)<.*?>"); //sunrice
				mat = pat.matcher(cadena);
				if(mat.find())              
				{
					//String sunr =  new WeatherCityCondition();
					weather.setSunrice(mat);
				}
				pat = Pattern.compile("^<suns.*?>(.*?)<.*?>"); //sunset
				mat = pat.matcher(cadena);
				if(mat.find())                                                                
				{                            
					//String suns = new WeatherCityCondition();
					weather.setSunset(mat);
				}
				pat = Pattern.compile("^<zone.*?>(.*?)<.*?>"); //time zone
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					//String zone =  new WeatherCityCondition();
					weather.setZone(mat);
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
					//String tem = new WeatherCityCondition();
					weather.setTm(mat);

				}
				pat = Pattern.compile("^<flik.*?>(.*?)<.*?>"); //feels like
				mat = pat.matcher(cadena);
				if(mat.find())              
				{
					//String flik = new WeatherCityCondition();
					weather.setFlik(mat);
				}
				pat = Pattern.compile("^<t>(.*?)<.*?>"); //wheather status
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					//String wt = new WeatherCityCondition();
					weather.setWt(mat);
				}
				pat = Pattern.compile("^<icon>(.*?)<.*?>"); //icon wheather status
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					//String wt = new WeatherCityCondition();
					weather.setIconWt(mat);
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
					//String press= new WeatherCityCondition();
					weather.setPres(mat);
				}
				pat = Pattern.compile("^<d>(.*?)<.*?>"); //status pressure				
				mat = pat.matcher(cadena);      
				if(mat.find())
				{
					//String stapres = new WeatherCityCondition();
					weather.setStapres(mat);
				}

				pat = Pattern.compile("^<s.*?>(.*?)<.*?>"); //v. wind
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					//String vwind = new WeatherCityCondition();
					weather.setVwind(mat);
				}
				pat = Pattern.compile("^<gust.*?>(.*?)<.*?>"); 
				mat = pat.matcher(cadena);
				if(mat.find())
				{                
					//String gust = new WeatherCityCondition();
					weather.setGust(mat);
				}
				pat = Pattern.compile("^<t>(.*?)<.*?>"); //direction
				mat = pat.matcher(cadena);
				//System.out.println(mat.group());
				if(mat.find())
				{              
					//String direc = new WeatherCityCondition();
					weather.setDirec(mat);
				}
				pat = Pattern.compile("^<hmid.*?>(.*?)<.*?>"); //humildity
				mat = pat.matcher(cadena);
				if(mat.find())
				{                            
					//String hmid = new WeatherCityCondition();
					weather.setHumil(mat);
				}
				pat = Pattern.compile("^<vis>(.*?)<.*?>"); //visibility
				mat = pat.matcher(cadena);

				if(mat.find())
				{
					//String vis = new WeatherCityCondition();
					weather.setVisi(mat);
				}
				pat = Pattern.compile("^<uv>"); //STOP
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
				pat = Pattern.compile("^<t>(.*?)<.*?>"); //direction
				mat = pat.matcher(cadena);
				//System.out.println(mat.group());
				if(mat.find())
				{              
					//String direc = new WeatherCityCondition();
					weather.setUv(mat);
				}
				pat = Pattern.compile("^<dewp.*?>(.*?)<.*?>"); //dew point
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					//String dewp = new WeatherCityCondition();
					weather.setDpoint(mat);
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
					//String moon = new WeatherCityCondition();
					weather.setMoon(mat);
					break;
				}

			}
			vector_condition.add(weather);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return vector_condition;	
	}

}
