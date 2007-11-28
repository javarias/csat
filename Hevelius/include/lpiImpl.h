#ifndef _LIPIMPL_H_
#define _LIPIMPL_H_

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <baciCharacteristicComponentImpl.h>
#include <baciSmartPropertyPointer.h>

#include <baciROlongSeq.h>

#include <DevCCDS.h>
#include <TypesC.h>

using namespace baci;

/**
 * The class DevTelescope implements 
 */

class lpiImpl : public virtual CharacteristicComponentImpl,
                public virtual POA_DEVCCD_MODULE::DevCCD
{

public:

	lpiImpl(const ACE_CString& name, maci::ContainerServices* containerServices);

	virtual ~lpiImpl();

	virtual void initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl);

	virtual TYPES::Image* image(CORBA::Double exposure) throw (CORBA::SystemException);

	virtual void on() throw (CORBA::SystemException);

	virtual void off() throw (CORBA::SystemException);
 
	virtual void lock() throw (CORBA::SystemException);

	virtual void unlock() throw (CORBA::SystemException);

	virtual char* device() throw (CORBA::SystemException);

	virtual bool locking() throw (CORBA::SystemException);

	virtual bool powered() throw (CORBA::SystemException);

	virtual ACS::ROlongSeq_ptr frame() throw (CORBA::SystemException);

private:

	// Properties
	SmartPropertyPointer<ROlongSeq> m_frame_sp;

	// Private attributes (IDL)
	char* m_device;
	bool m_locking;
	bool m_powered;

	// Private attributes (others)
	std::string  component_name;

	/**
	 * ALMA C++ coding standards state copy operators should be disabled.
	 */
	void operator=(const lpiImpl&);
};

#endif /* _LIPIMPL_H_ */
