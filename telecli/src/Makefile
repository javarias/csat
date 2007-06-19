CC=g++
CFLAGS=-Wall -O2 -ggdb

#SERIAL=serialport_rs232
OBJ=main.o serialport_rs232.o
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

#$(BINDIR)/$(EXE): $(OBJ) $(SERIAL).o
$(BINDIR)/$(EXE): $(OBJ)
	$(CC) $(CFLAGS) -o $(BINDIR)/$(EXE) $(addprefix $(OBJDIR)/,$(OBJ))

#$(SERIAL).o: $(LIBDIR)/$(SERIAL).cpp
#	$(CC) $(CFLAGS) -I$(INCDIR) -c $(LIBDIR)/$<  -o $(OBJDIR)/$(SERIAL).o

%.o: %.cpp
	$(CC) $(CFLAGS) -I$(INCDIR) -c $< -o $(OBJDIR)/$@

doc: $(wildcard *.cpp) $(wildcard *.h)
	@echo Making doc...
	@cd $(DOCDIR) && doxygen Doxyfile > /dev/null

clean:
	cd $(BINDIR) && rm -f $(EXE)
	cd $(OBJDIR) && rm -f $(OBJ)
#	cd $(LIBDIR) && rm -f $(SERIAL).o

distclean: clean
	rm -rf $(DOCDIR)/html
	
.PHONY: dirs clean
