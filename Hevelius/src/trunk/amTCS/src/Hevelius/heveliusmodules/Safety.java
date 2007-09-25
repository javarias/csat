package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;

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
						interfaz.getDrawingPanel().getTelStatusPanel().setDangerState(interfaz.getDrawingPanel().getCSATStatus().getSafety());
                                        }
                                        catch(Exception e)
                                        {
                                                e.printStackTrace();
                                        }
                                }
                                Thread.sleep(300000);


				
			}catch(InterruptedException e){
				System.out.println("Error en Thread de Safety Class");
			}
		}
	}

}
