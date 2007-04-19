## Process this file with automake to produce Makefile.in

INCLUDES = \
	-DPACKAGE_DATA_DIR=\""$(datadir)"\" \
	-DPACKAGE_LOCALE_DIR=\""$(prefix)/$(DATADIRNAME)/locale"\" \
	@PACKAGE_CFLAGS@

bin_PROGRAMS = threeaxis

threeaxis_SOURCES = \
	app.h \
	callbacks.c callbacks.h \
	controls.c controls.h \
	coord.c coord.h \
	glcallbacks.c glcallbacks.h \
	globject.c globject.h \
	interface.c interface.h \
	main.c \
	model.c model.h \
	support.c support.h 

threeaxis_LDADD = @PACKAGE_LIBS@

