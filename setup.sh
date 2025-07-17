#!/bin/bash

echo "Configurando entorno para Java 6..."

# Instala Maven (versión estándar)
sudo apt-get update
sudo apt-get install -y maven

# Verifica que Maven está disponible
mvn -v

# Ejecuta la compilación
mvn clean install -DskipTests -Dmaven.compiler.source=1.6 -Dmaven.compiler.target=1.6



