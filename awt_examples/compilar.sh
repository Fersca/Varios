echo "Borra datos anteriores..."
rm -rf ./Compilado
mkdir ./Compilado
mkdir ./Compilado/classes
mkdir ./Compilado/jar
echo "Compilando clases..."
javac -d ./Compilado/classes/ Zorrito.java
echo "Creando Jar..."
jar --create --file ./Compilado/jar/Zorrito.jar --manifest MANIFEST.MF -C ./Compilado/classes/ .
echo "Compilación Completa"
