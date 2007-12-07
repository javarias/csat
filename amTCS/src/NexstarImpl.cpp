#include <vltPort.h>
static char *rcsId="@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include "NexstarImpl.h"
#include "SerialRS232.h"
#include "NexstarCoordDevIO.h"
#include "NexstarVelDevIO.h"
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

		NexstarCoordDevIO *azmDevIO = NULL;
		NexstarCoordDevIO *altDevIO = NULL;
		NexstarVelDevIO   *altVelDevIO = NULL;
		NexstarVelDevIO   *azmVelDevIO = NULL;

		try{
			azmDevIO = new NexstarCoordDevIO("/dev/ttyS0", AZIMUTH_AXIS);
			altDevIO = new NexstarCoordDevIO("/dev/ttyS0", ALTITUDE_AXIS);
			azmVelDevIO = new NexstarVelDevIO("/dev/ttyS0", AZIMUTH_AXIS);
			altVelDevIO = new NexstarVelDevIO("/dev/ttyS0", ALTITUDE_AXIS);
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
	   	                          getComponent(), azmVelDevIO);
		m_azmVel_sp  = new RWdouble( ( component_name + std::string(":azmVel")).c_str(),
	   	                          getComponent(), altVelDevIO);
	}
}


/* IDL implementation */
void NexstarImpl::setCurrentAltAz(const TYPES::AltazPos &p) throw (CORBA::SystemException){
	ACS_TRACE("NexstarImpl::setCurrentAlzAz");
}

void NexstarImpl::setVel(const TYPES::AltazVel &vel) throw (CORBA::SystemException){

	azmVel()->set_sync(vel.azVel );
	altVel()->set_sync(vel.altVel);
	return;
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
