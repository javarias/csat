#include "Lx200EMCCoordDevIO.h"

Lx200EMCCoordDevIO::Lx200CoordDevIO(char *deviceName, int axis, bool reversed) throw (csatErrors::CannotOpenDeviceEx)
{
	char *_METHOD_="Lx200EMCCoordDevIO::Lx200CoordDevIO";

	try{
		this->sp = new SerialRS232(deviceName,100);
	} catch(SerialRS232::SerialRS232Exception serialEx) {
		ACS_LOG( LM_ERROR , _METHOD_ , (LM_ERROR, "CannotOpenDeviceEx: %s", serialEx.what()) );
		csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason",serialEx.what());
		throw ex.getCannotOpenDeviceEx();
	}

	/* Check telescope connection by getting telescope's name */
	try{
		char tmp[1]; tmp[0] = 0x06;
		this->sp->write_RS232(tmp, 1);
		this->sp->read_RS232();
	} catch(SerialRS232::SerialRS232Exception serialEx) {
		csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
		ex.addData("Reason",serialEx.what());
		throw ex.getCannotOpenDeviceEx();
	}

	this->axis = axis;
	this->reversed = reversed;
}

Lx200EMCCoordDevIO::~Lx200CoordDevIO() 
{
	char *_METHOD_="Lx200EMCCoordDevIO::~Lx200CoordDevIO";
	ACS_TRACE(_METHOD_);

	delete this->sp;
}

double Lx200EMCCoordDevIO::sexa2double(const char *sexaStr)
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

CORBA::Double Lx200EMCCoordDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
	char *_METHOD_="Lx200EMCCoordDevIO::read";
	CORBA::Double value(0.0);
	char *msg;

	ACS_TRACE(_METHOD_);

	/* Get information according to axis */
	if (this->axis == ALTITUDE_AXIS) {
		this->sp->write_RS232(":GA#", 4);
		msg = this->sp->read_RS232();
	} else {
		this->sp->write_RS232(":GZ#", 4);
		msg = this->sp->read_RS232();
	}

	/* Format */
	value = sexa2double(msg);

	if( this->reversed )
		value = 360 - value;

	while( value >= 360 )
		value -= 360;

	return value;
}

void Lx200EMCCoordDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
	ACS_SHORT_LOG((LM_ERROR, "Lx200EMCCoordDevIO::write: This method should never be called!"));
}
