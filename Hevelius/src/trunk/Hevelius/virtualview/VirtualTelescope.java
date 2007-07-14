package Hevelius.virtualview;

import java.io.*;
import java.util.regex.*;
import java.util.*;
import java.net.*;
import javax.imageio.*;
import java.awt.*;

public class VirtualTelescope
{
	public static Image getScreen(String RA, String DEC, float x, float y)
	{
		try
		{
			//ImageIO dip;
			Image screen=null;
			Pattern pat;
			Matcher mat;
			String line;
			URL url = new URL("http://archive.eso.org/dss/dss/image?ra="+RA+"&dec="+DEC+"&equinox=J2000&name=&x="+x+"&y="+y+"&Sky-Survey=DSS1&mime-type=image%2Fgif&");
			BufferedReader searchhtml =new BufferedReader( new InputStreamReader( url.openStream() ) );
			while((line = searchhtml.readLine()) != null)
			{
				pat = Pattern.compile("<IMG SRC=(.*?) .*?>");
				mat = pat.matcher(line);
				if(mat.find())
					screen = ImageIO.read(new URL("http://archive.eso.org"+mat.group(1)));
			}
			return screen;
		}
		catch(UnknownHostException e )
		{
		}
		catch(SocketException e)
		{
		}
		catch(MalformedURLException e)
		{
		}
		catch(Exception e)
		{
		}
		return null;
	}
}
