#include <vltPort.h>
static char *rcsId="@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);
#include <baciDB.h>

#include <DevTelescopeImpl.h>

using namespace baci;

DevTelescopeImpl::DevTelescopeImpl(const ACE_CString& name, maci::ContainerServices *
           containerServices):
           CharacteristicComponentImpl(name,containerServices)
           ,m_azimuth_sp(this)
           ,m_altitude_sp(this)
{
	component_name = name.c_str();
	ACS_TRACE("DevTelescopeImpl::DevTelescopeImpl");
}

DevTelescopeImpl::~DevTelescopeImpl(){
}

void DevTelescopeImpl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)
{
	ACS_TRACE("DevTelescopeImpl::initialize");
	if( getComponent() != 0){
		m_azimuth_sp = new ROdouble( (component_name + std::string(":azimuth")).c_str(),
		                             getComponent());
		m_altitude_sp = new ROdouble( (component_name + std::string(":altitude")).c_str(),
		                             getComponent());
	}
}

void DevTelescopeImpl::execute() throw (acsErrTypeLifeCycle::LifeCycleExImpl)
{
	ACS_TRACE("DevTelescopeImpl::execute");
}

void DevTelescopeImpl::cleanUp(){
}

void DevTelescopeImpl::aboutToAbort(){
}

void DevTelescopeImpl::goTo(TYPES::AltazPos p){
	ACS_TRACE("DevTelescopeImpl::goTo");
}

ACS::ROdouble_ptr DevTelescopeImpl::azimuth() throw (CORBA::SystemException){
	if( m_azimuth_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_azimuth_sp->getCORBAReference());
	return prop._retn();
}

ACS::ROdouble_ptr DevTelescopeImpl::altitude() throw (CORBA::SystemException){
	if( m_azimuth_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_altitude_sp->getCORBAReference());
	return prop._retn();
}

///* --------------- [ MACI DLL support functions ] -----------------*/
//#include <maciACSComponentDefines.h>
//MACI_DLL_SUPPORT_FUNCTIONS(DevTelescopeImpl)
///* ----------------------------------------------------------------*/
  
  
  /*___oOo___*/
