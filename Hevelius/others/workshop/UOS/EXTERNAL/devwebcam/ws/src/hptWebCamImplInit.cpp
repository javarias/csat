/*
 * "@(#) $Id: hptWebCamImplInit.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: hptWebCamImplInit.cpp,v $
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
 * Revision 2.3  2004/07/15 11:57:50  tjuerges
 * - Anstelle ACE-Threads QThreads.
 * - GUI erweitert.
 * - Freigeben der Komponente hinzugefügt.
 * - Von /dev/video1 nach /dev/video2.
 * - Viel aufgeräumt.
 *
 *
*/

vidDev=0;
paletteIO_p=0;
minHeightIO_p=0;
colourIO_p=0;
minWidthIO_p=0;
contrastIO_p=0;
frameIO_p=0;
actHeightIO_p=0;
compressionIO_p=0;
gammaIO_p=0;
maxWidthIO_p=0;
brightnessIO_p=0;
actWidthIO_p=0;
fpsIO_p=0;
maxHeightIO_p=0;
agcIO_p=0;
awbIO_p=0;

ACSErr::Completion_var completion;
const std::string device_name(m_videoDevice_p->get_sync(completion.out()));
vidDev=new webcamToUCam(device_name);
if(vidDev)
{
	paletteIO_p=new webcamPaletteIO(vidDev);
	frameIO_p=new webcamFrameIO(vidDev);
	gammaIO_p=new webcamGammaIO(vidDev);
	brightnessIO_p=new webcamBrightnessIO(vidDev);
	minHeightIO_p=new webcamMinHeightIO(vidDev);
	colourIO_p=new webcamColourIO(vidDev);
	minWidthIO_p=new webcamMinWidthIO(vidDev);
	contrastIO_p=new webcamContrastIO(vidDev);
	actHeightIO_p=new webcamActHeightIO(vidDev);
	compressionIO_p=new webcamCompressionIO(vidDev);
	gammaIO_p=new webcamGammaIO(vidDev);
	maxWidthIO_p=new webcamMaxWidthIO(vidDev);
	brightnessIO_p=new webcamBrightnessIO(vidDev);
	actWidthIO_p=new webcamActWidthIO(vidDev);
	fpsIO_p=new webcamFPSIO(vidDev);
	maxHeightIO_p=new webcamMaxHeightIO(vidDev);
	agcIO_p=new webcamAGCIO(vidDev);
	awbIO_p=new webcamAWBIO(vidDev);
}

DO_running=use_network_compression=false;

m_brightness_p=new RWlong(name+":brightness",getComponent(),brightnessIO_p);
m_palette_p=new RWlong(name+":palette",getComponent(),paletteIO_p);
m_frame_p=new ROlongSeq(name+":frame",getComponent(),frameIO_p);
m_gamma_p=new RWlong(name+":gamma",getComponent(),gammaIO_p);
m_minHeight_p=new ROlong(name+":minHeight",getComponent(),minHeightIO_p);
m_colour_p=new RWlong(name+":colour",getComponent(),colourIO_p);
m_minWidth_p=new ROlong(name+":minWidth",getComponent(),minWidthIO_p);
m_contrast_p=new RWlong(name+":contrast",getComponent(),contrastIO_p);
m_actHeight_p=new ROlong(name+":actHeight",getComponent(),actHeightIO_p);
m_compression_p=new RWlong(name+":compression",getComponent(),compressionIO_p);
m_maxWidth_p=new ROlong(name+":maxWidth",getComponent(),maxWidthIO_p);
m_actWidth_p=new ROlong(name+":actWidth",getComponent(),actWidthIO_p);
m_fps_p=new RWlong(name+":fps",getComponent(),fpsIO_p);
m_maxHeight_p=new ROlong(name+":maxHeight",getComponent(),maxHeightIO_p);
m_framesPerSecond_p=new RWlong(name+":framesPerSecond",getComponent(),fpsIO_p);
m_automaticGainControl_p=new RWlong(name+":automaticGainControl",getComponent(),agcIO_p);
m_automaticWhiteBalance_p=new RWlong(name+":automaticWhiteBalance",getComponent(),awbIO_p);
