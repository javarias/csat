/*
	##############################################
	# 		 Daniel Winkler		     #
	# Estudiante de Ingeniería Civil Informática #
	#  Universidad Técnica Federico Santa María  #
	##############################################
*/

#ifndef CCD_H
#define CCD_H

// Library included		function that needs the library
#include <fcntl.h>		//open()
#include <unistd.h>		//close(), stat()
#include <stdio.h>		//printf()
#include <stdlib.h>		//exit()
#include <sys/types.h>		//stat()
#include <sys/stat.h>		//stat()
#include <errno.h>		//errno
#include <string.h>		//strerror(), memset()
#include <sys/ioctl.h>		//ioctl()
#include <asm/types.h>  	//for videodev2.h
#include <linux/videodev2.h>	//VIDIOC_QUERYCAP

/* Private ioctl's for control settings supported by some image sensors */
#define SN9C102_V4L2_CID_DAC_MAGNITUDE (V4L2_CID_PRIVATE_BASE + 0)
#define SN9C102_V4L2_CID_GREEN_BALANCE (V4L2_CID_PRIVATE_BASE + 1)
#define SN9C102_V4L2_CID_RESET_LEVEL (V4L2_CID_PRIVATE_BASE + 2)
#define SN9C102_V4L2_CID_PIXEL_BIAS_VOLTAGE (V4L2_CID_PRIVATE_BASE + 3)
#define SN9C102_V4L2_CID_GAMMA (V4L2_CID_PRIVATE_BASE + 4)
#define SN9C102_V4L2_CID_BAND_FILTER (V4L2_CID_PRIVATE_BASE + 5)
#define SN9C102_V4L2_CID_BRIGHT_LEVEL (V4L2_CID_PRIVATE_BASE + 6)
#define SN9C102_V4L2_CID_LOW_RESET_LEVEL_COUNT (V4L2_CID_PRIVATE_BASE + 7)
#define SN9C102_V4L2_CID_HIGH_RESET_LEVEL_COUNT (V4L2_CID_PRIVATE_BASE + 8)


#define CLEAR(x) memset (&(x), 0, sizeof (x))  //fills the memory area pointed to by x with the constant byte 0

struct buffer
{
        void * start;
        size_t length;
};

struct ccd {
        int fd; //file descriptor returned by open
        struct buffer * buffers;
        char *dev_name;  //device to be opened
};

//send error
static inline void errno_exit (const char *name_error)
{
        fprintf (stderr, "%s error %d, %s\n", name_error, errno, strerror (errno));
        exit (EXIT_FAILURE);
}

void init(char *device, struct ccd *cam);
int read_frame(struct ccd *cam);
void process_image(const void *p,unsigned char *dst);
void free_ccd(struct ccd *cam);
int  get_control(struct ccd *cam, int control_id); 
void change_control(struct ccd *cam,int control_id,int value);
void check_controls(struct ccd *cam);
void *control_reset_level(void *args);

#endif
