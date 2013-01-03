<html>
<head>
	<meta name="layout" content="web">	
</head>
<body>

	<div id="fb-root"></div>
	<script>(function(d, s, id) {
	  var js, fjs = d.getElementsByTagName(s)[0];
	  if (d.getElementById(id)) return;
	  js = d.createElement(s); js.id = id;
	  js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=530295160319582";
	  fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));</script>

	<div align="center">	

			<h1 class="cs-logo">Ropa de Colores</h1>
			<h2>Elija el color de su ropa en <a href="http://www.mercadolibre.com.ar" target="_blank">MercadoLibre</a></h2>
			<table id="colorchart">
			<tbody>
			<tr>
				<td style="background-color: black; color: black" onclick="cambiarColor('negro');" onmouseover="cambiarNombreColor('negro');"></td>
				<!-- <td style="background-color: white; color: white" onclick="cambiarColor('blanco');" onmouseover="cambiarNombreColor('blanco');"></td> -->
				<td style="background-color: red; color: red" onclick="cambiarColor('rojo');" onmouseover="cambiarNombreColor('rojo');"></td>
				<td style="background-color: blue; color: blue" onclick="cambiarColor('azul');" onmouseover="cambiarNombreColor('azul');"></td>	
				<td style="background-color: green; color: green" onclick="cambiarColor('verde');" onmouseover="cambiarNombreColor('verde');"></td>
				<td style="background-color: yellow; color: yellow" onclick="cambiarColor('amarillo');" onmouseover="cambiarNombreColor('amarillo');"></td>
				<td style="background-color: grey; color: grey" onclick="cambiarColor('gris');" onmouseover="cambiarNombreColor('gris');"></td>
				<td style="background-color: violet; color: violet" onclick="cambiarColor('violeta');" onmouseover="cambiarNombreColor('violeta');"></td>
				<td style="background-color: pink; color: pink" onclick="cambiarColor('rosa');" onmouseover="cambiarNombreColor('rosa');"></td>
				<td style="background-color: brown; color: brown" onclick="cambiarColor('marron');" onmouseover="cambiarNombreColor('marron');"></td>
				<td style="background-color: orange; color: orange" onclick="cambiarColor('naranja');" onmouseover="cambiarNombreColor('naranja');"></td>
				<td style="background-color: skyblue; color: skyblue" onclick="cambiarColor('celeste');" onmouseover="cambiarNombreColor('celeste');"></td>
			</tr>
			</tbody>
			</table>

			<div id="nombrecolor" class="cs-logo2">
			${params.color.toUpperCase()}
			</div>

	</div>

	<br>
	<div id="nombrecolor" class="cs-logo2">
	Remeras
	</div>
	<hr>
	<div class="examplecarru ch-carousel">
		<ul>
		<g:each in="${remeras}">
			<li>
					<div class="cs-btn-container">
					<br>
					<a href="http://www.mercadolibre.com.ar/jm/item?site=MLA&id=${it.itemId.substring(3)}" target="_blank">
					- <img class="imagen" src="${it.imagen}" height="150" width="150" title="${it.titulo} - Precio: ${it.precio}"> -
					<br>
					-
					</a>
					</div>					
			</li>
		</g:each>
		</ul>
	</div>
	<div id="nombrecolor" class="cs-logo2">
	Carteras
	</div>		
	<hr>
	<div class="examplecarru ch-carousel">
		<ul>
		<g:each in="${carteras}">
			<li>
					<div class="cs-btn-container">
					<br>
					<a href="http://www.mercadolibre.com.ar/jm/item?site=MLA&id=${it.itemId.substring(3)}" target="_blank">
					- <img class="imagen" src="${it.imagen}" height="150" width="150" title="${it.titulo} - Precio: ${it.precio}"> -
					<br>
					-
					</a>
					</div>
			</li>
		</g:each>
		</ul>
	</div>
	<div id="nombrecolor" class="cs-logo2">
	Zapatos
	</div>		
	<hr>
	<div class="examplecarru ch-carousel">
		<ul>
		<g:each in="${zapatos}">
			<li>
					<div class="cs-btn-container">
					<br>
					<a href="http://www.mercadolibre.com.ar/jm/item?site=MLA&id=${it.itemId.substring(3)}" target="_blank">
					- <img class="imagen" src="${it.imagen}" height="150" width="150" title="${it.titulo} - Precio: ${it.precio}"> -
					<br>
					-
					</a>
					</div>
			</li>
		</g:each>
		</ul>
	</div>		
	<div id="nombrecolor" class="cs-logo2">
	</div>		
	<hr>	
	<br>
 	<div align="center">
 	<div class="fb-like" data-href="http://www.ropadecolores.com" data-send="false" data-width="200" data-show-faces="true"></div>
 	<br><br><br><br>
 	
 	<div>
 	<table>
 	<tr>
 	<td  width="40%" style="text-align: center;vertical-align: middle;">
	 	<div id="nombrecolor" class="cs-logo2">
		Pruebe nuestra API para analizar imágenes
		</div>	
 	</td>
 	<td  width="40%" style="text-align: center;vertical-align: middle;">
 		<div id="nombrecolor" class="cs-logo2">
		¿Desea utilizar la API para su sitio?
		</div>	
 	</td>
 	</tr>
 	<tr>
 	<td width="20%">
 	</td>
 	<td width="20%">
 	</td> 	
 	</tr>
 	<tr>
 	<td  width="40%" style="text-align: center;vertical-align: middle;">
		<form method="POST" action="/detecter/home/analizar/1">
			<p class="ch-form-row">
				<input name="foto" type="text" size="50" value="http://bimg1.mlstatic.com/remeras-abercrombie-fitch-temporada-2013-100-algodon_MLA-F-3559997388_122012.jpg">
				<input id="analizar" class="ch-btn-skin ch-btn-small" name="send" type="submit" value="Analizar">
			</p>
		</form>		
		
 	</td>
 	<td  width="40%" style="text-align: center;vertical-align: middle;">
		<form action="/detecter/home/contact/1">										
			<input id="contactar" class="ch-btn-skin ch-btn-small" type="submit" value="Contactar" style="cursor: pointer;"/>								
		</form>	 		
 	</td>
 	</tr>
 	</table>
 	</div>
						
	</div>
	<script src="${resource(dir:'/js',file:'jquery.js')}"></script>
	<script src="${resource(dir:'/js',file:'chico-0.13.1.js')}"></script>
	<script>

	var foo1 = $(".examplecarru").carousel();
	var foo2 = $(".imagen").tooltip();
	var foo3 = $("#contactar").modal({
		width: "650px",
		height: "600px"
	});
	
	cambiarNombreColor('${params.color.toLowerCase()}');
	
	function cambiarColor(color) {
		window.location.href = "index?color="+color;
	}
	
	function cambiarNombreColor(color) {
		var o = document.getElementById("nombrecolor");
		o.innerHTML=color.toUpperCase();
		if (color=='rojo')
			o.style.color="red";
		else if (color=="blanco")
			o.style.color="white";
		else if (color=="negro")
			o.style.color="black";
		else if (color=="verde")
			o.style.color="green";
		else if (color=="amarillo")
			o.style.color="yellow";
		else if (color=="azul")
			o.style.color="blue";
		else if (color=="gris")
			o.style.color="grey";
		else if (color=="marron")
			o.style.color="brown";
		else if (color=="violeta")
			o.style.color="violet";
		else if (color=="rosa")
			o.style.color="pink";
		else if (color=="naranja")
			o.style.color="orange";
		else if (color=="celeste")
			o.style.color="skyblue";
		
	}	
	</script> 
</body>
</html>