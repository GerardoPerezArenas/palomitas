#!/bin/bash

echo "⚙️ Configurando entorno Java 6 con Maven..."

# Verifica que Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "🧱 Maven no está instalado. Instalando Maven..."
    sudo apt-get update
    sudo apt-get install -y maven
fi

# Muestra versión de Maven
echo "✅ Maven instalado:"
mvn -v

# Fuerza la resolución de dependencias (reintenta en caso de error)
echo "📦 Descargando dependencias..."
for i in {1..3}; do
    mvn dependency:resolve -Dmaven.compiler.source=1.6 -Dmaven.compiler.target=1.6 && break
    echo "⚠️ Fallo al resolver dependencias. Reintentando ($i/3)..."
    sleep 5
done

# Compila el proyecto sin tests
echo "🏗️ Compilando proyecto..."
mvn clean install -DskipTests -Dmaven.compiler.source=1.6 -Dmaven.compiler.target=1.6

echo "✅ Entorno configurado correctamente."



