#include <vltPort.h>
static char *rcsId=(char *)"@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include <SerialRS232.h>

#include "EquatorialNexstarImpl.h"
#include "NexstarCoordDevIO.h"
#include "NexstarVelDevIO.h"
#include "csatErrors.h"

using namespace baci;

/* Constructor */
EquatorialNexstarImpl::EquatorialNexstarImpl(const ACE_CString& name, maci::ContainerServices *containerServices) :
       CharacteristicComponentImpl(name,containerServices)
      ,m_realAzm_sp(this)
      ,m_realAlt_sp(this)
      ,m_altVel_sp(this)
      ,m_azmVel_sp(this)
      ,m_mount_sp(this)
{
	component_name = name.c_str();
	ACS_TRACE("EquatorialNexstarImpl::EquatorialNexstarImpl");
	m_locking = true;
}

/* Destructor */
EquatorialNexstarImpl::~EquatorialNexstarImpl(){
}

/* Component Lifecycle */
void EquatorialNexstarImpl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)
{
	const char * _METHOD_ = "EquatorialNexstarImpl::initialize";
	ACS_TRACE("EquatorialNexstarImpl::initialize");
	if( getComponent() != 0){

		NexstarCoordDevIO *azmDevIO = NULL;
		NexstarCoordDevIO *altDevIO = NULL;
		NexstarVelDevIO   *altVelDevIO = NULL;
		NexstarVelDevIO   *azmVelDevIO = NULL;

		try{
			azmDevIO = new NexstarCoordDevIO((char *)"/dev/ttyS0", AZIMUTH_AXIS, false);
			altDevIO = new NexstarCoordDevIO((char *)"/dev/ttyS0", ALTITUDE_AXIS, true);
			azmVelDevIO = new NexstarVelDevIO((char *)"/dev/ttyS0", AZIMUTH_AXIS, false);
			altVelDevIO = new NexstarVelDevIO((char *)"/dev/ttyS0", ALTITUDE_AXIS, true);
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
	   	                          getComponent(), altVelDevIO);
		m_azmVel_sp  = new RWdouble( ( component_name + std::string(":azmVel")).c_str(),
	   	                          getComponent(), azmVelDevIO);

		m_mount_sp   = new ROEnumImpl<ACS_ENUM_T(DEVTELESCOPE_MODULE::mountType),
		                              POA_DEVTELESCOPE_MODULE::ROmountType>
		                            (( component_name + std::string(":mount")).c_str(),
		                             getComponent() );
	}
}


/* IDL implementation */
void EquatorialNexstarImpl::setCurrentAltAz(const TYPES::AltazPos &p) throw (CORBA::SystemException){
	ACS_TRACE("EquatorialNexstarImpl::setCurrentAlzAz");
}

void EquatorialNexstarImpl::setVel(const TYPES::AltazVel &vel) throw (CORBA::SystemException){

	azmVel()->set_sync(vel.azVel );
	altVel()->set_sync(vel.altVel);
	return;
}

void EquatorialNexstarImpl::lock() throw (CORBA::SystemException){
	m_locking = true;
}

void EquatorialNexstarImpl::unlock() throw (CORBA::SystemException){
	m_locking = false;
}


/* Attributes returning */
TYPES::AltazVel EquatorialNexstarImpl::getVel() throw (CORBA::SystemException){
	TYPES::AltazVel velocity;

	ACSErr::Completion_var completion;
	velocity.azVel  = azmVel()->get_sync(completion.out());
	velocity.altVel = altVel()->get_sync(completion.out());
	return velocity;
}

bool EquatorialNexstarImpl::locking() throw (CORBA::SystemException){
	return m_locking;
}

/* Properties returning */

ACS::ROdouble_ptr EquatorialNexstarImpl::realAzm() throw (CORBA::SystemException){
	if( m_realAzm_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAzm_sp->getCORBAReference());
	return prop._retn();
}

ACS::ROdouble_ptr EquatorialNexstarImpl::realAlt() throw (CORBA::SystemException){
	if( m_realAlt_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAlt_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWdouble_ptr EquatorialNexstarImpl::azmVel() throw (CORBA::SystemException){
	if( m_azmVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_azmVel_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWdouble_ptr EquatorialNexstarImpl::altVel() throw (CORBA::SystemException){
	if( m_altVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_altVel_sp->getCORBAReference());
	return prop._retn();
}

DEVTELESCOPE_MODULE::ROmountType_ptr EquatorialNexstarImpl::mount() throw (CORBA::SystemException){
	if( m_mount_sp == 0 ){
		return DEVTELESCOPE_MODULE::ROmountType::_nil();
	}
	DEVTELESCOPE_MODULE::ROmountType_var prop = DEVTELESCOPE_MODULE::ROmountType::_narrow(m_mount_sp->getCORBAReference());
	return prop._retn();
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(EquatorialNexstarImpl)
/* ----------------------------------------------------------------*/

/*___oOo___*/
