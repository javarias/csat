
#include "BufferThread.h"

BufferThread::BufferThread(const ACE_CString & name,
               const ACS::TimeInterval& responseTime,
               const ACS::TimeInterval& sleepTime,
               const bool del,
               const long _thrFlags) throw (acsthreadErrType::CanNotSpawnThreadExImpl) : ACS::Thread(name,responseTime,sleepTime,del,_thrFlags)
{
	char *_METHOD_ = (char * )"BufferThread::BufferThread";
        try{
               this->sp = new SerialRS232((char *)"/dev/ttyACM0",120);
        } catch(SerialRS232::SerialRS232Exception serialEx) {
                ACS_LOG( LM_ERROR , _METHOD_ , (LM_ERROR, "CannotOpenDeviceEx: %s", serialEx.what()) );
                csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
                ex.addData("Reason",serialEx.what());
                throw ex.getCannotOpenDeviceEx();
        }
	try{
		this->msg2send(0,0);
		this->sp->read_RS232();
		this->msg2send(2,1);

	} catch(SerialRS232::SerialRS232Exception serialEx) {
		csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason",serialEx.what());
		throw ex.getCannotOpenDeviceEx();
	}
	this->ok=false;
	this->receiving = true;
	this->HA = 0.0;
	this->Dec = 0.0;
}

BufferThread::~BufferThread() 
{
        char *_METHOD_ = (char *)"BufferThread::~BufferThread";
        ACS_TRACE(_METHOD_);
	delete this->sp;
}

void BufferThread::msg2send(int msg_type,int option)
{	
	char tty_buffer[40], *pointer;
	unsigned char checksum;
	int length;
	int i;
	char init[10]="ESO50";

	tty_buffer[0] = '#';
	tty_buffer[1] = 0;
 	tty_buffer[2] = 8;
	tty_buffer[3] = 32;
	tty_buffer[4] = 0;
	tty_buffer[5] = msg_type;
	
	length = (int)sizeof(init);
	pointer = (char *) & init;

	checksum = 0;

	for( i = 0; i < 6; i ++ )
	{
		checksum += tty_buffer[i];
	}
	
	tty_buffer[6]=0;
	
	if(msg_type == 2)
	{
		if(option)
		tty_buffer[6] += 1;
		checksum += tty_buffer[6];
		for(i=1; i<32 ; i++)
		{
			tty_buffer[6 + i] = 0;
		}
	}	

	else
	{
		for( i = 0; i < length; i ++ )
		{
			tty_buffer[6 + i] = pointer[i];
			checksum += pointer[i];
		}
		for( i = 6 + length; i < 38; i ++ )
		{
			tty_buffer[i] = 0;
		}
	}


	tty_buffer[6 + 32] = (char)checksum;
	tty_buffer[6 + 32 + 1] = '*';

	this->sp->flush_RS232();
	this->sp->write_RS232(tty_buffer,40);

}
/*void BufferThread::startmsg()
{
	this->msg2send(2,1);
	this->sp->read_RS232();
}
*/
void BufferThread::runLoop()
{
	char *msg;
	int i;
	char message[32];
	//while(1){
	msg = this->sp->read_RS232();
	for(i=0; i<40; i++) printf("%i ",msg[i]);
	printf("\n");
	//printf("tratando...\n");
	//this->sp->flush_RS232();
	//while(!(msg[0]==35 && msg[1]==8 && msg[2]==1 && msg[4]==0)) msg = this->sp->read_RS232();
	if(msg[1]==8 && msg[2]==1 && msg[4]==0)
	{	
		//printf("tratando...bueno\n");
		//for(i=0; i<40; i++) printf("%i ",msg[i]);
		//printf("\n");
		
		for(i=6; i<38; i++)
		message[i-6]=msg[i];
		this->ESO50Stat = (ESO50Stat_t*) message;
		this->receiving = false;
		//printf("haaxis :%i\n",this->ESO50Stat->Current_HA);
		//printf("decaxis :%i\n",this->ESO50Stat->Current_Dec);
		this->HA = (double)this->ESO50Stat->Current_HA;
		this->Dec = (double)this->ESO50Stat->Current_Dec;
	}
	/*char *msg;
	msg = this->sp->read_RS232();
	if(msg[0]==35 && msg[1]==8 && msg[2]==1 && msg[4]==0)
	{	
		this->coordinates = msg;
		this->ok = true;
	}*/
};

double BufferThread::getRA()
{
	return (this->HA - 240*512)*(360.0/(1536.0*240.0));//(((this->HA * 24)/(1024*240)) - 12.75);//((this->HA * 360)/(1024*240));
}

double BufferThread::getDec()
{
	return -33.26 + (360.0/(1024.0*288.0))*(this->Dec - 288*512);//(180 - ((this->Dec * 360)/(1024*288)));//((this->Dec * 360)/(1024*288));
}

