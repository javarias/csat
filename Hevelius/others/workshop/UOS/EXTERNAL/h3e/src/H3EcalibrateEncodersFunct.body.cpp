/** \file H3EcalibrateEncodersFunct.body.cpp
 * This file is included inside  the LegoControlImpl::calibrateEncoders()
 * method body.
 */

/** \fn void LegoControlImpl::calibrateEncoders() throw(CORBA::SystemException)
 * Starts the calibration routine onto the Lego hardware. To do this, a previous
 * call to setUncalibrated() is required. Moreover, the lego antenna needs to be 
 * at a specific position: the azimuthal axis has to be a few degrees clockwise
 * far from the azimuthal touch sensor, and the altitude axis must be set to 
 * the extreme (0° or 180°) where the altitude touch sensor is not being in 
 * touch.
 *
 * \returns Nothing.
 */

#include "SCTdefs.h"

status_m->set_sync((ACS::pattern)STAT_CALIBRATE);

