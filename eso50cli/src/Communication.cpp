/**
 * \file Communication.cpp
 *
 * Implements the communication with the ESO 50 telescope.
 * The communication with the device itself is done by the SerialRS232 class.
 *
 */

#include <sys/types.h>
#include <unistd.h>
#include <pthread.h>
#include <stdio.h>
#include <string.h>
#include <signal.h>
#include <time.h>

#include "Communication.h"

unsigned short bytefix(float data,int i)
{
	char *pointer;
	union pic_float_t
        {
        	unsigned short u[2] ;
        	char b[4];
	}x;
	pointer = (char *) & data;
    	x.b[3] = pointer[0];  //LSB mantisa
    	x.b[2] = pointer[1];
    	x.b[1] = pointer[2];  //casi MSB mantisa
    	x.b[0] = pointer[3];  //casi exponente
    	//Corrige exponente
    	x.b[0] <<= 1;
    	x.b[0] &= 254;                  //borra bit menos significativo
    	if( ( pointer[2] & 128 ) == 128 )     //si p[2] tiene bit mas significativo
    	{
        	x.b[0] |= 1;                //agrega bit menos significativo a b[0]
    	}
    	//Corrige bit signo
    	x.b[1] &= 127;                  //borra bit mas significativo
    	if( ( pointer[3] & 128 ) == 128 )     //si p[3] tiene bit mas significativo entonces
    	{
        	x.b[1] |= 128;              //agrega bit mas significativo a b[1]
    	}
	return x.u[i];
}
	
Communication::Communication(char *deviceName)
{
	this->sp = new SerialRS232(deviceName,80);
	this->device = deviceName;
	//this->sp->flush_RS232();
	//this->write(0,8, 0, 0, 0, 0,"ESO50");
}

Communication::~Communication()
{
        delete this->sp;
}

char* Communication::read()
{
	int i;
	char *mensaje,msg[32];

	mensaje = this->sp->read_RS232();
	this->sp->flush_RS232();
	//for(i=0;i<40;i++) printf("%i",mensaje[i]);
	//printf("\n");
	if(mensaje[0]==0) 
	for(i=0;i<40;i++) *(mensaje +i)=0;

	return mensaje;
}

/*
char* Communication::getGdata(char *mensaje, int option)
{
	char *msg;
	double data;
	int i,cont,j,k,orientation;
        char str[200];
	int deg;

	if (option == 0) cont=1;
	if (option == 1) cont=3;
	if (option == 2) cont=5;
	if (option == 3) cont=37;
	if (option == 4) cont=4;
	if (option == 5) cont=6;

	for( i=0; i<200; i++)
	{
		if(mensaje[i] == ','||mensaje[i] == ' '||mensaje[i] == '\n') cont--;
		if(cont == 0)
		{
			for ( j=0; j<83; j++ )
			{
				if(mensaje[i+j+1] == ','||mensaje[i+j+1] == ' '||mensaje[i+j+1] == '\n') 
				{
					str[j]='\0';
					break;
				}
				str[j]=mensaje[i+j+1];
			}
			break;
		}
	}

	strncpy (msg, str, sizeof(str)-1);
        msg[sizeof(str)-1] = '\0';
	return msg;
}
*/
double Communication::strtodou(char *msg)
{
	double data;
        sscanf (msg, "%lf", &data);
	return data;
}
	

int Communication::write(float wref,int direction, int msg_type, int run, int side, int pi, char* msg)
{
	SlavePWM_t msgSend;
	int auxTmr0,auxVfin,i,length,auxTm,auxRun,auxSide,auxPi;
	float auxWref,auxKi,auxKp;
	unsigned char ChkSum = 0;
	char *pointer, mensaje[32], tty_buffer[40];
	char *send;

	
	tty_buffer[0] = '#';
	tty_buffer[1] = 1;  //para quien va
 	tty_buffer[2] = direction;     //origen quien lo envia	
	tty_buffer[3] = 32;    //I2C_BUF_LEN
	tty_buffer[4] = 0;     //ack 
	tty_buffer[5] = msg_type;     //libre

	if(msg_type == 0)
	{
		for(i=0;i<32;i++) mensaje[i] = msg[i];
		printf("%s",mensaje);
		length = (int)sizeof(mensaje);
		pointer = (char *) & mensaje;
	}

	if(msg_type == 1)
	{
		msgSend.Wref_Lo = bytefix(wref,0);
		msgSend.Wref_Hi = bytefix(wref,1);
		msgSend.Ki_Lo = bytefix(1000,0);
		msgSend.Ki_Hi = bytefix(1000,1);
		msgSend.Kp_Lo = bytefix(500,0);
		msgSend.Kp_Hi = bytefix(500,1);

		if(auxWref >=20 && auxWref <=100)
		{
			msgSend.Ki_Lo = bytefix(200,0);
			msgSend.Ki_Hi = bytefix(200,1);
			msgSend.Kp_Lo = bytefix(100,0);
			msgSend.Kp_Hi = bytefix(100,1);
		}

		msgSend.Tm = (char)1;
		msgSend.Tmr0 = 60535;
		msgSend.Vfin = 0;

		msgSend.MtrCtrl = 0;		
		if(run == 1) msgSend.MtrCtrl += 1;
		if(side == 1) msgSend.MtrCtrl += 2;
		if(pi == 1) msgSend.MtrCtrl += 4;

		length = (int)sizeof(msgSend);
		pointer = (char *) & msgSend;
	}

	for( i = 0; i < 6; i ++ )
	{
		ChkSum += tty_buffer[i];
	}

	for( i = 0; i < length; i ++ )
	{
		mensaje[i] = pointer[i];
		ChkSum += pointer[i];
	}

	for( i = length; i < 32; i ++ )
	{
		mensaje[i] = 0;
	}
	
	for( i=0; i<32;i ++)
	{	
		tty_buffer[6+i]=mensaje[i];
	}

	tty_buffer[6 + 32] = ChkSum;
	tty_buffer[6 + 32 + 1] = '*';

	this->sp->flush_RS232();
	this->sp->write_RS232(tty_buffer,40);

	return tty_buffer[38];
}

void Communication::sendData(int option)
{	
	char tty_buffer[40], *pointer;
	unsigned char checksum;
	int lenght;
	int i;

	/*ESO50Prms.Target_HA_Lo = 0;
	ESO50Prms.Target_HA_Hi = 0;
	ESO50Prms.Target_Dec_Lo = 0;
	ESO50Prms.Target_Dec_Hi = 0;
	ESO50Prms.KpHA_Lo = 0;
	ESO50Prms.KpHA_Hi = 0;
	ESO50Prms.KiHA_Lo = 0;
	ESO50Prms.KiHA_Hi = 0;
	ESO50Prms.KdHA_Lo = 0;
	ESO50Prms.KdHA_Hi = 0;
 	ESO50Prms.KpDec_Lo = 0;
	ESO50Prms.KpDec_Hi = 0;
	ESO50Prms.KiDec_Lo = 0;
	ESO50Prms.KiDec_Hi = 0;
	ESO50Prms.KdDec_Lo = 0;
	ESO50Prms.KdDec_Hi = 0;*/
	/*MsgHead.Start  = '#';
   	MsgHead.From   = 0;
    	MsgHead.To     = 8;
    	MsgHead.Length = 32;
    	MsgHead.Ack    = 0;
    	MsgHead.Free   = 2;*/

	tty_buffer[0] = '#';
	tty_buffer[1] = 0;
 	tty_buffer[2] = 8;
	tty_buffer[3] = 32;
	tty_buffer[4] = 0;
	tty_buffer[5] = 2;
	
	checksum = 0;

	for( i = 0; i < 6; i ++ )
	{
		checksum += tty_buffer[i];
	}
	
	tty_buffer[6]=0;
	
	if(option)
	tty_buffer[6] = 1;

	checksum += tty_buffer[6];

	for(i=1; i<32 ; i++)
	{
		tty_buffer[6 + i] = 0;
	}
	
	tty_buffer[6 + 32] = (char)checksum;
	tty_buffer[6 + 32 + 1] = '*';

	this->sp->flush_RS232();
	this->sp->write_RS232(tty_buffer,40);
}
	
