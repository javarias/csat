#include "Lx200.h"

using namespace std;

Lx200::Lx200(bool isLocal, char *serialPort) : Telescope(isLocal,serialPort)
{
	this->alt = 0;
	this->az = 0;
	this->ra = 0;
	this->dec = 0;
	this->rate = 0;
}

void Lx200::parseInstructions()
{
	char buf;
	char *response;
	while(read(fdm, &buf, 1) && this->run)
	{
		if(buf == 'q') {
			cout << "Got a 'q' command, exiting..." << endl;
			break;
		}
		if(buf != '#')
		{
			if(buf == 6)
			{
				cout << "Getting Alignment" << endl;
				response = this->getAlignment();
			}
			else
			{
				printf("Something Wrong, Command: %d, %c\n", buf, buf);
				response = this->badMessageResponse();
			}
			if(this->length(response)>0)
				write(fdm,response,this->length(response));
			delete response;
			continue;
		}
		read(fdm,&buf,1);
		if(buf != ':')
		{
			printf("Something Wrong2, Command! %d, %c\n", buf, buf);
			response = this->badMessageResponse();
			if(this->length(response)>0)
				write(fdm,response,this->length(response));
			continue;
		}
		read(fdm,&buf,1);
		printf("Command: %d, %c\n", buf, buf);
		switch(buf)
		{
			case 'A':
						response = this->setAlignment();
						break;
			case 'B':
						response = this->reticuleControl();
						break;
			case 'C':
						response = this->sync();
						break;
			case 'D':
						response = this->getDistance();
						break;
			case 'f':
						response = this->fanControl();
						break;
			case 'F':
						response = this->focuserControl();
						break;
			case 'g':
						response = this->gpsControl();
						break;
			case 'G':
						response = this->getInformation();
						break;
			case 'h':
						response = this->homeControl();
						break;
			case 'H':
						response = this->timeFormat();
						break;
			case 'I':
						response = this->initialize();
						break;
			case 'L':
						response = this->objectsControl();
						break;
			case 'M':
						response = this->movement();
						break;
			case 'P':
						response = this->togglePointingPrecision();
						break;
			case 'Q':
						response = this->haltMovement();
						break;
			case 'r':
						response = this->fieldDerotatorControl();
						break;
			case 'R':
						response = this->slewRate();
						break;
			case 'S':
						response = this->telescopeSettings();
						break;
			case 'T':
						response = this->trackingControl();
						break;
			case 'U':
						response = this->togglePrecision();
						break;
			case 'V':
						response = this->badMessageResponse();
						break;
			case 'W':
						response = this->selectSite();
						break;
			case 'X':
						response = this->badMessageResponse();
						break;
			case '$':
						response = this->moreCommands();
						break;
			case '?':
						response = this->helpCommands();
						break;
			default:
						response = this->badMessageResponse();
						printf("Incorrect Command! %d, %c\n", buf, buf);
						sleep(1);
		}
		printf("Size: %d\n",this->length(response));
		if(this->length(response)>0)
		{
			if(response[0] == 15)
				printf("Error!\n");
			write(fdm,response,this->length(response));
		}
		delete response;
	}
}

char *Lx200::getAlignment()
{
	char *message, alignment;
	message = new char[2];
	alignment = 'A';//A=AltAz, L=Land, P=Polar.
	sprintf(message,"%c%c",alignment,ENDCHAR);
	return message;
}

char *Lx200::setAlignment()
{
	return this->badMessageResponse();
}

char *Lx200::reticuleControl()
{
	return this->badMessageResponse();
}

char *Lx200::sync()
{
	return this->badMessageResponse();
}

char *Lx200::getDistance()
{
	return this->badMessageResponse();
}

char *Lx200::fanControl()
{
	return this->badMessageResponse();
}

char *Lx200::focuserControl()
{
	return this->badMessageResponse();
}

char *Lx200::gpsControl()
{
	return this->badMessageResponse();
}

char *Lx200::getInformation()
{
	char *message, buf;
	TYPES::RadecPos rdPos;
	TYPES::AltazPos aaPos;
	TYPES::EarthPos ePos;
	int hr, min, sec;
	double sdt;
	read(fdm, &buf, 1);
	printf("G%c\n",buf);
	switch(buf)
	{
		case 'D':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[8];
					this->csatC->getcssClient()->getPos(rdPos,aaPos);
					sprintf(message,"%c%d*%d#%c",(rdPos.dec < 0)?'-':'+',(int)rdPos.dec,(int)(60*(rdPos.dec-(int)rdPos.dec)),ENDCHAR);
					printf("Dec: %s\n",message);
					return message;
		case 'R':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[9];
					this->csatC->getcssClient()->getPos(rdPos,aaPos);
					sprintf(message,"%d*%.1lf#%c",(int)(rdPos.ra/15.0),60*(rdPos.ra/15.0-(int)(rdPos.ra/15.0)),ENDCHAR);
					printf("Ra: %s\n",message);
					return message;
		case 'A':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[8];
					this->csatC->getcssClient()->getPos(rdPos,aaPos);
					sprintf(message,"%c%d*%d#%c",(aaPos.alt < 0)?'-':'+',(int)aaPos.alt,(int)(60*(aaPos.alt-(int)aaPos.alt)),ENDCHAR);
					return message;
		case 'Z':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[8];
					this->csatC->getcssClient()->getPos(rdPos,aaPos);
					sprintf(message,"%d*%d#%c",(int)aaPos.az,(int)(60*(aaPos.az-(int)aaPos.az)),ENDCHAR);
					return message;
		case 'V':
					read(fdm, &buf, 1);
					printf("GV%c\n",buf);
					switch(buf)
					{
						case 'D':
									read(fdm, &buf, 1);
									if(buf != '#')
										return this->badMessageResponse();
									message = new char[13];
									sprintf(message,"jun 05 1890#%c",ENDCHAR);
									break;
						case 'N':
									read(fdm, &buf, 1);
									if(buf != '#')
										return this->badMessageResponse();
									message = new char[6];
									sprintf(message,"01.0#%c",ENDCHAR);
									break;
						case 'P':
									read(fdm, &buf, 1);
									if(buf != '#')
										return this->badMessageResponse();
									message = new char[7];
									sprintf(message,"Lx200#%c",ENDCHAR);
									break;
						case 'T':
									read(fdm, &buf, 1);
									if(buf != '#')
										return this->badMessageResponse();
									message = new char[10];
									sprintf(message,"12:11:01#%c",ENDCHAR);
									break;
						default:
									printf("GV%c\n",buf);
									while(buf != '#')
										read(fdm, &buf, 1);
									message = this->badMessageResponse();
					}
					return message;
					break;
		case 'c':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[4];
					sprintf(message,"12#%c",ENDCHAR);
					return message;
		case 'M':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[5];
					sprintf(message,"ob1#%c",ENDCHAR);
					return message;
		case 'N':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[5];
					sprintf(message,"ob2#%c",ENDCHAR);
					return message;
		case 'O':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[5];
					sprintf(message,"ob3#%c",ENDCHAR);
					return message;
		case 'P':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[5];
					sprintf(message,"ob4#%c",ENDCHAR);
					return message;
		case 'T':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[5];
					sprintf(message,"00.0#%c",ENDCHAR);
					return message;
		case 't':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[8];
					ePos = this->csatC->getcssClient()->getLocalPos();
					sprintf(message,"%c%d*%d#%c",(ePos.latitude < 0)?'-':'+',(int)ePos.latitude,(int)(60*(ePos.latitude - (int)ePos.latitude)),ENDCHAR);
					return message;
		case 'g':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[9];
					ePos = this->csatC->getcssClient()->getLocalPos();
					sprintf(message,"%c%d*%d#%c",(ePos.longitude < 0)?'-':'+',(int)ePos.longitude,(int)(60*(ePos.longitude - (int)ePos.longitude)),ENDCHAR);
					return message;
		case 'L':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[9];
					sprintf(message,"11:02:03#%c",ENDCHAR);
					return message;
		case 'S':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[9];
					sdt = this->csatC->getcssClient()->getSiderealTime();
					hr = (int) sdt;
					min = (int)((sdt - hr)*60);
					sec = (int)(((sdt - hr)*60 - min)*60);
					sprintf(message,"%d:%d:%d#%c",hr,min,sec,ENDCHAR);
					return message;
		case 'C':
					read(fdm, &buf, 1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[9];
					sprintf(message,"09/02/85#%c",ENDCHAR);
					return message;
					
		default:
					printf("G%c\n",buf);
					while(buf != '#')
						read(fdm, &buf, 1);
					message = this->badMessageResponse();

	}
	return message;
}

char *Lx200::homeControl()
{
	return this->badMessageResponse();
}

char *Lx200::timeFormat()
{
	return this->badMessageResponse();
}

char *Lx200::initialize()
{
	return this->badMessageResponse();
}

char *Lx200::objectsControl()
{
	return this->badMessageResponse();
}

char *Lx200::movement()
{
	char *message, buf;
	TYPES::AltazPos aaPos;
	TYPES::AltazVel aaVel;
	TYPES::RadecPos rdPos;
	ACS::CBDescIn cbin;
	read(fdm,&buf,1);
	switch(buf)
	{
		case 'A':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[2];
					aaPos.alt = this->alt;
					aaPos.az = this->az;
					this->csatC->getcscClient()->goToAltAz(aaPos,aaVel,(ACS::CBvoid*)NULL,cbin);
					sprintf(message,"0%c",ENDCHAR);
					break;
		case 'S':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[2];
					rdPos.ra = this->ra;
					rdPos.dec = this->dec;
					this->csatC->getcscClient()->preset(rdPos,(ACS::CBvoid*)NULL,cbin);
					sprintf(message,"0%c",ENDCHAR);
					break;
		case 'n':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[1];
					aaVel = this->csatC->getcssClient()->getSlewRate();
					aaVel.azVel = this->rate;
					this->csatC->getcscClient()->setSlewRate(aaVel);
					sprintf(message,"%c",ENDCHAR);
					break;
		case 's':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[1];
					aaVel = this->csatC->getcssClient()->getSlewRate();
					aaVel.azVel = -this->rate;
					this->csatC->getcscClient()->setSlewRate(aaVel);
					sprintf(message,"%c",ENDCHAR);
					break;
		case 'e':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[1];
					aaVel = this->csatC->getcssClient()->getSlewRate();
					aaVel.altVel = -this->rate;
					this->csatC->getcscClient()->setSlewRate(aaVel);
					sprintf(message,"%c",ENDCHAR);
					break;
		case 'w':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[1];
					aaVel = this->csatC->getcssClient()->getSlewRate();
					aaVel.altVel = this->rate;
					this->csatC->getcscClient()->setSlewRate(aaVel);
					sprintf(message,"%c",ENDCHAR);
					break;
		default:
					printf("M%c\n",buf);
					while(buf != '#')
						read(fdm, &buf, 1);
					message = this->badMessageResponse();
	}
	return message;
}

char *Lx200::togglePointingPrecision()
{
	char *message, buf;
	read(fdm,&buf,1);
	if(buf != '#')
		return this->badMessageResponse();
	message = new char[1];
	sprintf(message,"%c",ENDCHAR);
	return message;
}

char *Lx200::haltMovement()
{
	char *message, buf;
	TYPES::AltazVel aaVel;
	aaVel = this->csatC->getcssClient()->getSlewRate();
	read(fdm,&buf,1);
	switch(buf)
	{
		case 'n':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					if(aaVel.azVel > 0){
						aaVel.azVel = 0;
						try{
							this->csatC->getcscClient()->setSlewRate(aaVel);
						}catch(csatErrors::TelescopeAlreadyMovingEx e){
							printf("Telescope is being moved by CSAT Control Loop.");
						}
					}
					message = new char[1];
					sprintf(message,"%c",ENDCHAR);
					break;
		case 's':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					if(aaVel.azVel < 0){
						aaVel.azVel = 0;
						try{
							this->csatC->getcscClient()->setSlewRate(aaVel);
						}catch(csatErrors::TelescopeAlreadyMovingEx e){
							printf("Telescope is being moved by CSAT Control Loop.");
						}
					}
					message = new char[1];
					sprintf(message,"%c",ENDCHAR);
					break;
		case 'e':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					if(aaVel.altVel < 0){
						aaVel.altVel = 0;
						try{
							this->csatC->getcscClient()->setSlewRate(aaVel);
						}catch(csatErrors::TelescopeAlreadyMovingEx e){
							printf("Telescope is being moved by CSAT Control Loop.");
						}
					}
					message = new char[1];
					sprintf(message,"%c",ENDCHAR);
					break;
		case 'w':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					if(aaVel.altVel > 0){
						aaVel.altVel = 0;
						try{
							this->csatC->getcscClient()->setSlewRate(aaVel);
						}catch(csatErrors::TelescopeAlreadyMovingEx e){
							printf("Telescope is being moved by CSAT Control Loop.");
						}
					}
					message = new char[1];
					sprintf(message,"%c",ENDCHAR);
					break;
		case '#':
					aaVel.altVel = 0;
					aaVel.azVel = 0;
						try{
							this->csatC->getcscClient()->setSlewRate(aaVel);
						}catch(csatErrors::TelescopeAlreadyMovingEx e){
							printf("Telescope is being moved by CSAT Control Loop.");
						}
					message = new char[1];
					sprintf(message,"%c",ENDCHAR);
					break;
		default:
					printf("Q%c\n",buf);
					while(buf != '#')
						read(fdm,&buf,1);
					message = this->badMessageResponse();
	}
	return message;
}

char *Lx200::fieldDerotatorControl()
{
	return this->badMessageResponse();
}

char *Lx200::slewRate()
{
	char *message, buf;
	read(fdm,&buf,1);
	switch(buf)
	{
		case 'G':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[1];
					this->rate = this->getSlewRate(1);
					sprintf(message,"%c",ENDCHAR);
					break;
		case 'C':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[1];
					this->rate = this->getSlewRate(2);
					sprintf(message,"%c",ENDCHAR);
					break;
		case 'M':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[1];
					this->rate = this->getSlewRate(3);
					sprintf(message,"%c",ENDCHAR);
					break;
		case 'S':
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[1];
					this->rate = this->getSlewRate(4);
					sprintf(message,"%c",ENDCHAR);
					break;
		default:
					printf("R%c\n",buf);
					while(buf != '#')
						read(fdm,&buf,1);
					message = this->badMessageResponse();
	}
	return message;
}

char *Lx200::telescopeSettings()
{
	char *message,buf,sign,*buffer;
	int var1, var2,var3;
	read(fdm,&buf,1);
	switch(buf)
	{
		case 'G':
					buffer = new char[4];
					read(fdm, buffer, 4);
					buffer[4] = '\0';
					printf("SG Buf: %s\n", buffer);
					sscanf(buffer," %c%d",&sign,&var1);
					delete buffer;
					if(sign == '-')
						var1 = -var1;
					read(fdm, &buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[2];
					sprintf(message,"1%c",ENDCHAR);
					break;
		case 'L':
					buffer = new char[10];
					read(fdm, buffer, 9);
					buffer[9] = '\0';
					printf("SL Buf: %s\n", buffer);
					sscanf(buffer," %d:%d:%d",&var1, &var2, &var3);
					delete buffer;
					read(fdm, &buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[2];
					sprintf(message,"1%c",ENDCHAR);
					break;
		case 'C':
					buffer = new char[10];
					read(fdm, buffer, 9);
					buffer[9] = '\0';
					printf("SC Buf: %s\n", buffer);
					sscanf(buffer," %d/%d/%d",&var1, &var2, &var3);
					delete buffer;
					read(fdm, &buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[26];
					sprintf(message,"1Updating Planetary Data#%c",ENDCHAR);
					break;
		case 'S':
					buffer = new char[10];
					read(fdm, buffer, 9);
					buffer[9] = '\0';
					printf("SS Buf: %s\n", buffer);
					sscanf(buffer," %d:%d:%d",&var1, &var2, &var3);
					delete buffer;
					read(fdm, &buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[2];
					sprintf(message,"1%c",ENDCHAR);
					break;
		case 'g':
					buffer = new char[7];
					read(fdm, buffer, 6);
					buffer[6] = '\0';
					printf("Sg Buf: %s\n", buffer);
					sscanf(buffer,"%d:%d",&var1, &var2);
					delete buffer;
					read(fdm, &buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[2];
					sprintf(message,"1%c",ENDCHAR);
					break;
		case 't':
					buffer = new char[10];
					read(fdm, buffer, 9);
					buffer[6] = '\0';
					printf("St Buf: %s\n", buffer);
					sscanf(buffer,"%c%d:%d.%d",&sign,&var1, &var2, &var3);
					delete buffer;
					read(fdm, &buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[2];
					sprintf(message,"1%c",ENDCHAR);
					break;
		case 'a':
					buffer = new char[7];
					read(fdm,buffer,6);
					buffer[6] = '\0';
					printf("Sa: %s\n",buffer);
					sscanf(buffer,"%c%d*%d",&sign,&var1,&var2);
					delete buffer;
					alt = var1 + var2/60.0;
					if(sign == '-')
						alt = -alt;
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[2];
					sprintf(message,"0%c",ENDCHAR);
					break;
		case 'd':
					buffer = new char[11];
					read(fdm,buffer,10);
					buffer[10] = '\0';
					printf("Sd: %s\n",buffer);
					sscanf(buffer," %c%d:%d:%d",&sign,&var1,&var2,&var3);
					delete buffer;
					dec = var1 + var2/60.0 + var3/3600.0;
					if(sign == '-')
						dec = -dec;
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[2];
					sprintf(message,"1%c",ENDCHAR);
					break;
		case 'r':
					buffer = new char[10];
					read(fdm,buffer,9);
					buffer[9] = '\0';
					printf("Sr: %s\n",buffer);
					sscanf(buffer," %d:%d:%d",&var1,&var2,&var3);
					delete buffer;
					ra = var1 + var2/60.0 +var3/3600.0;
					ra = ra*15;
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[2];
					sprintf(message,"1%c",ENDCHAR);
					break;
		case 'z':
					buffer = new char[7];
					read(fdm,buffer,6);
					buffer[6] = '\0';
					printf("Sa: %s\n",buffer);
					sscanf(buffer,"%d*%d",&var1,&var2);
					delete buffer;
					alt = var1 + var2/60.0;
					read(fdm,&buf,1);
					if(buf != '#')
						return this->badMessageResponse();
					message = new char[2];
					sprintf(message,"1%c",ENDCHAR);
					break;
		default:
					printf("S%c\n",buf);
					while(buf != '#')
						read(fdm, &buf, 1);
					message = this->badMessageResponse();
	}
	return message;
}

char *Lx200::trackingControl()
{
	return this->badMessageResponse();
}

char *Lx200::togglePrecision()
{
	char *message, buf;
	read(fdm,&buf,1);
	if(buf != '#')
		return this->badMessageResponse();
	message = new char[1];
	sprintf(message,"%c",ENDCHAR);
	return message;
}

char *Lx200::selectSite()
{
	return this->badMessageResponse();
}

char *Lx200::moreCommands()
{
	return this->badMessageResponse();
}

char *Lx200::helpCommands()
{
	return this->badMessageResponse();
}

char *Lx200::badMessageResponse()
{
	char *message;
	message = new char[2];
	sprintf(message,"#%c",ENDCHAR);
	message[0] = (char)15;
	return message;
}

char *Lx200::defaultMessageResponse()
{
   char *message;
   message = new char[2];
   sprintf(message,"#%c",ENDCHAR);
   return message;
}

int Lx200::length(char *msg)
{
	int i = 0;
	while(msg[i]!=(char)ENDCHAR) i++;
	return i;
}

double Lx200::getSlewRate(int val){
	switch(val)
	{
		case 0:
				return 0.0;
		case 1:
				return 8/3600.0;
		case 2:
				return 128/3600.0;
		case 3:
				return 2.0;
		case 4:
				return 8.0;  //7", 8" and 10" Telescopes.
				//return 6.0; //12" Telescope.
		default:
				return 0.0;
	}
}

Lx200::~Lx200()
{
}

void Lx200::configPort(){
}
