#!/usr/bin/env python

import sys
import UOSErr
import CORBA
import traceback
import unittest 
import TYPES
from Acspy.Clients.SimpleClient  import PySimpleClient
from Acspy.Container import Container
from maciErrType import CannotGetComponentEx

simpleclient = PySimpleClient()

try:
    comp = simpleclient.getComponent("CONSOLE")
except CannotGetComponentEx, e:
    print "Unable to get reference for CONSOLE"

def menu():
   print """
   
	MENU
	====
	1.- setMode
	2.- getMode
	3.- cameraOn
	4.- cameraOff
	5.- moveTelescope
	6.- getTelescopePosition
	7.- getCameraImage
	0.- Exit
            
   """
 
def setMode():
  while True:
  
	print """   
	MODE
	====
	1.- Automatic
	2.- Manual
    
	0.- Menu
            
	"""
   

	b = raw_input("	OPTION: ")

	if b=='1': 
		try:
			comp.setMode(True)    
			break
		except UOSErr.AlreadyInAutomaticEx, e:
			print """
		
	################################################
	# ERROR: The scheduler is already in automatic #
	################################################
		
			"""
		except UOSErr.SchedulerAlreadyRunningEx, e:
			print """
		
	################################################
	# ERROR: The scheduler is already in automatic #
	################################################
		
			"""
			
	elif b=='2':
		try:
			comp.setMode(False)   
			break
		except UOSErr.SchedulerAlreadyStoppedEx, e:
			print """
		
	#############################################
	# ERROR: The scheduler is already in manual #
	#############################################
		
			"""
	elif b==' ': setMode()
	elif b=='0': break
	
def getMode():
	mode = comp.getMode()
	
	if mode==True:
		b = raw_input("\n	It's Automatic\n")
	elif mode==False:
		b = raw_input("\n	It's Manual\n")
	
def cameraOn():
	try:
		comp.cameraOn()
		print "	You turned the camera on"
	except UOSErr.SystemInAutoModeEx, e:
		print """
		
	############################################
	# ERROR: The scheduler is set on automatic #
	############################################
		
		"""
	
def cameraOff():
	try:
		comp.cameraOff()
		print "	You turned the camera off"
	except UOSErr.SystemInAutoModeEx, e:
		print """
		
	############################################
	# ERROR: The scheduler is set on automatic #
	############################################
		
		"""
	
def moveTelescope():
	try:
		az = float(raw_input("	Set the azimut: "))
		el = float(raw_input("	Set the elevation: "))
		position = TYPES.Position(az, el)
		comp.moveTelescope(position)
	except UOSErr.SystemInAutoModeEx, e:
		print """
		
	############################################
	# ERROR: The scheduler is set on automatic #
	############################################
		
		"""
        except UOSErr.PositionOutOfLimitsEx, e:
                print """
		
	####################################################
	# ERROR: Can't move the telescope to that position #
	####################################################
		
		"""
 
def getTelescopePosition():
	position = comp.getTelescopePosition()
	print("\n	The position is:\n 	Azimut: " + str(position.az) + "\n	Elevation: " + str(position.el) + "\n")
	
def getCameraImage():
	try:
		pointer = comp.getCameraImage()
		archivo = open('image.txt', 'w')
		archivo.write(str(pointer))
		archivo.close()
		print "	Image stored in image.txt"
		
	except UOSErr.SystemInAutoModeEx, e:
		print """
		
	############################################
	# ERROR: The scheduler is set on automatic #
	############################################
		
		"""
	except UOSErr.CameraIsOffEx, e:
		print """
		
	############################
	# ERROR: The camera is off #
	############################
		
		"""
		
	except CORBA.SystemException, e:
		print """
		
	################################
	# ERROR: CORBA SystemException #
	################################
		
		"""

 
#---------------------
#- Start
#---------------------

#-- The menu is always there
while True:
	menu()
	c = raw_input("	OPTION: ")
   
	#-- options
	if c=='1': setMode()                    
	elif c=='2': getMode()        
	elif c=='3': cameraOn()       
	elif c=='4': cameraOff()      
	elif c=='5': moveTelescope()  
	elif c=='6': getTelescopePosition()
	elif c=='7': getCameraImage()
    
	elif c==' ': menu()
	elif c=='0': break   #--exit
 #-- End

print "\n	release CONSOLE"
simpleclient.releaseComponent("CONSOLE")
simpleclient.disconnect()
print "-- FIN --"
