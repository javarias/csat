#!/usr/bin/env python

# Import the acspy.PySimpleClient class
from Acspy.Clients.SimpleClient import PySimpleClient
from TYPES import Position, Target
import sys
# Make an instance of the PySimpleClient
simpleClient = PySimpleClient.getInstance()

print "creating a new proposal with only one target"
targetList = []
db = simpleClient.getComponent("DATABASE")

tid = 1
pos = Position(40.0, 30.0)
target = Target(tid,pos,5)
targetList.append(target)
db.storeProposal(targetList)

del(db)
del(simpleClient)

