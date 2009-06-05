
#include "Telescope.h"

Telescope::Telescope(Communication *com) : _tid(0)
{
	this->com = com;
	this->com->sendData(1);
}

int Telescope::start()
{
	return pthread_create(&_tid, NULL,gate,this);
}

Telescope::~Telescope()
{
	if (_tid)
	pthread_join(_tid,NULL);
}

void Telescope::run()
{
	ESO50Stat_t *absenc;
	char *tlc_info;
	char msg[32];
	int i;
	
	while(true)
	{
		tlc_info = this->com->read();
		if(tlc_info[0]==35 && tlc_info[1]==8 && tlc_info[2]==1 && tlc_info[4]==0)
		{
			for(i=6; i<38; i++)
			msg[i-6]=tlc_info[i];
			absenc = (ESO50Stat_t*) msg;
			this->coordRa = (double)absenc->Current_HA;
			this->coordDec = (double)absenc->Current_Dec;
		}
		
	}
}

void * Telescope::gate(void * p)
{
	((Telescope *)p)->run();
	return NULL;
}


double Telescope::getCoordRa () { return (this->coordRa - 240*512)*(360.0/(1536.0*240.0)) ;}

double Telescope::getCoordDec() { return -33.26 + (360.0/(1024.0*288.0))*(this->coordDec - 288*512); }