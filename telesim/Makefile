OPTS=-Wall -O2 -ggdb
EXE=sim_nex4
OBJ=telescope.o main.o listener.o

all: $(EXE)

sim_nex4: $(OBJ)
	gcc $(OPTS) -o $(EXE) $(OBJ)

.c.o:
	gcc -c $<

clean:
	rm -f $(EXE) $(OBJ)

