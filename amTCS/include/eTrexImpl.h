#ifndef _ETREXIMPL_H_
#define _ETREXIMPL_H_

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <baciCharacteristicComponentImpl.h>
#include <baciSmartPropertyPointer.h>

#include <baciROdouble.h>

#include <DevGPSS.h>
#include <TypesC.h>

using namespace baci;

/**
 * The class DevTelescope implements 
 */

class eTrexImpl : public virtual CharacteristicComponentImpl,
                public virtual POA_DEVGPS_MODULE::DevGPS
{

public:

	eTrexImpl(const ACE_CString& name, maci::ContainerServices* containerServices);

	virtual ~eTrexImpl();

	virtual void initialize() throw (acsErrTypeLifeCycle::LifeCycleExImpl);

	virtual void lock() throw (CORBA::SystemException);

	virtual void unlock() throw (CORBA::SystemException);

	virtual char* device() throw (CORBA::SystemException);

	virtual bool locking() throw (CORBA::SystemException);

	virtual ACS::ROdouble_ptr time() throw (CORBA::SystemException);

	virtual ACS::ROdouble_ptr latitude() throw (CORBA::SystemException);

	virtual ACS::ROdouble_ptr longitude() throw (CORBA::SystemException);

private:

	// Properties
	SmartPropertyPointer<ROdouble>  m_time_sp;
	SmartPropertyPointer<ROdouble>  m_latitude_sp;
	SmartPropertyPointer<ROdouble>  m_longitude_sp;

	// Private attributes (IDL)
	char* m_device;
	bool m_locking;

	// Private attributes (others)
	std::string  component_name;

	/**
	 * ALMA C++ coding standards state copy operators should be disabled.
	 */
	void operator=(const eTrexImpl&);
};

#endif /* _ETREXIMPL_H_ */
