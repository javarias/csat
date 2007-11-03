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
#include "ccd.h"

//open the device


void free_buffers(struct ccd *cam)
{
	free (cam->buffers[0].start);
	free (cam->buffers);
}

void free_ccd(struct ccd *cam){
        free_buffers(cam);
        if(close(cam->fd) != 0 )
                errno_exit ("Cannot close the device:"); 
        free(cam->dev_name);
}


void open_device(struct ccd *cam)
{
        int flag = O_RDWR; //read write flag, is the accesmode you have to put in open

        struct stat st; 

        //we see if the dev_name device exist
        if (-1 == stat(cam->dev_name, &st)) 
        {
                fprintf (stderr, "File '%s' not found: %d, %s\n", cam->dev_name, errno, strerror (errno));
                exit (EXIT_FAILURE);
        }


        //if it is a character device
        if (!S_ISCHR (st.st_mode)) 
        {
                fprintf (stderr, "%s is not a device\n", cam->dev_name);
                exit (EXIT_FAILURE);
        }

        cam->fd = open(cam->dev_name, flag);

        //if it cannot open
        if(cam->fd==-1)
        {
                fprintf (stderr, "Cannot open '%s': %d, %s\n", cam->dev_name, errno, strerror (errno));
                exit (EXIT_FAILURE);
        }
}

void init_buffers(struct ccd *cam,unsigned int buffer_size)
{
        cam->buffers = calloc (1, sizeof (struct buffer));

        if (!cam->buffers) {
                fprintf (stderr, "Out of memory\n");
                exit (EXIT_FAILURE);
        }

        cam->buffers[0].length = buffer_size;
        cam->buffers[0].start = malloc (buffer_size);

        if (!cam->buffers[0].start) {
                fprintf (stderr, "Out of memory\n");
            	exit (EXIT_FAILURE);
	}
}

//initialize the device
void init_device(struct ccd *cam)
{
        struct v4l2_capability cap; //struct filled by the driver
        struct v4l2_cropcap cropcap; //to set the type field of a v4l2_cropcap structure to the respective buffer (stream) type and call the VIDIOC_CROPCAP ioctl with a pointer to this structure. Drivers fill the rest of the structure. 
        struct v4l2_crop crop;  //to set the current cropping rectangle
        struct v4l2_format fmt;  //to negotiate a data format (to engage in data exchange)
        unsigned int min;

        //obtain information about driver and hardware capabilities
        if (-1 == ioctl (cam->fd, VIDIOC_QUERYCAP, &cap)) 
        {
                //driver is not compatible with this specification
                if (EINVAL == errno) 
                {
                        fprintf (stderr, "%s is no V4L2 device\n", cam->dev_name);
                        exit (EXIT_FAILURE);
                } 
                else errno_exit ("VIDIOC_QUERYCAP");
        }

        //if the device supports the Video Capture interface.
        if (!(cap.capabilities & V4L2_CAP_VIDEO_CAPTURE)) 
        {
                fprintf (stderr, "%s is no video capture device\n", cam->dev_name);
                exit (EXIT_FAILURE);
        }

        // 
        //if the device supports the read() I/O method
        if (!(cap.capabilities & V4L2_CAP_READWRITE)) 
        {
                fprintf (stderr, "%s does not support read i/o\n", cam->dev_name);
                exit (EXIT_FAILURE);
        }

        //
        //if the device supports the streaming I/O method
        if (!(cap.capabilities & V4L2_CAP_STREAMING)) 
        {
                fprintf (stderr, "%s does not support streaming i/o\n", cam->dev_name);
                exit (EXIT_FAILURE);
        }

        /* Select video input, video standard and tune here. */

        CLEAR (cropcap);

        cropcap.type = V4L2_BUF_TYPE_VIDEO_CAPTURE; //Type of the data stream, set by the application. Only these types are valid here: V4L2_BUF_TYPE_VIDEO_CAPTURE, V4L2_BUF_TYPE_VIDEO_OUTPUT, V4L2_BUF_TYPE_VIDEO_OVERLAY, and custom (driver defined) types with code V4L2_BUF_TYPE_PRIVATE and higher.
        //V4L2_BUF_TYPE_VIDEO_CAPTURE: Buffer of a video capture stream

        //to query the cropping limits, the pixel aspect of images and to calculate scale factors.
        if (0 == ioctl (cam->fd, VIDIOC_CROPCAP, &cropcap))
        {
                crop.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;  //same as the cropcap.type
                crop.c = cropcap.defrect; //set to default cropping rectangle

                //to set the current cropping rectangle
                if (-1 == ioctl (cam->fd, VIDIOC_S_CROP, &crop)) 
                {
                        switch (errno) 
                        {
                                case EINVAL:
                                        fprintf (stderr, "Cropping not supported for the %s\n", cam->dev_name);
                                        break;
                                default:
                                        fprintf (stderr, "There is a problem setting the cropping limits of the %s\n",cam->dev_name);
                                        break;
                        }
                }
        } 
        else 
        {	
                fprintf (stderr, "There is a problem quering the cropping limits of the %s\n", cam->dev_name);
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
        if (-1 == ioctl (cam->fd, VIDIOC_S_FMT, &fmt))
                errno_exit ("VIDIOC_S_FMT");

        /* Buggy driver paranoia. */
        min = fmt.fmt.pix.width * 2;
        if (fmt.fmt.pix.bytesperline < min)
                fmt.fmt.pix.bytesperline = min;
        min = fmt.fmt.pix.bytesperline * fmt.fmt.pix.height;
        if (fmt.fmt.pix.sizeimage < min)
                fmt.fmt.pix.sizeimage = min;

        init_buffers(cam,fmt.fmt.pix.sizeimage);
}


void enumerate_menu(struct ccd *cam, struct v4l2_querymenu *querymenu,struct v4l2_queryctrl *queryctrl){
        printf ("  Menu items:\n");
        memset (querymenu, 0, sizeof (*querymenu));
        querymenu->id = queryctrl->id;
        for (querymenu->index = queryctrl->minimum;
                        querymenu->index <= queryctrl->maximum;
                        querymenu->index++) {
                if (0 == ioctl (cam->fd, VIDIOC_QUERYMENU, querymenu)) {
                        printf ("  %s\n", querymenu->name);
                } else {
                        perror ("VIDIOC_QUERYMENU");
                        exit (EXIT_FAILURE);
                }
        }
}

void change_control(struct ccd *cam,int control_id,int value){
        struct v4l2_queryctrl queryctrl;
        struct v4l2_control control;

        memset (&queryctrl, 0, sizeof (queryctrl));
        queryctrl.id = control_id;

        if (-1 == ioctl (cam->fd, VIDIOC_QUERYCTRL, &queryctrl)) {
                if (errno != EINVAL) {
                        perror ("VIDIOC_QUERYCTRL");
                        exit (EXIT_FAILURE);
                } else {
                        printf ("The control id %d is not supported\n",control_id);
                }
        } else if (queryctrl.flags & V4L2_CTRL_FLAG_DISABLED) {
                        printf ("The control id %d is not supported\n",control_id);
        } else {
                memset (&control, 0, sizeof (control));
                control.id = control_id;
                control.value = value;
                if (-1 == ioctl (cam->fd, VIDIOC_S_CTRL, &control)) {
                        perror ("VIDIOC_S_CTRL");
                        exit (EXIT_FAILURE);
                }
        }
}

// Check Controls
void check_controls(struct ccd *cam){
        struct v4l2_queryctrl queryctrl;
        struct v4l2_querymenu querymenu;
        memset (&queryctrl, 0, sizeof (queryctrl));
        for (queryctrl.id = V4L2_CID_BASE;queryctrl.id < V4L2_CID_LASTP1;queryctrl.id++) {
                if (0 == ioctl (cam->fd, VIDIOC_QUERYCTRL, &queryctrl)) {
                        if (queryctrl.flags & V4L2_CTRL_FLAG_DISABLED)
                                continue;

                        printf ("Standard Control %s = %x\n", queryctrl.name,queryctrl.default_value);

                        if (queryctrl.type == V4L2_CTRL_TYPE_MENU)
                                enumerate_menu (cam,&querymenu,&queryctrl);
                } else {
                        if (errno == EINVAL)
                                continue;

                        perror ("VIDIOC_QUERYCTRL");
                        exit (EXIT_FAILURE);
                }
        }
        for (queryctrl.id = V4L2_CID_PRIVATE_BASE;queryctrl.id < V4L2_CID_PRIVATE_BASE + 7;
                        queryctrl.id++) {
                if (0 == ioctl (cam->fd, VIDIOC_QUERYCTRL, &queryctrl)) {
                        if (queryctrl.flags & V4L2_CTRL_FLAG_DISABLED)
                                continue;
                        printf ("Private Control %s = %x\n", queryctrl.name,queryctrl.default_value);

                        if (queryctrl.type == V4L2_CTRL_TYPE_MENU)
                                enumerate_menu (cam,&querymenu,&queryctrl);
                } else {
                        if (errno == EINVAL)
                                continue;

                        perror ("VIDIOC_QUERYCTRL");
                        exit (EXIT_FAILURE);
                }
        }

}


void process_image(const void *p,const char *filename)
{
	unsigned char *dst;
	FILE *image;
	dst=(unsigned char*)malloc(640*480*3);
	image = fopen(filename, "w");
	bayer2rgb24(dst, (unsigned char *)p, 640,480);
	fwrite(dst, 640*480*3, 1, image);
	fclose(image);
        free(dst);
}

int read_frame(struct ccd *cam)
{
        if (-1 == read (cam->fd, cam->buffers[0].start, cam->buffers[0].length)) 
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
        return 1;
}

void init_ccd(struct ccd *cam, const char *file){
        // Allocate the device name: the main idea is to support
        // several devices in a future
        cam->dev_name=(char *)malloc(strlen(file)+1);
        strncpy(cam->dev_name,file,strlen(file));
        // Try to open the device
        open_device(cam);
        init_device(cam);
}


int main(int argc,char *argv[])
{       
        struct ccd cam;
        if (argc!=3){
                fprintf(stdout,"USAGE: %s device output.rgb\n",argv[0]);
                exit(0);
        }

        fprintf(stdout,"Initialiting CCD...\t");
        fflush(stdout);
        init_ccd(&cam,argv[1]);
        fprintf(stdout,"[OK]\n");
        check_controls(&cam);
        //Defaults
        change_control(&cam,V4L2_CID_EXPOSURE,0x0250);
        change_control(&cam,SN9C102_V4L2_CID_GREEN_BALANCE,0x1e);
        change_control(&cam,V4L2_CID_RED_BALANCE,0x00);
        change_control(&cam,V4L2_CID_BLUE_BALANCE,0x20);
        change_control(&cam,SN9C102_V4L2_CID_RESET_LEVEL,0x30);
        change_control(&cam,SN9C102_V4L2_CID_PIXEL_BIAS_VOLTAGE,0x02);

        change_control(&cam,SN9C102_V4L2_CID_GREEN_BALANCE,0x1e);
        change_control(&cam,V4L2_CID_RED_BALANCE,0x1e);
        change_control(&cam,V4L2_CID_BLUE_BALANCE,0x1e);
        change_control(&cam,SN9C102_V4L2_CID_RESET_LEVEL,0x38);
        change_control(&cam,SN9C102_V4L2_CID_PIXEL_BIAS_VOLTAGE,0x02);
        change_control(&cam,V4L2_CID_EXPOSURE,0xf401);
        //change_control(&cam,SN9C102_V4L2_CID_RESET_LEVEL,0x3f);
        //change_control(&cam,SN9C102_V4L2_CID_PIXEL_BIAS_VOLTAGE,0x07);
        fprintf(stdout,"Grabbing Frame...\t");
        fflush(stdout);
        read_frame(&cam);
        fprintf(stdout,"[OK]\n");

        fprintf(stdout,"Saving to '%s'file...\t",argv[2]);
        fflush(stdout);
        process_image (cam.buffers[0].start,argv[2]);
        fprintf(stdout,"[OK]\n");

        fprintf(stdout,"Freeing CCD...\t");
        fflush(stdout);
        free_ccd(&cam);
        fprintf(stdout,"[OK]\n");

        exit (EXIT_SUCCESS);
        return 0;
}


