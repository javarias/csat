#include <vltPort.h>
static const char *rcsId=(char *)"@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include <sys/time.h>
#include "PcDummyGpsImpl.h"

using namespace baci;

/* Constructor */
PcDummyGpsImpl::PcDummyGpsImpl(const ACE_CString& name, maci::ContainerServices *containerServices) :
       CharacteristicComponentImpl(name,containerServices)
      ,m_latitude_sp(this)
      ,m_longitude_sp(this)
{
	component_name = name.c_str();
	ACS_TRACE("PcDummyGpsImpl::PcDummyGpsImpl");
	m_device = (char *)"/dev/ttyS0";
	m_locking = true;
}

/* Destructor */
PcDummyGpsImpl::~PcDummyGpsImpl(){
}

/* Component Lifecycle */
void PcDummyGpsImpl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)
{
	ACS_TRACE("PcDummyGpsImpl::initialize");
	if( getComponent() != 0){

		m_latitude_sp = new ROdouble( (component_name
		                             + std::string(":latitude")).c_str(),
		                             getComponent());

		m_longitude_sp = new ROdouble( (component_name
		                             + std::string(":longitude")).c_str(),
		                             getComponent());
	}
}


/* IDL implementation */

void PcDummyGpsImpl::lock() throw (CORBA::SystemException){
	m_locking = true;
}

void PcDummyGpsImpl::unlock() throw (CORBA::SystemException){
	m_locking = false;
}

/* Attributes returning */
char* PcDummyGpsImpl::device() throw (CORBA::SystemException){
	return m_device;
}

bool PcDummyGpsImpl::locking() throw (CORBA::SystemException){
	return m_locking;
}

TYPES::TimeVal PcDummyGpsImpl::time() throw (CORBA::SystemException){
	struct timeval tv;
	struct timezone tz;

	gettimeofday(&tv, &tz);

	TYPES::TimeVal time = TYPES::TimeVal();
	time.sec = tv.tv_sec;
	time.usec = tv.tv_usec;
	return time;
}

/* Properties returning */
ACS::ROdouble_ptr PcDummyGpsImpl::latitude() throw (CORBA::SystemException){
	if( m_latitude_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_latitude_sp->getCORBAReference());
	return prop._retn();
}

ACS::ROdouble_ptr PcDummyGpsImpl::longitude() throw (CORBA::SystemException){
	if( m_longitude_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_longitude_sp->getCORBAReference());
	return prop._retn();
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(PcDummyGpsImpl)
/* ----------------------------------------------------------------*/

/*___oOo___*/
