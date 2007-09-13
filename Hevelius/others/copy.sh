#!/bin/bash


if [ -d $2 ]; then
	cp -r $1/* $2/
else
	mkdir $2
	cp -r $1/* $2/
fi
