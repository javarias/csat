/*
 "@(#) $Id: hptPadThreadImpl.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
*/

#include <linux/joystick.h>
#define NAME_LENGTH 128
#define JS_SELECT_UDELAY 25000

/// @todo: G.Chiozzi - Axis mapping to be verified. 
///        Does not correspond to my joystick

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

int wingManDevice;

void
Pad::workerThread(void *threadParam_p)
{


    if(!threadParam_p)
        {
        return;
        }

    ACE_DECLARE_NEW_CORBA_ENV;
    ACSErr::Completion_var completion;

    BACIThreadParameter *baciParameter_p = (BACIThreadParameter *)threadParam_p;
    BACIThread *myself_p = baciParameter_p->getBACIThread();

    // Variables have to be passed explicitly
    Pad *pad_p = (Pad *)baciParameter_p->getParameter();

    if (BACIThread::InitThread) BACIThread::InitThread("workerThread");

    struct js_event js;
    int readrc;
    struct timeval tv;
    fd_set set;
 
    /// @todo: G.Chiozzi - Error handling to be done
    // int acs_error = 0;
    unsigned long long timestamp = 0LL;

    while(myself_p->check())
       {
        if(!myself_p->isSuspended())
          {
	         fcntl(wingManDevice, F_SETFL, O_NONBLOCK);
           tv.tv_sec = 0; /* always reload timeout after select!*/
           tv.tv_usec = JS_SELECT_UDELAY;

           FD_ZERO(&set);
           FD_SET(wingManDevice, &set);

           if (!select(wingManDevice+1, &set, NULL, NULL, &tv)) continue;

					 readrc=read(wingManDevice, &js, sizeof(struct js_event));
           if (readrc == sizeof(struct js_event)) {
//	   ACS_STATIC_SHORT_LOG((LM_INFO, "Type: %d, number: %d\n",js.type, js.number));
              switch ((js.type&3)) {
                 case JS_EVENT_BUTTON:
    								switch (js.number) {
			  							case 0: // Button A
  											pad_p->m_btnA_p->getDevIO()->write(js.value, timestamp); break;
			  							case 1: // Button B
  											pad_p->m_btnB_p->getDevIO()->write(js.value, timestamp); break;
		 	  							case 2: // Button C
  											pad_p->m_btnC_p->getDevIO()->write(js.value, timestamp); break;
			  							case 3: // Button X
  											pad_p->m_btnX_p->getDevIO()->write(js.value, timestamp); break;
			  							case 4: // Button Y
  											pad_p->m_btnY_p->getDevIO()->write(js.value, timestamp); break;
			  							case 5: // Button Z
  											pad_p->m_btnZ_p->getDevIO()->write(js.value, timestamp); break;
			  							case 6: // Button L1
  											pad_p->m_L1_p->getDevIO()->write(js.value, timestamp); break;
			  							case 7: // Button R1
  											pad_p->m_R1_p->getDevIO()->write(js.value, timestamp); break;
			  							case 8: // Button Start (not implemented)
												break;
			  							case 9: // Button L2
  											pad_p->m_L2_p->getDevIO()->write(js.value, timestamp); break;
			  							case 10: // Button R2
  											pad_p->m_R2_p->getDevIO()->write(js.value, timestamp); break;
			  							default:
												break;
											}
                     break;
                case JS_EVENT_AXIS:
                   switch (js.number) {
											case 0: // Joystick 2 X axis
												pad_p->m_axisX2_p->getDevIO()->write(js.value, timestamp); break;
											case 1: // Joystick 2 Y axis
												pad_p->m_axisY2_p->getDevIO()->write(js.value, timestamp); break;
											case 2: // Slider 
												pad_p->m_slider_p->getDevIO()->write(js.value, timestamp); break;
											case 3: // Joystick 1 X axis
												pad_p->m_axisX1_p->getDevIO()->write(js.value, timestamp); break;
											case 4: // Joystick 1 Y axis
												pad_p->m_axisY1_p->getDevIO()->write(js.value, timestamp); break;
											case 5: // Joystick 3 Y axis
												pad_p->m_axisY3_p->getDevIO()->write(js.value, timestamp); break;
											case 6: // Joystick 3 X axis
												pad_p->m_axisX3_p->getDevIO()->write(js.value, timestamp); break;
											default:
												break;
											}
                  break;
                 }

                    //gxprint_resety();
                } else if (errno != EAGAIN) {
                    //ACS_SHORT_LOG((LM_INFO, "error reading"));
                }
          }
       }

    ACS_STATIC_SHORT_LOG((LM_INFO, "%s: terminating thread", myself_p->getName().c_str()));


    if (BACIThread::DoneThread)
        {
        BACIThread::DoneThread();
        }

    delete baciParameter_p;
    myself_p->setStopped();
}
