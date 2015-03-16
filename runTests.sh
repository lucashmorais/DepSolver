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

for i in $( seq 1 6 )
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

for i in $( seq 1 7 )
	do
		./lu $i > ./executions/e-$i
	done

#Section for generating the dependency graph for each of the inputs
cd $benchmarksDir/$choleskyDir
echo $( pwd )
for i in $( ls executions )
	do
		java -jar "/home/lucas/Dropbox/Eclipse Workspace/DepSolver/jar/DepSolver.jar" -i executions/$i -m -cf \
		-O graphs/$i-FALSE.png -h histograms/$i-FALSE.png 
		java -jar "/home/lucas/Dropbox/Eclipse Workspace/DepSolver/jar/DepSolver.jar" -i executions/$i -m -ct \
		-Ot graphs/$i-TRUE -h histograms/$i-TRUE.png

	done

cd $benchmarksDir/$matmulDir
echo $( pwd )
for i in $( ls executions )
	do
		java -jar "/home/lucas/Dropbox/Eclipse Workspace/DepSolver/jar/DepSolver.jar" -i executions/$i -m -cf \
		-O graphs/$i-FALSE.png -h histograms/$i-FALSE.png 
                java -jar "/home/lucas/Dropbox/Eclipse Workspace/DepSolver/jar/DepSolver.jar" -i executions/$i -m -ct \
		-Ot graphs/$i-TRUE -h histograms/$i-TRUE.png
	done

cd $benchmarksDir/$luDir
echo $( pwd )
for i in $( ls executions )
        do
	        java -jar "/home/lucas/Dropbox/Eclipse Workspace/DepSolver/jar/DepSolver.jar" -i executions/$i -m -cf \
	        -O graphs/$i-FALSE.png -h histograms/$i-FALSE.png
		java -jar "/home/lucas/Dropbox/Eclipse Workspace/DepSolver/jar/DepSolver.jar" -i executions/$i -m -ct \
		-Ot graphs/$i-TRUE -h histograms/$i-TRUE.png
	done

