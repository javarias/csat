#ifndef _NEXSTARIMPL_H_
#define _NEXSTARIMPL_H_

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <baciCharacteristicComponentImpl.h>
#include <baciSmartPropertyPointer.h>

#include <baciROdouble.h>

#include "DevTelescopeS.h"
#include "TypesC.h"
#include "csatErrors.h"

using namespace baci;

/**
 * The class DevTelescope implements 
 */

class NexstarImpl : public virtual CharacteristicComponentImpl,
                    public virtual POA_DEVTELESCOPE_MODULE::DevTelescope
//                         public ActionImplementator
{

public:
	/**
	 * Constructor
	 * @param poa POA which will activate this and also all other COBs
	 * @param name Component name
	 */
	NexstarImpl(const ACE_CString& name, maci::ContainerServices* containerServices);
	/**
	 * Destructor
	 */
	virtual ~NexstarImpl();

	/**
	 * Override component lifecycles method
	 */
	virtual void initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl);//,csatErrors::CannotOpenDeviceEx);
	
	/* IDL operations */
	virtual void setCurrentAltAz(const TYPES::AltazPos &p) throw (CORBA::SystemException);

	virtual void setVel(const TYPES::AltazVel &vel) throw (CORBA::SystemException);

	virtual void lock() throw (CORBA::SystemException);

	virtual void unlock() throw (CORBA::SystemException);

	virtual TYPES::AltazVel velocity() throw (CORBA::SystemException);

	virtual bool locking() throw (CORBA::SystemException);

	virtual ACS::ROdouble_ptr realAlt() throw (CORBA::SystemException);

	virtual ACS::ROdouble_ptr realAzm() throw (CORBA::SystemException);

private:

	// Properties
	SmartPropertyPointer<ROdouble> m_realAzm_sp;
	SmartPropertyPointer<ROdouble> m_realAlt_sp;

	// Private attributes (IDL)
	bool m_locking;
	TYPES::AltazVel m_velocity;

	// Private attributes (others)
	
	std::string  component_name;	

	/**
	 * ALMA C++ coding standards state copy operators should be disabled.
	 */
	void operator=(const NexstarImpl&);
};

#endif /* _NEXSTARIMPL_H_ */
