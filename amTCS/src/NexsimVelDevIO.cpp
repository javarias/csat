#include <math.h>

#include "NexsimVelDevIO.h"

NexsimVelDevIO::NexsimVelDevIO(NEXSIM_MODULE::NexSim_var simulator, int axis){
	//char *_METHOD_="NexsimVelDevIO::NexsimVelDevIO";
	this->m_simulator = simulator;
	this->axis = axis;
}

CORBA::Double NexsimVelDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl) {

	CORBA::Double alt(0.0);
	return alt;
}

void NexsimVelDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
	int vel = 0;
	double absValue = fabs(value);
	char command[8];

	// We see which telescope's velocity is adecuated for the given double value
	if( absValue >= 3 )
		vel = 9;
	else if( absValue < (4+2)/2 && absValue >= (2+1)/2 )
		vel = 8;
	else if( absValue < (2+1)/2 && absValue >= (1 + 5/60)/2 )
		vel = 7;
	else if( absValue < (1 + 5/60)/2 && absValue >= (5/60 + 32/3600)/2 )
		vel = 6;
	else if( absValue < (5/60 + 32/3600)/2 && absValue >= (32/3600 + 16/3600)/2 )
		vel = 5;
	else if( absValue < (32/3600 + 16/3600)/2 && absValue >= (16/3600 + 8/3600)/2 )
		vel = 4;
	else if( absValue < (16/3600 + 8/3600)/2 && absValue >= (8/3600 + 4/3600)/2 )
		vel = 3;
	else if( absValue < (8/3600 + 4/3600)/2 && absValue >= (4/3600 + 2/3600)/2 )
		vel = 2;
	else if( absValue < (4/3600 + 2/3600)/2 && absValue >= (2/3600 + 0/3600)/2 )
		vel = 1;
	else
		vel = 0;
	
	vel = ( value >= 0 ) ? vel : (-1)*vel;

   command[0] = 'P';
   command[1] = 2;
   command[2] = (this->axis == ALTITUDE_AXIS) ? 0x11 : 0x10 ;
   command[3] = ( vel > 0 ) ? 0x24 : 0x25 ;
   command[4] = labs((long int)vel);
   command[5] = 0;
   command[6] = 0;
   command[7] = 0;

	m_simulator->executeAction(command);

	return;
}

