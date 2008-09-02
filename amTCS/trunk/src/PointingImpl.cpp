#include "PointingImpl.h"

PointingImpl::PointingImpl(const ACE_CString &name, maci::ContainerServices *containerServices) :
	ACSComponentImpl(name,containerServices)
{
	m_state[0] = false;
	m_state[1] = false;
	resetAdjusts();
}

void PointingImpl::initialize() throw (ACSErr::ACSbaseExImpl) {

	// new ObsList(mount,latitude)
	m_obslist = new ObsList(1,100);
}

::CORBA::Double PointingImpl::altOffset () throw (::CORBA::SystemException){
	if(m_state[0])
		return m_manualOffset.alt;
	else
		return 0;
}

::CORBA::Double PointingImpl::azmOffset () throw (::CORBA::SystemException){
	if(m_state[0])
		return m_manualOffset.az;
	else
		return 0;
}

void PointingImpl::offSetAlt (::CORBA::Double degrees) throw (::CORBA::SystemException){
	if(m_state[0])
		m_manualOffset.alt += degrees;
}

void PointingImpl::offSetAzm (::CORBA::Double degrees) throw (::CORBA::SystemException){
	if(m_state[0])
		m_manualOffset.az += degrees;
}

::TYPES::RadecPos PointingImpl::offSet (const ::TYPES::RadecPos & p, const ::CORBA::Double st) throw (::CORBA::SystemException){
	double ra_d, dec_d;
	::TYPES::RadecPos return_p;
	if(m_state[1])
	{
		m_obslist->cOff((double)p.ra, (double)p.dec, (double)st, ra_d, dec_d);
		return_p.ra = ra_d;
		return_p.dec = dec_d;
	}
	else
	{
		return_p.ra = 0;
		return_p.dec = 0;
	}
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
	if(m_state[1])
		m_obslist->cCoeffs();
}

void PointingImpl::setState (::CORBA::Boolean state, ::TYPES::PointingModel model) throw (::CORBA::SystemException){
	m_state[model] = state;
}

::CORBA::Boolean PointingImpl::getState (::TYPES::PointingModel model) throw (::CORBA::SystemException){
	return m_state[model];
}
