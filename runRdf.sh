#!/bin/sh
. ./mvnalias.sh
mvn -U -DskipTests=true -P rdf spring-boot:run
