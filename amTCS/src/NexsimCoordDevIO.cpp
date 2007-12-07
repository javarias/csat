#include "NexsimCoordDevIO.h"

NexsimCoordDevIO::NexsimCoordDevIO(NEXSIM_MODULE::NexSim_var simulator, int axis){

	//char *_METHOD_="NexsimCoordDevIO::NexsimCoordDevIO";
	this->m_simulator = simulator;
	this->axis = axis;
}

CORBA::Double NexsimCoordDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl) {

	CORBA::Double value(0.0);
	unsigned long read_alt, read_azm;
	char *msg;

	msg = m_simulator->executeAction("z");
	sscanf(msg,"%08lX,%08lX#",&read_azm, &read_alt);
	value = ( this->axis == axisAltitude ) ? read_alt/MAX_PRECISE_ROTATION : read_azm/MAX_PRECISE_ROTATION ;
	value *= 360;
	return value;
}

void NexsimCoordDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl){
	ACS_SHORT_LOG((LM_ERROR, "NexstarAltDevIO::write: This method should never be called!"));
	return;
}
