#include <vltPort.h>
static char *rcsId="@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include <SerialRS232.h>

#include "Lx200Impl.h"
#include "Lx200CoordDevIO.h"
#include "Lx200VelDevIO.h"
#include "csatErrors.h"

using namespace baci;

/* Constructor */
Lx200Impl::Lx200Impl(const ACE_CString& name, maci::ContainerServices *containerServices) :
       CharacteristicComponentImpl(name,containerServices)
      ,m_realAzm_sp(this)
      ,m_realAlt_sp(this)
      ,m_altVel_sp(this)
      ,m_azmVel_sp(this)
{
	const char * _METHOD_ = "Lx200Impl::Lx200Impl";
	component_name = name.c_str();
	ACS_TRACE(_METHOD_);
	m_locking = true;
}

/* Destructor */
Lx200Impl::~Lx200Impl()
{
	const char * _METHOD_ = "Lx200Impl::~Lx200Impl";
	ACS_TRACE(_METHOD_);
}

/* Component Lifecycle */
void Lx200Impl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)//,csatErrors::CannotOpenDeviceEx)
{
	const char * _METHOD_ = "Lx200Impl::initialize";
	ACS_TRACE(_METHOD_);

        if( getComponent() != 0){

                Lx200CoordDevIO *azmDevIO = NULL;
                Lx200CoordDevIO *altDevIO = NULL;
                Lx200VelDevIO   *altVelDevIO = NULL;
                Lx200VelDevIO   *azmVelDevIO = NULL;

                try{
                        azmDevIO = new Lx200CoordDevIO("/dev/ttyS0", AZIMUTH_AXIS);
                        altDevIO = new Lx200CoordDevIO("/dev/ttyS0", ALTITUDE_AXIS);
                        azmVelDevIO = new Lx200VelDevIO("/dev/ttyS0", AZIMUTH_AXIS);
                        altVelDevIO = new Lx200VelDevIO("/dev/ttyS0", ALTITUDE_AXIS);
                } catch (csatErrors::CannotOpenDeviceEx &ex){
                        acsErrTypeLifeCycle::LifeCycleExImpl lifeEx(ex,__FILE__,__LINE__,_METHOD_);
                        lifeEx.addData("Reason","Cannot create DevIOs");
                        throw lifeEx;
                }

                m_realAzm_sp = new ROdouble( (component_name + std::string(":realAzm")).c_str(),
                                                     getComponent(), azmDevIO);
                m_realAlt_sp = new ROdouble( (component_name + std::string(":realAlt")).c_str(),
                                          getComponent(), altDevIO);

                m_altVel_sp  = new RWdouble( ( component_name + std::string(":altVel")).c_str(),
                                          getComponent(), altVelDevIO);
                m_azmVel_sp  = new RWdouble( ( component_name + std::string(":azmVel")).c_str(),
                                          getComponent(), azmVelDevIO);
        }
}


/* IDL implementation */
void Lx200Impl::setCurrentAltAz(const TYPES::AltazPos &p) throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200Impl::setCurrentAltAz";
	ACS_TRACE(_METHOD_);
}

void Lx200Impl::setVel(const TYPES::AltazVel &vel) throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200Impl::setVel";
	ACS_TRACE(_METHOD_);

	azmVel()->set_sync(vel.azVel);
	altVel()->set_sync(vel.altVel);
}

void Lx200Impl::lock() throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200Impl::lock";
	ACS_TRACE(_METHOD_);

	m_locking = true;
}

void Lx200Impl::unlock() throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200Impl::unlock";
	ACS_TRACE(_METHOD_);

	m_locking = false;
}


/* Attributes returning */
TYPES::AltazVel Lx200Impl::getVel() throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200Impl::getVel";
	ACS_TRACE(_METHOD_);

	TYPES::AltazVel velocity;

	ACSErr::Completion_var completion;
	velocity.azVel  = azmVel()->get_sync(completion.out());
	velocity.altVel = altVel()->get_sync(completion.out());
	return velocity;
}

bool Lx200Impl::locking() throw (CORBA::SystemException)
{
	const char * _METHOD_ = "Lx200Impl::locking";
	ACS_TRACE(_METHOD_);

	return m_locking;
}

/* Properties returning */

ACS::ROdouble_ptr Lx200Impl::realAzm() throw (CORBA::SystemException)
{
	if( m_realAzm_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAzm_sp->getCORBAReference());
	return prop._retn();
}

ACS::ROdouble_ptr Lx200Impl::realAlt() throw (CORBA::SystemException)
{
	if( m_realAlt_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAlt_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWdouble_ptr Lx200Impl::azmVel() throw (CORBA::SystemException)
{
	if( m_azmVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_azmVel_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWdouble_ptr Lx200Impl::altVel() throw (CORBA::SystemException)
{
	if( m_altVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_altVel_sp->getCORBAReference());
	return prop._retn();
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(Lx200Impl)
/* ----------------------------------------------------------------*/

/*___oOo___*/
