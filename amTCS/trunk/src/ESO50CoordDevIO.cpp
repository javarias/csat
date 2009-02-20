#include "ESO50CoordDevIO.h"

ESO50CoordDevIO::ESO50CoordDevIO(char *deviceName, int axis, BufferThread *thread_p) throw (csatErrors::CannotOpenDeviceEx)
{
	char *_METHOD_ = (char *)"ESO50CoordDevIO::ESO50CoordDevIO";

	try{
		this->sp = new SerialRS232(deviceName,120);
		this->thread_p = thread_p;
	} catch(SerialRS232::SerialRS232Exception serialEx) {
		ACS_LOG( LM_ERROR , _METHOD_ , (LM_ERROR, "CannotOpenDeviceEx: %s", serialEx.what()) );
		csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason",serialEx.what());
		throw ex.getCannotOpenDeviceEx();
	}

	//Check telescope connection by getting telescope's name 
	try{
		this->msg2send(0,0);
		this->sp->read_RS232();
		this->msg2send(1,1);

	} catch(SerialRS232::SerialRS232Exception serialEx) {
		csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason",serialEx.what());
		throw ex.getCannotOpenDeviceEx();
	}

	this->axis = axis;
}

ESO50CoordDevIO::~ESO50CoordDevIO() 
{
	char *_METHOD_ = (char *)"ESO50CoordDevIO::~ESO50CoordDevIO";
	ACS_TRACE(_METHOD_);
	this->msg2send(1,0);
	delete this->thread_p;
	delete this->sp;
}

void ESO50CoordDevIO::msg2send(int msg_type,int option)
{	
	ESO50Prms_t ESO50Prms;
	char tty_buffer[40], *pointer;
	unsigned char checksum = 0;
	int length;
	int i;
	char init[6] = "ESO50";
	if(msg_type == 1)
	{
		ESO50Prms.Target_HAAxis  = 0;
		ESO50Prms.Target_HAWorm  = 0;
		ESO50Prms.Target_DecAxis = 0;
		ESO50Prms.Target_DecWorm = 0;
		
		ESO50Prms.KpHA  = 0;
		ESO50Prms.KiHA  = 0;
		
		ESO50Prms.KpDec = 0;
		ESO50Prms.KiDec = 0;
		
		ESO50Prms.KdDec_Hi = 0;
		ESO50Prms.KdDec_Lo = 0;
		ESO50Prms.KdHA_Hi = 0;
		ESO50Prms.KdHA_Lo = 0;
		
		tty_buffer[0] = '#';
		tty_buffer[1] = 2;
		tty_buffer[2] = 8;
		tty_buffer[3] = 0;
		tty_buffer[4] = 0;
		tty_buffer[5] = 1;
		
		length = (int) sizeof( ESO50Prms );
		pointer = (char *) & ESO50Prms;
	}
	else {
		tty_buffer[0] = '#';
		tty_buffer[1] = 1;
		tty_buffer[2] = 8;
		tty_buffer[3] = 32;
		tty_buffer[4] = 0;
		tty_buffer[5] = msg_type;
	
		//strcpy(init,"ESO50");
		length = (int)sizeof(init);
		pointer = (char *) & init;
	}

	for( i = 0; i < 6; i ++ )
	{
		checksum += tty_buffer[i];
	}

	for( i = 0; i < length; i ++ )
	{
		tty_buffer[6 + i] = pointer[i];
		checksum += pointer[i];
	}

	if(msg_type == 1)
	{
		tty_buffer[6 + length] = (char)option; /*el importante*/
		checksum += tty_buffer[6 + length]; 
		length ++;
	}

	for( i = 6 + length; i < 38; i ++ )
	{
		tty_buffer[i] = 0;
	}
	
	tty_buffer[6 + 32] = (char)checksum;
	tty_buffer[6 + 32 + 1] = '*';
	
	this->sp->flush_RS232();
	this->sp->write_RS232(tty_buffer,40);
}
	

CORBA::Double ESO50CoordDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
	char *_METHOD_ = (char *)"ESO50CoordDevIO::read";
	CORBA::Double value(0.0);
	char *msg, message[32];
	int i;

	ACS_TRACE(_METHOD_);

	msg = this->thread_p->getCoord();
	while(!(msg[0]==35 && msg[1]==8 && msg[2]==1 && msg[4]==0)) msg = this->thread_p->getCoord();
	for(i=6; i<38; i++)
	message[i-6]=msg[i];
	this->ESO50Prms = (ESO50Prms_t*) message;

	/* Get information according to axis */
	if (this->axis == ALTITUDE_AXIS) {
		value = this->ESO50Prms->Target_DecAxis;
	} else {
		value = this->ESO50Prms->Target_HAAxis;
	}

	return value;
}

void ESO50CoordDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
	ACS_SHORT_LOG((LM_ERROR, "ESO50CoordDevIO::write: This method should never be called!"));
}
