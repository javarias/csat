#! /usr/bin/env python
import os
import gtk
from gtk import glade
import string
import re
import gobject
from threading import Thread
from Acspy.Clients.SimpleClient import PySimpleClient
import ACS
import ACS__POA
import TYPES
import Numeric
import time

class calGUI:
	def __init__(self):
		self.video = False
		gobject.threads_init()
		gtk.gdk.threads_init()
		gladeFile = "../calClient.glade"
		if(not os.path.exists(gladeFile)):
			introot = 'os.environ.get("INTROOT")'
			gladeFile = introot+'/calClient.glade'
		self.xml = glade.XML(gladeFile)
		self.TCSCal = self.xml.get_widget("TCSCal")
		self.xml.signal_connect("on_TCSCal_destroy",self.quit)

		self.ra = self.xml.get_widget("ra")
		self.dec = self.xml.get_widget("dec")
		self.start = self.xml.get_widget("start")
		self.stop = self.xml.get_widget("stop")
		self.current = self.xml.get_widget("current")
		self.preset = self.xml.get_widget("preset")
		self.accept = self.xml.get_widget("accept")
		self.cancel = self.xml.get_widget("cancel")
		self.set = self.xml.get_widget("set")
		self.up = self.xml.get_widget("up")
		self.down = self.xml.get_widget("down")
		self.left = self.xml.get_widget("left")
		self.right = self.xml.get_widget("right")
		self.img = self.xml.get_widget("img")
		self.offset = self.xml.get_widget("offset")

	def quit(self,w):
		gtk.gdk.threads_leave()
		self.cupd.stop()
		self.iupd.stop()
		self.cupd.join()
		if(self.video):
			self.iupd.join()
		self.simpleClient.disconnect()
		gtk.gdk.threads_enter()
		gtk.main_quit()

	def run(self):
		self.simpleClient = PySimpleClient.getInstance()
		self.ccinstance = self.simpleClient.getDefaultComponent("IDL:alma/CSATCONTROL_MODULE/CSATControl:1.0")
		self.csinstance = self.simpleClient.getDefaultComponent("IDL:alma/CSATSTATUS_MODULE/CSATStatus:1.0")
		self.cupd = coorUpdater(self.ra,self.dec,self.csinstance)
		self.iupd = imgUpdater(self.img,self.ccinstance)
		self.cupd.start()
		if(self.video):
			self.iupd.start()
		gtk.main()

class coorUpdater(Thread):
	def __init__(self, ra_coor, dec_coor, csinst):
		Thread.__init__(self)
		self.csinstance = csinst
		self.ra = ra_coor
		self.dec = dec_coor
		self.quit = False

	def run(self):
		while(not self.quit):
			(rd_p, aa_p) = self.csinstance.getPos()
			gtk.gdk.threads_enter()
			self.ra.set_text(str(rd_p.ra))
			self.dec.set_text(str(rd_p.dec))
			gtk.gdk.threads_leave()
			time.sleep(1.0)

	def stop(self):
		self.quit = True

class imgUpdater(Thread):
	def __init__(self, img, ccinst):
		Thread.__init__(self)
		self.ccinstance = ccinst
		self.image = img
		self.quit = True
		self.video = True

	def run(self):
		self.data = Numeric.zeros((480,640,3),'b');
		desc = ACS.CBDescIn(0L,0L,0L)
		while(not self.quit):
			if(self.video):
				tmp = self.ccinstance.getPreviewImage(None, desc)
				for i in range(480):
					for j in range(640):
						self.data[i][j][0] = tmp[3*(i*640+j) + 0]
						self.data[i][j][1] = tmp[3*(i*640+j) + 1]
						self.data[i][j][2] = tmp[3*(i*640+j) + 2]
				gtk.gdk.threads_enter()
				self.pixbuf= gtk.gdk.pixbuf_new_from_array(self.data , gtk.gdk.COLORSPACE_RGB, 8)
				self.image.set_from_pixbuf(self.pixbuf)
				self.image.show()
				gtk.gdk.threads_leave()

	def stop(self):
		self.quit = True
		self.video = False


if __name__ == "__main__":
	app = calGUI()
	app.TCSCal.show()
	app.run()
