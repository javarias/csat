#ifndef _LX200_IMPL_H_
#define _LX200_IMPL_H_

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <baciCharacteristicComponentImpl.h>
#include <baciSmartPropertyPointer.h>

#include <baciROdouble.h>
#include <baciRWdouble.h>
#include <enumpropROImpl.h>

#include "DevTelescopeS.h"
#include "TypesC.h"

using namespace baci;

/**
 * The class DevTelescope implements 
 */

class Lx200Impl : public virtual CharacteristicComponentImpl,
                    public virtual POA_DEVTELESCOPE_MODULE::DevTelescope
{
	public:
		/**
		 * Constructor
		 * @param poa POA which will activate this and also all other COBs
		 * @param name Component name
		 */
		Lx200Impl(const ACE_CString& name, maci::ContainerServices* containerServices);

		/**
		 * Destructor
		 */
		virtual ~Lx200Impl();

		/**
		 * Override component lifecycles method
		 */
		virtual void initialize() 
			throw (acsErrTypeLifeCycle::LifeCycleExImpl);
		
		/* IDL operations */
		virtual void setCurrentAltAz(const TYPES::AltazPos &p) 
			throw (CORBA::SystemException);

		virtual void setVel(const TYPES::AltazVel &vel) 
			throw (CORBA::SystemException);

		virtual void lock() 
			throw (CORBA::SystemException);

		virtual void unlock() 
			throw (CORBA::SystemException);

		virtual TYPES::AltazVel getVel() 
			throw (CORBA::SystemException);

		virtual bool locking() 
			throw (CORBA::SystemException);

		virtual ACS::ROdouble_ptr realAlt() 
			throw (CORBA::SystemException);

		virtual ACS::ROdouble_ptr realAzm() 
			throw (CORBA::SystemException);

		virtual ACS::RWdouble_ptr azmVel() 
			throw (CORBA::SystemException);

		virtual ACS::RWdouble_ptr altVel() 
			throw (CORBA::SystemException);

		virtual DEVTELESCOPE_MODULE::ROmountType_ptr mount()
		   throw (CORBA::SystemException);

	private:

		// Properties
		SmartPropertyPointer<ROdouble> m_realAzm_sp;
		SmartPropertyPointer<ROdouble> m_realAlt_sp;
		SmartPropertyPointer<RWdouble> m_altVel_sp;
		SmartPropertyPointer<RWdouble> m_azmVel_sp;
		SmartPropertyPointer<ROEnumImpl<ACS_ENUM_T(DEVTELESCOPE_MODULE::mountType), POA_DEVTELESCOPE_MODULE::ROmountType> > m_mount_sp;

		// Private attributes (IDL)
		bool m_locking;

		// Private attributes (others)
		
		std::string  component_name;	

		/**
		 * ALMA C++ coding standards state copy operators should be disabled.
		 */
		void operator=(const Lx200Impl&);
};

#endif /* _LX200_IMPL_H_ */
