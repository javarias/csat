<?php

require("includes/class.phpmailer.php");

$mail = new PHPMailer();

$mail->IsSMTP();                                      // set mailer to use SMTP
$mail->Host = "alegre.inf.utfsm.cl";  // specify main and backup server
$mail->SMTPAuth = true;     // turn on SMTP authentication
$mail->Username = "cguajard";  // SMTP username
$mail->Password = "carlos007!"; // SMTP password

$mail->From = "$_GET[mail]";
$mail->FromName = "$_GET[nombre]";
$mail->AddAddress("cguajard@alumnos.inf.utfsm.cl");
//$mail->AddAddress("ellen@example.com");                  // name is optional
//$mail->AddReplyTo("info@example.com", "Information");

$mail->WordWrap = 50;                                 // set word wrap to 50 characters
//$mail->AddAttachment("/var/tmp/file.tar.gz");         // add attachments
//$mail->AddAttachment("/tmp/image.jpg", "new.jpg");    // optional name
$mail->IsHTML(false);                                  // set email format to HTML

$mail->Subject = "Contacto DevNull";
$mail->Body    = "".nl2br($_GET[comentario]);
$mail->AltBody = "".nl2br($_GET[comentario]);

if(!$mail->Send())
{
	echo "<p align=\"center\"><h3>No se ha podido enviar su comentario. Por favor intente m&aacute;s tarde.</h3><br />";
	//echo "<b>Mailer Error:</b> " . $mail->ErrorInfo . "</p>";
	exit;
}

echo "<p align=\"center\"><h3>Su mensaje ha sido enviado correctamente!</h3></p>";

/*
ini_set('SMTP','alegre.inf.utfsm.cl');

//Coloca aquí tu e-mail
$para = 'cguajard@alumnos.inf.utfsm.cl';

// Encabezados del correo
$headers  = "From: $_GET[nombre] <$_GET[mail]> \r\n";
$headers .= "Content-Type: text/html; charset=ISO-8859-1 ";
$headers .= "MIME-Version: 1.0 "; 

//Acá formamos el cuerpo del mensaje, con formato HTML
$msj  = "Nombre: $_GET[nombre]<br />";
$msj .= "E-mail: $_GET[mail]<br />";
$msj .= "Comentario: ".nl2br($_GET[comentario]);

// Enviamos el mail, y guardamos en la variable $enviado el resultado
$enviado = @mail($para, 'Contacto desde sitio web DevNull', $msj, $headers);

// Y dependiendo de este resultado enviamos la respuesta a nuestro objeto XMLHttpRequest
if($enviado){
echo 'Su mensaje ha sido enviado correctamente!';
}else{
echo 'No se ha podido enviar su comentario. Por favor intente m&aacute;s tarde';
}*/
?>
