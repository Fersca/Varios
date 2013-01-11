<html>
<head>
	<meta name="layout" content="web">	
</head>
  <body>

	<div align="center">
	 <div id="nombrecolor" class="cs-logo2">
		Resultado del Análisis
	</div>	
	<table>
	<tr>
		<td width="40%" style="text-align: center;vertical-align: middle;">
			<img src="${params.foto}" width="220" height="220"> 
		</td>
		<td width="40%" style="text-align: left;vertical-align: middle;">
  	<pre>
{
	"global_primary":{
		"color":"<b>${primario}</b>",
		"percentage":<b>${cantPrimario}</b>
	},
	"global_secondary":{
		"color":"<b>${secundario}</b>",
		"percentage":<b>${cantSecundario}</b>
	},
	"background":"<b>${borde}</b>",
	"product":"<b>${produ}</b>",
	"width":<b>${ancho}</b>,
	"height":<b>${alto}</b>
}
  	</pre>		
	</td>
	</tr>
	</table> 
	<hr>
			<form method="POST" action="/detecter/home/analizar/1">
			<p class="ch-form-row">
				<input name="foto" type="text" size="50" value="">
				<input id="analizar" class="ch-btn-skin ch-btn-small" name="send" type="submit" value="Analizar">
			</p>
		</form>	
	<hr>
	<div id="nombrecolor" class="cs-logo2">
		¿Desea utilizar la API para su sitio?
	</div>	
	
  	La API de detección de imágenes proporciona:
  	<br><br><br>

  	Detección de color central<br>
  	Detección de color de fondo<br>
  	Detección de colores principales<br>
  	Detección de tamaño<br>
  	Próximamente detección de objetos<br>

  	<br><br>
  	Si desea utilizar la API de detección de imágenes puede contactarme a través de Linkedin o Twitter.
  	<br><br>
  	<div align="center">
  	<a href="http://www.linkedin.com/in/fersca" target="_blanck"><img src="http://s.c.lnkd.licdn.com/scds/common/u/img/logos/logo_linkedin_92x22.png"></a>
  	<a href="http://www.twitter.com/fersca" target="_blanck"><img src="https://twitter.com/images/resources/twitter-bird-white-on-blue.png" width="30" height="30"></a>
  	</div>	
	
	</div>
	<script src="${resource(dir:'/js',file:'jquery.js')}"></script>
	<script src="${resource(dir:'/js',file:'chico-0.13.1.js')}"></script>
  </body>
</html>