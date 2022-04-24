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
    $foto = $_POST["foto"];
    $user=$_POST["user"];
    $izena=$_POST["izena"];
    insertIrudia($foto,$user,$izena,$con);
}else{

    $user=$_POST["user"];
    $izena=$_POST["izena"];
    getIrudia($user,$izena,$con);
}




function insertIrudia($foto,$user,$izena,$con){

    mysqli_query($con," DELETE from irudiak where izena='$izena' and user='$user' ");

    $resultado = mysqli_query($con," INSERT into irudiak (izena,user,foto) VALUES ('$izena','$user','$foto') ");

    if (!$resultado) {
        echo 'Ha ocurrido algún error: ' . mysqli_error($con);
        $result[] = array('resultado' => false);
    }else{
        $result[] = array('resultado' => true);
    }

    mysqli_close($con);
    echo json_encode($result);
}

function getIrudia($user,$izena,$con){

    $resultado = mysqli_query($con," SELECT * FROM irudiak WHERE izena='$izena' and user='$user'");

     if (!$resultado) {
     echo 'Ha ocurrido algún error: ' . mysqli_error($con);
     $result[] = array('resultado' => false);
     }else{
        #$fila = mysqli_fetch_row($resultado);

            while($row = $resultado->fetch_assoc())
            {
                $imagenes= $row['foto'];
            }

            $result[] = array('resultado' => $imagenes);
     }

     #Devolver el resultado en formato JSON
     mysqli_close($con);
     echo json_encode($result);
}

?>