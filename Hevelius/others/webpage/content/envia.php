<?php 
//Coloca aquí tu e-mail
$para = 'devnull@listas.bla.cl';

// Encabezados del correo
$headers  = "From: $_GET[nombre] <$_GET[mail]> \r\n";
$headers .= "Content-Type: text/html; charset=ISO-8859-1 ";
$headers .= "MIME-Version: 1.0 "; 

//Acá formamos el cuerpo del mensaje, con formato HTML
$msj  = "Nombre: $_GET[nombre]<br />";
$msj .= "E-mail: $_GET[mail]<br />";
$msj .= "Comentario: ".nl2br($_GET[comentario]);

// Enviamos el mail, y guardamos en la variable $enviado el resultado
$enviado = mail($para, 'Contacto desde sitio web', $msj, $headers);

// Y dependiendo de este resultado enviamos la respuesta a nuestro objeto XMLHttpRequest
if($enviado){
echo 'Su mensaje ha sido enviado correctamente!';
}else{
echo 'No se ha podido enviar su comentario. Por favor intente más tarde';
}
?>
