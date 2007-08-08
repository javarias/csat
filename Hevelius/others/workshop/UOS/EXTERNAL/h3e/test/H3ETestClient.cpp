/*
 * "@(#) $Id: H3ETestClient.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: H3ETestClient.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#include <acsutil.h>
#include <maciSimpleClient.h>
#include <baciC.h>
#include <logging.h>

#include <H3EC.h>

using namespace maci;


char* cobname;

int main(int argc, char* argv[])
{
	// Check command line arguments
	if (argc < 2)
	{
		cobname = "LegoControl";
	}
	else
	{
		cobname = argv[1];
	}

	// Creates and initializes the SimpleClient object
	SimpleClient client;

	if(client.init(argc,argv) == 0)
	{
		ACS_SHORT_LOG((LM_ERROR,"Cannot init client"));
		return -1;
	}

	ACS_SHORT_LOG((LM_INFO, "Welcome to H3ETestClient!"));
	ACS_SHORT_LOG((LM_INFO, "Login in maciManager..."));
	client.login();

	try
	{
		// List all components the Manager knows of our type.
		ACS_SHORT_LOG((LM_INFO, "Listing all componentes of type *LegoControl*"));
		maci::HandleSeq seq;
		maci::ComponentInfoSeq_var components = 
			       client.manager()->get_component_info(client.handle(), seq, "*", 
								                                  "*LegoControl*", false);

		for (CORBA::ULong i = 0; i < components->length(); i++)
		{
			ACS_SHORT_LOG((LM_INFO,"%s (%s)", components[i].name.in(), 
						                            components[i].type.in()));
		}

		// Now get the specific component we have requested on the command line.
		ACS_SHORT_LOG((LM_INFO, "Getting component LegoControl..."));
		H3E::LegoControl_var component = 
			       client.get_object<H3E::LegoControl>("LegoControl", 0, true);

		if (!CORBA::is_nil(component.in()))
		{
			ACS_SHORT_LOG((LM_INFO, "... got component LegoControl"));
			// Prints the descriptor of the requested component.
			ACS_SHORT_LOG((LM_INFO, "Requesting descriptor()... "));
			ACS::CharacteristicComponentDesc_var descriptor = 
				                                       component->descriptor();

			ACS_SHORT_LOG((LM_INFO, "Descriptor:"));
			ACS_SHORT_LOG((LM_INFO, "\tname: %s", descriptor->name.in()));

			ACSErr::Completion_var completion;


			ACS_SHORT_LOG((LM_INFO, "Getting component property: %s::commandedAltitude", 
						                                                       cobname));
			if (CORBA::is_nil(component->commandedAltitude()) == false)
			{ 
				CORBA::Double value = component->commandedAltitude()->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO, "Value: %f", value));
			}


			ACS_SHORT_LOG((LM_INFO, "Getting component property: %s::commandedAzimuth", 
						                                                       cobname));
			if (CORBA::is_nil(component->commandedAzimuth()) == false)
			{ 
				CORBA::Double value = component->commandedAzimuth()->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO, "Value: %f", value));
			}


			ACS_SHORT_LOG((LM_INFO, "Getting component property: %s::actualAltitude", 
						                                                       cobname));
			if (CORBA::is_nil(component->actualAltitude()) == false)
			{ 
				CORBA::Double value = component->actualAltitude()->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO, "Value: %f", value));
			}


			ACS_SHORT_LOG((LM_INFO, "Getting component property: %s::actualAzimuth", 
						                                                       cobname));
			if (CORBA::is_nil(component->actualAzimuth()) == false)
			{ 
				CORBA::Double value = component->actualAzimuth()->get_sync(completion.out());
				ACS_SHORT_LOG((LM_INFO, "Value: %f", value));
			}


			ACS_SHORT_LOG((LM_INFO, "Getting component property: %s:status", 
						                  cobname));
			if(CORBA::is_nil(component->status()) == false)
			{ 
				ACS_SHORT_LOG((LM_INFO, "No auto generated return type in the "
							                  "acsGenerator found! Ignoring property."));
			}



		} /* end if component reference OK */
		else
		{
			ACS_SHORT_LOG((LM_INFO, "Component LegoControl is nil !!!"));
		}
	} /* end main try */
	catch (...)
	{
		ACS_SHORT_LOG((LM_ERROR, "Error in TestClient::main!"));
	}

	/* Another try section where we release our component
	 * and logout from the Manager.
	 */
	try
	{
		ACS_SHORT_LOG((LM_INFO, "Releasing..."));
		client.manager()->release_component(client.handle(), cobname);
		client.logout();
	}
	catch (...)
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
