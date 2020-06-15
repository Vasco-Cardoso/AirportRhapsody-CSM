#!/usr/bin/env sh

find -name "*.java" > sources.txt
javac -target 1.8 -source 1.8 -d bin @sources.txt