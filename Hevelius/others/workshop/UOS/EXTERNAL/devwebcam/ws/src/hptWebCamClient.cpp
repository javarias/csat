#include <maciSimpleClient.h>
#include <baciS.h>
#include <baciCORBA.h>
#include <hptWebCamC.h>
#include <ccvt.h>
#include <ccvt_types.h>

#include <stdio.h>
NAMESPACE_USE(maci);


#include "ace/Thread.h"
#include "ace/Synch.h"

#include <qapplication.h>
#include <qpushbutton.h>

#include <qvariant.h>
#include <qpixmap.h>
#include <qwidget.h>

#include <qslider.h>
#include <qlcdnumber.h>
#include <qfont.h>

#include <qlabel.h>
#include <qvbox.h>
#include <qcombobox.h>
#include <qpixmap.h>
#include <qimage.h>
#include <qpainter.h>

#include ".ui/hptWebCamGUI.h"
#include <zlib.h>

#include <iostream>


ACE_Mutex frameTakerMutex;
bool  theEnd = FALSE;
static const char *webCamResolutionsStr[] = { "320x240", "640x480", 0};
static const int webCamResolutions[2][2] = {{320, 240}, {640, 480}};

#define CHECK_ERR(err, msg) { \
	if (err == Z_MEM_ERROR) { \
		fprintf(stderr, "%s error Z_MEM_ERROR\n", msg); \
	} \
	if (err == Z_BUF_ERROR) { \
		fprintf(stderr, "%s error Z_BUF_ERROR\n", msg); \
	} \
	if (err == Z_DATA_ERROR) { \
		fprintf(stderr, "%s error Z_DATA_ERROR\n", msg); \
	} \
	if (err != Z_OK) { \
		fprintf(stderr, "%s error: %d\n", msg, err); \
	} \
}

hptWebCam::WebCam_var dev;

void hptWebCamGUI::myOn()
{
 long value;
 ACE_OS::printf("switching WebCam on\n");
 try
      {
        dev->on();
        
        ACSErr::Completion_var c = new ACSErr::Completion();

        value = 20000;
        dev->brightness()->set_sync(value);
        value = 10000;
        dev->gamma()->set_sync(value);
        dev->setFrameSize(webCamResolutions[0][0], webCamResolutions[0][1]);
      }
      catch(...)
        {
        }
}



void hptWebCamGUI::myOff()
{
 frameTakerMutex.release();
 theEnd = true; 
 ACE_OS::printf("switching WebCam off\n");
 try
      {
        dev->off();
      }
      catch(...)
        {
        }
}

unsigned long val[250000];

hptWebCamGUI *tGUI;

static void *frameTaker(void *arg)
{
	ACE_OS::printf("Thread: waiting for mutex\n");
	frameTakerMutex.acquire();
	frameTakerMutex.release();
	ACE_OS::printf("Thread: mutex received\n");

	unsigned char tmp[2000000];
	unsigned char tmp2[2000000];
	long ltmp;

	QPixmap pm;
	Byte *uncompr;
	uLong comprLen, uncomprLen, len;
	int err, i;
	unsigned long tmpLong;
	ACE_Time_Value timeFrameStart, timeFrameElapsed;
	int counter = 0;
	double fps;
	char strFps[30];
	sprintf(strFps, "Hallo");
        int width, height, res;

	try
		{
		while (1)
			{
				if (theEnd) return 0;
                                res = tGUI->resolutionBox->currentItem();
                                width = webCamResolutions[res][0];
                                height = webCamResolutions[res][1];
				ACS::longSeq *frame = dev->getFrame();
				if (counter == 0) timeFrameStart = ACE_OS::gettimeofday();
				
				len     =  frame->length();
				tmpLong =  (*frame)[0];
				
				i = 0;
				for(int j = 1; j < (int)(frame->length()); ++j)
					{
						ltmp = (*frame)[j];
						tmpLong = 0;
						memcpy(&tmpLong, &ltmp, sizeof(long));;
						tmp[i++] = tmpLong >> 24;
						tmp[i++] = (tmpLong >> 16) & 0xff;
						tmp[i++] = (tmpLong >> 8) & 0xff;
						tmp[i++] = tmpLong & 0xff;
					}

				len = (*frame)[0]+1;
				comprLen = len * sizeof(int);
				uncomprLen = 921601 * sizeof(int);

				uncompr = (Byte*)calloc((uInt)uncomprLen, 1);

				if (uncompr == Z_NULL) ACS_SHORT_LOG((LM_INFO, "webcamFrameIO::read out of memoryn"));

				err = uncompress(uncompr, &uncomprLen, (const Bytef*)(tmp), comprLen);
				if (err == Z_DATA_ERROR) printf("Z_DATA_ERROR\n");
				if (err == Z_OK) {
					for(int j = 0; j < (int)(uncomprLen); ++j)
						tmp[j] = uncompr[j];

					ACE_DEBUG((LM_INFO,"converting frame"));
					ccvt_420p_bgr32(width, height, tmp, tmp2);
					QRgb null_palette;
					QImage camImage(tmp2,width,height,32,&null_palette,255,QImage::BigEndian);
					pm.convertFromImage(camImage);
					
					//put_image_ppm(tmp2, 320, 240, 0, 0);
					//put_image_png(tmp2, 320, 240);
					//pm = QPixmap("testpict.png");

					//tGUI->webCamPixMap->setPixmap(pm);
					/*
					  Thomas:
					  Das Painter-Widget ist wesentlich
					  schneller und somit besser geeignet,
					  schnelle Pixel-Operationen
					  durchzuführen.
					  1. QPainter p; (Malfläche festlegen)
					  2. p.begin(irgendein Widget); (Malfläche mit Widget verbinden)
					  3. p.drawPixmap(startX,startY,*QPixmap);
					  4. p.end(); (Graphikoperation zu Ende.
					*/
					QPainter p;
					p.begin(tGUI->webCamPixMap);
					p.drawPixmap(0,0,pm);
					p.end();
					//tGUI->webCamPixMap->setBackgroundMode( tGUI->NoBackground );
					//tGUI->update();
				}
				free(uncompr);
				delete frame;
				counter++;
				if (counter == 10){
					timeFrameElapsed = ACE_OS::gettimeofday() - timeFrameStart;
					fps = 10.0/ (timeFrameElapsed.sec() + (double)(  timeFrameElapsed.usec())/1.0E6);
					//printf("frames per second = %f\n", fps);
					sprintf(strFps, "%.2f", fps);
					counter = 0;
					tGUI->framesPerSecond->setText(strFps);
					tGUI->update();
				}
			}
		ACE_OS::printf("all done\n");
		}
	catch(...)
        {
        }
  return 0;
}

void hptWebCamGUI::myTakeFrames()
{
 tGUI = this;
 frameTakerMutex.release();
}

void hptWebCamGUI::newResolution()
{
   int res = resolutionBox->currentItem();
   CORBA::Long height(webCamResolutions[res][0]);
   CORBA::Long width(webCamResolutions[res][1]);
   dev->setFrameSize(height, width); 
}
   

static void *worker(void *arg)
{

    int argc=0;
    char **argv=NULL ;

    QApplication a( argc, argv);

    hptWebCamGUI w;

    a.setMainWidget (&w );

    w.resolutionBox->insertStrList(webCamResolutionsStr);

    w.show();

    a.exec();

    return 0;
}




int main(int argc, char* argv[])
{

  static const char* myVersion = ZLIB_VERSION;
                                                                            

  if (argc != 2)
        {
        std::cout << "Usage: " << argv[0] << " <component name>" << std::endl;
        std::cout << "For example: " << argv[0] << " ARTM" << std::endl;
        return -1;
        }
  ACE_CString componentName = argv[1];

  if (zlibVersion()[0] != myVersion[0]) {
        fprintf(stderr, "incompatible zlib version\n");
        exit(1);
    } else if (strcmp(zlibVersion(), ZLIB_VERSION) != 0) {
        fprintf(stderr, "warning: different zlib version\n");
   }


    

    frameTakerMutex.acquire();

    int n_threads = 1;
    ACE_thread_t *threadID = new ACE_thread_t[n_threads + 1];
    ACE_hthread_t *threadHandles = new ACE_hthread_t[n_threads + 1];

   if (ACE_Thread::spawn_n(threadID,
												n_threads,
												(ACE_THR_FUNC)(worker),
												0,
	THR_JOINABLE | THR_NEW_LWP,
	ACE_DEFAULT_THREAD_PRIORITY,
	0, 0, threadHandles) == -1)
		ACE_DEBUG((LM_DEBUG, "Error in spawning threads \n"));

  int n_fthreads = 1;
  ACE_thread_t *fthreadID = new ACE_thread_t[n_fthreads + 1];
  ACE_hthread_t *fthreadHandles = new ACE_hthread_t[n_fthreads + 1];

  if (ACE_Thread::spawn_n(fthreadID,
	n_fthreads,
	(ACE_THR_FUNC)(frameTaker),
	0,
	THR_JOINABLE | THR_NEW_LWP,
	ACE_DEFAULT_THREAD_PRIORITY,
	0, 0, fthreadHandles) == -1)
		ACE_DEBUG((LM_DEBUG, "Error in spawning threads \n"));
                                                                            
    SimpleClient ci;
                                                                                
    try
        {
       /**
         * Create the instance of Client and Init() it.
         */
        if(ci.init(argc, argv) == 0)
            {
            ACE_DEBUG ((LM_DEBUG, "Cannot init client"));
            return -1;
            }
                                                                                
        if(ci.login() == 0)
            {
            ACE_DEBUG ((LM_DEBUG, "Cannot login"));
            return -1;
            }

        CORBA::Object_var hptObject = ci.get_object(componentName.c_str(),0,true);
        if (CORBA::is_nil(hptObject.in()))
            {
            ACE_DEBUG ((LM_DEBUG, "Cannot get AntMount Object"));
            return -1;
            }
                                                                                
        dev = hptWebCam::WebCam::_narrow (hptObject.in ());
        if (CORBA::is_nil(dev.in()))
            {
            ACE_DEBUG((LM_DEBUG, "Failed to narrow hptWebCam \n"));
            return 0;
            }
        ACE_DEBUG((LM_DEBUG, "Device narrowed.\n"));
       }
       catch(...)
        {
        return -1;
        }
    ACE_CHECK_RETURN (-1);


    ACS_SHORT_LOG((LM_INFO,"wait for GUI"));
    for (int ii=0; ii<n_threads; ii++) ACE_Thread::join(threadHandles[ii]);
                                                                                
    return 0;
}

                                                                                

