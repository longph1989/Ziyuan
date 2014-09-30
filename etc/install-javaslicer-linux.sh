#!/bin/bash

JARS_PATH=$TRUNK/etc/javaslicer/assembly
echo JARS_PATH = $JARS_PATH
SRC_PATH=$TRUNK/etc/javaslicer/src

cd $M2_BIN

mvn install:install-file -Dfile=$JARS_PATH/slicer.jar -Dsources=$SRC_PATH/javaslicer_src_20101004.zip -DgroupId=de.unisb.cs.st -DartifactId=slicer -Dversion=20101004 -Dpackaging=jar

mvn install:install-file -Dfile=$JARS_PATH/tracer.jar -Dsources=$SRC_PATH/javaslicer_src_20101004.zip -DgroupId=de.unisb.cs.st -DartifactId=tracer -Dversion=20101004 -Dpackaging=jar

mvn install:install-file -Dfile=$JARS_PATH/traceReader.jar -Dsources=$SRC_PATH/javaslicer_src_20101004.zip -DgroupId=de.unisb.cs.st -DartifactId=traceReader -Dversion=20101004 -Dpackaging=jar

mvn install:install-file -Dfile=$JARS_PATH/visualize.jar -Dsources=$SRC_PATH/javaslicer_src_20101004.zip -DgroupId=de.unisb.cs.st -DartifactId=visualize -Dversion=20101004 -Dpackaging=jar