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
if ($funcion=='insertUser'){
    insertUser($_POST["user"],$_POST["password"])
}else{
    checkCredentials($_POST["user"],$_POST["password"])
}

//insert new user into db
function insertUser($user,$password) {
    $hash=password_hash($password, PASSWORD_DEFAULT)
    $resultado = mysqli_query($con," INSERT into users (user,password) VALUES ('$user','$hash') ");

    if (!$resultado) {
        echo 'Ha ocurrido algún error: ' . mysqli_error($con);
        $result[] = array('resultado' => false);
    }else{
        $result[] = array('resultado' => true);
    }

mysqli_close($con);
echo json_encode($result)
}


 //check login user credentials
function checkCredentials($user, $password) {

     $resultado = mysqli_query($con," SELECT password FROM users WHERE user='$user'");

     if (!$resultado) {
     echo 'Ha ocurrido algún error: ' . mysqli_error($con);
     $result[] = array('resultado' => false);
     }else{
        $fila = mysqli_fetch_row($resultado);
        $hash=$fila[1]
         if(password_verify($password,$hash)){
             $result[] = array('resultado' => true);
         }
         else{
             $result[] = array('resultado' => false);
         }
     }

     #Devolver el resultado en formato JSON
     mysqli_close($con);
     echo json_encode($result);

}



?>