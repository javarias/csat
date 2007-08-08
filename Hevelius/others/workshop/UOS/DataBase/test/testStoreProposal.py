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
for i in range(0,5):
	tid = i
	pos = Position(50.0, 70.0)
	target = Target(tid,pos,5)
	targetList.append(target)

db.storeProposal(targetList)

sys.exit()
print "TEST: getProposals"
proposalList = db.getProposals()
n = len(proposalList)
print n

print "TEST: getProposalStatus"
for p in n:
	pid = p.pid
	status = db.getProposalStatus(pid)
	print "PID=%d STATUS=%d" %(pid,status)

print "TEST: setProposalStatus"	
for p in n:	
	for i in range(0,3):
		db.setProposalStatus(pid, i)
		newStatus = db.getProposal(pid)
		print "PID=%d NEW_STATUS=%d" %(pid,newStatus)

del(db)
del(simpleClient)
