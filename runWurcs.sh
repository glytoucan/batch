#!/bin/sh
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=128M -Djava.security.egd=file:/dev/./urandom"
. ./triplestore.dev.env
echo SPRING_TRIPLESTORE_URL=$SPRING_TRIPLESTORE_URL SPRING_TRIPLESTORE_PASSWORD=$SPRING_TRIPLESTORE_PASSWORD mvn -U -DskipTests=true -P wurcs spring-boot:run
SPRING_TRIPLESTORE_URL=$SPRING_TRIPLESTORE_URL SPRING_TRIPLESTORE_PASSWORD=$SPRING_TRIPLESTORE_PASSWORD mvn -U -DskipTests=true -P wurcs spring-boot:run
