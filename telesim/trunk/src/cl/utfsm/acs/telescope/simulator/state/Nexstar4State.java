/**
 * 
 */
package cl.utfsm.acs.telescope.simulator.state;

/**
 * @author  dcontard
 */
public class Nexstar4State {
	public static final long maxXAxis = 4294967296l; 
	public static final long maxYAxis = 4294967296l;
	
	public static final char slewSpeed_4DegreesPerSec = (char) 9;
	public static final char slewSpeed_2DegreesPerSec = (char) 8;
	public static final char slewSpeed_1DegreesPerSec = (char) 7;
	public static final char slewSpeed_5MinutessPerSec = (char) 6;
	
	public static final char slewSpeed_32x = (char) 5;
	public static final char slewSpeed_16x = (char) 4;
	public static final char slewSpeed_8x = (char) 3;
	public static final char slewSpeed_4x = (char) 2;
	public static final char slewSpeed_2x = (char) 1;
	public static final char slewSpeed_0x = (char) 0;
	
	public static final char trackingMode_Off = (char) 0;
	public static final char trackingMode_AltAz = (char) 1;
	public static final char trackingMode_EQNorth = (char) 2;
	public static final char trackingMode_EQSouth = (char) 3;
	
	public static final char device_AzmRaMotor = (char) 16; 	// AZM/RA Motor
	public static final char device_AltDecMotor = (char) 17; 	// ALT/DEC Motor
	public static final char device_GpsUnit = (char) 176; 		// GPS Unit
	public static final char device_Rtc = (char) 178; 			// RTC (CGE only)
	
	public static final boolean positiveDirection = true;
	public static final boolean negativeDirection = false;
	
	protected static final String defaultResponse = "#"; 
	
	protected long xAxis;
	protected long yAxis;
	protected char slewSpeed;
	protected char trackingMode;
	
	protected int degreesOfLatitude;
	protected int minutesOfLatitude;
	protected int secondsOfLatitude;
	protected boolean south;
	
	protected int degreesOfLongitude;
	protected int minutesOfLongitude;
	protected int secondsOfLongitude;
	protected boolean west;
	
	protected boolean calibrated;
	protected boolean gotoInProgress;
	protected boolean gpsLinked;
	
	protected short version;
	protected short deviceVersion;
	protected short model;
	
	/**
	 * @param calibrated
	 */
	void setCalibrated(boolean calibrated) {
		this.calibrated = calibrated;
	}
	/**
	 * @param slewSpeed
	 */
	void setSlewSpeed(char slewSpeed) {
		this.slewSpeed = slewSpeed;
	}
	/**
	 * @param gotoInProgress
	 */
	void setGotoInProgress(boolean gotoInProgress) {
		this.gotoInProgress = gotoInProgress;
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
	void setXAxis(long axis) {
		xAxis = axis;
	}
	/**
	 * @param axis
	 */
	void setYAxis(long axis) {
		yAxis = axis;
	}
	/**
	 * @return
	 */
	long getXAxis() {
		return xAxis;
	}
	/**
	 * @return
	 */
	long getYAxis() {
		return yAxis;
	}
	
	/* **************************************/
	/* ********************Interface Comands*/
	/**
	 * @param trackingMode
	 */
	public String setTrackingMode(char trackingMode) {
		this.trackingMode = trackingMode;
		//TODO
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
	public String getSlewSpeed() {
		return Character.toString(slewSpeed) + defaultResponse;
	}
	/**
	 * @return
	 */
	public String isGotoInProgress() {
		String response; 
		if(gotoInProgress)
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
		return Character.toString(trackingMode) + defaultResponse;
	}
	/**
	 * @return
	 */
	public String getVersion() {
		return	Character.toString((char)  4) +
				Character.toString((char) 12) +defaultResponse;
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
		azm = Long.toHexString(xAxis);
		for(int i = 0; i<8-azm.length(); i++)
			buffer.append('0');
		buffer.append(azm);
		buffer.append(',');
		alt = Long.toHexString(yAxis);
		for(int i = 0; i<8-azm.length(); i++)
			buffer.append('0');
		buffer.append(alt);
		buffer.append('#');
		response = buffer.toString();
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
		azm = Long.toHexString(xAxis);
		for(int i = 0; i<8-azm.length(); i++)
			buffer.append('0');
		buffer.append(azm);
		buffer.append(',');
		alt = Long.toHexString(yAxis);
		for(int i = 0; i<8-azm.length(); i++)
			buffer.append('0');
		buffer.append(alt);
		buffer.append('#');
		response = buffer.toString();
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
		String response; 
		if(gotoInProgress)
			response = Character.toString((char) 1) + defaultResponse;
		else
			response = Character.toString((char) 0) + defaultResponse;

		return response;
	}
	/**
	 * @return
	 */
	public String getDeviceVersion(String message) {
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
		//TODO
		return defaultResponse;
	}
	public String gotoPreciseRA_DEC(long ra, long dec){
		//TODO
		return defaultResponse;
	}
	public String gotoAZM_ALT(long azm, long alt){
		//TODO
		return defaultResponse;
	}
	public String gotoPreciseAZM_ALT(long azm, long alt){
		//TODO
		return defaultResponse;
	}
	public String cancelGoto(){
		//TODO
		return defaultResponse;
	}
	public String setVariableRateAZM_RA(boolean direction, int rate){
		//TODO
		return defaultResponse;
	}
	public String setVariableRateALT_DEC(boolean direction, int rate){
		//TODO
		return defaultResponse;
	}
	public String setFixedRateAZM_RA(boolean direction, char rate){
		//TODO
		return defaultResponse;
	}
	public String setFixedRateALT_DEC(boolean direction, char rate){
		//TODO
		return defaultResponse;
	}
	public String syncRA_DEC(long ra, long dec){
		//TODO
		return defaultResponse;
	}
	public String syncPreciseRA_DEC(long ra, long dec){
		//TODO
		return defaultResponse;
	}
}
