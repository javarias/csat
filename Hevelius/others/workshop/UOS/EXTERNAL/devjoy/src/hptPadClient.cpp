/*
  "@(#) $Id: hptPadClient.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
*/
#include <vltPort.h>
#include <acsutil.h>
#include <maciSimpleClient.h>
#include "hptPadC.h"
#include <baciS.h>

NAMESPACE_USE(maci);

int main(int argc, char *argv[])
{

    /*
     * Checks command line arguments.
     * We need the name of the device to get in touch with.
     */
    if (argc < 2)
        {
        ACS_SHORT_LOG((LM_INFO, "Usage: %s <COB name> <options>", argv[0]));
        return -1;
        }

    /*
     * Creates and initialises the SimpleClient object
     */
    SimpleClient client;

    if (client.init(argc,argv) == 0)
        {
        ACE_DEBUG((LM_DEBUG,"Cannot init client"));
        return -1;
        }

    ACS_SHORT_LOG((LM_INFO, "Welcome to $$prefixClient!"));
    ACS_SHORT_LOG((LM_INFO, "Login into maciManager!"));
    client.login();


    try
        {

        /*
         * List all DOs of type "" the Manager knows of.
         */
        ACS_SHORT_LOG((LM_INFO, "Listing all COBs of type *Pad*"));
        maci::HandleSeq seq;
        maci::ComponentInfoSeq_var cobs = client.manager()->get_component_info(client.handle(), seq, "*", "*Pad*",
                                                                               false);

        for (CORBA::ULong i=0; i<cobs->length(); i++)
            {
            ACS_SHORT_LOG((LM_INFO,"%s (%s)", cobs[i].name.in(), cobs[i].type.in()));
            }

        /*
         * Now gets the specific DO we have requested on the command line
         */
        ACS_SHORT_LOG((LM_INFO, "Getting COB: %s", argv[1]));
        hptPad::Pad_var mount = client.get_object<hptPad::Pad>(argv[1], 0, true);

        if (!CORBA::is_nil(mount.in()))
            {
            /*
             * Prints the descriptor of the requested DO
             */
            ACS_SHORT_LOG((LM_DEBUG, "Requesting descriptor()... "));
            ACS::CharacteristicComponentDesc_var descriptor = mount->descriptor();

            ACS_SHORT_LOG((LM_DEBUG, "Got descriptor()."));
            ACS_SHORT_LOG((LM_INFO,"Descriptor:"));
            ACS_SHORT_LOG((LM_INFO,"\tname: %s", descriptor->name.in()));

            ACSErr::Completion_var completion; 

            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:btnX", argv[1]));
            ACS::ROlong_var btnX = mount->btnX();
            if (btnX.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = btnX->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:btnY", argv[1]));
            ACS::ROlong_var btnY = mount->btnY();
            if (btnY.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = btnY->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:btnZ", argv[1]));
            ACS::ROlong_var btnZ = mount->btnZ();
            if (btnZ.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = btnZ->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:L1", argv[1]));
            ACS::ROlong_var L1 = mount->L1();
            if (L1.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = L1->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:axisX1", argv[1]));
            ACS::ROlong_var axisX1 = mount->axisX1();
            if (axisX1.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = axisX1->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:btnA", argv[1]));
            ACS::ROlong_var btnA = mount->btnA();
            if (btnA.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = btnA->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:L2", argv[1]));
            ACS::ROlong_var L2 = mount->L2();
            if (L2.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = L2->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:axisX2", argv[1]));
            ACS::ROlong_var axisX2 = mount->axisX2();
            if (axisX2.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = axisX2->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:slider", argv[1]));
            ACS::ROlong_var slider = mount->slider();
            if (slider.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = slider->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:btnB", argv[1]));
            ACS::ROlong_var btnB = mount->btnB();
            if (btnB.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = btnB->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:axisY1", argv[1]));
            ACS::ROlong_var axisY1 = mount->axisY1();
            if (axisY1.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = axisY1->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:btnC", argv[1]));
            ACS::ROlong_var btnC = mount->btnC();
            if (btnC.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = btnC->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:axisX3", argv[1]));
            ACS::ROlong_var axisX3 = mount->axisX3();
            if (axisX3.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = axisX3->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:axisY2", argv[1]));
            ACS::ROlong_var axisY2 = mount->axisY2();
            if (axisY2.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = axisY2->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:axisY3", argv[1]));
            ACS::ROlong_var axisY3 = mount->axisY3();
            if (axisY3.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = axisY3->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:R1", argv[1]));
            ACS::ROlong_var R1 = mount->R1();
            if (R1.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = R1->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }
            ACS_SHORT_LOG((LM_INFO, "Getting COB property: %s:R2", argv[1]));
            ACS::ROlong_var R2 = mount->R2();
            if (R2.ptr() != ACS::ROlong::_nil())
               { 
                CORBA::Long val = R2->get_sync(completion.out());
                ACS_SHORT_LOG((LM_INFO,"Value: %f", val));
               }

            } /* end if DO reference OK */
        } /* end main try */
    catch(...)
        {
        ACE_ERROR((LM_ERROR, "main"));
        }

    /*
     * Another try section where we release our COB and logout from the Manager
     */
    try
        {
        ACS_SHORT_LOG((LM_INFO,"Releasing..."));
        client.manager()->release_component(client.handle(), argv[1]);
        client.logout();
        }
    catch(...)
        {
        ACE_ERROR((LM_ERROR, "main"));
        }

    /*
     * sleep for 3 sec to allow everytihng to cleanup and stabilyse
     * so that the tests can be determinitstic.
     */
    ACE_OS::sleep(3);
    return 0;
}
/*___oOo___*/
