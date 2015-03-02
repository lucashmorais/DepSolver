#!/bin/bash

benchmarksDir=$1
choleskyDir=cholesky
matmulDir=matmul
luDir=lu
testString="one two three four five six"


#Runs cholesky for various inputs
cd $benchmarksDir/$choleskyDir
echo $( pwd )
for i in $( seq 1 20 )
	do
		for j in $( seq 1 20 )
			do
				name="e-$i-$j"
				$( ./cholesky $i $j > ./executions/$name & )
			done
	done

#Runs matmul for various inputs
#cd $benchmarksDir/$matmulDir
#echo $( pwd )
#for i in $( seq 1 20 )
#	do
#		$( ./matmul $i > ./executions/e-$i & )
#	done

#Old code, just for exploring bash
#cd $benchmarksDir/$choleskyDir
#echo $( pwd )
#for i in $( seq 1 20 )
#	do
#		for j in $( seq 1 20 )
#			do
#				echo $( uname -r )	
#			done
#	done

#Section for generating the dependency graph for each of the inputs
cd $benchmarksDir/$choleskyDir
echo $( pwd )
for i in $( ls executions )
	do
		java -jar "/home/lucas/Dropbox/Eclipse Workspace/DepSolver/jar/DepSolver.jar" -i executions/$i -cf -sf -O graphs/$i-FALSE.png -h histograms/$-FALSE.png
	done
