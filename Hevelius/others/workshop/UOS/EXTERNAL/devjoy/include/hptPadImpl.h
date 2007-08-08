#ifndef hptPadImpl_h
#define hptPadImpl_h

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

/*
  "@(#) $Id: hptPadImpl.h,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
*/

#include <baciCharacteristicComponentImpl.h>

#include "hptExport.h"
#include "hptPadS.h"

#include <baciROlong.h>
#include <baciRWlong.h>
#include <baciROstring.h>
#include <baciRWstring.h>
#include <baciROdouble.h>
#include <baciRWdouble.h>
#include <baciROpattern.h>
#include <baciRWpattern.h>

#include "hptPadIncludes.h"

NAMESPACE_USE(baci);

class hpt_EXPORT Pad: public virtual CharacteristicComponentImpl,
                                   public virtual POA_hptPad::Pad,
                                   public ActionImplementator

{

    public:
    /**
     * Constructor
     * @param poa poa which will activate this and also all other COBs
     * @param name DO name
     */
    Pad(const ACE_CString &name, maci::ContainerServices *containerServices);

    /**
     * Destructor
     */
    virtual ~Pad();

    virtual ActionRequest
    invokeAction (int function,
                  BACIComponent *cob_p,
                  const int &callbackID,
                  const CBDescIn &descIn,
                  BACIValue *value_p,
                  Completion &completion,
                  CBDescOut &descOut);


    /* --------------- [ Action START implementator interface ] ------ */
   virtual ActionRequest
   statusAction (BACIComponent *cob,
                      const int &callbackID,
                      const CBDescIn &descIn,
                      BACIValue *value,
                      Completion &completion,
                      CBDescOut &descOut);

   virtual ActionRequest
   rebootAction (BACIComponent *cob,
                      const int &callbackID,
                      const CBDescIn &descIn,
                      BACIValue *value,
                      Completion &completion,
                      CBDescOut &descOut);

   virtual ActionRequest
   offAction (BACIComponent *cob,
                      const int &callbackID,
                      const CBDescIn &descIn,
                      BACIValue *value,
                      Completion &completion,
                      CBDescOut &descOut);

   virtual ActionRequest
   onAction (BACIComponent *cob,
                      const int &callbackID,
                      const CBDescIn &descIn,
                      BACIValue *value,
                      Completion &completion,
                      CBDescOut &descOut);

   virtual ActionRequest
   resetAction (BACIComponent *cob,
                      const int &callbackID,
                      const CBDescIn &descIn,
                      BACIValue *value,
                      Completion &completion,
                      CBDescOut &descOut);


    /* --------------- [ Action END   implementator interface ] ------ */

    /* --------------------- [ CORBA START interface ] ----------------*/
   virtual void
   status (ACS::CBvoid_ptr cb, const ACS::CBDescIn &desc)
     throw(CORBA::SystemException);

   virtual void
   reboot (ACS::CBvoid_ptr cb, const ACS::CBDescIn &desc)
     throw(CORBA::SystemException);

   virtual void
   off (ACS::CBvoid_ptr cb, const ACS::CBDescIn &desc)
     throw(CORBA::SystemException);

   virtual void
   on (ACS::CBvoid_ptr cb, const ACS::CBDescIn &desc)
     throw(CORBA::SystemException);

   virtual void
   reset (ACS::CBvoid_ptr cb, const ACS::CBDescIn &desc)
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   btnX ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   btnY ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   btnZ ()
     throw(CORBA::SystemException);

   virtual ACS::RWstring_ptr
   deviceName ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   L1 ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   axisX1 ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   btnA ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   L2 ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   axisX2 ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   slider ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   btnB ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   axisY1 ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   btnC ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   axisX3 ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   axisY2 ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   axisY3 ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   R1 ()
     throw(CORBA::SystemException);

   virtual ACS::ROlong_ptr
   R2 ()
     throw(CORBA::SystemException);


    /* --------------------- [ CORBA END interface ] ----------------*/

    /* ----------------------------------------------------------------*/

  private:
    /* --------------------- [ Properties START ] ----------------------*/
  ROlong  *m_btnX_p;
  ROlong  *m_btnY_p;
  ROlong  *m_btnZ_p;
  RWstring  *m_deviceName_p;
  ROlong  *m_L1_p;
  ROlong  *m_axisX1_p;
  ROlong  *m_btnA_p;
  ROlong  *m_L2_p;
  ROlong  *m_axisX2_p;
  ROlong  *m_slider_p;
  ROlong  *m_btnB_p;
  ROlong  *m_axisY1_p;
  ROlong  *m_btnC_p;
  ROlong  *m_axisX3_p;
  ROlong  *m_axisY2_p;
  ROlong  *m_axisY3_p;
  ROlong  *m_R1_p;
  ROlong  *m_R2_p;

    /* --------------------- [ Properties END ] ------------------------*/

    /* --------------------- [ Local properties START ] ----------------*/
#include "hptPadLocalProperties.h"
    /* --------------------- [ Local properties END ] ------------------*/
};


#endif /* hptPadImpl_h */
