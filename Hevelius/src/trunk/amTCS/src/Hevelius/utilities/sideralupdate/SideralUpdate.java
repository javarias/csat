package Hevelius.utilities.sideralupdate;

import java.util.*;
import Hevelius.acsmodules.*;
import Hevelius.interfaz.*;
import alma.TYPES.*;

public class SideralUpdate implements Runnable
{
	private RadecPosHolder r; 
	private AltazPosHolder a;
	private double ST;

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
				if(interfaz.getDrawingPanel().getCSATStatus()!=null)
				{
					try
					{
						interfaz.getDrawingPanel().getCSATStatus().getPos(r,a);
						ST = interfaz.getDrawingPanel().getCSATStatus().getSideralTime();
						interfaz.getDrawingPanel().getCoordinatesPanel().setRa(r.value.ra);
						interfaz.getDrawingPanel().getCoordinatesPanel().setDec(r.value.dec);
						interfaz.getDrawingPanel().getCoordinatesPanel().setAlt(a.value.alt);
						interfaz.getDrawingPanel().getCoordinatesPanel().setAz(a.value.az);
						interfaz.getDrawingPanel().setSideralTime(ST);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				Thread.sleep(2000);
			}
			catch(InterruptedException e)
			{
				System.out.println("The thread updating information died.");
			}
		}
	}
}
