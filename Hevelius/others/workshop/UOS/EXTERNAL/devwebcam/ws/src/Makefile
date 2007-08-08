


#*******************************************************************************
# E.S.O. - ACS project
#
# "@(#) $Id: Makefile,v 1.2 2007/07/09 18:20:38 wg5 Exp $"
#
# $Log: Makefile,v $
# Revision 1.2  2007/07/09 18:20:38  wg5
# Fix compilation in Scientific linux
#    - Added specifically the directory of qt-3.3
#
# Revision 1.1.1.1  2007/07/09 12:47:39  wg8
# Repository Setup
#
# Revision 1.1  2007/06/25 13:24:22  jibsen
# External files.
#
# Revision 1.6  2007/01/09 12:41:16  gchiozzi
# Fixed problem with compressions. zlib was not linked.
#
# Revision 1.5  2007/01/09 10:28:59  gchiozzi
# Done basic porting to ACS 6.0 and RH-Enterprise/Scientific Linux 4.
# Both the webcam Component and the GUI work, just with basic features:
# - 640x480 resolution
# - no compression.
# There are a number of things to do:
# - error handling, in particular for devIOs to be redone
# - check why compression does not work (some code commented out by GCH.
# - check why switching to 320x200 does not work.
# - name of device hardcode in the GUI
# Some general cleanup is needed.
#
# Revision 1.4  2004/07/20 15:56:06  tjuerges
# - Added latest kernel modules for Philips ToUCam PWC-740K model, including decompressor library (binary only) which allows the camera to take frames of 640*480 at 15 fps.
#
# Revision 1.2  2004/07/20 12:48:44  tjuerges
# - hptWebCamClient: Fehler in statischer Speichallozierung behoben im Frame-Abholen. Es gab Probleme auf manchen Maschinen, die beim Allozieren das Programm abgeborchen haben.
# - hptWebCam.idl: get_frame nun mit (in boolean); zeigt an, ob auf Server Daten per zlib komprimiert werden sollen.
# - GUI: Schönheitsarbeiten + prozentuale Anzeige der Kamerawerte.
#
# Revision 1.1  2004/07/16 15:17:41  tjuerges
# - Jetzt mit Sources des Kernel-Modules. Aufgesplittet in lcu/ws.
#
# Revision 2.3  2004/07/15 11:57:50  tjuerges
# - Anstelle ACE-Threads QThreads.
# - GUI erweitert.
# - Freigeben der Komponente hinzugefügt.
# - Von /dev/video1 nach /dev/video2.
# - Viel aufgeräumt.
#
#
# Makefile of hptWebCam
#
# who       when      what
# --------  --------  ----------------------------------------------
# root      25/09/02  created
# tjuerges  02/09/03  added compatibility settings for ACS 2.0 and 2.1 and
#                     making of an archive which contains every file needed
#                     for the build build process.
# tjuerges  01/12/03  added stuff for ACS 3.0
# tjuerges  10/06/04  added stuff for ACS 3.1
# tjuerges  10/07/04  refined Qt stuff
#

#*******************************************************************************
# This Makefile follows VLT Standards (see Makefile(5) for more).
#*******************************************************************************
# REMARKS
#    None
#------------------------------------------------------------------------

#
# user definable C-compilation flags
#

USER_CFLAGS = -DQT_THREAD_SUPPORT\
	-DMEASURE_FPS_EVERY_N_FRAMES=5\
	-DCOMPRESSION=0\
	-DFRAMES_PER_SECOND=5

#FIX for SCIENTIFIC LINUX.
QTDIR=/usr/lib/qt-3.3
# additional include and library search paths
USER_INC = -I$(QTDIR)/include
USER_LIB = -lACE \
	-lTAO \
	-lTAO_DsLogAdmin \
	-lTAO_CosNaming \
	-lTAO_IORTable \
	-lTAO_PortableServer \
	-lTAO_Svc_Utils \
	-lTAO_CosTrading \
	-lTAO_CosNotification \
	-lTAO_DynamicAny \
	-lTAO_IFR_Client \
	-lTAO_CosProperty \
	-lacsutil \
	-lcdb \
	-llogging \
	-lacscomponent \
	-lbaci \
	-lmaci \
	-lacsErrTypeComponent \
	-lmaciClient \
	-lacserr \
	-lm \
	-lloki \
	-L$(QTDIR)/lib \
	-lacstime \
        -lqt-mt \
        -lz   # zlib compression library.


#
# MODULE CODE DESCRIPTION:
# ------------------------
# As a general rule:  public file are "cleaned" and "installed"  
#                     local (_L) are not "installed".

#
# C programs (public and local)
# -----------------------------
EXECUTABLES = hptWebCamTestClient hptWebCamClient
###EXECUTABLES = hptWebCamTestClient 
EXECUTABLES_L   =
#

#
# <brief description of xxxxx program>

#
# Qt GUI files
# ------------
#
# ATTENTION:
# ----------
# !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
# When using Qt, then uncomment
# the Qt lines above in USER_LIB = ...
# !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
#
# Do not forget to add ../include/ if
# the files live there!
#
# Add .ui files here:
QT_UI_FILES = hptWebCamGUI
#
# Add class declaration files (.h)
# which use the Q_OBJECT macro here:
QT_MOC_FILES_H =
#
# Add class implementation files (.cpp)
# which use the Q_OBJECT macro here:
QT_MOC_FILES_CPP =

#
# Definitions for "easier" generation of needed Qt files.
# Extremly ugly!!! But it works. :)
# ------------------------------------------
# PLEASE DO NOT TOUCH...
# ------------------------------------------
# Correct filenames: foo --> foo.ui
QT_UI_FILES_UI = $(addsuffix .ui,$(QT_UI_FILES))
# foo --> ../include/foo.h
QT_UI_FILES_H = $(addprefix ../include/,$(addsuffix .h,$(QT_UI_FILES)))
# foo --> foo.cpp
QT_UI_FILES_CPP = $(addsuffix .cpp,$(QT_UI_FILES))

# Build 'moc'ed filename target: foo --> moc_foo.cpp
QT_UI_FILES_MOC = $(addprefix moc_,$(addsuffix .cpp,$(QT_UI_FILES)))

# Build correct filenames for 'moc'ed files: foo --> ../include/foo.h
QT_UI_FILES_EXTERN_H = $(addprefix ../include/,$(addsuffix .h,$(QT_MOC_FILES_H)))
# foo --> foo.cpp
QT_UI_FILES_EXTERN_CPP = $(addsuffix .cpp,$(QT_MOC_FILES_CPP))

# Add moc_ and .cpp to 'moc'ed file targets: foo --> moc_foo.cpp
QT_UI_FILES_EXTERN_MOC_H = $(addprefix moc__,$(addsuffix .cpp,$(QT_MOC_FILES_H)))
# foo --> moc_foo.cpp
QT_UI_FILES_EXTERN_MOC_CPP = $(addprefix moc_,$(addsuffix .cpp,$(QT_MOC_FILES_CPP)))

# Build list of files to be compiled: foo.cpp --> foo
QT_COMPILE_FILES = $(QT_UI_FILES) $(subst .cpp,,$(QT_UI_FILES_MOC) $(QT_UI_FILES_EXTERN_MOC_H) $(QT_UI_FILES_EXTERN_MOC_CPP))
# ------------------------------------------
# PLEASE DO NOT TOUCH UNTIL HERE!
# ------------------------------------------

hptWebCamTestClient_OBJECTS = hptWebCamTestClient
hptWebCamTestClient_LDFLAGS =
hptWebCamTestClient_LIBS = hptWebCamStubs maciClient acsErrTypeComponent acstime

hptWebCamClient_OBJECTS = $(QT_COMPILE_FILES) hptWebCamGUIMain hptWebCamGUIFrameTaker hptWebCamGUIEventLoop
hptWebCamClient_LDFLAGS = -u ccvt_420p_bgr24
#hptWebCamClient_LIBS = hptWebCamStubs maciClient acsErrTypeComponent acstime pwcx ccvt z qt-mt
hptWebCamClient_LIBS = hptWebCamStubs maciClient acsErrTypeComponent acstime ccvt z


#
# special compilation flags for single c sources
#yyyyy_CFLAGS   =

$(PROG)_OBJECTS            = $(PROG)
$(PROG)_LDFLAGS            =
$(PROG)_LIBS               = C++

#
# Includes (.h) files (public only)
# ---------------------------------
INCLUDES = hptWebCamImpl.h

#
# Libraries (public and local)
# ----------------------------
LIBRARIES = hptWebCam
LIBRARIES_L     =
hptWebCam_OBJECTS = hptWebCamImpl hptWebCamC hptWebCamS hptWebCamAccess

#
# <brief description of lllll library>
lllll_OBJECTS   =

#
# Scripts (public and local)
# ----------------------------
SCRIPTS         =
SCRIPTS_L       =

#
# TCL scripts (public and local)
# ------------------------------
TCL_SCRIPTS     =
TCL_SCRIPTS_L   =

#
# <brief description of tttttt tcl-script>
tttttt_OBJECTS  =
tttttt_TCLSH    = 
tttttt_LIBS     = 

#
# TCL libraries (public and local)
# ------------------------------
TCL_LIBRARIES   =
TCL_LIBRARIES_L =

#
# <brief description of tttlll library>
tttlll_OBJECTS  = 

#
# Python stuff (public and local)
# ----------------------------
PY_SCRIPTS         =
PY_SCRIPTS_L       =

PY_MODULES         =
PY_MODULES_L       =

PY_PACKAGES        =
PY_PACKAGES_L      =
pppppp_MODULES	   =

#
# UIF panels (public and local)
# ----------------------------
PANELS   =
PANELS_L =

#
# Configuration Database Files
# ----------------------------
CDB_SCHEMAS = WEBCAM 

#
# IDL Files and flags
#
IDL_FILES = hptWebCam
IDL_TAO_FLAGS =
USER_IDL =
#
# Jarfiles and their directories
#
JARFILES= 
jjj_DIRS=
jjj_EXTRAS= 
#
# java sources in Jarfile on/off
DEBUG= 
#
# ACS XmlIdl generation on/off
#
XML_IDL =  
#
# Java Component Helper Classes generation on/off
#
COMPONENT_HELPERS = 
#
# Java Entity Classes generation on/off
#
XSDBIND = 
#
# Schema Config files for the above
#
XSDBIND_INCLUDE = 
# man pages to be done
# --------------------
MANSECTIONS =
MAN1 =
MAN3 =
MAN5 =
MAN7 =
MAN8 =

#
# local man pages
# ---------------
MANl =

#
# ASCII file to be converted into Framemaker-MIF
# --------------------
ASCII_TO_MIF = 

#
# other files to be installed
#----------------------------
INSTALL_FILES =

#
# list of all possible C-sources (used to create automatic dependencies)
# ------------------------------
CSOURCENAMES = \
	$(foreach exe, $(EXECUTABLES) $(EXECUTABLES_L), $($(exe)_OBJECTS)) \
	$(foreach rtos, $(RTAI_MODULES) , $($(rtos)_OBJECTS)) \
	$(foreach lib, $(LIBRARIES) $(LIBRARIES_L), $($(lib)_OBJECTS))

#
#>>>>> END OF standard rules

#
# INCLUDE STANDARDS
# -----------------
ifdef ACSROOT
    MAKEDIR  = $(shell if [ -f $(INTROOT)/include/acsMakefile ];  then \
                     echo $(INTROOT)/include; \
	           else \
	             echo $(ACSROOT)/include; \
		   fi;)
    include $(MAKEDIR)/acsMakefile
else
    MAKEDIR  = $(shell if [ -f $(INTROOT)/include/vltMakefile ];  then \
                     echo $(INTROOT)/include; \
	           else \
	             echo $(VLTROOT)/include; \
		   fi;)
    include $(MAKEDIR)/vltMakefile
endif

#
# TARGETS
# -------
$(QT_UI_FILES_H): ../include/%.h: %.ui $(QT_UI_FILES_UI)
	$(QTDIR)/bin/uic -o $@ $<

$(QT_UI_FILES_CPP): %.cpp: ../include/%.h $(QT_UI_FILES_H)
	$(QTDIR)/bin/uic -o $@ -impl $< $(subst .cpp,.ui,$@)

$(QT_UI_FILES_MOC): moc_%.cpp: ../include/%.h $(QT_UI_FILES_H)
	$(QTDIR)/bin/moc -o $@ $<

$(QT_UI_FILES_EXTERN_MOC_H): moc__%.cpp: ../include/%.h $(QT_UI_FILES_EXTERN_H)
	$(QTDIR)/bin/moc -o $@ $<

$(QT_UI_FILES_EXTERN_MOC_CPP): moc_%.cpp: %.cpp $(QT_UI_FILES_EXTERN_CPP)
	$(QTDIR)/bin/moc -o $@ $<

qt:     $(QT_UI_FILES_H) $(QT_UI_FILES_CPP) $(QT_UI_FILES_MOC) $(QT_UI_FILES_EXTERN_MOC_H) $(QT_UI_FILES_EXTERN_MOC_CPP)
	@echo " . . . 'qt' done"

qt_clean:
	@rm -f $(QT_UI_FILES_H) $(QT_UI_FILES_CPP) $(QT_UI_FILES_MOC) $(QT_UI_FILES_EXTERN_MOC_H) $(QT_UI_FILES_EXTERN_MOC_CPP)
	@echo " . . . 'qt_clean' done"

all: qt do_all
	@echo " . . . all done"

clean: qt_clean clean_all
	@echo " . . . clean done"

clean_dist: clean_all clean_dist_all
	@echo " . . . clean_dist done"

man: do_man
	@echo " . . . man page(s) done"

install: install_all
	@echo " . . . installation done"

db: db_all
	@echo " . . . ../DB done"

dist:
	@echo ". . . Packing tar archive..."
	@./make_archive
	@echo ". . . Packing tar archive done."

#___oOo___
