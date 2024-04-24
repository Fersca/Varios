#!/bin/bash

# Compile the plain Java tests
echo "Compiling ZorritoTest.java..."
javac ZorritoTest.java
if [ $? -ne 0 ]; then
    echo "Compilation of ZorritoTest.java failed."
    exit 1
fi
echo "Compilation successful."

# Run the plain Java tests
echo "Running plain Java tests..."
java ZorritoTest
if [ $? -ne 0 ]; then
    echo "Plain Java tests failed."
    exit 1
fi
echo "Plain Java tests completed successfully."

# Compile the JUnit tests
echo "Compiling ZorritoJUnitTest.java with JUnit..."
javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar ZorritoJUnitTest.java
if [ $? -ne 0 ]; then
    echo "Compilation of ZorritoJUnitTest.java failed."
    exit 1
fi
echo "Compilation successful."

# Run the JUnit tests
echo "Running JUnit tests..."
java -Djava.awt.headless=true -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore ZorritoJUnitTest
if [ $? -ne 0 ]; then
    echo "JUnit tests failed."
    exit 1
fi
echo "JUnit tests completed successfully."

# If all tests pass, exit with code 0
exit 0
