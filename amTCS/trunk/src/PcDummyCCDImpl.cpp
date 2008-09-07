#include <vltPort.h>
static char *rcsId=(char *)"@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include <stdlib.h>
#include "PcDummyCCDImpl.h"
#include "csatErrors.h"


using namespace baci;

/* Constructor */
PcDummyCCDImpl::PcDummyCCDImpl(const ACE_CString& name, maci::ContainerServices *containerServices) :
	CharacteristicComponentImpl(name,containerServices)
	,m_frame_sp(this)
	,m_device_sp(this)
	,m_red_sp(this)
	,m_blue_sp(this)
	,m_green_sp(this)
	,m_pixelBias_sp(this)
	,m_resetLevel_sp(this)
	,m_exposure_sp(this)
{
	component_name = name.c_str();
}

/* Destructor */
PcDummyCCDImpl::~PcDummyCCDImpl(){
}

/* Component Lifecycle */
void PcDummyCCDImpl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)
{
	static char * _METHOD_ = (char *)"PcDummyCCDImpl::initialize";

	ACSErr::Completion_var completion;
	m_device_sp = new RWstring((string(component_name)+":device").c_str(), getComponent());

	ACS_TRACE( _METHOD_ );
	if( getComponent() != 0){

		m_frame_sp = new ROlongSeq( (component_name + std::string(":frame")).c_str(), getComponent());
		m_red_sp = new RWlong( (component_name + std::string(":red")).c_str(), getComponent());
		m_blue_sp = new RWlong( (component_name + std::string(":blue")).c_str(), getComponent());
		m_green_sp = new RWlong( (component_name + std::string(":green")).c_str(), getComponent());
		m_pixelBias_sp = new RWlong( (component_name + std::string(":pixelBias")).c_str(), getComponent());
		m_resetLevel_sp = new RWlong( (component_name + std::string(":resetLevel")).c_str(), getComponent());
		m_exposure_sp = new RWlong( (component_name + std::string(":exposure")).c_str(), getComponent());
	}
}

/* IDL implementation */

TYPES::Image* PcDummyCCDImpl::image(CORBA::Double exposure) throw (CORBA::SystemException){

	ACSErr::Completion_var comp;
	unsigned int length = 640*480*3;

	TYPES::Image_var image = TYPES::Image(length);
	image->length((CORBA::ULong)length);
	for(unsigned int i=0;i!=length/3.;i++) {
		image[3*i+0] = 255*m_red_sp->get_sync(comp.out());
		image[3*i+1] = 255*m_green_sp->get_sync(comp.out());
		image[3*i+2] = 255*m_blue_sp->get_sync(comp.out());
	}

	ACS_SHORT_LOG((LM_INFO,"PcDummyCCDImpl::image: Obtained an image"));
	return image._retn();
}

void PcDummyCCDImpl::lock() throw (CORBA::SystemException){
	m_locking = true;
}

void PcDummyCCDImpl::unlock() throw (CORBA::SystemException){
	m_locking = false;
}

void PcDummyCCDImpl::on() throw (CORBA::SystemException){
	m_powered = true;
}

void PcDummyCCDImpl::off() throw (CORBA::SystemException){
	m_powered = false;
}

/* Attributes returning */
bool PcDummyCCDImpl::locking() throw (CORBA::SystemException){
	return m_locking;
}

bool PcDummyCCDImpl::powered() throw (CORBA::SystemException){
	return m_powered;
}

/* Properties returning */
ACS::RWstring_ptr PcDummyCCDImpl::device() throw (CORBA::SystemException){
	if( m_device_sp == 0 ){
		return ACS::RWstring::_nil();
	}
	ACS::RWstring_var prop = ACS::RWstring::_narrow(m_device_sp->getCORBAReference());
	return prop._retn();
}

ACS::ROlongSeq_ptr PcDummyCCDImpl::frame() throw (CORBA::SystemException){
	if( m_frame_sp == 0 ){
		return ACS::ROlongSeq::_nil();
	}
	ACS::ROlongSeq_var prop = ACS::ROlongSeq::_narrow(m_frame_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWlong_ptr PcDummyCCDImpl::red() throw (CORBA::SystemException)
{
	if( m_red_sp == 0 ){
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_red_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWlong_ptr PcDummyCCDImpl::blue() throw (CORBA::SystemException)
{
	if( m_blue_sp == 0 ){
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_blue_sp->getCORBAReference());
	return prop._retn();
}

ACS::RWlong_ptr PcDummyCCDImpl::green() throw (CORBA::SystemException)
{
	if( m_green_sp == 0 ){
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_green_sp->getCORBAReference());
	return prop._retn();}

ACS::RWlong_ptr PcDummyCCDImpl::pixelBias() throw (CORBA::SystemException)
{
	if( m_pixelBias_sp == 0 ){
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_pixelBias_sp->getCORBAReference());
	return prop._retn();}

ACS::RWlong_ptr PcDummyCCDImpl::resetLevel() throw (CORBA::SystemException)
{
	if( m_resetLevel_sp == 0 ){
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_resetLevel_sp->getCORBAReference());
	return prop._retn();}

ACS::RWlong_ptr PcDummyCCDImpl::exposure() throw (CORBA::SystemException)
{
	if( m_exposure_sp == 0 ){
		return ACS::RWlong::_nil();
	}
	ACS::RWlong_var prop = ACS::RWlong::_narrow(m_exposure_sp->getCORBAReference());
	return prop._retn();}


/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(PcDummyCCDImpl)
/* ----------------------------------------------------------------*/

/*___oOo___*/
