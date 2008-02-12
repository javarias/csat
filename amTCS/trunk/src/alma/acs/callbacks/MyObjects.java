package alma.acs.callbacks;

import org.omg.CORBA.UserException;

import alma.ACS.CBDescOut;
import alma.ACSErr.Completion;
import alma.acs.exceptions.AcsJCompletion;
import alma.acs.exceptions.AcsJException;

import alma.acs.callbacks.*;

public class MyObjects extends Objects {
	static public class CBvoidImpl extends alma.ACS.CBvoidPOA {

		protected MyResponseReceiver toBeCalledBack;

		public void setXXX (MyResponseReceiver x) {
			this.toBeCalledBack = x;
		}

		protected CBvoidImpl(MyResponseReceiver x) {
			toBeCalledBack = x;
		}

		public boolean negotiate (long time_to_transmit, CBDescOut desc) {
			return true;
		}

		public void working (Completion completion, CBDescOut desc) {}

		public void done (Completion completion, CBDescOut desc) {
			AcsJCompletion c = AcsJCompletion.fromCorbaCompletion(completion);
			if (c.isError()) {
				toBeCalledBack.incomingException(c.getAcsJException());

			} else {
				toBeCalledBack.incomingResponse();

			}
		}

	}
}
