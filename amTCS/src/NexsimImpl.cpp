#include <vltPort.h>
static char *rcsId="@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include "NexstarImpl.h"
#include "SerialRS232.h"
#include "NexstarAltDevIO.h"
#include "NexstarAzmDevIO.h"
#include "csatErrors.h"

using namespace baci;

/* Constructor */
NexstarImpl::NexstarImpl(const ACE_CString& name, maci::ContainerServices *containerServices) :
       CharacteristicComponentImpl(name,containerServices)
      ,m_realAzm_sp(this)
      ,m_realAlt_sp(this)
      ,m_altVel_sp(this)
      ,m_azmVel_sp(this)
{
	component_name = name.c_str();
	ACS_TRACE("NexstarImpl::NexstarImpl");
	m_locking = true;
}

/* Destructor */
NexstarImpl::~NexstarImpl(){
}

/* Component Lifecycle */
void NexstarImpl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)//,csatErrors::CannotOpenDeviceEx)
{
	const char * _METHOD_ = "NexstarImpl::initialize";
	ACS_TRACE("NexstarImpl::initialize");
	if( getComponent() != 0){

		NexstarAzmDevIO *azmDevIO = NULL;
		NexstarAltDevIO *altDevIO = NULL;

		try{
			azmDevIO = new NexstarAzmDevIO("/dev/ttyS0");
			altDevIO = new NexstarAltDevIO("/dev/ttyS0");
		} catch (csatErrors::CannotOpenDeviceEx &ex){
			acsErrTypeLifeCycle::LifeCycleExImpl lifeEx(ex,__FILE__,__LINE__,_METHOD_);
			lifeEx.addData("Reason","Cannot create DevIOs");
			throw lifeEx;
		}

		m_realAzm_sp = new ROdouble( (component_name + std::string(":realAzm")).c_str(),
			                             getComponent(), azmDevIO);
		m_realAlt_sp = new ROdouble( ( component_name + std::string(":realAlt")).c_str(),
	   	                          getComponent(), altDevIO);

		m_altVel_sp  = new RWdouble( ( component_name + std::string(":altVel")).c_str(),
	   	                          getComponent());
		m_azmVel_sp  = new RWdouble( ( component_name + std::string(":azmVel")).c_str(),
	   	                          getComponent());
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
TYPES::AltazVel NexstarImpl::getVel() throw (CORBA::SystemException){
	TYPES::AltazVel velocity;

	ACSErr::Completion_var completion;
	velocity.azVel  = azmVel()->get_sync(completion.out());
	velocity.altVel = altVel()->get_sync(completion.out());
	return velocity;
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

ACS::RWdouble_ptr NexstarImpl::azmVel() throw (CORBA::SystemException){
	if( m_azmVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_azmVel_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWdouble_ptr NexstarImpl::altVel() throw (CORBA::SystemException){
	if( m_altVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_altVel_sp->getCORBAReference());
	return prop._retn();
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(NexstarImpl)
/* ----------------------------------------------------------------*/

/*___oOo___*/