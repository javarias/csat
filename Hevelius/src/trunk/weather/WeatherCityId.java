import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class WeatherCityId
{
	Mather mat;
	public void setNameCity(Mather mat)
	{
		String name=mat.group(2);
	}
	public void setId(Mather mat)
	{
		String id=mat.group(1);
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
