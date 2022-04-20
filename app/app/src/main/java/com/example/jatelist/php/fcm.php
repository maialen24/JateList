<?php

$cabecera= array(
'Authorization: key=AAAAzS4ko6Q:APA91bGumFCGdtq7vc0ohfNDQCue1q09DnJHxl__RzoHfFzOq3Dwa8L8pEgbUvzH42glkCW1HGPWY8yv57mYhWA5LHbcMu7DamY2W7h8iWixtrMSmOvtfB_TB-Z6UP9kYhlmUQagfnD4',
'Content-Type: application/json'
);

$msg= array(
'to'=> $token,
'data' => array (
"mensaje" => "Este es mi mensaje",
"fecha" => "31/03/2020"),

'notification' => array (
'body' => 'Este es el texto de la notificación! ',
'title' => 'Título de la notificación',
'icon' => 'ic_stat_ic_notification',
),

);
$msgJSON= json_encode ( $msg);

$ch = curl_init(); #inicializar el handler de curl
#indicar el destino de la petición, el servicio FCM de google
curl_setopt( $ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
#indicar que la conexión es de tipo POST
curl_setopt( $ch, CURLOPT_POST, true );
#agregar las cabeceras
curl_setopt( $ch, CURLOPT_HTTPHEADER, $cabecera);
#Indicar que se desea recibir la respuesta a la conexión en forma de string
curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
#agregar los datos de la petición en formato JSON
curl_setopt( $ch, CURLOPT_POSTFIELDS, $msgJSON );
#ejecutar la llamada
$resultado= curl_exec( $ch );
#cerrar el handler de curl
curl_close( $ch );
if (curl_errno($ch)) {
print curl_error($ch);
}
echo $resultado;
?>