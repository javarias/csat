#include <vltPort.h>

static char *rcsId="@(#) $Id: hptPadImpl.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $";
 static void *use_rcsId=((void)&use_rcsId,(void *) &rcsId);

#include "hptPadImpl.h"
#include "hptPadImplIncludes.h"
#include "hptPadHelperFunctions.cpp"
/*
  "@(#) $Id: hptPadImpl.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
*/

NAMESPACE_USE(baci);

const static int status_ACTION = 0;
const static int reboot_ACTION = 1;
const static int off_ACTION = 2;
const static int on_ACTION = 3;
const static int reset_ACTION = 4;
Pad::Pad(const ACE_CString &name, maci::ContainerServices *containerServices) :
   CharacteristicComponentImpl(name, containerServices)
  , m_btnX_p(0) 
  , m_btnY_p(0) 
  , m_btnZ_p(0) 
  , m_deviceName_p(0) 
  , m_L1_p(0) 
  , m_axisX1_p(0) 
  , m_btnA_p(0) 
  , m_L2_p(0) 
  , m_axisX2_p(0) 
  , m_slider_p(0) 
  , m_btnB_p(0) 
  , m_axisY1_p(0) 
  , m_btnC_p(0) 
  , m_axisX3_p(0) 
  , m_axisY2_p(0) 
  , m_axisY3_p(0) 
  , m_R1_p(0) 
  , m_R2_p(0) 
{
   ACS_TRACE("::Pad::Pad");
   m_btnX_p = new ROlong(name+":btnX", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(btnX, m_btnX_p)
   m_btnY_p = new ROlong(name+":btnY", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(btnY, m_btnY_p)
   m_btnZ_p = new ROlong(name+":btnZ", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(btnZ, m_btnZ_p)
   m_deviceName_p = new RWstring(name+":deviceName", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(deviceName, m_deviceName_p)
   m_L1_p = new ROlong(name+":L1", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(L1, m_L1_p)
   m_axisX1_p = new ROlong(name+":axisX1", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(axisX1, m_axisX1_p)
   m_btnA_p = new ROlong(name+":btnA", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(btnA, m_btnA_p)
   m_L2_p = new ROlong(name+":L2", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(L2, m_L2_p)
   m_axisX2_p = new ROlong(name+":axisX2", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(axisX2, m_axisX2_p)
   m_slider_p = new ROlong(name+":slider", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(slider, m_slider_p)
   m_btnB_p = new ROlong(name+":btnB", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(btnB, m_btnB_p)
   m_axisY1_p = new ROlong(name+":axisY1", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(axisY1, m_axisY1_p)
   m_btnC_p = new ROlong(name+":btnC", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(btnC, m_btnC_p)
   m_axisX3_p = new ROlong(name+":axisX3", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(axisX3, m_axisX3_p)
   m_axisY2_p = new ROlong(name+":axisY2", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(axisY2, m_axisY2_p)
   m_axisY3_p = new ROlong(name+":axisY3", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(axisY3, m_axisY3_p)
   m_R1_p = new ROlong(name+":R1", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(R1, m_R1_p)
   m_R2_p = new ROlong(name+":R2", getComponent());
   CHARACTERISTIC_COMPONENT_PROPERTY(R2, m_R2_p)
#include "hptPadImplInit.cpp"
#include "hptPadThreadInit.cpp"
}
#include "hptPadThreadImpl.cpp"
Pad::~Pad()
{
   ACS_TRACE("::Pad::~Pad");
   if (getComponent())
   {
    ACS_DEBUG_PARAM("::Pad::~Pad", "Destroying %s...",  getComponent()->getName());
    getComponent()->stopAllThreads();
   }
   if (m_btnX_p)
   {
      m_btnX_p->destroy();
      m_btnX_p = 0;
   }
   if (m_btnY_p)
   {
      m_btnY_p->destroy();
      m_btnY_p = 0;
   }
   if (m_btnZ_p)
   {
      m_btnZ_p->destroy();
      m_btnZ_p = 0;
   }
   if (m_deviceName_p)
   {
      m_deviceName_p->destroy();
      m_deviceName_p = 0;
   }
   if (m_L1_p)
   {
      m_L1_p->destroy();
      m_L1_p = 0;
   }
   if (m_axisX1_p)
   {
      m_axisX1_p->destroy();
      m_axisX1_p = 0;
   }
   if (m_btnA_p)
   {
      m_btnA_p->destroy();
      m_btnA_p = 0;
   }
   if (m_L2_p)
   {
      m_L2_p->destroy();
      m_L2_p = 0;
   }
   if (m_axisX2_p)
   {
      m_axisX2_p->destroy();
      m_axisX2_p = 0;
   }
   if (m_slider_p)
   {
      m_slider_p->destroy();
      m_slider_p = 0;
   }
   if (m_btnB_p)
   {
      m_btnB_p->destroy();
      m_btnB_p = 0;
   }
   if (m_axisY1_p)
   {
      m_axisY1_p->destroy();
      m_axisY1_p = 0;
   }
   if (m_btnC_p)
   {
      m_btnC_p->destroy();
      m_btnC_p = 0;
   }
   if (m_axisX3_p)
   {
      m_axisX3_p->destroy();
      m_axisX3_p = 0;
   }
   if (m_axisY2_p)
   {
      m_axisY2_p->destroy();
      m_axisY2_p = 0;
   }
   if (m_axisY3_p)
   {
      m_axisY3_p->destroy();
      m_axisY3_p = 0;
   }
   if (m_R1_p)
   {
      m_R1_p->destroy();
      m_R1_p = 0;
   }
   if (m_R2_p)
   {
      m_R2_p->destroy();
      m_R2_p = 0;
   }
    ACS_DEBUG("::Pad::~Pad", "Properties destroyed");
}
/* --------------- [ Action implementator interface ] -------------- */

ActionRequest
Pad::invokeAction (int function,
                           BACIComponent *cob,
                           const int &callbackID,
                           const CBDescIn &descIn,
                           BACIValue *value,
                           Completion &completion,
                           CBDescOut &descOut)
{
   switch (function)
   {
      case status_ACTION:
      {
         return statusAction (cob, callbackID, descIn, value, completion, descOut);
      }
      case reboot_ACTION:
      {
         return rebootAction (cob, callbackID, descIn, value, completion, descOut);
      }
      case off_ACTION:
      {
         return offAction (cob, callbackID, descIn, value, completion, descOut);
      }
      case on_ACTION:
      {
         return onAction (cob, callbackID, descIn, value, completion, descOut);
      }
      case reset_ACTION:
      {
         return resetAction (cob, callbackID, descIn, value, completion, descOut);
      }
      default:
      {
       return reqDestroy;
      }
   }
}
/* ------------------ [ Action implementations ] ----------------- */

ActionRequest
Pad::statusAction (BACIComponent *cob,
                           const int &callbackID,
                           const CBDescIn &descIn,
                           BACIValue *value,
                           Completion &completion,
                           CBDescOut &descOut)
{
   ACS_DEBUG_PARAM("::Pad::statusAction", "%s",  getComponent()->getName());
#include "hptPadstatusAction.body"

  completion = ACSErrTypeOK::ACSErrOKCompletion();
  return reqInvokeDone;
}
ActionRequest
Pad::rebootAction (BACIComponent *cob,
                           const int &callbackID,
                           const CBDescIn &descIn,
                           BACIValue *value,
                           Completion &completion,
                           CBDescOut &descOut)
{
   ACS_DEBUG_PARAM("::Pad::rebootAction", "%s",  getComponent()->getName());
#include "hptPadrebootAction.body"

  completion = ACSErrTypeOK::ACSErrOKCompletion();
  return reqInvokeDone;
}
ActionRequest
Pad::offAction (BACIComponent *cob,
                           const int &callbackID,
                           const CBDescIn &descIn,
                           BACIValue *value,
                           Completion &completion,
                           CBDescOut &descOut)
{
   ACS_DEBUG_PARAM("::Pad::offAction", "%s",  getComponent()->getName());
#include "hptPadoffAction.body"

  completion = ACSErrTypeOK::ACSErrOKCompletion();
  return reqInvokeDone;
}
ActionRequest
Pad::onAction (BACIComponent *cob,
                           const int &callbackID,
                           const CBDescIn &descIn,
                           BACIValue *value,
                           Completion &completion,
                           CBDescOut &descOut)
{
   ACS_DEBUG_PARAM("::Pad::onAction", "%s",  getComponent()->getName());
  #include "hptPadonAction.body"

  completion = ACSErrTypeOK::ACSErrOKCompletion();

  return reqInvokeDone;
}
ActionRequest
Pad::resetAction (BACIComponent *cob,
                           const int &callbackID,
                           const CBDescIn &descIn,
                           BACIValue *value,
                           Completion &completion,
                           CBDescOut &descOut)
{
   ACS_DEBUG_PARAM("::Pad::resetAction", "%s",  getComponent()->getName());
#include "hptPadresetAction.body"

  completion = ACSErrTypeOK::ACSErrOKCompletion();
  return reqInvokeDone;
}
/* ------------------ [ Functions ] ----------------- */

/* --------------------- [ CORBA interface ] ----------------------*/
void
Pad::status (ACS::CBvoid_ptr cb,
                 const ACS::CBDescIn &desc)
                   throw(CORBA::SystemException)
{
    getComponent()->registerAction(BACIValue::type_null, cb, desc, this, status_ACTION);
}

void
Pad::reboot (ACS::CBvoid_ptr cb,
                 const ACS::CBDescIn &desc)
                   throw(CORBA::SystemException)
{
    getComponent()->registerAction(BACIValue::type_null, cb, desc, this, reboot_ACTION);
}

void
Pad::off (ACS::CBvoid_ptr cb,
                 const ACS::CBDescIn &desc)
                   throw(CORBA::SystemException)
{
    getComponent()->registerAction(BACIValue::type_null, cb, desc, this, off_ACTION);
}

void
Pad::on (ACS::CBvoid_ptr cb,
                 const ACS::CBDescIn &desc)
                   throw(CORBA::SystemException)
{
    getComponent()->registerAction(BACIValue::type_null, cb, desc, this, on_ACTION);
}

void
Pad::reset (ACS::CBvoid_ptr cb,
                 const ACS::CBDescIn &desc)
                   throw(CORBA::SystemException)
{
    getComponent()->registerAction(BACIValue::type_null, cb, desc, this, reset_ACTION);
}

ACS::ROlong_ptr
Pad::btnX()
    throw(CORBA::SystemException)
{
    if (!m_btnX_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_btnX_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::btnY()
    throw(CORBA::SystemException)
{
    if (!m_btnY_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_btnY_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::btnZ()
    throw(CORBA::SystemException)
{
    if (!m_btnZ_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_btnZ_p->getCORBAReference());
    return prop._retn();
}
ACS::RWstring_ptr
Pad::deviceName()
    throw(CORBA::SystemException)
{
    if (!m_deviceName_p)
       {
        return ACS::RWstring::_nil();
       }
    ACS::RWstring_var prop = ACS::RWstring::_narrow(m_deviceName_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::L1()
    throw(CORBA::SystemException)
{
    if (!m_L1_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_L1_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::axisX1()
    throw(CORBA::SystemException)
{
    if (!m_axisX1_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_axisX1_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::btnA()
    throw(CORBA::SystemException)
{
    if (!m_btnA_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_btnA_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::L2()
    throw(CORBA::SystemException)
{
    if (!m_L2_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_L2_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::axisX2()
    throw(CORBA::SystemException)
{
    if (!m_axisX2_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_axisX2_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::slider()
    throw(CORBA::SystemException)
{
    if (!m_slider_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_slider_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::btnB()
    throw(CORBA::SystemException)
{
    if (!m_btnB_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_btnB_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::axisY1()
    throw(CORBA::SystemException)
{
    if (!m_axisY1_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_axisY1_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::btnC()
    throw(CORBA::SystemException)
{
    if (!m_btnC_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_btnC_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::axisX3()
    throw(CORBA::SystemException)
{
    if (!m_axisX3_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_axisX3_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::axisY2()
    throw(CORBA::SystemException)
{
    if (!m_axisY2_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_axisY2_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::axisY3()
    throw(CORBA::SystemException)
{
    if (!m_axisY3_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_axisY3_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::R1()
    throw(CORBA::SystemException)
{
    if (!m_R1_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_R1_p->getCORBAReference());
    return prop._retn();
}
ACS::ROlong_ptr
Pad::R2()
    throw(CORBA::SystemException)
{
    if (!m_R2_p)
       {
        return ACS::ROlong::_nil();
       }
    ACS::ROlong_var prop = ACS::ROlong::_narrow(m_R2_p->getCORBAReference());
    return prop._retn();
}
/* --------------- [ MACI DLL support functions ] -----------------*/

#include <maciACSComponentDefines.h>
MACI_DLL_SUPPORT_FUNCTIONS(Pad)
