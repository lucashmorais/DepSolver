#!/bin/bash
rm *.class
rm *.jar
javac ../src/*.java -d .
jar cfm DepSolver.jar manifest.txt *.class
