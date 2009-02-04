#*******************************************************************************
# PPPPPPPP
#
# "@(#) $Id$"
#
# Makefile of ........
#
# who       when      what
# --------  --------  ----------------------------------------------
# almadev  08/08/07  created
#

#*******************************************************************************
# This Makefile follows VLT Standards (see Makefile(5) for more).
#*******************************************************************************
# REMARKS
#    None
#------------------------------------------------------------------------

#
# user definable C-compilation flags
#USER_CFLAGS = 

#
# additional include and library search paths
#USER_INC = 
#USER_LIB =

#
# MODULE CODE DESCRIPTION:
# ------------------------
# As a general rule:  public file are "cleaned" and "installed"  
#                     local (_L) are not "installed".

#
# C programs (public and local)
# -----------------------------
EXECUTABLES     =
EXECUTABLES_L   = 

#
# <brief description of xxxxx program>
xxxxx_OBJECTS   =	
xxxxx_LDFLAGS   =
xxxxx_LIBS      =

#
# special compilation flags for single c sources
#yyyyy_CFLAGS   = 

#
# Includes (.h) files (public only)
# ---------------------------------
INCLUDES        = 

#
# Libraries (public and local)
# ----------------------------
LIBRARIES       = ESO50Impl Lx200GPSImpl Lx200EMCImpl NexstarImpl NexsimImpl EquatorialNexsimImpl EquatorialNexstarImpl lpiImpl eTrexImpl PcDummyGpsImpl CalculationsImpl PointingImpl PcDummyCCDImpl
LIBRARIES_L     =

#
# <brief description of lllll library>
ESO50Impl_OBJECTS           = ESO50CoordDevIO ESO50VelDevIO ESO50Impl
ESO50Impl_LIBS              = TypesStubs DevTelescopeStubs csatErrors SerialCommunication baci acsutil acserr
Lx200GPSImpl_OBJECTS	    = Lx200GPSCoordDevIO Lx200GPSVelDevIO Lx200GPSImpl
Lx200GPSImpl_LIBS		    = TypesStubs DevTelescopeStubs csatErrors SerialCommunication baci acsutil acserr
Lx200EMCImpl_OBJECTS	    = Lx200EMCCoordDevIO Lx200EMCVelDevIO Lx200EMCImpl
Lx200EMCImpl_LIBS		    = TypesStubs DevTelescopeStubs csatErrors SerialCommunication baci acsutil acserr
NexstarImpl_OBJECTS      = NexstarCoordDevIO NexstarVelDevIO NexstarImpl
NexstarImpl_LIBS         = TypesStubs DevTelescopeStubs csatErrors SerialCommunication baci acsutil acserr
NexsimImpl_OBJECTS       = NexsimCoordDevIO NexsimVelDevIO NexsimImpl
NexsimImpl_LIBS          = TypesStubs DevTelescopeStubs csatErrors NexSimStubs baci acsutil acserr
EquatorialNexsimImpl_OBJECTS       = NexsimCoordDevIO NexsimVelDevIO EquatorialNexsimImpl
EquatorialNexsimImpl_LIBS          = TypesStubs DevTelescopeStubs csatErrors NexSimStubs baci acsutil acserr
EquatorialNexstarImpl_OBJECTS       = NexstarCoordDevIO NexstarVelDevIO EquatorialNexstarImpl
EquatorialNexstarImpl_LIBS          = TypesStubs DevTelescopeStubs csatErrors NexSimStubs baci acsutil acserr SerialCommunication
lpiImpl_OBJECTS          = lpiFrameDevIO lpiPropsDevIO lpiImpl
lpiImpl_LIBS             = TypesStubs DevCCDStubs csatErrors baci acsutil acserr
eTrexImpl_OBJECTS        = eTrexCommunication eTrexCoordDevIO eTrexImpl
eTrexImpl_LIBS           = TypesStubs DevGPSStubs csatErrors SerialCommunication baci acsutil acserr
PcDummyGpsImpl_OBJECTS   = PcDummyGpsImpl
PcDummyGpsImpl_LIBS      = TypesStubs DevGPSStubs csatErrors baci acsutil acserr
CalculationsImpl_OBJECTS = CalculationsImpl
CalculationsImpl_LIBS    = TypesStubs LocaleStubs CalculationsStubs csatErrors baci acsutil acserr
PointingImpl_OBJECTS     = PointingImpl
PointingImpl_LIBS        = TypesStubs PointingStubs csatErrors pointingsystem baci acsutil acserr
PcDummyCCDImpl_OBJECTS   = PcDummyCCDImpl
PcDummyCCDImpl_LIBS      = TypesStubs DevCCDStubs csatErrors baci acsutil acserr

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
CDB_SCHEMAS = DevTelescope DevCCD DevGPS

# 
# IDL Files and flags
# 
IDL_FILES = Types DevTelescope DevCCD DevGPS Telescope CCD Locale Pointing Tracking Calculations Safety CSATControl CSATStatus
IDL_TAO_FLAGS =
USER_IDL =

#
# Error definition files for exceptions and completions
# -----------------------------------------------------
ACSERRDEF = csatErrors

# Jarfiles and their directories
#
JARFILES= EquatorialTelescopeImpl TelescopeImpl CCDImpl LocaleImpl TrackingImpl SafetyImpl CSATControlImpl CSATStatusImpl callbacks
TelescopeImpl_DIRS=alma/TELESCOPE_MODULE/TelescopeImpl
EquatorialTelescopeImpl_DIRS=alma/TELESCOPE_MODULE/EquatorialTelescopeImpl
CCDImpl_DIRS=alma/CCD_MODULE/CCDImpl
LocaleImpl_DIRS=alma/LOCALE_MODULE/LocaleImpl
TrackingImpl_DIRS=alma/TRACKING_MODULE/TrackingImpl
SafetyImpl_DIRS=alma/SAFETY_MODULE/SafetyImpl
CSATControlImpl_DIRS=alma/CSATCONTROL_MODULE/CSATControlImpl
CSATStatusImpl_DIRS= alma/CSATSTATUS_MODULE/CSATStatusImpl
callbacks_DIRS=alma/acs/callbacks

USER_JFLAGS=-Xlint:unchecked -Xlint:deprecated

#
# java sources in Jarfile on/off
DEBUG=on
#
# ACS XmlIdl generation on/off
#
XML_IDL= 
#
# Java Component Helper Classes generation on/off
#
# COMPONENT_HELPERS=on
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
all:	do_all
	@echo " . . . 'all' done" 

clean : clean_all 
	@echo " . . . clean done"

clean_dist : clean_all clean_dist_all 
	@echo " . . . clean_dist done"

man   : do_man 
	@echo " . . . man page(s) done"

install : install_all
	@echo " . . . installation done"


#___oOo___
