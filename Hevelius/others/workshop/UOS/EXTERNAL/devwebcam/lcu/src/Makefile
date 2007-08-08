GCCLIB=`dirname \`gcc -print-libgcc-file-name\``

INCLUDES=-I../include -I/lib/modules/`uname -r`/build/include \
-I${GCCLIB}/include -I/usr/include \
-include /lib/modules/`uname -r`/build/include/linux/modversions.h \
-nostdinclude

CFLAGS=${INCLUDES} -O3 \
-DEXPORT_SYMTAB -D__KERNEL__ -DMODULE -Wall -DMODVERSIONS \
-DCONFIG_USB_PWCX_MODULE \
-funroll-loops -frerun-cse-after-loop \
-funroll-all-loops -fomit-frame-pointer

CC=/usr/bin/gcc

OBJ-pwc=../object/pwc-ctrl.o ../object/pwc-if.o ../object/pwc-misc.o ../object/pwc-uncompress.o
OBJ-pwcx=../object/pwcx-glue.o

all: ../lib/pwc-9.o ../lib/pwcx.o
	@echo " . . . all done."

$(OBJ-pwc): ../object/%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $<

$(OBJ-pwcx): ../object/%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $<

../lib/pwc-9.o: $(OBJ-pwc) $(OBJ-pwcx)
	$(LD) -r -o $@ $(OBJ-pwc)

../lib/pwcx.o: $(OBJ-pwcx)
	$(LD) -r -o $@ $(OBJ-pwcx) -L../lib -lpwcx
	
install: all
	cp ../lib/pwc-9.o ../lib/pwcx.o ../lib/libpwcx.a $(INTROOT)/lib
	cp ../include/pwc-ioctl.h $(INTROOT)/include
	@echo " . . . install done."

clean:
	rm -f $(OBJ-pwc) $(OBJ-pwcx) ../lib/pwc-9.o ../lib/pwcx.o
	@echo " . . . clean done."
