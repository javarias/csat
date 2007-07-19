package Hevelius.telescopefunctions;

import java.util.*;

import Hevelius.interfaz.*;
import Hevelius.utilities.converter.*;

public class Tracking implements Runnable
{
	private boolean state = true;

	public void setState(boolean state)
	{

		this.state = state;
	}
	
	public boolean getState()
	{
		return state;
	}

	public void run()
	{
		while (true)
		{
			try
			{
				if(state)
				{
					Converter.convertir(interfaz.getDrawingPanel().getCoordinatesPanel().getRa(),
							interfaz.getDrawingPanel().getCoordinatesPanel().getDec());
					interfaz.getDrawingPanel().getCoordinatesPanel().setAlt(Converter.getAlt());
					interfaz.getDrawingPanel().getCoordinatesPanel().setAz(Converter.getAz());
					Thread.sleep(20000);
				}
			}
			catch(InterruptedException e)
			{
			}
		}
	}
}
