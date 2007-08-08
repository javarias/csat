/*
 * The idea behind this method is to move the antenna
 * some distance away from the actual position.
 * I.e. set the commanded position to (actual position+offset).
 */

ACSErr::Completion *comp = new ACSErr::Completion();
commandedAltitude_m->set_sync(actualAltitude_m->get_sync(comp) + param_p->altOffset);
commandedAzimuth_m->set_sync (actualAzimuth_m->get_sync(comp) + param_p->azOffset);
delete comp;

