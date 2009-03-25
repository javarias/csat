#include "ESO50CoordDevIO.h"
#include "CalculationsImpl.h"

ESO50CoordDevIO::ESO50CoordDevIO(char *deviceName, int axis,BufferThread *thread_p, bool reversed) throw (csatErrors::CannotOpenDeviceEx)
{
	char *_METHOD_ = (char *)"ESO50CoordDevIO::ESO50CoordDevIO";

	try{
		//this->sp = new SerialRS232(deviceName,120);
		this->thread_p = thread_p;

	} catch(SerialRS232::SerialRS232Exception serialEx) {
		ACS_LOG( LM_ERROR , _METHOD_ , (LM_ERROR, "CannotOpenDeviceEx: %s", serialEx.what()) );
		csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason",serialEx.what());
		throw ex.getCannotOpenDeviceEx();
	}

	//Check telescope connection by getting telescope's name 
	/*try{
		this->msg2send(0,0);
		this->sp->read_RS232();
		this->msg2send(2,1);

	} catch(SerialRS232::SerialRS232Exception serialEx) {
		csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason",serialEx.what());
		throw ex.getCannotOpenDeviceEx();
	}*/

	this->axis = axis;
	this->receiving = false;
	this->reversed = reversed;
	//this->thread_p->startmsg();
}

ESO50CoordDevIO::~ESO50CoordDevIO() 
{
	char *_METHOD_ = (char *)"ESO50CoordDevIO::~ESO50CoordDevIO";
	ACS_TRACE(_METHOD_);
	//this->msg2send(2,0);
	//delete this->thread_p;
	delete this->sp;
}

void ESO50CoordDevIO::msg2send(int msg_type,int option)
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

CORBA::Double ESO50CoordDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
	char *_METHOD_ = (char *)"ESO50CoordDevIO::read";
	CORBA::Double value(0.0);
	char *msg, message[32];
	int i;

	ACS_TRACE(_METHOD_);

	if (this->axis == ALTITUDE_AXIS) {
		value = this->thread_p->getDec();
		//printf("DecAxis %lf\n",value);
	} 
	else {
		value = this->thread_p->getRA();
		//printf("HAAxis %lf\n",value);
	}

	if( this->reversed )
		value = 360 - reversed;

	return value;
}

void ESO50CoordDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
	ACS_SHORT_LOG((LM_ERROR, "ESO50CoordDevIO::write: This method should never be called!"));
}
