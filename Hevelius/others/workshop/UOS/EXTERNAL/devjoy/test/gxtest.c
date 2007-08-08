/*
 * gxtest.c, V 1.0
 * Copyright 1998 Dave Boynton
 *
 * based on jstest.c  Version 1.2
 * Copyright (C) 1996 Vojtech Pavlik
 */

/*
 * to compile: gcc -o gxtest gxtest.c
 * needs recent joystick.h in /usr/include/linux
 * 
 *
 * usage: gxtest [options] [device], device defaults to /dev/js0
 */

#include <sys/ioctl.h>
#include <sys/time.h>
#include <sys/types.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>
#include <errno.h>
#include <string.h>

#include <linux/joystick.h>

#define NAME_LENGTH 128
#define JS_SELECT_UDELAY 25000

static int x_offset=15;
static int x_multiply=1;
static int y_offset=7;
static int y_multiply=1;
static int y_resetto=15;

typedef struct tagJsObject {
	int yLabel;
	int xLabel;
	int yValue;
	int xValue;
	int vLength;
	char *lString;
} JSObject;

/* ------------------------------
 * Gravis Xterminator
 * ------------------------------*/
#define MAX_XTERM_BUTTONS	11
JSObject XterminatorButtons[MAX_XTERM_BUTTONS] = {
	{ 8, 35, 8, 38, 1, "R1:" },
	{ 8, 1, 8, 4, 1, "L1:" },
	{ 5, 35, 5, 38, 1, "A:" },
	{ 5, 40, 5, 43, 1, "B:" },
	{ 5, 45, 5, 48, 1, "C:" },
	{ 4, 35, 4, 38, 1, "X:" },
	{ 4, 40, 4, 43, 1, "Y:" },
	{ 4, 45, 4, 48, 1, "Z:" },
	{ 1, 15, 1, 22, 1, "Start:" },
	{ 2, 14, 2, 22, 1, "Select:" },
	{ 7, 20, 7, 29, 1, "Hot-Set:" }
};
	
#define MAX_XTERM_AXIS	9
JSObject XterminatorAxis[MAX_XTERM_AXIS] = {
	{ 3, 1, 3, 4, 6, "X:" },
	{ 4, 1, 4, 4, 6, "Y:" },
	{ 0, 7, 0, 11, 6, "FL:" },
	{ 0, 25, 0, 29, 6, "FR:" },
	{ 2, 35, 2, 45, 6, "Throttle:" },
	{ 5, 20, 5, 26, 6, "PovX:" },
	{ 6, 20, 6, 26, 6, "PovY:" },
	{ 5, 4, 5, 10, 6, "PadX:" },
	{ 6, 4, 6, 10, 6, "PadY:" }
};

/* ---------------------------------
 * Standard, with extra definitions
 * ---------------------------------*/
JSObject StandardButtons[10] = {
	{ 3, 30, 3, 38, 1, "Button1:" },
	{ 4, 30, 4, 38, 1, "Button2:" },
	{ 5, 30, 5, 38, 1, "Button3:" },
	{ 6, 30, 6, 38, 1, "Button4:" },
	{ 7, 30, 7, 38, 1, "Button5:" },
	{ 8, 30, 8, 38, 1, "Button6:" },
	{ 9, 30, 9, 38, 1, "Button7:" },
	{ 10, 30, 10, 38, 1, "Button8:" },
	{ 11, 30, 11, 38, 1, "Button9:" },
	{ 12, 29, 12, 38, 1, "Button10:" }
};

JSObject StandardAxis[10] = {
	{ 3, 6, 3, 9, 6, "X:" },
	{ 4, 6, 4, 9, 6, "Y:" },
	{ 5, 2, 5, 9, 6, "Axis3:" },
	{ 6, 2, 6, 9, 6, "Axis4:" },
	{ 7, 2, 7, 9, 6, "Axis5:" },
	{ 8, 2, 8, 9, 6, "Axis6:" },
	{ 9, 2, 9, 9, 6, "Axis7:" },
	{ 10, 2, 10, 9, 6, "Axis8:" },
	{ 11, 2, 11, 9, 6, "Axis9:" },
	{ 12, 1, 12, 9, 6, "Axis10:" }
};

typedef struct tagJSType {
	char * name;
	int buttons;
	int axis;
	JSObject * sButtons;
	JSObject * sAxis;
} JSType;

#define KNOWN_TYPES 2

JSType JoystickTypes[KNOWN_TYPES] = {
	{ "Standard", 10, 10, StandardButtons, StandardAxis },
	{ "Gravis Xterminator", 11, 9, XterminatorButtons, XterminatorAxis }
};

JSType *joystick=NULL;

void gxprint_string(int x, int y, char *s);
void gxprint_number(int x, int y, int v, int max);
void gxprint_resety(void);
void gxprint_button_label(JSType *, int i);
void gxprint_button_value(JSType *, int i, int value);
void gxprint_axis_label(JSType *, int i);
void gxprint_axis_value(JSType *, int i, int value);

int option_select=0;
int option_nonblock=0; 
int option_plain=0;

void do_usage(void)
{
	printf("Gxtest v1.0\n\nGeneric joystick tester.\n");
	printf("usage: gxtest [-hnps] [device]\n");
	printf("	-h	show this help\n");
	printf("	-n	use non-blocking mode (eats CPU!)\n");
	printf("	-p	use the 'Standard' Joystick view\n");
	printf("	-s	use select call with timeout\n");
	printf("\n	device defaults to /dev/js0\n\n");
}


/* --------------------------------------------------------
 *
 *
 *-----------------------------------------------------------*/
int main (int argc, char *argv[])
{
	int fd;
	char axis = 2;
	char buttons = 2;
	int version = 0x000800;
	char name[NAME_LENGTH] = "Unknown";
	char *devname= "/dev/js0";
	int i;

	while (argc > 1) {
		if ( argv[1][0] == '-') {
		    char * opt;
		    opt=argv[1];
		    while (*opt) {
			switch (*opt) {
			    case 'h':
				do_usage();
				exit(1);
				break;
			    case 'n':
				option_nonblock=1;
				break;
			    case 'p':
				option_plain=1;
				break;
			    case 's':
				option_select=1;
				break;
			}
			opt++;
		    }
		} else
			devname=strdup(argv[1]);
		argc--; argv++;
	}
	if ((fd = open(devname, O_RDONLY)) < 0) {
		perror("gxtest");
		exit(1);
	}

	ioctl(fd, JSIOCGVERSION, &version);
	ioctl(fd, JSIOCGAXES, &axis);
	ioctl(fd, JSIOCGBUTTONS, &buttons);
	ioctl(fd, JSIOCGNAME(NAME_LENGTH), name);

	if ( !option_plain ) {
	    for (i=0; (!joystick)&&(i<KNOWN_TYPES); i++) {
	        if ( strncmp(name, JoystickTypes[i].name, 9) == 0 ) {
		    joystick=&JoystickTypes[i];
		}
	    }
	}
	if ( !joystick ) { /* "standard" */
	    joystick=&JoystickTypes[0];
	    joystick->name=name;
	    if ( axis < joystick->axis )
		joystick->axis=axis;
	    if ( buttons < joystick->buttons )
	    	joystick->buttons=buttons;
	}

	printf("\033[1;1H\033[2J\n");
	printf("Joystick: \"%s\"\nAxis: %d\nButtons: %d\n", 
		joystick->name, axis, buttons);
	printf("Driver version is %d.%d.%d.\n\n",
		version >> 16, (version >> 8) & 0xff, version & 0xff);
	fflush(stdout);

	for (i=0; i<joystick->buttons; i++) {
		gxprint_button_label(joystick, i);
		gxprint_button_value(joystick, i,0);
	}
	for (i=0; i<joystick->axis; i++) {
		gxprint_axis_label(joystick, i);
		gxprint_axis_value(joystick, i, 0);
	}

        printf("\033[%u;1H", (y_resetto*y_multiply)+y_offset-1);
	printf("Testing ... (interrupt to exit)\n");
	gxprint_resety();
/*
 * Event interface, events being printed.
 */

	{
	    struct js_event js;
	    int readrc;
	    struct timeval tv;
	    fd_set set;

	    int count = 0;

	    if (option_nonblock)
		fcntl(fd, F_SETFL, O_NONBLOCK);

	    while (1) {
		if (option_select) {
	            tv.tv_sec = 0; /* always reload timeout after select!*/
	            tv.tv_usec = JS_SELECT_UDELAY;

		    FD_ZERO(&set);
		    FD_SET(fd, &set);

		    if (!select(fd+1, &set, NULL, NULL, &tv))
			continue;
		}
		readrc=read(fd, &js, sizeof(struct js_event));
		if (readrc == sizeof(struct js_event)) {
		    switch ((js.type&3)) {
		    case JS_EVENT_BUTTON:
			gxprint_button_value(joystick, js.number, js.value);
			break;
		    case JS_EVENT_AXIS:
			gxprint_axis_value(joystick, js.number, js.value);
			break;
		    }
		
		    gxprint_resety();
		} else if (errno != EAGAIN) {
		    perror("\njstest: error reading");
		    exit (1);
		}


	    }
	}

	return 1;
}

void gxprint_string(int x, int y, char *s)
{
#ifdef DEBUG
    printf("%u, %u, %s\n", x, y, s);
    fflush(stdout);
#else
    printf("\033[%u;%uH%s", (y*y_multiply)+y_offset, 
			    (x*x_multiply)+x_offset, s);
#endif
}

void gxprint_number(int x, int y, int v, int max)
{
#ifdef DEBUG
    printf("%u, %u, %u, max %u\n", x, y, v, max);
    fflush(stdout);
#else
    char buffer[20];
    if ( max == 1 )
	if ( v )
	    strcpy(buffer, "X");
	else
	    strcpy(buffer, " ");
    else
        sprintf(buffer, "%*d", max, v);
    printf("\033[%u;%uH%-*.*s", (y*y_multiply)+y_offset, 
			    (x*x_multiply)+x_offset, max, max, buffer);
#endif
}

void gxprint_resety(void)
{
    printf("\033[%u;1H", (y_resetto*y_multiply)+y_offset);
    fflush(stdout);
}

void gxprint_button_label(JSType *js, int i)
{
    if ( (i>-1) && (i<js->buttons) ) {
        gxprint_string(js->sButtons[i].xLabel, js->sButtons[i].yLabel, 
			js->sButtons[i].lString);
    }
#ifdef DEBUG
    else
	printf("%s: buttons=%d, i=%d\n", js->name, js->buttons, i);
#endif
}

void gxprint_button_value(JSType *js, int i, int value)
{
    if ( (i>-1) && (i<js->buttons) ) {
        gxprint_number(js->sButtons[i].xValue, js->sButtons[i].yValue, 
			value, js->sButtons[i].vLength);
    }
#ifdef DEBUG
    else
	printf("%s: buttons=%d, i=%d, value=%d\n", 
			js->name, js->buttons, i, value);
#endif
}

void gxprint_axis_label(JSType *js, int i)
{
    if ( (i>-1) && (i<js->axis) ) {
        gxprint_string(js->sAxis[i].xLabel, js->sAxis[i].yLabel, 
			js->sAxis[i].lString);
    }
#ifdef DEBUG
    else
	printf("%s: axis=%d, i=%d\n", js->name, js->axis, i);
#endif
}

void gxprint_axis_value(JSType *js, int i, int value)
{
    if ( (i>-1) && (i<js->axis) ) {
        gxprint_number(js->sAxis[i].xValue, js->sAxis[i].yValue, value, 
			js->sAxis[i].vLength);
    }
#ifdef DEBUG
    else
	printf("%s: axis=%d, i=%d, value=%d\n", js->name, js->axis, i, value);
#endif
}
