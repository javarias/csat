#include <stdio.h>
#include <stdlib.h>
#include "conversor.h"
#include "verificador.h"

#define MAX 100

int main()
{
    char entrada[MAX];
    int i;
    double coord[2]; //coord0 = radec/altitude, coord1 = declination/azimuth
    for(i = 0; i<2; i++)
    {
        fgets(entrada, MAX, stdin);
        sscanf(entrada, "%lf",&coord[i]); 
    }
    if(!verificar(coord[0],coord[1]))
    {
        exit(0);
    }
    convertir(&coord[0],&coord[1]);
    if(!validar(coord[0], coord[1]))
    {
        printf("Aa");
        exit(0);
    }
    //ps_OpenGL(coord[0],coord[1]);
    //ps_TelACS(coord[0],coord[1]);
    return 0;
}
