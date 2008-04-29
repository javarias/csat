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
	this->login = 0;
   p = new char*[3];
   for(i=0;i<3;i++)
      p[i] = new char[15];
   int n = 0;
   strcpy(p[0],"WrapControl");
   strcpy(p[1],"CSATCONTROL");
   strcpy(p[2],"");
	this->client = new SimpleClient::SimpleClient();
	if(this->client->init(n,p) != 0){
		this->login = this->client->login();
		if(this->login != 0){
			pthread_create(&(this->t1),NULL,idle_ping,(void *)(this->client));
		}
	}
	for(i=0;i<3;i++)
		delete p[i];
	delete p;
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
	if(this->login == 0)
		return -1;
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
	if(this->login == 0)
		return -1;
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
	if(this->login != 0){
		this->client->logout();
	}
	delete this->client;
	if(this->login !=0)
		pthread_join(this->t1,NULL);
}
