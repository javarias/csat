/*
 * "@(#) $Id: hptWebCamAccess.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: hptWebCamAccess.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 * Revision 1.1  2007/06/25 13:24:22  jibsen
 * External files.
 *
 * Revision 1.4  2007/01/09 12:41:11  gchiozzi
 * Fixed problem with compressions. zlib was not linked.
 * Removed periodic logs posted at each frame.
 *
 * Revision 1.3  2007/01/09 10:28:59  gchiozzi
 * Done basic porting to ACS 6.0 and RH-Enterprise/Scientific Linux 4.
 * Both the webcam Component and the GUI work, just with basic features:
 * - 640x480 resolution
 * - no compression.
 * There are a number of things to do:
 * - error handling, in particular for devIOs to be redone
 * - check why compression does not work (some code commented out by GCH.
 * - check why switching to 320x200 does not work.
 * - name of device hardcode in the GUI
 * Some general cleanup is needed.
 *
 * Revision 1.2  2004/07/20 15:56:06  tjuerges
 * - Added latest kernel modules for Philips ToUCam PWC-740K model, including decompressor library (binary only) which allows the camera to take frames of 640*480 at 15 fps.
 *
 * Revision 1.2  2004/07/20 12:48:44  tjuerges
 * - hptWebCamClient: Fehler in statischer Speichallozierung behoben im Frame-Abholen. Es gab Probleme auf manchen Maschinen, die beim Allozieren das Programm abgeborchen haben.
 * - hptWebCam.idl: get_frame nun mit (in boolean); zeigt an, ob auf Server Daten per zlib komprimiert werden sollen.
 * - GUI: Schönheitsarbeiten + prozentuale Anzeige der Kamerawerte.
 *
 * Revision 1.1  2004/07/16 15:17:41  tjuerges
 * - Jetzt mit Sources des Kernel-Modules. Aufgesplittet in lcu/ws.
 *
 * Revision 2.5  2004/07/15 11:57:50  tjuerges
 * - Anstelle ACE-Threads QThreads.
 * - GUI erweitert.
 * - Freigeben der Komponente hinzugefügt.
 * - Von /dev/video1 nach /dev/video2.
 * - Viel aufgeräumt.
 *
 *
*/

#include <hptWebCamAccess.h>
#include <zlib.h>
#include <cstdio>
#include <linux/version.h>
#include <iostream>

webcamVideoDevice::webcamVideoDevice(const std::string& deviceName)
	:name(deviceName),activated(0),fd(-1)
{
	ACS_SHORT_LOG((LM_INFO,"webcamVideoDevice::webcamVideoDevice: Loading kernel modules"));
	try
	{
		const char* introot=ACE_OS::getenv("INTROOT");
		if(introot==NULL)
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::webcamVideoDevice: Webcam module NOT loaded due to missing $INTROOT!"));
			return;
		}
		ACE_OS::system("/sbin/modprobe videodev");

		std::string insmod("/sbin/insmod -f ");
		insmod+=introot;
#if KERNEL_VERSION(2,4,20)==LINUX_VERSION_CODE
		insmod+="/lib/pwc-9.o";
#elif KERNEL_VERSION(2,6,0)==LINUX_VERSION_CODE
		insmod+="/lib/pwc-9.ko";
#endif
		insmod+=" fps=";
		char char_dummy[10];
		std::sprintf(char_dummy,"%d",FRAMES_PER_SECOND);
		insmod+=char_dummy;
		insmod+=" size=vga compression=";
		std::sprintf(char_dummy,"%d",COMPRESSION);
		insmod+=char_dummy;
		insmod+=" dev_hint=";
		/* Erase the text "/dev/video" in the name string of the device
		 * in order to get the device number.
		 */
		std::string dummy(name);
		dummy.erase(std::size_t(0),std::size_t(10));
 		insmod+=dummy;
		ACE_OS::system(insmod.c_str());
	
		insmod="/sbin/insmod -f ";
		insmod+=introot;
#if KERNEL_VERSION(2,4,20)==LINUX_VERSION_CODE
		insmod+="/lib/pwcx.o";
#elif KERNEL_VERSION(2,6,0)==LINUX_VERSION_CODE
		insmod+="/lib/pwcx.ko";
#endif
		ACE_OS::system(insmod.c_str());

		std::string chmod("/bin/chmod 777 ");
		chmod+=name;
		ACE_OS::system(chmod.c_str());
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_WARNING,"webcamVideoDevice::webcamVideoDevice: It seems that there is a problem either to load kernel modules or to chmod the video device (%s) permissions.",name.c_str()));
	}	

//	ACS_SHORT_LOG((LM_INFO,"webcamVideoDevice::webcamVideoDevice: sleep for 4 seconds"));
//	ACE_OS::sleep(4);
}

webcamVideoDevice::~webcamVideoDevice(void)
{
	try
	{
		if(isActive())
		{
			activated=0;
			ACE_OS::close(fd);
		}
		ACS_SHORT_LOG((LM_INFO,"webcamVideoDevice::webcamVideoDevice: unloading kernel modules..."));
// GCH 2007-01-09: commented out for the time being 
//		ACE_OS::system("/sbin/rmmod pwcx pwc-9 videodev");
//		ACS_SHORT_LOG((LM_INFO,"webcamVideoDevice::~webcamVideoDevice: The device is destroyed, The End."));
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::~webcamVideoDevice: Exception!"));
	};
}

int webcamVideoDevice::getVideoWindow(webcamVideoWindow &videoWindow)
{
	int ret;
	struct video_window vw;

	try
	{
		ret=ioctl(fd,VIDIOCGWIN,&vw);
		if(ret>=0)
		{
			videoWindow.width=act_width=vw.width;
			videoWindow.height=act_height=vw.height;
			videoWindow.flags=vw.flags;
		}
		else
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::getVideoWindow: Problem with ioctl! Ignoring it."));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::getVideoWindow: Exception in ioctl!"));
		ret=-1;
	}
	return ret;
}

int webcamVideoDevice::getVideoWindow(video_window &videoWindow)
{
	int ret;
	struct video_window vw;

	try
	{
		ret=ioctl(fd,VIDIOCGWIN,&vw);
		if(ret>=0)
		{
			act_width=vw.width;
			act_height=vw.height;
			videoWindow=vw;
		}
		else
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::getVideoWindow: Problem with ioctl! Ignoring it."));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::getVideoWindow: Exception in ioctl!"));
		ret=-1;
	}
	return ret;
}

int webcamVideoDevice::setVideoWindow(webcamVideoWindow videoWindow)
{
	struct video_window vw;
	int ret;

	try
	{
		ret=getVideoWindow(vw);
		if(ret<0)
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::setVideoWindow: getVideoWindow failed."));
			return ret;
		}   

		vw.width=videoWindow.width;
		vw.height=videoWindow.height;

		ret=ioctl(fd,VIDIOCSWIN,&vw);
		if(ret>=0)
		{
			act_width=vw.width;
			act_height=vw.height;
			ACS_SHORT_LOG((LM_INFO,"webcamVideoDevice::setVideoWindow: videoWindow set to %d*%d.",vw.width,vw.height));
		}
		else
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::setVideoWindow: Problem with ioctl! Ignoring it."));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::setVideoWindow: Exception in ioctl!"));
		ret=-1;
	}
	return ret;
}

int webcamVideoDevice::getVideoPicture(webcamVideoPicture &videoPicture)
{
	struct video_picture vp;
	int ret;

	try
	{
		ret=ioctl(fd,VIDIOCGPICT,&vp);
		if(ret>=0)
		{
			videoPicture.brightness=vp.brightness;
			videoPicture.contrast =vp.contrast;
			videoPicture.gamma=vp.whiteness;
			videoPicture.colour=vp.colour;
			videoPicture.palette=current_palette=vp.palette;
		}
		else
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::getVideoPicture: Problem with ioctl! Ignoring it."));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::getVideoPicture: Exception ioctl!"));
		ret=-1;
	};
	return ret;
}

int webcamVideoDevice::getVideoPicture(video_picture &videoPicture) const
{
	struct video_picture vp;
	int ret;

	try
	{
		ret=ioctl(fd,VIDIOCGPICT,&vp);
		if(ret>=0)
		{
			videoPicture=vp;
		}
		else
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::getVideoPicture: Problem with ioctl! Ignoring it."));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::getVideoPicture: Exception ioctl!"));
		ret=-1;
	};
	return ret;
}

int webcamVideoDevice::setVideoPicture(webcamVideoPicture videoPicture)
{
	// These values are always in [0,65535].
 	if((videoPicture.brightness<0)||
 			(videoPicture.brightness>65535)||
 			(videoPicture.contrast<0)||
 			(videoPicture.contrast>65535)||
 			(videoPicture.gamma<0)||
 			(videoPicture.gamma>65535)||
 			(videoPicture.colour<0)||
 			(videoPicture.colour>65535)||
 			(videoPicture.palette<VIDEO_PALETTE_GREY)||
			(videoPicture.palette>VIDEO_PALETTE_YUV410P))
 		{
			ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::setVideoPicture: Problem with values. They are not in range!"));
			return -1;
 		}

	int ret(0);
	struct video_picture vp;

	try
	{
		if(getVideoPicture(vp)>=0)
		{
			vp.brightness=videoPicture.brightness;
			vp.colour=videoPicture.colour;
			vp.contrast=videoPicture.contrast;
			vp.palette=current_palette=videoPicture.palette;
			vp.hue=videoPicture.gamma;

			ret=ioctl(fd,VIDIOCSPICT,&vp);
			if(ret<0)
			{
				ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::setVideoPicture: Problem with ioctl! Ignoring it."));
			}
			else
			{
				current_palette=videoPicture.palette;
			}
		}
		else
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::setVideoPicture: Cannot set picture properties."));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::setVideoPicture: Exception!"));
		ret=-1;
	}
	return ret;
}   

int webcamVideoDevice::getVideoCaps(webcamVideoCaps &videoCaps) const
{
	struct video_capability vid_cap;
	int ret;

	try
	{
		ret=ioctl(fd,VIDIOCGCAP,&vid_cap);
		if(ret>=0)
		{
			videoCaps.minWidth=vid_cap.minwidth;
			videoCaps.maxWidth=vid_cap.maxwidth;
			videoCaps.minHeight=vid_cap.minheight;
			videoCaps.maxHeight=vid_cap.maxheight;
		}
		else
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::getVideoCaps: Problem with ioctl! Ignoring it."));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::getVideoCaps: Exception!"));
		ret=-1;
	};
	return ret;
}

int webcamVideoDevice::getVideoFrame(webcamVideoFrame &frame)
{
	int ret;

	try
	{
		struct video_window vid_win;
		ret=getVideoWindow(vid_win);
		if(ret<0)
		{
			return ret;
		}

		struct video_mbuf vid_buf;
		ret=ioctl(fd,VIDIOCGMBUF,&vid_buf);
		if(ret<0)
		{
			return ret;
		}

		unsigned char *map=static_cast<unsigned char*>(mmap(0,vid_buf.size,PROT_READ|PROT_WRITE,MAP_SHARED,fd,0));

		struct video_mmap vid_mmap;
		vid_mmap.frame=0;
		vid_mmap.width=vid_win.width;
		vid_mmap.height=vid_win.height;
		vid_mmap.format=current_palette;
 
		ret=ioctl(fd,VIDIOCMCAPTURE,&vid_mmap);
		if(ret<0)
		{
			return ret;
		}

		int i(0);
		ret=ioctl(fd,VIDIOCSYNC,&i);
		if(ret>=0)
		{
			frame.data=static_cast<unsigned char*>(malloc(vid_buf.size));

			memcpy(frame.data,map,vid_buf.size);
			frame.width=vid_win.width;
			frame.height=vid_win.height;
			frame.palette=current_palette;
			frame.size=vid_buf.size;

			munmap(map,vid_buf.size);

			act_width=frame.width;
			act_height=frame.height;
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamVideoDevice::getVideoFrame: Exception!"));
		ret=-1;
	};
	return ret;
}

webcamToUCam::webcamToUCam(const std::string& deviceName)
	:webcamVideoDevice(deviceName)
{
}

int webcamToUCam::activate()
{
	if(isActive())
	{
		return 0;
	}
	else
	{
		try
		{
			fd=ACE_OS::open(name.c_str(),O_RDWR);
			if(fd<0)
			{
				ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::activate: Device %s open failed.",name.c_str()));
				return -1;
			}
			activated=1;

			int ret=setVideoAutomaticGainControl(0);
			if(ret<0)
			{
				ACS_SHORT_LOG((LM_WARNING,"webcamToUCam::activate: Setting automatic gain control failed."));
			}
			ret=setVideoAutomaticWhiteBalance(PWC_WB_OUTDOOR);
			if(ret<0)
			{
			ACS_SHORT_LOG((LM_WARNING,"webcamToUCam::activate: Setting automatic white balance failed."));
			}
			ret=setVideoCompression(COMPRESSION);
			if(ret<0)
			{
				ACS_SHORT_LOG((LM_WARNING,"webcamToUCam::activate: Setting video compression failed."));
			}

			webcamVideoWindow vw;
			if(getVideoWindow(vw)>=0)
			{
				act_width=vw.width;
				act_height=vw.height;
			}
		}
		catch(...)
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::activate: Exception!."));
			return -1;
		}
	}
	return 0;
}

int webcamToUCam::deactivate()
{
	if(!isActive())
	{
		return -1;
	}
	else
	{
		activated=0;
		try
		{
			ACE_OS::close(fd);
		}
		catch(...)
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::deactivate: Exception in closing device!"));
			return -1;
		};
	}
	return 0;
}

int webcamToUCam::setVideoAutomaticWhiteBalance(const int value) const
{
	// White balance control: from pwc-ioctl.h
	if((value<0)||(value>65535))
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::setVideoAutoWhiteBalance: Value (%d) not in range!",value));
		return -1;
	}

	struct pwc_whitebalance pwb;
	int ret;

	try
	{
		ret=ioctl(fd,VIDIOCPWCGAWB,&pwb);
		if(ret<0)
		{
			ACS_SHORT_LOG((LM_WARNING,"webcamToUCam::setVideoAutoWhiteBalance: Getting value failed."));
			return ret;
		}
		pwb.mode=value;
		ret=ioctl(fd,VIDIOCPWCSAWB,&pwb);
		if(ret<0)
		{
			ACS_SHORT_LOG((LM_WARNING,"webcamToUCam::setVideoAutoWhiteBalance: Maybe: Setting value failed?"));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::setVideoAutoWhiteBalance: Exception!"));
		ret=-1;
	};
	return ret;
}

int webcamToUCam::getVideoAutomaticWhiteBalance(void) const
{
	struct pwc_whitebalance pwb;
	int ret;
	
	try
	{
		ret=ioctl(fd,VIDIOCPWCGAWB,&pwb);
		if(ret>=0)
		{
			ret=pwb.mode;
		}
		else
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::getVideoAutoWhiteBalance: Getting value failed."));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::getVideoAutoWhiteBalance: Exception!"));
		ret=-1;
	};
	return ret;
}

int webcamToUCam::setVideoFPS(const int value)
{
	// FPS: from pwc-ioctl.h
	if((value<1)||(value>30))
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::setVideoFPS: Value (%d) not in range!",value));
		return -1;
	}

	int v=(value<<PWC_FPS_SHIFT)&PWC_FPS_FRMASK;

	struct video_window vw;
	int ret=getVideoWindow(vw);
	if(ret<0)
	{
		return ret;
	}

	vw.flags=(vw.flags&(~PWC_FPS_FRMASK))|v;
	try
	{
		ret=ioctl(fd,VIDIOCSWIN,&vw);
		if(ret<0)
		{
			ACS_SHORT_LOG((LM_WARNING,"webcamToUCam::setVideoFPS: Maybe: Setting value failed?"));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_WARNING,"webcamToUCam::setVideoFPS: Exception!"));
		ret=-1;
	};
	return ret;
}

int webcamToUCam::getVideoFPS(void)
{
	struct video_window vw;
	
	int ret=getVideoWindow(vw);
	if(ret<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::getVideoFPS: Getting value failed."));
	}
	else
	{
		ret=(vw.flags&PWC_FPS_FRMASK)>>PWC_FPS_SHIFT;
	}
	return ret;
}

int webcamToUCam::setVideoCompression(const int value) const
{
	// Compression: from pwc-ioctl.h
	if((value<0)||(value>3))
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::setVideoCompression: Value (%d) not in range!",value));
		return -1;
	}

	int ret;
	try
	{
		ret=ioctl(fd,VIDIOCPWCSCQUAL,value);
		if(ret<0)
		{
			ACS_SHORT_LOG((LM_WARNING,"webcamToUCam::setVideoCompression: Maybe: Setting value failed?"));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::setVideoCompression: Exception!"));
		ret=-1;
	};
	return ret;
}

int webcamToUCam::getVideoCompression(void) const
{
	int value,ret;
	try
	{
		ret=ioctl(fd,VIDIOCPWCGCQUAL,&value);
		if(ret>=0)
		{
			ret=value;
		}
		else
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::getVideoCompression: Getting value failed."));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::getVideoCompression: Exception!"));
		ret=-1;
	}
	return ret;
}

int webcamToUCam::setVideoAutomaticGainControl(const int value) const
{
  // Automagic gain control: from pwc-ioctl.h 0=automagic, >0<65536 fixed.
	if((value<-65535)||(value>65535))
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::setVideoCompression: Value (%d) not in range!",value));
		return -1;
	}

	int ret;
	try
	{
		ret=ioctl(fd,VIDIOCPWCSAGC,value);
		if(ret<0)
		{
		   ACS_SHORT_LOG((LM_WARNING,"webcamToUCam::setVideoAutomaticGainControl: Maybe: Setting value failed?"));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::setVideoCompression: Exception!"));
		ret=-1;
	}
	return ret;
}

int webcamToUCam::getVideoAutomaticGainControl(void) const
{
	int value,ret;
	try
	{
		ret=ioctl(fd,VIDIOCPWCGAGC,&value);
		if(ret>=0)
		{
			ret=value;
		}
		else
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::getVideoAutomaticGainControl: Getting value failed."));
		}
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamToUCam::getVideoAutomaticGainControl: Exception!"));
		ret=-1;
	};
	return ret;
}

ACS::longSeq webcamFrameIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	ACS::longSeq val;
	webcamVideoFrame frame;
	unsigned long tmpLong(0UL);
   
	// errcode=0;

	if(device->getVideoFrame(frame)<0)
	{
		// errcode=-1;
		return val;
	}

	if(compression==true)
	{
		uLong comprLen(0UL);
		unsigned char tmp[2000000];
	   
		if(device->getVideoFrame(frame)<0)
		{
			// errcode=-1;
			return val;
		}
	
		unsigned long len(frame.size);
		if(len>2000000)
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamFrameIO::read: frame size (%d) too big (max is 2000000).",len));
			// errcode=-1;
			return val;
		}

		int j(0);
		do
		{
			tmp[j]=frame.data[j];
			j++;
		}
		while(j<static_cast<int>(len));

		comprLen=len+1;
		Byte* compr(static_cast<Byte*>(calloc((uInt)comprLen,1)));

		int err(compress(compr,&comprLen,static_cast<const Bytef*>(tmp),len));
		if(err!=Z_OK)
		{
			ACS_SHORT_LOG((LM_ERROR,"webcamFrameIO::read: Problem while compression of frame. Skipping it."));
			// errcode=-1;
			return val;
		}

		len=comprLen/4+2;
		val.length(len);
		val[0]=comprLen;

		int i(0);
		j=1;
		do
		{
			tmpLong=0;
			tmpLong=compr[i++]  << 24;
			tmpLong+=compr[i++] << 16;
			tmpLong+=compr[i++] <<  8;
			tmpLong+=compr[i++];
			memcpy(&val[j++],&tmpLong,sizeof(long));
		}
		while(j<static_cast<int>(len));
		free(compr);
	}
	else	// No compression used
	{
		int len(frame.size/4),i(0),j(0);
		val.length(len);

		do
		{
			tmpLong=0;
			tmpLong=frame.data[i++]  << 24;
			tmpLong+=frame.data[i++] << 16;
			tmpLong+=frame.data[i++] <<  8;
			tmpLong+=frame.data[i++];
			memcpy(&val[j++],&tmpLong,sizeof(long));
		}
		while(i<frame.size);
	}
	free(frame.data);
	timestamp=getTimeStamp();
	return val;
}

CORBA::Long webcamBrightnessIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoPicture vp;
	// errcode=0;
	
	if(device->getVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamBrightnessIO::read: Cannot fetch value."));
		// errcode=-1;
		timestamp=getTimeStamp();
		return -1;
	}
	timestamp=getTimeStamp();
	return vp.brightness;
}  

void webcamBrightnessIO::write(const CORBA::Long &value,unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoPicture vp;
	// errcode=0;

	if(!device->isActive())
	{
		ACS_SHORT_LOG((LM_INFO,"webcamBrightnessIO::write: Camera is not active."));
		timestamp=getTimeStamp();
		// errcode=-1;
		return;
	}

	if(device->getVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamBrightnessIO::write: Cannot fetch value."));
		// errcode=-1;
		timestamp=getTimeStamp();
		return;
	}
	vp.brightness=value;

	if(device->setVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamBrightnessIO::write: Cannot set value."));
		// errcode=-2;
		timestamp=getTimeStamp();
		return;
	}
	timestamp=getTimeStamp();
}

CORBA::Long webcamGammaIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoPicture vp;
	// errcode=0;
	
	if(device->getVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamGammaIO::read: Cannot fetch value."));
		// errcode=-1;
		timestamp=getTimeStamp();
		return -1;
	}
	timestamp=getTimeStamp();
	return vp.gamma;
}  

void webcamGammaIO::write(const CORBA::Long &value,unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoPicture vp;
	// errcode=0;
	
	if(!device->isActive())
	{
		ACS_SHORT_LOG((LM_INFO,"webcamGammaIO::write: Camera is not active."));
		timestamp=getTimeStamp();
		// errcode=-1;
		return;
	}

	if(device->getVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamGammaIO::write: Cannot fetch value."));
    // errcode=-1;
    timestamp=getTimeStamp();
    return;
	}
	vp.gamma=value;

	if(device->setVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamGammaIO::write: Cannot set value."));
    // errcode=-2;
	}
	timestamp=getTimeStamp();
}

CORBA::Long webcamContrastIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoPicture vp;
	// errcode=0;

	if(device->getVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamContrastIO::read: Cannot fetch value."));
		// errcode=-1;
		timestamp=getTimeStamp();
		return -1;
	}
	timestamp=getTimeStamp();
	return vp.contrast;
}  

void webcamContrastIO::write(const CORBA::Long &value,unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoPicture vp;
	// errcode=0;

	if(!device->isActive())
	{
		ACS_SHORT_LOG((LM_INFO,"webcamContrastIO::write: Camera is not active."));
		timestamp=getTimeStamp();
		// errcode=-1;
		return;
	}

	if(device->getVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamContrastIO::write: Cannot fetch value."));
		// errcode=-1;
		timestamp=getTimeStamp();
		return;
	}
	vp.contrast=value;

	if(device->setVideoPicture(vp)<0)
	{
		// errcode=-2;
		timestamp=getTimeStamp();
		return;
	}
	timestamp=getTimeStamp();
}

CORBA::Long webcamColourIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoPicture vp;
	// errcode=0;

	if(device->getVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamColourIO::read: Cannot fetch value."));
		// errcode=-1;
		timestamp=getTimeStamp();
		return -1;
	}

	timestamp=getTimeStamp();
	return vp.colour;
}  

void webcamColourIO::write(const CORBA::Long &value,unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoPicture vp;
	// errcode=0;

	if(!device->isActive())
	{
		ACS_SHORT_LOG((LM_INFO,"webcamColourIO::write: Camera is not active."));
		timestamp=getTimeStamp();
		// errcode=-1;
		return;
	}

	if(device->getVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamColourIO::write: Cannot fetch value."));
		// errcode=-1;
		timestamp=getTimeStamp();
		return;
	}
	vp.colour=value;

	if(device->setVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamColourIO::write: Cannot set value."));
		// errcode=-2;
	}
	timestamp=getTimeStamp();
}

CORBA::Long webcamFPSIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	// errcode=0;
	timestamp=getTimeStamp();
	return device->getVideoFPS();
}  

void webcamFPSIO::write(const CORBA::Long &value,unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	// errcode=0;

	if(!device->isActive())
	{
		ACS_SHORT_LOG((LM_INFO,"webcamFPSIO::write: Camera is not active."));
		timestamp=getTimeStamp();
		// errcode=-1;
		return;
	}

	if(device->setVideoFPS(static_cast<int>(value))<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamFPSIO::write: Cannot set value."));
		// errcode=-2;
	}
	timestamp=getTimeStamp();
}

CORBA::Long webcamAGCIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	// errcode=0;
	timestamp=getTimeStamp();
	return device->getVideoAutomaticGainControl();
}  

void webcamAGCIO::write(const CORBA::Long &value,unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	// errcode=0;

	if(!device->isActive())
	{
		ACS_SHORT_LOG((LM_INFO,"webcamAGCIO::write: Camera is not active."));
		timestamp=getTimeStamp();
		// errcode=-1;
		return;
	}

	if(device->setVideoAutomaticGainControl(static_cast<int>(value))<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamAGCIO::write: Cannot set value."));
		// errcode=-2;
	}
	timestamp=getTimeStamp();
}

CORBA::Long webcamAWBIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	// errcode=0;
	timestamp=getTimeStamp();
	return device->getVideoAutomaticWhiteBalance();
}  

void webcamAWBIO::write(const CORBA::Long &value,unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	// errcode=0;

	if(!device->isActive())
	{
		ACS_SHORT_LOG((LM_INFO,"webcamAWBIO::write: Camera is not active."));
		timestamp=getTimeStamp();
		// errcode=-1;
		return;
	}

	if(device->setVideoAutomaticWhiteBalance(static_cast<int>(value))<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamAWBIO::write: Cannot set value."));
		// errcode=-2;
	}
	timestamp=getTimeStamp();
}

CORBA::Long webcamPaletteIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoPicture vp;
	// errcode=0;

	if(device->getVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamPaletteIO::read: Cannot fetch value."));
		// errcode=-1;
		timestamp=getTimeStamp();
		return -1;
	}
	timestamp=getTimeStamp();
	return vp.palette;
}  

void webcamPaletteIO::write(const CORBA::Long &value,unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoPicture vp;
	// errcode=0;

	if(!device->isActive())
	{
		ACS_SHORT_LOG((LM_INFO,"webcamPaletteIO::write: Camera is not active."));
		timestamp=getTimeStamp();
		// errcode=-1;
		return;
	}

	if(device->getVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamPaletteIO::write: Cannot fetch value."));
		// errcode=-1;
		timestamp=getTimeStamp();
		return;
	}
	vp.palette=value;

	if(device->setVideoPicture(vp)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamPaletteIO::write: Cannot set value."));
		// errcode=-2;
	}
	timestamp=getTimeStamp();
}

CORBA::Long webcamCompressionIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	// errcode=0;
	timestamp=getTimeStamp();
	return static_cast<CORBA::Long>(device->getVideoCompression());
}  

void webcamCompressionIO::write(const CORBA::Long &value,unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	// errcode=0;

	if(!device->isActive())
	{
		ACS_SHORT_LOG((LM_INFO,"webcamCompressionIO::write: Camera is not active."));
		timestamp=getTimeStamp();
		// errcode=-1;
		return;
	}

	if(device->setVideoCompression(static_cast<int>(value))<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamCompressionIO::write: Cannot set value."));
		// errcode=-2;
	}
	timestamp=getTimeStamp();
}

CORBA::Long webcamMinHeightIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoCaps vc;
	// errcode=0;

	if(device->getVideoCaps(vc)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamMinHeightIO::read: Cannot fetch value."));
		// errcode=-1;
		timestamp=getTimeStamp();
		return -1;
	}
	timestamp=getTimeStamp();
	return vc.minHeight;
}

CORBA::Long webcamMaxHeightIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoCaps vc;
	// errcode=0;

	if(device->getVideoCaps(vc)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamMaxHeightIO::read: Cannot fetch value."));
		// errcode=-1;
		timestamp=getTimeStamp();
		return -1;
	}
	timestamp=getTimeStamp();
	return vc.maxHeight;
}

CORBA::Long webcamMinWidthIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoCaps vc;
	// errcode=0;

	if(device->getVideoCaps(vc)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamMinWidthIO::read: Cannot fetch value."));
		// errcode=-1;
		timestamp=getTimeStamp();
		return -1;
	}
	timestamp=getTimeStamp();
	return vc.minWidth;
}

CORBA::Long webcamMaxWidthIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	struct webcamVideoCaps vc;
	// errcode=0;

	if(device->getVideoCaps(vc)<0)
	{
		ACS_SHORT_LOG((LM_ERROR,"webcamMaxWidthIO::read: Cannot fetch value."));
		// errcode=-1;
		timestamp=getTimeStamp();
		return -1;
	}
	timestamp=getTimeStamp();
	return vc.maxWidth;
}

CORBA::Long webcamActWidthIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	// errcode=0;
	timestamp=getTimeStamp();

	return device->get_actWidth();
}

CORBA::Long webcamActHeightIO::read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl)
{
	// errcode=0;
	timestamp=getTimeStamp();

	return device->get_actHeight();
}
