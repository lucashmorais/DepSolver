#!/bin/bash

benchmarksDir=$1
choleskyDir=cholesky
matmulDir=matmul
luDir=lu


#Runs cholesky for various inputs
cd $benchmarksDir/$choleskyDir
echo $( pwd )
if [ ! -d "./executions" ]
then
	mkdir executions
fi

for i in $( seq 1 1)
	do
		for j in $( seq 1 8 )
			do
				name="e-$i-$j"
#				echo $name
				./cholesky $i $j > ./executions/$name
			done
	done

#Runs matmul for various inputs
cd $benchmarksDir/$matmulDir
echo $( pwd )
if [ ! -d "./executions" ]
then
	mkdir executions
fi

for i in $( seq 1 10 )
	do
		./matmul $i > ./executions/e-$i
	done

#Runs lu for various inputs
cd $benchmarksDir/$luDir
echo $( pwd )
if [ ! -d "./executions" ]
then
	mkdir executions
fi

for i in $( seq 1 3 )
	do
		./lu $i > ./executions/e-$i
	done

#Section for generating the dependency graph for each of the inputs
cd $benchmarksDir/$choleskyDir
echo $( pwd )
for i in $( ls executions )
	do
		java -jar "/home/lucas/Dropbox/Eclipse Workspace/DepSolver/jar/DepSolver.jar" -i $benchmarksDir/$choleskyDir/executions/$i -m \
		-O $benchmarksDir/$choleskyDir/graphs/$i-FALSE.png -h $benchmarksDir/$choleskyDir/histograms/$-FALSE.png 
	done
