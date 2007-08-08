#ifndef _SCTDEFS_H_
#define _SCTDEFS_H_

/*
 * Lego USB tower device location.
 * On most systems set this to "/dev/legousbtower0".
 * On debian systems use /dev/usb/legousbtower0" instead.
 * Allways check RW permission to almamgr user or group!
 */
#define LEGO_TOWER_DEV 		"/dev/legousbtower0"

/* Registry locations. Must be the same as in RCX code */
#define RCX_REAL_ALTITUDE 	4
#define RCX_REAL_AZIMUTH  	5
#define RCX_COMM_ALTITUDE   	6
#define RCX_COMM_AZIMUTH    	7
#define RCX_STATUS		8

/* Status bits masks */
#define STAT_CALIBRATED		0x01
#define STAT_ALTITUDE_ERROR	0x02
#define STAT_COMM_ERROR		0x20
#define STAT_CALIBRATE		0x40

/* Minimum wait period for RCX coordination */
#define RCX_WAIT_PERIOD 	1

/* Sensor assignments. Must be the same as in RCX code*/
#define RCX_MOTOR_ALTITUDE		0
#define RCX_MOTOR_AZIMUTH		1
#define RCX_TOUCH_ALTITUDE		0
#define RCX_TOUCH_AZIMUTH		0
#define RCX_ROTATION_ALTITUDE		1
#define RCX_ROTATION_AZIMUTH		2

#endif
