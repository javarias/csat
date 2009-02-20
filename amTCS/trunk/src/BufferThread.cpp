
#include "BufferThread.h"

BufferThread::BufferThread(const ACE_CString & name,
               const ACS::TimeInterval& responseTime,
               const ACS::TimeInterval& sleepTime,
               const bool del,
               const long _thrFlags) throw (acsthreadErrType::CanNotSpawnThreadExImpl) : ACS::Thread(name,responseTime,sleepTime,del,_thrFlags)
{
	char *_METHOD_ = (char * )"ESO50VelDevIO::BufferThread";
        try{
                this->sp = new SerialRS232((char *)"/dev/ttyACM0",120);
        } catch(SerialRS232::SerialRS232Exception serialEx) {
                ACS_LOG( LM_ERROR , _METHOD_ , (LM_ERROR, "CannotOpenDeviceEx: %s", serialEx.what()) );
                csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
                ex.addData("Reason",serialEx.what());
                throw ex.getCannotOpenDeviceEx();
        }
	this->ok=false;
}

BufferThread::~BufferThread() 
{
        char *_METHOD_ = (char *)"ESO50VelDevIO::~ESO50VelDevIO";
        ACS_TRACE(_METHOD_);
	delete this->sp;
}

void BufferThread::runLoop()
{
	char *msg;
	msg = this->sp->read_RS232();
	if(msg[0]==35 && msg[1]==8 && msg[2]==1 && msg[4]==0)
	{	
		this->coordinates = msg;
		this->ok = true;
	}
};

char* BufferThread::getCoord()
{
	if(this->ok)
		return this->coordinates;
	else 
		return (char *)"\0";
}
