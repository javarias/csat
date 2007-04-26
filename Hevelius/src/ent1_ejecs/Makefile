#MakeFile

CC = gcc
CFLAGS = -Wall
OFLAGS = -lm

telescopio  : programa.c conversor.o presetting.o
	$(CC) -o $@ $^ $(CFLAGS) $(OFLAGS)

conversor.o : conversor.h verificador.h

presetting.o : presetting.h

clean:

	rm -f *.o \
	*~

cleandis:
	rm -f telescopio
