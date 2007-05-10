#ifndef _TELESCOPE_H_
#define _TELESCOPE_H_

/* Definitions of the states of the telescope GOTO */
#define GOTO_MOVING 0
#define GOTO_STOPED 1

/* Definitinos of the states of the telescope aligment */
#define ALIGNED     0
#define NOT_ALIGNED 1

#define MAX_REVOLUTION 65536

typedef struct version{
	int major;
	int minor;
} version_t;

/* Definition of the telescope structure */
typedef struct telescope{
	char *message;
	version_t version;
	int alignmentStatus;
	int gotoStatus;
} telescope_t;

char* get_ra_dec(char*);
char* get_azm_alt(char*);
char* goto_ra_dec(char*);
char* goto_azm_alt(char*);
char* get_version(char*);
char* echo(char*);
char* alignment_complete(char*);
char* goto_in_progress(char*);
char* cancel_goto(char*);

#endif
