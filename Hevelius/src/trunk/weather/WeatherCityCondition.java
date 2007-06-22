import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class WeatherCityCondition
{
	private String time;
	private String lat;
	private String lon;
	private String sunrice;
	private String sunset;
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
	public void setLat(Matcher mat)
	{
		lat=mat.group(1);
	}
 	public void setLon(Matcher mat)
	{
		lon=mat.group(1);
	}	
	public void setSunrice(Matcher mat)
	{
		sunrice=mat.group(1);
	}
	public void sunset(Matcher mat)
	{
		sunset=mat.group(1);
	}
	public void setZone(Matcher mat)
	{
		tzone=mat.group(1);
	}
	public void setTm(Matcher mat)
	{
		tmp=mat.group(1);
	}
	public void setFlik(Matcher mat)
	{
		flik=mat.group(1);
	}
	public void setWt(Matcher mat)
	{
		wt=mat.group(1);
	}
	public void setPres(Matcher mat)
	{
		pres=mat.group(1);
	}
	public void setVwind(Matcher mat)
	{
		vwind=mat.group(1);
	}
	public void setGust(Matcher mat)
	{
		gust=mat.group(1);
	}
	public void setDirec(Matcher mat)
	{
		direc=mat.group(1);
	}
	public void setHumil(Matcher mat)
	{
		humil=mat.group(1);
	}
	public void setDpoint(Matcher mat)
	{
		dpoint=mat.group(1);
	}
	public void setMoon(Matcher mat)
	{
		moon=mat.group(1);
	}
	public String getTime()
	{
		return time;
	}
	public String getLat()
	{
		return lat;
	}
	public String getLon()
	{
		return lon;
	}
	public String getSunrice()
	{
		return sunrice;
	}
	public String getSunset(Matcher mat)
	{
		return sunset;
	}
	public String getTzone()
	{
		return tzone;
	}
	public String getTm()
	{
		return tmp;
	}
	public String getFlik()
	{
		return flik;
	}
	public String getWt()
	{
		return wt;
	}
	public String getPres()
	{
		return pres;
	}
	public String getVwind()
	{
		return vwind;
	}
	public String getGust()
	{
		return gust;
	}
	public String getDirec()
	{
		return direc;
	}
}