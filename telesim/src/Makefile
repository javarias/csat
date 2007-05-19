CC=gcc
CFLAGS=-Wall -O2 -g

OBJ=telescope.o main.o listener.o
EXE=sim_nex4
OBJDIR=../object
BINDIR=../bin
INCDIR=../include

VPATH=$(OBJDIR):$(BINDIR):$(INCDIR)

all: dirs $(EXE)

dirs:
	if ! test -d $(BINDIR) ; then mkdir $(BINDIR) ; fi
	if ! test -d $(OBJDIR) ; then mkdir $(OBJDIR) ; fi

../bin/sim_nex4: $(OBJ)
	$(CC) $(CFLAGS) -o $(BINDIR)/$(EXE) $(addprefix $(OBJDIR)/,$(OBJ))

%.o: %.c
	$(CC) $(CFLAGS) -I$(INCDIR) -c $< -o $(OBJDIR)/$@

clean:
	cd $(BINDIR) && rm -f $(EXE)
	cd $(OBJDIR) && rm -f $(OBJ)

.PHONY: dirs clean

