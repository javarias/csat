#include "NexstarAzmDevIO.h"

NexstarAzmDevIO::NexstarAzmDevIO(char *deviceName) {
	this->sp = new SerialRS232(deviceName);
	this->sp->flush_RS232();
}

NexstarAzmDevIO::NexstarAzmDevIO(void *data) {
}

NexstarAzmDevIO::~NexstarAzmDevIO() {
	delete this->sp;
}

bool NexstarAzmDevIO::initializeValue() {
	return true;
}

CORBA::Double NexstarAzmDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl) {

	CORBA::Double azm(0.0);
	char *msg;
	unsigned long read_alt, read_azm;

	/* Send the message to the telescope */
	this->sp->write_RS232("z");
	msg = this->sp->read_RS232();
	sscanf(msg,"%08lX,%08lX#",&read_azm, &read_alt);
	azm = read_azm / MAX_PRECISE_ROTATION;
	azm *= 360.0;

	return azm;
}

void NexstarAzmDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl){
	ACS_SHORT_LOG((LM_ERROR, "NexstarAzmDevIO::write: This method should never be called!"));
	return;
}

