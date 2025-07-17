#!/bin/bash

echo "Configurando entorno para Java 6..."

# Forzamos compilación para Java 6
mvn clean install -Dmaven.compiler.source=1.6 -Dmaven.compiler.target=1.6 -DskipTests

