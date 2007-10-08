package Hevelius.utilities.historial;

import java.util.*;
import Hevelius.acsmodules.*;
import Hevelius.interfaz.*;
import alma.TYPES.*;

public class Historial implements Runnable
{
	private RadecPosHolder r; 
	private AltazPosHolder a;
	private double ST;

	public Historial(){
		r = new RadecPosHolder();
		a = new AltazPosHolder();
	}

	public void run()
	{
		while (true)
		{
			try
			{
//				interfaz.getDrawingPanel().repaint();
				if(interfaz.getDrawingPanel().getCSATStatus()!=null)
				{
					try
					{
						interfaz.getDrawingPanel().getCSATStatus().getPos(r,a);
						ST = interfaz.getDrawingPanel().getCSATStatus().getSiderealTime();
						interfaz.getDrawingPanel().getCoordinatesPanel().setRa(r.value.ra);
						interfaz.getDrawingPanel().getCoordinatesPanel().setDec(r.value.dec);
						interfaz.getDrawingPanel().getCoordinatesPanel().setAlt(a.value.alt);
						interfaz.getDrawingPanel().getCoordinatesPanel().setAz(a.value.az);
						interfaz.getDrawingPanel().setSideralTime(ST);
						interfaz.getDrawingPanel().getVirtualTelescopePanel().getListener().setAltAzDest((float)a.value.alt, (float)a.value.az);

					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{
				System.out.println("The thread updating information died.");
			}
		}
	}
}
