/*
 * BAYER2RGB24 ROUTINE TAKEN FROM:
 *
 * Sonix SN9C101 based webcam basic I/F routines
 * Copyright (C) 2004 Takafumi Mizuno <taka-qce@ls-a.jp>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

#include "bayer.h"
#include <stdio.h>

void
bayer2rgb24 (unsigned char *dst, unsigned char *src, long int WIDTH,
	     long int HEIGHT)
{

  long int i;
  unsigned char *rawpt, *scanpt;
  long int size;

  unsigned char r, g, b;
  unsigned char rm, gm, bm;
  long int rp, gp, bp;

  rawpt = src;
  scanpt = dst;
  size = WIDTH * HEIGHT;
/*
  rp = 0;
  gp = 0;
  bp = 0;
  r = 255;
  g = 255;
  b = 255;
  rm = 0;
  gm = 0;
  bm = 0;

  for(i=0; i<size;i++)
  {
	  if ((i / WIDTH) % 2 == 0)
	  {
		  if ((i % 2) == 0)
		  {
			  if(*rawpt < b)
				  b = *rawpt;
			  if(*rawpt > bm)
				  bm = *rawpt;
			  bp += *rawpt;
		  }
		  else
		  {
			  if(*rawpt < g)
				  g = *rawpt;
			  if(*rawpt > gm)
                                  gm = *rawpt;
			  gp += *rawpt;
		  }
	  }
	  else
	  {
		  if ((i % 2) == 0)
		  {
			  if(*rawpt < g)
				  g = *rawpt;
			  if(*rawpt > gm)
                                  gm = *rawpt;
			  gp += *rawpt;
		  }
		  else
		  {
			  if(*rawpt < r)
				  r = *rawpt;
			  if(*rawpt > rm)
                                  rm = *rawpt;
			  rp += *rawpt;
		  }
	  }
	  rawpt++;
  }

  rp = rp/(size/4);
  gp = gp/(size/2);
  bp = bp/(size/4);

  printf("Min r: %d\t g: %d\t b: %d\n", r, g, b);
  printf("Max r: %d\t g: %d\t b: %d\n", rm, gm, bm);
  printf("Promedio r: %d\t g: %d\t b: %d\n", rp, gp, bp);

  r = (r+3*rp)/4;
  g = (g+3*gp)/4;
  b = (b+3*bp)/4;

  printf("Final r: %d\t g: %d\t b:%d\n", r,g,b);

  r = 136*3/4;
  g = 142*3/4;
  b = 134*3/4;

  rawpt = rawpt - i;
*/

  for (i = 0; i < size; i++)
  {
	  if ((i / WIDTH) % 2 == 0)
	  {
		  if ((i % 2) == 0)
		  {
			  /* B */
			  if ((i > WIDTH) && ((i % WIDTH) > 0))
			  {
				  *scanpt++ = (*(rawpt - WIDTH - 1) + *(rawpt - WIDTH + 1) + *(rawpt + WIDTH - 1) + *(rawpt + WIDTH + 1)) / 4;	/* R */
				  *scanpt++ = (*(rawpt - 1) + *(rawpt + 1) + *(rawpt + WIDTH) + *(rawpt - WIDTH)) / 4;	/* G */
				  *scanpt++ = *rawpt;	/* B */
			  }
			  else
			  {
				  /* first line or left column */
				  *scanpt++ = *(rawpt + WIDTH + 1);	/* R */
				  *scanpt++ = (*(rawpt + 1) + *(rawpt + WIDTH)) / 2;	/* G */
				  *scanpt++ = *rawpt;	/* B */
			  }
		  }
		  else
		  {
			  /* (B)G */
			  if ((i > WIDTH) && ((i % WIDTH) < (WIDTH - 1)))
			  {
				  *scanpt++ = (*(rawpt + WIDTH) + *(rawpt - WIDTH)) / 2;	/* R */
				  *scanpt++ = *rawpt;	/* G */
				  *scanpt++ = (*(rawpt - 1) + *(rawpt + 1)) / 2;	/* B */
			  }
			  else
			  {
				  /* first line or right column */
				  *scanpt++ = *(rawpt + WIDTH);	/* R */
				  *scanpt++ = *rawpt;	/* G */
				  *scanpt++ = *(rawpt - 1);	/* B */
			  }
		  }
	  }
	  else
	  {
		  if ((i % 2) == 0)
		  {
			  /* G(R) */
			  if ((i < (WIDTH * (HEIGHT - 1))) && ((i % WIDTH) > 0))
			  {
				  *scanpt++ = (*(rawpt - 1) + *(rawpt + 1)) / 2;	/* R */
				  *scanpt++ = *rawpt;	/* G */
				  *scanpt++ = (*(rawpt + WIDTH) + *(rawpt - WIDTH)) / 2;	/* B */
			  }
			  else
			  {
				  /* bottom line or left column */
				  *scanpt++ = *(rawpt + 1);	/* R */
				  *scanpt++ = *rawpt;	/* G */
				  *scanpt++ = *(rawpt - WIDTH);	/* B */
			  }
		  }
		  else
		  {
			  /* R */
			  if (i < (WIDTH * (HEIGHT - 1)) && ((i % WIDTH) < (WIDTH - 1)))
			  {
				  *scanpt++ = *rawpt;	/* R */
				  *scanpt++ = (*(rawpt - 1) + *(rawpt + 1) + *(rawpt - WIDTH) + *(rawpt + WIDTH)) / 4;	/* G */
				  *scanpt++ = (*(rawpt - WIDTH - 1) + *(rawpt - WIDTH + 1) + *(rawpt + WIDTH - 1) + *(rawpt + WIDTH + 1)) / 4;	/* B */
			  }
			  else
			  {
				  /* bottom line or right column */
				  *scanpt++ = *rawpt;	/* R */
				  *scanpt++ = (*(rawpt - 1) + *(rawpt - WIDTH)) / 2;	/* G */
				  *scanpt++ = *(rawpt - WIDTH - 1);	/* B */
			  }
		  }
	  }
/*
	  if(*(scanpt -3) > r)
		  *(scanpt - 3) = *(scanpt -3) - r;
	  else
		  *(scanpt - 3) = (unsigned char)0;

	  if(*(scanpt -2) > g)
		  *(scanpt - 2) = *(scanpt -2) - g;
	  else
		  *(scanpt - 2) = (unsigned char)0;

	  if(*(scanpt -1) > b)
		  *(scanpt - 1) = *(scanpt -1) - b;
	  else
		  *(scanpt - 1) = (unsigned char)0;
*/
	  rawpt++;
  }

}
