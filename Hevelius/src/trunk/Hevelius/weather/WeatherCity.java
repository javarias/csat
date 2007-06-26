package Hevelius.weather;

import java.io.*;
import java.util.regex.*;
import java.net.*;
import java.util.*;
public class WeatherCity
{
	//BufferedReader lugar;
	//BufferedReader id;
	String cadena;
	//String tmp;
	Pattern pat;
	Matcher mat;
	public static Vector<WeatherCityId> vector_city;
	private String city;
	public WeatherCity(String city)
	{
		this.city = city;
	}
	public Vector ListCity()
	{
		try
		{
			WeatherCityId weather;
			vector_city = new Vector<WeatherCityId>();
			city=city.trim();
			city= city.replace(' ','+');
			URL url = new URL("http://xoap.weather.com/search/search?where="+city );
			BufferedReader paginaHtml =new BufferedReader( new InputStreamReader( url.openStream() ) );
			//}
			//catch (MalformedURLException e)
			//{
			//	System.out.println( e );
			//}

			//BufferedReader paginaHtml =new BufferedReader( new InputStreamReader( url.openStream() ) );
			while( (cadena = paginaHtml.readLine()) != null )
			{
				cadena=cadena.trim();
				pat = Pattern.compile("^<loc.*?=\"(.*?)\".*?>(.*?)<.*?>");
				mat = pat.matcher(cadena);
				if(mat.find())
				{
					//System.out.println("ID= "+mat.group(1)+"     City= "+ mat.group(2));
					weather=  new WeatherCityId();
					weather.setNameCity(mat);
					weather.setId(mat);
					vector_city.add(weather);
				}


			}
			return vector_city;
	} 
	catch (UnknownHostException e )
	{
		System.out.println("false");
	}

	catch (Exception e)
	{
		e.printStackTrace();
	}
	return null;
}
}
