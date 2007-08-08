/*
 * "@(#) $Id: hptWebCamGUI.ui.h,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: hptWebCamGUI.ui.h,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 * Revision 1.1  2007/06/25 13:24:22  jibsen
 * External files.
 *
 * Revision 1.5  2007/01/09 10:28:59  gchiozzi
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
 * Revision 1.4  2004/07/20 15:56:06  tjuerges
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
 * Revision 2.4  2004/07/15 11:57:50  tjuerges
 * - Anstelle ACE-Threads QThreads.
 * - GUI erweitert.
 * - Freigeben der Komponente hinzugefügt.
 * - Von /dev/video1 nach /dev/video2.
 * - Viel aufgeräumt.
 *
 *
*/

#include <iostream>
#include <math.h>
#include <logging.h>

#include <acserr.h>

#include <hptWebCamC.h>

#include <linux/videodev.h>
#include <pwc-ioctl.h>

#include <qapplication.h>
#include <qpushbutton.h>

#include <hptWebCamGUIFrameTaker.h>
#include <hptWebCamGUIEventLoop.h>

#include <ACSErrTypeOK.h>

extern hptWebCam::WebCam_var dev;
extern frameTaker* myframeTaker;
extern eventLoop* myeventLoop;

int current_palette;
int current_resolution;
int current_width,current_height;

void hptWebCamGUI::init()
{
}

void hptWebCamGUI::destroy()
{
}

void hptWebCamGUI::cameraOn()
{
	if(CORBA::is_nil(dev))
  {
	  ACS_SHORT_LOG((LM_ERROR,"hptWebCamGUI::cameraOn: The camera container seems to be not active!"));
    return;
  }

  try
  {
    ACSErr::Completion_var c=dev->on();
    if(c->code==ACSErrTypeOK::ACSErrOK)
    {
    	newResolution();
    	//Init the device with the GUI values
    	newContrast(Contrast->value());
			newColour(Colour->value());
			newBrightness(Brightness->value());
			newGamma(Gamma->value());
			newAGC(AGC->value());
			newWhiteBalance(WhiteBalance->currentItem());
			newCompression(Compression->currentItem());
			newFPS(FPS->value());
			current_palette=dev->palette()->get_sync(c.out());
			if(current_palette==VIDEO_PALETTE_YUV420P)
			{
				Palette->setCurrentItem(0);
			}
			else
			{
				Palette->setCurrentItem(1);
			}
			ACS_SHORT_LOG((LM_INFO,"hptWebCamGUI::cameraOn: Switching Webcam on done."));
		}
    else
		{
		  ACS_SHORT_LOG((LM_ERROR,"hptWebCamGUI::cameraOn: Switching the camera on did not work!"));
		}
  }
  catch(...)
  {
    ACS_SHORT_LOG((LM_ERROR,"hptWebCamGUI::cameraOn: Something weird happended during setup of the camera."));
  }
}

void hptWebCamGUI::cameraOff()
{
	myframeTaker->stop();

  try
  {
    dev->off();
	  ACS_SHORT_LOG((LM_INFO,"hptWebCamGUI::cameraOff: Webcam switched off."));
  }
  catch(...)
  {
    ACS_SHORT_LOG((LM_ERROR,"hptWebCamGUI::cameraOff: Something weird happended during shutdown of the camera."));
  }
}

void hptWebCamGUI::takeFramesOn()
{
	if(!myframeTaker->running())
	{
	  myframeTaker->start();
	}
}

void hptWebCamGUI::takeFramesOff()
{
	myframeTaker->stop();
}

void hptWebCamGUI::newResolution()
{
  qApp->lock();
  current_resolution=resolutionBox->currentItem();
	if(current_resolution==0)
	{
		FPS->setMaxValue(30);
		current_width=320;
		current_height=240;
	}
	else
	{
		FPS->setMaxValue(15);
		current_width=640;
		current_height=480;
	}
  dev->setFrameSize(current_width,current_height);
  qApp->unlock();
}

void hptWebCamGUI::newCompression(int value)
{
	dev->compression()->set_sync(value);
}

void hptWebCamGUI::newWhiteBalance(int value)
{
	long v;
	switch(value)
	{
		case 0:
			v=PWC_WB_INDOOR;
			break;
		case 1:
			v=PWC_WB_OUTDOOR;
			break;
		case 2:
			v=PWC_WB_FL;
			break;
		case 3:
			v=PWC_WB_MANUAL;
			break;
		case 4:
		default:
			v=PWC_WB_AUTO;
			break;
	}
	dev->automaticWhiteBalance()->set_sync(v);
}

void hptWebCamGUI::newBrightness(int value)
{
	dev->brightness()->set_sync(static_cast<int>(floor(static_cast<float>(value)*65535.0/100.0+0.5)));
}

void hptWebCamGUI::newColour(int value)
{
	dev->colour()->set_sync(static_cast<int>(floor(static_cast<float>(value)*65535.0/100.0+0.5)));
}

void hptWebCamGUI::newGamma(int value)
{
	dev->gamma()->set_sync(static_cast<int>(floor(static_cast<float>(value)*65535.0/100.0+0.5)));
}

void hptWebCamGUI::newContrast(int value)
{
	dev->contrast()->set_sync(static_cast<int>(floor(static_cast<float>(value)*65535.0/100.0+0.5)));
}

void hptWebCamGUI::newAGC(int value)
{
	dev->automaticGainControl()->set_sync(static_cast<int>(floor(static_cast<float>(value)*65535.0/100.0+(value>=0?0.5:-0.5))));
}

void hptWebCamGUI::newFPS(int value)
{
	dev->fps()->set_sync(value);
}

void hptWebCamGUI::newPalette(int value)
{
	long v(0L);
	switch(value)
	{
		case 1:
			v=current_palette=VIDEO_PALETTE_RAW;
			break;
		default:
			v=current_palette=VIDEO_PALETTE_YUV420P;
			break;
	};
	dev->palette()->set_sync(v);
}

void hptWebCamGUI::cameraReset()
{
  ACSErr::Completion_var c=dev->reset();
  if(c->code!=ACSErrTypeOK::ACSErrOK)
  {
		ACS_SHORT_LOG((LM_ERROR,"hptWebCamGUI::cameraReset: Resetting the camera failed!"));
	}
}

void hptWebCamGUI::byebye()
{
	myframeTaker->stop();
	ACS_SHORT_LOG((LM_INFO,"hptWebCamGUI::byebye: Waiting approx. 5s for the event loop to finish."));
	myeventLoop->stop();
	myeventLoop->wait();
	close();
}
