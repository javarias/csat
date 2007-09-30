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
		String response = "#";
		char firstChar;
		
		if(message.length()==0)
			return response;
		
		firstChar = message.charAt(0);
		if(firstChar == GetRADEC){
			
		}else if(firstChar == GetPreciseRADEC){
			telescope.getTrackingMode();
		}else if(firstChar == GetAZMALT){
			
		}else if(firstChar == GetPreciseAZMALT){
		
		}else if(firstChar == GotoRADEC ){
			
		}else if(firstChar == GotoPreciseRADEC ){
			
		}else if(firstChar == GotoAZMALT ){
			
		}else if(firstChar == GotoPreciseAZMALT ){
		
		}else if(firstChar == SyncRADEC ){
			
		}else if(firstChar == SyncPreciseRADEC ){
		
		}else if(firstChar == GetTrackingMode ){
			
		}else if(firstChar == SetTrackingMode ){
/*			if(firstChar == TrackingModeOff ){
				
			}else if(firstChar == TrackingModeAltAz ){
				
			}else if(firstChar == TrackingModeEQNorth ){
				
			}else if(firstChar == TrackingModeEQSouth ){
				
			}
*/		}else if(firstChar == PassThroughCommand ){
			response = handlePassThroughCommand(message);
		}else if(firstChar == GetLocation ){
			
		}else if(firstChar == SetLocation ){
		
		}else if(firstChar == GetTime ){
			
		}else if(firstChar == SetTime ){
		
		}else if(firstChar == GetVersion){
			
		}else if(firstChar == GetModel){
		
		}else if(firstChar == Echo){
		
		}else if(firstChar == IsAlignmentComplete){
			
		}else if(firstChar == IsGotoInProgress){
			
		}else if(firstChar == CancelGoto){
			
		}
		return response;
	}
	
	protected String handlePassThroughCommand(String message){
		String defaultResponse = "#";
		String badMessageResponse = "#";
		
		if(message.length() < 8)
			return defaultResponse;
		
		//***********************************
		// Set Variable Slew Rate OR Set Date on the CGE mount 
		if((int) message.charAt(1) == 3){
			if(	(	(int) message.charAt(2) == 16 || (int) message.charAt(2) == 17) && // Azm-RA || Alt-DEC
				(	(int) message.charAt(3) ==  6 || (int) message.charAt(3) ==  7) && // Positive || Negative  
					(int) message.charAt(6) ==  0 && 
					(int) message.charAt(7) ==  0){
				//TODO telescope.setVariableSlewRate
				return defaultResponse;
			}
			// Set Date or Year on the CGE mount
			else if((int) message.charAt(2) == 178 && 
				   ((int) message.charAt(3) == 131 || (int) message.charAt(3) == 132) && // Date || Year
					(int) message.charAt(6) ==   0 && 
					(int) message.charAt(7) ==   0){
				//TODO setCGEDate & setCGEYear
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
					//TODO telescope.setFixedSlewRate
					return defaultResponse;
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
					return "#";
				}
				// Get Latitude 
				else if((int) message.charAt(3) == 1 && 
						(int) message.charAt(4) == 0 && 
						(int) message.charAt(5) == 0 && 
						(int) message.charAt(6) == 0 && 
						(int) message.charAt(7) == 3){
						//TODO telescope.getLatitude
						return "#";
				}
				// Get Longitude 
				else if((int) message.charAt(3) == 2 && 
						(int) message.charAt(4) == 0 && 
						(int) message.charAt(5) == 0 && 
						(int) message.charAt(6) == 0 && 
						(int) message.charAt(7) == 3){
						//TODO telescope.getLongitude
						return "#";
				}
				// Get Date
				else if((int) message.charAt(3) == 3 && 
						(int) message.charAt(4) == 0 && 
						(int) message.charAt(5) == 0 && 
						(int) message.charAt(6) == 0 && 
						(int) message.charAt(7) == 2){
						//TODO telescope.getGPSDate
						return "#";
				}
				// Get Year
				else if((int) message.charAt(3) == 4 && 
						(int) message.charAt(4) == 0 && 
						(int) message.charAt(5) == 0 && 
						(int) message.charAt(6) == 0 && 
						(int) message.charAt(7) == 2){
						//TODO telescope.getGPSYear
						return "#";
				}
				// Get Time
				else if((int) message.charAt(3) == 51 && 
						(int) message.charAt(4) ==  0 && 
						(int) message.charAt(5) ==  0 && 
						(int) message.charAt(6) ==  0 && 
						(int) message.charAt(7) ==  3){
						//TODO telescope.getGPSTime
						return "#";
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
					return "#";
				}
				// Get Year
				else if((int) message.charAt(3) == 4 && 
						(int) message.charAt(4) == 0 && 
						(int) message.charAt(5) == 0 && 
						(int) message.charAt(6) == 0 && 
						(int) message.charAt(7) == 2){
						//TODO telescope.getYear
						return "#";
				}
				// Get Time
				else if((int) message.charAt(3) == 51 && 
						(int) message.charAt(4) ==  0 && 
						(int) message.charAt(5) ==  0 && 
						(int) message.charAt(6) ==  0 && 
						(int) message.charAt(7) ==  3){
						//TODO telescope.getTime
						return "#";
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
					return "#";
				}
			else{
				return badMessageResponse;
			}
		}
		return badMessageResponse;		
	}
}
