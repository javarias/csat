/**
* Esta clase se encarga de setear el nombre de la ciudad 
* a buscar y de setear el Id correspondiente.
* Tambien posee sus respectivos get's para poder
* tener acceso a ellos.
*/

package Hevelius.weather;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class WeatherCityId
{
	public String id;
	public String name;
	Matcher mat;
	public void setNameCity(Matcher mat)
	{
		name=mat.group(2);
	}
	public void setId(Matcher mat)
	{
		id=mat.group(1);
	}
	public String getNameCity()
        {
                return name;
        }
        public String getId()
        {
                return id;
        }
}
