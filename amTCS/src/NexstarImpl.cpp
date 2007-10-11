#include <vltPort.h>
static char *rcsId="@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include "NexstarImpl.h"
#include "SerialRS232.h"
#include "NexstarAltDevIO.h"
#include "NexstarAzmDevIO.h"

using namespace baci;

/* Constructor */
NexstarImpl::NexstarImpl(const ACE_CString& name, maci::ContainerServices *containerServices) :
       CharacteristicComponentImpl(name,containerServices)
      ,m_realAzm_sp(this)
      ,m_realAlt_sp(this)
{
	component_name = name.c_str();
	ACS_TRACE("NexstarImpl::NexstarImpl");
	m_locking = true;
}

/* Destructor */
NexstarImpl::~NexstarImpl(){
}

/* Component Lifecycle */
void NexstarImpl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)
{
	ACS_TRACE("NexstarImpl::initialize");
	if( getComponent() != 0){
		m_realAzm_sp = new ROdouble( (component_name + std::string(":realAzm")).c_str(),
		                             getComponent(), new NexstarAzmDevIO("/dev/ttyS0"));
		m_realAlt_sp = new ROdouble( (component_name + std::string(":realAlt")).c_str(),
		                             getComponent(), new NexstarAltDevIO("/dev/ttyS0"));
	}
}


/* IDL implementation */
void NexstarImpl::setCurrentAltAz(const TYPES::AltazPos &p) throw (CORBA::SystemException){
	ACS_TRACE("NexstarImpl::setCurrentAlzAz");
}

void NexstarImpl::setVel(const TYPES::AltazVel &vel) throw (CORBA::SystemException){

	char command[8];
	int movement;

	SerialRS232 *sp = new SerialRS232("/dev/ttyS0");

	/* Setting the Altitude velocity */
	if( vel.azVel > 0 )
		movement = 0x24;
	else
		movement = 0x25;

	command[0] = 'P';
	command[1] = 2;
	command[2] = 0x10;
	command[3] = movement;
	command[4] = labs((long int)vel.azVel);
	command[5] = 0;
	command[6] = 0;
	command[7] = 0;

	sp->write_RS232(command,8);
	sp->read_RS232();

	/* Setting the azimuth velocity */
	if( vel.altVel > 0 )
		movement = 0x24;
	else
		movement = 0x25;

	command[0] = 'P';
	command[1] = 2;
	command[2] = 0x11;
	command[3] = movement;
	command[4] = labs((long int)vel.altVel);
	command[5] = 0;
	command[6] = 0;
	command[7] = 0;

	sp->write_RS232(command,8);
	sp->read_RS232();
	delete sp;
}

void NexstarImpl::lock() throw (CORBA::SystemException){
	m_locking = true;
}

void NexstarImpl::unlock() throw (CORBA::SystemException){
	m_locking = false;
}


/* Attributes returning */
TYPES::AltazVel NexstarImpl::velocity() throw (CORBA::SystemException){
	return m_velocity;
}

bool NexstarImpl::locking() throw (CORBA::SystemException){
	return m_locking;
}

/* Properties returning */

ACS::ROdouble_ptr NexstarImpl::realAzm() throw (CORBA::SystemException){
	if( m_realAzm_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAzm_sp->getCORBAReference());
	return prop._retn();
}

ACS::ROdouble_ptr NexstarImpl::realAlt() throw (CORBA::SystemException){
	if( m_realAlt_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAlt_sp->getCORBAReference());
	return prop._retn();
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(NexstarImpl)
/* ----------------------------------------------------------------*/

/*___oOo___*/
