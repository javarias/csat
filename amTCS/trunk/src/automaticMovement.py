#!/usr/bin/python

import time
import os
from TYPES import RadecPos
from Acspy.Clients.SimpleClient import PySimpleClient
from Acspy.Common.Callbacks     import CBvoid
from ACS import CBDescIn

simpleClient = PySimpleClient.getInstance()

csatControl = simpleClient.getComponent("CSATCONTROL")

pos = RadecPos(135,40)
while 1:
	lala = CBvoid()
	lalo = CBDescIn(0,0,0)
	csatControl.preset(pos,simpleClient.activateOffShoot(lala),lalo)
	time.sleep(20)
	csatControl.stopTelescope()

	lala = CBvoid()
	lalo = CBDescIn(0,0,0)
	pos = RadecPos(180,0)
	csatControl.preset(pos,simpleClient.activateOffShoot(lala),lalo)
	time.sleep(20)
	csatControl.stopTelescope()

	lala = CBvoid()
	lalo = CBDescIn(0,0,0)
	pos = RadecPos(160,30)
	csatControl.preset(pos,simpleClient.activateOffShoot(lala),lalo)
	time.sleep(20)
	csatControl.stopTelescope()

	lala = CBvoid()
	lalo = CBDescIn(0,0,0)
	pos = RadecPos(234,-15)
	csatControl.preset(pos,simpleClient.activateOffShoot(lala),lalo)
	time.sleep(20)
	csatControl.stopTelescope()

