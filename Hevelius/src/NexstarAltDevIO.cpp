#include "NexstarAltDevIO.h"

NexstarAltDevIO::NexstarAltDevIO(char *deviceName) {
	this->sp = new SerialRS232(deviceName);
	this->sp->flush_RS232();
}

NexstarAltDevIO::NexstarAltDevIO(void *data) {
}

NexstarAltDevIO::~NexstarAltDevIO() {
	delete this->sp;
}

bool NexstarAltDevIO::initializeValue() {
	return true;
}

CORBA::Double NexstarAltDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl) {

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

void NexstarAltDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl){
	ACS_SHORT_LOG((LM_ERROR, "NexstarAltDevIO::write: This method should never be called!"));
	return;
}

