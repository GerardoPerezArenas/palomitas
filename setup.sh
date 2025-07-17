#!/bin/bash

echo "Configurando entorno para Java 6..."

sudo apt-get update
sudo apt-get install -y maven

mvn -v

# ⚠️ Solo compilación, sin forzar resolución previa
mvn -s .mvn/settings.xml clean install -DskipTests -Dmaven.compiler.source=1.6 -Dmaven.compiler.target=1.6



