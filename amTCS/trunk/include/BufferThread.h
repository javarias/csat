#ifndef _BUFFERTHREAD_H_
#define _BUFFERTHREAD_H_

#include <acsThread.h>
#include <acstime.h>
#include <logging.h>
#include <SerialRS232.h>
#include "csatErrors.h"
#include <baciDevIO.h>


class BufferThread :public ACS::Thread
{
	private:
    		char *device;
		SerialRS232 *sp;
		char *coordinates;
		bool ok;
	public:
		BufferThread(const ACE_CString & name,
		const ACS::TimeInterval & responseTime=ACS::ThreadBase::defaultResponseTime,
		const ACS::TimeInterval & sleepTime=10000000,
		const bool del=false,
		const long _thrFlags=THR_NEW_LWP | THR_DETACHED) throw (acsthreadErrType::CanNotSpawnThreadExImpl);
		virtual ~BufferThread();
		void runLoop();
		char *getCoord();
};

#endif /* _BUFFERTHREAD_H_ */
