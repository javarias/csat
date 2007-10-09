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
				response =	getRaDec();
			}else if(firstChar == GetPreciseRADEC){
				response =	getPreciseRaDec();
			}else if(firstChar == GetAZMALT){
				response =	getAzmAlt();
			}else if(firstChar == GetPreciseAZMALT){
				response =	getPreciseAzmAlt();
			}else if(firstChar == GotoRADEC ){
				response =	gotoRA_DEC(message);
			}else if(firstChar == GotoPreciseRADEC ){
				response =	gotoPreciseRA_DEC(message);
			}else if(firstChar == GotoAZMALT ){
				response =	gotoAZM_ALT(message);
				
				System.out.println("Go to " + response);
			}else if(firstChar == GotoPreciseAZMALT ){
				response =	gotoPreciseAZM_ALT(message);
				
				System.out.println("Go to " + response);
			}else if(firstChar == SyncRADEC ){
				response = syncRA_DEC(message);
			}else if(firstChar == SyncPreciseRADEC ){
				response = syncPreciseRA_DEC(message);
			}else if(firstChar == GetTrackingMode ){
				response = getTrackingMode();
			}else if(firstChar == SetTrackingMode ){
				response = setTrackingMode(message.charAt(1));
			}else if(firstChar == PassThroughCommand ){
				response = handlePassThroughCommand(message);
			}else if(firstChar == GetLocation ){
				
			}else if(firstChar == SetLocation ){
			
			}else if(firstChar == GetTime ){
				
			}else if(firstChar == SetTime ){
			
			}else if(firstChar == GetVersion){
				response = getVersion();
			}else if(firstChar == GetModel){
				response = getModel();
			}else if(firstChar == Echo){
				response = echo(message);
			}else if(firstChar == IsAlignmentComplete){
				response = isAlignmentComplete();
			}else if(firstChar == IsGotoInProgress){
				response = isGotoInProgress();
			}else if(firstChar == CancelGoto){
				response = cancelGoto();
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
				
				return setVariableRate(message);
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
			
				return setFixedRate(message);
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
	
	/* **************************************/
	/* ********************Interface Comands*/
	
	/**
	 * @param trackingMode
	 */
	protected String setTrackingMode(char trackingMode) {
		telescope.setTrackingMode(trackingMode);
		return defaultResponse;
	}
	/**
	 * @return
	 */
	protected String isCalibrated() {
		String response;
		if(telescope.isCalibrated())
			response = Character.toString((char) 1) + defaultResponse;
		else
			response = Character.toString((char) 0) + defaultResponse;		
		return response;
	}
	/**
	 * @return
	 */
	protected String isGotoInProgress() {
		String response; 
		if(telescope.isGotoInProgress())
			response = "1" + defaultResponse;
		else
			response = "0" + defaultResponse;

		return response;
	}
	/**
	 * @return
	 */
	protected String isGpsLinked() {
		String response; 
		if(telescope.isGpsLinked())
			response = Character.toString((char) 1) + defaultResponse;
		else
			response = Character.toString((char) 0) + defaultResponse;

		return response;
	}
	/**
	 * @return
	 */
	protected String getModel() {
		return Character.toString(telescope.getModel()) +defaultResponse;
	}
	/**
	 * @return
	 */
	protected String getTrackingMode() {
		return Character.toString(telescope.getTrackingMode()) + defaultResponse;
	}
	/**
	 * @return
	 */
	protected String getVersion() {
		return	telescope.getVersion() + defaultResponse;
	}
	/**
	 * 
	 * @param message The character to echo
	 * @return The return string
	 */
	protected String echo(String message){
		return telescope.echo(message.charAt(1)) + defaultResponse;
	}
	/**
	 * 
	 * @return
	 */
	protected String getAzmAlt(){
		String response, preciseResponse;
		
		preciseResponse = getPreciseAzmAlt();
		response = 	preciseResponse.substring(0,  4) +","+ 
					preciseResponse.substring(9, 13) +"#";
		return response;
	}
	/**
	 * 
	 * @return
	 */
	protected String getPreciseAzmAlt(){
		String azm, alt, response;
		StringBuffer buffer = new StringBuffer(18);
		azm = Long.toHexString(telescope.getAzmAxis());
		for(int i = 0; i<8-azm.length(); i++)
			buffer.append('0');
		buffer.append(azm);
		buffer.append(',');
		alt = Long.toHexString(telescope.getAltAxis());
		for(int i = 0; i<8-alt.length(); i++)
			buffer.append('0');
		buffer.append(alt);
		buffer.append('#');
		response = buffer.toString().toUpperCase();
		return response;
	}
	/**
	 * 
	 * @return
	 */
	protected String getRaDec(){
		String response, preciseResponse;
		
		preciseResponse = getPreciseRaDec();
		response = 	preciseResponse.substring(0,  4) +","+ 
					preciseResponse.substring(9, 13) +"#";
		return response;
	}
	/**
	 * 
	 * @return
	 */
	protected String getPreciseRaDec(){
		String azm, alt, response;
		StringBuffer buffer = new StringBuffer(18);
		azm = Long.toHexString(telescope.getRa());
		for(int i = 0; i<8-azm.length(); i++)
			buffer.append('0');
		buffer.append(azm);
		buffer.append(',');
		alt = Long.toHexString(telescope.getDec());
		for(int i = 0; i<8-alt.length(); i++)
			buffer.append('0');
		buffer.append(alt);
		buffer.append('#');
		response = buffer.toString().toUpperCase();
		return response;
	}
	/**
	 * @return
	 */
	protected String isAlignmentComplete(){
		String response;
		if(telescope.isAlignmentComplete())
			response = Character.toString((char) 1) + defaultResponse;
		else
			response = Character.toString((char) 0) + defaultResponse;
		return response;
	}
	
	/**
	 * @return
	 */
	protected String getDeviceVersion(String message) {
		String response;
		response = telescope.getDeviceVersion(message.charAt(2)) + defaultResponse;
		return response;
	}
	protected String gotoRA_DEC(String message){
		String raString, decString;
		long ra, dec;
		raString  = message.substring(1,  5);
		decString = message.substring(6, 10);
		ra = Long.parseLong( raString + "0000", 16);
		dec= Long.parseLong(decString + "0000", 16);
		telescope.gotoRA_DEC(ra, dec);
		return defaultResponse;
	}
	protected String gotoPreciseRA_DEC(String message){
		String raString, decString;
		long ra, dec;
		raString  = message.substring( 1,  9);
		decString = message.substring(10, 18);
		ra = Long.parseLong( raString, 16);
		dec= Long.parseLong(decString, 16);
		telescope.gotoPreciseRA_DEC(ra, dec);
		return defaultResponse;
	}
	protected String gotoAZM_ALT(String message){
		String azmString, altString;
		long azm, alt;
		azmString = message.substring(1,  5);
		altString = message.substring(6, 10);
		azm = Long.parseLong(azmString + "0000", 16);
		alt = Long.parseLong(altString + "0000", 16);
		telescope.gotoAZM_ALT(azm, alt);
		return defaultResponse;
	}
	protected String gotoPreciseAZM_ALT(String message){
		String azmString, altString;
		long azm , alt;
		azmString = message.substring( 1,  9);
		altString = message.substring(10, 18);
		azm = Long.parseLong(azmString, 16);
		alt = Long.parseLong(altString, 16);
		telescope.gotoPreciseAZM_ALT(azm, alt);
		return defaultResponse;
	}
	protected String cancelGoto(){
		telescope.cancelGoto();
		return defaultResponse;
	}
	protected String setVariableRate(String message){
		
		boolean direction;
		int trackRateHigh, trackRateLow, rate;
		
		if((int) message.charAt(3) ==  6)
			direction = Nexstar4State.positiveDirection;
		else
			direction = Nexstar4State.negativeDirection;
		
		trackRateHigh = (int) message.charAt(4);
		trackRateLow = (int) message.charAt(5);
		rate = (trackRateHigh*256/4) + (int)(Math.floor(((double)trackRateLow)/4.0));
		
		System.out.println("Variable Slew Rate in AZM axis: "+rate);
		
		if((int) message.charAt(2) == 16)
			telescope.setVariableRateAZM_RA(rate, direction);
		else
			telescope.setVariableRateALT_DEC(rate, direction);
		return defaultResponse;
	}
	protected String setFixedRate(String message){
		boolean direction;
		char rate;
		
		if((int) message.charAt(3) == 36)
			direction = Nexstar4State.positiveDirection;
		else
			direction = Nexstar4State.negativeDirection;
		
		rate = message.charAt(4);
		
		if((int) message.charAt(2) == 16)
			telescope.setFixedRateAZM_RA(direction, rate);
		else
			telescope.setFixedRateALT_DEC(direction, rate);
		
		return defaultResponse;
	}
	protected String syncRA_DEC(String message){
		String raString, decString;
		long ra, dec;
		raString  = message.substring(1,  5);
		decString = message.substring(7, 10);
		ra = Long.parseLong( raString + "0000", 16);
		dec= Long.parseLong(decString + "0000", 16);
		telescope.syncRA_DEC(ra, dec);
		return defaultResponse;
	}
	protected String syncPreciseRA_DEC(String message){
		String raString, decString;
		long ra, dec;
		raString  = message.substring( 1,  9);
		decString = message.substring(10, 18);
		ra = Long.parseLong( raString, 16);
		dec= Long.parseLong(decString, 16);
		telescope.syncPreciseRA_DEC(ra, dec);
		return defaultResponse;
	}
}
