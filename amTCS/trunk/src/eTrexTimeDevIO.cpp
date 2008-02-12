#include <sys/time.h>

#include "eTrexTimeDevIO.h"

CORBA::LongLong eTrexTimeDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl) {

	CORBA::LongLong time(0);

	struct timeval  measuredTime;
	struct timezone tz;

	gettimeofday(&measuredTime,&tz);

	time = measuredTime.tv_sec;

	return time;
}

void eTrexTimeDevIO::write(const CORBA::LongLong &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl){
	ACS_SHORT_LOG((LM_ERROR, "eTrexTimeDevIO::write: This method should never be called!"));
	return;
}
