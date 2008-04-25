#include "Telescope.h"

Telescope::Telescope(bool isLocal)
{
	this->isLocal = isLocal;
	this->serial   = "/dev/ttyS0";
	this->serialbk = "/dev/ttyS0.bk";
}

Telescope::~Telescope()
{
	if( this->isLocal ) {
		close(this->fdm);
		close(this->fds);
		unlink(this->serial);
		rename(this->serialbk,serial);
	}
	else {
		close(this->fds);
	}
}

void Telescope::parseInstructions()
{
}

int Telescope::start()
{
	char *slavename;
	//char buf;
	static struct termios termorig;  /* static for zero's */

	if( this->isLocal ) {
		if( (this->fdm = open("/dev/ptmx", O_RDWR)) < 0)
			return -1;
		printf("fdm: %d\n", this->fdm);
		grantpt(this->fdm); // change permission of slave
		unlockpt(this->fdm); // unlock slave
		setsid();
		if( (slavename = ptsname(this->fdm)) == NULL) // get name of slave
		{
			close(this->fdm);
			return -1;
		}
		if( (this->fds = open(slavename, O_RDWR)) < 0) // open slave
		{
			close(this->fdm);
			return -1;
		}
		rename(this->serial,this->serialbk);
		if(symlink(slavename,this->serial)!=0)
			printf("Unable\n");
		chmod(slavename, S_IRWXU|S_IRWXG|S_IRWXO);
		printf("fds: %d\n", this->fds);
		ioctl(this->fds, I_PUSH, "ptem"); // push ptem
		ioctl(this->fds, I_PUSH, "ldterm"); // push ldterm
		if (ioctl(this->fds, TCGETS, &termorig) == -1) {
			perror("ioctl TCGETS failed");
			return -1;
		}
		termorig.c_lflag = 0;
		(void) ioctl(this->fds, TCSETS, &termorig);
	}

	else {
		slavename = (char *)this->serial;
		this->fds = open(slavename,O_RDWR);
	}

	printf("%s\n\n", slavename);

	this->parseInstructions();
	return 0;
}
