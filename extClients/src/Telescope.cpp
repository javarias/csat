#include "Telescope.h"

Telescope::Telescope(bool isLocal)
{
	this->isLocal = isLocal;
	this->serial   = "/dev/ttyS0";
	this->serialbk = "/dev/ttyS0.bk";
	this->run = true;
	this->move = false;
	this->csatC = new CSATClient();
	this->cscRun = false;
	this->cssRun = false;
	this->fdm = 0;
	this->fds = 0;
}

Telescope::~Telescope()
{
	this->csatC->stop();
	delete this->csatC;
	if( this->isLocal ) {
		if(this->fdm > 0)
			close(this->fdm);
		if(this->fds > 0)
			close(this->fds);
		if(this->move){
			unlink(this->serial);
			rename(this->serialbk,serial);
		}
	}
	else {
		if(this->fdm > 0)
			close(this->fdm);
	}
}

void Telescope::parseInstructions()
{
}

int Telescope::start()
{
	char *slavename;
	struct stat buf;
	static struct termios termorig;  /* static for zero's */

	if( this->isLocal ) {
		if( (this->fdm = open("/dev/ptmx", O_RDWR)) < 0)
			return -1;
		printf("fdm: %d\n", this->fdm);
		grantpt(this->fdm); // change permission of slave
		unlockpt(this->fdm); // unlock slave
		setsid();
		if( (slavename = ptsname(this->fdm)) == NULL) // get name of slave
			return -1;
		lstat(this->serial, &buf);
		if ((buf.st_mode & S_IFMT) == S_IFLNK){
			printf("Device is a Symlink, check it.\n");
			return -1;
		}
		if( (this->fds = open(slavename, O_RDWR)) < 0) // open slave
			return -1;
		if(!strcmp(this->serial,slavename)){
			rename(this->serial,this->serialbk);
			this->move = true;
			if(symlink(slavename,this->serial)!=0)
				printf("Unable to create Symlink\n");
		}
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
		this->fdm = open(slavename,O_RDWR);
		this->configPort();
	}
	printf("%s\n\n", slavename);
	this->cscRun = (this->csatC->startCSC() == 0);
	this->cssRun = (this->csatC->startCSS() == 0);
	this->parseInstructions();
	return 0;
}

void Telescope::stop() {
	char buf = '#';
	this->run = false;
	if(this->isLocal){
		write(this->fds,&buf,1);
		write(this->fds,&buf,1);
	}
}

void Telescope::configPort() {
	struct termio config;

	config.c_cflag  = ~CSTOPB;
	config.c_cflag |= CS8;
	config.c_cflag &= ~PARENB;
	config.c_cflag |= B9600;
	ioctl (this->fds, TCSETA, &config);
}
