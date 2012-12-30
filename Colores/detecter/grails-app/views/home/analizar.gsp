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
	    background: url("../../assets/piedrarosa.jpg") repeat scroll 0 0 #E8BA89;
	    color: #333333;
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
	
	pre, code {
	    background-color: #F8F8F8;
	    border: 1px solid #CCCCCC;
	    border-radius: 3px 3px 3px 3px;
	    color: #333333;
	    font-family: Monaco,"Courier New",monospace;
	    font-size: 12px;
	    margin: 10px 0;
	    padding: 7px 10px;
	}	

	</style>	
	
</head>
  <body>

	<div align="center">
	 <div id="nombrecolor" class="cs-logo2">
		Resultado del Análisis
	</div>	
	<table>
	<tr>
		<td>
			<img src="${params.foto}" width="220" height="220"> 
		</td>
		<td>
		</td>
		<td>
  	<pre>
{
	"primary":{
		"color":"<b>${primario}</b>",
		"percentage":<b>${cantPrimario}</b>
	},
	"secondary":{
		"color":"<b>${secundario}</b>",
		"percentage":<b>${cantSecundario}</b>
	},
	"background":"<b>white</b>",
	"width":<b>${ancho}</b>,
	"height":<b>${alto}</b>
}
  	</pre>		
		</td>
	</tr>
	</table> 
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
	
	<script src="../js/jquery.js"></script>
	<script src="../js/chico-0.13.1.js"></script>
  </body>
</html>