/**
 * 
 */
package cl.utfsm.acs.telescope.simulator.state;

import ec.util.MersenneTwisterFast;

/**
 * @author   dcontard
 */
public class Nexstar4PositionControl implements Runnable{
	public static final char slewSpeedSymbol_4DegreesPerSec = (char) 9;
	public static final char slewSpeedSymbol_2DegreesPerSec = (char) 8;
	public static final char slewSpeedSymbol_1DegreesPerSec = (char) 7;
	public static final char slewSpeedSymbol_5MinutessPerSec = (char) 6;
	
	public static final char slewSpeedSymbol_32x = (char) 5;
	public static final char slewSpeedSymbol_16x = (char) 4;
	public static final char slewSpeedSymbol_8x = (char) 3;
	public static final char slewSpeedSymbol_4x = (char) 2;
	public static final char slewSpeedSymbol_2x = (char) 1;
	public static final char slewSpeedSymbol_0x = (char) 0;
	
	public static final long slewSpeed_4DegreesPerSec =  Nexstar4State.maxAzmAxis * 4l / (60l);
	public static final long slewSpeed_2DegreesPerSec =  Nexstar4State.maxAzmAxis * 2l / (60l);
	public static final long slewSpeed_1DegreesPerSec =  Nexstar4State.maxAzmAxis * 1l / (60l);
	public static final long slewSpeed_5MinutessPerSec = Nexstar4State.maxAzmAxis * 5l / (60l * 60l);
	
	public static final long slewSpeed_32x = Nexstar4State.maxAzmAxis * 32l / (360l * 60l * 60l);
	public static final long slewSpeed_16x = Nexstar4State.maxAzmAxis * 16l / (360l * 60l * 60l);
	public static final long slewSpeed_8x =  Nexstar4State.maxAzmAxis *  8l / (360l * 60l * 60l);
	public static final long slewSpeed_4x =  Nexstar4State.maxAzmAxis *  4l / (360l * 60l * 60l);
	public static final long slewSpeed_2x =  Nexstar4State.maxAzmAxis *  2l / (360l * 60l * 60l);
	public static final long slewSpeed_0x =  Nexstar4State.maxAzmAxis *  0l / (360l * 60l * 60l);
	
	public static final long standardPrecision = 65536l;
	public static final long precisePrecision = 265l;
	
	public static final boolean positiveDirection = true;
	public static final boolean negativeDirection = false;
	
	public static final short action_noActionInProgress = 0;
	public static final short action_gotoInProgress = 1;
	public static final short action_slewingInProgress = 2;
	public static final short action_syncInProgress = 3;
	public static final short action_aligmentInProgress = 4;
	
	public static final char trackingMode_Off = (char) 0;
	public static final char trackingMode_AltAz = (char) 1;
	public static final char trackingMode_EQNorth = (char) 2;
	public static final char trackingMode_EQSouth = (char) 3;
	
	protected Nexstar4State telescope;
	
	protected short actionInProgress;
	protected char trackingMode;
	
	protected boolean gotoPrecise;
	protected boolean syncPrecise;
	protected boolean slewingInAzmAxis;
	protected boolean slewingInAltAxis;
	protected boolean variableSlewingInAzmAxis;
	protected boolean variableSlewingInAltAxis;
	
	protected long gotoAzmDestination;
	protected long gotoAltDestination;
	protected long syncRaAxis;
	protected long syncDecAxis;
	
	//slewSpeeds in arcseconds per second
	protected long slewRateInAzmAxis;
	protected long slewRateInAltAxis;
	protected long variableRateInAzmAxis;
	protected long variableRateInAltAxis;
	protected long totalRateInAzmAxis;
	protected long totalRateInAltAxis;
	protected int numberOfRatesInAzmAxis;
	protected int numberOfRatesInAltAxis;
	
	protected boolean noiseOn;
	protected MersenneTwisterFast prng;
	
	protected final long refreshRateInMilliseconds = 10;
	protected final int refreshRateInNanoseconds = 10;
	
	public Nexstar4PositionControl(Nexstar4State telescope) {
		super();
		this.telescope = telescope;
		
		actionInProgress = action_noActionInProgress;
		trackingMode = trackingMode_Off;
		
		gotoPrecise = false;
		syncPrecise = false;
		slewingInAzmAxis = false;
		slewingInAltAxis = false;
		variableSlewingInAzmAxis = false;
		variableSlewingInAltAxis = false;
		
		slewRateInAltAxis = 0;
		slewRateInAzmAxis = 0;
		variableRateInAzmAxis = 0;
		variableRateInAltAxis = 0;
		totalRateInAzmAxis = 0;
		totalRateInAltAxis = 0;
		
		gotoAzmDestination = telescope.getAzmAxis();
		gotoAltDestination = telescope.getAltAxis();
		syncRaAxis = 0l;
		syncDecAxis = 0l;
		
		noiseOn = true;
		prng = new MersenneTwisterFast();
	}
	public void gotoPreciseRA_DEC(long ra, long dec){
		//TODO: change ra-dec coordinates to azm-alt coordinates
		actionInProgress = action_noActionInProgress;
		
		gotoAzmDestination = ra  % Nexstar4State.maxAzmAxis;
		gotoAltDestination = dec % Nexstar4State.maxAltAxis;
		
		if(gotoAzmDestination < 0l)
			gotoAzmDestination = Nexstar4State.maxAzmAxis + gotoAzmDestination;
		if(gotoAltDestination < 0l)
			gotoAltDestination = Nexstar4State.maxAltAxis + gotoAltDestination;
		
		gotoPrecise = true;
		actionInProgress = action_gotoInProgress;
	}
	public void gotoPreciseAZM_ALT(long azm, long alt){
		actionInProgress = action_noActionInProgress;
		
		gotoAzmDestination = azm % Nexstar4State.maxAzmAxis;
		gotoAltDestination = alt % Nexstar4State.maxAltAxis;
		
		if(gotoAzmDestination < 0l)
			gotoAzmDestination = Nexstar4State.maxAzmAxis + gotoAzmDestination;
		if(gotoAltDestination < 0l)
			gotoAltDestination = Nexstar4State.maxAltAxis + gotoAltDestination;
		
		gotoPrecise = true;
		actionInProgress = action_gotoInProgress;
	}
	public void gotoRA_DEC(long ra, long dec){
		//TODO: change ra-dec coordinates to azm-alt coordinates
		actionInProgress = action_noActionInProgress;
		
		gotoAzmDestination = ra  % Nexstar4State.maxAzmAxis;
		gotoAltDestination = dec % Nexstar4State.maxAltAxis;
		
		if(gotoAzmDestination < 0l)
			gotoAzmDestination = Nexstar4State.maxAzmAxis + gotoAzmDestination;
		if(gotoAltDestination < 0l)
			gotoAltDestination = Nexstar4State.maxAltAxis + gotoAltDestination;
		
		gotoPrecise = false;
		actionInProgress = action_gotoInProgress;
	}
	public void gotoAZM_ALT(long azm, long alt){
		actionInProgress = action_noActionInProgress;
		
		gotoAzmDestination = azm % Nexstar4State.maxAzmAxis;
		gotoAltDestination = alt % Nexstar4State.maxAltAxis;
		
		if(gotoAzmDestination < 0l)
			gotoAzmDestination = Nexstar4State.maxAzmAxis + gotoAzmDestination;
		if(gotoAltDestination < 0l)
			gotoAltDestination = Nexstar4State.maxAltAxis + gotoAltDestination;
		
		gotoPrecise = false;
		actionInProgress = action_gotoInProgress;
	}
	public void cancelGoto(){
		if(actionInProgress == action_gotoInProgress){
			actionInProgress = action_noActionInProgress;
			gotoAzmDestination = telescope.getAzmAxis();
			gotoAltDestination = telescope.getAltAxis();
		}
	}
	public void syncRA_DEC(long ra, long dec){
		actionInProgress = action_noActionInProgress;
		syncRaAxis = ra;
		syncDecAxis = dec;
		syncPrecise = false;
		actionInProgress = action_syncInProgress;
	}
	public void syncPreciseRA_DEC(long ra, long dec){
		actionInProgress = action_noActionInProgress;
		syncRaAxis = ra;
		syncDecAxis = dec;
		syncPrecise = false;
		actionInProgress = action_syncInProgress;
	}
	public void variableRateAZM_RA(long arcsecondsPerSecond, boolean direction){
		long unitsPerSecond;
		
		if(actionInProgress != action_slewingInProgress)
			actionInProgress = action_noActionInProgress;
		
		slewingInAzmAxis = false;
		unitsPerSecond = Nexstar4State.maxAzmAxis * arcsecondsPerSecond / (360l * 60l * 60l);
		if(direction == negativeDirection)
			unitsPerSecond = (-1l)*unitsPerSecond;
		slewRateInAzmAxis = 0l;
		variableRateInAzmAxis = unitsPerSecond;
		totalRateInAzmAxis = 0l;
		numberOfRatesInAzmAxis = 0;
		variableSlewingInAzmAxis = true;
		slewingInAzmAxis = true;
		actionInProgress = action_slewingInProgress;
	}
	public void variableRateALT_DEC(long arcsecondsPerSecond, boolean direction){
		long unitsPerSecond;
		
		if(actionInProgress != action_slewingInProgress)
			actionInProgress = action_noActionInProgress;
		slewingInAltAxis = false;
		
		unitsPerSecond = Nexstar4State.maxAzmAxis * arcsecondsPerSecond / (60l);
		if(direction == negativeDirection)
			unitsPerSecond = (-1l)*unitsPerSecond;
		slewRateInAltAxis = 0l;
		variableRateInAltAxis = unitsPerSecond;
		totalRateInAltAxis = 0l;
		numberOfRatesInAltAxis = 0;
		variableSlewingInAltAxis = true;
		slewingInAltAxis = true;
		actionInProgress = action_slewingInProgress;
	}
	public void fixedRateAZM_RA(char slewSpeedSymbol, boolean direction){
		if(actionInProgress != action_slewingInProgress)
			actionInProgress = action_noActionInProgress;
		
		slewingInAzmAxis = false;
		variableRateInAzmAxis = 0l;
		totalRateInAzmAxis = 0l;
		
		switch (slewSpeedSymbol) {
		case slewSpeedSymbol_4DegreesPerSec:
			slewRateInAzmAxis = slewSpeed_4DegreesPerSec;
			break;

		case slewSpeedSymbol_2DegreesPerSec:
			slewRateInAzmAxis = slewSpeed_2DegreesPerSec;
			break;

		case slewSpeedSymbol_1DegreesPerSec:
			slewRateInAzmAxis = slewSpeed_1DegreesPerSec;
			break;

		case slewSpeedSymbol_5MinutessPerSec:
			slewRateInAzmAxis = slewSpeed_5MinutessPerSec;
			break;
			
		case slewSpeedSymbol_32x:
			slewRateInAzmAxis = slewSpeed_32x;
			break;
			
		case slewSpeedSymbol_16x:
			slewRateInAzmAxis = slewSpeed_16x;
			break;
			
		case slewSpeedSymbol_8x:
			slewRateInAzmAxis = slewSpeed_8x;
			break;
			
		case slewSpeedSymbol_4x:
			slewRateInAzmAxis = slewSpeed_4x;
			break;
			
		case slewSpeedSymbol_2x:
			slewRateInAzmAxis = slewSpeed_2x;
			break;
			
		default:
			slewRateInAzmAxis = slewSpeed_0x;
			break;
		}
		if(direction == negativeDirection)
			slewRateInAzmAxis = (-1l)*slewRateInAzmAxis;
		
		variableSlewingInAzmAxis = false;
		slewingInAzmAxis = true;
		actionInProgress = action_slewingInProgress;
	}
	public void fixedRateALT_DEC(char slewSpeedSymbol, boolean direction){
		if(actionInProgress != action_slewingInProgress)
			actionInProgress = action_noActionInProgress;
		
		slewingInAltAxis = false;
		variableRateInAltAxis = 0l;
		totalRateInAltAxis = 0l;
		
		System.out.println("slewSpeedSymbol:"+(int)slewSpeedSymbol);
		
		switch (slewSpeedSymbol) {
		case slewSpeedSymbol_4DegreesPerSec:
			slewRateInAltAxis = slewSpeed_4DegreesPerSec;
			break;

		case slewSpeedSymbol_2DegreesPerSec:
			slewRateInAltAxis = slewSpeed_2DegreesPerSec;
			break;

		case slewSpeedSymbol_1DegreesPerSec:
			slewRateInAltAxis = slewSpeed_1DegreesPerSec;
			break;

		case slewSpeedSymbol_5MinutessPerSec:
			slewRateInAltAxis = slewSpeed_5MinutessPerSec;
			break;
			
		case slewSpeedSymbol_32x:
			slewRateInAltAxis = slewSpeed_32x;
			break;
			
		case slewSpeedSymbol_16x:
			slewRateInAltAxis = slewSpeed_16x;
			break;
			
		case slewSpeedSymbol_8x:
			slewRateInAltAxis = slewSpeed_8x;
			break;
			
		case slewSpeedSymbol_4x:
			slewRateInAltAxis = slewSpeed_4x;
			break;
			
		case slewSpeedSymbol_2x:
			slewRateInAltAxis = slewSpeed_2x;
			break;
			
		default:
			slewRateInAltAxis = slewSpeed_0x;
			break;
		}
		if(direction == negativeDirection)
			slewRateInAltAxis = (-1l)*slewRateInAltAxis;
		
		variableSlewingInAltAxis = false;
		slewingInAltAxis = true;
		actionInProgress = action_slewingInProgress;
	}
	public void setTrackingMode(char trackingMode){
		if(		(trackingMode != trackingMode_AltAz)	&&
				(trackingMode != trackingMode_EQNorth)	&&
				(trackingMode != trackingMode_EQSouth)	&&
				(trackingMode != trackingMode_Off)		  )
			this.trackingMode  = trackingMode_Off;
		else
			this.trackingMode = trackingMode;
	}
	public char getTrackingMode(){
		return trackingMode;
	}
	public short getActionInProgress(){
		return actionInProgress;
	}
	public void run() {
		try {
			while(true){
				if(trackingMode != trackingMode_Off){
					//TODO: change position acording to tracking mode
				}
				
				switch (actionInProgress) {
				case action_noActionInProgress:
					break;
					
				case action_gotoInProgress:
					actionGotoInProgress();
					//System.out.println("Goto In Progress");
					break;
					
				case action_slewingInProgress:
					actionSlewingInProgress();
					break;
					
				case action_syncInProgress:
					break;
					
				case action_aligmentInProgress:
					break;
					
				default:
					break;
				}
				Thread.sleep(refreshRateInMilliseconds);
				continue;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void actionGotoInProgress(){
		short azmDirection, altDirection; //-1 or 1 value
		long 	azmTrace, altTrace, 
				azmChange, altChange, 
				noisyAzmChange, noisyAltChange,
				azmNewPosition, altNewPosition;
		double azmChangeD, altChangeD;
		
		azmDirection = determinateDirection(gotoAzmDestination,telescope.getAzmAxis(),Nexstar4State.maxAzmAxis);
		altDirection = determinateDirection(gotoAltDestination,telescope.getAltAxis(),Nexstar4State.maxAltAxis);
		
		azmTrace = determinateTrace(gotoAzmDestination,telescope.getAzmAxis(),Nexstar4State.maxAzmAxis);
		altTrace = determinateTrace(gotoAltDestination,telescope.getAltAxis(),Nexstar4State.maxAltAxis);
				
		azmChangeD = ((double)getProperSlewSpeedForTrace(azmTrace)) * ((double)azmDirection) * ((double)refreshRateInMilliseconds)/(1000.0);
		altChangeD = ((double)getProperSlewSpeedForTrace(altTrace)) * ((double)altDirection) * ((double)refreshRateInMilliseconds)/(1000.0);
				
		azmChange = (long) Math.ceil(azmChangeD);
		altChange = (long) Math.ceil(altChangeD);
				
		if(noiseOn){
			noisyAzmChange = azmChange + getNoise(azmChange, altChange, telescope.getAzmAxis(), telescope.getAltAxis());
			noisyAltChange = altChange + getNoise(altChange, azmChange, telescope.getAltAxis(), telescope.getAzmAxis());
		}
		else{
			noisyAzmChange = azmChange;
			noisyAltChange = altChange;
		}
		
		azmNewPosition = (telescope.getAzmAxis() + noisyAzmChange) % Nexstar4State.maxAzmAxis;
		altNewPosition = (telescope.getAltAxis() + noisyAltChange) % Nexstar4State.maxAltAxis;
		
		if(azmNewPosition < 0l)
			azmNewPosition = Nexstar4State.maxAzmAxis + azmNewPosition;
		if(altNewPosition < 0l)
			altNewPosition = Nexstar4State.maxAltAxis + altNewPosition;
		
		telescope.setAzmAxis(azmNewPosition);
		telescope.setAltAxis(altNewPosition);
		
		System.out.println("Direction in Azm: "+ azmDirection);
		System.out.println("Direction in Alt: "+ altDirection);
		System.out.println("Trace in Azm: "+ azmTrace);
		System.out.println("Trace in Alt: "+ altTrace);
		System.out.println("Exact Change in Azm: "+ azmChangeD);
		System.out.println("Exact Change in Alt: "+ altChangeD);
		System.out.println("Truncated Change in Azm: "+ azmChange);
		System.out.println("Truncated Change in Alt: "+ altChange);
		System.out.println("Noisy Change in Azm: "+ noisyAzmChange);
		System.out.println("Noisy Change in Alt: "+ noisyAltChange);
		System.out.println("Azm new position: "+ azmNewPosition);
		System.out.println("Alt new position: "+ altNewPosition);
		
		azmTrace = determinateTrace(gotoAzmDestination,telescope.getAzmAxis(),Nexstar4State.maxAzmAxis);
		altTrace = determinateTrace(gotoAltDestination,telescope.getAltAxis(),Nexstar4State.maxAltAxis);
		
		System.out.println("New Trace in Azm: "+ azmTrace);
		System.out.println("New Trace in Alt: "+ altTrace);
		
		if(	( gotoPrecise ) &&
			( azmTrace <= precisePrecision ) &&
			( altTrace <= precisePrecision )){
			actionInProgress = action_noActionInProgress;
			gotoAzmDestination = telescope.getAzmAxis();
			gotoAltDestination = telescope.getAltAxis();
//			System.out.println("Goto not in progress");
		} else if(	( !gotoPrecise ) &&
					( azmTrace <= standardPrecision ) &&
					( altTrace <= standardPrecision )){
			actionInProgress = action_noActionInProgress;
			gotoAzmDestination = telescope.getAzmAxis();
			gotoAltDestination = telescope.getAltAxis();
//			System.out.println("Goto not in progress");
		}
	}
	private long getNoise(long principalAxisChange, long secondaryAxisChange, long principalAxisPosition, long secondaryAxisPosition){
		long noise;
		
		noise =   Math.round(0.20 * prng.nextGaussian()*Math.abs(principalAxisChange)/((double) Nexstar4State.maxAzmAxis)) 
				+ Math.round(0.05 * prng.nextGaussian()*Math.abs(secondaryAxisChange)/((double) Nexstar4State.maxAltAxis));
		return noise;
	}
	private short determinateDirection(long destination, long actualPosition, long maxPosition){
		long positiveTrace, negativeTrace;
		
		if(Math.abs(destination) >= maxPosition)
			destination = destination % maxPosition;
		if(destination < 0l)
			destination = maxPosition + destination;
		
		if(actualPosition < destination){
			positiveTrace = destination - actualPosition;
			negativeTrace = actualPosition + maxPosition - destination;
		}
		else{
			negativeTrace = actualPosition - destination;
			positiveTrace = destination + maxPosition - actualPosition;
		}
		if(positiveTrace < negativeTrace)
			return 1;
		return -1;
	}
	private long determinateTrace(long destination, long actualPosition, long maxPosition){
		long positiveTrace, negativeTrace;
		
		if(Math.abs(destination) >= maxPosition)
			destination = destination % maxPosition;
		if(destination < 0l)
			destination = maxPosition + destination;
		
		if(actualPosition < destination){
			positiveTrace = destination - actualPosition;
			negativeTrace = actualPosition + maxPosition - destination;
		}
		else{
			negativeTrace = actualPosition - destination;
			positiveTrace = destination + maxPosition - actualPosition;
		}
		if(positiveTrace < negativeTrace)
			return positiveTrace;
		return negativeTrace;
	}	
	private long getProperSlewSpeedForTrace(long trace){
		long deltaTrace;
		
		deltaTrace = (long) ( 1.25 * ((double) trace) *1000.0/((double) refreshRateInMilliseconds) );
		
		if(deltaTrace > slewSpeed_4DegreesPerSec){
			return slewSpeed_4DegreesPerSec;
		}else if(deltaTrace > slewSpeed_2DegreesPerSec){
			return slewSpeed_2DegreesPerSec;
		}else if(deltaTrace > slewSpeed_1DegreesPerSec){
			return slewSpeed_1DegreesPerSec;
		}else if(deltaTrace > slewSpeed_5MinutessPerSec){
			return slewSpeed_5MinutessPerSec;
		}else if(deltaTrace > slewSpeed_32x){
			return slewSpeed_32x;
		}else if(deltaTrace > slewSpeed_16x){
			return slewSpeed_16x;
		}else if(deltaTrace > slewSpeed_8x){
			return slewSpeed_8x;
		}else if(deltaTrace > slewSpeed_4x){
			return slewSpeed_4x;
		}else if(deltaTrace > slewSpeed_2x){
			return slewSpeed_2x;
		}
		return slewSpeed_0x;
	}
	protected void actionSlewingInProgress(){
		long 	azmChange, altChange, 
				noisyAzmChange, noisyAltChange,
				azmNewPosition, altNewPosition;
		
		azmChange = 0;
		altChange = 0;
		if(slewingInAzmAxis){
			if(!variableSlewingInAzmAxis){
				azmChange = (long) Math.ceil( ((double) slewRateInAzmAxis) * ((double)refreshRateInMilliseconds)/(1000.0) );
			} else {
				azmChange = (long) Math.ceil( ((double) getFixedSlewRateForVariableSlewRate(variableRateInAzmAxis, ((double)totalRateInAzmAxis)/((double)numberOfRatesInAzmAxis))) * ((double)refreshRateInMilliseconds)/(1000.0) );
				numberOfRatesInAzmAxis++;
			}
		}
		if(slewingInAltAxis){
			if(!variableSlewingInAltAxis){
				altChange = (long) Math.ceil( ((double)slewRateInAltAxis) * ((double)refreshRateInMilliseconds)/(1000.0) );
			} else {
				altChange = (long) Math.ceil( ((double) getFixedSlewRateForVariableSlewRate(variableRateInAltAxis, ((double)totalRateInAltAxis)/((double)numberOfRatesInAzmAxis))) * ((double)refreshRateInMilliseconds)/(1000.0) );
				numberOfRatesInAltAxis++;
			}
		}
		
		if(noiseOn){
			noisyAzmChange = azmChange + getNoise(azmChange, altChange, telescope.getAzmAxis(), telescope.getAltAxis());
			noisyAltChange = altChange + getNoise(altChange, azmChange, telescope.getAltAxis(), telescope.getAzmAxis());
		}
		else{
			noisyAzmChange = azmChange;
			noisyAltChange = altChange;
		}
		
		azmNewPosition = (telescope.getAzmAxis() + noisyAzmChange) % Nexstar4State.maxAzmAxis;
		altNewPosition = (telescope.getAltAxis() + noisyAltChange) % Nexstar4State.maxAltAxis;
		
		if(azmNewPosition < 0l)
			azmNewPosition = Nexstar4State.maxAzmAxis + azmNewPosition;
		if(altNewPosition < 0l)
			altNewPosition = Nexstar4State.maxAltAxis + altNewPosition;
		
		telescope.setAzmAxis(azmNewPosition);
		telescope.setAltAxis(altNewPosition);
		
		if(variableSlewingInAzmAxis)
			totalRateInAzmAxis += Math.abs(noisyAzmChange);
		if(variableSlewingInAltAxis)
			totalRateInAltAxis += Math.abs(noisyAltChange);
		
		//TODO: Reset tracking mode under certain slew speeds
		
		if (!slewingInAzmAxis && !slewingInAltAxis){
			actionInProgress = action_noActionInProgress;
//			System.out.println("A: Slewing not in progress");
		}else if(	
				(( variableSlewingInAzmAxis && variableRateInAzmAxis == 0l) || 
				 (!variableSlewingInAzmAxis && slewRateInAzmAxis 	 == 0l)	  )	
			 								&&
			 	(( variableSlewingInAltAxis && variableRateInAltAxis == 0l) || 
			 	 (!variableSlewingInAltAxis && slewRateInAltAxis 	 == 0l)	  )  
		   	   )
		{
			   	   actionInProgress = action_noActionInProgress;
/*			   	   System.out.println("B: Slewing not in progress");
			   	   System.out.println("variableSlewingInAzmAxis:"+variableSlewingInAzmAxis+" variableRateInAzmAxis:"+variableRateInAzmAxis+" slewRateInAzmAxis:"+slewRateInAzmAxis);
			   	   System.out.println("variableSlewingInAltAxis:"+variableSlewingInAltAxis+" variableRateInAltAxis:"+variableRateInAltAxis+" slewRateInAltAxis:"+slewRateInAltAxis);
*/		}
	}
	private long getFixedSlewRateForVariableSlewRate(long variableSlewRate, double meanSlewRate){
		long variableSlewRateAbs, upperSlewRate, lowerSlewRate, upperDistance, lowerDistance;
		
		variableSlewRateAbs = Math.abs(variableSlewRate);
		
		upperSlewRate = getUpperSlewRate(variableSlewRateAbs);
		lowerSlewRate = getLowerSlewRate(variableSlewRateAbs);
		upperDistance = Math.round( Math.abs((double)upperSlewRate - meanSlewRate) );
		lowerDistance = Math.round( Math.abs((double)lowerSlewRate - meanSlewRate) );
		if(lowerDistance < upperDistance)
			return lowerSlewRate* ( (long) Math.signum(variableSlewRate) );
		return upperSlewRate*( (long) Math.signum(variableSlewRate) );
	}
	private long getLowerSlewRate(long variableSlewRateAbs){
		if(variableSlewRateAbs > slewSpeed_0x){
			return slewSpeed_0x;
		}else if(variableSlewRateAbs > slewSpeed_2x){
			return slewSpeed_2x;
		}else if(variableSlewRateAbs > slewSpeed_4x){
			return slewSpeed_4x;
		}else if(variableSlewRateAbs > slewSpeed_8x){
			return slewSpeed_8x;
		}else if(variableSlewRateAbs > slewSpeed_16x){
			return slewSpeed_16x;
		}else if(variableSlewRateAbs > slewSpeed_32x){
			return slewSpeed_32x;
		}else if(variableSlewRateAbs > slewSpeed_5MinutessPerSec){
			return slewSpeed_5MinutessPerSec;
		}else if(variableSlewRateAbs > slewSpeed_1DegreesPerSec){
			return slewSpeed_1DegreesPerSec;
		}else if(variableSlewRateAbs > slewSpeed_2DegreesPerSec){
			return slewSpeed_2DegreesPerSec;
		}
		return slewSpeed_4DegreesPerSec;
	}
	private long getUpperSlewRate(long variableSlewRateAbs){
		if(variableSlewRateAbs <= slewSpeed_4DegreesPerSec){
			return slewSpeed_4DegreesPerSec;
		}else if(variableSlewRateAbs <= slewSpeed_2DegreesPerSec){
			return slewSpeed_2DegreesPerSec;
		}else if(variableSlewRateAbs <= slewSpeed_1DegreesPerSec){
			return slewSpeed_1DegreesPerSec;
		}else if(variableSlewRateAbs <= slewSpeed_5MinutessPerSec){
			return slewSpeed_5MinutessPerSec;
		}else if(variableSlewRateAbs <= slewSpeed_32x){
			return slewSpeed_32x;
		}else if(variableSlewRateAbs <= slewSpeed_16x){
			return slewSpeed_16x;
		}else if(variableSlewRateAbs <= slewSpeed_8x){
			return slewSpeed_8x;
		}else if(variableSlewRateAbs <= slewSpeed_4x){
			return slewSpeed_4x;
		}else if(variableSlewRateAbs <= slewSpeed_2x){
			return slewSpeed_2x;
		}
		return slewSpeed_0x;
	}
	protected void actionSyncInProgress(){
		
	}

	protected void actionAligmentInProgress(){
		
	}
}
