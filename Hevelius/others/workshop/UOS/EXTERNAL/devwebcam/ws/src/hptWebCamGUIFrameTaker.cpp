/*
 * "@(#) $Id: hptWebCamGUIFrameTaker.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: hptWebCamGUIFrameTaker.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 * Revision 1.1  2007/06/25 13:24:22  jibsen
 * External files.
 *
 * Revision 1.1  2004/07/20 15:56:06  tjuerges
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
 * Revision 2.1  2004/07/15 11:57:50  tjuerges
 * - Anstelle ACE-Threads QThreads.
 * - GUI erweitert.
 * - Freigeben der Komponente hinzugefügt.
 * - Von /dev/video1 nach /dev/video2.
 * - Viel aufgeräumt.
 *
 *
*/

#include <hptWebCamGUIFrameTaker.h>
#include <hptWebCamGUI.h>

#include <hptWebCamC.h>

#include <qapplication.h>
#include <qpainter.h>
#include <qimage.h>
#include <qpixmap.h>
#include <qlcdnumber.h>
#include <qcombobox.h>
#include <qcheckbox.h>
#include <qdatetime.h>

#include <iostream>

#include <ccvt.h>
#include <ccvt_types.h>
#include <zlib.h>

extern hptWebCam::WebCam_var dev;
extern int current_resolution;
extern int current_width,current_height;

void frameTaker::setGUI(hptWebCamGUI* theGUI)
{
	tGUI=theGUI;
}

void frameTaker::run(void)
{
	theEnd=false;

  unsigned char* tmp=new unsigned char[2000000];
  unsigned char* tmp2=new unsigned char[2000000];
	if((tmp==0)||(tmp2==0))
	{
		std::cerr<<"hptWebCamGUI::frameTaker: Not enought memory for frames. Aborting!"<<std::endl;
		theEnd=true;
		return;
	}

  long ltmp(0L);
	QPixmap pm;
  unsigned long tmpLong(0UL);
  QTime timer;
  int counter(0);
  double fps(0.0);
	
	while(!theEnd)
	{
		bool use_compression(tGUI->UseNetworkCompression->isChecked());
		ACS::longSeq *frame=dev->getFrame(use_compression);
		int len(frame->length()),i(0);
		if(len<=0)
		{
		  std::cerr<<"hptWebCamGUI::frameTaker: Something is wrong with the frame size ("<<len<<"). Skipping it."<<std::endl;
			delete frame;
			break;
		}

		if(counter==0)
		{
			timer.start();
		}

		if(use_compression)
		{
			int err(Z_OK),j(1);
			unsigned long comprLen(0UL),uncomprLen(0UL);
	
			tmpLong=(*frame)[0];
		
			do
			{
			  ltmp=(*frame)[j++];
			  tmpLong=0;
			  memcpy(&tmpLong,&ltmp,sizeof(long));
			  tmp[i++]=tmpLong >> 24;
			  tmp[i++]=(tmpLong >> 16) & 0xff;
			  tmp[i++]=(tmpLong >> 8) & 0xff;
			  tmp[i++]=tmpLong & 0xff;
			}
			while(j<len);

			if(theEnd)
			{
				delete frame;
				break;
			}

			len=(*frame)[0]+1;
			comprLen=len*sizeof(int);
			uncomprLen=921601*sizeof(int);

			Byte* uncompr(static_cast<Byte*>(calloc((uInt)uncomprLen,1)));

			if(theEnd)
			{
				free(uncompr);
				delete frame;
				break;
			}
			else if(uncompr==Z_NULL)
			{
			  std::cerr<<"hptWebCamGUI::frameTaker: Uncompressor is out of memory."<<std::endl;
				free(uncompr);
				delete frame;
				continue;
			}

			err=uncompress(uncompr,&uncomprLen,static_cast<const Bytef*>(tmp),comprLen);

			if(theEnd)
			{
				free(uncompr);
				delete frame;
				break;
			}
			else if(err==Z_DATA_ERROR)
			{
			  std::cerr<<"hptWebCamGUI::frameTaker: The uncompressor returned a data error."<<std::endl;
				free(uncompr);
				delete frame;
				continue;
			}
			else if(err!=Z_OK)
			{
				free(uncompr);
				delete frame;
				continue;
			}

		  j=0;
			do
			{
				tmp[j]=uncompr[j];
				++j;
			}
			while(j<static_cast<int>(uncomprLen));
		}
		else	// Do not use compression
		{
			int j(0);
			do
			{
			  ltmp=(*frame)[j++];
			  tmpLong=0;
			  memcpy(&tmpLong,&ltmp,sizeof(long));
			  tmp[i++]=tmpLong >> 24;
			  tmp[i++]=(tmpLong >> 16) & 0xff;
			  tmp[i++]=(tmpLong >> 8) & 0xff;
			  tmp[i++]=tmpLong & 0xff;
			}
			while(j<len);
		}
		delete frame;

		if(theEnd)
		{
			break;
		}

	  ccvt_420p_bgr32(current_width,current_height,tmp,tmp2);
	  qApp->lock();
	  QRgb null_palette;
	  QImage camImage(tmp2,current_width,current_height,32,&null_palette,255,QImage::BigEndian);
	  pm.convertFromImage(camImage);

	  QPainter p;
	  p.begin(tGUI->webCamPixMap);
	  if(current_resolution==0)
	  {
	  	p.scale(2.0,2.0);
	  }
	  p.drawPixmap(0,0,pm);
	  p.end();
	  qApp->unlock();

		counter++;
		if(counter==MEASURE_FPS_EVERY_N_FRAMES)
		{
		  qApp->lock();
		  fps=MEASURE_FPS_EVERY_N_FRAMES/static_cast<double>(timer.elapsed())*1000.0;
		  counter=0;
		  tGUI->FramesPerSecond->display(fps);
		  qApp->unlock();
		}
	}
	delete[] tmp;
	delete[] tmp2;
}

void frameTaker::stop(void)
{
	theEnd=true;
};
