package cl.utfsm.acs.telescope.simulator.comm;

import cl.utfsm.acs.telescope.simulator.state.Nexstar4State;

/**
 * @author dcontard
 *
 */
public class Nexstar4MessageWrapper {
	public static final char GetRADEC = 'E';
	public static final char GetPreciseRADEC = 'e';
	public static final char GetAZMALT = 'Z';
	public static final char GetPreciseAZMALT = 'z';
	
	public static final char GotoRADEC = 'R';
	public static final char GotoPreciseRADEC = 'r';
	public static final char GotoAZMALT = 'B';
	public static final char GotoPreciseAZMALT = 'b';
	
	public static final char SyncRADEC = 'S';
	public static final char SyncPreciseRADEC = 's';
	
	public static final char GetTrackingMode = 't';
	public static final char SetTrackingMode = 'T';
		
	public static final char PassThroughCommand = 'P';
	
	public static final char GetLocation = 'w';
	public static final char SetLocation = 'W';
	
	public static final char GetTime = 'h';
	public static final char SetTime = 'H';
	
	public static final char GetVersion = 'v';
	public static final char GetModel = 'm';
	
	public static final char Echo = 'K';
	
	public static final char IsAlignmentComplete = 'J';
	public static final char IsGotoInProgress = 'L';
	public static final char CancelGoto = 'M';
	
	protected static final String defaultResponse = "#";
	protected static final String badMessageResponse = "#";
	
	protected Nexstar4State telescope;
	
	public Nexstar4MessageWrapper() {
		super();
		telescope = new Nexstar4State();
	}
	int expectedMessageLength(String partialMessage){
		char firstChar;
		
		if (partialMessage.length() == 0)
			return -1; //no message to identify
		
		firstChar = partialMessage.charAt(0);
		
		if(firstChar == GetRADEC){
			return 1;
		}else if(firstChar == GetPreciseRADEC){
			return 1;
		}else if(firstChar == GetAZMALT){
			return 1;
		}else if(firstChar == GetPreciseAZMALT){
			return 1;
		}else if(firstChar == GotoRADEC ){
			return 10;
		}else if(firstChar == GotoPreciseRADEC ){
			return 18;
		}else if(firstChar == GotoAZMALT ){
			return 10;
		}else if(firstChar == GotoPreciseAZMALT ){
			return 18;
		}else if(firstChar == SyncRADEC ){
			return 10;
		}else if(firstChar == SyncPreciseRADEC ){
			return 18;
		}else if(firstChar == GetTrackingMode ){
			return 1;
		}else if(firstChar == SetTrackingMode ){
			return 2;
		}else if(firstChar == PassThroughCommand ){
			return 8;
		}else if(firstChar == GetLocation ){
			return 1;
		}else if(firstChar == SetLocation ){
			return 9;
		}else if(firstChar == GetTime ){
			return 1;
		}else if(firstChar == SetTime ){
			return 9;
		}else if(firstChar == GetVersion){
			return 1;
		}else if(firstChar == GetModel){
			return 1;
		}else if(firstChar == Echo){
			return 2;
		}else if(firstChar == IsAlignmentComplete){
			return 1;
		}else if(firstChar == IsGotoInProgress){
			return 1;
		}else if(firstChar == CancelGoto){
			return 1;
		}
		return -2; //not recognized message
	}
	public String executeAction(String message){
		String response = defaultResponse;
		try{
			char firstChar;
			
			if(message.length()==0)
				return response;
			
			firstChar = message.charAt(0);
			if(firstChar == GetRADEC){
				response =	telescope.getRaDec();
			}else if(firstChar == GetPreciseRADEC){
				response =	telescope.getPreciseRaDec();
			}else if(firstChar == GetAZMALT){
				response =	telescope.getAzmAlt();
			}else if(firstChar == GetPreciseAZMALT){
				response =	telescope.getPreciseAzmAlt();
			}else if(firstChar == GotoRADEC ){
				String ra, dec;
				long ral, decl;
				ra  = message.substring(1,  5);
				dec = message.substring(6, 10);
				ral = Long.parseLong( ra + "0000", 16);
				decl= Long.parseLong(dec + "0000", 16);
				response =	telescope.gotoRA_DEC(ral,decl);
			}else if(firstChar == GotoPreciseRADEC ){
				String ra, dec;
				long ral, decl;
				ra  = message.substring( 1,  9);
				dec = message.substring(10, 18);
				ral = Long.parseLong( ra, 16);
				decl= Long.parseLong(dec, 16);
				response =	telescope.gotoPreciseRA_DEC(ral,decl);
			}else if(firstChar == GotoAZMALT ){
				String azm, alt;
				long azml, altl;
				azm = message.substring(1,  5);
				alt = message.substring(6, 10);
				azml= Long.parseLong(azm + "0000", 16);
				altl= Long.parseLong(alt + "0000", 16);
				response =	telescope.gotoAZM_ALT(azml, altl);
				
				System.out.println("Go to "+azml+","+altl);
			}else if(firstChar == GotoPreciseAZMALT ){
				String azm, alt;
				long azml, altl;
				azm = message.substring( 1,  9);
				alt = message.substring(10, 18);
				azml= Long.parseLong(azm, 16);
				altl= Long.parseLong(alt, 16);
				response =	telescope.gotoPreciseAZM_ALT(azml, altl);
				
				System.out.println("Go to "+azml+","+altl);
			}else if(firstChar == SyncRADEC ){
				String ra, dec;
				long ral, decl;
				ra  = message.substring(1,  5);
				dec = message.substring(7, 10);
				ral = Long.parseLong( ra + "0000", 16);
				decl= Long.parseLong(dec + "0000", 16);
				response = telescope.syncRA_DEC(ral, decl);
			}else if(firstChar == SyncPreciseRADEC ){
				String ra, dec;
				long ral, decl;
				ra  = message.substring( 1,  9);
				dec = message.substring(10, 18);
				ral = Long.parseLong( ra, 16);
				decl= Long.parseLong(dec, 16);
				response = telescope.syncPreciseRA_DEC(ral, decl);
			}else if(firstChar == GetTrackingMode ){
				response = telescope.getTrackingMode();
			}else if(firstChar == SetTrackingMode ){
				response = telescope.setTrackingMode(message.charAt(1));
			}else if(firstChar == PassThroughCommand ){
				response = handlePassThroughCommand(message);
			}else if(firstChar == GetLocation ){
				
			}else if(firstChar == SetLocation ){
			
			}else if(firstChar == GetTime ){
				
			}else if(firstChar == SetTime ){
			
			}else if(firstChar == GetVersion){
				response = telescope.getVersion();
			}else if(firstChar == GetModel){
				response = telescope.getModel();
			}else if(firstChar == Echo){
				response = telescope.echo(message.charAt(1));
			}else if(firstChar == IsAlignmentComplete){
				response = telescope.isAlignmentComplete();
			}else if(firstChar == IsGotoInProgress){
				response = telescope.isGotoInProgress();
			}else if(firstChar == CancelGoto){
				response = telescope.cancelGoto();
			}
		}catch (NumberFormatException e) {
			// bad hexadecimal format in goto or sync call
		}
		return response;
	}
	
	protected String handlePassThroughCommand(String message){
		
		if(message.length() < 8)
			return defaultResponse;
		
		//***********************************
		// Set Variable Slew Rate OR Set Date on the CGE mount 
		if((int) message.charAt(1) == 3){
			if(	(	(int) message.charAt(2) == 16 || (int) message.charAt(2) == 17) && // Azm-RA || Alt-DEC
				(	(int) message.charAt(3) ==  6 || (int) message.charAt(3) ==  7) && // Positive || Negative  
					(int) message.charAt(6) ==  0 && 
					(int) message.charAt(7) ==  0){
				
				boolean direction;
				int trackRateHigh, trackRateLow, rate;
				
				if((int) message.charAt(3) ==  6)
					direction = Nexstar4State.positiveDirection;
				else
					direction = Nexstar4State.negativeDirection;
				
				trackRateHigh = (int) message.charAt(4);
				trackRateLow = (int) message.charAt(5);
				rate = (trackRateHigh*256/4) + (int)(Math.floor(((double)trackRateLow)/4.0));
				
				if((int) message.charAt(2) == 16)
					return telescope.setVariableRateAZM_RA(rate, direction);
				else
					return telescope.setVariableRateALT_DEC(rate, direction);
			}
			// Set Date or Year on the CGE mount
			else if((int) message.charAt(2) == 178 && 
				   ((int) message.charAt(3) == 131 || (int) message.charAt(3) == 132) && // Date || Year
					(int) message.charAt(6) ==   0 && 
					(int) message.charAt(7) ==   0){
				//TODO setCGEDate & setCGEYear
				return defaultResponse;
			}
			else{
				return badMessageResponse;
			}
		}
		//***********************************
		// Set Fixed Slew Rate
		else if((int) message.charAt(1) == 2){
			if(	(	(int) message.charAt(2) == 16 || (int) message.charAt(2) == 17) && // Azm-RA || Alt-DEC
				(	(int) message.charAt(3) == 36 || (int) message.charAt(3) == 37) && // Positive || Negative  
					(int) message.charAt(5) == 0 && 
					(int) message.charAt(6) == 0 && 
					(int) message.charAt(7) == 0){
			
				boolean direction;
				char rate;
				
				if((int) message.charAt(3) == 36)
					direction = Nexstar4State.positiveDirection;
				else
					direction = Nexstar4State.negativeDirection;
				
				rate = message.charAt(4);
				
				if((int) message.charAt(2) == 16)
					return telescope.setFixedRateAZM_RA(direction, rate);
				else
					return telescope.setFixedRateALT_DEC(direction, rate);
			}
			// Bad Message
			else{
				return badMessageResponse;
			}
		}
		//***********************************
		// Set Time on the CGE mount
		else if((int) message.charAt(1) == 4){
			if(		(int) message.charAt(2) == 178 && 
					(int) message.charAt(3) == 132 && 
					(int) message.charAt(6) ==   0 && 
					(int) message.charAt(7) ==   0){
					//TODO telescope.setCGETime
					return defaultResponse;
			}
			// Bad Message
			else{
				return badMessageResponse;
			}
		}
		//***********************************
		else if((int) message.charAt(1) == 1){
			//***********************************
			// GPS Commands
			if((int) message.charAt(2) == 176){
				// Is GPS Linked?
				if(	(int) message.charAt(3) == 55 && 
					(int) message.charAt(4) ==  0 && 
					(int) message.charAt(5) ==  0 && 
					(int) message.charAt(6) ==  0 && 
					(int) message.charAt(7) ==  1){
					//TODO telescope.isGPSLinked
					return defaultResponse;
				}
				// Get Latitude 
				else if((int) message.charAt(3) == 1 && 
						(int) message.charAt(4) == 0 && 
						(int) message.charAt(5) == 0 && 
						(int) message.charAt(6) == 0 && 
						(int) message.charAt(7) == 3){
						//TODO telescope.getLatitude
						return defaultResponse;
				}
				// Get Longitude 
				else if((int) message.charAt(3) == 2 && 
						(int) message.charAt(4) == 0 && 
						(int) message.charAt(5) == 0 && 
						(int) message.charAt(6) == 0 && 
						(int) message.charAt(7) == 3){
						//TODO telescope.getLongitude
						return defaultResponse;
				}
				// Get Date
				else if((int) message.charAt(3) == 3 && 
						(int) message.charAt(4) == 0 && 
						(int) message.charAt(5) == 0 && 
						(int) message.charAt(6) == 0 && 
						(int) message.charAt(7) == 2){
						//TODO telescope.getGPSDate
						return defaultResponse;
				}
				// Get Year
				else if((int) message.charAt(3) == 4 && 
						(int) message.charAt(4) == 0 && 
						(int) message.charAt(5) == 0 && 
						(int) message.charAt(6) == 0 && 
						(int) message.charAt(7) == 2){
						//TODO telescope.getGPSYear
						return defaultResponse;
				}
				// Get Time
				else if((int) message.charAt(3) == 51 && 
						(int) message.charAt(4) ==  0 && 
						(int) message.charAt(5) ==  0 && 
						(int) message.charAt(6) ==  0 && 
						(int) message.charAt(7) ==  3){
						//TODO telescope.getGPSTime
						return defaultResponse;
				}
				else{
					return badMessageResponse;
				}
			}
			//***********************************
			// RTC commands on the CGE mount
			if((int) message.charAt(2) == 178){
				// Get Date
				if(	(int) message.charAt(3) == 3 && 
					(int) message.charAt(4) == 0 && 
					(int) message.charAt(5) == 0 && 
					(int) message.charAt(6) == 0 && 
					(int) message.charAt(7) == 2){
					//TODO telescope.getDate
					return defaultResponse;
				}
				// Get Year
				else if((int) message.charAt(3) == 4 && 
						(int) message.charAt(4) == 0 && 
						(int) message.charAt(5) == 0 && 
						(int) message.charAt(6) == 0 && 
						(int) message.charAt(7) == 2){
						//TODO telescope.getYear
						return defaultResponse;
				}
				// Get Time
				else if((int) message.charAt(3) == 51 && 
						(int) message.charAt(4) ==  0 && 
						(int) message.charAt(5) ==  0 && 
						(int) message.charAt(6) ==  0 && 
						(int) message.charAt(7) ==  3){
						//TODO telescope.getTime
						return defaultResponse;
				}
				else{
					return badMessageResponse;
				}
			}
			//***********************************
			// Get Device Version			
			if((	(int) message.charAt(2) == 16  || //of the AZM/RA Motor
					(int) message.charAt(2) == 17  || //of the ALT/DEC Motor
					(int) message.charAt(2) == 176 || //of the GPS Unit
					(int) message.charAt(2) == 178)&& //of the RTC (CGE only)
					(int) message.charAt(3) == 254 && 
					(int) message.charAt(4) ==   0 && 
					(int) message.charAt(5) ==   0 && 
					(int) message.charAt(6) ==   0 && 
					(int) message.charAt(7) ==   2){
					//TODO telescope.getDeviceVersion
					return defaultResponse;
				}
			else{
				return badMessageResponse;
			}
		}
		return badMessageResponse;		
	}
}
