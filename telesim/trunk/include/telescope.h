/**
 * \file telescope.h
 * Constant and data types definitions and function prototypes
 * declaration for telescope.c.
 * \author Rodrigo Tobar <rtobar@alumnos.inf.utfsm.cl>
 * \author Jorge Valencia <jorjazo@alumnos.inf.utfsm.cl>
 */

#ifndef _TELESCOPE_H_
#define _TELESCOPE_H_

/** \c telescope_t.gotoStatus constant telling that the telescope is moving. */
#define GOTO_MOVING 0
/** \c telescope_t.gotoStatus constant telling that the telescope is not moving. */
#define GOTO_STOPED 1

/** \c telescope_t.alignmentStatus constant telling that the telescope is being aligned. */
#define ALIGNED     0
/** \c telescope_t.alignmentStatus constant telling that the telescope is not being aligned. */
#define NOT_ALIGNED 1

/** Max number of axis revolutions. */
#define MAX_REVOLUTION 65536

/** Version structure definition. */
typedef struct version {
	/** Major version number. */
	int major;
	/** Minor version number. */
	int minor;
} version_t;

/** Telescope status data structure. */
typedef struct telescope {
	/** Protocol version. */
	version_t version;
	int alignmentStatus;
	int gotoStatus;
	/** Actual azimuth axis revolutions. */
	unsigned int azimuthRevolutions;
	/** Actual altitude axis revolutions. */
	unsigned int altitudeRevolutions;
} telescope_t;

/* Telescope functions */
char* get_ra_dec(char*);
char* get_azm_alt(char*);
char* goto_ra_dec(char*);
char* goto_azm_alt(char*);
char* get_version(char*);
char* echo(char*);
char* alignment_complete(char*);
char* goto_in_progress(char*);
char* cancel_goto(char*);

/* Verbosity function */
void verbosity(const char*,...);
#endif

