<?php


function news(){
//le decimos la carpeta
$path = "content/news";

//abrimos la carpeta
$dir = opendir($path);

//Mostramos los archivos
$i = 0;
while ($elemento = readdir($dir))
{
	if(is_numeric($elemento)){
		//echo $elemento."<br>";
		$news[$i] = $elemento;
		$i = $i+1;
		//echo($elemento."<br />");
	}
}
sort($news);
//Cerramos la carpeta
closedir($dir);
?>

<h2 style="margin-top:10px">Noticias</h2>
<ul>
<?php

for ($t = count($news)-1; $t >= count($news)-2; $t=$t-1)
{
	$lineas = file('content/news/'.$news[$t]);

	for($i = 0; $i < count($lineas) ; $i++){
		$lineas[$i] = ereg_replace("á","&aacute;",$lineas[$i]);
		$lineas[$i] = ereg_replace("é","&eacute;",$lineas[$i]);
		$lineas[$i] = ereg_replace("í","&iacute;",$lineas[$i]);
		$lineas[$i] = ereg_replace("ó","&oacute;",$lineas[$i]);
		$lineas[$i] = ereg_replace("ú","&uacute;",$lineas[$i]);
		$lineas[$i] = ereg_replace("ñ","&ntilde;",$lineas[$i]);
	}


	//echo("<li><a href=\"javascript:void(null);\" onclick=\"cargar('noticias.php?id=".$news[$t]."');\">".trim($lineas[0])."</a> <i>(".trim($lineas[1]).")</i>");
	echo("<li>".trim($lineas[0])." <a href=\"javascript:void(null);\" onclick=\"cargar('noticias.php?id=".$news[$t]."');\">[+]</a><br /><i>".trim($lineas[1])."</i></li>");
}
?>
</ul>

<?php
}
?>
