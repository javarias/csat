#!/usr/bin/env python

import time
from TYPES import AltazVel
from Acspy.Clients.SimpleClient import PySimpleClient

simpleClient = PySimpleClient.getInstance()

devTelescope = simpleClient.getComponent("LX200")

print "Enviando telescopio a 2,2 velocidad"
devTelescope.setVel(AltazVel(2,2))
time.sleep(4)
print "Parando telescopio"
devTelescope.setVel(AltazVel(0,0))

# 
print "Enviando telescopio a -2,-2 velocidad"
devTelescope.setVel(AltazVel(-2,-2))
time.sleep(4)
print "Parando telescopio"
devTelescope.setVel(AltazVel(0,0))

simpleClient.releaseComponent("LX200")
simpleClient.disconnect()
