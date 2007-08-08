#! /usr/bin/env python
#*******************************************************************************
# ALMA - Atacama Large Millimiter Array
# (c) Associated Universities Inc., 2007 
# 
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
# 
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
# 
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
#
# "@(#) $Id: telescopeSim.py,v 1.1 2007/07/10 18:57:20 wg5 Exp $"
#
# who       when      what
# --------  --------  ----------------------------------------------
# wg5  2007-07-10  created
#

#************************************************************************
#   NAME
# 
#   SYNOPSIS
# 
#   DESCRIPTION
#
#   FILES
#
#   ENVIRONMENT
#
#   RETURN VALUES
#
#   CAUTIONS
#
#   EXAMPLES
#
#   SEE ALSO
#
#   BUGS     
#
#------------------------------------------------------------------------
#

import acstime
from Acssim.Goodies import getGlobalData,setGlobalData
import TYPES
import time

def moveTo(params):
	print "Simulated  moveTo"
	print

	currentAz=params[0].az
	currentEl=params[0].el
	setGlobalData("currentAz",currentAz)
	setGlobalData("currentEl",currentEl)

	print "imprime %f" % getGlobalData("currentAz")
	print "imprime %f" % getGlobalData("currentEl")

	time.sleep(5)
	print "moveTo Finished"

def getCurrentPosition():
	print "Simulated getCurrentPosition"
	print

	position = TYPES.Position(getGlobalData("currentAz"),getGlobalData("currentEl"))
	return position

def observe(params):
	print "Simulated observe"
	print
	
	currentAz=params[0].az
        currentEl=params[0].el
        setGlobalData("currentAz",currentAz)
        setGlobalData("currentEl",currentEl)

        print "imprime %f" % getGlobalData("currentAz")
        print "imprime %f" % getGlobalData("currentEl")

        time.sleep(float(params[1]))
        print "observe Finished"

#
# ___oOo___
