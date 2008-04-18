package alma.acs.callbacks;

import alma.ACS.CBvoid;
import alma.ACS.CBvoidHelper;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.container.ContainerServices;
import alma.ACS.OffShoot;
import alma.acs.callbacks.*;

public class MyRequesterUtil extends RequesterUtil{
	static public CBvoid giveCBVoid (ContainerServices cs, MyResponseReceiver x) throws AcsJContainerServicesEx {
		CBvoid ret;

		MyObjects.CBvoidImpl cb = new MyObjects.CBvoidImpl(x);
		OffShoot offshoot = cs.activateOffShoot(cb);
		ret = CBvoidHelper.narrow(offshoot);

		return ret;
	}
}
