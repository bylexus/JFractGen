#!/bin/sh

JAVA_BIN=$(which java)
JAVA_OPTS="-Xmx512M"
BASE=$(dirname ${0})
JAR=JFractGen.jar

"${JAVA_BIN}" ${JAVA_OPTS} -jar "${BASE}/${JAR}"


