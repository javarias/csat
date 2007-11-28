package alma.acs.callbacks;

import alma.ACS.CBDescIn;
import alma.ACS.CBDescOut;
import alma.ACSErr.Completion;
import alma.ACS.CBvoid;
import alma.ACS.CBvoidHelper;

public class MyResponderUtil extends ResponderUtil {
	public static void respond (CBvoid cb, CBDescIn descIn) {
		Completion completion = MyResponderUtil.giveCompletion();
		CBDescOut cbDescOut = MyResponderUtil.giveDescOut(descIn);

		cb.done(completion, cbDescOut);
	}
}


