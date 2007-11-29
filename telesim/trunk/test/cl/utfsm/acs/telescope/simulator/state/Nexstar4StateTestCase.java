/**
 * 
 */
package cl.utfsm.acs.telescope.simulator.state;

import ec.util.MersenneTwisterFast;
import junit.framework.TestCase;

/**
 * @author dcontard
 *
 */
public class Nexstar4StateTestCase extends TestCase {
	protected static final boolean AZM = true;
	protected static final boolean ALT = false;	

	protected Nexstar4State state;
	protected MersenneTwisterFast prng;
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		state = new Nexstar4State();
		prng = new MersenneTwisterFast();
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#Nexstar4State()}.
	 */
	public void testNexstar4State() {
		assertNotNull( new Nexstar4State() );
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#Nexstar4State(java.lang.String)}.
	 */
	public void testNexstar4StateString() {
		String portName;
		
		portName = "/dev/ttyS0";
		assertNotNull( new Nexstar4State(portName) );
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getAzmAxis()}.
	 */
	public void testGetAzmAxis() {
		long azm, azmAxis, azmAxisValue;
		
		azmAxis = 67;
		azm = state.getAzmAxis();
		
		state.setAzmAxis(azmAxis);
		azmAxisValue = state.getAzmAxis();
		assertEquals(azmAxis, azmAxisValue);
		
		state.setAzmAxis(azm);
		azmAxisValue = state.getAzmAxis();
		assertEquals(azm, azmAxisValue);
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getAltAxis()}.
	 */
	public void testGetAltAxis() {
		long alt, altAxis, altAxisValue;
		
		altAxis = 67;
		alt = state.getAltAxis();
		
		state.setAltAxis(altAxis);
		altAxisValue = state.getAltAxis();
		assertEquals(altAxis, altAxisValue);
		
		state.setAltAxis(alt);
		altAxisValue = state.getAltAxis();
		assertEquals(alt, altAxisValue);
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getRa()}.
	 */
	public void testGetRa() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getDec()}.
	 */
	public void testGetDec() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#setTrackingMode(char)}.
	 */
	public void testSetTrackingMode() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#isGotoInProgress()}.
	 */
	public void testIsGotoInProgress() {
		long azm, alt, aux;
		
		aux = 50000;
		azm = state.getAzmAxis();
		alt = state.getAltAxis();
		
		state.gotoAZM_ALT(azm+aux, alt+aux);
		assertTrue(state.isGotoInProgress());
		
		state.cancelGoto();
		assertFalse(state.isGotoInProgress());
		
		state.setAltAxis(alt);
		state.setAzmAxis(azm);
		state.gotoPreciseAZM_ALT(azm+aux, alt+aux);
		assertTrue(state.isGotoInProgress());
		
		state.setFixedRateALT_DEC(Nexstar4State.negativeDirection, Nexstar4PositionControl.slewSpeedSymbol_4DegreesPerSec);
		assertFalse(state.isGotoInProgress());
		
		state.setFixedRateALT_DEC(Nexstar4State.negativeDirection, Nexstar4PositionControl.slewSpeedSymbol_0x);
		state.setAzmAxis(azm);
		state.setAltAxis(alt);
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#isGpsLinked()}.
	 */
	public void testIsGpsLinked() {
		state.setGpsLinked(true);
		assertTrue(state.isGpsLinked());
		
		state.setGpsLinked(false);
		assertFalse(state.isGpsLinked());
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getModel()}.
	 */
	public void testGetModel() {
		assertEquals(Nexstar4State.model, state.getModel());
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getTrackingMode()}.
	 */
	public void testGetTrackingMode() {
		state.setTrackingMode(Nexstar4PositionControl.trackingMode_AltAz);
		assertEquals(Nexstar4PositionControl.trackingMode_AltAz, state.getTrackingMode());
		
		state.setTrackingMode(Nexstar4PositionControl.trackingMode_EQNorth);
		assertEquals(Nexstar4PositionControl.trackingMode_EQNorth, state.getTrackingMode());
		
		state.setTrackingMode(Nexstar4PositionControl.trackingMode_EQSouth);
		assertEquals(Nexstar4PositionControl.trackingMode_EQSouth, state.getTrackingMode());
		
		state.setTrackingMode(Nexstar4PositionControl.trackingMode_Off);
		assertEquals(Nexstar4PositionControl.trackingMode_Off, state.getTrackingMode());
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getVersion()}.
	 */
	public void testGetVersion() {
		assertEquals(Nexstar4State.version, state.getVersion());
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#echo(char)}.
	 */
	public void testEcho() {
		int testIterations;
		char echoChar;
		
		testIterations = 10;
		for( int i = 0; i < testIterations; i++ ){
			echoChar = prng.nextChar();
			assertEquals(echoChar, state.echo(echoChar));
		}	
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#isAlignmentComplete()}.
	 */
	public void testIsAlignmentComplete() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getDeviceVersion(char)}.
	 */
	public void testGetDeviceVersion() {
		assertEquals(Nexstar4State.deviceVersion_AltDecMotor, state.getDeviceVersion(Nexstar4State.device_AltDecMotor));
		assertEquals(Nexstar4State.deviceVersion_AzmRaMotor, state.getDeviceVersion(Nexstar4State.device_AzmRaMotor));
		assertEquals(Nexstar4State.deviceVersion_GpsUnit, state.getDeviceVersion(Nexstar4State.device_GpsUnit));
		assertEquals(Nexstar4State.deviceVersion_Rtc, state.getDeviceVersion(Nexstar4State.device_Rtc));
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#gotoAZM_ALT(long, long)}.
	 */
	public void testGotoAZM_ALT() {
		long azm, newAzm, alt, newAlt, aux, sleepTime;
		
		try {
			sleepTime = 300;
			aux = 50000;
			azm = state.getAzmAxis();
			alt = state.getAltAxis();
			
			/*going clockwise*/
			state.setAltAxis(0);
			state.setAzmAxis(0);
			
			state.gotoAZM_ALT(aux, aux);
			Thread.sleep(sleepTime);
			state.cancelGoto();
			
			newAzm = state.getAzmAxis();
			newAlt = state.getAltAxis();
			assertTrue(0 < newAzm);
			assertTrue(0 < newAlt);
			
			/*going anti-clockwise*/
			state.setAltAxis(aux);
			state.setAzmAxis(aux);
			
			state.gotoAZM_ALT(0, 0);
			Thread.sleep(sleepTime);
			state.cancelGoto();
			
			newAzm = state.getAzmAxis();
			newAlt = state.getAltAxis();
			assertTrue(aux > newAzm);
			assertTrue(aux > newAlt);
			
			/*going anti-clockwise because of nearest of the goal*/
			aux = Nexstar4State.maxAltAxis - aux;
			state.setAltAxis(0);
			state.setAzmAxis(0);
			
			state.gotoAZM_ALT(aux, aux);
			Thread.sleep(sleepTime);
			state.cancelGoto();
			
			newAzm = state.getAzmAxis();
			newAlt = state.getAltAxis();
			assertTrue( Nexstar4State.maxAltAxis > newAlt);
			assertTrue( Nexstar4State.maxAltAxis > newAzm);
			
			/*going clockwise because of nearest of the goal*/
			state.setAltAxis(aux);
			state.setAzmAxis(aux);
			
			state.gotoAZM_ALT(0, 0);
			Thread.sleep(sleepTime);
			state.cancelGoto();
			
			newAzm = state.getAzmAxis();
			newAlt = state.getAltAxis();
			assertTrue(aux < newAzm);
			assertTrue(aux < newAlt);
			
			state.setAltAxis(alt);
			state.setAzmAxis(azm);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#gotoPreciseAZM_ALT(long, long)}.
	 */
	public void testGotoPreciseAZM_ALT() {
		long azm, newAzm, alt, newAlt, aux, sleepTime;
		
		try {
			sleepTime = 300;
			aux = 50000;
			azm = state.getAzmAxis();
			alt = state.getAltAxis();
			
			/*going clockwise*/
			state.setAltAxis(0);
			state.setAzmAxis(0);
			
			state.gotoPreciseAZM_ALT(aux, aux);
			Thread.sleep(sleepTime);
			state.cancelGoto();
			
			newAzm = state.getAzmAxis();
			newAlt = state.getAltAxis();
			assertTrue(0 < newAzm);
			assertTrue(0 < newAlt);
			
			/*going anti-clockwise*/
			state.setAltAxis(aux);
			state.setAzmAxis(aux);
			
			state.gotoPreciseAZM_ALT(0, 0);
			Thread.sleep(sleepTime);
			state.cancelGoto();
			
			newAzm = state.getAzmAxis();
			newAlt = state.getAltAxis();
			assertTrue(aux > newAzm);
			assertTrue(aux > newAlt);
			
			/*going anti-clockwise because of nearest of the goal*/
			aux = Nexstar4State.maxAltAxis - aux;
			state.setAltAxis(0);
			state.setAzmAxis(0);
			
			state.gotoPreciseAZM_ALT(aux, aux);
			Thread.sleep(sleepTime);
			state.cancelGoto();
			
			newAzm = state.getAzmAxis();
			newAlt = state.getAltAxis();
			assertTrue( Nexstar4State.maxAltAxis > newAlt );
			assertTrue( Nexstar4State.maxAltAxis > newAzm );
			
			/*going clockwise because of nearest of the goal*/
			state.setAltAxis(aux);
			state.setAzmAxis(aux);
			
			state.gotoPreciseAZM_ALT(0, 0);
			Thread.sleep(sleepTime);
			state.cancelGoto();
			
			newAzm = state.getAzmAxis();
			newAlt = state.getAltAxis();
			assertTrue( aux < newAzm );
			assertTrue( aux < newAlt );
			
			state.setAltAxis(alt);
			state.setAzmAxis(azm);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#gotoRA_DEC(long, long)}.
	 */
	public void testGotoRA_DEC() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#gotoPreciseRA_DEC(long, long)}.
	 */
	public void testGotoPreciseRA_DEC() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#cancelGoto()}.
	 */
	public void testCancelGoto() {
		long azm, newAzm, auxAzm, alt, newAlt, auxAlt, aux, sleepTime;
		
		try {
			sleepTime = 300;
			aux = 50000;
			azm = state.getAzmAxis();
			alt = state.getAltAxis();
			
			state.setAltAxis(0);
			state.setAzmAxis(0);
			
			state.gotoPreciseAZM_ALT(aux, aux);
			Thread.sleep(sleepTime);
			state.cancelGoto();
			
			newAzm = state.getAzmAxis();
			newAlt = state.getAltAxis();
			
			Thread.sleep(sleepTime);
			
			auxAzm = state.getAzmAxis();
			auxAlt = state.getAltAxis();
			
			assertEquals(newAlt, auxAlt);
			assertEquals(newAzm, auxAzm);
			
			state.setAltAxis(alt);
			state.setAzmAxis(azm);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#setVariableRateAZM_RA(long, boolean)}.
	 */
	public void testSetVariableRateAZM_RA() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#setVariableRateALT_DEC(long, boolean)}.
	 */
	public void testSetVariableRateALT_DEC() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#setFixedRateAZM_RA(boolean, char)}.
	 */
	public void testSetFixedRateAZM_RA_negativeDirection() {
		long azm, alt;
		boolean azmOrAlt, direction;
		char slewSpeedSymbol;
		
		azm = state.getAzmAxis();
		alt = state.getAltAxis();
		try {
			azmOrAlt = AZM;
			direction = Nexstar4PositionControl.negativeDirection;
			
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_4DegreesPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_2DegreesPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_1DegreesPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_5MinutessPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_32x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_16x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_8x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_4x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_2x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_0x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("The test catched an InterruptedException while trying to sleep");
		}
		state.setAltAxis(alt);
		state.setAzmAxis(azm);
	}
	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#setFixedRateAZM_RA(boolean, char)}.
	 */
	public void testSetFixedRateAZM_RA_positiveDirection() {
		long azm, alt;
		boolean azmOrAlt, direction;
		char slewSpeedSymbol;
		
		azm = state.getAzmAxis();
		alt = state.getAltAxis();
		try {
			azmOrAlt = AZM;
			direction = Nexstar4PositionControl.positiveDirection;
			
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_4DegreesPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_2DegreesPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_1DegreesPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_5MinutessPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_32x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_16x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_8x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_4x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_2x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_0x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("The test catched an InterruptedException while trying to sleep");
		}
		state.setAltAxis(alt);
		state.setAzmAxis(azm);
	}
	
	protected void setFixedRate(char slewSpeedSymbol, boolean azmOrAlt, boolean direction) throws InterruptedException {
		long oldAzm, newAzm, newAlt, oldAlt, rate, delta, slewSpeed;
		int testIterations, sleepTime;
		
		testIterations = 8;
		sleepTime = 300;
				
		switch (slewSpeedSymbol) {
		case Nexstar4PositionControl.slewSpeedSymbol_4DegreesPerSec:
			slewSpeed = Nexstar4PositionControl.slewSpeed_4DegreesPerSec;
			break;

		case Nexstar4PositionControl.slewSpeedSymbol_2DegreesPerSec:
			slewSpeed = Nexstar4PositionControl.slewSpeed_2DegreesPerSec;
			break;

		case Nexstar4PositionControl.slewSpeedSymbol_1DegreesPerSec:
			slewSpeed = Nexstar4PositionControl.slewSpeed_1DegreesPerSec;
			break;

		case Nexstar4PositionControl.slewSpeedSymbol_5MinutessPerSec:
			slewSpeed = Nexstar4PositionControl.slewSpeed_5MinutessPerSec;
			break;

		case Nexstar4PositionControl.slewSpeedSymbol_32x:
			slewSpeed = Nexstar4PositionControl.slewSpeed_32x;
			break;

		case Nexstar4PositionControl.slewSpeedSymbol_16x:
			slewSpeed = Nexstar4PositionControl.slewSpeed_16x;
			break;

		case Nexstar4PositionControl.slewSpeedSymbol_8x:
			slewSpeed = Nexstar4PositionControl.slewSpeed_8x;
			break;

		case Nexstar4PositionControl.slewSpeedSymbol_4x:
			slewSpeed = Nexstar4PositionControl.slewSpeed_4x;
			break;

		case Nexstar4PositionControl.slewSpeedSymbol_2x:
			slewSpeed = Nexstar4PositionControl.slewSpeed_2x;
			break;

		default:
			slewSpeed = 0;
			break;
		}
		
		rate = (long)( ((double) (slewSpeed*sleepTime) )/1000.0 );
		delta = (long)( ((double) rate)/3.0);
		
		if(direction == Nexstar4PositionControl.negativeDirection){
			rate *= (-1l);
			
			state.setAltAxis(Nexstar4PositionControl.maxAzmAxis-1);
			state.setAzmAxis(Nexstar4PositionControl.maxAltAxis-1);
			
			oldAzm = Nexstar4PositionControl.maxAzmAxis-1;
			oldAlt = Nexstar4PositionControl.maxAltAxis-1;
		}
		else{
			state.setAltAxis(0);
			state.setAzmAxis(0);
			
			oldAzm = 0;
			oldAlt = 0;
		}
		
		System.out.println("rate: "+rate);
		System.out.println("delta: "+delta);
		state.setFixedRateAZM_RA(direction, slewSpeedSymbol);
		state.setFixedRateALT_DEC(direction, slewSpeedSymbol);
		for(int i=0; i<testIterations; i++){
			Thread.sleep(sleepTime);
			
			newAzm = state.getAzmAxis();
			newAlt = state.getAltAxis();
			
			System.out.println("newAzm: "+newAzm);
			System.out.println("oldAzm: "+oldAzm);
			System.out.println("newAzm-(oldAzm+rate): "+(newAzm-oldAzm-rate));
			if(Math.abs(newAzm-oldAzm-rate)<delta)
				System.out.println("diff<delta?: true");
			else
				System.out.println("diff<delta?: false");
			
			if(azmOrAlt == AZM){
				assertTrue(oldAzm+rate-delta <= newAzm);
				assertTrue(oldAzm+rate+delta >= newAzm);
			}
			else{
				assertTrue(oldAlt+rate-delta <= newAlt);
				assertTrue(oldAlt+rate+delta >= newAlt);
			}
			
			oldAzm = newAzm;
			oldAlt = newAlt;
		}
		state.setFixedRateAZM_RA(direction, Nexstar4PositionControl.slewSpeedSymbol_0x);
		state.setFixedRateALT_DEC(direction, Nexstar4PositionControl.slewSpeedSymbol_0x);
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#setFixedRateALT_DEC(boolean, char)}.
	 */
	public void testSetFixedRateALT_DEC_negativeDirection() {
		long azm, alt;
		boolean azmOrAlt, direction;
		char slewSpeedSymbol;
		
		azm = state.getAzmAxis();
		alt = state.getAltAxis();
		try {
			azmOrAlt = ALT;
			direction = Nexstar4PositionControl.negativeDirection;
			
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_4DegreesPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_2DegreesPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_1DegreesPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_5MinutessPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_32x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_16x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_8x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_4x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_2x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_0x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("The test catched an InterruptedException while trying to sleep");
		}
		state.setAltAxis(alt);
		state.setAzmAxis(azm);
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#setFixedRateALT_DEC(boolean, char)}.
	 */
	public void testSetFixedRateALT_DEC_positiveDirection() {
		long azm, alt;
		boolean azmOrAlt, direction;
		char slewSpeedSymbol;
		
		azm = state.getAzmAxis();
		alt = state.getAltAxis();
		try {
			azmOrAlt = ALT;
			direction = Nexstar4PositionControl.positiveDirection;
			
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_4DegreesPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_2DegreesPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_1DegreesPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_5MinutessPerSec;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_32x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_16x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_8x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_4x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_2x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
			slewSpeedSymbol = Nexstar4PositionControl.slewSpeedSymbol_0x;
			setFixedRate(slewSpeedSymbol, azmOrAlt, direction);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("The test catched an InterruptedException while trying to sleep");
		}
		state.setAltAxis(alt);
		state.setAzmAxis(azm);
	}
	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#syncRA_DEC(long, long)}.
	 */
	public void testSyncRA_DEC() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#syncPreciseRA_DEC(long, long)}.
	 */
	public void testSyncPreciseRA_DEC() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#setLocation(int, int, int, boolean, int, int, int, boolean)}.
	 */
	public void testSetLocation() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getDegreesOfLatitude()}.
	 */
	public void testGetDegreesOfLatitude() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getDegreesOfLongitude()}.
	 */
	public void testGetDegreesOfLongitude() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getMinutesOfLatitude()}.
	 */
	public void testGetMinutesOfLatitude() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getMinutesOfLongitude()}.
	 */
	public void testGetMinutesOfLongitude() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getSecondsOfLatitude()}.
	 */
	public void testGetSecondsOfLatitude() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#getSecondsOfLongitude()}.
	 */
	public void testGetSecondsOfLongitude() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#isSouth()}.
	 */
	public void testIsSouth() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link cl.utfsm.acs.telescope.simulator.state.Nexstar4State#isWest()}.
	 */
	public void testIsWest() {
		fail("Not yet implemented"); // TODO
	}

}
