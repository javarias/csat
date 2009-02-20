#include <math.h>

#include "ESO50VelDevIO.h"

ESO50VelDevIO::ESO50VelDevIO(char *deviceName, int axis) throw (csatErrors::CannotOpenDeviceEx)
{
        char *_METHOD_ = (char * )"ESO50VelDevIO::ESO50VelDevIO";
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

        /*Check telescope connection by setting initial slew rate */
        try{
		this->axis = axis;
		write(initialSlewRate, time);

        } catch(SerialRS232::SerialRS232Exception serialEx) {
                csatErrors::CannotOpenDeviceExImpl ex(__FILE__,__LINE__,_METHOD_);
                ex.addData("Reason",serialEx.what());
                throw ex.getCannotOpenDeviceEx();
        }
}

ESO50VelDevIO::~ESO50VelDevIO() 
{
        char *_METHOD_ = (char *)"ESO50VelDevIO::~ESO50VelDevIO";
        ACS_TRACE(_METHOD_);
	delete this->sp;
}

CORBA::Double ESO50VelDevIO::read(ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
        char *_METHOD_ = (char *)"ESO50VelDevIO::read";
        ACS_TRACE(_METHOD_);
	FILE *fp;

	if(this->axis == ALTITUDE_AXIS)
	{
		return this->velocityDec;
	}
	else
	{
		return this->velocityHA;
	}
}

unsigned short ESO50VelDevIO::bytefix(float data,int i)
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

void ESO50VelDevIO::write(const CORBA::Double &value, ACS::Time &timestamp) throw (ACSErr::ACSbaseExImpl)
{
	SlavePWM_t msgSend;
	int auxTmr0,auxVfin,i,length,auxTm,auxRun,auxSide,auxPi;
	float auxWref,auxKi,auxKp;
	unsigned char ChkSum = 0;
	char *pointer, mensaje[32], tty_buffer[40];
	char *send;

	tty_buffer[0] = '#';
	tty_buffer[1] = 1;  //para quien va
 	tty_buffer[2] = this->axis;     //origen quien lo envia	
	tty_buffer[3] = 32;    //I2C_BUF_LEN
	tty_buffer[4] = 0;     //ack 
	tty_buffer[5] = 1;     //libre
	
	if(this->axis == ALTITUDE_AXIS)	this->velocityDec = value;
	else this->velocityHA = value;

	auxWref = (float)value;

	auxRun = 1;
	auxSide = 1;
	auxPi = 1;

	if(value == 0) auxRun = 0;

	msgSend.Wref_Lo = bytefix(auxWref,0);
	msgSend.Wref_Hi = bytefix(auxWref,1);
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
	if(auxRun) msgSend.MtrCtrl += 1;
	if(auxSide) msgSend.MtrCtrl += 2;
	if(auxPi) msgSend.MtrCtrl += 4;

	length = (int)sizeof(msgSend);
	pointer = (char *) & msgSend;
	

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
}
