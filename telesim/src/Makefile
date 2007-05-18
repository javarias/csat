CC=gcc
CFLAGS=-Wall -O2 -g -I../include

OBJ=telescope.o main.o listener.o
EXE=sim_nex4
OBJDIR=../object
BINDIR=../bin

vpath %.o $(OBJDIR)
vpath % $(OBJDIR):$(BINDIR)

GPATH=$(OBJDIR)

all: dirs $(EXE)

dirs:
	if ! test -d $(BINDIR) ; then mkdir $(BINDIR) ; fi
	if ! test -d $(OBJDIR) ; then mkdir $(OBJDIR) ; fi

../bin/sim_nex4: $(OBJ)
	$(CC) $(CFLAGS) -o $(BINDIR)/$(EXE) $^

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $(OBJDIR)/$@

clean:
	cd $(BINDIR) && rm -f $(EXE)
	cd $(OBJDIR) && rm -f $(OBJ)

.PHONY: dirs clean

