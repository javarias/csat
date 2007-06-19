#ifndef _DEVIO_RS232_H_
#define _DEVIO_RS232_H_
/*******************************************************************************
* OAN - Observatorio Astronómico Nacional
* (c) Observatorio Astronómico Nacional, 2004
* Copyright by OAN,  All rights reserved
*
* This code is under GNU General Public Licence (GPL).
* Correspondence concerning ALMA Software should be addressed to:
* alma-sw-admin@nrao.edu
*
* "@(#) $Id: DevIORS232.h,v 1.5 2004/03/22 15:04:43 vicente Exp $"
*
* who          when        what
* --------     --------    ----------------------------------------------
* rbolano      2004-01-21  Created
* p.devicente  2004-03-18  Cleanup
*
*/

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <baciDevIO.h>
#include <serialport_rs232.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

NAMESPACE_USE(baci)

template <class T> class DevIORS232 : public DevIO<T>
{
	public:
		DevIORS232(char * dev, SerialRS232::baudrates speed = SerialRS232::b9600, SerialRS232::parities parity = SerialRS232::noparity, SerialRS232::databits databit = SerialRS232::data8, SerialRS232::stopbits stopbit = SerialRS232::stop1, int cend = 10, unsigned int buffer = 1024, int timeout = 300) 
		{
			sp = new SerialRS232(dev, speed, parity, databit, stopbit, cend, buffer, timeout);
			ACS_SHORT_LOG ((LM_DEBUG, "Port opened: %s", dev));
		}
		
		virtual ~DevIORS232() 
		{
			delete sp;
			ACS_SHORT_LOG ((LM_DEBUG, "Port closed"));
		}
		
		virtual bool initalizeValue(){ return true; }
		
		virtual T read(int& errcode, unsigned long long& timestamp)
		{
			const char delimit[]=",\r\n";
			char aux[80];
			char *token;
			char *lectura;
			
			{
			ThreadSyncGuard guard(&m_serialAccessMutex);
			sp->write_RS232("WS");
			sleep(4);
			strcpy(aux, sp->read_RS232());
			}
			token = strtok(aux, delimit);
			token = strtok(NULL, delimit);
			lectura = (char *)malloc(strlen(token)+1);
			strcpy(lectura, token);
			ACS_SHORT_LOG ((LM_DEBUG, "Retrieved: %s", lectura));
			m_value = atof(lectura);
			free(lectura);
			errcode = 0;
			timestamp = getTimeStamp();
			return m_value;
		}
		
		virtual void write(const T& value, int& errcode, unsigned long long& timestamp)
		{
			ACS_SHORT_LOG ((LM_DEBUG, "Cannot write!"));
			errcode = 0;
			timestamp = getTimeStamp();
			m_value = value;
		}
		
	private:
		T m_value;
		SerialRS232 * sp;
		BACIMutex m_serialAccessMutex;
};

#endif
