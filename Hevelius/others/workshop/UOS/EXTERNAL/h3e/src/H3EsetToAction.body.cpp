/*
 * "@(#) $Id: H3EsetToAction.body.cpp,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log: H3EsetToAction.body.cpp,v $
 * Revision 1.1.1.1  2007/07/09 12:47:39  wg8
 * Repository Setup
 *
 *
 */

/*
 * needed:
 * ret_value = something of type void.
 * By default it is set to 1|true|"1","OK".
 *
 */

commandedAltitude_m->set_sync(param_p->altitude);
commandedAzimuth_m->set_sync(param_p->azimuth);

