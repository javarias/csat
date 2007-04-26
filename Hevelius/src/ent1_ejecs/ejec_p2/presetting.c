#include <stdio.h>
#include "presetting.h"

void ps_OpenGL(double coord0,double coord1)
{
    FILE *realAzDevIO, *realAltDevIO;
    realAzDevIO=fopen("/tmp/realAzDevIO","w+");
    realAltDevIO=fopen("/tmp/realAltDevIO","w+");

    fprintf(realAzDevIO,"%lf",coord1);
    fprintf(realAltDevIO,"%lf",coord0);
    fclose(realAzDevIO);
    fclose(realAltDevIO);

}
 


//ps_TelACS(coord[0],coord[1]);

