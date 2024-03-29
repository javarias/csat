#include <vltPort.h>
static char *rcsId=(char *)"@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include <SerialRS232.h>

#include "Lx200GPSImpl.h"
#include "Lx200GPSCoordDevIO.h"
#include "Lx200GPSVelDevIO.h"
#include "csatErrors.h"

using namespace baci;

/* Constructor */
Lx200GPSImpl::Lx200GPSImpl(const ACE_CString& name, maci::ContainerServices *containerServices) :
       CharacteristicComponentImpl(name,containerServices)
      ,m_realAzm_sp(this)
      ,m_realAlt_sp(this)
      ,m_altVel_sp(this)
      ,m_azmVel_sp(this)
      ,m_mount_sp(this)
{
	const char * _METHOD_ = (char *)"Lx200GPSImpl::Lx200GPSImpl";
	ACS_TRACE(_METHOD_);
	m_locking = true;
	m_name = name;
}

/* Destructor */
Lx200GPSImpl::~Lx200GPSImpl()
{
	const char * _METHOD_ = (char *)"Lx200GPSImpl::~Lx200GPSImpl";
	ACS_TRACE(_METHOD_);
}

/* Component Lifecycle */
void Lx200GPSImpl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)//,csatErrors::CannotOpenDeviceEx)
{
	const char * _METHOD_ = (char *)"Lx200GPSImpl::initialize";
	ACS_TRACE(_METHOD_);

        if( getComponent() != 0){

                Lx200GPSCoordDevIO *azmDevIO = NULL;
                Lx200GPSCoordDevIO *altDevIO = NULL;
                Lx200GPSVelDevIO   *altVelDevIO = NULL;
                Lx200GPSVelDevIO   *azmVelDevIO = NULL;

                try{
                        azmDevIO = new Lx200GPSCoordDevIO((char *)"/dev/ttyS0", AZIMUTH_AXIS);
                        altDevIO = new Lx200GPSCoordDevIO((char *)"/dev/ttyS0", ALTITUDE_AXIS);
                        azmVelDevIO = new Lx200GPSVelDevIO((char *)"/dev/ttyS0", AZIMUTH_AXIS);
                        altVelDevIO = new Lx200GPSVelDevIO((char *)"/dev/ttyS0", ALTITUDE_AXIS);
                } catch (csatErrors::CannotOpenDeviceEx &ex){
                        acsErrTypeLifeCycle::LifeCycleExImpl lifeEx(ex,__FILE__,__LINE__,_METHOD_);
                        lifeEx.addData("Reason","Cannot create DevIOs");
                        throw lifeEx;
                }

                m_realAzm_sp = new ROdouble( ( m_name + ":realAzm").c_str(),
                                                     getComponent(), azmDevIO);
                m_realAlt_sp = new ROdouble( ( m_name + ":realAlt").c_str(),
                                          getComponent(), altDevIO);

                m_altVel_sp  = new RWdouble( ( m_name + ":altVel").c_str(),
                                          getComponent(), altVelDevIO);
                m_azmVel_sp  = new RWdouble( ( m_name + ":azmVel").c_str(),
                                          getComponent(), azmVelDevIO);
                m_mount_sp   = new ROEnumImpl<ACS_ENUM_T(DEVTELESCOPE_MODULE::mountType),
                                              POA_DEVTELESCOPE_MODULE::ROmountType>
                                            (( m_name + ":mount").c_str(),
                                             getComponent() );
        }
}


/* IDL implementation */
void Lx200GPSImpl::setCurrentAltAz(const TYPES::AltazPos &p) throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200GPSImpl::setCurrentAltAz";
	ACS_TRACE(_METHOD_);
}

void Lx200GPSImpl::setVel(const TYPES::AltazVel &vel) throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200GPSImpl::setVel";
	ACS_TRACE(_METHOD_);

	azmVel()->set_sync(vel.azVel);
	altVel()->set_sync(vel.altVel);
}

void Lx200GPSImpl::lock() throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200GPSImpl::lock";
	ACS_TRACE(_METHOD_);

	m_locking = true;
}

void Lx200GPSImpl::unlock() throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200GPSImpl::unlock";
	ACS_TRACE(_METHOD_);

	m_locking = false;
}


/* Attributes returning */
TYPES::AltazVel Lx200GPSImpl::getVel() throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200GPSImpl::getVel";
	ACS_TRACE(_METHOD_);

	TYPES::AltazVel velocity;

	ACSErr::Completion_var completion;
	velocity.azVel  = azmVel()->get_sync(completion.out());
	velocity.altVel = altVel()->get_sync(completion.out());
	return velocity;
}

bool Lx200GPSImpl::locking() throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200GPSImpl::locking";
	ACS_TRACE(_METHOD_);

	return m_locking;
}

/* Properties returning */

ACS::ROdouble_ptr Lx200GPSImpl::realAzm() throw (CORBA::SystemException)
{
	if( m_realAzm_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAzm_sp->getCORBAReference());
	return prop._retn();
}

ACS::ROdouble_ptr Lx200GPSImpl::realAlt() throw (CORBA::SystemException)
{
	if( m_realAlt_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAlt_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWdouble_ptr Lx200GPSImpl::azmVel() throw (CORBA::SystemException)
{
	if( m_azmVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_azmVel_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWdouble_ptr Lx200GPSImpl::altVel() throw (CORBA::SystemException)
{
	if( m_altVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_altVel_sp->getCORBAReference());
	return prop._retn();
}

DEVTELESCOPE_MODULE::ROmountType_ptr Lx200GPSImpl::mount() throw (CORBA::SystemException){
	if( m_mount_sp == 0 ){
		return DEVTELESCOPE_MODULE::ROmountType::_nil();
	}
	DEVTELESCOPE_MODULE::ROmountType_var prop = DEVTELESCOPE_MODULE::ROmountType::_narrow(m_mount_sp->getCORBAReference());
	return prop._retn();
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(Lx200GPSImpl)
/* ----------------------------------------------------------------*/

/*___oOo___*/
