/*
 * "@(#) $Id: hptWebCamImpl.h,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: hptWebCamImpl.h,v $
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
 * Revision 1.45  2004/07/12 09:32:19  tjuerges
 * - Fehler in Generierung der *Func.cpp behoben. Bei Rückgabe eines ORBA_var Typs muß die Variable explizit mit new erzeugt werden. Außerdem muß es return c._retn() sein.
 *
 * Hinweise von Martin:
 * - Einrückungen. Das wo es möglich ist, habe ich dies beherzigt.
 * - Struktur der If/else. Ich habe sämtlichen Code meinem Lieblingsaussehen angepaßt.
 * - Log hinzugefügt.
 * - vim-Key hinzugefügt.
 *
 * - Außerdem noch einige Änderungen kosmetischer Natur.
 *
 *
*/

#ifndef hptWebCamImpl_h
#define hptWebCamImpl_h

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <baciCharacteristicComponentImpl.h>

#include <hptExport.h>
#include <hptWebCamS.h>

#include <baciROlong.h>
#include <baciRWlong.h>
#include <baciROstring.h>
#include <baciRWstring.h>
#include <baciROdouble.h>
#include <baciRWdouble.h>
#include <baciROpattern.h>
#include <baciRWpattern.h>

#include <baciSmartPropertyPointer.h>

#include <hptWebCamIncludes.h>

using namespace baci;

class hpt_EXPORT WebCam: public virtual CharacteristicComponentImpl,
    public virtual POA_hptWebCam::WebCam,public ActionImplementator
{
    public:
    /**
     * Constructor
     * @param poa poa which will activate this and also all other COBs
     * @param name DO name
     */
    WebCam(const ACE_CString &name, maci::ContainerServices *containerServices);

    /**
     * Destructor
     */
    virtual ~WebCam();

    virtual void cleanUp();

    virtual ActionRequest invokeAction (int function,
	BACIComponent* cob_p,
	const int& callbackID,
        const CBDescIn& descIn,
        BACIValue* value_p,
        Completion& completion,
        CBDescOut& descOut);


/* --------------- [ Action START implementator interface ] ------ */

/* --------------- [ Action END   implementator interface ] ------ */

/* --------------------- [ CORBA START interface ] ----------------*/
	virtual ACS::RWlong_ptr palette() throw(CORBA::SystemException);

	virtual ACS::RWlong_ptr colour() throw(CORBA::SystemException);

	virtual ACS::ROlong_ptr minWidth() throw(CORBA::SystemException);

	virtual ACS::RWstring_ptr videoDevice() throw(CORBA::SystemException);

	virtual ACS::ROlongSeq_ptr frame() throw(CORBA::SystemException);

	virtual ACS::ROlong_ptr actHeight() throw(CORBA::SystemException);

	virtual ACS::RWlong_ptr framesPerSecond() throw(CORBA::SystemException);

	virtual ACS::RWlong_ptr automaticGainControl() throw(CORBA::SystemException);

	virtual ACS::RWlong_ptr compression() throw(CORBA::SystemException);

	virtual ACS::ROlong_ptr maxWidth() throw(CORBA::SystemException);

	virtual ACS::ROlong_ptr actWidth() throw(CORBA::SystemException);

	virtual ACS::ROlong_ptr maxHeight() throw(CORBA::SystemException);

	virtual ACS::RWlong_ptr automaticWhiteBalance() throw(CORBA::SystemException);

	virtual ACS::ROlong_ptr minHeight() throw(CORBA::SystemException);

	virtual ACS::RWlong_ptr contrast() throw(CORBA::SystemException);

	virtual ACS::RWlong_ptr gamma() throw(CORBA::SystemException);

	virtual ACS::RWlong_ptr brightness() throw(CORBA::SystemException);

	virtual ACS::RWlong_ptr fps() throw(CORBA::SystemException);

	virtual ACSErr::Completion* setFrameSize(CORBA::Long width ,CORBA::Long height ) throw(CORBA::SystemException);

	virtual ACSErr::Completion* on() throw(CORBA::SystemException);

	virtual ACSErr::Completion* reset() throw(CORBA::SystemException);

	virtual ACS::longSeq* getFrame(CORBA::Boolean use_compression ) throw(CORBA::SystemException);

	virtual ACSErr::Completion* off() throw(CORBA::SystemException);


/* --------------------- [ CORBA END interface ] ----------------*/

/* ----------------------------------------------------------------*/

  private:
/* --------------------- [ Properties START ] ----------------------*/
	SmartPropertyPointer<RWlong> m_palette_p;
	SmartPropertyPointer<RWlong> m_colour_p;
	SmartPropertyPointer<ROlong> m_minWidth_p;
	SmartPropertyPointer<RWstring> m_videoDevice_p;
	SmartPropertyPointer<ROlongSeq> m_frame_p;
	SmartPropertyPointer<ROlong> m_actHeight_p;
	SmartPropertyPointer<RWlong> m_framesPerSecond_p;
	SmartPropertyPointer<RWlong> m_automaticGainControl_p;
	SmartPropertyPointer<RWlong> m_compression_p;
	SmartPropertyPointer<ROlong> m_maxWidth_p;
	SmartPropertyPointer<ROlong> m_actWidth_p;
	SmartPropertyPointer<ROlong> m_maxHeight_p;
	SmartPropertyPointer<RWlong> m_automaticWhiteBalance_p;
	SmartPropertyPointer<ROlong> m_minHeight_p;
	SmartPropertyPointer<RWlong> m_contrast_p;
	SmartPropertyPointer<RWlong> m_gamma_p;
	SmartPropertyPointer<RWlong> m_brightness_p;
	SmartPropertyPointer<RWlong> m_fps_p;

/* --------------------- [ Properties END ] ------------------------*/

/* --------------------- [ Local properties START ] ----------------*/
#include <hptWebCamLocalProperties.h>
/* --------------------- [ Local properties END ] ------------------*/
};


#endif /* hptWebCamImpl_h */
