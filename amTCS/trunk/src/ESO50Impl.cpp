#include <vltPort.h>
static char *rcsId=(char *)"@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include <SerialRS232.h>

#include "ESO50Impl.h"
#include "ESO50CoordDevIO.h"
#include "ESO50VelDevIO.h"
#include "csatErrors.h"

using namespace baci;

/* Constructor */
ESO50Impl::ESO50Impl(const ACE_CString& name, maci::ContainerServices *containerServices) :
       CharacteristicComponentImpl(name,containerServices)
      ,m_realAzm_sp(this)
      ,m_realAlt_sp(this)
      ,m_altVel_sp(this)
      ,m_azmVel_sp(this)
      ,m_mount_sp(this)
{
	//component_name = name.c_str();
	ACS_TRACE("ESO50Impl::ESO50Impl");
	m_locking = true;
	m_name = name;
	printf("\n\n\n\nESO50Impl\n\n\n\n");
}

/* Destructor */
ESO50Impl::~ESO50Impl(){
}

/* Component Lifecycle */
void ESO50Impl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)
{
	const char * _METHOD_ = "ESO50Impl::initialize";
	ACS_TRACE("ESO50Impl::initialize");
	if( getComponent() != 0){

		ESO50CoordDevIO *azmDevIO = NULL;
		ESO50CoordDevIO *altDevIO = NULL;
		ESO50VelDevIO   *altVelDevIO = NULL;
		ESO50VelDevIO   *azmVelDevIO = NULL;

		try{
			azmDevIO = new ESO50CoordDevIO((char *)"/dev/ttyACM0", AZIMUTH_AXIS );
			altDevIO = new ESO50CoordDevIO((char *)"/dev/ttyACM0", ALTITUDE_AXIS );
			azmVelDevIO = new ESO50VelDevIO((char *)"/dev/ttyACM0", AZIMUTH_AXIS );
			altVelDevIO = new ESO50VelDevIO((char *)"/dev/ttyACM0", ALTITUDE_AXIS );
			printf("\n\nSe completaron las instancias %d %d\n\n",AZIMUTH_AXIS,ALTITUDE_AXIS);
		} catch (csatErrors::CannotOpenDeviceEx &ex){
			acsErrTypeLifeCycle::LifeCycleExImpl lifeEx(ex,__FILE__,__LINE__,_METHOD_);
			lifeEx.addData("Reason","Cannot create DevIOs");
			throw lifeEx;
		}

		m_realAzm_sp = new ROdouble( ( m_name + ":realAzm").c_str(), getComponent(), azmDevIO);
                m_realAlt_sp = new ROdouble( ( m_name + ":realAlt").c_str(), getComponent(), altDevIO);

                m_altVel_sp  = new RWdouble( ( m_name + ":altVel").c_str(), getComponent(), altVelDevIO);
                m_azmVel_sp  = new RWdouble( ( m_name + ":azmVel").c_str(), getComponent(), azmVelDevIO);
                m_mount_sp   = new ROEnumImpl<ACS_ENUM_T(DEVTELESCOPE_MODULE::mountType),
                                              POA_DEVTELESCOPE_MODULE::ROmountType>
                                            (( m_name + ":mount").c_str(),
                                             getComponent() );
		printf("\n\ndone \n\n");
	}
}


/* IDL implementation */
void ESO50Impl::setCurrentAltAz(const TYPES::AltazPos &p) throw (CORBA::SystemException){
	ACS_TRACE("ESO50Impl::setCurrentAlzAz");
}

void ESO50Impl::setVel(const TYPES::AltazVel &vel) throw (CORBA::SystemException){

	azmVel()->set_sync(vel.azVel );
	altVel()->set_sync(vel.altVel);
	return;
}

void ESO50Impl::lock() throw (CORBA::SystemException){
	m_locking = true;
}

void ESO50Impl::unlock() throw (CORBA::SystemException){
	m_locking = false;
}


/* Attributes returning */
TYPES::AltazVel ESO50Impl::getVel() throw (CORBA::SystemException){
	TYPES::AltazVel velocity;

	ACSErr::Completion_var completion;
	velocity.azVel  = azmVel()->get_sync(completion.out());
	velocity.altVel = altVel()->get_sync(completion.out());
	return velocity;
}

bool ESO50Impl::locking() throw (CORBA::SystemException){
	return m_locking;
}

/* Properties returning */

ACS::ROdouble_ptr ESO50Impl::realAzm() throw (CORBA::SystemException)
{
	printf("\n\nyeah!!!\n\n");
	if( m_realAzm_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAzm_sp->getCORBAReference());
	return prop._retn();
}

ACS::ROdouble_ptr ESO50Impl::realAlt() throw (CORBA::SystemException)
{
	if( m_realAlt_sp == 0 ){
		return ACS::ROdouble::_nil();
	}
	ACS::ROdouble_var prop = ACS::ROdouble::_narrow(m_realAlt_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWdouble_ptr ESO50Impl::azmVel() throw (CORBA::SystemException)
{
	if( m_azmVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_azmVel_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWdouble_ptr ESO50Impl::altVel() throw (CORBA::SystemException)
{
	if( m_altVel_sp == 0 ){
		return ACS::RWdouble::_nil();
	}
	ACS::RWdouble_var prop = ACS::RWdouble::_narrow(m_altVel_sp->getCORBAReference());
	return prop._retn();
}

DEVTELESCOPE_MODULE::ROmountType_ptr ESO50Impl::mount() throw (CORBA::SystemException)
{
	if( m_mount_sp == 0 ){
		return DEVTELESCOPE_MODULE::ROmountType::_nil();
	}
	DEVTELESCOPE_MODULE::ROmountType_var prop = DEVTELESCOPE_MODULE::ROmountType::_narrow(m_mount_sp->getCORBAReference());
	return prop._retn();
}

/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(ESO50Impl)
/* ----------------------------------------------------------------*/

/*___oOo___*/
