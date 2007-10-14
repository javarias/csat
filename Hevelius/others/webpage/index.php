<?php

function detectarIdioma(){

	//revisamos cabecera HTTP_ACCEPT_LANGUAGE

	$idiomas = explode(";", $_SERVER['HTTP_ACCEPT_LANGUAGE']);
	if(strpos($idiomas[0], "es") !== FALSE){
		$idioma = "es";
	}
	elseif(strpos($idiomas[0], "en") !== FALSE){
		$idioma = "en";
	}

	//Ante cualquier otro idioma devolvemos "es"
	if($idioma <> "es" && $idioma <> "en"){
		$idioma = "en";
	}
	return $idioma;
}
?>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
   <meta http-equiv="content-type" content="text/html;charset=utf-8" />
        <meta name="author" content="DevNull Web Team" />
        <meta name="description" content="Sitio Corporativo de la Empresa DevNull" />
        <meta name="keywords" content="devnull, feria de software, software, hevelius, utfsm, informatica" />
	<meta HTTP-EQUIV="Refresh" CONTENT="3; URL=<?php echo(detectarIdioma());?>/index.php">

	<title>DevNull</title>
        <link rel="stylesheet" type="text/css" href="css/style.css" media="screen" />
	<link rel="shortcut icon" href="favicon.ico" />

</head>

<body>

<!-- <div align="center">
<img src="img/main.png" /> 

</div> -->


<table width="100%" height="100%" style="border:0; background: url(img/bg.png); background-repeat:repeat;">
<tr>
<td valign="center" align="center">
<img src="img/main.png" alt="DevNull" />
<br />
<img src="img/loading_home.gif" alt="DevNull" />
</td>
</tr>
</table>


</body>
</html>
