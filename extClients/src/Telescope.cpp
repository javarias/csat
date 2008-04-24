#include "Telescope.h"

Telescope::Telescope(bool isLocal)
{
	this->isLocal = isLocal;
	serial   = "/dev/ttyS0";
	serialbk = "/dev/ttyS0.bk";
}

Telescope::~Telescope()
{
	close(fdm);
	close(fds);
	unlink(serial);
	rename(serialbk,serial);
}

void Telescope::parseInstructions()
{
}

int Telescope::start()
{
	char *slavename;
	//char buf;
	static struct termios termorig;  /* static for zero's */

	if( (fdm = open("/dev/ptmx", O_RDWR)) < 0)
		return -1;
	printf("fdm: %d\n", fdm);
	grantpt(fdm); // change permission of slave
	unlockpt(fdm); // unlock slave
	setsid();
	if( (slavename = ptsname(fdm)) == NULL) // get name of slave
	{
		close(fdm);
		return -1;
	}
	if( (fds = open(slavename, O_RDWR)) < 0) // open slave
	{
		close(fdm);
		return -1;
	}
	rename(serial,serialbk);
	if(symlink(slavename,serial)!=0)
		printf("Unable\n");
	chmod(slavename, S_IRWXU|S_IRWXG|S_IRWXO);
	printf("fds: %d\n", fds);
	ioctl(fds, I_PUSH, "ptem"); // push ptem
	ioctl(fds, I_PUSH, "ldterm"); // push ldterm
	if (ioctl(fds, TCGETS, &termorig) == -1) {
		perror("ioctl TCGETS failed");
		return -1;
	}
	termorig.c_lflag = 0;
	(void) ioctl(fds, TCSETS, &termorig);
	printf("%s\n\n", slavename);

	this->parseInstructions();
	return 0;	
}
