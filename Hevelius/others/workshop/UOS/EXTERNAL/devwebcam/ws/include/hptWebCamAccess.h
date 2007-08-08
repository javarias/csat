#ifndef hptWebCamAccess_h
#define hptWebCamAccess_h
/*
 * "@(#) $Id: hptWebCamAccess.h,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: hptWebCamAccess.h,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 * Revision 1.1  2007/06/25 13:24:22  jibsen
 * External files.
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
 * Revision 2.3  2004/07/15 11:58:31  tjuerges
 * - Anstelle ACE-Threads QThreads.
 * - GUI erweitert.
 * - Freigeben der Komponente hinzugefügt.
 * - Von /dev/video1 nach /dev/video2.
 * - Viel aufgeräumt.
 *
 *
*/

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <acsutil.h>
#include <baci.h>
#include <baciDevIO.h>
#include <string>
#include <linux/videodev.h>
#include <pwc-ioctl.h>

using namespace baci;

struct webcamVideoPicture
{
   long brightness;
   long contrast;
   long gamma;
   long colour;
   long palette;
};

struct webcamVideoWindow
{
   long width;
   long height;
   long flags;
};   

struct webcamVideoCaps
{
   long maxHeight;
   long maxWidth;
   long minHeight;
   long minWidth;
};   

struct webcamVideoFrame
{
   long height;
   long width;
   long palette;
   long size;
   unsigned char *data;
};   

class webcamVideoDevice
{
	public:
	webcamVideoDevice(const std::string& deviceName);
	virtual ~webcamVideoDevice();

	int getVideoPicture(webcamVideoPicture&);
	int getVideoPicture(video_picture&) const;
	int getVideoWindow(webcamVideoWindow&);
	int getVideoWindow(video_window&);
	int getVideoCaps(webcamVideoCaps&) const;
	int getVideoFrame(webcamVideoFrame &frame);

	virtual int getVideoAutomaticGainControl(void) const=0;
	virtual int getVideoCompression(void) const=0;
	virtual int getVideoFPS(void)=0;
	virtual int getVideoAutomaticWhiteBalance(void) const=0;

	int setVideoPicture(webcamVideoPicture newVideoPicture);
	int setVideoWindow(webcamVideoWindow newVideoWindow);

	virtual int setVideoCompression(const int) const=0;
	virtual int setVideoAutomaticGainControl(const int) const=0;
	virtual int setVideoFPS(const int)=0;
	virtual int setVideoAutomaticWhiteBalance(const int) const=0;

	virtual int activate()=0;
	virtual int deactivate()=0;

	int isActive() const{return activated;};
	CORBA::Long get_actWidth(void) const{return act_width;};
	CORBA::Long get_actHeight(void) const{return act_height;};

	protected:
	std::string name;
	CORBA::Long act_width,act_height;
	unsigned int current_palette;
	int activated;
	int fd;
};   

class webcamToUCam: public webcamVideoDevice
{
	public:
	webcamToUCam(const std::string& deviceName);

	virtual int getVideoAutomaticGainControl(void) const;
	virtual int getVideoCompression(void) const;
	virtual int getVideoFPS(void);
	virtual int getVideoAutomaticWhiteBalance(void) const;

	virtual int setVideoAutomaticGainControl(const int) const;
	virtual int setVideoCompression(const int) const;
	virtual int setVideoFPS(const int);
	virtual int setVideoAutomaticWhiteBalance(const int) const;

	int activate();
	int deactivate();
};

class webcamBrightnessIO: public DevIO<CORBA::Long>
{
	public:
	webcamBrightnessIO(webcamVideoDevice *dev): device(dev){};
	virtual ~webcamBrightnessIO(){};

	virtual bool initalizeValue(){ return true; }

	CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);
        void write(const CORBA::Long &value,  unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

	private:
	webcamVideoDevice *device;
};

class webcamContrastIO: public DevIO<CORBA::Long>
{
public:
   webcamContrastIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamContrastIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);
    void write(const CORBA::Long &value,  unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamGammaIO: public DevIO<CORBA::Long>
{
public:
   webcamGammaIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamGammaIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);
    void write(const CORBA::Long &value,  unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamColourIO: public DevIO<CORBA::Long>
{
public:
   webcamColourIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamColourIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);
    void write(const CORBA::Long &value,  unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamPaletteIO: public DevIO<CORBA::Long>
{
public:
   webcamPaletteIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamPaletteIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);
    void write(const CORBA::Long &value,  unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamFPSIO: public DevIO<CORBA::Long>
{
public:
   webcamFPSIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamFPSIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);
    void write(const CORBA::Long &value,  unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamAGCIO: public DevIO<CORBA::Long>
{
public:
   webcamAGCIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamAGCIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);
    void write(const CORBA::Long &value,  unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamAWBIO: public DevIO<CORBA::Long>
{
public:
   webcamAWBIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamAWBIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);
    void write(const CORBA::Long &value,  unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamCompressionIO: public DevIO<CORBA::Long>
{
public:
   webcamCompressionIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamCompressionIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);
    void write(const CORBA::Long &value,  unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamMaxHeightIO: public DevIO<CORBA::Long>
{
public:
   webcamMaxHeightIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamMaxHeightIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamMaxWidthIO: public DevIO<CORBA::Long>
{
public:
   webcamMaxWidthIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamMaxWidthIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamMinHeightIO: public DevIO<CORBA::Long>
{
public:
   webcamMinHeightIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamMinHeightIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamMinWidthIO: public DevIO<CORBA::Long>
{
public:
   webcamMinWidthIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamMinWidthIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamActHeightIO: public DevIO<CORBA::Long>
{
public:
   webcamActHeightIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamActHeightIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamActWidthIO: public DevIO<CORBA::Long>
{
public:
   webcamActWidthIO(webcamVideoDevice *dev): device(dev){};
   virtual ~webcamActWidthIO(){};

   virtual bool initalizeValue(){ return true; }

   CORBA::Long read( unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);

private:
   webcamVideoDevice *device;
};

class webcamFrameIO: public DevIO<ACS::longSeq>
{
	public:
	webcamFrameIO(webcamVideoDevice *dev): device(dev){};
	virtual ~webcamFrameIO(){};
	virtual bool initalizeValue(){ return true; }
	ACS::longSeq read(unsigned long long& timestamp) throw (ACSErr::ACSbaseExImpl);
	void use_network_compression(CORBA::Boolean use_compression){ compression=use_compression; };

	private:
	webcamVideoDevice *device;
	bool compression;
};

#endif /* hptWebCamAccess_h */
