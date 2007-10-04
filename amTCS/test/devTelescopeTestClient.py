#!/usr/bin/env python

import time
from TYPES import AltazVel
from Acspy.Clients.SimpleClient import PySimpleClient

simpleClient = PySimpleClient.getInstance()

devTelescope = simpleClient.getComponent("NEXSTAR")

print "Enviando telescopio a 9,9 velocidad"
devTelescope.setVel(AltazVel(9,9))
time.sleep(4)
print "Parando telescopio"
devTelescope.setVel(AltazVel(0,0))

# 
print "Enviando telescopio a -9,-9 velocidad"
devTelescope.setVel(AltazVel(-9,-9))
time.sleep(4)
print "Parando telescopio"
devTelescope.setVel(AltazVel(0,0))

simpleClient.releaseComponent("NEXSTAR")
simpleClient.disconnect()
