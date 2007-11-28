/**
* Esta clase es la encargada de realiar la busqueda
* de la ciudad elejida, dentro de la base de datos.
*/

package Hevelius.weather;

import java.io.*;
import java.util.regex.*;
import java.net.*;
import java.util.*;
public class WeatherCity
{
	String cadena;
	Pattern pat;
	Matcher mat;
	public static Vector<WeatherCityId> vector_city;
	private String city;
	/**
	* Este constructor recibe el nombre de la ciudad a buscar y lo 
	* setea en las variables locales para trabajarlas posteriormente.
	* 
	* @param city	String que posee el nombre de la ciudad a buscar.
	*/
	public WeatherCity(String city)
	{
		this.city = city;
	}
	/**
	* Esta funcion se encarga de buscar los datos correspondientes 
	* a la ciudad deseada.
	* @return	 Vector Retorna el vector que contiene el nombre y el Id
	* de la ciudad buscada.
	*/
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
			while( (cadena = paginaHtml.readLine()) != null )
			{
				cadena=cadena.trim();
				pat = Pattern.compile("^<loc.*?=\"(.*?)\".*?>(.*?)<.*?>");
				mat = pat.matcher(cadena);
				if(mat.find())
				{
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
		vector_city = new Vector<WeatherCityId>();
		return vector_city;
	}

	catch (Exception e)
	{
		//e.printStackTrace();
		vector_city = new Vector<WeatherCityId>();
                return vector_city;
	}
}
}
