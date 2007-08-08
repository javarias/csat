#*******************************************************************************
# ACS project Makefile
#
# "@(#) $Id: Makefile,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
#
# $Log: Makefile,v $
# Revision 1.1.1.1  2007/07/09 12:47:39  wg8
# Repository Setup
#
#
# Makefile of H3E
#
# who       when      what
# --------  --------  ----------------------------------------------
# root      25/09/02  created
#

#*******************************************************************************
# This Makefile follows VLT Standards (see Makefile(5) for more).
#*******************************************************************************
# REMARKS
#    None
#------------------------------------------------------------------------

#
# user definable C-compilation flags
USER_CFLAGS = -DQT_THREAD_SUPPORT

#
# additional include and library search paths
# If you use the Qwt toolkit:
#USER_INC = -I$(QTDIR)/include -I$(QWTDIR)/include/qwt
# else (Qt only):
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
	-lacstime

#
# MODULE CODE DESCRIPTION:
# ------------------------
# As a general rule:  public file are "cleaned" and "installed"  
#                     local (_L) are not "installed".

#
# C programs (public and local)
# -----------------------------
EXECUTABLES =  H3ETestClient
EXECUTABLES_L   =
#

#
# special compilation flags for single c sources
#LegoControl_CFLAGS   = 

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
QT_UI_FILES = 
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

H3ETestClient_OBJECTS = H3ETestClient
H3ETestClient_LDFLAGS =
H3ETestClient_LIBS =  H3EStubs  maciClient acsErrTypeComponent acstime ACSErrTypeCommon



$(PROG)_OBJECTS = $(PROG)
$(PROG)_LDFLAGS =
$(PROG)_LIBS = C++

#
# Includes (.h) files (public only)
# ---------------------------------
INCLUDES = H3EImpl.h

#
# Libraries (public and local)
# ----------------------------
LIBRARIES = H3E

#
# <brief description of H3E library>
H3E_OBJECTS = H3ELegoCmdAzDevIO H3ELegoStatusDevIO H3ELegoAltDevIO H3ELegoCmdAltDevIO H3ELegoAzDevIO H3EImpl
H3E_LDFLAGS =
H3E_LIBS    =  H3EStubs ACSErrTypeDevIO


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
# Configuration Database Files
# ----------------------------
CDB_SCHEMAS = LegoControl

# 
# IDL Files and flags
# 
IDL_FILES = H3E
IDL_TAO_FLAGS =
USER_IDL =

#
#
#
ACSERRDEF = 

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
XML_IDL= 
#
# Java Component Helper Classes generation on/off
#
COMPONENT_HELPERS=
#
# Java Entity Classes generation on/off
#
XSDBIND=
#
# Schema Config files for the above
#
XSDBIND_INCLUDE=
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

MAKEDIRTMP := $(shell searchFile include/acsMakefile)
ifneq ($(MAKEDIRTMP),\#error\#)
   MAKEDIR := $(MAKEDIRTMP)/include
   include $(MAKEDIR)/acsMakefile
endif

#
# TARGETS
# -------
$(QT_UI_FILES_H): ../include/%.h: %.ui $(QT_UI_FILES_UI)
	(unset LD_LIBRARY_PATH; $(QTDIR)/bin/uic -o $@ $<;)

$(QT_UI_FILES_CPP): %.cpp: ../include/%.h $(QT_UI_FILES_H)
	(unset LD_LIBRARY_PATH; $(QTDIR)/bin/uic -o $@ -impl $< $(subst .cpp,.ui,$@);)

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

all:	qt do_all doxygen
	@echo " . . . 'all' done"

clean:	qt_clean clean_all
	@echo " . . . clean done"

clean_dist:	clean_all clean_dist_all
	@echo " . . . clean_dist done"

man:	do_man
	@echo " . . . man page(s) done"

install:	install_all
	@echo " . . . installation done"

dist:
	@echo ". . . Packing tar archive..."
	@./make_archive.sh
	@echo ". . . Packing tar archive done."

#___oOo___
