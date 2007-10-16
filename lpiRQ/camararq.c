/*
	##############################################
	# 		 Daniel Winkler		     #
	# Estudiante de Ingeniería Civil Informática #
	#  Universidad Técnica Federico Santa María  #
	##############################################

  Information Source: 
     * Api V4L2 ->http://www.linuxtv.org/downloads/video4linux/API/V4L2_API/spec/book1.htm
     * sn-webcam sourcecode	
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
#include "bayer.h"

#define CLEAR(x) memset (&(x), 0, sizeof (x))  //fills the memory area pointed to by x with the constant byte 0

struct buffer 
{
        void *                  start;
        size_t                  length;
};
struct buffer *         buffers         = NULL;


int fd; //file descriptor returned by open
char dev_name[20] = "/dev/video0";  //device to be opened

//send error
static void errno_exit (const char *name_error)
{
        fprintf (stderr, "%s error %d, %s\n", name_error, errno, strerror (errno));

        exit (EXIT_FAILURE);
}

//open the device
static void open_device()
{
   int flag = O_RDWR; //read write flag, is the accesmode you have to put in open

   struct stat st; 

   //we see if the dev_name device exist
   if (-1 == stat (dev_name, &st)) 
   {
	fprintf (stderr, "Cannot identify '%s': %d, %s\n", dev_name, errno, strerror (errno));
	exit (EXIT_FAILURE);
   }

   
   //if it is a character device
   if (!S_ISCHR (st.st_mode)) 
   {
	fprintf (stderr, "%s is no device\n", dev_name);
	exit (EXIT_FAILURE);
   }

   fd = open(dev_name, flag);

   //if it cannot open
   if(fd==-1)
   {
	fprintf (stderr, "Cannot open '%s': %d, %s\n", dev_name, errno, strerror (errno));
	exit (EXIT_FAILURE);
   }
   else printf("It is alive\n");
}

//close the device
static void close_device()
{
   if( close(fd) == 0 )
	printf("Freedoooommm\n");
   //if the device could not close properly
   else errno_exit ("Houston we have got a problem: close"); 
}

void init_read (unsigned int buffer_size)
{
        buffers = calloc (1, sizeof (*buffers));

        if (!buffers) {
                fprintf (stderr, "Out of memory\n");
                exit (EXIT_FAILURE);
        }

	buffers[0].length = buffer_size;
	buffers[0].start = malloc (buffer_size);

	if (!buffers[0].start) {
    		fprintf (stderr, "Out of memory\n");
            	exit (EXIT_FAILURE);
	}
	else printf("Init Read Done\n");
}

//initialize the device
void init_device()
{
   struct v4l2_capability cap; //struct filled by the driver
   struct v4l2_cropcap cropcap; //to set the type field of a v4l2_cropcap structure to the respective buffer (stream) type and call the VIDIOC_CROPCAP ioctl with a pointer to this structure. Drivers fill the rest of the structure. 
   struct v4l2_crop crop;  //to set the current cropping rectangle
   struct v4l2_format fmt;  //to negotiate a data format (to engage in data exchange)
	unsigned int min;

   //obtain information about driver and hardware capabilities
   if (-1 == ioctl (fd, VIDIOC_QUERYCAP, &cap)) 
   {
	//driver is not compatible with this specification
	if (EINVAL == errno) 
        {
   	   fprintf (stderr, "%s is no V4L2 device\n", dev_name);
           exit (EXIT_FAILURE);
	} 
	else errno_exit ("VIDIOC_QUERYCAP");
   }

   //if the device supports the Video Capture interface.
   if (!(cap.capabilities & V4L2_CAP_VIDEO_CAPTURE)) 
   {
	fprintf (stderr, "%s is no video capture device\n", dev_name);
	exit (EXIT_FAILURE);
   }

   // 
   //if the device supports the read() I/O method
   if (!(cap.capabilities & V4L2_CAP_READWRITE)) 
   {
	fprintf (stderr, "%s does not support read i/o\n", dev_name);
	exit (EXIT_FAILURE);
   }

   //
   //if the device supports the streaming I/O method
   if (!(cap.capabilities & V4L2_CAP_STREAMING)) 
   {
	fprintf (stderr, "%s does not support streaming i/o\n", dev_name);
	exit (EXIT_FAILURE);
   }

   /* Select video input, video standard and tune here. */

   CLEAR (cropcap);

   cropcap.type = V4L2_BUF_TYPE_VIDEO_CAPTURE; //Type of the data stream, set by the application. Only these types are valid here: V4L2_BUF_TYPE_VIDEO_CAPTURE, V4L2_BUF_TYPE_VIDEO_OUTPUT, V4L2_BUF_TYPE_VIDEO_OVERLAY, and custom (driver defined) types with code V4L2_BUF_TYPE_PRIVATE and higher.
   //V4L2_BUF_TYPE_VIDEO_CAPTURE: Buffer of a video capture stream

   //to query the cropping limits, the pixel aspect of images and to calculate scale factors.
   if (0 == ioctl (fd, VIDIOC_CROPCAP, &cropcap))
   {
	crop.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;  //same as the cropcap.type
	crop.c = cropcap.defrect; //set to default cropping rectangle

	//to set the current cropping rectangle
	if (-1 == ioctl (fd, VIDIOC_S_CROP, &crop)) 
	{
        	switch (errno) 
		{
                	case EINVAL:
				fprintf (stderr, "Cropping not supported for the %s\n", dev_name);
                                break;
                        default:
                                fprintf (stderr, "There is a problem setting the cropping limits of the %s\n", dev_name);
                                break;
		}
	}
   } 
   else 
   {	
   	fprintf (stderr, "There is a problem quering the cropping limits of the %s\n", dev_name);
   }

   CLEAR (fmt);

   //
   //to set the format
   fmt.type                = V4L2_BUF_TYPE_VIDEO_CAPTURE;
   fmt.fmt.pix.width       = 640; 
   fmt.fmt.pix.height      = 480;
   fmt.fmt.pix.pixelformat = V4L2_PIX_FMT_SBGGR8;
   //fmt.fmt.pix.pixelformat = V4L2_PIX_FMT_YUYV;
   //fmt.fmt.pix.field       = V4L2_FIELD_INTERLACED;

   //to negotiate the format
   if (-1 == ioctl (fd, VIDIOC_S_FMT, &fmt))
                errno_exit ("VIDIOC_S_FMT");
   else printf("System up and running\n");

   	/* Buggy driver paranoia. */
	min = fmt.fmt.pix.width * 2;
	if (fmt.fmt.pix.bytesperline < min)
		fmt.fmt.pix.bytesperline = min;
	min = fmt.fmt.pix.bytesperline * fmt.fmt.pix.height;
	if (fmt.fmt.pix.sizeimage < min)
		fmt.fmt.pix.sizeimage = min;

   init_read(fmt.fmt.pix.sizeimage);

   
}

static void uninit_device(void)
{
	free (buffers[0].start);
	free (buffers);
}


void process_image(const void *p)
{
	unsigned char *dst;
	FILE *image;
	dst=(unsigned char*)malloc(640*480*3);
	image = fopen("image.rgb", "w");
	bayer2rgb24(dst, (unsigned char *)p, 640,480);
	fwrite(dst, 640*480*3, 1, image);
	fclose(image);
	/*
        //printf("\n%s\n\n", (char *)p);
	fprintf (image, "%s", (char *)p);
        fclose(image);*/
}

int read_frame(void)
{


    	if (-1 == read (fd, buffers[0].start, buffers[0].length)) 
	{
       		switch (errno) 
		{
	       		case EAGAIN:
                		return 0;

			case EIO:
				/* Could ignore EIO, see spec. */

				/* fall through */

			default:
				errno_exit ("read");
		}
	}

	printf("Frame readed yahoooooooooo!!!\n");
        
    	process_image (buffers[0].start);

	return 1;
}



int main()
{
   open_device();
   init_device(); 
   /*start_capturing();*/  

   read_frame ();

   /*stop_capturing ();*/
   uninit_device();
   close_device();
   exit (EXIT_SUCCESS);

   return 0;
}

