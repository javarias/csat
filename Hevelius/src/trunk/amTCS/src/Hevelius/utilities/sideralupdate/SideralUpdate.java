package Hevelius.utilities.sideralupdate;

import java.util.*;
import Hevelius.acsmodules.*;
import Hevelius.interfaz.*;
import alma.TYPES.*;

public class SideralUpdate implements Runnable
{
	private RadecPosHolder r = new RadecPosHolder(); 
	private AltazPosHolder a = new AltazPosHolder();
	
	public void run()
	{
		while (true)
		{
			try
			{
				interfaz.getDrawingPanel().getCSATStatus().getPos(r,a);
				interfaz.getDrawingPanel().getCoordinatePanel().setRa(r.value.ra);
                                interfaz.getDrawingPanel().getCoordinatePanel().setDec(r.value.dec);
                                interfaz.getDrawingPanel().getCoordinatePanel().setAlt(a.value.alt);
                                interfaz.getDrawingPanel().getCoordinatePanel().setAz(a.value.az);
				Thread.sleep(2000);
			}
			catch(InterruptedException e)
			{
			}
		}
	}
}
