#*******************************************************************************
# E.S.O. - ACS project
#
# "@(#) $Id: Makefile,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
#
# Makefile of ........
#
# who       when      what
# --------  --------  ----------------------------------------------
# root  25/09/02  created
# tjuerges  02/09/03  added compatibility settings for ACS 2.0 and 2.1 and
#                     making of an archive which contains every file needed
#                     for the build build process.
#
# tjuerges  01/12/03  added stuff for ACS 3.0

#*******************************************************************************
# This Makefile follows VLT Standards (see Makefile(5) for more).
#*******************************************************************************
# REMARKS
#    None
#------------------------------------------------------------------------

#
# user definable C-compilation flags
#

#USER_CFLAGS =

#
# additional include and library search paths
#USER_INC =
USER_LIB = -lmaci -lbaci -lcdb -llogging -lacscomponent -lrecovery -lacserr -lacsutil -lexpat -lACE -lTAO -lTAO_DsLogAdmin -lTAO_CosNaming -lTAO_IORTable -lTAO_PortableServer -lTAO_Svc_Utils -lTAO_CosTrading -lTAO_DynamicAny -lTAO_CosProperty -lTAO_IFR_Client -lmaciClient -lacsclock

#
# MODULE CODE DESCRIPTION:
# ------------------------
# As a general rule:  public file are "cleaned" and "installed"  
#                     local (_L) are not "installed".

#
# C programs (public and local)
# -----------------------------
EXECUTABLES     = $(PROG) hptPadClient
EXECUTABLES_L   =

#
# <brief description of xxxxx program>
hptPadClient_OBJECTS = hptPadClient	
xxxxx_LDFLAGS   =
hptPadClient_LIBS = hptPadStubs maciClient acsclock acsErrTypeComponent

ABEANS = hptPad

#
# special compilation flags for single c sources
#yyyyy_CFLAGS   =

#
# Includes (.h) files (public only)
# ---------------------------------
INCLUDES        = hptPadImpl.h

#
# Libraries (public and local)
# ----------------------------
LIBRARIES       = hptPad
LIBRARIES_L     =
hptPad_OBJECTS = hptPadImpl hptPadC hptPadS 

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
# UIF panels (public and local)
# ----------------------------
PANELS   =
PANELS_L =

#
# On-Line Database Files
# ----------------------
DBL_CLASSES = 
DBL_BCF =
DBL_FLAGS =
xxxx_DBL_FLAGS =

# CDB schema files
CDB_SCHEMAS = PAD

#
# IDL Files and flags
#
IDL_FILES = hptPad
IDL_TAO_FLAGS =
USER_IDL =
#
# Jarfiles and their directories
#
JARFILES =
jjj_DIRS =

#
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
# INS_ROOT files to be installed
#-------------------------------
INS_ROOT_FILES =
INS_ROOT_DIR   =

#
# other files to be installed
#----------------------------
INSTALL_FILES =

#
# list of all possible C-sources (used to create automatic dependencies)
# ------------------------------
CSOURCENAMES = \
	$(foreach exe, $(EXECUTABLES) $(EXECUTABLES_L), $($(exe)_OBJECTS)) \
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

db : db_all
	@echo " . . . ../DB done"

dist:
	@echo ". . . Packing tar archive..."
	@./make_archive
	@echo ". . . Packing tar archive done."

#___oOo___
