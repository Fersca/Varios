#!/bin/bash

# run.sh - Script para ejecutar Mouse
# Uso: ./run.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

# Crear directorio bin si no existe
mkdir -p bin

# Compilar
echo "Compilando Mouse.java..."
javac -d bin src/Mouse.java 2>/dev/null || javac -Xlint:-deprecation -d bin src/Mouse.java

# Ejecutar
echo "Ejecutando Mouse..."
echo ""
java -cp bin Mouse
