#include <Pointing.h>

#include "PointingImpl.h"

PointingImpl::PointingImpl(const ACE_CString &name, maci::ContainerServices *containerServices) :
	ACSComponentImpl(name,containerServices)
{

}

::CORBA::Double PointingImpl::altOffset () throw (::CORBA::SystemException){

}

::CORBA::Double PointingImpl::azmOffset () throw (::CORBA::SystemException){

}

void PointingImpl::offSetAlt (::CORBA::Double degrees) throw (::CORBA::SystemException){

}

void PointingImpl::offSetAzm (::CORBA::Double degrees) throw (::CORBA::SystemException){

}

::TYPES::RadecPos PointingImpl::offSet (const ::TYPES::RadecPos & p) throw (::CORBA::SystemException){

}

void PointingImpl::resetAdjusts () throw(::CORBA::SystemException){

}

void PointingImpl::addObs (const ::TYPES::RadecPos & p_t, const ::TYPES::RadecPos & p_e, ::CORBA::Double sidereal_time) throw(::CORBA::SystemException){

}

void PointingImpl::calculateCoeffs () throw(::CORBA::SystemException){

}

void PointingImpl::setState (::CORBA::Boolean state, ::TYPES::PointingModel model) throw (::CORBA::SystemException){

}

::CORBA::Boolean PointingImpl::getState (::TYPES::PointingModel model) throw (::CORBA::SystemException){

}
