/*
 * "@(#) $Id: hptExport.h,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: hptExport.h,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 * Revision 1.1  2007/06/25 13:24:22  jibsen
 * External files.
 *
 * Revision 1.4  2004/07/20 15:56:06  tjuerges
 * - Added latest kernel modules for Philips ToUCam PWC-740K model, including decompressor library (binary only) which allows the camera to take frames of 640*480 at 15 fps.
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
#ifndef HPT_EXPORT_H
#define HPT_EXPORT_H

#include <acsutil.h>


/** @file acsexmplExport.h
 *  Header file used for Win32 Export directives.
 *  
 *  Details goes here.
 */

#if !defined (HPT_HAS_DLL)
    #define HPT_HAS_DLL 1
#endif /* ! HPT_HAS_DLL */

#if defined (HPT_HAS_DLL) && (HPT_HAS_DLL == 1)

    #if defined (HPT_BUILD_DLL)
	#define hpt_EXPORT ACS_DLL_EXPORT
	#define HPT_SINGLETON_DECLARATION(T) ACE_EXPORT_SINGLETON_DECLARATION (T)
	#define HPT_SINGLETON_DECLARE(SINGLETON_TYPE, CLASS, LOCK) ACE_EXPORT_SINGLETON_DECLARE(SINGLETON_TYPE, CLASS, LOCK)
    #else /* HPT_BUILD_DLL */
	#define hpt_EXPORT ACS_DLL_IMPORT
	#define HPT_SINGLETON_DECLARATION(T) ACE_IMPORT_SINGLETON_DECLARATION (T)
	#define HPT_SINGLETON_DECLARE(SINGLETON_TYPE, CLASS, LOCK) ACE_IMPORT_SINGLETON_DECLARE(SINGLETON_TYPE, CLASS, LOCK)
    #endif /* HPT_BUILD_DLL */

#else /* HPT_HAS_DLL == 1 */
    #define hpt_EXPORT
    #define HPT_SINGLETON_DECLARATION(T)
    #define HPT_SINGLETON_DECLARE(SINGLETON_TYPE, CLASS, LOCK)
#endif /* HPT_HAS_DLL == 1 */

#endif /* HPT_EXPORT_H */

// End of auto generated file.
