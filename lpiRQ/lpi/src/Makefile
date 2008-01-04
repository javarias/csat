CC      = gcc
CFLAGS  = -Wall -O2 -ggdb

OBJ     = ccd.o bayer.o
EXE     = lpi
ROOTDIR =..
OBJDIR  = $(ROOTDIR)/object
BINDIR  = $(ROOTDIR)/bin
INCDIR  = $(ROOTDIR)/include
DOCDIR  = $(ROOTDIR)/doc

VPATH=$(OBJDIR):$(BINDIR):$(INCDIR)

all: dirs $(EXE)

dirs:
	@if ! test -d $(BINDIR) ; then mkdir $(BINDIR) ; fi
	@if ! test -d $(OBJDIR) ; then mkdir $(OBJDIR) ; fi

$(EXE): $(OBJ)
	$(CC) $(CFLAGS) -o $(BINDIR)/$(EXE) $(addprefix $(OBJDIR)/,$(OBJ))

%.o: %.c
	$(CC) $(CFLAGS) -I$(INCDIR) -c $< -o $(OBJDIR)/$@

doxygen: $(wildcard *.c) $(wildcard *.h)
	cd $(DOCDIR) && doxygen Doxyfile

clean:
	cd $(BINDIR) && rm -f $(EXE)
	cd $(OBJDIR) && rm -f $(OBJ)

distclean: clean
	rm -rf $(DOCDIR)/html

install:
	cp ../bin/* $(INTROOT)/bin
	
.PHONY: dirs clean

