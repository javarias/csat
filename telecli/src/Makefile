CC=g++
CFLAGS=-Wall -O2 -ggdb

OBJ=SerialRS232.o Communication.o
TEST_EXE=test_nex4
CLI_EXE=cli_nex4

ROOTDIR=..
OBJDIR=$(ROOTDIR)/object
BINDIR=$(ROOTDIR)/bin
INCDIR=$(ROOTDIR)/include
DOCDIR=$(ROOTDIR)/doc
LIBDIR=$(ROOTDIR)/lib

VPATH=$(OBJDIR):$(BINDIR):$(INCDIR)

all: dirs $(TEST_EXE) $(CLI_EXE) doc

dirs:
	if ! test -d $(BINDIR) ; then mkdir $(BINDIR) ; fi
	if ! test -d $(OBJDIR) ; then mkdir $(OBJDIR) ; fi

$(TEST_EXE): $(OBJ) test.o
	$(CC) $(CFLAGS) -o $(BINDIR)/$(TEST_EXE) $(addprefix $(OBJDIR)/,$^)


$(CLI_EXE): $(OBJ) client.o
	$(CC) $(CFLAGS) -o $(BINDIR)/$(CLI_EXE) $(addprefix $(OBJDIR)/,$^)

%.o: %.cpp
	$(CC) $(CFLAGS) -I$(INCDIR) -c $< -o $(OBJDIR)/$@

doc: $(wildcard *.cpp) $(wildcard *.h)
	@echo Making doc...
	@cd $(DOCDIR) && doxygen Doxyfile &> /dev/null

clean:
	cd $(BINDIR) && rm -f $(TEST_EXE) $(CLI_EXE)
	cd $(OBJDIR) && rm -f $(OBJ)

distclean: clean
	rm -rf $(DOCDIR)/html
	
.PHONY: dirs clean
