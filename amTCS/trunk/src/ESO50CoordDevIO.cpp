#include "ESO50CoordDevIO.h"

ESO50CoordDevIO::ESO50CoordDevIO(char *deviceName, int axis) throw (csatErrors::CannotOpenDeviceEx)
{
	char *_METHOD_ = (char *)"ESO50CoordDevIO::ESO50CoordDevIO";
	printf("\n\n\n\nESO50CoordDevIO\n\n\n\n");

	try{
		this->sp = new SerialRS232(deviceName,120);
	} catch(SerialRS232::SerialRS232Exception serialEx) {
		ACS_LOG( LM_ERROR , _METHOD_ , (LM_ERROR, "CannotOpenDeviceEx: %s", serialEx.what()) );
		csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason",serialEx.what());
		throw ex.getCannotOpenDeviceEx();
	}

	//Check telescope connection by getting telescope's name 
	/*try{
		this->sp->write_RS232(":GVP#", 5);
		this->sp->read_RS232();
	} catch(SerialRS232::SerialRS232Exception serialEx) {
		csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason",serialEx.what());
		throw ex.getCannotOpenDeviceEx();
	}*/

	this->axis = axis;
}

ESO50CoordDevIO::~ESO50CoordDevIO() 
{
	char *_METHOD_ = (char *)"ESO50CoordDevIO::~ESO50CoordDevIO";
	ACS_TRACE(_METHOD_);

//	delete this->sp;
}

double ESO50CoordDevIO::sexa2double(const char *sexaStr)
{
        double converted = 0;
        double x = 0, y = 0, z = 0;
        char str[128];
        char *sign;

        strncpy (str, sexaStr, sizeof(str)-1);
        str[sizeof(str)-1] = '\0';
        sign = strchr(str, '-');
        if (sign)
            *sign = ' ';

        sscanf (str, "%lf%*[^0-9]%lf%*[^0-9]%lf", &x, &y, &z);
        converted = x + y/60 + z/3600;

        if (sign)
            converted *= -1;

        return converted;
}

CORBA::Double ESO50CoordDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
	char *_METHOD_ = (char *)"ESO50CoordDevIO::read";
	CORBA::Double value(0.0);
	char *msg;
	

	ACS_TRACE(_METHOD_);

	/* Get information according to axis */
	/*if (this->axis == ALTITUDE_AXIS) {
		this->sp->write_RS232(":GA#", 4);
		msg = this->sp->read_RS232();
	} else {
		this->sp->write_RS232(":GZ#", 4);
		msg = this->sp->read_RS232();
	}*/

	/* Format */
	value = sexa2double(msg);

	return value;
}

void ESO50CoordDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
	ACS_SHORT_LOG((LM_ERROR, "ESO50CoordDevIO::write: This method should never be called!"));
}
