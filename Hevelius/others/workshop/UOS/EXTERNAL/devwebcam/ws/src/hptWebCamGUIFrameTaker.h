#ifndef HPTWEBCAMGUIFRAMETAKER_H
#define HPTWEBCAMGUIFRAMETAKER_H
/*
 * "@(#) $Id: hptWebCamGUIFrameTaker.h,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: hptWebCamGUIFrameTaker.h,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 * Revision 1.1  2007/06/25 13:24:22  jibsen
 * External files.
 *
 * Revision 1.1  2004/07/20 15:56:06  tjuerges
 * - Added latest kernel modules for Philips ToUCam PWC-740K model, including decompressor library (binary only) which allows the camera to take frames of 640*480 at 15 fps.
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

#include <qthread.h>

class hptWebCamGUI;

class frameTaker:public QThread
{
	public:
	virtual void setGUI(hptWebCamGUI*);
	virtual void run(void);
	virtual void stop(void);

	private:
	bool theEnd;
	hptWebCamGUI* tGUI;
};
#endif //HPTWEBCAMGUIFRAMETAKER_H
