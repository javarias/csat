#include <math.h>

#include "Lx200GPSVelDevIO.h"

Lx200GPSVelDevIO::Lx200GPSVelDevIO(char *deviceName, int axis) throw (csatErrors::CannotOpenDeviceEx)
{
        char *_METHOD_ = (char * )"Lx200GPSVelDevIO::Lx200GPSVelDevIO";
	ACS::Time time = getTimeStamp();
	CORBA::Double initialSlewRate(0.0);

        try{
                this->sp = new SerialRS232(deviceName,120);
        } catch(SerialRS232::SerialRS232Exception serialEx) {
                ACS_LOG( LM_ERROR , _METHOD_ , (LM_ERROR, "CannotOpenDeviceEx: %s", serialEx.what()) );
                csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
                ex.addData("Reason",serialEx.what());
                throw ex.getCannotOpenDeviceEx();
        }

        /* Check telescope connection by setting initial slew rate */
        try{
		this->axis = axis;
		write(initialSlewRate, time);

        } catch(SerialRS232::SerialRS232Exception serialEx) {
                csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
                ex.addData("Reason",serialEx.what());
                throw ex.getCannotOpenDeviceEx();
        }
}

Lx200GPSVelDevIO::~Lx200GPSVelDevIO() 
{
        char *_METHOD_ = (char *)"Lx200GPSVelDevIO::~Lx200GPSVelDevIO";
        ACS_TRACE(_METHOD_);

	delete this->sp;
}

CORBA::Double Lx200GPSVelDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
        char *_METHOD_ = (char *)"Lx200GPSVelDevIO::read";
        ACS_TRACE(_METHOD_);

	if(this->axis == ALTITUDE_AXIS)
		return this->slewRateElevation;
	else
		return this->slewRateAzimuth;
}

void Lx200GPSVelDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
	char slewRateCmd[9];
	char moveCmd[4];
	char posHaltCmd[4];
	char negHaltCmd[4];
	bool moving = true;
	double absValue = fabs(value);

	moveCmd[0] = ':';
	moveCmd[1] = 'M';
	moveCmd[3] = '#';
	posHaltCmd[0] = ':';
	posHaltCmd[1] = 'Q';
	posHaltCmd[3] = '#';
	negHaltCmd[0] = ':';
	negHaltCmd[1] = 'Q';
	negHaltCmd[3] = '#';

	if(this->axis == ALTITUDE_AXIS) {
		sprintf(slewRateCmd, ":RE%.3f#", absValue);
		this->slewRateElevation = value;
		if(value > 0)
			moveCmd[2] = 'n';
		else if (value < 0)
			moveCmd[2] = 's';
		else {
			moving = false;
			posHaltCmd[2] = 'n';
			negHaltCmd[2] = 's';
		}
			
			
	} else {
		sprintf(slewRateCmd, ":RA%.3f#", absValue);
		this->slewRateAzimuth = value;
		if(value > 0)
			moveCmd[2] = 'e';
		else if (value < 0)
			moveCmd[2] = 'w';
		else {
			moving = false;
			posHaltCmd[2] = 'w';
			negHaltCmd[2] = 'e';
		}
	}

	this->sp->write_RS232(slewRateCmd, 9);
	this->sp->flush_RS232();
	if(moving)
		this->sp->write_RS232(moveCmd, 4);
	else {
		this->sp->write_RS232(posHaltCmd, 4);
		this->sp->flush_RS232();
		this->sp->write_RS232(negHaltCmd, 4);
	}
}
