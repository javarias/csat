CC=g++
CFLAGS=-Wall -O2 -ggdb

OBJ=main.o SerialRS232.o Communication.o
EXE=cli_nex4
ROOTDIR=..
OBJDIR=$(ROOTDIR)/object
BINDIR=$(ROOTDIR)/bin
INCDIR=$(ROOTDIR)/include
DOCDIR=$(ROOTDIR)/doc
LIBDIR=$(ROOTDIR)/lib

VPATH=$(OBJDIR):$(BINDIR):$(INCDIR)

all: dirs $(EXE) doc

dirs:
	if ! test -d $(BINDIR) ; then mkdir $(BINDIR) ; fi
	if ! test -d $(OBJDIR) ; then mkdir $(OBJDIR) ; fi

$(BINDIR)/$(EXE): $(OBJ)
	$(CC) $(CFLAGS) -o $(BINDIR)/$(EXE) $(addprefix $(OBJDIR)/,$(OBJ))

%.o: %.cpp
	$(CC) $(CFLAGS) -I$(INCDIR) -c $< -o $(OBJDIR)/$@

doc: $(wildcard *.cpp) $(wildcard *.h)
	@echo Making doc...
	@cd $(DOCDIR) && doxygen Doxyfile &> /dev/null

clean:
	cd $(BINDIR) && rm -f $(EXE)
	cd $(OBJDIR) && rm -f $(OBJ)

distclean: clean
	rm -rf $(DOCDIR)/html
	
.PHONY: dirs clean
