package Hevelius.acsmodules;

import alma.ACS.*;

public class MovementCB extends alma.ACS.CBvoidPOA /*org.omg.CORBA.portable.ObjectImpl implements CBvoid*/{
	public void working(alma.ACSErr.Completion c, CBDescOut desc){
		System.out.println("A");
		//interfaz.getDrawingPanel().getCoordinatesPanel().setRa(-1.0f);
	}
	public void done(alma.ACSErr.Completion c, CBDescOut desc){
		System.out.println("B");
	}
	public boolean negotiate(long time, CBDescOut desc){
		System.out.println("C");
		return true;
	}
	public String[] _ids(){
		System.out.println("ALO");
		String[] a = {"0","1","2"};
		return a;
	}
}
