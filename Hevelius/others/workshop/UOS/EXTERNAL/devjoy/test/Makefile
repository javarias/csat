#*******************************************************************************
#*    HPT - Hexapod Telescope 
#*    (c) Ruhr-Universität Bochum, 2003
#*    Copyright by AIRUB 
#*
#*    This library is free software; you can redistribute it and/or
#*    modify it under the terms of the GNU Lesser General Public
#*    License as published by the Free Software Foundation; either
#*    version 2.1 of the License, or (at your option) any later version.
#*
#*    This library is distributed in the hope that it will be useful,
#*    but WITHOUT ANY WARRANTY; without even the implied warranty of
#*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
#*    Lesser General Public License for more details.
#*
#*    You should have received a copy of the GNU Lesser General Public
#*    License along with this library; if not, write to the Free Software
#*    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
#*
#* "@(#) $Id: Makefile,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
#*
#*-----------------------------------------------------------------------------
#*                        Makefile  -  description
#*                            -------------------
#*   begin                : Wed Jan 29 2003
#*   copyright            : (C) 2003 by Roland Lemke
#*   email                : rlemke@astro.rub.de
#*
#*-----------------------------------------------------------------------------
#*
#* who       when      what
#* --------  --------  ----------------------------------------------
#* rlemke    02/02/03  added standard header
#*
#********************************************************************************

all: gxtest 

HPTLOCAL = $(shell echo $$HPTROOT)/local
TOPDIR    = $(shell echo $$HPTROOT)

#CC = $(HPTLOCAL)/bin/gcc
SRCS	= gxtest.c 
OBJS	= gxtest.o

USER_CFLAGS     := -Wall -Wstrict-prototypes -Wno-trigraphs -fno-strict-aliasing -O2 -I.
USER_LIBS       :=

gxtest:	gxtest.o

clean:
	rm -f *.o .depend gxtest 

%.o: %.c
	${CC} ${USER_CFLAGS} ${CFLAGS_FP} -c $< ;

depend .depend: $(SRCS) $(INCL)
	@$(CC) $(INCLUDE) $(CFLAGS_FP) -M $(SRCS) > ./.depend ; 

include .depend
