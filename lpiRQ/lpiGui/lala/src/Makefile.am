## Process this file with automake to produce Makefile.in

INCLUDES = \
	-DPACKAGE_DATA_DIR=\""$(datadir)"\" \
	-DPACKAGE_LOCALE_DIR=\""$(prefix)/$(DATADIRNAME)/locale"\" \
	@PACKAGE_CFLAGS@

bin_PROGRAMS = lpiGui

lpiGui_SOURCES = \
	main.c \
	ccd.c ccd.h \
	bayer.c bayer.h \
	support.c support.h \
	interface.c interface.h \
	callbacks.c callbacks.h \
   sonix_compress.c sonix_compress.h

lpiGui_LDADD = @PACKAGE_LIBS@ $(INTLLIBS)

