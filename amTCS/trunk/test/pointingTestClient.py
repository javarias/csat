#!/usr/bin/env python

from Acspy.Clients.SimpleClient import PySimpleClient

simpleClient = PySimpleClient.getInstance()

pointing = simpleClient.getComponent("POINTING")
pointing.offSetAlt(0.01)
pointing.offSetAzm(0.03)

print "Current altitude offset: %s" % pointing._get_altOffset()
print "Current azimuth  offset: %s" % pointing._get_azmOffset()

print "Reseting the offsets..."

pointing.offSetAlt(-pointing._get_altOffset())
pointing.offSetAzm(-pointing._get_azmOffset())

print "Current altitude offset: %s" % pointing._get_altOffset()
print "Current azimuth  offset: %s" % pointing._get_azmOffset()

simpleClient.releaseComponent("POINTING")
simpleClient.disconnect()
