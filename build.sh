#!/bin/bash
rm -r bin/

echo "creating necessary binaris directories"
# server dirs
mkdir -p bin/server/

# client dirs
mkdir -p bin/client/

# common dirs
mkdir -p bin/common

echo "compiling all java code"
javac -target 1.8 -source 1.8 src/clientSide/*.java
javac -target 1.8 -source 1.8 src/serverSide/*.java
javac -target 1.8 -source 1.8 src/comInf/*.java

echo "build done, ready to deploy"


#!/usr/bin/env sh

find -name "*.java" > sources.txt
javac -d bin @sources.txt