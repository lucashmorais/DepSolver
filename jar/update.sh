#!/bin/bash
rm *.class
cp ../bin/*.class .
rm *.jar
jar cfm DepSolver.jar manifest.txt *.class
