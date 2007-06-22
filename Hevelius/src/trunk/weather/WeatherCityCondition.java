import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class WeatherCityId
{
	private String time;
	private String lat;
	private String lon;
	private String sunrice;
	private String sinset;
	private String tzone;
	private String tmp;
	private String flik;
	private String wt;
	private String pres;
	private String vwind;
	private String gust;
	private String direc;
	private String humil;
	private String dpoint;
	private String moon;
	Matcher mat;
	public void setTime(Matcher mat)
	{
		time=mat.group(1);
	}
	public void setId(Matcher mat)
	{
		time=mat.group(1);
	}
 	public void setId(Matcher mat)
	public void setId(Matcher mat)
	public String getNameCity()
        {
                return name;
        }
        public String getId()
        {
                return id;
        }
}
