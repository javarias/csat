#!/bin/bash
#
#  "@(#) $Id: make_archive.sh,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
#
#  $Log: make_archive.sh,v $
#  Revision 1.1.1.1  2007/07/09 12:47:39  wg8
#  Repository Setup
#
#

INTERFACE_NAME=LegoControl

make clean

pushd ..

if [ -d ../ws ]; then
	pushd ..
	tar --exclude={.svn,CVS,*~,*.bak,*.o,lib*,*.a,#*} -cjf $INTERFACE_NAME.tar.bz2 ./{.project,.cdtproject} ws/{config,idl,include,rtai,src} lcu/{config,idl,include,rtai,src}
	popd
else
	tar --exclude={.svn,CVS,*~,*.bak,*.o,lib*,*.a,#*} -cjf $INTERFACE_NAME.tar.bz2 ./{.project,.cdtproject,config,idl,include,rtai,src}
fi

popd
exit 0
