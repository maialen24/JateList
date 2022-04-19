<?php
$DB_SERVER="localhost"; #la dirección del servidor
$DB_USER="Xmruiz142"; #el usuario para esa base de datos
$DB_PASS="*oIgvMPY1"; #la clave para ese usuario
$DB_DATABASE="Xmruiz142_jateList"; #la base de datos a la que hay que conectarse

#Se establece la conexión:
$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);
#Comprobamos conexión
if (mysqli_connect_errno($con)) {
echo 'Error de conexion: ' . mysqli_connect_error();
exit();
}

$foto = $_POST["foto"];
$jatetxeid = $_POST["jatetxeid"];

$funcion = $_POST["funcion"];
if ($funcion=='insert'){
    inserIrudia($foto)
}else{
    getIrudia($jatetxeid)
}




function inserIrudia($foto,$jatetxeid){
    $izena=$jatetxeid+'f'
    $resultado = mysqli_query($con," INSERT into irudiak (foto,jatetxeid) VALUES ('$foto','$jatetxeid','$izena') ");

    if (!$resultado) {
        echo 'Ha ocurrido algún error: ' . mysqli_error($con);
        $result[] = array('resultado' => false);
    }else{
        $result[] = array('resultado' => true);
    }

mysqli_close($con);
echo json_encode($result)
}

function getIrudia($jatetxeid){
    $izena=$jatetxeid+'f'
    $resultado = mysqli_query($con," SELECT * FROM irudiak WHERE izena='$izena' and jatetxeid='$jatetxeid'");

     if (!$resultado) {
     echo 'Ha ocurrido algún error: ' . mysqli_error($con);
     $result[] = array('resultado' => false);
     }else{
        $fila = mysqli_fetch_row($resultado);
        $image=$fila['foto']
        $result[] = array('resultado' => true);
        $result[] = array('image' => $image);


     }

     #Devolver el resultado en formato JSON
     mysqli_close($con);
     echo json_encode($result);
}

?>