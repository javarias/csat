// JavaScript Document

function cargar(valor) {
	//	Usuario inteligente...
	var consulta="content/"+valor+".php";
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
		req.onreadystatechange = process;
		req.open("GET", consulta, true);
		req.send(null);
		//	...y usuario de Internet Explorer Windows
	} else if (window.ActiveXObject) {
		isIE = true;
		req = new ActiveXObject("Microsoft.XMLHTTP");
		if (req) {
			req.onreadystatechange = process;
			req.open("GET", consulta, true);
			req.send();
		}
	}
}

function process(){
	var detalles = document.getElementById('left');
	if(req.readyState == 4){
		detalles.innerHTML = req.responseText;
	} else {
		detalles.innerHTML = '<p class="body_text" align="center"><img src="../img/loading.gif" alt="..." /> Loading ...</p>';
	}
}








var ajax=false;

function iniciaAjax(){
	try {
		ajax = new ActiveXObject("Msxml2.XMLHTTP");
	} catch (e) {
		try {
			ajax = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (E) {
			try{
				ajax = new XMLHttpRequest();
			} catch(E2){
				ajax = false;
			}
		}
	}
	if (!ajax && typeof XMLHttpRequest!='undefined') {
		ajax = new XMLHttpRequest();
	}
}


function enviar(){

	if(ValidarCorreo() != false && ValidarNombre() != false && ValidarMensaje() != false){

		iniciaAjax();
		var nombre = document.getElementById('nombre').value;
		var mail = document.getElementById('mail').value;
		var com = document.getElementById('comentario').value;

		var query = 'content/envia.php?';
		query += 'nombre='+nombre;
		query += '&mail='+mail;
		query += '&comentario='+com;
		
		//alert(query);

		ajax.open('GET', query, true);
		ajax.onreadystatechange = procesa;
		ajax.send(null);
		//cargar("home");
	}

}


function procesa(){
	var detalles = document.getElementById('left');
	if(ajax.readyState==4){
		if(ajax.status == 200){
			//	alert(ajax.responseText);
			//cargar("enviado");
			detalles.innerHTML = ajax.responseText;
		}
	} else {
		detalles.innerHTML = '<p class="body_text" align="center"><img src="../img/loading.gif" alt="..." />Sending E-Mail...</p>';
	}

}


function ValidarCorreo(){
	var Email = document.getElementById('mail').value;
	var Formato = /^([\w-\.])+@([\w-]+\.)+([a-z]){2,4}$/;
	var Comparacion = Formato.test(Email);
	if(Comparacion == false){
		alert("Invalid E-Mail");
		return false;
	}
	else{
		return true;
	}
}

function ValidarNombre(){
	var Nombre = document.getElementById('nombre').value;
	var Formato = /^([\w-\.])+/;
	var Comparacion = Formato.test(Nombre);
	if(Comparacion == false){
		alert("No Name");
		return false;
	}
	else{
		return true;
	}
}

function ValidarMensaje(){
	var Mensaje = document.getElementById('comentario').value;
	var Formato = /^([\w-\.])+/;
	var Comparacion = Formato.test(Mensaje);
	if(Comparacion == false){
		alert("Empty Message");
		return false;
	}
	else{
		return true;
	}
}


