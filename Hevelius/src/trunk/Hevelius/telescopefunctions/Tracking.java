package Hevelius.telescopefunctions;

import java.util.*;


public class Tracking implements Runnable
{
	private boolean state;


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
					//Converter.convert(CoordinatePanel.getRa(),CoordinatePanel.getDec());
					//CoordinatePanel.setAlt(Converter.getAlt());
					//CoordinatePanel.setAz(Converter.getAz());
					Thread.sleep(20000);
				}
				catch(InterruptedException e)
				{
				}
			}
		}
	}
}
