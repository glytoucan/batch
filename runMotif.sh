#!/bin/sh
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=512M -Djava.security.egd=file:/dev/./urandom"
mvn exec:java -Dexec.mainClass="org.glycoinfo.batch.substructure.SubstructureTripleBatch"
