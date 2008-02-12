package alma.POINTING_MODULE;

import alma.POINTING_MODULE.Pointing;

import junit.framework.TestCase;
import java.util.Vector;

public class PointingTest extends TestCase{

	Pointing pointing_comp;
	PointingTestClient pointingC;

	public void setUp() {
		pointingC = PointingTestClient.start();
		pointing_comp = pointingC.getPointingComponent();
	}

	public void testValue() {
		pointing_comp.offSetAlt(0.01);
		pointing_comp.offSetAzm(0.02);
		this.assertEquals(0.01,pointing_comp.altOffset());
		this.assertEquals(0.02,pointing_comp.azmOffset());

		// Reset pointing
		pointing_comp.offSetAlt(-pointing_comp.altOffset());
		pointing_comp.offSetAzm(-pointing_comp.azmOffset());
		this.assertEquals(0.0,pointing_comp.altOffset());
		this.assertEquals(0.0,pointing_comp.azmOffset());
	}

	public void tearDown() {
		try{
			pointingC.tearDown();
		} catch (Exception e3){
			e3.printStackTrace();
		}
	}
}
