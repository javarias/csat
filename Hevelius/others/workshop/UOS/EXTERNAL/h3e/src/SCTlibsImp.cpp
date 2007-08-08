#ifndef SCT_LIBS_IMP
#define SCT_LIBS_IMP

#include "SCTlibs.h"
#include "SCTdefs.h"

#include <sys/poll.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/ioctl.h>
#include <sys/time.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>

#define LEGO_TOWER_SET_READ_TIMEOUT _IOW('u', 0xc8, int) 
#define LEGO_TOWER_SET_WRITE_TIMEOUT _IOW('u', 0xc9, int) 
#define LEGO_TOWER_READ_TIMEOUT_VALUE 100 
#define BUF_SIZE 40 

static int __sct_lego_fd = -1;

void sct_send(unsigned char *buf, int count)
{
        if (__sct_lego_fd < 0)
           return;

	unsigned char buf2[BUF_SIZE];
	int j = 0,i = 0;
	unsigned char c = 0x00;
	int nw, pos = 0, remaining = 2*count+5;

	buf2[j++] = 0x55;
	buf2[j++] = 0xff;
	buf2[j++] = 0x00;
	
        for(i = 0; (i < count) && (i < BUF_SIZE); ++i) {
		buf2[j++] = buf[i];
		buf2[j++] = ~buf[i];
		c += buf[i];
	}

	buf2[j++] = c;
	buf2[j++] = ~c;
	
	while((nw = write(__sct_lego_fd, buf2 + pos, remaining)) > 0) {
		pos += nw;
		remaining -= nw;
	}
}

int sct_receive(unsigned char *buf, int count) 
{
        if (__sct_lego_fd < 0)
           return __sct_lego_fd;

	unsigned char buf2[BUF_SIZE];
	int i=0, j=0, k=0, nr = 0, pos = 0;
	unsigned char chksum = 0;

	ioctl(__sct_lego_fd, LEGO_TOWER_SET_READ_TIMEOUT, LEGO_TOWER_READ_TIMEOUT_VALUE);
	
        while((nr = read(__sct_lego_fd, buf2, BUF_SIZE)) > 0 && pos < BUF_SIZE) {
		for(i = 0; i < nr; ++i) {
			buf[pos++] = buf2[i];
		}
	}
	
        if(buf[0] != 0x55 || buf[1] != 0xff || buf[2] != 0x00) {
		return -1;
	}
	
        for(j = 4; (j < pos-2) && (k < count); j+=2) {
		if((buf[j] | ~buf[j-1]) & 0xFF != 0xFF)
			return -1;

		buf[k++] = buf[j-1];
		chksum += buf[j-1];
	}

	if(j < pos-2) {
		return -1;
	}
	if(buf[pos-2] != chksum || (buf[pos-2] | ~buf[pos-1]) & 0xFF != 0xFF) {
		return -1;
	}
	return k;
}

/**
 * Get the value of a variable from a link to RCX.
 */
short int sct_get_variable(unsigned char variable)
{
        if (__sct_lego_fd < 0)
           return __sct_lego_fd;

	unsigned char buf_in[3], buf_out[3];
	static unsigned char mask = 0;
	int result;

        /*
	 * Ensure that commands 0x12 and 0x1A are sent interchangeably because
	 * of RCX's error correction protocol.
	 */
	mask ^= 0x08;
	buf_out[0] = 0x12 | mask;
	buf_out[1] = 0x00;
	buf_out[2] = variable;
	sct_send(buf_out, 3);
	result = sct_receive(buf_in, 3);
	if(result == 3 && (buf_in[0] | buf_out[0]) & 0xFF == 0xFF) {
		return buf_in[1] | (buf_in[2] << 8);
	}

        return -1;
}

/**
 * Set the value of a RCX variable.
 */
int sct_set_variable(int variable, short int value)
{
        if (__sct_lego_fd < 0)
           return __sct_lego_fd;

	unsigned char buf_in[5], buf_out[1];
	static unsigned char mask = 0;
        int result;

	mask ^= 0x08;
	buf_in[0] = 0x14 | mask;
	buf_in[1] = variable;
	buf_in[2] = 0x02;
	buf_in[3] = value & 0xFF;
	buf_in[4] = value >> 8;
        sct_send(buf_in, 5);
	result = sct_receive(buf_out, 1);
        if(result == 1 && (buf_out[0] | buf_in[0]) & 0xFF == 0xFF) {
           return 0;
        }
        return 1;
}

/*
 * Opens the Lego USB tower device
 * at the scpecified device file
 */
int sct_open_lego(const char *dev)
{
        if (__sct_lego_fd > 0)
              return(__sct_lego_fd);

        __sct_lego_fd = open(dev, O_RDWR);
        return(__sct_lego_fd);
}

/*
 * Opens the lego USB tower device
 * using the default LEGO_TOWER_DEV
 * device file
 */
int sct_open_lego()
{
        int fd = -1;
        
        #ifdef LEGO_TOWER_DEV
        fd = sct_open_lego(LEGO_TOWER_DEV);
        #endif

        return fd;
}

void sct_close_lego()
{
        if (__sct_lego_fd > 0) {
           close(__sct_lego_fd);
           __sct_lego_fd = -1;
        }
}

#endif
/* ___oOo___ */
