<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<!-- ==========================================================	-->
<!--	Created by Devit Schizoper                          	-->
<!--	Created HomePages http://LoadFoo.starzonewebhost.com   	-->
<!--	Created Day 01.12.2006                              	-->
<!-- ========================================================== -->

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
   <meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<meta name="author" content="DevNull Web Team" />
	<meta name="description" content="Sitio Corporativo de la Empresa DevNull" />
	<meta name="keywords" content="devnull, feria de software, software, hevelius, utfsm, informatica" />
	<title>DevNull</title>
	<link rel="stylesheet" type="text/css" href="../css/style.css" media="screen" />
	<link rel="shortcut icon" href="../favicon.ico" />
	<script type="text/javascript" src="../js/textsizer.js"></script>
	<script type="text/javascript" src="../js/rel.js"></script>
	<script type="text/javascript" src="../js/ajax.js"></script>
	<script language="JavaScript">
	function cambiar_idioma(){
		document.form1.submit();
	}	
	</script>
</head>

<?php
if(isset($_POST['page']))
        $pagina = $_POST['page'];
else
        $pagina = "home";
?>

<body onload="cargar('<?php echo($pagina); ?>');">
<div id="wrap">
<div id="top">
<h2><a href="javascript:void(null);" onclick="cargar('home');" title="inicio" ><img src="../img/main.png" width="170px" height="35px" style="border-width:0;" /></a></h2>
<div id="menu">
<ul>
<!-- <li><a href="#" class="current">home</a></li> -->
<li><a href="javascript:void(null);" onclick="cargar('home');">inicio</a></li>
<li><a href="javascript:void(null);" onclick="cargar('empresa');">empresa</a></li>
<li><a href="javascript:void(null);" onclick="cargar('about');">quienes somos</a></li>
<li><a href="javascript:void(null);" onclick="cargar('producto');">producto</a></li>
<li><a href="javascript:void(null);" onclick="cargar('cliente');">cliente</a></li>
<li><a href="javascript:void(null);" onclick="cargar('contacto');">contacto</a></li>
</ul>
</div>
</div>
<div id="content">
<form target ="" name="form1" method="post" action="../en/index.php">
<div style="float: right;">espa&ntilde;ol | <a href="javascript:void(null);" onclick="cambiar_idioma();" >english</a></div>
<div id="left">

</div>
</form>
<div id="right">

<div class="box">
<!--<h2 style="margin-top:10px">Noticias</h2>
<ul>
<li>Segundo Hito Presentado.<br/><i>23 Ago 07</i></li>
<li>Web Finalizada.<br /><i>17 Ago 07</i></li>
</ul>-->
<?php
include_once("content/includes/noticias.php");
news();
?>
<a href="javascript:void(null);" onclick="cargar('noticias');">ver m&aacute;s</a>
</div>

<ul id="nav">
<table width="100%" border="0">
<tr height="50"><td align="center" valign="center">
<li><a href="http://www.feriadesoftware.cl" target="externo" ><img src="../img/logo_feria.gif" border="0" alt="FS" /><!-- Feria de Software--></a></li>
</td></tr>
<tr height="50"><td align="center" valign="center">
<li><a href="http://acs.inf.utfsm.cl" target="externo" ><img src="../img/logo_acs.gif" size="50%" border="0" alt="ACS" /><!-- ACS-UTFSM Group--></a></li>
</td></tr>
<tr height="50"><td align="center" valign="center">
<li><a href="http://www.inf.utfsm.cl" target="externo" ><img src="../img/logo_inf.gif" border="0" alt="ACS" /><!-- DI-UTFSM--></a></li>
</td></tr>
<tr height="50"><td align="center" valign="center">
<li><a href="http://www.utfsm.cl" target="externo" ><img src="../img/logo_utfsm.gif" border="0" alt="USM" /><!-- UTFSM--></a></li>
</td></tr>
<!--<li><a href="http://auspicio.com">Auspicio</a></li>-->
<!--<li><a href="#contact">Contact</a></li> -->
</table>
</ul>
<!-- <div class="box">
<h2 style="margin-top:17px">Recent Entries</h2>
<ul>
<li><a href="#">Recent Entries1</a> <i>01 Des 06</i></li>
<li><a href="#">Recent Entries2</a> <i>01 Des 06</i></li>
<li><a href="#">Recent Entries3</a> <i>01 Des 06</i></li>
<li><a href="#">Recent Entries4</a> <i>01 Des 06</i></li>
<li><a href="#">Recent Entries5</a> <i>01 Des 06</i></li>
</ul>
</div> -->
</div>
<div id="clear"></div></div>
<div id="footer">
<p>Derechos Reservados 2007 DevNull. Dise&ntilde;ado por <a href="http://loadfoo.org/" rel="external">LoadFoO</a>.</p>
</div>
</div>

</body>
</html>
