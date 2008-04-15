#include "Nexstar.h"

Nexstar::Nexstar()
{
	this->csatC = new CSATClient();
	this->cscRun = false;
	this->cssRun = false;
}

void Nexstar::parseInstructions()
{
	char buf;
	char *response;
	cscRun = (this->csatC->startCSC() == 0);
	cssRun = (this->csatC->startCSS() == 0);
	while(read(fdm, &buf, 1))
	{
		if(buf == 'q')
			break;
		printf("Command: %d, %c\n", buf, buf);
		if(!this->cscRun || !this->cssRun)
		{
			response = this->badMessageResponse();
			write(fdm,response,this->length(response));
			delete response;
			continue;
		}
		switch(buf)
		{
			case 'E':
						response = this->getRaDec();
						break;
			case 'e':
						response = this->getPreciseRaDec();
						break;
			case 'Z':
						response = this->getAzmAlt();
						break;
			case 'z':
						response = this->getPreciseAzmAlt();
						break;
			case 'R':
						response = this->gotoRaDec();
						break;
			case 'r':
						response = this->gotoPreciseRaDec();
						break;
			case 'B':
						response = this->gotoAzmAlt();
						break;
			case 'b':
						response = this->gotoPreciseAzmAlt();
						break;
			case 'S':
						response = this->syncRaDec();
						break;
			case 's':
						response = this->syncPreciseRaDec();
						break;
			case 't':
						response = this->getTrackingMode();
						break;
			case 'T':
						response = this->setTrackingMode();
						break;
			case 'P':
						response = this->passThroughCommand();
						break;
			case 'w':
						response = this->getLocation();
						break;
			case 'W':
						response = this->setLocation();
						break;
			case 'h':
						response = this->getTime();
						break;
			case 'H':
						response = this->setTime();
						break;
			case 'v':
						response = this->getVersion();
						break;
			case 'm':
						response = this->getModel();
						break;
			case 'K':
						response = this->echo();
						break;
			case 'J':
						response = this->isAlignmentComplete();
						break;
			case 'L':
						response = this->isGotoComplete();
						break;
			case 'M':
						response = this->cancelGoto();
						break;
			default:
						response = this->badMessageResponse();
						printf("Incorrect Command! %d, %c\n", buf, buf);
						sleep(1);
		}
		printf("Size: %d\n",this->length(response));
		write(fdm,response,this->length(response));
		delete response;
	}
}

char *Nexstar::getRaDec()
{
	char *message, *tmpmsg;
	char num1[5], num2[5];
	num1[4] = '\0';
	num2[4] = '\0';
	message = new char[11];
	tmpmsg = getPreciseRaDec();
	strncpy(num1,tmpmsg,4);
	strncpy(num2,tmpmsg+9,4);
	sprintf(message,"%s,%s#%c",num1,num2,ENDCHAR);
	delete tmpmsg;
	return message;
}

char *Nexstar::getPreciseRaDec()
{
	char *message;
	unsigned long ra, dec;
	TYPES::RadecPos rdPos;
	TYPES::AltazPos aaPos;
	message = new char[19];
	this->csatC->getcssClient()->getPos(rdPos,aaPos);
	ra = (unsigned long)(rdPos.ra*MAX_PRECISE_ROTATION);
	dec = (unsigned long)(rdPos.dec*MAX_PRECISE_ROTATION);
	sprintf(message,"%08lX,%08lX#%c",ra,dec,ENDCHAR);
	return message;
}

char *Nexstar::getAzmAlt()
{
	char *message, *tmpmsg;
   char num1[5], num2[5];
   num1[4] = '\0';
   num2[4] = '\0';
   message = new char[11];
   tmpmsg = getPreciseAzmAlt();
   strncpy(num1,tmpmsg,4);
   strncpy(num2,tmpmsg+9,4);
   sprintf(message,"%s,%s#%c",num1,num2,ENDCHAR);
   delete tmpmsg;
   return message;
}

char *Nexstar::getPreciseAzmAlt()
{
	char *message;
	unsigned long alt, azm;
	TYPES::RadecPos rdPos;
	TYPES::AltazPos aaPos;
	message = new char[19];
	this->csatC->getcssClient()->getPos(rdPos,aaPos);
	alt = (unsigned long) (aaPos.alt*MAX_PRECISE_ROTATION);
	azm = (unsigned long) (aaPos.az*MAX_PRECISE_ROTATION);
	sprintf(message,"%08lX,%08lX#%c",azm,alt,ENDCHAR);
	return message;
}

char *Nexstar::gotoRaDec()
{
	char buf[5], num[9], comma;
	long ra, dec;
	double rad, decd;
	TYPES::RadecPos rdPos;
	ACS::CBDescIn cbin;
	buf[4] = '\0';
	read(fdm,buf,4);
	sprintf(num,"%s0000",buf);
	sscanf(num,"%08lX", &ra);
	read(fdm,&comma,1);
	if(comma != ',')
		return this->badMessageResponse();
	read(fdm,buf,4);
	sprintf(num,"%s0000",buf);
   sscanf(num,"%08lX", &dec);
	rad = ra/MAX_PRECISE_ROTATION*360;
	decd = dec/MAX_PRECISE_ROTATION*360;
	printf("ra: %f, dec: %f\n",rad,decd);
	rdPos.ra = rad;
	rdPos.dec = decd;
	this->csatC->getcscClient()->preset(rdPos,(ACS::CBvoid*)NULL,cbin);
	return this->defaultMessageResponse();
}

char *Nexstar::gotoPreciseRaDec()
{
	char buf[9], num[9], comma;
   long ra, dec;
   double rad, decd;
	TYPES::RadecPos rdPos;
	ACS::CBDescIn cbin;
   buf[8] = '\0';
   read(fdm,buf,8);
   sprintf(num,"%s",buf);
   sscanf(num,"%08lX", &ra);
	read(fdm,&comma,1);
	if(comma != ',')
		return this->badMessageResponse();
   read(fdm,buf,8);
   sprintf(num,"%s",buf);
   sscanf(num,"%08lX", &dec);
   rad = ra/MAX_PRECISE_ROTATION*360;
   decd = dec/MAX_PRECISE_ROTATION*360;
   printf("ra: %f, dec: %f\n",rad,decd);
	rdPos.ra = rad;
	rdPos.dec = decd;
	this->csatC->getcscClient()->preset(rdPos,(ACS::CBvoid*)NULL,cbin);
   return this->defaultMessageResponse();
}

char *Nexstar::gotoAzmAlt()
{
	char buf[5], num[9], comma;
	long alt, azm;
	double altd, azmd;
	TYPES::AltazPos aaPos;
	TYPES::AltazVel aaVel;
	ACS::CBDescIn cbin;
	buf[4] = '\0';
	read(fdm,buf,4);
	sprintf(num,"%s0000",buf);
	sscanf(num,"%08lX", &alt);
	read(fdm,&comma,1);
	if(comma != ',')
		return this->badMessageResponse();
	read(fdm,buf,4);
	sprintf(num,"%s0000",buf);
	sscanf(num,"%08lX", &azm);
	altd = alt/MAX_PRECISE_ROTATION*360;
	azmd = azm/MAX_PRECISE_ROTATION*360;
	printf("alt: %f, azm: %f\n",altd,azmd);
	aaPos.alt = altd;
	aaPos.az = azmd;
	this->csatC->getcscClient()->goToAltAz(aaPos,aaVel,(ACS::CBvoid*)NULL,cbin);
	return this->defaultMessageResponse();
}

char *Nexstar::gotoPreciseAzmAlt()
{
	char buf[9], num[9], comma;
	long alt, azm;
	double altd, azmd;
	TYPES::AltazPos aaPos;
	TYPES::AltazVel aaVel;
	ACS::CBDescIn cbin;
	buf[8] = '\0';
	read(fdm,buf,8);
	sprintf(num,"%s",buf);
	sscanf(num,"%08lX", &alt);
	read(fdm,&comma,1);
	if(comma != ',')
		return this->badMessageResponse();
	read(fdm,buf,8);
	sprintf(num,"%s",buf);
	sscanf(num,"%08lX", &azm);
	altd = alt/MAX_PRECISE_ROTATION*360;
	azmd = azm/MAX_PRECISE_ROTATION*360;
	printf("alt: %f, azm: %f\n",altd,azmd);
	aaPos.alt = altd;
	aaPos.az = azmd;
	this->csatC->getcscClient()->goToAltAz(aaPos,aaVel,(ACS::CBvoid*)NULL,cbin);
	return this->defaultMessageResponse();
}

char *Nexstar::syncRaDec()
{
	return this->badMessageResponse();
}

char *Nexstar::syncPreciseRaDec()
{
	return this->badMessageResponse();
}

char *Nexstar::getTrackingMode()
{
	char *message;
	int status;
	message = new char[3];
	if(this->csatC->getcssClient()->getTrackingStatus())
		status = 1;
	else
		status = 0;
	sprintf(message,"%d#%c",status,ENDCHAR);
	return message;
}

char *Nexstar::setTrackingMode()
{
	char buf;
	read(fdm,&buf,1);
	if(buf=='0')
		this->csatC->getcscClient()->setTrackingStatus(false);
	else if(buf=='1')
		this->csatC->getcscClient()->setTrackingStatus(true);
	else if(buf=='2')
		printf("EQ North Not Implemented Yet!\n");
	else if(buf=='3')
		printf("EQ South Not Implemented Yet!\n");
	else
		printf("Incorrect tracking value!\n");
	return this->defaultMessageResponse();
}

char *Nexstar::passThroughCommand()
{
	//Important!!
	char buf[7];
	read(fdm,buf,7);
	return this->defaultMessageResponse();
}
char *Nexstar::getLocation()
{
	return this->badMessageResponse();
}

char *Nexstar::setLocation()
{
	return this->badMessageResponse();
}

char *Nexstar::getTime()
{
	return this->badMessageResponse();
}

char *Nexstar::setTime()
{
	return this->badMessageResponse();
}

char *Nexstar::getVersion()
{
	char *message;
   message = new char[5];
   sprintf(message, "%d%d#%c",4,12,ENDCHAR);
   return message;
	return this->badMessageResponse();
}

char *Nexstar::getModel()
{
	char *message;
	message = new char[4];
	sprintf(message, "%d#%c",11,ENDCHAR);
	return message;
}

char *Nexstar::echo()
{
	char *message;
	message = new char[3];
	char buf;
	read(fdm, &buf, 1);
	sprintf(message, "%c#%c",buf,ENDCHAR);
	return message;
}

char *Nexstar::isAlignmentComplete()
{
	return this->badMessageResponse();
}

char *Nexstar::isGotoComplete()
{
	return this->badMessageResponse();
}

char *Nexstar::cancelGoto()
{
	return this->badMessageResponse();
}

char *Nexstar::badMessageResponse()
{
	char *message;
	message = new char[2];
	sprintf(message,"#%c",ENDCHAR);
	return message;
}

char *Nexstar::defaultMessageResponse()
{
   char *message;
   message = new char[2];
   sprintf(message,"#%c",ENDCHAR);
   return message;
}

int Nexstar::length(char *msg)
{
	int i = 0;
	while(msg[i]!=(char)ENDCHAR) i++;
	return i;
}

Nexstar::~Nexstar()
{
	this->csatC->stop();
	delete this->csatC;
}
