CC=gcc
CFLAGS=-Wall -O2 -ggdb

OBJ=telescope.o main.o listener.o
EXE=sim_nex4
ROOTDIR=..
OBJDIR=$(ROOTDIR)/object
BINDIR=$(ROOTDIR)/bin
INCDIR=$(ROOTDIR)/include
DOCDIR=$(ROOTDIR)/doc

VPATH=$(OBJDIR):$(BINDIR):$(INCDIR)

all: dirs $(EXE) doc

dirs:
	if ! test -d $(BINDIR) ; then mkdir $(BINDIR) ; fi
	if ! test -d $(OBJDIR) ; then mkdir $(OBJDIR) ; fi

../bin/sim_nex4: $(OBJ)
	$(CC) $(CFLAGS) -o $(BINDIR)/$(EXE) $(addprefix $(OBJDIR)/,$(OBJ))

%.o: %.c
	$(CC) $(CFLAGS) -I$(INCDIR) -c $< -o $(OBJDIR)/$@

doc: $(wildcard *.c) $(wildcard *.h)
	cd $(DOCDIR) && doxygen Doxyfile

clean:
	cd $(BINDIR) && rm -f $(EXE)
	cd $(OBJDIR) && rm -f $(OBJ)

distclean: clean
	rm -rf $(DOCDIR)/html
	
.PHONY: dirs clean

