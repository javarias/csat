#include "NexstarAltDevIO.h"

NexstarAltDevIO::NexstarAltDevIO() {
}

NexstarAltDevIO::NexstarAltDevIO(void *data) {
}

NexstarAltDevIO::~NexstarAltDevIO() {
}

bool NexstarAltDevIO::initializeValue() {
	return true;
}

CORBA::Double NexstarAltDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl) {

}

void NexstarAltDevIO::write(const CORBA::Double &value, ACS::Time &timestamp), throw (ACS::ACSbaseExImpl){
	ACS_SHORT_LOG((LM_ERROR, "NexstarAltDevIO::write: This method should never be called!"));
	return;
}

