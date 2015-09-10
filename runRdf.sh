#!/bin/sh
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=128M -Djava.security.egd=file:/dev/./urandom"
mvn -U -DskipTests=true -P rdf spring-boot:run
