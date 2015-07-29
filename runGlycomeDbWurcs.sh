#!/bin/sh
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=128M -Djava.security.egd=file:/dev/./urandom"
mvn $1 -DskipTests=true -P glycomedb spring-boot:run
