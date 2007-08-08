#ifndef H3EImpl_h
#define H3EImpl_h

/*
 * "@(#) $Id: H3EImpl.h,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: H3EImpl.h,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <baciCharacteristicComponentImpl.h>
#include <baciSmartPropertyPointer.h>


#include <baciRWdouble.h>
#include <baciRWpattern.h>
#include <baciROdouble.h>

#include <H3ES.h>
#include <H3EIncludes.h>

using namespace baci;


class LegoControlImpl : public virtual CharacteristicComponentImpl,
                        public virtual POA_H3E::LegoControl,
                        public         ActionImplementator

{
	public:
		/**
	 	 * Constructor
	   * @param containerServices ContainerServices which are needed for 
		 * various component related methods.
	   * @param name component name
	   */
		LegoControlImpl(const ACE_CString& name, 
				            maci::ContainerServices* containerServices);

		/**
	 	 * Destructor
	 	 */
		virtual ~LegoControlImpl();

		
/* ------------------- [ Lifecycle START interface ] --------------------- */

		/**
	 	 * Lifecycle method called at component initialization state.
	 	 * Overwrite it in case you need it.
	 	 */
		virtual void initialize(void) throw (acsErrTypeLifeCycle::acsErrTypeLifeCycleExImpl);

		/**
	 	 * Lifecycle method called at component execute state.
	 	 * Overwrite it in case you need it.
	 	 */
		virtual void execute(void) throw (acsErrTypeLifeCycle::acsErrTypeLifeCycleExImpl);

		/**
	 	 * Lifecycle method called at component clean up state.
	 	 * Overwrite it in case you need it.
	 	 */
		virtual void cleanUp(void);

		/**
	   * Lifecycle method called at component abort state.
	   * Overwrite it in case you need it.
	   */
		virtual void aboutToAbort(void);

/* ------------------- [ Lifecycle END interface ] --------------- */
/* --------------- [ Action START implementator interface ] ------ */

		/* Action handler for asynchronus calls.  */
		virtual ActionRequest invokeAction (int function,
			BACIComponent*   cob_p,
			const int&       callbackID,
			const CBDescIn&  descIn,
			BACIValue*       value_p,
			Completion&      completion,
			CBDescOut&       descOut);



	virtual ActionRequest setToAction (BACIComponent* cob,
		const int&       callbackID,
		const CBDescIn&  descIn,
		BACIValue*       value,
		Completion&      completion,
		CBDescOut&       descOut);


	virtual ActionRequest offSetAction (BACIComponent* cob,
		const int&       callbackID,
		const CBDescIn&  descIn,
		BACIValue*       value,
		Completion&      completion,
		CBDescOut&       descOut);


	virtual ActionRequest zenithAction (BACIComponent* cob,
		const int&       callbackID,
		const CBDescIn&  descIn,
		BACIValue*       value,
		Completion&      completion,
		CBDescOut&       descOut);


	virtual ActionRequest parkAction (BACIComponent* cob,
		const int&       callbackID,
		const CBDescIn&  descIn,
		BACIValue*       value,
		Completion&      completion,
		CBDescOut&       descOut);


/* --------------- [ Action END   implementator interface ] ------ */


/* --------------------- [ CORBA START interface ] ----------------*/

	virtual void setTo(CORBA::Double altitude, CORBA::Double azimuth, ACS::CBvoid_ptr cb, const ACS::CBDescIn& desc) throw(CORBA::SystemException);

	virtual void offSet(CORBA::Double altOffset, CORBA::Double azOffset, ACS::CBvoid_ptr cb, const ACS::CBDescIn& desc) throw(CORBA::SystemException);

	virtual void zenith(ACS::CBvoid_ptr cb, const ACS::CBDescIn& desc) throw(CORBA::SystemException);

	virtual void park(ACS::CBvoid_ptr cb, const ACS::CBDescIn& desc) throw(CORBA::SystemException);

	virtual void setUncalibrated() throw(CORBA::SystemException);

	virtual void calibrateEncoders() throw(CORBA::SystemException);

	virtual ACS::RWdouble_ptr commandedAltitude() throw(CORBA::SystemException);

	virtual ACS::RWdouble_ptr commandedAzimuth() throw(CORBA::SystemException);

	virtual ACS::ROdouble_ptr actualAltitude() throw(CORBA::SystemException);

	virtual ACS::ROdouble_ptr actualAzimuth() throw(CORBA::SystemException);

	virtual ACS::RWpattern_ptr status() throw(CORBA::SystemException);


/* --------------------- [ CORBA END interface ] ----------------*/

/* ----------------------------------------------------------------*/

	private:
		/**
		 * Copy constructor is not allowed following the ACS desgin rules.
		 */
		LegoControlImpl(const LegoControlImpl&);

		/**
	 	 * Assignment operator is not allowed due to ACS design rules.
	 	 */
		void operator=(const LegoControlImpl&);


/* --------------------- [ Constants START ] ----------------------*/
		const static int setTo_ACTION = 0;
		const static int offSet_ACTION = 1;
		const static int zenith_ACTION = 2;
		const static int park_ACTION = 3;

/* --------------------- [ Constants END ] ------------------------*/

/* --------------------- [ Properties START ] ----------------------*/

	SmartPropertyPointer<RWdouble> commandedAltitude_m;
	SmartPropertyPointer<RWdouble> commandedAzimuth_m;
	SmartPropertyPointer<ROdouble> actualAltitude_m;
	SmartPropertyPointer<ROdouble> actualAzimuth_m;
	SmartPropertyPointer<RWpattern> status_m;

/* --------------------- [ Properties END ] ------------------------*/

/* --------------------- [ Local properties START ] ----------------*/
#include <H3ELocalProperties.h>
/* --------------------- [ Local properties END ] ------------------*/
};


#endif /* H3EImpl_h */

