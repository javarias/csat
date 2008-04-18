#include "eTrexCoordDevIO.h"
#include <math.h>

eTrexCoordDevIO::eTrexCoordDevIO(char *deviceName, int coordinate) throw (csatErrors::CannotOpenDeviceEx){

	const char *_METHOD_="eTrexCoordDevIO::eTrexCoordDevIO";

	try{
		this->comm = new eTrexCommunication(deviceName, true);
	} catch(SerialRS232::SerialRS232Exception serialEx) {
		ACS_LOG( LM_ERROR , _METHOD_ , (LM_ERROR, "CannotOpenDeviceEx: %s", serialEx.what()) );
		csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason",serialEx.what());
		throw ex.getCannotOpenDeviceEx();
	}

	/* Test if the telescope is connected */
	//try{
	//	this->sp->write_RS232("Kx",2);
	//	this->sp->read_RS232();
	//} catch(SerialRS232::SerialRS232Exception serialEx) {
	//	csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
	//	ex.addData("Reason",serialEx.what());
	//	throw ex.getCannotOpenDeviceEx();
	//}

	this->coordinate = coordinate;
}

eTrexCoordDevIO::~eTrexCoordDevIO() {
	delete this->comm;
}

CORBA::Double eTrexCoordDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl) {

	double coordinate = 0;
	char *msg;

	///* Send the message to the GPS */
	this->comm->request( POSITION_XFER );
	msg = this->comm->getResponse();

	if( !strcmp(msg,MSG_ERR) )	{
		ACS_ERROR( "Couldn't get the position from the GPS\n");
		return ERR_VALUE;
	}

	if( this->coordinate == LATITUDE_COORD )
		memcpy(&coordinate,msg + 3,8);
	else
		memcpy(&coordinate,msg + 11, 8);
	coordinate = (coordinate*180)/M_PI;

	return (coordinate);
}

void eTrexCoordDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl){
	ACS_SHORT_LOG((LM_ERROR, "eTrexCoordDevIO::write: This method should never be called!"));
	return;
}

