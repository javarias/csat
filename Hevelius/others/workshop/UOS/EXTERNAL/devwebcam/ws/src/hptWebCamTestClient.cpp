/*
 * "@(#) $Id: hptWebCamTestClient.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: hptWebCamTestClient.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 * Revision 1.1  2007/06/25 13:24:22  jibsen
 * External files.
 *
 * Revision 1.1  2004/07/20 15:56:06  tjuerges
 * - Added latest kernel modules for Philips ToUCam PWC-740K model, including decompressor library (binary only) which allows the camera to take frames of 640*480 at 15 fps.
 *
 * Revision 1.1  2004/07/16 15:17:41  tjuerges
 * - Jetzt mit Sources des Kernel-Modules. Aufgesplittet in lcu/ws.
 *
 * Revision 2.3  2004/07/15 11:57:50  tjuerges
 * - Anstelle ACE-Threads QThreads.
 * - GUI erweitert.
 * - Freigeben der Komponente hinzugefügt.
 * - Von /dev/video1 nach /dev/video2.
 * - Viel aufgeräumt.
 *
 *
*/
#include <vltPort.h>
#include <acsutil.h>
#include <maciSimpleClient.h>
#include "hptWebCamC.h"
#include <baciS.h>

/*
 * $Id: hptWebCamTestClient.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $
 *
 * $Log: hptWebCamTestClient.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 * Revision 1.1  2007/06/25 13:24:22  jibsen
 * External files.
 *
 * Revision 1.1  2004/07/20 15:56:06  tjuerges
 * - Added latest kernel modules for Philips ToUCam PWC-740K model, including decompressor library (binary only) which allows the camera to take frames of 640*480 at 15 fps.
 *
 * Revision 1.1  2004/07/16 15:17:41  tjuerges
 * - Jetzt mit Sources des Kernel-Modules. Aufgesplittet in lcu/ws.
 *
 * Revision 2.3  2004/07/15 11:57:50  tjuerges
 * - Anstelle ACE-Threads QThreads.
 * - GUI erweitert.
 * - Freigeben der Komponente hinzugefügt.
 * - Von /dev/video1 nach /dev/video2.
 * - Viel aufgeräumt.
 *
 * Revision 1.45  2004/07/12 09:32:19  tjuerges
 * - Fehler in Generierung der *Func.cpp behoben. Bei Rückgabe eines ORBA_var Typs muß die Variable explizit mit new erzeugt werden. Außerdem muß es return c._retn() sein.
 *
 * Hinweise von Martin:
 * - Einrückungen. Das wo es möglich ist, habe ich dies beherzigt.
 * - Struktur der If/else. Ich habe sämtlichen Code meinem Lieblingsaussehen angepaßt.
 * - Log hinzugefügt.
 * - vim-Key hinzugefügt.
 *
 * - Außerdem noch einige Änderungen kosmetischer Natur.
 *
 *
*/

#include <logging.h>

using namespace maci;

int main(int argc, char* argv[])
{
/*
 * Checks command line arguments.
 * We need the name of the device to get in touch with.
*/
	if(argc < 2)
	{
		ACS_SHORT_LOG((LM_INFO, "Usage: %s <Component name> <options>", argv[0]));
		return -1;
	}

	// Creates and initialises the SimpleClient object
	SimpleClient client;

	if(client.init(argc,argv) == 0)
	{
		ACS_SHORT_LOG((LM_DEBUG,"Cannot init client"));
		return -1;
	}

	ACS_SHORT_LOG((LM_INFO, "Welcome to WebCamTestClient!"));
	ACS_SHORT_LOG((LM_INFO, "Login in maciManager..."));
	client.login();

	try
	{
		// List all components the Manager knows of our type.
		ACS_SHORT_LOG((LM_INFO, "Listing all COBs of type *WebCam*"));
		maci::HandleSeq seq;
		maci::ComponentInfoSeq_var cobs = client.manager()->get_component_info(client.handle(), seq, "*", "*WebCam*", false);

		for(CORBA::ULong i=0; i<cobs->length(); i++)
		{
			ACS_SHORT_LOG((LM_INFO,"%s (%s)", cobs[i].name.in(), cobs[i].type.in()));
		}

		// Now get the specific component we have requested on the command line.
		ACS_SHORT_LOG((LM_INFO, "Getting component %s...", argv[1]));
		hptWebCam::WebCam_var component = client.get_object<hptWebCam::WebCam>(argv[1], 0, true);

		if(!CORBA::is_nil(component.in()))
		{
			// Prints the descriptor of the requested component.
			ACS_SHORT_LOG((LM_DEBUG, "Requesting descriptor()... "));
			ACS::CharacteristicComponentDesc_var descriptor = component->descriptor();

			ACS_SHORT_LOG((LM_DEBUG, "Got descriptor()."));
			ACS_SHORT_LOG((LM_INFO,"Descriptor:"));
			ACS_SHORT_LOG((LM_INFO,"\tname: %s", descriptor->name.in()));

			ACSErr::Completion_var completion;

			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:palette", argv[1]));
			ACS::RWlong_var palette = component->palette();
			if(palette.ptr() != ACS::RWlong::_nil())
			{ 
				CORBA::Long val = palette->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:colour", argv[1]));
			ACS::RWlong_var colour = component->colour();
			if(colour.ptr() != ACS::RWlong::_nil())
			{ 
				CORBA::Long val = colour->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:minWidth", argv[1]));
			ACS::ROlong_var minWidth = component->minWidth();
			if(minWidth.ptr() != ACS::ROlong::_nil())
			{ 
				CORBA::Long val = minWidth->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:actHeight", argv[1]));
			ACS::ROlong_var actHeight = component->actHeight();
			if(actHeight.ptr() != ACS::ROlong::_nil())
			{ 
				CORBA::Long val = actHeight->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:framesPerSecond", argv[1]));
			ACS::RWlong_var framesPerSecond = component->framesPerSecond();
			if(framesPerSecond.ptr() != ACS::RWlong::_nil())
			{ 
				CORBA::Long val = framesPerSecond->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:automaticGainControl", argv[1]));
			ACS::RWlong_var automaticGainControl = component->automaticGainControl();
			if(automaticGainControl.ptr() != ACS::RWlong::_nil())
			{ 
				CORBA::Long val = automaticGainControl->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:compression", argv[1]));
			ACS::RWlong_var compression = component->compression();
			if(compression.ptr() != ACS::RWlong::_nil())
			{ 
				CORBA::Long val = compression->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:maxWidth", argv[1]));
			ACS::ROlong_var maxWidth = component->maxWidth();
			if(maxWidth.ptr() != ACS::ROlong::_nil())
			{ 
				CORBA::Long val = maxWidth->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:actWidth", argv[1]));
			ACS::ROlong_var actWidth = component->actWidth();
			if(actWidth.ptr() != ACS::ROlong::_nil())
			{ 
				CORBA::Long val = actWidth->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:maxHeight", argv[1]));
			ACS::ROlong_var maxHeight = component->maxHeight();
			if(maxHeight.ptr() != ACS::ROlong::_nil())
			{ 
				CORBA::Long val = maxHeight->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:automaticWhiteBalance", argv[1]));
			ACS::RWlong_var automaticWhiteBalance = component->automaticWhiteBalance();
			if(automaticWhiteBalance.ptr() != ACS::RWlong::_nil())
			{ 
				CORBA::Long val = automaticWhiteBalance->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:minHeight", argv[1]));
			ACS::ROlong_var minHeight = component->minHeight();
			if(minHeight.ptr() != ACS::ROlong::_nil())
			{ 
				CORBA::Long val = minHeight->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:contrast", argv[1]));
			ACS::RWlong_var contrast = component->contrast();
			if(contrast.ptr() != ACS::RWlong::_nil())
			{ 
				CORBA::Long val = contrast->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:gamma", argv[1]));
			ACS::RWlong_var gamma = component->gamma();
			if(gamma.ptr() != ACS::RWlong::_nil())
			{ 
				CORBA::Long val = gamma->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:brightness", argv[1]));
			ACS::RWlong_var brightness = component->brightness();
			if(brightness.ptr() != ACS::RWlong::_nil())
			{ 
				CORBA::Long val = brightness->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
			ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:fps", argv[1]));
			ACS::RWlong_var fps = component->fps();
			if(fps.ptr() != ACS::RWlong::_nil())
			{ 
				CORBA::Long val = fps->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
			}
		} /* end if DO reference OK */
	} /* end main try */
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR, "Error in TestClient::main!"));
	}

	/* Another try section where we release our component
	* and logout from the Manager.
	*/
	try
	{
		ACS_SHORT_LOG((LM_INFO,"Releasing..."));
		client.manager()->release_component(client.handle(), argv[1]);
		client.logout();
	}
	catch(...)
	{
		ACS_SHORT_LOG((LM_ERROR, "Error in TestClient::main!"));
	}

	/*
	* sleep for 3 sec to allow everytihng to cleanup and stabilize
	* so that the tests can be determinitstic.
	*/
	ACE_OS::sleep(3);
	return 0;
}
