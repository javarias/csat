#include <math.h>

#include "Lx200EMCVelDevIO.h"

Lx200EMCVelDevIO::Lx200EMCVelDevIO(char *deviceName, int axis, bool reversed) throw (csatErrors::CannotOpenDeviceEx)
{
		  char *_METHOD_="Lx200EMCVelDevIO::Lx200EMCVelDevIO";
		  ACS::Time time = getTimeStamp();
		  CORBA::Double initialSlewRate(0.0);

		  try{
					 this->sp = new SerialRS232(deviceName,100);
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

	if(this->axis == ALTITUDE_AXIS)
      this->slewRateElevation = 0;
   else
      this->slewRateAzimuth = 0;
	this->reversed = reversed;
}

Lx200EMCVelDevIO::~Lx200EMCVelDevIO() 
{
		  char *_METHOD_="Lx200EMCVelDevIO::~Lx200EMCVelDevIO";
		  ACS_TRACE(_METHOD_);

		  delete this->sp;
}

CORBA::Double Lx200EMCVelDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
	char *_METHOD_="Lx200EMCVelDevIO::read";
	if(this->axis == ALTITUDE_AXIS)
		if( this->slewRateElevation )
	      return ( (this->reversed) ? this->slewRateElevation*(-1) : this->slewRateElevation);
		else
			return 0.0;
   else
		if( this->slewRateAzimuth )
	      return ( (this->reversed) ? this->slewRateAzimuth*(-1) : this->slewRateAzimuth);
		else
			return 0.0;
	ACS_TRACE(_METHOD_);

}

void Lx200EMCVelDevIO::write(const CORBA::Double &recv_value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
		char *_METHOD_="Lx200EMCVelDevIO::write";
		char slewRateCmd[4];
		char moveCmd[4];
		char posHaltCmd[4];
		char negHaltCmd[4];
		bool moving = true;
		double value;
		double absValue;

		value = recv_value;
		if( this->reversed )
			value *= (-1);

		absValue = fabs(value);

		slewRateCmd[0] = ':';
		slewRateCmd[1] = 'R';
		slewRateCmd[3] = '#';
		moveCmd[0] = ':';
		moveCmd[1] = 'M';
		moveCmd[3] = '#';
		posHaltCmd[0] = ':';
		posHaltCmd[1] = 'Q';
		posHaltCmd[3] = '#';
		negHaltCmd[0] = ':';
		negHaltCmd[1] = 'Q';
		negHaltCmd[3] = '#';


		if(absValue >= 5)
			slewRateCmd[2] = 'S';
		else if(absValue >= 2)
			slewRateCmd[2] = 'M';
		else if(absValue >= 0.035)
			slewRateCmd[2] = 'C';
		else if(absValue >= 0.002)
			slewRateCmd[2] = 'G';
		else
			value = 0;

		if(this->axis == ALTITUDE_AXIS) {

			posHaltCmd[2] = 'n';
			negHaltCmd[2] = 's';

			if ( (this->slewRateElevation < 0 && value > 0 ) || 
			      (this->slewRateElevation > 0 && value < 0)  ) {
				this->sp->write_RS232(posHaltCmd, 4);
				this->sp->flush_RS232();
				this->sp->write_RS232(negHaltCmd, 4);
				this->sp->flush_RS232();
			}

			this->slewRateElevation = value;
			if(value > 0)
			  		moveCmd[2] = 'n';
			else if (value < 0)
			  		moveCmd[2] = 's';
			else 
				moving = false;

		} else {

			posHaltCmd[2] = 'w';
			negHaltCmd[2] = 'e';

			if ( (this->slewRateAzimuth < 0 && value > 0 ) || 
			      (this->slewRateAzimuth > 0 && value < 0)  ) {
				this->sp->write_RS232(posHaltCmd, 4);
				this->sp->flush_RS232();
				this->sp->write_RS232(negHaltCmd, 4);
				this->sp->flush_RS232();
			}

			this->slewRateAzimuth = value;
			if(value > 0)
				 moveCmd[2] = 'w';
			else if (value < 0)
				 moveCmd[2] = 'e';
			else
				 moving = false;
		}

		if(moving) {
			this->sp->write_RS232(slewRateCmd, 4);
			this->sp->flush_RS232();
			this->sp->write_RS232(moveCmd, 4);
		}
		 else{
			this->sp->write_RS232(posHaltCmd, 4);
			this->sp->flush_RS232();
			this->sp->write_RS232(negHaltCmd, 4);
		}

}
