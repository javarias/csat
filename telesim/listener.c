#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include <termios.h>

#include "telescope.h"

#define BAUDRATE B9600

void listen(char *device) {
	char* (*commands[256])(char*);
	char cmd, *args, *out, in[256];
	int fd,res;
	struct termios *oldtio, *newtio;

	commands['E']=get_ra_dec;
	commands['Z']=get_azm_alt;
	commands['R']=goto_ra_dec;
	commands['B']=goto_azm_alt;
	commands['V']=get_version;
	commands['K']=echo;
	commands['J']=alignment_complete;
	commands['L']=goto_in_progress;
	commands['M']=cancel_goto;

	/* We open the serial port and get its attributes */
	fd = open(device, O_RDWR | O_NOCTTY);
	if( fd < 0 ){
		fprintf(stderr,"Fatal: Can't open %s for read/write\n",device);
		exit(-1);
	}

	tcgetattr(fd,oldtio);
	bzero(newtio,sizeof(newtio));

	newtio->c_cflag  = BAUDRATE | CREAD | CS8 | CLOCAL;
	newtio->c_iflag = IGNPAR | ICRNL;
	newtio->c_oflag = 0;
	newtio->c_lflag = ICANON;

	tcsetattr(fd,TCSANOW,newtio);

	while(1){
		res = read(fd,in,256);
		cmd=in[0];
		args=(char *)malloc(strlen(in)-1);
		strncpy(args,in+1,strlen(in)-1);
		out=commands[cmd](args);
		
		//escribir a puerto
		write(fd,out,strlen(out));
	
		free(out);
		free(args);
	}

	tcsetattr(fd,TCSANOW,oldtio);
	close(fd);
	return;
}

