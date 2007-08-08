#!/usr/bin/env python

# Import the acspy.PySimpleClient class
from Acspy.Clients.SimpleClient import PySimpleClient
from TYPES import Position, Target
import sys
# Make an instance of the PySimpleClient
simpleClient = PySimpleClient.getInstance()

print "TEST: StoreProposal"
targetList = []
db = simpleClient.getComponent("DATABASE")

propList = db.getProposals()

print "data %s" % propList[0]  


