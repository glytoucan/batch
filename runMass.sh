#!/bin/sh
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=128M -Djava.security.egd=file:/dev/./urandom"
mvn -U -DskipTests=true -f pomMass.xml spring-boot:run
