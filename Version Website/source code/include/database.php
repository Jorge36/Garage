<?php //call the configuration.php in order to bring the values from the variables
include ("configuration.php");
$conexion =new mysqli($host,$username,$password,$database);
if (!$conexion) {
    die("Connection failed: " . mysqli_connect_error());
}

?>