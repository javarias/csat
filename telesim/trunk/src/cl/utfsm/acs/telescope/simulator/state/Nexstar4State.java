/**
 * 
 */
package cl.utfsm.acs.telescope.simulator.state;

/**
 * @author  dcontard
 */
public class Nexstar4State {
	public static final long maxAzmAxis = 4294967296l; 
	public static final long maxAltAxis = 4294967296l;
	
	protected static final String version = Character.toString((char) 4) + Character.toString((char) 12);
	protected static final char model = ((char) 11);
	
	public static final char device_AzmRaMotor = (char) 16; 	// AZM/RA Motor
	public static final char device_AltDecMotor = (char) 17; 	// ALT/DEC Motor
	public static final char device_GpsUnit = (char) 176; 		// GPS Unit
	public static final char device_Rtc = (char) 178; 			// RTC (CGE only)
	
	/*
	 * TODO: assign the proper character sequence for every device.
	 */
	public static final String deviceVersion_AzmRaMotor = 	Character.toString((char)  16) + Character.toString((char) 12); 	// AZM/RA Motor
	public static final String deviceVersion_AltDecMotor = 	Character.toString((char)  17) + Character.toString((char) 12); 	// ALT/DEC Motor
	public static final String deviceVersion_GpsUnit = 		Character.toString((char) 176) + Character.toString((char) 12); 		// GPS Unit
	public static final String deviceVersion_Rtc = 			Character.toString((char) 178) + Character.toString((char) 12); 			// RTC (CGE only)
	
	public static final boolean positiveDirection = Nexstar4PositionControl.positiveDirection;
	public static final boolean negativeDirection = Nexstar4PositionControl.negativeDirection;
	
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
	
	protected boolean calibrated;
	protected boolean alignmentComplete;
	protected boolean gpsLinked;
		
	protected Thread positionControlThread;
	
	/**
	 * 
	 *
	 */
	public Nexstar4State( ) {
		super();
		calibrated = false;
		alignmentComplete = true;
		gpsLinked = true;
		positionControl = new Nexstar4PositionControl(this);
		positionControlThread = new Thread(positionControl);
		positionControlThread.start();
	}
	/**
	 * @param calibrated
	 */
	void setCalibrated(boolean calibrated) {
		this.calibrated = calibrated;
	}
	/**
	 * 
	 * @param alignmentComplete
	 */
	void setAlignmentComplete(boolean alignmentComplete) {
		this.alignmentComplete = alignmentComplete;
	}
	/**
	 * @param gpsLinked
	 */
	void setGpsLinked(boolean gpsLinked) {
		this.gpsLinked = gpsLinked;
	}
	/**
	 * @param axis
	 */
	void setAzmAxis(long axis) {
		azmAxis = axis;
	}
	/**
	 * @param axis
	 */
	void setAltAxis(long axis) {
		altAxis = axis;
	}
	/* **************************************/
	/* ********************Interface Comands*/
	/**
	 * @return
	 */
	public long getAzmAxis() {
		return azmAxis;
	}
	/**
	 * @return
	 */
	public long getAltAxis() {
		return altAxis;
	}
	/**
	 * @return
	 */
	public long getRa() {
		//TODO: transform AzmAlt coordinates to RaDec coordinates
		return azmAxis;
	}
	/**
	 * @return
	 */
	public long getDec() {
		//TODO: transform AzmAlt coordinates to RaDec coordinates
		return altAxis;
	}
	/**
	 * @param trackingMode
	 */
	public void setTrackingMode(char trackingMode) {
		positionControl.setTrackingMode(trackingMode);
	}
	/**
	 * @return
	 */
	public boolean isCalibrated() {
		return calibrated;
	}
	/**
	 * @return
	 */
	public boolean isGotoInProgress() {
		if(positionControl.actionInProgress == Nexstar4PositionControl.action_gotoInProgress)
			return true;
		else
			return false;
	}
	/**
	 * @return
	 */
	public boolean isGpsLinked() {
		return gpsLinked;
	}
	/**
	 * @return
	 */
	public char getModel() {
		return model;
	}
	/**
	 * @return
	 */
	public char getTrackingMode() {
		return positionControl.getTrackingMode();
	}
	/**
	 * @return
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * 
	 * @param message The character to echo
	 * @return The return character (same as the argument character).
	 */
	public char echo(char character){
		return character;
	}
	/**
	 * @return
	 */
	public boolean isAlignmentComplete(){
		return alignmentComplete;
	}
	/**
	 * @return
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
	public void gotoRA_DEC(long ra, long dec){
		positionControl.gotoRA_DEC(ra, dec);
	}
	public void gotoPreciseRA_DEC(long ra, long dec){
		positionControl.gotoPreciseRA_DEC(ra, dec);
	}
	public void gotoAZM_ALT(long azm, long alt){
		positionControl.gotoAZM_ALT(azm, alt);
	}
	public void gotoPreciseAZM_ALT(long azm, long alt){
		positionControl.gotoPreciseAZM_ALT(azm, alt);
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
}
