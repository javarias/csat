#! /usr/bin/env python
import gtk
from gtk import glade
import string
import re
import gobject
from threading import Thread
from Acspy.Clients.SimpleClient import PySimpleClient
import ACS
import TYPES
import Numeric

class calGUI:
	def __init__(self):
		gobject.threads_init()
		gtk.gdk.threads_init()
		
		self.xml = glade.XML("calClient.glade")
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
		#self.simpleClient.disconnect()
		gtk.main_quit()
		#gtk.gdk.threads_leave()
		#gtk.gdk.threads_enter()

	def run(self):
		self.simpleClient = PySimpleClient.getInstance()
		self.ccinstance = self.simpleClient.getDefaultComponent("IDL:alma/CSATCONTROL_MODULE/CSATControl:1.0")
		self.csinstance = self.simpleClient.getDefaultComponent("IDL:alma/CSATSTATUS_MODULE/CSATStatus:1.0")
		self.upd = coorUpdater(self.img,self.csinstance)
		#self.upd.run()
		gtk.main()

class coorUpdater(Thread):
	def __init__(self, img, csinst):
		Thread.__init__(self)
		self.csinstance = csinst
		self.image = img

	def run(self):
		gtk.gdk.threads_enter()
		self.data = Numeric.zeros((480,640,3),'b');
		while(self.salir):
			while(self.bool):
				tmp = self.ccdinstance.getImage()
				for i in range(480):
					for j in range(640):
						self.data[i][j][0] = tmp[3*(i*640+j) + 0]
						self.data[i][j][1] = tmp[3*(i*640+j) + 1]
						self.data[i][j][2] = tmp[3*(i*640+j) + 2]
				self.pixbuf= gtk.gdk.pixbuf_new_from_array(self.data , gtk.gdk.COLORSPACE_RGB, 8)
				self.image.set_from_pixbuf(self.pixbuf)
				self.image.show()
				if self.bool2:
					self.bool=False
		gtk.gdk.threads_leave()

if __name__ == "__main__":
	app = calGUI()
	app.TCSCal.show()
	app.run()
