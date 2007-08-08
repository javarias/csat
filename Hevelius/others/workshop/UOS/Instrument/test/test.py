#!/usr/bin/env python

# Import the acspy.PySimpleClient class
from Acspy.Clients.SimpleClient import PySimpleClient


# Make an instance of the PySimpleClient
simpleClient = PySimpleClient.getInstance()

instrument = simpleClient.getComponent("INSTRUMENT")
instrument.cameraOn()
instrument.cameraOff()
simpleClient.releaseComponent("INSTRUMENT")
