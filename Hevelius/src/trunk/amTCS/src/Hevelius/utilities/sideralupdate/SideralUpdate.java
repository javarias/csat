package Hevelius.utilities.sideralupdate;

import java.util.*;
import Hevelius.acsmodules.*;
import Hevelius.interfaz.*;
import alma.TYPES.*;

public class SideralUpdate implements Runnable
{
	private RadecPosHolder r; 
	private AltazPosHolder a;

	public SideralUpdate(){
		r = new RadecPosHolder();
		a = new AltazPosHolder();
	}
	
	public void run()
	{
		while (true)
		{
			try
			{
				if(interfaz.getDrawingPanel()==null)
					System.out.println("ABC");
				if(interfaz.getDrawingPanel().getCSATStatus()==null)
					System.out.println("csat");
				if(interfaz.getDrawingPanel().getCSATStatus()!=null)
				{
					interfaz.getDrawingPanel().getCSATStatus().getPos(r,a);
					interfaz.getDrawingPanel().getCoordinatesPanel().setRa(r.value.ra);
        	                        interfaz.getDrawingPanel().getCoordinatesPanel().setDec(r.value.dec);
                	                interfaz.getDrawingPanel().getCoordinatesPanel().setAlt(a.value.alt);
                        	        interfaz.getDrawingPanel().getCoordinatesPanel().setAz(a.value.az);
				}
				Thread.sleep(2000);
			}
			catch(InterruptedException e)
			{
			}
		}
	}
}
