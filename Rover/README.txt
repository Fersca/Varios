How to use the Mars Rover System
--------------------------------

First add execution permision to "nasa.jar" (Which is in this directory):

- chmod 777 nasa.jar

Then you could execute the Nasa Rover system with two options:

- ./nasa.jar -remote

This command will start the remote system listening at port 5555.

You could connect to the server with the telnet command using another teminal:

- telnet localhost 5555

This is an example instruction set that you could send:

5 5
1 2 N
RRLLRRLLMM
END

The first line is the map limits.
The second line is the first location and orientation of the rover.
The third line is the instructions to move the rover.
Note that you could keep adding rovers until you send the END command.

After sending the END command you will receive the result set:

Result
------
14N
------

You can shut down the server pressing ctrl+C

You can start the Mars Rover system using an instruction file.
You should pass the instruction file as an argument in the command line:

- ./nasa.jar -file roverConfig.txt

(the roverConfig.txt file is sent as an example with the code and it's located in this directory too)

After that you will receive the following message:

13N
51E

Which indicates the position of each rover in the file.

You can find several test in the class: test.ExporeTest.java

Note:
Several assumptions have been taken during the resolution of the problem:
- Instructions must be sent in perfect format, see the examples.
- An error message is sent back to the terminal if the position of the rover is invalid or busy by another rover.

Fernando Scasserra




