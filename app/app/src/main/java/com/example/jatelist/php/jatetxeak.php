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

$funcion = $_POST["funcion"];
if ($funcion=='insert'){
    insertJatetxea($_POST["name"],$_POST["ubi"],$_POST["valoracion"],$_POST["comentarios"],$_POST["tlf"],$_POST["user"]);
}else if($funcion=='update'){
    updateJatetxea($_POST["name"],$_POST["ubi"],$_POST["valoracion"],$_POST["comentarios"],$_POST["tlf"],$_POST["user"]);
}else if($funcion=='delete'){
     deleteJatetxea($_POST["user"],$_POST["name"]);
 }else if($funcion=='get'){
      getAllfromUser($_POST["user"]);
  }else if($funcion=='getAll'){
         getAll();
  }

function insertJatetxea($name, $ubi, $valoracion, $comentarios, $tlf, $user){

    $resultado = mysqli_query($con," INSERT into jatetxea (izena,ubicacion,valoracion,comentarios,tlf,user) VALUES ('$name','$ubi','$valoracion','$comentarios','$tlf','$user') ");

    if (!$resultado) {
        echo 'Ha ocurrido algún error: ' . mysqli_error($con);
        $result[] = array('resultado' => false);
    }else{
        $result[] = array('resultado' => true);
    }

mysqli_close($con);
echo json_encode($result)

}

function deleteJatetxea($name,$user){

    $resultado = mysqli_query($con," DELETE FROM jatetxea where izena='$name' and user='$user' ");

    if (!$resultado) {
        echo 'Ha ocurrido algún error: ' . mysqli_error($con);
        $result[] = array('resultado' => false);
    }else{
        $result[] = array('resultado' => true);
    }

mysqli_close($con);
echo json_encode($result)
}

function updateJatetxea($name, $ubi, $valoracion, $comentarios, $tlf, $user){

    $resultado = mysqli_query($con," UPDATE jatetxea SET ubi='$ubi',valoracion='$valoracion',comentarios='$comentarios',tlf='$tlf' where user='$user' and izena='$name' ");

    if (!$resultado) {
        echo 'Ha ocurrido algún error: ' . mysqli_error($con);
        $result[] = array('resultado' => false);
    }else{
        $result[] = array('resultado' => true);
    }

mysqli_close($con);
echo json_encode($result)

}

function getAllfromUser($user){

    $resultado = mysqli_query($con," SELECT * FROM jatetxea WHERE user='$user' ");

     if (!$resultado) {
     echo 'Ha ocurrido algún error: ' . mysqli_error($con);
     $arrayresultados[] = array('resultado' => false);
     }else{
     while($fila=mysqli_fetch_row($resultado)){
         //$fila = mysqli_fetch_row($resultado);
         $arrayresultados[$fila] = array(
         'izena' => $fila[1],
         'ubicacion' => $fila[2],
         'valoracion' => $fila[3],
         'comentarios' => $fila[4],
         'tlf' => $fila[5],
         'user' => $fila[6],
         );
     }

     }

     #Devolver el resultado en formato JSON
     mysqli_close($con);
     echo json_encode($arrayresultados);
}

function getAll(){
    $resultado = mysqli_query($con," SELECT * FROM jatetxea");

     if (!$resultado) {
     echo 'Ha ocurrido algún error: ' . mysqli_error($con);
     $arrayresultados[] = array('resultado' => false);
     }else{
     while($fila=mysqli_fetch_row($resultado)){
         //$fila = mysqli_fetch_row($resultado);
         $arrayresultados[$fila] = array(
         'izena' => $fila[1],
         'ubicacion' => $fila[2],
         'valoracion' => $fila[3],
         'comentarios' => $fila[4],
         'tlf' => $fila[5],
         'user' => $fila[6],
         );
     }

     }

     #Devolver el resultado en formato JSON
     mysqli_close($con);
     echo json_encode($arrayresultados);
}
?>