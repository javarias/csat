#include <math.h>

#include "NexsimVelDevIO.h"

NexsimVelDevIO::NexsimVelDevIO(NEXSIM_MODULE::NexSim_var simulator, int axis){
	//char *_METHOD_="NexsimVelDevIO::NexsimVelDevIO";
	this->m_simulator = simulator;
	this->axis = axis;
	if(this->axis == ALTITUDE_AXIS)
      this->slewRateElevation = 0;
   else
      this->slewRateAzimuth = 0;
}

CORBA::Double NexsimVelDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl) {

	//CORBA::Double alt(0.0);
	//return alt;
	if(this->axis == ALTITUDE_AXIS)
      return this->slewRateElevation;
   else
      return this->slewRateAzimuth;
}

void NexsimVelDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
	int vel = 0;
	double absValue = fabs(value);
	char command[8];

	// We see which telescope's velocity is adecuated for the given double value
	if( absValue >= 3.0f )
		vel = 9;
	else if( absValue < (4.0f+2.0f)/2.0f && absValue >= (2.0f+1.0f)/2.0f )
		vel = 8;
	else if( absValue < (2.0f+1.0f)/2.0f && absValue >= (1.0f + 5.0f/60.0f)/2.0f )
		vel = 7;
	else if( absValue < (1.0f + 5.0f/60.0f)/2.0f && absValue >= (5.0f/60.0f + 32.0f/3600.0f)/2.0f )
		vel = 6;
	else if( absValue < (5.0f/60.0f + 32.0f/3600.0f)/2.0f && absValue >= (32.0f/3600.0f + 16.0f/3600.0f)/2.0f )
		vel = 5;
	else if( absValue < (32.0f/3600.0f + 16.0f/3600.0f)/2.0f && absValue >= (16.0f/3600.0f + 8.0f/3600.0f)/2.0f )
		vel = 4;
	else if( absValue < (16.0f/3600.0f + 8.0f/3600.0f)/2.0f && absValue >= (8.0f/3600.0f + 4.0f/3600.0f)/2.0f )
		vel = 3;
	else if( absValue < (8.0f/3600.0f + 4.0f/3600.0f)/2.0f && absValue >= (4.0f/3600.0f + 2.0f/3600.0f)/2.0f )
		vel = 2;
	else if( absValue < (4.0f/3600.0f + 2.0f/3600.0f)/2.0f && absValue >= (2.0f/3600.0f + 0.0f/3600.0f)/2.0f )
		vel = 1;
	else
		vel = 0;
	
	vel = ( value >= 0.0f ) ? vel : (-1)*vel;

   command[0] = 'P';
   command[1] = 2;
   command[2] = (this->axis == ALTITUDE_AXIS) ? 0x11 : 0x10 ;
   command[3] = ( vel > 0 ) ? 0x24 : 0x25 ;
   command[4] = labs((long int)vel);
   command[5] = 0;
   command[6] = 0;
   command[7] = 0;

	if(this->axis == ALTITUDE_AXIS)
		this->slewRateElevation = vel;
	else
		this->slewRateAzimuth = vel;

	m_simulator->executeAction(command);

	return;
}

