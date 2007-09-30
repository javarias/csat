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
	
	protected long xAxis;
	protected long yAxis;
	protected char slewSpeed;
	protected short trackingMode;
	
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
	 * @param trackingMode
	 */
	void setTrackingMode(short trackingMode) {
		this.trackingMode = trackingMode;
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
	public char getSlewSpeed() {
		return slewSpeed;
	}
	/**
	 * @return
	 */
	public boolean isGotoInProgress() {
		return gotoInProgress;
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
	public long getXAxis() {
		return xAxis;
	}
	/**
	 * @return
	 */
	public long getYAxis() {
		return yAxis;
	}
	/**
	 * @return
	 */
	public short getDeviceVersion() {
		return deviceVersion;
	}
	/**
	 * @return
	 */
	public short getModel() {
		return model;
	}
	/**
	 * @return
	 */
	public short getTrackingMode() {
		return trackingMode;
	}
	/**
	 * @return
	 */
	public int getVersion() {
		return version;
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
}
