<!doctype html>
<!--[if IE 7]>    <html class="no-js lt-ie10 lt-ie9 lt-ie8 ie7" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie10 lt-ie9 ie8" lang="en"> <![endif]-->
<!--[if IE 9]>    <html class="no-js lt-ie10 ie9" lang="en"> <![endif]-->
<!--[if gt IE 9]><!--> <html class="no-js" lang="en"> <!--<![endif]-->
<head>
	<!-- Avoid script blocking -->
	<script></script>
	<meta charset="utf-8">
	<title>Ropa de Colores</title>
 
	<!-- Chico UI Core stylesheet -->
	<link rel="stylesheet" href="../css/chico-0.13.1.css">
	
	<style type="text/css">
	
	body {
	    background: url("../assets/piedrarosa.jpg") repeat scroll 0 0 #E8BA89;
	    color: #524D4D;
	    font-family: "Helvetica Neue",Helvetica,Arial,"MS Trebuchet",sans-serif;
	    font-size: 14px;
	    line-height: 1.231em;
	    max-width: none;
	    min-width: 0;
	    padding: 0;
	    background-size: 100%;
	}	
	
	#colorchart td {
	    height: 40px;
	    width: 40px;
	}
	
	#colorchart {
	    border-collapse: collapse;
	}
	
	h2,h1,h3 {
	color: white;
	}
	
	.cs-logo {
	    font-size: 10em;
	    margin-bottom: 0.2em;
	}
	.cs-logo {
	    color: #AD335C;
	    font-family: "Lobster Two",cursive;
	    font-size: 4em;
	    font-style: italic;
	    font-weight: bold;
	    line-height: 1.5em;
	    text-align: center;
	    text-shadow: 0 -2px #520029, 0 1px rgba(255, 255, 255, 0.2);
	}	
	
	.cs-logo2 {
	    color: #FFFFFF;
	    font-family: "Lobster Two",cursive;
	    font-size: 2em;
	    font-style: italic;
	    font-weight: bold;
	    line-height: 1.5em;
	    text-align: center;
	    text-shadow: 0 -2px #003300, 0 1px rgba(255, 255, 255, 0.2);
	}		
	
	.cs-btn-container {
	    background: none repeat scroll 0 0 rgba(0, 0, 0, 0.3);
	    border-radius: 6px 6px 6px 6px;
	    box-shadow: 0 1px rgba(255, 255, 255, 0.2), 0 0 6px rgba(0, 0, 0, 0.08) inset;
	    display: inline-block;
	    text-align: center;
	}
	
	@font-face {
	    font-family: "Lobster Two";
	    font-style: italic;
	    font-weight: 400;
	    src: local("Lobster Two Italic"), local("LobsterTwo-Italic"), url("https://themes.googleusercontent.com/static/fonts/lobstertwo/v4/Ul_16MSbfayQv1I4QhLEoNkZXW4sYc4BjuAIFc1SXII.woff") format("woff");
	}

	</style>	
	
</head>
<body>

	<div id="fb-root"></div>
	<script>(function(d, s, id) {
	  var js, fjs = d.getElementsByTagName(s)[0];
	  if (d.getElementById(id)) {return;}
	  js = d.createElement(s); js.id = id;
	  js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=169747283118913";
	  fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));</script>

	<div align="center">	

			<h1 class="cs-logo">Ropa de Colores</h1>
			<!-- <h2>Elija el color de su ropa en <a href="http://www.mercadolibre.com.ar" target="_blank">MercadoLibre</a></h2> -->
			<table id="colorchart">
			<tbody>
			<tr>
				<td style="background-color: black; color: black" onclick="cambiarColor('negro');" onmouseover="cambiarNombreColor('negro');"></td>
				<td style="background-color: white; color: white" onclick="cambiarColor('blanco');" onmouseover="cambiarNombreColor('blanco');"></td>
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
		<g:each in="${listado}">
			<li>
					<div class="cs-btn-container">
					<br>
					<a href="http://www.mercadolibre.com.ar/jm/item?site=MLA&id=${it.itemId.substring(3)}" target="_blank">
					- <img class="imagen" src="${it.imagen}" height="100" width="100" title="${it.titulo} - Precio: ${it.precio}"> -
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
		<g:each in="${listado}">
			<li>
					<div class="cs-btn-container">
					<br>
					<a href="http://www.mercadolibre.com.ar/jm/item?site=MLA&id=${it.itemId.substring(3)}" target="_blank">
					- <img class="imagen" src="${it.imagen}" height="100" width="100" title="${it.titulo} - Precio: ${it.precio}"> -
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
		<g:each in="${listado}">
			<li>
					<div class="cs-btn-container">
					<br>
					<a href="http://www.mercadolibre.com.ar/jm/item?site=MLA&id=${it.itemId.substring(3)}" target="_blank">
					- <img class="imagen" src="${it.imagen}" height="100" width="100" title="${it.titulo} - Precio: ${it.precio}"> -
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
 	<div class="fb-like" data-href="http://xchange.vg" data-send="false" data-width="200" data-show-faces="true"></div>
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
				<input name="foto" type="text" size="50" value="http://www.montagneoutdoors.com.ar/1102-2768-thickbox/remera-de-ninos-exploring.jpg">
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
	<script src="../js/jquery.js"></script>
	<script src="../js/chico-0.13.1.js"></script>
	<script>

	var foo1 = $(".examplecarru").carousel();
	var foo2 = $(".imagen").tooltip();
	var foo3 = $("#contactar").modal({
		width: "650px",
		height: "580px"
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