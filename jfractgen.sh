#!/bin/sh

JAVA_BIN=$(which java)
JAVA_OPTS="-Xmx1024M"
BASE=$(dirname ${0})
JAR=jfractgen-0.9.jar

"${JAVA_BIN}" ${JAVA_OPTS} -jar "${BASE}/${JAR}"


