#include "NexsimAltDevIO.h"

NexsimAltDevIO::NexsimAltDevIO(char *deviceName) throw (csatErrors::CannotOpenDeviceEx){

	char *_METHOD_="NexsimAltDevIO::NexsimAltDevIO";

	try{
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
}

NexsimAltDevIO::~NexsimAltDevIO() {
	delete this->sp;
}

bool NexsimAltDevIO::initializeValue() {
	return true;
}

CORBA::Double NexsimAltDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl) {

	CORBA::Double alt(0.0);
	char *msg;
	unsigned long read_alt, read_azm;

	/* Send the message to the telescope */
	this->sp->write_RS232("z",1);
	msg = this->sp->read_RS232();
	sscanf(msg,"%08lX,%08lX#",&read_azm, &read_alt);
	alt = read_alt / MAX_PRECISE_ROTATION;
	alt *= 360.0;

	return alt;
}

void NexsimAltDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl){
	ACS_SHORT_LOG((LM_ERROR, "NexsimAltDevIO::write: This method should never be called!"));
	return;
}
