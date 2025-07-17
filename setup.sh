#!/bin/bash

echo "Configurando entorno para Java 6..."

# Instala Maven
sudo apt-get update
sudo apt-get install -y maven

# Verifica instalación
mvn -v

# ⚠️ Descarga todas las dependencias mientras hay internet
echo "Descargando dependencias..."
mvn dependency:resolve

# Compila (sin tests)
mvn clean install -DskipTests -Dmaven.compiler.source=1.6 -Dmaven.compiler.target=1.6


