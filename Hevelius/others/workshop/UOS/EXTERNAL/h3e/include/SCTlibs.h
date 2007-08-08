#ifndef _SCTLIBS_H_
#define _SCTLIBS_H_

int             sct_open_lego(const char *);
int             sct_open_lego();
void            sct_close_lego();
short int       sct_get_variable(unsigned char variable);
int             sct_set_variable(int variable, short int value);
 
#endif
