package Hevelius.heveliusmodules;

import java.util.*;

import Hevelius.interfaz.*;
import Hevelius.utilities.converter.*;

public class Tracking implements Runnable
{
	private double RA;
	private double DEC;
	
	private boolean acstrck;
	private boolean trckState;

	public Tracking()
	{
		RA = 0d;
		DEC = 0d;
	}

	public void setRaDec()
	{
		if(acstrck)
		{
			//Obtain Actual Ra && Dec from Telescope
		}
		else
		{
			RA = interfaz.getDrawingPanel().getCoordinatesPanel().getRa();
			DEC = interfaz.getDrawingPanel().getCoordinatesPanel().getDec();
		}
	}

	public void setTrackingState(boolean state)
	{
		if(!trckState && state)
		{
			trckState = state;
			new Thread(this).start();
		}
		else
			trckState = state;
	}

	public void setACSTracking(boolean state)
	{
			acstrck = state;
	}

	public boolean getTrackingState()
	{
		return trckState;
	}

	public void run()
	{
		double Ra, Dec, Alt, Az;
		while (trckState)
		{
			try
			{
				if(acstrck)
				{
					//ACS Tracking Code
				}
				else
				{
					Ra = interfaz.getDrawingPanel().getCoordinatesPanel().getRa();
					Dec = interfaz.getDrawingPanel().getCoordinatesPanel().getDec();
					if(RA == Ra && DEC== Dec)
					{
						Converter.convertir(Ra,Dec);
						Alt = Converter.getAlt();
						Az = Converter.getAz();
						interfaz.getDrawingPanel().getCoordinatesPanel().setAlt(Alt);
						interfaz.getDrawingPanel().getCoordinatesPanel().setAz(Az);
					}
					else
					{
						RA = Ra;
						DEC = Dec;
					}
				}
				Thread.sleep(20000);
			}
			catch(InterruptedException e)
			{
			}
		}
	}
}
