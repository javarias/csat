<?php

//le decimos la carpeta
$path = "news";

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
	}
}
sort($news);
//Cerramos la carpeta
closedir($dir);

?>

<?php

if($_GET['id']<=0){

?>


<h2>Noticias</h2><br />
<!--<span class="subtitle_gray"></span><br />-->
<br />
<!-- <p class="body_text" align="justify"> -->
<?php //echo(count($news));?>
<p align="justify">
<ul>
<?php //<li>
//<li>

for ($t = count($news)-1; $t>=0; $t=$t-1)
{
	$lineas = file('news/'.$news[$t]);

	for($i = 0; $i < count($lineas) ; $i++){
		$lineas[$i] = ereg_replace("á","&aacute;",$lineas[$i]);
		$lineas[$i] = ereg_replace("é","&eacute;",$lineas[$i]);
		$lineas[$i] = ereg_replace("í","&iacute;",$lineas[$i]);
		$lineas[$i] = ereg_replace("ó","&oacute;",$lineas[$i]);
		$lineas[$i] = ereg_replace("ú","&uacute;",$lineas[$i]);
		$lineas[$i] = ereg_replace("ñ","&ntilde;",$lineas[$i]);
	}


	echo("<li><a href=\"javascript:void(null);\" onclick=\"cargar('noticias.php?id=".$news[$t]."');\">".trim($lineas[0])."</a> <i>(".trim($lineas[1]).")</i>");
	//echo("<li><a href=\"javascript:void(null);\" onclick=\"cargar('home');\">".$lineas[0]." <i>(".trim($lineas[1]).")</i></a>");
}


?>
</ul>
</p>

<input type="hidden" name="page" value="noticias">
<?php
}
else{

//echo($_GET['id']."<br />");
$_GET['id'] = ereg_replace(".php","",$_GET['id']);
//echo($_GET['id']."<br />");

$lineas = file('news/'.$_GET['id']);
for($i = 0; $i < count($lineas) ; $i++){
	$lineas[$i] = ereg_replace("á","&aacute;",$lineas[$i]);
	$lineas[$i] = ereg_replace("é","&eacute;",$lineas[$i]);
	$lineas[$i] = ereg_replace("í","&iacute;",$lineas[$i]);
	$lineas[$i] = ereg_replace("ó","&oacute;",$lineas[$i]);
	$lineas[$i] = ereg_replace("ú","&uacute;",$lineas[$i]);
	$lineas[$i] = ereg_replace("ñ","&ntilde;",$lineas[$i]);
}
?>
	<h2><?php echo($lineas[0]); ?></h2> <i><?php echo(trim($lineas[1])); ?></i><br />
<!--<span class="subtitle_gray"></span><br />-->
<br />
<!-- <p class="body_text" align="justify"> -->
<p align="justify">
<?php
for ($t = 2; $t<count($lineas); $t=$t+1)
{
	echo($lineas[$t]."<br />");
}


?>
</p>
<div align="center"><br /><a href="javascript:void(null);" onclick="cargar('noticias');">Volver a Noticias</a></div>
<input type="hidden" name="page" value="noticias.php?id=<?php echo($_GET['id']); ?>">

<?php
}
?>
