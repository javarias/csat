s/[0-9]\+-[0-9]\+-[0-9]\+T[0-9]\+:[0-9]\+:[0-9]\+\.[0-9]\+ PySingletonClient disconnect - Shutdown called for client/nnnn-nn-nnTnn:nn:nn.nnn PySingletonClient disconnect - Shutdown called for client/g
s/Current sidereal time: [0-9]\+\.[0-9]\+/Current sidereal time: nnn.nnn/g
s/Current latitude:      -\?[0-9]\+\.[0-9]\+/Current latitude:      nn.nnn/g
s/Current longitude:     -\?[0-9]\+\.[0-9]\+/Current longitude:     nn.nnn/g
s/.*client 'Pointing Test Client' has successfully released.*/1 - Client released component/g
s/Time: [0-9]\+,[0-9]\+/Time: n,nnn/g
s/[0-9A-F][0-9A-F] [0-9A-F][0-9A-F] [0-9A-F][0-9A-F] [0-9A-F][0-9A-F] [0-9A-F][0-9A-F] [0-9A-F][0-9A-F] [0-9A-F][0-9A-F] [0-9A-F][0-9A-F] [0-9A-F][0-9A-F] [0-9A-F][0-9A-F] [0-9A-F][0-9A-F]//g
