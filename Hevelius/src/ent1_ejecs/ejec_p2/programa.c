#include <stdio.h>
#include <stdlib.h>
#include "conversor.h"
#include "verificador.h"
#include "presetting.h"
#define MAX 100

int main()
{
    char entrada[MAX],tipo_c;
    int i;
    double coord[2]; //coord0 = radec/altitude, coord1 = declination/azimuth
    fgets(entrada,MAX,stdin);
    sscanf(entrada,"%c",&tipo_c);
    for(i = 0; i<2; i++)
    {
        fgets(entrada, MAX, stdin);
        sscanf(entrada, "%lf",&coord[i]); 
    }
    if(tipo_c=='R')
    {
        if(!verificar(coord[0],coord[1]))
        {
            fprintf(stderr,"Las coordenadas ingresadas son incorrectas\n");
            return 1;
        }
        convertir(&coord[0],&coord[1]);
    }
    else
      if (tipo_c != 'H')
      {
          fprintf(stderr,"La letra ingresada no corresponde a sistemas de coordenadas\n");
          return 1;
      }
    if(!validar(&coord[0], &coord[1]))
    {
        fprintf(stderr,"Las coordenadas son inaccesibles en este momento\n");
        return 1;;
    }
    ps_OpenGL(coord[0],coord[1]);

    //ps_TelACS(coord[0],coord[1]);
    return 0;
}
