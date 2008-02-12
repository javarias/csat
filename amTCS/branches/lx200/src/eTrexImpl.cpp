#include <vltPort.h>
static char *rcsId="@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include "eTrexImpl.h"
#include "eTrexTimeDevIO.h"

using namespace baci;

/* Constructor */
eTrexImpl::eTrexImpl(const ACE_CString& name, maci::ContainerServices *containerServices) :
       CharacteristicComponentImpl(name,containerServices)
      ,m_time_sp(this)
      ,m_latitude_sp(this)
      ,m_longitude_sp(this)
{
	component_name = name.c_str();
	ACS_TRACE("eTrexImpl::eTrexImpl");
	m_device = "";
	m_locking = true;
}

/* Destructor */
eTrexImpl::~eTrexImpl(){
}

/* Component Lifecycle */
void eTrexImpl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)
{
	ACS_TRACE("eTrexImpl::initialize");
	if( getComponent() != 0){
		m_latitude_sp = new ROdouble( (component_name
		                             + std::string(":latitude")).c_str(),
		                             getComponent());

		m_longitude_sp = new ROdouble( (component_name
		                             + std::string(":longitude")).c_str(),
		                             getComponent());

		m_time_sp = new ROlongLong( (component_name
		                          + std::string(":time")).c_str(),
		                          getComponent(), new eTrexTimeDevIO());
	}
}


/* IDL implementation */

void eTrexImpl::lock() throw (CORBA::SystemException){
	m_locking = true;
}

void eTrexImpl::unlock() throw (CORBA::SystemException){
	m_locking = false;
}

/* Attributes returning */
char* eTrexImpl::device() throw (CORBA::SystemException){
	return m_device;
}

bool eTrexImpl::locking() throw (CORBA::SystemException){
	return m_locking;
}

/* Properties returning */
ACS::ROlongLong_ptr eTrexImpl::time() throw (CORBA::SystemException){
	if( m_time_sp == 0 ){
		return ACS::ROlongLong::_nil();
	}
	ACS::ROlongLong_var prop = ACS::ROlongLong::_narrow(m_time_sp->getCORBAReference());
	return prop._retn();
}

ACS::ROdouble_ptr eTrexImpl::latitude() throw (CORBA::SystemException){
	if( m_latitude_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_latitude_sp->getCORBAReference());
	return prop._retn();
}

ACS::ROdouble_ptr eTrexImpl::longitude() throw (CORBA::SystemException){
	if( m_longitude_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_longitude_sp->getCORBAReference());
	return prop._retn();
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(eTrexImpl)
/* ----------------------------------------------------------------*/

/*___oOo___*/
