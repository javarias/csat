CC=gcc
OPTS=-Wall -O2 -g
EXE=sim_nex4
OBJ=telescope.o main.o listener.o

all: $(EXE)

sim_nex4: $(OBJ)
	$(CC) $(OPTS) -o $(EXE) $(OBJ)

.c.o:
	$(CC) -g -c $<

clean:
	rm -f $(EXE) $(OBJ)

