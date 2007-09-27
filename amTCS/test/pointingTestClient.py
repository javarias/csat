#!/usr/bin/env python

from Acspy.Clients.SimpleClient import PySimpleClient

simpleClient = PySimpleClient.getInstance()

pointing = simpleClient.getComponent("POINTING")
pointing.offSetAlt(0.01)
pointing.offSetAzm(0.03)

print "alt: %s" % pointing._get_altOffset()
print "azm: %s" % pointing._get_azmOffset()

simpleClient.releaseComponent("POINTING")
simpleClient.disconnect()
