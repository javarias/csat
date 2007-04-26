#MakeFile

CC = gcc
CFLAGS = -Wall
OFLAGS = -lm

telescopio  : programa.c conversor.o
	$(CC) -o $@ $^ $(CFLAGS) $(OFLAGS)

conversor.o : conversor.h

clean:

	rm -f *.o \
	*~

cleandis:
	rm -f telescopio