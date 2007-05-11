OPTS=-Wall -O2 -g
EXE=sim_nex4
OBJ=telescope.o main.o listener.o

all: $(EXE)

sim_nex4: $(OBJ)
	gcc $(OPTS) -o $(EXE) $(OBJ)

.c.o:
	gcc -g -c $<

clean:
	rm -f $(EXE) $(OBJ)

