/*
 * "@(#) $Id: H3EparkAction.body.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: H3EparkAction.body.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

/*
 * This method is void and returns nothing.
 */

/* Park position is at 180° altitude, 360° azimuth,
 * the perfect position to turn the RCX off having
 * the telescope ready to start calibration.
 */

commandedAltitude_m->set_sync(180);
commandedAzimuth_m->set_sync(360);

