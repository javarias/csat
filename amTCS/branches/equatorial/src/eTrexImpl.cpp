#include <vltPort.h>
static const char *rcsId="@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include <sys/time.h>
#include "eTrexImpl.h"
#include "eTrexCoordDevIO.h"

using namespace baci;

/* Constructor */
eTrexImpl::eTrexImpl(const ACE_CString& name, maci::ContainerServices *containerServices) :
       CharacteristicComponentImpl(name,containerServices)
      ,m_latitude_sp(this)
      ,m_longitude_sp(this)
{
	component_name = name.c_str();
	ACS_TRACE("eTrexImpl::eTrexImpl");
	m_device = (char *)"/dev/ttyS0";
	m_locking = true;
}

/* Destructor */
eTrexImpl::~eTrexImpl(){
}

/* Component Lifecycle */
void eTrexImpl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)
{
	const char * _METHOD_ = "eTrexImpl::initialize";
	ACS_TRACE("eTrexImpl::initialize");
	if( getComponent() != 0){

		eTrexCoordDevIO *latDevIO = NULL;
		eTrexCoordDevIO *lonDevIO = NULL;

		try {
			latDevIO = new eTrexCoordDevIO(m_device,LATITUDE_COORD);
			lonDevIO = new eTrexCoordDevIO(m_device,LONGITUDE_COORD);
		} catch ( csatErrors::CannotOpenDeviceEx &ex) {
			acsErrTypeLifeCycle::LifeCycleExImpl lifeEx(ex,__FILE__,__LINE__,_METHOD_);
			lifeEx.addData("Reason","Cannot create DevIOs");
			throw lifeEx;
		}			

		m_latitude_sp = new ROdouble( (component_name
		                             + std::string(":latitude")).c_str(),
		                             getComponent(), latDevIO, true);

		m_longitude_sp = new ROdouble( (component_name
		                             + std::string(":longitude")).c_str(),
		                             getComponent(), lonDevIO, true);
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

TYPES::TimeVal eTrexImpl::time() throw (CORBA::SystemException){
	struct timeval tv;
	struct timezone tz;

	gettimeofday(&tv, &tz);

	TYPES::TimeVal time = TYPES::TimeVal();
	time.sec = tv.tv_sec;
	time.usec = tv.tv_usec;
	return time;
}

/* Properties returning */
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
