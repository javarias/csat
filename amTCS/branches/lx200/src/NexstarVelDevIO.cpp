#include <math.h>

#include "NexstarVelDevIO.h"

NexstarVelDevIO::NexstarVelDevIO(char *deviceName, int axis) throw (csatErrors::CannotOpenDeviceEx){

	char *_METHOD_="NexstarVelDevIO::NexstarVelDevIO";

	try{
		ACS_TRACE("Creating SeriaRS232 device");
		this->sp = new SerialRS232(deviceName);
	} catch(SerialRS232::SerialRS232Exception serialEx) {
		ACS_LOG( LM_ERROR , _METHOD_ , (LM_ERROR, "CannotOpenDeviceEx: %s", serialEx.what()) );
		csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason",serialEx.what());
		throw ex.getCannotOpenDeviceEx();
	}
	this->sp->flush_RS232();

	/* Test if the telescope is connected */
	try{
		this->sp->write_RS232("Kx",2);
		this->sp->read_RS232();
	} catch(SerialRS232::SerialRS232Exception serialEx) {
		csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason",serialEx.what());
		throw ex.getCannotOpenDeviceEx();
	}

	this->axis = axis;
}

NexstarVelDevIO::~NexstarVelDevIO() {
	delete this->sp;
}

CORBA::Double NexstarVelDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl) {
	CORBA::Double azm(0.0);
	return azm;
}

void NexstarVelDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl){

	int vel = 0;
	double absValue = fabs(value);
	char command[8];

	// We see which telescope's velocity is adecuated for the given double value
	if( absValue >= 3 )
		vel = 9;
	else if( absValue < (4+2)/2 && absValue >= (2+1)/2 )
		vel = 8;
	else if( absValue < (2+1)/2. && absValue >= (1 + 5/60)/2. )
		vel = 7;
	else if( absValue < (1 + 5/60.)/2. && absValue >= (5/60. + 32/3600.)/2. )
		vel = 6;
	else if( absValue < (5/60. + 32/3600.)/2. && absValue >= (32/3600. + 16/3600.)/2. )
		vel = 5;
	else if( absValue < (32/3600. + 16/3600.)/2. && absValue >= (16/3600. + 8/3600.)/2. )
		vel = 4;
	else if( absValue < (16/3600. + 8/3600.)/2. && absValue >= (8/3600. + 4/3600.)/2. )
		vel = 3;
	else if( absValue < (8/3600. + 4/3600.)/2. && absValue >= (4/3600. + 2/3600.)/2. )
		vel = 2;
	else if( absValue < (4/3600. + 2/3600.)/2. && absValue >= (2/3600. + 0/3600.)/2. )
		vel = 1;
	else
		vel = 0;
	
	vel = ( value >= 0 ) ? vel : (-1)*vel;

   command[0] = 'P';
   command[1] = 2;
   command[2] = (this->axis == ALTITUDE_AXIS) ? 0x10 : 0x11 ;
   command[3] = ( vel > 0 ) ? 0x24 : 0x25 ;
   command[4] = labs((long int)vel);
   command[5] = 0;
   command[6] = 0;
   command[7] = 0;

	this->sp->write_RS232(command,8);
	this->sp->read_RS232();
	this->sp->flush_RS232();

	return;
}

