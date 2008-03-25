/**
* SideralUpdate class is used to update periodically information about position 
* and time CSATStatus component to retrieve current information and then it 
* sends information to GUI.
*/

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
	private boolean working = false;
	private boolean doControl = true;

	public SideralUpdate(){
		r = new RadecPosHolder();
		a = new AltazPosHolder();
	}

	/**
	* This method is thread's action that will be executed periodically.
	*/
	public void run()
	{
		while (doControl)
		{
			try
			{
				if(interfaz.getDrawingPanel().getCSATStatus()!=null)
				{
					working = true;
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
					working = false;
				}
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{
				System.out.println("The thread updating information died.");
			}
		}
		System.out.println("Sideral Update Thread Ended without problems!");
	}

	/**
	* This methos is used to return current state of Thread, so if it is interacting with TCS 
	* the program cannot terminate, it will have to wait for this thread to end.
	* @return	boolean that informs if thread is currently interacting with TCS.
	*/
	public boolean getThreadState()
	{
		return working;
	}

	public void stopThread()
	{
		doControl = false;
	}
}
