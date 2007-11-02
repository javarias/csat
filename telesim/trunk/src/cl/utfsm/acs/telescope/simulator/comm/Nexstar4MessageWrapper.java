package cl.utfsm.acs.telescope.simulator.comm;

import cl.utfsm.acs.telescope.simulator.state.Nexstar4State;

/**
 * <code>Nexstar4MessageWrapper</code> recives messages
 * from a stream listener, every message wrapped in a string, 
 * identifies the message type and the parameters values that 
 * are contained in the string, and delivers the proper actions 
 * to be taked by a <code>{@link Nexstar4State}</code>
 * 
 * @author dcontard
 *
 */
public class Nexstar4MessageWrapper {
	protected static final char GetRADEC = 'E';
	protected static final char GetPreciseRADEC = 'e';
	protected static final char GetAZMALT = 'Z';
	protected static final char GetPreciseAZMALT = 'z';
	
	protected static final char GotoRADEC = 'R';
	protected static final char GotoPreciseRADEC = 'r';
	protected static final char GotoAZMALT = 'B';
	protected static final char GotoPreciseAZMALT = 'b';
	
	protected static final char SyncRADEC = 'S';
	protected static final char SyncPreciseRADEC = 's';
	
	protected static final char GetTrackingMode = 't';
	protected static final char SetTrackingMode = 'T';
		
	protected static final char PassThroughCommand = 'P';
	
	protected static final char GetLocation = 'w';
	protected static final char SetLocation = 'W';
	
	protected static final char GetTime = 'h';
	protected static final char SetTime = 'H';
	
	protected static final char GetVersion = 'v';
	protected static final char GetModel = 'm';
	
	protected static final char Echo = 'K';
	
	protected static final char IsAlignmentComplete = 'J';
	protected static final char IsGotoInProgress = 'L';
	protected static final char CancelGoto = 'M';
	
	protected static final String defaultResponse = "#";
	protected static final String badMessageResponse = "#";
	
	protected Nexstar4State telescope;
	
	public Nexstar4MessageWrapper() {
		super();
		telescope = new Nexstar4State();
	}
	/**
	 * Tries to identifie the type of a message by knowing
	 * only a part of it, and returns the expected length
	 * that should have the complete message.
	 * <p>
	 * It is important to enphatize that the partial part of 
	 * the message must be a substring starting at the beginning 
	 * of the original message. If the provided string is a 
	 * substring of the middle or the tail of the message, the
	 * message won't be properly identified, and wrong message
	 * length could be returned.
	 * <p>
	 * Usually, only the first character of the message is
	 * needed to identify its type, but if there are available 
	 * more than one character, the more complete the message
	 * string is, the better.
	 * <p>
	 * This method is realy useful for stream listeners to know
	 * how many characters they need to read before delivering
	 * the complete message to the {@link Nexstar4MessageWrapper}.
	 * 
	 * @param partialMessage	the string containing a initial substring
	 * 							of the message that needs to be identified
	 * @return					if the partial message is identified, its 
	 * 							expected length is returned as an integer;
	 * 							if the <code>partialMessage</code> parameter 
	 * 							is an empty	string or a <code>null</code> 
	 * 							object, -1 is returned; if the message is 
	 * 							not identified, -2 is returned
	 */
	public int expectedMessageLength(String partialMessage){
		char firstChar;
		
		if (partialMessage == null || partialMessage.length() == 0)
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
	/**
	 * Excecutes a message codified in a <code>{@link String}</code> object, according to
	 * Nexstar SE Communication Protocol specifications.
	 * 
	 * @param message	message that contains the action to be excecuted
	 * @return			a <code>{@link String}</code> object containing 
	 * 					the proper response for the action excecuted, 
	 * 					according to Nexstar SE Communication Protocol 
	 * 					specifications
	 */
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
				response = getLocation(message);
			}else if(firstChar == SetLocation ){
				response = setLocation(message);
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
					
					return isGPSLinked();
				}
				// Get Latitude 
				else if((int) message.charAt(3) == 1 && 
						(int) message.charAt(4) == 0 && 
						(int) message.charAt(5) == 0 && 
						(int) message.charAt(6) == 0 && 
						(int) message.charAt(7) == 3){
						
						return getLatitude();
				}
				// Get Longitude 
				else if((int) message.charAt(3) == 2 && 
						(int) message.charAt(4) == 0 && 
						(int) message.charAt(5) == 0 && 
						(int) message.charAt(6) == 0 && 
						(int) message.charAt(7) == 3){
						
						return getLongitude();
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
	protected String getLatitude() {
		long lat65536, lat256, lat;
		double latitude;
		StringBuffer buffer;
		if(telescope.isWest()){
			latitude = 	    (double)(90 - telescope.getDegreesOfLatitude())
						+ ( (double)(60 - telescope.getMinutesOfLatitude()) )/(60.0) 
						+ ( (double)(60 - telescope.getSecondsOfLatitude()) )/(60.0*60.0);
		} else {
			latitude = 	    (double)(90 + telescope.getDegreesOfLatitude())
						+ ( (double) 	  telescope.getMinutesOfLatitude()  )/(60.0) 
						+ ( (double)	  telescope.getSecondsOfLatitude()  )/(60.0*60.0);
		}
		latitude = latitude*Math.pow(2, 24);
		latitude = latitude/180.0;
		
		lat65536 =  ((long)latitude)/65536;
		lat256	 = (((long)latitude)%65536)/256;
		lat		 = (((long)latitude)%65536)%256;
		
		buffer = new StringBuffer(4);
		buffer.append((char)lat65536);
		buffer.append((char)lat256);
		buffer.append((char)lat);
		buffer.append(defaultResponse);
		
		return buffer.toString();
	}
	protected String getLongitude() {
		long longitude65536, longitude256, longitude1;
		double longitude;
		StringBuffer buffer;
		if(telescope.isSouth()){
			longitude   = 	(double)(180 - telescope.getDegreesOfLatitude())
						+ ( (double)( 60 - telescope.getMinutesOfLatitude()) )/(60.0) 
						+ ( (double)( 60 - telescope.getSecondsOfLatitude()) )/(60.0*60.0);
		} else {
			longitude   = 	(double)(180 + telescope.getDegreesOfLatitude())
						+ ( (double) 	   telescope.getMinutesOfLatitude()  )/(60.0) 
						+ ( (double)	   telescope.getSecondsOfLatitude()  )/(60.0*60.0);
		}
		longitude = longitude*Math.pow(2, 24);
		longitude = longitude/360.0;
		
		longitude65536 =  ((long)longitude)/65536;
		longitude256	 = (((long)longitude)%65536)/256;
		longitude1		 = (((long)longitude)%65536)%256;
		
		buffer = new StringBuffer(4);
		buffer.append((char)longitude65536);
		buffer.append((char)longitude256);
		buffer.append((char)longitude1);
		buffer.append(defaultResponse);
		
		return buffer.toString();
	}
	/* **************************************/
	/* ********************Interface Comands*/
	protected String setLocation(String message) {
		boolean south, west;
		
		if(message.charAt(4) > 0)
			south = true;
		else
			south = false;
		if(message.charAt(8) > 0)
			west = true;
		else
			west = false;
		
		telescope.setLocation((int)message.charAt(1), (int)message.charAt(2), (int)message.charAt(3), south, (int)message.charAt(5), (int)message.charAt(6), (int)message.charAt(7), west);
		return defaultResponse;
	}
	protected String getLocation(String message) {
		StringBuffer buffer = new StringBuffer(9);
		buffer.append((char)telescope.getDegreesOfLatitude());
		buffer.append((char)telescope.getMinutesOfLatitude());
		buffer.append((char)telescope.getSecondsOfLatitude());
		if(telescope.isSouth())
			buffer.append((char) 1);
		else
			buffer.append((char) 0);
		buffer.append((char)telescope.getDegreesOfLongitude());
		buffer.append((char)telescope.getMinutesOfLongitude());
		buffer.append((char)telescope.getSecondsOfLongitude());
		if(telescope.isWest())
			buffer.append((char) 1);
		else
			buffer.append((char) 0);
		buffer.append(defaultResponse);
		return buffer.toString();
	}
	protected String isGPSLinked() {
		if(telescope.isGpsLinked())
			return (char)1 + defaultResponse;
		return (char)0 + defaultResponse;
	}
	protected String setTrackingMode(char trackingMode) {
		telescope.setTrackingMode(trackingMode);
		return defaultResponse;
	}
	protected String isGotoInProgress() {
		String response; 
		if(telescope.isGotoInProgress())
			response = "1" + defaultResponse;
		else
			response = "0" + defaultResponse;

		return response;
	}
	protected String isGpsLinked() {
		String response; 
		if(telescope.isGpsLinked())
			response = Character.toString((char) 1) + defaultResponse;
		else
			response = Character.toString((char) 0) + defaultResponse;

		return response;
	}
	protected String getModel() {
		return Character.toString(telescope.getModel()) +defaultResponse;
	}
	protected String getTrackingMode() {
		return Character.toString(telescope.getTrackingMode()) + defaultResponse;
	}
	protected String getVersion() {
		return	telescope.getVersion() + defaultResponse;
	}
	protected String echo(String message){
		return telescope.echo(message.charAt(1)) + defaultResponse;
	}
	protected String getAzmAlt(){
		String response, preciseResponse;
		
		preciseResponse = getPreciseAzmAlt();
		response = 	preciseResponse.substring(0,  4) +","+ 
					preciseResponse.substring(9, 13) +"#";
		return response;
	}
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
	protected String getRaDec(){
		String response, preciseResponse;
		
		preciseResponse = getPreciseRaDec();
		response = 	preciseResponse.substring(0,  4) +","+ 
					preciseResponse.substring(9, 13) +"#";
		return response;
	}
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
	protected String isAlignmentComplete(){
		String response;
		if(telescope.isAlignmentComplete())
			response = Character.toString((char) 1) + defaultResponse;
		else
			response = Character.toString((char) 0) + defaultResponse;
		return response;
	}
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
