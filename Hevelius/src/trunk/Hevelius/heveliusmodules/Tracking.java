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
			RA = interfaz.getDrawingPanel().getCoordinatesPanel().getRa();
			DEC = interfaz.getDrawingPanel().getCoordinatesPanel().getDec();
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
		if(!state)
			setACSTracking(acstrck);
	}

	public void setACSTracking(boolean state)
	{
		acstrck = state;
		if(state)
			interfaz.getDrawingPanel().getCSATControl().setTrackingStatus(trckState);
		else
			interfaz.getDrawingPanel().getCSATControl().setTrackingStatus(false);
	}

	public boolean getTrackingState()
	{
		return trckState;
	}

	public boolean getACSTracking()
	{
		return acstrck;
	}

	public void run()
	{
		double Ra, Dec, Alt, Az;
		while (trckState)
		{
			try
			{
				if(!acstrck)
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
