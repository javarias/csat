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
	
	public static final char device_AzmRaMotor = (char) 16; 	// AZM/RA Motor
	public static final char device_AltDecMotor = (char) 17; 	// ALT/DEC Motor
	public static final char device_GpsUnit = (char) 176; 		// GPS Unit
	public static final char device_Rtc = (char) 178; 			// RTC (CGE only)
	
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
	protected boolean gpsLinked;
	
	protected short version;
	protected short deviceVersion;
	protected short model;
	
	protected Thread positionControlThread;
	
	public Nexstar4State( ) {
		super();
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
	/**
	 * @return
	 */
	long getAzmAxis() {
		return azmAxis;
	}
	/**
	 * @return
	 */
	long getAltAxis() {
		return altAxis;
	}
	
	/* **************************************/
	/* ********************Interface Comands*/
	/**
	 * @param trackingMode
	 */
	public String setTrackingMode(char trackingMode) {
		positionControl.setTrackingMode(trackingMode);
		return defaultResponse;
	}
	/**
	 * @return
	 */
	public String isCalibrated() {
		String response;
		if(calibrated)
			response = Character.toString((char) 1) + defaultResponse;
		else
			response = Character.toString((char) 0) + defaultResponse;		
		return response;
	}
	/**
	 * @return
	 */
	public String isGotoInProgress() {
		String response; 
		if(positionControl.actionInProgress == Nexstar4PositionControl.action_gotoInProgress)
			response = "1" + defaultResponse;
		else
			response = "0" + defaultResponse;

		return response;
	}
	/**
	 * @return
	 */
	public String isGpsLinked() {
		String response; 
		if(gpsLinked)
			response = Character.toString((char) 1) + defaultResponse;
		else
			response = Character.toString((char) 0) + defaultResponse;

		return response;
	}
	/**
	 * @return
	 */
	public String getModel() {
		return Character.toString((char) 11) +defaultResponse;
	}
	/**
	 * @return
	 */
	public String getTrackingMode() {
		return Character.toString(positionControl.getTrackingMode()) + defaultResponse;
	}
	/**
	 * @return
	 */
	public String getVersion() {
		return	Character.toString((char)  4) +
				Character.toString((char) 12) + defaultResponse;
	}
	/**
	 * 
	 * @param message The character to echo
	 * @return The return string
	 */
	public String echo(char character){
		return character + "#";
	}
	/**
	 * 
	 * @return
	 */
	public String getAzmAlt(){
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
	public String getPreciseAzmAlt(){
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
		System.out.println("ALT axis position: "+altAxis);
		System.out.println("Example: "+(0l-(maxAltAxis+44l)));
		System.out.println("Example: "+((0l-(maxAltAxis+44l))%maxAltAxis));
		System.out.println("Example: "+(maxAltAxis+(0l-(maxAltAxis+44l))%maxAltAxis));
		return response;
	}
	/**
	 * 
	 * @return
	 */
	public String getRaDec(){
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
	public String getPreciseRaDec(){
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
	/* TODO
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
	
	public String isAlignmentComplete(){
		//TODO: isAlignmentComplete()
		String response; 
		response = Character.toString((char) 1) + defaultResponse;
		return response;
	}
	/**
	 * @return
	 */
	public String getDeviceVersion(String message) {
		//TODO: getDeviceVersion(String message) change to getDeviceVersion(char deviceChat)
		String response = defaultResponse;
		if		((int) message.charAt(2) == 16){  //of the AZM/RA Motor
			
		}
		else if	((int) message.charAt(2) == 17){ //of the ALT/DEC Motor
			
		}
		else if ((int) message.charAt(2) == 176){ //of the GPS Unit
			
		}
		else if	((int) message.charAt(2) == 178){ //of the RTC (CGE only)
			
		}
		return response;
	}
	public String gotoRA_DEC(long ra, long dec){
		positionControl.gotoRA_DEC(ra, dec);
		return defaultResponse;
	}
	public String gotoPreciseRA_DEC(long ra, long dec){
		positionControl.gotoPreciseRA_DEC(ra, dec);
		return defaultResponse;
	}
	public String gotoAZM_ALT(long azm, long alt){
		positionControl.gotoAZM_ALT(azm, alt);
		return defaultResponse;
	}
	public String gotoPreciseAZM_ALT(long azm, long alt){
		positionControl.gotoPreciseAZM_ALT(azm, alt);
		return defaultResponse;
	}
	public String cancelGoto(){
		positionControl.cancelGoto();
		return defaultResponse;
	}
	public String setVariableRateAZM_RA(long arcsecondsPerSecond, boolean direction){
		positionControl.variableRateAZM_RA(arcsecondsPerSecond, direction);
		return defaultResponse;
	}
	public String setVariableRateALT_DEC(long arcsecondsPerSecond, boolean direction){
		positionControl.variableRateALT_DEC(arcsecondsPerSecond, direction);
		return defaultResponse;
	}
	public String setFixedRateAZM_RA(boolean direction, char slewSpeedSymbol){
		positionControl.fixedRateAZM_RA(slewSpeedSymbol, direction);
		return defaultResponse;
	}
	public String setFixedRateALT_DEC(boolean direction, char slewSpeedSymbol){
		positionControl.fixedRateALT_DEC(slewSpeedSymbol, direction);
		return defaultResponse;
	}
	public String syncRA_DEC(long ra, long dec){
		positionControl.syncRA_DEC(ra, dec);
		return defaultResponse;
	}
	public String syncPreciseRA_DEC(long ra, long dec){
		positionControl.syncPreciseRA_DEC(ra, dec);
		return defaultResponse;
	}
}
