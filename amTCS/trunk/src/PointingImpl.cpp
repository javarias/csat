#include "PointingImpl.h"

PointingImpl::PointingImpl(const ACE_CString &name, maci::ContainerServices *containerServices) :
	ACSComponentImpl(name,containerServices)
{
	resetAdjusts();
}

void PointingImpl::initialize() throw (ACSErr::ACSbaseExImpl) {

	// new ObsList(mount,latitude)
	m_obslist = new ObsList(1,100);
}

::CORBA::Double PointingImpl::altOffset () throw (::CORBA::SystemException){
	return m_manualOffset.alt;
}

::CORBA::Double PointingImpl::azmOffset () throw (::CORBA::SystemException){
	return m_manualOffset.az;
}

void PointingImpl::offSetAlt (::CORBA::Double degrees) throw (::CORBA::SystemException){
	m_manualOffset.alt += degrees;
}

void PointingImpl::offSetAzm (::CORBA::Double degrees) throw (::CORBA::SystemException){
	m_manualOffset.az += degrees;
}

::TYPES::RadecPos PointingImpl::offSet (const ::TYPES::RadecPos & p, ::CORBA::Double st) throw (::CORBA::SystemException){
	double ra_d, dec_d;
	m_obslist->cOff(p.ra, p.dec, st, &ra_d, &dec_d);
	::TYPES::RadecPos return_p;
	return_p.ra = ra_d;
	return_p.dec = dec_d;
	return return_p;
}

void PointingImpl::resetAdjusts () throw(::CORBA::SystemException){
	m_manualOffset.alt = 0;
	m_manualOffset.az = 0;
}

void PointingImpl::addObs (const ::TYPES::RadecPos & p_t, const ::TYPES::RadecPos & p_e, ::CORBA::Double sidereal_time) throw(::CORBA::SystemException){
	m_obslist->add(p_t.ra, p_t.dec, p_e.ra, p_e.dec, sidereal_time);
}

void PointingImpl::calculateCoeffs () throw(::CORBA::SystemException){
	m_obslist->cCoeffs();
}

void PointingImpl::setState (::CORBA::Boolean state, ::TYPES::PointingModel model) throw (::CORBA::SystemException){

}

::CORBA::Boolean PointingImpl::getState (::TYPES::PointingModel model) throw (::CORBA::SystemException){

}
