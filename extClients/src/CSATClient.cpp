#include "CSATClient.h"

void *idle_ping(void *p)
{
	SimpleClient *sc;
	sc = (SimpleClient *) p;
	sc->run();
	return NULL;
}

CSATClient::CSATClient()
{
	int i;
   char **p;
   p = new char*[3];
   for(i=0;i<3;i++)
      p[i] = new char[15];
   int n = 0;
   strcpy(p[0],"WrapControl");
   strcpy(p[1],"CSATCONTROL");
   strcpy(p[2],"");
	this->client = new SimpleClient::SimpleClient();
	this->client->init(n,p);
	this->client->login();
	for(i=0;i<3;i++)
		delete p[i];
	delete p;
	pthread_create(&(this->t1),NULL,idle_ping,(void *)(this->client));
}

CSATClient::~CSATClient()
{
}

CSATCONTROL_MODULE::CSATControl_var CSATClient::getcscClient()
{
	return this->csc;
}

CSATSTATUS_MODULE::CSATStatus_var CSATClient::getcssClient()
{
	return this->css;
}

int CSATClient::startCSC()
{
	try
	{
		this->csc = this->client->getComponent<CSATCONTROL_MODULE::CSATControl>("CSATCONTROL",0,true);
	}
	catch(maciErrType::CannotGetComponentExImpl &_ex)
	{
		_ex.log();
		return -1;
	}
	return 0;
}

int CSATClient::startCSS()
{
   try
   {
      this->css = this->client->getComponent<CSATSTATUS_MODULE::CSATStatus>("CSATSTATUS",0,true);
   }
   catch(maciErrType::CannotGetComponentExImpl &_ex)
   {
      _ex.log();
      return -1;
   }
   return 0;
}

void CSATClient::stop()
{
	this->client->logout();
	ACE_OS::sleep(3);
	//this->client->doneCORBA();
	delete this->client;
	pthread_join(this->t1,NULL);
}
