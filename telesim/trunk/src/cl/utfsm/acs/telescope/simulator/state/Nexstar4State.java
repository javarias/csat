/**
 * 
 */
package cl.utfsm.acs.telescope.simulator.state;

import java.io.File;

/**
 * @author  dcontard
 */
public class Nexstar4State {
	/**
	 * The maximum resolution that can be achieved by the azimuth axis of 
	 * the <code>{@link Nexstar4State}</code>, so the percentage of a 
	 * revolution of the azimuth axis can go from <code>0/maxAzmAxis</code>
	 * to <code>maxAzmAxis-1/maxAzmAxis</code>
	 * 
	 * @see <code>{@link Nexstar4PositionControl#maxAzmAxis}</code>
	 */
	public static final long maxAzmAxis = Nexstar4PositionControl.maxAzmAxis; 
	/**
	 * The maximum resolution that can be achieved by the altitude axis of 
	 * the <code>{@link Nexstar4State}</code>, so the percentage of a 
	 * revolution of the altitude axis can go from <code>0/maxAltAxis</code>
	 * to <code>maxAltAxis-1/maxAltAxis</code>
	 * 
	 * @see <code>{@link Nexstar4PositionControl#maxAltAxis}</code>
	 */
	public static final long maxAltAxis = Nexstar4PositionControl.maxAltAxis;
	
	/**
	 * Character encoding for the version of Nexstar4 Telescope, 
	 * according to Nexstar SE Communication Protocol specifications.
	 */
	public static final String version = Character.toString((char) 4) + Character.toString((char) 12);
	/**
	 * Character encoding for the model of Nexstar4 Telescope, 
	 * according to Nexstar SE Communication Protocol specifications.
	 */
	public static final char model = ((char) 11);
	
	/**
	 * Character encoding of the azimuth (and rigth ascension) motor, according to
	 * Nexstar SE Communication Protocol specifications.
	 */
	public static final char device_AzmRaMotor = (char) 16; 	// AZM/RA Motor
	/**
	 * Character encoding of the altitude (and declination) motor, according to
	 * Nexstar SE Communication Protocol specifications.
	 */
	public static final char device_AltDecMotor = (char) 17; 	// ALT/DEC Motor
	/**
	 * Character encoding of the GPS Unit, according to
	 * Nexstar SE Communication Protocol specifications.
	 */
	public static final char device_GpsUnit = (char) 176; 		// GPS Unit
	/**
	 * Character encoding of the RTC, according to
	 * Nexstar SE Communication Protocol specifications.
	 */
	public static final char device_Rtc = (char) 178; 			// RTC (CGE only)
	
	/*
	 * TODO: assign the proper character sequence for every device.
	 */
	/**
	 * Character encoding for the version of the azimuth (and rigth ascension) 
	 * motor, according to Nexstar SE Communication Protocol specifications.
	 */
	public static final String deviceVersion_AzmRaMotor = 	Character.toString((char)  16) + Character.toString((char) 12); 	// AZM/RA Motor
	/**
	 * Character encoding for the version of the altitude (and declination)
	 * motor, according to Nexstar SE Communication Protocol specifications.
	 */
	public static final String deviceVersion_AltDecMotor = 	Character.toString((char)  17) + Character.toString((char) 12); 	// ALT/DEC Motor
	/**
	 * Character encoding for the version of the GPS Unit, 
	 * according to Nexstar SE Communication Protocol specifications.
	 */
	public static final String deviceVersion_GpsUnit = 		Character.toString((char) 176) + Character.toString((char) 12); 		// GPS Unit
	/**
	 * Character encoding for the version of the RTC, 
	 * according to Nexstar SE Communication Protocol specifications.
	 */
	public static final String deviceVersion_Rtc = 			Character.toString((char) 178) + Character.toString((char) 12); 			// RTC (CGE only)
	
	/**
	 * Convention boolean value for determining the positive direction of the motors.
	 * 
	 * @see	Nexstar4PositionControl#positiveDirection
	 */
	public static final boolean positiveDirection = Nexstar4PositionControl.positiveDirection;
	/**
	 * Convention boolean value for determining the negative direction of the motors.
	 * 
	 * @see	Nexstar4PositionControl#negativeDirection
	 */
	public static final boolean negativeDirection = Nexstar4PositionControl.negativeDirection;
	
	public static final char alignmentType_NotAligned = (char) 0;
	public static final char alignmentType_SkyAlign = (char) 1;
	public static final char alignmentType_AutoTwoStarAlign = (char) 2;
	public static final char alignmentType_TwoStarAlign = (char) 3;
	public static final char alignmentType_OneStarAlign = (char) 4;
	public static final char alignmentType_SolarSystemAlign = (char) 5;
	public static final char alignmentType_EQAutoAlign = (char) 6;
	public static final char alignmentType_EQTwoStarAlign = (char) 7;
	public static final char alignmentType_EQOneStarAlign = (char) 8;
	public static final char alignmentType_EQSolarSystemAlign = (char) 9;
	
	protected static final String defaultResponse = "#";
	
	protected Nexstar4PositionControl positionControl;
	
	protected long azmAxis;
	protected long altAxis;
	
	protected int degreesOfLatitude;
	protected int minutesOfLatitude;
	protected int secondsOfLatitude;
	protected boolean south;
	
	protected int degreesOfLongitude;
	protected int minutesOfLongitude;
	protected int secondsOfLongitude;
	protected boolean west;
	
	protected boolean alignmentComplete;
	protected boolean gpsLinked;
	
	protected char alignmentState;
		
	protected Thread positionControlThread;
	
	/**
	 * 
	 *
	 */
	public Nexstar4State( ) {
		/*
		 * TODO:	Change this constructor, using a default xml file containing 
		 * 			the corresponding configuration. If the default xml file doesn't
		 * 			exist, create it.
		 */
		super();
		alignmentComplete = true;
		gpsLinked = true;
		
		degreesOfLatitude = 71;
		minutesOfLatitude = 31;
		secondsOfLatitude =  0;
		west = true;
		
		degreesOfLongitude = 33;
		minutesOfLongitude =  0;
		secondsOfLongitude =  0;
		south = true;
		
		positionControl = new Nexstar4PositionControl(this);
		positionControlThread = new Thread(positionControl);
		positionControlThread.start();
	}
	public Nexstar4State(String pathToTheConfigurationFile) {
		/*
		 * TODO: 	Implement this constructor, using a xml file containing 
		 * 			the corresponding configuration.
		 */
		this();
	}
	protected File createDefaultConfigurationFile(){
		/*
		 * TODO: Implement this method.
		 */
		return null;
	}
	void setAlignmentComplete(boolean alignmentComplete) {
		this.alignmentComplete = alignmentComplete;
	}
	void setGpsLinked(boolean gpsLinked) {
		this.gpsLinked = gpsLinked;
	}
	void setAzmAxis(long axis) {
		azmAxis = axis%maxAzmAxis;
		if(azmAxis < 0)
			azmAxis = maxAzmAxis - azmAxis;
	}
	void setAltAxis(long axis) {
		altAxis = axis%maxAltAxis;
		if(altAxis < 0)
			altAxis = maxAltAxis - altAxis;
	}
	protected char getAlignmentState() {
		return alignmentState;
	}
	protected void setAlignmentState(char alignmentState) {
		if(	
			(alignmentState != alignmentType_AutoTwoStarAlign) &&
			(alignmentState != alignmentType_EQAutoAlign) &&
			(alignmentState != alignmentType_EQOneStarAlign) &&
			(alignmentState != alignmentType_EQSolarSystemAlign) &&
			(alignmentState != alignmentType_EQTwoStarAlign) &&
			(alignmentState != alignmentType_NotAligned) &&
			(alignmentState != alignmentType_OneStarAlign) &&
			(alignmentState != alignmentType_SkyAlign) &&
			(alignmentState != alignmentType_TwoStarAlign)
			)
			return;
		
		this.alignmentState = alignmentState;
	}
	/* **************************************/
	/* ********************Interface Comands*/
	/**
	 * Returns the azimuth angle of the telescope simulator as the numerator of the 
	 * fraction of a revolution around the axis. The denominator of the fraction is
	 * given by the static public field <code>{@link Nexstar4State#maxAzmAxis}</code>.
	 * <p>
	 * If you want to transform this value to degrees, simply multiply the fraction
	 * by 360.
	 * 
	 * @return a long integer varying between 0 and <code>{@link Nexstar4State#maxAzmAxis}</code>-1
	 */
	public long getAzmAxis() {
		return azmAxis;
	}
	/**
	 * Returns the altitude angle of the <code>{@link Nexstar4State}</code> as the numerator of the 
	 * fraction of a revolution around the axis. The denominator of the fraction is
	 * given by the static public field <code>{@link Nexstar4State#maxAltAxis}</code>.
	 * <p>
	 * If you want to transform this value to degrees, simply multiply the fraction
	 * by 360.
	 * 
	 * @return a long integer varying between 0 and <code>{@link Nexstar4State#maxAltAxis}</code> -1
	 */
	public long getAltAxis() {
		return altAxis;
	}
	/**
	 * Returns the Right Ascension angle of the <code>{@link Nexstar4State}</code> as the numerator 
	 * of the fraction of a revolution around the axis. The denominator of the 
	 * fraction is given by the static public field <code>{@link Nexstar4State#maxAzmAxis}</code>.
	 * <p>
	 * If you want to transform this value to degrees, simply multiply the fraction
	 * by 360.
	 * 
	 * @return a long integer varying between 0 and <code>{@link Nexstar4State#maxAzmAxis}</code> -1
	 */
	public long getRa() {
		//TODO: transform AzmAlt coordinates to RaDec coordinates
		return azmAxis;
	}
	/**
	 * Returns the Declination angle of the <code>{@link Nexstar4State}</code> as the numerator 
	 * of the fraction of a revolution around the axis. The denominator of the 
	 * fraction is given by the static public field <code>{@link Nexstar4State#maxAltAxis}</code>.
	 * <p>
	 * If you want to transform this value to degrees, simply multiply the fraction
	 * by 360.
	 * 
	 * @return a long integer varying between 0 and <code>{@link Nexstar4State#maxAltAxis}</code> -1
	 */
	public long getDec() {
		//TODO: transform AzmAlt coordinates to RaDec coordinates
		return altAxis;
	}
	/**
	 * Sets the way in witch the <code>{@link Nexstar4State}</code> will change 
	 * its coordinates to emulate the way a real telescope would follow the sky 
	 * for specific location conditions (north or south hemisphere) or for other
	 * purposes.
	 * <p> 
	 * There are four tracking modes that can be used: 
	 * <code>{@link Nexstar4PositionControl#trackingMode_Off}</code>, 
	 * <code>{@link Nexstar4PositionControl#trackingMode_AltAz}</code>, 
	 * <code>{@link Nexstar4PositionControl#trackingMode_EQNorth}</code> and 
	 * <code>{@link Nexstar4PositionControl#trackingMode_EQSouth}</code>.
	 * 
	 * @param trackingMode	tracking mode symbol that determines the tracking mode 
	 * 						that will be adopted by the <code>{@link Nexstar4State}</code>
	 * @see 				Nexstar4PositionControl#setTrackingMode(char)
	 */
	public void setTrackingMode(char trackingMode) {
		positionControl.setTrackingMode(trackingMode);
	}
	/**
	 * Determines whether the <code>{@link Nexstar4State}</code> is excecuting a GOTO 
	 * action or not. 
	 * <p>
	 * It is important to clarify that slewing commands or tracking modes that also
	 * change the <code>{@link Nexstar4State}</code> orientation are not considered 
	 * by this method. The method only returns true if 
	 * a <code>{@link Nexstar4State#gotoRA_DEC}</code>, 
	 * a <code>{@link Nexstar4State#gotoPreciseRA_DEC}</code>,
	 * a <code>{@link Nexstar4State#gotoAZM_ALT}</code> or 
	 * a <code>{@link Nexstar4State#gotoPreciseAZM_ALT}</code> method is being excecuted.
	 * 
	 * @return <code>true</code> if a GOTO action is being excecuted, otherwise <code>false</code>
	 */
	public boolean isGotoInProgress() {
		if(positionControl.actionInProgress == Nexstar4PositionControl.action_gotoInProgress)
			return true;
		else
			return false;
	}
	/**
	 * Determines whether the <code>{@link Nexstar4State}</code> is simulating that it's
	 * connected to a GPS Unit or not. 
	 * <p>
	 * If this is actually true, then 
	 * @return
	 */
	public boolean isGpsLinked() {
		return gpsLinked;
	}
	/**
	 * Returns the character symbol that corresponds with the Nexstar4 Telescope.
	 * 
	 * @return the character symbol corresponding with the Nexstar4 Telescope model
	 */
	public char getModel() {
		return model;
	}
	/**
	 * Returns the tracking mode that is taking place in the
	 * <code>{@link Nexstar4State}</code>.
	 * 
	 * @return	a character corresponding to one of the four 
	 * 			posible tracking modes.
	 * @see Nexstar4PositionControl#trackingMode_Off
	 * @see Nexstar4PositionControl#trackingMode_AltAz
	 * @see Nexstar4PositionControl#trackingMode_EQNorth
	 * @see Nexstar4PositionControl#trackingMode_EQSouth
	 * @see	Nexstar4PositionControl#setTrackingMode(char)
	 * @see	Nexstar4PositionControl#getTrackingMode()
	 */
	public char getTrackingMode() {
		return positionControl.getTrackingMode();
	}
	/**
	 * Returns the character sequence (usually two characters) that corresponds with the 
	 * Nexstar4 Telescope version.
	 * 
	 * @return the character sequence corresponding with the Nexstar4 Telescope version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * Returns the character that recives as argument.
	 * <p>
	 * Useful method to determine whether the communication with the 
	 * <code>{@link Nexstar4State}</code> is working fine or not. 
	 * 
	 * @param character	the character to echo
	 * @return 			the same character as the <code>character</code>
	 * 					parameter
	 */
	public char echo(char character){
		return character;
	}
	/**
	 * Determines whether the <code>{@link Nexstar4State}</code> is excecuting an 
	 * alignment action or not. 
	 * <p>
	 * It's important to notice that the Nexstar SE 
	 * Communication Protocol specifications only supports this command for alignment
	 * porpuses. Commands for performing alignment actions aren't supported by the
	 * serial communication protocol, so that these commands shouldn't be implemented
	 * in a serial port interface.
	 * 
	 * @return	<code>true</code> if an alignment of any type is taking in progress, 
	 * 			otherwise <code>false</code>
	 */
	public boolean isAlignmentComplete(){
		return alignmentComplete;
	}
	/**
	 * Returns the character sequence that corresponds with the 
	 * version of one of the devices that usually has integrated
	 * a Nexstar4 Telescope. 
	 * 
	 * @param 	device	the character symbol corresponding with
	 * 					the device which version is requested
	 * @return 			the character sequence corresponding with 
	 * 					the version of the specified device
	 * @see		Nexstar4State#device_AltDecMotor
	 * @see		Nexstar4State#device_AzmRaMotor
	 * @see		Nexstar4State#device_GpsUnit
	 * @see		Nexstar4State#device_Rtc
	 */
	public String getDeviceVersion(char device) {
		if		(device == device_AzmRaMotor){  //of the AZM/RA Motor
			return deviceVersion_AzmRaMotor;
		}
		else if	(device == device_AltDecMotor){ //of the ALT/DEC Motor
			return deviceVersion_AltDecMotor;
		}
		else if (device == device_GpsUnit){ //of the GPS Unit
			return deviceVersion_GpsUnit;
		}
		else if	(device == device_Rtc){ //of the RTC (CGE only)
			return deviceVersion_Rtc;
		}
		return Character.toString((char) -2); //not recognized device
	}
	/* PositionControl  Commands: */
	/**
	 * 
	 * @param azm	
	 * @param alt	
	 */
	public void gotoAZM_ALT(long azm, long alt){
		positionControl.gotoAZM_ALT(azm, alt);
	}
	/**
	 * 
	 * @param azm	
	 * @param alt	
	 */
	public void gotoPreciseAZM_ALT(long azm, long alt){
		positionControl.gotoPreciseAZM_ALT(azm, alt);
	}
	/**
	 * @param ra	
	 * @param dec	
	 */
	public void gotoRA_DEC(long ra, long dec){
		positionControl.gotoRA_DEC(ra, dec);
	}
	/**
	 * 
	 * @param ra	
	 * @param dec	
	 */
	public void gotoPreciseRA_DEC(long ra, long dec){
		positionControl.gotoPreciseRA_DEC(ra, dec);
	}
	public void cancelGoto(){
		positionControl.cancelGoto();
	}
	public void setVariableRateAZM_RA(long arcsecondsPerSecond, boolean direction){
		positionControl.variableRateAZM_RA(arcsecondsPerSecond, direction);
	}
	public void setVariableRateALT_DEC(long arcsecondsPerSecond, boolean direction){
		positionControl.variableRateALT_DEC(arcsecondsPerSecond, direction);
	}
	public void setFixedRateAZM_RA(boolean direction, char slewSpeedSymbol){
		positionControl.fixedRateAZM_RA(slewSpeedSymbol, direction);
	}
	public void setFixedRateALT_DEC(boolean direction, char slewSpeedSymbol){
		positionControl.fixedRateALT_DEC(slewSpeedSymbol, direction);
	}
	public void syncRA_DEC(long ra, long dec){
		positionControl.syncRA_DEC(ra, dec);
	}
	public void syncPreciseRA_DEC(long ra, long dec){
		positionControl.syncPreciseRA_DEC(ra, dec);
	}
	/* Deprecated methods:
	String getAzmAlt(){
		String response, preciseResponse;
		
		preciseResponse = getPreciseAzmAlt();
		response = 	preciseResponse.substring(0,  4) +","+ 
					preciseResponse.substring(9, 13) +"#";
		return response;
	}
	String getPreciseAzmAlt(){
		String azm, alt, response;
		StringBuffer buffer = new StringBuffer(18);
		azm = Long.toHexString(azmAxis);
		for(int i = 0; i<8-azm.length(); i++)
			buffer.append('0');
		buffer.append(azm);
		buffer.append(',');
		alt = Long.toHexString(altAxis);
		for(int i = 0; i<8-alt.length(); i++)
			buffer.append('0');
		buffer.append(alt);
		buffer.append('#');
		response = buffer.toString().toUpperCase();
		return response;
	}
	String getRaDec(){
		String response, preciseResponse;
		
		preciseResponse = getPreciseRaDec();
		response = 	preciseResponse.substring(0,  4) +","+ 
					preciseResponse.substring(9, 13) +"#";
		return response;
	}
	String getPreciseRaDec(){
		String azm, alt, response;
		StringBuffer buffer = new StringBuffer(18);
		azm = Long.toHexString(azmAxis);
		for(int i = 0; i<8-azm.length(); i++)
			buffer.append('0');
		buffer.append(azm);
		buffer.append(',');
		alt = Long.toHexString(altAxis);
		for(int i = 0; i<8-alt.length(); i++)
			buffer.append('0');
		buffer.append(alt);
		buffer.append('#');
		response = buffer.toString().toUpperCase();
		return response;
	}*/
	
	/* TODO:
	 * get/set	Location
	 * get/set	Time
	 *
	 * GPS Commands
	 * get		latitude/longitude
	 * get		date/year/time
	 * 
	 * RTC Commands
	 * get/set	date/year/time
	 * 
	 */
	public void setLocation(	int degreesOfLatitude, int minutesOfLatitude, int secondsOfLatitude, boolean south,
								int degreesOfLongitude, int minutesOfLongitude, int secondsOfLongitude, boolean west){
		if (!gpsLinked) {
			secondsOfLatitude = Math.abs(secondsOfLatitude);
			secondsOfLongitude = Math.abs(secondsOfLongitude);
			minutesOfLatitude = Math.abs(minutesOfLatitude);
			minutesOfLongitude = Math.abs(minutesOfLongitude);
			degreesOfLatitude = Math.abs(degreesOfLatitude);
			degreesOfLongitude = Math.abs(degreesOfLongitude);
			
			this.secondsOfLatitude = secondsOfLatitude%60;
			this.minutesOfLatitude = (minutesOfLatitude + secondsOfLatitude/60)%60;
			this.degreesOfLatitude = (degreesOfLatitude + (minutesOfLatitude + secondsOfLatitude/60)/60 )%90;
			this.south = south;
			
			this.secondsOfLongitude = secondsOfLongitude%60;
			this.minutesOfLongitude = (minutesOfLongitude + secondsOfLongitude/60)%60;
			this.degreesOfLongitude = (degreesOfLongitude + (minutesOfLongitude + secondsOfLongitude/60)/60 )%180;
			this.west = west;
		}
	}
	public int getDegreesOfLatitude() {
		return degreesOfLatitude;
	}
	public int getDegreesOfLongitude() {
		return degreesOfLongitude;
	}
	public int getMinutesOfLatitude() {
		return minutesOfLatitude;
	}
	public int getMinutesOfLongitude() {
		return minutesOfLongitude;
	}
	public int getSecondsOfLatitude() {
		return secondsOfLatitude;
	}
	public int getSecondsOfLongitude() {
		return secondsOfLongitude;
	}
	public boolean isSouth() {
		return south;
	}
	public boolean isWest() {
		return west;
	}
}
