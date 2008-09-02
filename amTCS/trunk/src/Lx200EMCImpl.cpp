#include <vltPort.h>
static char *rcsId="@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include <SerialRS232.h>
#include <math.h>

#include "Lx200EMCImpl.h"
#include "Lx200EMCCoordDevIO.h"
#include "Lx200EMCVelDevIO.h"
#include "csatErrors.h"

using namespace baci;

/* Constructor */
Lx200EMCImpl::Lx200EMCImpl(const ACE_CString& name, maci::ContainerServices *containerServices) :
       CharacteristicComponentImpl(name,containerServices)
      ,m_realAzm_sp(this)
      ,m_realAlt_sp(this)
      ,m_altVel_sp(this)
      ,m_azmVel_sp(this)
      ,m_mount_sp(this)
{
	const char * _METHOD_ = "Lx200EMCImpl::Lx200EMCImpl";
	ACS_TRACE(_METHOD_);
	m_locking = true;
	m_name = name;
}

/* Destructor */
Lx200EMCImpl::~Lx200EMCImpl()
{
	const char * _METHOD_ = "Lx200EMCImpl::~Lx200EMCImpl";
	ACS_TRACE(_METHOD_);
}

/* Component Lifecycle */
void Lx200EMCImpl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)//,csatErrors::CannotOpenDeviceEx)
{
	const char * _METHOD_ = "Lx200EMCImpl::initialize";
	ACS_TRACE(_METHOD_);

        if( getComponent() != 0){

                Lx200EMCCoordDevIO *azmDevIO = NULL;
                Lx200EMCCoordDevIO *altDevIO = NULL;
                Lx200EMCVelDevIO   *altVelDevIO = NULL;
                Lx200EMCVelDevIO   *azmVelDevIO = NULL;

                try{
                        azmDevIO = new Lx200EMCCoordDevIO("/dev/ttyS0", AZIMUTH_AXIS,false);
                        altDevIO = new Lx200EMCCoordDevIO("/dev/ttyS0", ALTITUDE_AXIS,true);
                        azmVelDevIO = new Lx200EMCVelDevIO("/dev/ttyS0", AZIMUTH_AXIS,false);
                        altVelDevIO = new Lx200EMCVelDevIO("/dev/ttyS0", ALTITUDE_AXIS,true);
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
void Lx200EMCImpl::setCurrentAltAz(const TYPES::AltazPos &p) throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200EMCImpl::setCurrentAltAz";
	ACS_TRACE(_METHOD_);
}

void Lx200EMCImpl::setVel(const TYPES::AltazVel &vel) throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200EMCImpl::setVel";
	ACS_TRACE(_METHOD_);
	int altRate, azRate;
	float absAltVel, absAzmVel;
	absAltVel = fabs(vel.altVel);
	absAzmVel = fabs(vel.azVel);

	if (absAltVel >= 5)
		altRate = 4;
	else if (absAltVel >= 2)
		altRate = 3;
	else if (absAltVel >= 0.035)
		altRate = 2;
	else
		altRate = 1;
	
	if (absAzmVel >= 5)
		azRate = 4;
	else if (absAzmVel >= 2)
		azRate = 3;
	else if (absAzmVel >= 0.035)
		azRate = 2;
	else
		azRate = 1;

	if(azRate > altRate) {
		azmVel()->set_sync(vel.azVel);
		altVel()->set_sync(0.0);
	}
	else if(altRate > azRate) {
		altVel()->set_sync(vel.altVel);
		azmVel()->set_sync(0.0);
	}
	else {
		altVel()->set_sync(vel.altVel);
		azmVel()->set_sync(vel.azVel);
	}
}

void Lx200EMCImpl::lock() throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200EMCImpl::lock";
	ACS_TRACE(_METHOD_);

	m_locking = true;
}

void Lx200EMCImpl::unlock() throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200EMCImpl::unlock";
	ACS_TRACE(_METHOD_);

	m_locking = false;
}


/* Attributes returning */
TYPES::AltazVel Lx200EMCImpl::getVel() throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200EMCImpl::getVel";
	ACS_TRACE(_METHOD_);

	TYPES::AltazVel velocity;

	ACSErr::Completion_var completion;
	velocity.azVel  = azmVel()->get_sync(completion.out());
	velocity.altVel = altVel()->get_sync(completion.out());
	return velocity;
}

bool Lx200EMCImpl::locking() throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200EMCImpl::locking";
	ACS_TRACE(_METHOD_);

	return m_locking;
}

/* Properties returning */

ACS::ROdouble_ptr Lx200EMCImpl::realAzm() throw (CORBA::SystemException)
{
	if( m_realAzm_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAzm_sp->getCORBAReference());
	return prop._retn();
}

ACS::ROdouble_ptr Lx200EMCImpl::realAlt() throw (CORBA::SystemException)
{
	if( m_realAlt_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAlt_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWdouble_ptr Lx200EMCImpl::azmVel() throw (CORBA::SystemException)
{
	if( m_azmVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_azmVel_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWdouble_ptr Lx200EMCImpl::altVel() throw (CORBA::SystemException)
{
	if( m_altVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_altVel_sp->getCORBAReference());
	return prop._retn();
}

DEVTELESCOPE_MODULE::ROmountType_ptr Lx200EMCImpl::mount() throw (CORBA::SystemException){
	if( m_mount_sp == 0 ){
		return DEVTELESCOPE_MODULE::ROmountType::_nil();
	}
	DEVTELESCOPE_MODULE::ROmountType_var prop = DEVTELESCOPE_MODULE::ROmountType::_narrow(m_mount_sp->getCORBAReference());
	return prop._retn();
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(Lx200EMCImpl)
/* ----------------------------------------------------------------*/

/*___oOo___*/
