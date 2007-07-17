#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#include "conversor.h"
#include "verificador.h"

void convertir(double *RA, double *DEC)
{
    struct tm *datetime;
    time_t t;
    int an_o, mes, dia, hora, min, sec;
    double jt, jd, MST, LMST, LON, LAT, HA, ALT, AZ;
    
    //Obtener el datetime actual, se puede cambiar por un metodo con NTP.
    t = time(NULL);
    datetime = localtime(&t);
    
    //Todas los datos de tiempo que vienen a continuacion
    //son con respecto a la fecha en que se quiere hacer la
    //observacion.
    an_o = datetime->tm_year + 1900;
    mes = datetime->tm_mon + 1;
    dia = datetime->tm_mday;
    hora = datetime->tm_hour;
    min = datetime->tm_min;
    sec = datetime->tm_sec;    
    //--------------------------------------------------//
    
    //Estas direcciones qud se muestran son de la posicion
    //fisica del telescopio(Ingresadas o por otro Sistema).
    LON = -71.5; //Longitud +Este -Oeste
    LAT = -33; //Latitud +Norte -sur
    //--------------------------------------------------//

    //Conversion de grados a reales.    
    //*RA =15*(*RA);
    
    //Por formula si es el primer o segundo mes hay que hacer como que fuera
    //el año anterior con mas meses. Seguramente por años bisiestos...
    if(mes == 1 || mes == 2)
    {
        mes += 12;
        an_o -= 1;
    }    
    jd = (int)(an_o/100.0); //Cada 100 años se resta 1 dia.
    jd = 2 - jd + (int)(jd/4.0); //Cada 4 años se suma 1 dia. La base no se xke.
    jd += (int)(365.25*an_o); //Se cuentan los dias de cada año.
    jd += (int)(30.6001*(mes+1)); //Se le suman los dias de los meses que faltan.
    jd += dia - 730550.5; //Se suman los dias y se resta una constante.
    jd += (hora + min/60.0 + sec/3600.0)/24.0; //Se suman fracciones de dia.

    //Con esto se obtienen los Julian Days contando desde Epoch J2000.0
    jt = jd/36525; //Se obtienen Julian Centuries.
    
    MST = 280.46061837 + 360.98564736629*jd; //Se obtiene una buena aprox.
    MST += 0.000387933*jt*jt - jt*jt*jt/38710000; //Mas exacto.
    //Esto es el Mean Sidereal Time... Formula.
    
    while(MST>360)
        MST -= 360;
    while(MST<0)
        MST += 360;
    //MST se deja entre 0 y 360
        
    LMST = MST + LON; //Se centra el MST en el punto de observacion.
    HA = LMST - (*RA); //Se obtiene Hour Angle.
    if(HA < 0)
        HA += 360;//Tiene que ser positivo.

    //Se obtiene la altitud en grados.
    ALT = sin((*DEC)*PI/180)*sin(LAT*PI/180);
    ALT += cos((*DEC)*PI/180)*cos(LAT*PI/180)*cos(HA*PI/180);
    ALT = asin(ALT)*180/PI;

    //Se obtiene el Azimuth en grados
    AZ = sin((*DEC)*PI/180) - sin(ALT*PI/180)*sin(LAT*PI/180);
    AZ /= cos(ALT*PI/180)*cos(LAT*PI/180);
    AZ = acos(AZ)*180/PI;
    //Si es que el Sin de Hour Angle es mayor que 0, entonces Azimuth cambia.
    if (sin(HA*PI/180) > 0)
        AZ = 360 - AZ;
    *RA = ALT;
    *DEC = AZ;

}

int verificar(double RA, double DEC)
{
    if((0<= RA && RA<= 24*15) &&(-90 <= DEC && DEC <= 90))
        return 1;
    return 0;
}

int validar(double *ALT, double *AZ)
{
    while(*ALT > 360)
        *ALT -= 360;
    while(*AZ > 360)
        *AZ -= 360;
    while(*ALT < 0)
        *ALT += 360;
    while(*AZ < 0)
        *AZ += 360;
    if(1<= *ALT && *ALT <= 179)
{
        if (*ALT > 90)
        {
            *AZ = 180 + *AZ;
            *ALT = 180 - *ALT;
        } 
        return 1;
}
    return 0;
}
