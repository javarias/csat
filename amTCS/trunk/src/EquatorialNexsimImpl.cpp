#include <vltPort.h>
static char *rcsId=(char *)"@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include <SerialRS232.h>

#include "EquatorialNexsimImpl.h"
#include "NexsimCoordDevIO.h"
#include "NexsimVelDevIO.h"

#include "csatErrors.h"

using namespace baci;

/* Constructor */
EquatorialNexsimImpl::EquatorialNexsimImpl(const ACE_CString& name, maci::ContainerServices *containerServices) :
       CharacteristicComponentImpl(name,containerServices)
      ,m_realAzm_sp(this)
      ,m_realAlt_sp(this)
      ,m_altVel_sp(this)
      ,m_azmVel_sp(this)
      ,m_mount_sp(this)
{
	component_name = name.c_str();
	ACS_TRACE("EquatorialNexsimImpl::EquatorialNexsimImpl");
	m_locking = true;
}

/* Destructor */
EquatorialNexsimImpl::~EquatorialNexsimImpl(){
	m_simulator->off();
}

/* Component Lifecycle */
void EquatorialNexsimImpl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)//,csatErrors::CannotOpenDeviceEx)
{
	const char * _METHOD_ = "EquatorialNexsimImpl::initialize";
	ACS_TRACE("EquatorialNexsimImpl::initialize");

	// We get the simulator component
	m_simulator = NEXSIM_MODULE::NexSim::_nil();
	m_simulator = getContainerServices()->getDefaultComponent<NEXSIM_MODULE::NexSim>("IDL:alma/NEXSIM_MODULE/NexSim:1.0");

	// And we put power it up
	m_simulator->on();

	if( CORBA::is_nil(m_simulator.in()) ){
		throw acsErrTypeLifeCycle::LifeCycleExImpl(__FILE__,__LINE__,_METHOD_);
	}
	
	if( getComponent() != 0){

		// Initialize the DevIOs
		m_realAlt_sp = new ROdouble( ( component_name + std::string(":realAlt")).c_str(),
	   	                          getComponent(), new NexsimCoordDevIO(m_simulator, ALTITUDE_AXIS, true));
		m_realAzm_sp = new ROdouble( (component_name + std::string(":realAzm")).c_str(),
			                          getComponent(), new NexsimCoordDevIO(m_simulator, AZIMUTH_AXIS, false));

		m_altVel_sp  = new RWdouble( ( component_name + std::string(":altVel")).c_str(),
	   	                          getComponent(), new NexsimVelDevIO(m_simulator, ALTITUDE_AXIS, true));
		m_azmVel_sp  = new RWdouble( ( component_name + std::string(":azmVel")).c_str(),
	   	                          getComponent(), new NexsimVelDevIO(m_simulator, AZIMUTH_AXIS, false));
		m_mount_sp   = new ROEnumImpl<ACS_ENUM_T(DEVTELESCOPE_MODULE::mountType),
		                              POA_DEVTELESCOPE_MODULE::ROmountType>
		                            (( component_name + std::string(":mount")).c_str(),
		                             getComponent() );
	}
}


/* IDL implementation */
void EquatorialNexsimImpl::setCurrentAltAz(const TYPES::AltazPos &p) throw (CORBA::SystemException){
	ACS_TRACE("EquatorialNexsimImpl::setCurrentAlzAz");
}

void EquatorialNexsimImpl::setVel(const TYPES::AltazVel &vel) throw (CORBA::SystemException){

	azmVel()->set_sync(vel.azVel);
	altVel()->set_sync(vel.altVel);
	return;
}

void EquatorialNexsimImpl::lock() throw (CORBA::SystemException){
	m_locking = true;
}

void EquatorialNexsimImpl::unlock() throw (CORBA::SystemException){
	m_locking = false;
}


TYPES::AltazVel EquatorialNexsimImpl::getVel() throw (CORBA::SystemException){
	TYPES::AltazVel velocity;

	ACSErr::Completion_var completion;
	velocity.azVel  = azmVel()->get_sync(completion.out());
	velocity.altVel = altVel()->get_sync(completion.out());
	return velocity;
}

bool EquatorialNexsimImpl::locking() throw (CORBA::SystemException){
	return m_locking;
}

/* Properties returning */
ACS::ROdouble_ptr EquatorialNexsimImpl::realAzm() throw (CORBA::SystemException){
	if( m_realAzm_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAzm_sp->getCORBAReference());
	return prop._retn();
}

ACS::ROdouble_ptr EquatorialNexsimImpl::realAlt() throw (CORBA::SystemException){
	if( m_realAlt_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAlt_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWdouble_ptr EquatorialNexsimImpl::azmVel() throw (CORBA::SystemException){
	if( m_azmVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_azmVel_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWdouble_ptr EquatorialNexsimImpl::altVel() throw (CORBA::SystemException){
	if( m_altVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_altVel_sp->getCORBAReference());
	return prop._retn();
}

DEVTELESCOPE_MODULE::ROmountType_ptr EquatorialNexsimImpl::mount() throw (CORBA::SystemException){
	if( m_mount_sp == 0 ){
		return DEVTELESCOPE_MODULE::ROmountType::_nil();
	}
	DEVTELESCOPE_MODULE::ROmountType_var prop = DEVTELESCOPE_MODULE::ROmountType::_narrow(m_mount_sp->getCORBAReference());
	return prop._retn();
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(EquatorialNexsimImpl)
/* ----------------------------------------------------------------*/

/*___oOo___*/
