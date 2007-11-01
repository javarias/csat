/*
	##############################################
	# 		 Daniel Winkler		     #
	# Estudiante de Ingeniería Civil Informática #
	#  Universidad Técnica Federico Santa María  #
	##############################################
*/

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

