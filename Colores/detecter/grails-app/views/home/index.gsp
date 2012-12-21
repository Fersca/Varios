<html>
<head>
</head>
<body bgcolor="lightgrey">

<center><h1>Busqueda por Colores</h1>

<table border="0">
<tr>
	<td style="background-color: black; color: black"><a href="index?color=negro">###</a></td>
	<td style="background-color: white; color: white"><a href="index?color=blanco">###</a></td>
	<td style="background-color: red; color: red"><a href="index?color=rojo">###</a></td>
	<td style="background-color: blue; color: blue"><a href="index?color=azul">###</a></td>
	<td style="background-color: green; color: green"><a href="index?color=verde">###</a></td>
	<td style="background-color: yellow; color: yellow"><a href="index?color=amarillo">###</a></td>
</tr>
<tr>
	<td style="background-color: grey; color: grey"><a href="index?color=gris">###</a></td>
	<td style="background-color: violet; color: violet"><a href="index?color=violeta">###</a></td>
	<td style="background-color: pink; color: pink"><a href="index?color=rosa">###</a></td>
	<td style="background-color: brown; color: brown"><a href="index?color=marron">###</a></td>
	<td style="background-color: orange; color: orange"><a href="index?color=naranja">###</a></td>
	<td style="background-color: skyblue; color: skyblue"><a href="index?color=celeste">###</a></td>
</tr>
</table>
</center>

<br>
<g:each in="${listado}">

	<img src="${it.imagen}" height="200" width="200">
 
</g:each>

</body>
</html>
