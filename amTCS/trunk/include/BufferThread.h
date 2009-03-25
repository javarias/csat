#ifndef _BUFFERTHREAD_H_
#define _BUFFERTHREAD_H_

#include <acsThread.h>
#include <acstime.h>
#include <logging.h>
#include <SerialRS232.h>
#include "csatErrors.h"
#include <baciDevIO.h>
//#include "ESO50CoordDevIO.h"
typedef struct
{
     short int Current_HAAxis;
     short int Current_HAWorm;
     short int Current_DecAxis;
     short int Current_DecWorm;
     unsigned int Current_HA;
     unsigned int Current_Dec;

} ESO50Stat_t;

class BufferThread :public ACS::Thread
{
	private:
    		char *device;
		SerialRS232 *sp;
		char *coordinates;
		bool ok;
		bool receiving;
		ESO50Stat_t *ESO50Stat;
		double HA;
		double Dec;
	public:
		BufferThread(const ACE_CString & name,
		const ACS::TimeInterval & responseTime=ACS::ThreadBase::defaultResponseTime,
		const ACS::TimeInterval & sleepTime=defaultSleepTime ,
		const bool del=false,
		const long _thrFlags=THR_NEW_LWP | THR_DETACHED) throw (acsthreadErrType::CanNotSpawnThreadExImpl);
		virtual ~BufferThread();
		void runLoop();
		char *getCoord();
		double getRA();
		double getDec();
		void startmsg();
		void msg2send(int msg_type,int option);

};

#endif /* _BUFFERTHREAD_H_ */
