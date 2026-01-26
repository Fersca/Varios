#!/bin/bash

# test.sh - Script para ejecutar tests de Mouse con JUnit 5
# Uso: ./test.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "========================================="
echo "  Mouse Project - Test Runner (JUnit 5)"
echo "========================================="
echo ""

# 1. Crear directorio bin si no existe
echo -e "${YELLOW}[1/7]${NC} Verificando directorios..."
mkdir -p bin lib

# 2. Descargar JAR de JUnit 5 Platform Console Standalone
JUNIT5_JAR="lib/junit-platform-console-standalone-1.10.2.jar"

echo -e "${YELLOW}[2/7]${NC} Verificando dependencias de JUnit 5..."

if [ ! -f "$JUNIT5_JAR" ]; then
    echo "  Descargando junit-platform-console-standalone-1.10.2.jar..."
    curl -sL "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar" -o "$JUNIT5_JAR"
    echo "  ✓ junit-platform-console-standalone-1.10.2.jar descargado"
else
    echo "  ✓ junit-platform-console-standalone-1.10.2.jar ya existe"
fi

# Limpiar JARs antiguos de JUnit 4 si existen
if [ -f "lib/junit-4.13.2.jar" ]; then
    rm "lib/junit-4.13.2.jar"
    echo "  ✓ Removido junit-4.13.2.jar (obsoleto)"
fi
if [ -f "lib/hamcrest-core-1.3.jar" ]; then
    rm "lib/hamcrest-core-1.3.jar"
    echo "  ✓ Removido hamcrest-core-1.3.jar (ya no necesario)"
fi

# 3. Compilar código fuente (target Java 21 para compatibilidad con JaCoCo)
echo -e "${YELLOW}[3/7]${NC} Compilando código fuente..."
javac --release 21 -d bin src/Mouse.java
echo "  ✓ Mouse.java compilado"

# 4. Compilar tests
echo -e "${YELLOW}[4/7]${NC} Compilando tests..."
javac --release 21 -d bin -cp "bin:$JUNIT5_JAR" test/MouseTest.java
echo "  ✓ MouseTest.java compilado"

# 5. Descargar JaCoCo y ejecutar tests con coverage
echo -e "${YELLOW}[5/7]${NC} Ejecutando tests..."
echo ""
echo "-----------------------------------------"

# Descargar JaCoCo si no existe
JACOCO_VERSION="0.8.11"
JACOCO_AGENT="lib/jacocoagent.jar"
JACOCO_CLI="lib/jacococli.jar"

echo -e "${YELLOW}[6/7]${NC} Verificando JaCoCo para code coverage..."

if [ ! -f "$JACOCO_AGENT" ] || [ ! -f "$JACOCO_CLI" ]; then
    echo "  Descargando JaCoCo $JACOCO_VERSION..."
    JACOCO_ZIP="lib/jacoco-$JACOCO_VERSION.zip"
    curl -sL "https://repo1.maven.org/maven2/org/jacoco/jacoco/$JACOCO_VERSION/jacoco-$JACOCO_VERSION.zip" -o "$JACOCO_ZIP"
    unzip -q -o "$JACOCO_ZIP" -d lib/jacoco-temp
    mv lib/jacoco-temp/lib/jacocoagent.jar "$JACOCO_AGENT"
    mv lib/jacoco-temp/lib/jacococli.jar "$JACOCO_CLI"
    rm -rf lib/jacoco-temp "$JACOCO_ZIP"
    echo "  ✓ JaCoCo descargado"
else
    echo "  ✓ JaCoCo ya existe"
fi

# Ejecutar tests con JaCoCo agent
echo -e "${YELLOW}[7/7]${NC} Ejecutando tests con coverage..."
echo ""
echo "-----------------------------------------"

java -javaagent:$JACOCO_AGENT=destfile=bin/jacoco.exec,includes=Mouse,excludes=MouseTest \
    -jar "$JUNIT5_JAR" --class-path bin --scan-class-path

RESULT=$?
echo "-----------------------------------------"
echo ""

if [ $RESULT -eq 0 ]; then
    echo -e "${GREEN}✓ Todos los tests pasaron exitosamente${NC}"
else
    echo -e "${RED}✗ Algunos tests fallaron${NC}"
    exit 1
fi

# Generar reporte de coverage
echo ""
echo -e "${YELLOW}Generando reporte de code coverage...${NC}"
echo ""

# Generar reporte HTML
mkdir -p coverage
java -jar "$JACOCO_CLI" report bin/jacoco.exec \
    --classfiles bin/Mouse.class \
    --sourcefiles src \
    --html coverage \
    --csv coverage/coverage.csv \
    --name "Mouse Coverage Report" 2>/dev/null

# Mostrar resumen de coverage en terminal
echo -e "${GREEN}═══════════════════════════════════════${NC}"
echo -e "${GREEN}           CODE COVERAGE REPORT        ${NC}"
echo -e "${GREEN}═══════════════════════════════════════${NC}"
echo ""

# Parsear CSV y mostrar resultados
if [ -f coverage/coverage.csv ]; then
    # Leer la segunda línea del CSV (datos)
    DATA=$(tail -1 coverage/coverage.csv)

    # Extraer valores (formato: GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,...)
    INST_MISSED=$(echo "$DATA" | cut -d',' -f4)
    INST_COVERED=$(echo "$DATA" | cut -d',' -f5)
    BRANCH_MISSED=$(echo "$DATA" | cut -d',' -f6)
    BRANCH_COVERED=$(echo "$DATA" | cut -d',' -f7)
    LINE_MISSED=$(echo "$DATA" | cut -d',' -f8)
    LINE_COVERED=$(echo "$DATA" | cut -d',' -f9)
    METHOD_MISSED=$(echo "$DATA" | cut -d',' -f12)
    METHOD_COVERED=$(echo "$DATA" | cut -d',' -f13)

    # Calcular porcentajes
    INST_TOTAL=$((INST_MISSED + INST_COVERED))
    if [ $INST_TOTAL -gt 0 ]; then
        INST_PCT=$((INST_COVERED * 100 / INST_TOTAL))
    else
        INST_PCT=0
    fi

    LINE_TOTAL=$((LINE_MISSED + LINE_COVERED))
    if [ $LINE_TOTAL -gt 0 ]; then
        LINE_PCT=$((LINE_COVERED * 100 / LINE_TOTAL))
    else
        LINE_PCT=0
    fi

    BRANCH_TOTAL=$((BRANCH_MISSED + BRANCH_COVERED))
    if [ $BRANCH_TOTAL -gt 0 ]; then
        BRANCH_PCT=$((BRANCH_COVERED * 100 / BRANCH_TOTAL))
    else
        BRANCH_PCT=0
    fi

    METHOD_TOTAL=$((METHOD_MISSED + METHOD_COVERED))
    if [ $METHOD_TOTAL -gt 0 ]; then
        METHOD_PCT=$((METHOD_COVERED * 100 / METHOD_TOTAL))
    else
        METHOD_PCT=0
    fi

    printf "  %-20s %3d%% (%d/%d)\n" "Instructions:" $INST_PCT $INST_COVERED $INST_TOTAL
    printf "  %-20s %3d%% (%d/%d)\n" "Lines:" $LINE_PCT $LINE_COVERED $LINE_TOTAL
    printf "  %-20s %3d%% (%d/%d)\n" "Branches:" $BRANCH_PCT $BRANCH_COVERED $BRANCH_TOTAL
    printf "  %-20s %3d%% (%d/%d)\n" "Methods:" $METHOD_PCT $METHOD_COVERED $METHOD_TOTAL
fi

echo ""
echo -e "${GREEN}═══════════════════════════════════════${NC}"
echo ""
echo "Reporte HTML detallado: coverage/index.html"
