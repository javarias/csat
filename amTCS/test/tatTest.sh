#!/bin/ksh

echo "This is my first tat test"
echo "This should be exiting :D"
echo "My IP is $(/sbin/ifconfig  | grep "inet addr" | grep Bcast | cut -d: -f2 | cut -d" " -f1)"
exit 0
