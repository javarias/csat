/**
* Safety class is used to update periodically
* danger state calling CSATStatus component to
* retrieve state and setting it to TelStatusPanel.
*/

package Hevelius.heveliusmodules;

import Hevelius.interfaz.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import alma.TYPES.*;

public class Safety implements Runnable
{
	private boolean working = false;
	private boolean doControl = true;
	/**
	* Method that is executed independently with each thread that 
	* is created from the class.
	*/
	public void run(){
		while(doControl){
			try{
				if(interfaz.getDrawingPanel().getCSATStatus()!=null)
                                {
					working = true;
                                        try
                                        {
						RadecPos rd = new RadecPos();
						rd.ra = (interfaz.getDrawingPanel().getCoordinatesPanel().getRa());
						rd.dec = (interfaz.getDrawingPanel().getCoordinatesPanel().getDec());
						interfaz.getDrawingPanel().getTelStatusPanel().setDangerState(interfaz.getDrawingPanel().getCSATStatus().getSafety(rd));
                                        }
                                        catch(Exception e)
                                        {
                                                e.printStackTrace();
                                        }
					working = false;
                                }
                                //Thread.sleep(300000);
				Thread.sleep(20000);


				
			}catch(InterruptedException e){
				System.out.println("Error en Thread de Safety Class");
			}
		}
		System.out.println("Safety Thread Ended without problems!");
	}
	/**
	* Method used to retrieve information about Thread status, to know 
	* if it is secure to stop the ACS components.
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
