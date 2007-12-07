#include "NexstarCoordDevIO.h"

NexstarCoordDevIO::NexstarCoordDevIO(char *deviceName, int axis) throw (csatErrors::CannotOpenDeviceEx){

	char *_METHOD_="NexstarCoordDevIO::NexstarCoordDevIO";

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

	this->axis = axis;
}

NexstarCoordDevIO::~NexstarCoordDevIO() {
	delete this->sp;
}

CORBA::Double NexstarCoordDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl) {

	CORBA::Double value(0.0);
	unsigned long read_alt, read_azm;
	char *msg;

	/* Send the message to the telescope */
	this->sp->write_RS232("z",1);
	msg = this->sp->read_RS232();
	sscanf(msg,"%08lX,%08lX#",&read_azm, &read_alt);
	value = ( this->axis == axisAltitude ) ? read_alt/MAX_PRECISE_ROTATION : read_azm/MAX_PRECISE_ROTATION ;
	value *= 360.0;

	return value;
}

void NexstarCoordDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl){
	ACS_SHORT_LOG((LM_ERROR, "NexstarCoordDevIO::write: This method should never be called!"));
	return;
}

