#include <vltPort.h>
static char *rcsId="@(#) $Id: $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

#include <stdlib.h>
#include "lpiFrameDevIO.h"
#include "lpiImpl.h"

using namespace baci;

/* Constructor */
lpiImpl::lpiImpl(const ACE_CString& name, maci::ContainerServices *containerServices) :
       CharacteristicComponentImpl(name,containerServices)
      ,m_frame_sp(this)
{
	component_name = name.c_str();
	m_device = "/dev/video0";
	m_locking = true;
}

/* Destructor */
lpiImpl::~lpiImpl(){
}

/* Component Lifecycle */
void lpiImpl::initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl)
{
	ACS_TRACE("lpiImpl::initialize");
	if( getComponent() != 0){
		m_frame_sp = new ROlongSeq( (component_name + std::string(":frame")).c_str(),
		                             getComponent(), new lpiFrameDevIO("/dev/video0"),true);
	}
}

/* IDL implementation */

TYPES::Image* lpiImpl::image(CORBA::Double exposure) throw (CORBA::SystemException){
	
	ACSErr::Completion_var comp;
	unsigned int length = 640*480*3;

	ACS::longSeq *frame = m_frame_sp->get_sync(comp.out());

//	TYPES::Image image = new TYPES::Image(length);
//	image->length(length);
	TYPES::Image_var image = TYPES::Image(length);
	image->length((CORBA::ULong)length);
	for(unsigned int i=0;i!=length;i++)
		image[i] = frame[0][i];
	
	ACS_SHORT_LOG((LM_INFO,"lpiImpl::image: Obtained the Image"));
	return image._retn();
}

void lpiImpl::lock() throw (CORBA::SystemException){
	m_locking = true;
}

void lpiImpl::unlock() throw (CORBA::SystemException){
	m_locking = false;
}

void lpiImpl::on() throw (CORBA::SystemException){
	m_powered = true;
}

void lpiImpl::off() throw (CORBA::SystemException){
	m_powered = false;
}

/* Attributes returning */
char* lpiImpl::device() throw (CORBA::SystemException){
	return m_device;
}

bool lpiImpl::locking() throw (CORBA::SystemException){
	return m_locking;
}

bool lpiImpl::powered() throw (CORBA::SystemException){
	return m_powered;
}

/* Properties returning */
ACS::ROlongSeq_ptr lpiImpl::frame() throw (CORBA::SystemException){
	if( m_frame_sp == 0 ){
		return ACS::ROlongSeq::_nil();
	}
	ACS::ROlongSeq_var prop = ACS::ROlongSeq::_narrow(m_frame_sp->getCORBAReference());
	return prop._retn();
}


/* --------------- [ MACI DLL support functions ] -----------------*/
#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(lpiImpl)
/* ----------------------------------------------------------------*/

/*___oOo___*/
