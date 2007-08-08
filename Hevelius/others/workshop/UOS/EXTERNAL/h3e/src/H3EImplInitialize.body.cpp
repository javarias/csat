/*
 * CVS_ID_TAG
 *
 * CVS_LOG_TAG
 *
 */

	commandedAltitude_m = new RWdouble((component_name + std::string(":commandedAltitude")).c_str(), 
			                  getComponent(), new LegoCmdAltDevIO);
	commandedAzimuth_m = new RWdouble((component_name + std::string(":commandedAzimuth")).c_str(), 
			                  getComponent(), new LegoCmdAzDevIO);
	actualAltitude_m = new ROdouble((component_name + std::string(":actualAltitude")).c_str(), 
			                  getComponent(), new LegoAltDevIO);
	actualAzimuth_m = new ROdouble((component_name + std::string(":actualAzimuth")).c_str(), 
			                  getComponent(), new LegoAzDevIO);
	status_m = new RWpattern((component_name + std::string(":status")).c_str(), 
			                  getComponent(), new LegoStatusDevIO);


