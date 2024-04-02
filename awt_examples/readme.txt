COMPILAR A NATIVO
------------------

Para compilar nativo hay que ejecutar:
native-image -jar Zorrito.jar

El tema es que eso genera un compilado que al ejecutarlo dice que no puede acceder a los archivos de awt.

Parece ser que hay que indicarle cuáles son los archivos nativos de awt, para eso encontré este blog que dice que 
se puede ejecutar el programa con un parámetro que te genera un directorio con unos archivos en donde se pueden 
ver las dependencias se se usa en la app. 

 https://www.praj.in/posts/2021/compiling-swing-apps-ahead-of-time/

Para eso hay que correr el programa con una cabecera, de esta forma:

java -agentlib:native-image-agent=config-output-dir=config -jar Zorrito.jar

De esa forma se corre el jar, pero se le indica que deje en el directorio confif los archivos.

Luego de hacer eso al parecer hay que compilar de nuevo a nativo con esa info, pasándole ese directorio al 
compilador

native-image -jar Zorrito.jar -H:ConfigurationFileDirectories=./config

El tema es que luego de hacer esto, igualmente no termina de funcionar, aparece el mismo problema.

