#include <iostream>
#include <string>

#include "Telescope.h"

using namespace std;

Telescope::Telescope(bool isLocal, char *serialPort)
{
	char *introot;
	this->isLocal  = isLocal;
	this->serial   = serialPort;
	this->sympath  = NULL;
	introot = getenv("INTROOT");
	this->path = new char[strlen(introot)+4];
	sprintf(path,"%s/var",introot);
	this->sympath = new char[strlen(this->path)+5];
	sprintf(this->sympath,"%s/pts0",this->path);
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
	cout << "Closing client connection... ";
	this->csatC->stop();
	cout << "done!" << endl;
	delete this->csatC;
	if( this->isLocal ) {
		if(this->fdm > 0)
			close(this->fdm);
		if(this->fds > 0)
			close(this->fds);
		if(this->move)
			unlink(this->sympath);
	}
	else 
		if(this->fdm > 0) {
			ioctl (this->fdm, TCSETA, &this->termorig);
			close(this->fdm);
		}
	delete this->path;
	delete this->sympath;
}

void Telescope::parseInstructions()
{
}

int Telescope::start()
{
	char *slavename;
	struct stat buf;

	if( this->isLocal ) {
		cout << "Working in local mode, opening slave/master ports..." << endl;
		if( (this->fdm = open("/dev/ptmx", O_RDWR)) < 0)
			return -1;
		printf("fdm: %d\n", this->fdm);
		grantpt(this->fdm); // change permission of slave
		unlockpt(this->fdm); // unlock slave
		setsid();
		if( (slavename = ptsname(this->fdm)) == NULL) // get name of slave
			return -1;
		lstat(this->serial, &buf);
		if ((buf.st_mode & S_IFMT) == S_IFLNK)
			printf("Device is a Symlink, check it.\n");
		if( (this->fds = open(slavename, O_RDWR)) < 0) // open slave
			return -1;
		if( lstat( this->path, &buf ) ){
			if( mkdir( this->path, S_IRWXU|S_IRWXG|S_IRWXO ) ){
					printf("Unable to create %s directory\n", this->path);
					return -1;
			}
		}
		else
		{
			if ( !S_ISDIR(buf.st_mode)){
				printf("A file with the name \"%s\" exists. Please delete it.\n", this->path);
				return -1;
			}
		}
		chmod(slavename, S_IRWXU|S_IRWXG|S_IRWXO);
		printf("fds: %d\n", this->fds);
		ioctl(this->fds, I_PUSH, "ptem"); // push ptem
		ioctl(this->fds, I_PUSH, "ldterm"); // push ldterm
		if (ioctl(this->fds, TCGETS, &this->termorig) == -1) {
			perror("ioctl TCGETS failed");
			return -1;
		}
		this->termorig.c_lflag = 0;
		(void) ioctl(this->fds, TCSETS, &this->termorig);
	}
	else {
		cout << "Working in non-local mode" << endl;
		slavename = (char *)this->serial;
		this->fdm = open(slavename,O_RDWR | O_NDELAY);
		this->configPort();
	}
	this->move = true;
	if(symlink(slavename,this->sympath)!=0){
		printf("Unable to create Symlink\n");
		return -1;
	}
	if(this->csatC->getStatus() != 0){
		cout << "Using port '" << slavename << "' with symlink '" << sympath << "' for listening to commmands" << endl;
		cout << "Getting CSATControl reference... ";
		this->cscRun = (this->csatC->startCSC() == 0);
		if(this->cscRun)
			cout << "done!" << endl;
		else
			cout << "failed!" << endl;
		cout << "Getting CSATStatus reference... ";
		this->cssRun = (this->csatC->startCSS() == 0);
		if(this->cssRun)
			cout << "done!" << endl;
		else
			cout << "failed!" << endl;
		if(this->cssRun && this->cscRun){
			cout << "Ready and listening commands..." << endl;
			this->parseInstructions();
		}
	}
	else
		cout << "Couldn't find manager." << endl;
	return 0;
}

void Telescope::stop() {
	char buf = '#';
	this->run = false;
	if(this->isLocal){
		write(this->fds,&buf,1);
		write(this->fds,&buf,1);
	}
	cout << "Done stoping telescope" << endl;
}

void Telescope::configPort() {
	struct termio config;

	fcntl(this->fdm, F_SETFL, fcntl(this->fdm, F_GETFL, 0) & ~O_NDELAY);

	ioctl( this->fdm, TCGETA, &this->termorig);
	config.c_lflag &= ~(ICANON | ISIG | ECHO);
	config.c_iflag &= ~(BRKINT | IGNPAR | PARMRK | INPCK | ISTRIP | INLCR | IGNCR | ICRNL | IUCLC | IXON | IXANY | IXOFF);
	config.c_iflag |= IGNBRK;
	config.c_oflag &= ~OPOST;

	config.c_cc[VMIN] = 1;
	config.c_cc[VTIME] = 1;
	config.c_cflag  = ~CSTOPB;
	config.c_cflag |= CS8;
	config.c_cflag &= ~PARENB;
	config.c_cflag |= B9600;
	ioctl (this->fdm, TCSETA, &config);
}
