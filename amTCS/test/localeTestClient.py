#!/usr/bin/env python

from Acspy.Clients.SimpleClient import PySimpleClient

simpleClient = PySimpleClient.getInstance()

locale = simpleClient.getComponent("LOCALE")

print "Current sidereal time: %s" % locale.siderealTime()
print "Current latitude:      %s" % locale.localPos().latitude
print "Current longitude:     %s" % locale.localPos().longitude

simpleClient.releaseComponent("LOCALE")
simpleClient.disconnect()
