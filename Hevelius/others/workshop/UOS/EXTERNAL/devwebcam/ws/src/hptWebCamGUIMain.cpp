/*
 * "@(#) $Id: hptWebCamGUIMain.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: hptWebCamGUIMain.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 * Revision 1.1  2007/06/25 13:24:22  jibsen
 * External files.
 *
 * Revision 1.3  2007/01/18 22:09:08  acsg8
 * Added command line parameter as WebCam component name.
 *
 * Revision 1.2  2007/01/09 10:28:59  gchiozzi
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

#include <qapplication.h>
#include <qcombobox.h>

#include <hptWebCamGUIFrameTaker.h>
#include <hptWebCamGUIEventLoop.h>
#include <hptWebCamGUI.h>

#include <hptWebCamC.h>

#include <maciSimpleClient.h>
#include <baciCORBA.h>

#include <ccvt.h>
#include <zlib.h>

hptWebCam::WebCam_var dev;
eventLoop* myeventLoop;
frameTaker* myframeTaker;

int main(int argc,char* argv[])
{
    static const char* myVersion=ZLIB_VERSION;
    std::string component("WEBCAM");

    if(argc == 2)
	{
	component = argv[1];
	}
    ACS_SHORT_LOG((LM_ERROR,"hptWebCamClient::main: Will connect to camera: %s (change with cmd line parm)!", component.c_str()));
    
    if(zlibVersion()[0]!=myVersion[0])
	{
	ACS_SHORT_LOG((LM_ERROR,"hptWebCamClient::main: The installed zlib is incompatible!"));
	return -1;
	}
    else if(strcmp(zlibVersion(),ZLIB_VERSION)!=0)
	{
	ACS_SHORT_LOG((LM_WARNING,"hptWebCamClient::main: The installed zlib has a different version than needed. Continuing anyway..."));
	}

    QApplication a(argc,argv);

    hptWebCamGUI* w=new hptWebCamGUI;
    a.setMainWidget(w);

    myframeTaker=new frameTaker;
    myframeTaker->setGUI(w);

    myeventLoop=new eventLoop;
    maci::SimpleClient* sc=new maci::SimpleClient;
    myeventLoop->setSimpleClient(sc);

    w->resolutionBox->setCurrentItem(1);
    w->show();
                                                                                
    try
	{
	/**
	 * Create the instance of Client and Init() it.
	 */
	if(sc->init(qApp->argc(),qApp->argv())==0)
	    {
	    ACS_SHORT_LOG((LM_ERROR,"hptWebCamGUI::main: Cannot init client."));
	    return -1;
	    }
                                                                                
	if(sc->login()==0)
	    {
	    ACS_SHORT_LOG((LM_ERROR,"hptWebCamGUI::main: Cannot login."));
	    return -1;
	    }

	dev=sc->get_object<hptWebCam::WebCam>(component.c_str(),0,true);
	if(CORBA::is_nil(dev.in()))
	    {
	    ACS_SHORT_LOG((LM_ERROR,"hptWebCamGUI::main: Failed to narrow component."));
	    return 0;
	    }
	}
    catch(...)
	{
        ACS_SHORT_LOG((LM_ERROR,"hptWebCamGUI::main: Failed to get in touch with camera."));
    	if(!CORBA::is_nil(dev))
	    {
	    sc->manager()->release_component(sc->handle(),component.c_str());
	    }
    	sc->logout();
	return -1;
	}


    ACS_SHORT_LOG((LM_INFO,"hptWebCamGUI::main: Entering event loop."));

    myeventLoop->start();

    a.exec();

    ACS_SHORT_LOG((LM_INFO,"hptWebCamGUI::main: Going to finalize application."));

    delete myframeTaker;
    delete myeventLoop;
    delete w;

    if(!CORBA::is_nil(dev))
	{
	sc->manager()->release_component(sc->handle(),component.c_str());
	}
    sc->logout();
    delete sc;
    return 0;
}
