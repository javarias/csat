/*
 * "@(#) $Id: hptWebCamImpl.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: hptWebCamImpl.cpp,v $
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

#include <vltPort.h>

static char *rcsId="@(#) $Id: hptWebCamImpl.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $";
static void* use_rcsId=((void)&use_rcsId,(void *)&rcsId);

#include <baciDB.h>
#include <hptWebCamImpl.h>
#include <hptWebCamImplIncludes.h>
#include <hptWebCamHelperFunctions.cpp>

using namespace baci;

WebCam::WebCam(const ACE_CString &name, maci::ContainerServices *containerServices)
	:CharacteristicComponentImpl(name, containerServices)
	,m_palette_p(this)
	,m_colour_p(this)
	,m_minWidth_p(this)
	,m_videoDevice_p(this)
	,m_frame_p(this)
	,m_actHeight_p(this)
	,m_framesPerSecond_p(this)
	,m_automaticGainControl_p(this)
	,m_compression_p(this)
	,m_maxWidth_p(this)
	,m_actWidth_p(this)
	,m_maxHeight_p(this)
	,m_automaticWhiteBalance_p(this)
	,m_minHeight_p(this)
	,m_contrast_p(this)
	,m_gamma_p(this)
	,m_brightness_p(this)
	,m_fps_p(this)
{
	ACS_TRACE("::WebCam::WebCam");
	m_videoDevice_p = new RWstring(name+":videoDevice", getComponent());


#include <hptWebCamImplInit.cpp>
#include <hptWebCamThreadInit.cpp>
}


#include <hptWebCamThreadImpl.cpp>


WebCam::~WebCam()
{
	ACS_TRACE("::WebCam::~WebCam");
	if(getComponent())
	{
		ACS_DEBUG_PARAM("::WebCam::~WebCam", "Destroying %s...",  getComponent()->getName());
		getComponent()->stopAllThreads();
	}
	ACS_DEBUG("::WebCam::~WebCam", "Properties destroyed");
}


void WebCam::cleanUp()
{
	ACS_TRACE("::cleanUp()");
	if(getComponent())
	{
		#include <hptWebCamImplCleanUp.body>
		CharacteristicComponentImpl::cleanUp();
	}
}


/* --------------- [ Action implementator interface ] -------------- */

ActionRequest WebCam::invokeAction(int function,
	BACIComponent *cob,
	const int& callbackID,
	const CBDescIn& descIn,
	BACIValue* value,
	Completion& completion,
	CBDescOut& descOut)
{
	switch(function)
	{
		default:
		{
			return reqDestroy;
		}
	}
}


/* ------------------ [ Action implementations ] ----------------- */

/* ------------------ [ Functions ] ----------------- */

ACSErr::Completion* WebCam::setFrameSize(CORBA::Long width, CORBA::Long height) throw(CORBA::SystemException)
{
	#include "hptWebCamsetFrameSizeFunct.body"
}


ACSErr::Completion* WebCam::on() throw(CORBA::SystemException)
{
	#include "hptWebCamonFunct.body"
}


ACSErr::Completion* WebCam::reset() throw(CORBA::SystemException)
{
	#include "hptWebCamresetFunct.body"
}


ACS::longSeq* WebCam::getFrame(CORBA::Boolean use_compression) throw(CORBA::SystemException)
{
	#include "hptWebCamgetFrameFunct.body"
}


ACSErr::Completion* WebCam::off() throw(CORBA::SystemException)
{
	#include "hptWebCamoffFunct.body"
}


/* --------------------- [ CORBA interface ] ----------------------*/
ACS::RWlong_ptr WebCam::palette() throw(CORBA::SystemException)
{
	if(!m_palette_p)
	{
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_palette_p->getCORBAReference());
	return prop._retn();
}


ACS::RWlong_ptr WebCam::colour() throw(CORBA::SystemException)
{
	if(!m_colour_p)
	{
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_colour_p->getCORBAReference());
	return prop._retn();
}


ACS::ROlong_ptr WebCam::minWidth() throw(CORBA::SystemException)
{
	if(!m_minWidth_p)
	{
		return ACS::ROlong::_nil();
	}
	ACS::ROlong_var prop = ACS::ROlong::_narrow(m_minWidth_p->getCORBAReference());
	return prop._retn();
}


ACS::RWstring_ptr WebCam::videoDevice() throw(CORBA::SystemException)
{
	if(!m_videoDevice_p)
	{
		return ACS::RWstring::_nil();
	}
	ACS::RWstring_var prop = ACS::RWstring::_narrow(m_videoDevice_p->getCORBAReference());
	return prop._retn();
}


ACS::ROlongSeq_ptr WebCam::frame() throw(CORBA::SystemException)
{
	if(!m_frame_p)
	{
		return ACS::ROlongSeq::_nil();
	}
	ACS::ROlongSeq_var prop = ACS::ROlongSeq::_narrow(m_frame_p->getCORBAReference());
	return prop._retn();
}


ACS::ROlong_ptr WebCam::actHeight() throw(CORBA::SystemException)
{
	if(!m_actHeight_p)
	{
		return ACS::ROlong::_nil();
	}
	ACS::ROlong_var prop = ACS::ROlong::_narrow(m_actHeight_p->getCORBAReference());
	return prop._retn();
}


ACS::RWlong_ptr WebCam::framesPerSecond() throw(CORBA::SystemException)
{
	if(!m_framesPerSecond_p)
	{
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_framesPerSecond_p->getCORBAReference());
	return prop._retn();
}


ACS::RWlong_ptr WebCam::automaticGainControl() throw(CORBA::SystemException)
{
	if(!m_automaticGainControl_p)
	{
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_automaticGainControl_p->getCORBAReference());
	return prop._retn();
}


ACS::RWlong_ptr WebCam::compression() throw(CORBA::SystemException)
{
	if(!m_compression_p)
	{
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_compression_p->getCORBAReference());
	return prop._retn();
}


ACS::ROlong_ptr WebCam::maxWidth() throw(CORBA::SystemException)
{
	if(!m_maxWidth_p)
	{
		return ACS::ROlong::_nil();
	}
	ACS::ROlong_var prop = ACS::ROlong::_narrow(m_maxWidth_p->getCORBAReference());
	return prop._retn();
}


ACS::ROlong_ptr WebCam::actWidth() throw(CORBA::SystemException)
{
	if(!m_actWidth_p)
	{
		return ACS::ROlong::_nil();
	}
	ACS::ROlong_var prop = ACS::ROlong::_narrow(m_actWidth_p->getCORBAReference());
	return prop._retn();
}


ACS::ROlong_ptr WebCam::maxHeight() throw(CORBA::SystemException)
{
	if(!m_maxHeight_p)
	{
		return ACS::ROlong::_nil();
	}
	ACS::ROlong_var prop = ACS::ROlong::_narrow(m_maxHeight_p->getCORBAReference());
	return prop._retn();
}


ACS::RWlong_ptr WebCam::automaticWhiteBalance() throw(CORBA::SystemException)
{
	if(!m_automaticWhiteBalance_p)
	{
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_automaticWhiteBalance_p->getCORBAReference());
	return prop._retn();
}


ACS::ROlong_ptr WebCam::minHeight() throw(CORBA::SystemException)
{
	if(!m_minHeight_p)
	{
		return ACS::ROlong::_nil();
	}
	ACS::ROlong_var prop = ACS::ROlong::_narrow(m_minHeight_p->getCORBAReference());
	return prop._retn();
}


ACS::RWlong_ptr WebCam::contrast() throw(CORBA::SystemException)
{
	if(!m_contrast_p)
	{
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_contrast_p->getCORBAReference());
	return prop._retn();
}


ACS::RWlong_ptr WebCam::gamma() throw(CORBA::SystemException)
{
	if(!m_gamma_p)
	{
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_gamma_p->getCORBAReference());
	return prop._retn();
}


ACS::RWlong_ptr WebCam::brightness() throw(CORBA::SystemException)
{
	if(!m_brightness_p)
	{
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_brightness_p->getCORBAReference());
	return prop._retn();
}


ACS::RWlong_ptr WebCam::fps() throw(CORBA::SystemException)
{
	if(!m_fps_p)
	{
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_fps_p->getCORBAReference());
	return prop._retn();
}


/* --------------- [ MACI DLL support functions ] -----------------*/

#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(WebCam)
