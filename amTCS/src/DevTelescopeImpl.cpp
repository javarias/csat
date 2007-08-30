#include <vltPort.h>
static char *rcsId="@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include <DevTelescopeImpl.h>

using namespace baci;

/* Constructor */
DevTelescopeImpl::DevTelescopeImpl(const ACE_CString& name, maci::ContainerServices *containerServices) :
       CharacteristicComponentImpl(name,containerServices)
      ,m_realAzm_sp(this)
      ,m_realAlt_sp(this)
{
	component_name = name.c_str();
	ACS_TRACE("DevTelescopeImpl::DevTelescopeImpl");
}

/* Destructor */
DevTelescopeImpl::~DevTelescopeImpl(){
}

/* Component Lifecycle */
void DevTelescopeImpl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)
{
	ACS_TRACE("DevTelescopeImpl::initialize");
	if( getComponent() != 0){
		m_realAzm_sp = new ROdouble( (component_name + std::string(":realAzm")).c_str(),
		                             getComponent());
		m_realAlt_sp = new ROdouble( (component_name + std::string(":realAlt")).c_str(),
		                             getComponent());
	}
}


/* IDL implementation */
void DevTelescopeImpl::setCurrentAltAz(const TYPES::AltazPos &p) throw (CORBA::SystemException){
	ACS_TRACE("DevTelescopeImpl::setCurrentAlzAz");
}

void DevTelescopeImpl::setVel(const TYPES::AltazVel &vel) throw (CORBA::SystemException){
}

void DevTelescopeImpl::lock() throw (CORBA::SystemException){
	//locking = true;
}

void DevTelescopeImpl::unlock() throw (CORBA::SystemException){
	//locking = false;
}


/* Attributes returning */
TYPES::AltazVel DevTelescopeImpl::velocity() throw (CORBA::SystemException){
	return m_velocity;
}

bool DevTelescopeImpl::locking() throw (CORBA::SystemException){
	return m_locking;
}

/* Properties returning */

ACS::ROdouble_ptr DevTelescopeImpl::realAzm() throw (CORBA::SystemException){
	if( m_realAzm_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAzm_sp->getCORBAReference());
	return prop._retn();
}

ACS::ROdouble_ptr DevTelescopeImpl::realAlt() throw (CORBA::SystemException){
	if( m_realAlt_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAlt_sp->getCORBAReference());
	return prop._retn();
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(DevTelescopeImpl)
/* ----------------------------------------------------------------*/

/*___oOo___*/
