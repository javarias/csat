#ifndef _POINTINGIMPL_H
#define _POINTINGIMPL_H
 
#include <acscomponentImpl.h>

#include <ObsList.h>

#include "PointingS.h"
#include "TypesC.h"

class PointingImpl: virtual public POA_POINTING_MODULE::Pointing,
                    virtual public acscomponent::ACSComponentImpl
{

public:
	PointingImpl(const ACE_CString &name, maci::ContainerServices *containerServices);

	void initialize() throw (ACSErr::ACSbaseExImpl);

	::CORBA::Double altOffset () throw (::CORBA::SystemException);

	::CORBA::Double azmOffset () throw (::CORBA::SystemException);

	void offSetAlt (::CORBA::Double degrees) throw (::CORBA::SystemException);

	void offSetAzm (::CORBA::Double degrees) throw (::CORBA::SystemException);

	::TYPES::RadecPos offSet (const ::TYPES::RadecPos & p, const ::CORBA::Double st) throw (::CORBA::SystemException);

	void resetAdjusts () throw(::CORBA::SystemException);

	void addObs (const ::TYPES::RadecPos & p_t, const ::TYPES::RadecPos & p_e, ::CORBA::Double sidereal_time) throw(::CORBA::SystemException);

	void calculateCoeffs () throw(::CORBA::SystemException);

	void setState (::CORBA::Boolean state, ::TYPES::PointingModel model) throw (::CORBA::SystemException);

	::CORBA::Boolean getState (::TYPES::PointingModel model) throw (::CORBA::SystemException);

private:

	ObsList *m_obslist;
	::TYPES::AltazPos m_manualOffset;
	bool m_state[2];
};

#endif /* _POINTINGIMPL_H */
