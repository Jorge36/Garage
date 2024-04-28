<?php

session_start();

include 'include/database.php';


// get the date parameter from URL
$date = $_GET["date"];

// get status
$sqlStatus = "SELECT * FROM status";

// get type of service
$sqlTypeApp = "SELECT * FROM type_app";

$date_aux1 = str_replace('/', '-', $date);

$date_aux2 = date_create($date_aux1, new DateTimeZone('utc'));

$date_sql = date_format($date_aux2, 'Y-m-d');

// get appoinments for a date and their shifts
$sql = "SELECT appoinment.id, appoinment.user_email, appoinment.type_app_id,  
               appoinment_has_shift.shift_id, appoinment.status_id
        FROM appoinment INNER JOIN appoinment_has_shift
        ON  appoinment.id = appoinment_has_shift.appoinment_id
        AND appoinment.user_email = appoinment_has_shift.appoinment_user_email
        WHERE appoinment.date = '$date_sql'";

$result = mysqli_query($conexion, $sql);

if (mysqli_num_rows($result) > 0) { // if there is no appointments for DATE, insert in database

    $ismayorrepair = false; 
    // build table
    while($row = mysqli_fetch_assoc($result)) { 

        if ($ismayorrepair) { // if the current row is a mayor repair, i will only use the first row

            $ismayorrepair = false; 
            
            continue;
        }
        // id appointment
        echo "<tr><th scope='row'>" . 
        $row["id"]  . "</th>";

        // type of appointmnet
        $resultTypeApp = mysqli_query($conexion, $sqlTypeApp);

        while($row2 = mysqli_fetch_assoc($resultTypeApp)) {

          if ($row2["id"] == $row["type_app_id"]) {

              echo "<td>" . $row2["type"] . "</td>";
              break;
          }
        
        }
        // user email
        echo "<td>" . $row["user_email"] . "</td>";

        echo "<td>";

        // shift
        if ($row["shift_id"] == 1) {
            echo "08:00:00";
        } else if ($row["shift_id"] == 2) {
            echo "10:00:00";
        } else if ($row["shift_id"] == 3) {
            echo "13:00:00";
        } if ($row["shift_id"] == 4) {
            echo "15:00:00";
        }      
        
        echo "</td>";  

        // status
        $resultStatus = mysqli_query($conexion, $sqlStatus);

        while($row2 = mysqli_fetch_assoc($resultStatus)) {

          if ($row2["id"] == $row["status_id"]) {

              echo "<td>" . $row2["status"] . "</td>";
              break;
          }
        
        }


        echo "</tr>";

        if ($row["type_app_id"] == 4) {

            $ismayorrepair = true;

        }
    
    
    }   


}    

include 'include/closeDatabase.php';

?>