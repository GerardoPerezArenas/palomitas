#!/bin/bash

echo "Configurando entorno para Java 6..."

# Actualiza paquetes e instala Maven
sudo apt-get update
sudo apt-get install -y maven

# Verifica versión de Maven instalada
mvn -v

# Descarga dependencias mientras hay conexión
echo "Descargando dependencias..."
mvn dependency:resolve

# Compila el proyecto sin tests
mvn clean install -DskipTests -Dmaven.compiler.source=1.6 -Dmaven.compiler.target=1.6



