#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

int main ()
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
    printf("dia %d %d %d %d %d %d",an_o, mes, dia, hora, min, sec);
    return 0;
}
