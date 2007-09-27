package Hevelius.heveliusmodules;

import Hevelius.interfaz.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import alma.TYPES.*;

public class Safety implements Runnable
{
	public void run(){
		while(true){
			try{
				//interfaz.getDrawingPanel().getCSATStatus().getSafety()

				if(interfaz.getDrawingPanel().getCSATStatus()!=null)
                                {
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
                                }
                                //Thread.sleep(300000);
				Thread.sleep(20000);


				
			}catch(InterruptedException e){
				System.out.println("Error en Thread de Safety Class");
			}
		}
	}

}
