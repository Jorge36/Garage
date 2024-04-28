<?php
    session_start();

    if (!isset($_SESSION['loggedin'])) {
      header("Location: index.php");
    }

    if (isset($_SESSION['loggedin'])) {

      if ($_SESSION['typeUser'] == 1) {

        header("Location: homeadmin.php");

      } 

    }

    include 'include/database.php';

    if(!empty($_POST)) { 

      $vehiclePlateNumber= mysqli_real_escape_string($conexion,$_POST['vehiclePlateNumber']);
      $booking=mysqli_real_escape_string($conexion,$_POST['booking']);
      $comment=mysqli_real_escape_string($conexion,$_POST['comment']);
      $date=mysqli_real_escape_string($conexion,$_POST['date']);
      $time=mysqli_real_escape_string($conexion,$_POST['time']);
      $email = $_SESSION['user'];
      $idStatus = 1;

      // get type of user
      $sqlType_app = "SELECT id FROM type_app WHERE type = '$booking'";
      $resultType_app = mysqli_query($conexion, $sqlType_app);
      $rowType_app = mysqli_fetch_assoc($resultType_app);
      $idType_app = $rowType_app["id"];

      $date_aux1 = str_replace('/', '-', $date);

      $date_aux2 = date_create($date_aux1, new DateTimeZone('utc'));

      $date_sql = date_format($date_aux2, 'Y-m-d');

      // get appoinments for a date and their shifts
      $sql = "SELECT appoinment.id, appoinment.user_email, appoinment_has_shift.appoinment_id,  appoinment_has_shift.shift_id
              FROM appoinment INNER JOIN appoinment_has_shift
              ON  appoinment.id = appoinment_has_shift.appoinment_id
              AND appoinment.user_email = appoinment_has_shift.appoinment_user_email
              WHERE appoinment.date = '$date_sql'";

      $result = mysqli_query($conexion, $sql);

      if (mysqli_num_rows($result) == 0) { // if there is no appointments for DATE, insert in database

        $sqlApp = "INSERT INTO appoinment(user_email, date, comment, status_id, type_app_id) VALUES('$email','$date_sql','$comment','$idStatus','$idType_app')";
        
        if ((mysqli_query($conexion, $sqlApp))) { // if the insert is successfull, insert in database

          $last_id = mysqli_insert_id($conexion);
          if ($booking == "major repair") {    // majot repair counts double, groups shifts     

            if ($time == 1) {

              $firstShift = 1;
              $secondShift = 2;

            } elseif ($time == 2) {

              $firstShift = 2;
              $secondShift = 3;

            } elseif ($time == 3) {

              $firstShift = 3;
              $secondShift = 4;

            }  

            $sqlappoinmentShift1 = "INSERT INTO appoinment_has_shift(appoinment_id, appoinment_user_email, shift_id) VALUES('$last_id', '$email', '$firstShift')";
            $sqlappoinmentShift2 = "INSERT INTO appoinment_has_shift(appoinment_id, appoinment_user_email, shift_id) VALUES('$last_id', '$email', '$secondShift')";
            if ((mysqli_query($conexion, $sqlappoinmentShift1)) && (mysqli_query($conexion, $sqlappoinmentShift2))) {

              echo"<script> alert('Appointment " . $last_id . " created successfully');</script>";

            } else {

              echo"<script> alert('Appointment was not created');</script>"; 

            }

          } else { // annother type of service major service/repair/major repair, only insert one row because these services don't count double

            $sqlappoinmentShift1 = "INSERT INTO appoinment_has_shift(appoinment_id, appoinment_user_email, shift_id) VALUES('$last_id', '$email', '$time')";

            if (mysqli_query($conexion, $sqlappoinmentShift1)) {

              echo"<script> alert('Appointment " . $last_id . " created successfully');</script>"; 

            } else {
              echo"<script> alert('Appointment was not created. Try it again.');</script>"; 

            }
                    
          } 

        } else {

          echo"<script> alert('Appointment was not created. Try it again.');</script>"; 
          
        }

        } else { // there are appointments for DATE, first check 

          if ($booking == "major repair") {    // majot repair counts double      

            if ($time == 1) {

              $firstShift = 1;
              $secondShift = 2;

            } elseif ($time == 2) {

              $firstShift = 2;
              $secondShift = 3;

            } elseif ($time == 3) {

              $firstShift = 3;
              $secondShift = 4;

            } 

            $first = 0;
            $second = 0;
            while($row = mysqli_fetch_assoc($result)) { // check if shifts were already taken
                      
              if ($row['shift_id'] == $firstShift) {

                $first++;

              } 

              if ($row['shift_id'] == $secondShift) {

                $second++;
              } 

            } 

            if (($first == 4) || ($second == 4)) { // shifts were already taken

              echo"<script> alert('Appointment was not created. Please, choose another Shift or Date/Shift');</script>"; 

            } else { // shifts were not taken, insert in database

              $sqlApp = "INSERT INTO appoinment(user_email, date, comment, status_id, type_app_id) VALUES('$email','$date_sql','$comment','$idStatus','$idType_app')";
        
              if ((mysqli_query($conexion, $sqlApp))) { // if the insert is successfull, insert in database
      
                  $last_id = mysqli_insert_id($conexion);
                  $sqlappoinmentShift1 = "INSERT INTO appoinment_has_shift(appoinment_id, appoinment_user_email, shift_id) VALUES('$last_id', '$email', '$firstShift')";
                  $sqlappoinmentShift2 = "INSERT INTO appoinment_has_shift(appoinment_id, appoinment_user_email, shift_id) VALUES('$last_id', '$email', '$secondShift')";

                  if ((mysqli_query($conexion, $sqlappoinmentShift1)) && (mysqli_query($conexion, $sqlappoinmentShift2))) {
      
                    echo"<script> alert('Appointment " . $last_id . " created successfully');</script>"; 
      
                  } else {
      
                    echo"<script> alert('Appointment was not created. Try it again.');</script>"; 
      
                  }

              } else {

                echo"<script> alert('Appointment was not created. Try it again.');</script>"; 
                
              }   
  
            }
         
          
        }  else { // another type of service major service/repair/major repair


          $first = 0;
          while($row = mysqli_fetch_assoc($result)) { // check if shift was already taken
                    
            if ($row['shift_id'] == $time) {

              $first++;

            } 

          } 

          if ($first == 4) {

            echo"<script> alert('Appointment was not created. Please, choose another Shift or Date/Shift');</script>"; 

          } else {

            $sqlApp = "INSERT INTO appoinment(user_email, date, comment, status_id, type_app_id) VALUES('$email','$date_sql','$comment','$idStatus','$idType_app')";
        
            if ((mysqli_query($conexion, $sqlApp))) { // if the insert is successfull, insert in database
    
                $last_id = mysqli_insert_id($conexion);
                $sqlappoinmentShift1 = "INSERT INTO appoinment_has_shift(appoinment_id, appoinment_user_email, shift_id) VALUES('$last_id', '$email', '$time')";

                if (mysqli_query($conexion, $sqlappoinmentShift1)) {

                  echo"<script> alert('Appointment " . $last_id . " created successfully');
                      </script>"; 

                } else {

                    echo"<script> alert('Appointment was not created. Try it again.');</script>"; 

                }

            } else {

              echo"<script> alert('Appointment was not created. Try it again.');</script>"; 
              
            }      
          }

        }
        
      }

    }  

?>

<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <title>Garage</title>

    <!-- Custom styles for this template -->
    <link href="css/cover.css" rel="stylesheet">

    <!-- Javascript functionalities -->
    <script src="js/javascript.js"></script>

  </head>
  <body style="height:auto;">

    <div class="cover-container d-flex h-100 w-100 p-3 mx-auto flex-column"> 
      
        <header class="masthead mb-auto">
          <div class="inner">
            <h3 class="masthead-brand">Garage</h3>
            <nav class="nav nav-masthead justify-content-center">
              <a class="nav-link active" href="homeuser.php">Home</a>
              <a class="nav-link" href="logout.php">Log out</a>
            </nav>
          </div>
        </header>

        <main role="main" class="inner cover w-100">

            <hr class="featurette-divider">

            <p class="lead" style="text-align:center;">Welcome <?php echo $_SESSION['name'];?>!</p>

            <form action="homeuser.php" method="post">

                <div class="form-group text-left">
                    <label for="Vehicle">Vehicle Plate Number</label>
                    <?php
                        // get vehicle make and plate number
                        $email = $_SESSION['user'];
                        $sqlEmail = "SELECT plateNumber, make FROM user WHERE email= '$email'";
                        $resultEmail = mysqli_query($conexion, $sqlEmail);

                        if (mysqli_num_rows($resultEmail) > 0) { // user exists in the database

                          $row = mysqli_fetch_assoc($resultEmail);

                          echo '<input type="text" name="vehiclePlateNumber"
                          class="form-control" id="vehiclePlateNumber" value="'. $row["make"] . ' - ' . $row["plateNumber"] .'"readonly>';


                        }  
                    ?>

                </div>

                <div class="form-group text-left">
                    <label for="booking">Choose a type of booking
                      <span class="asteriskField">
                        *
                      </span>
                    </label>
                    <select type="text" class="form-control" id="booking" name="booking" placeholder="Choose a type of booking" onchange="fillTimeSelect()" required>
                        <option value="" selected>Choose a type of booking</option>

                        <?php
                         // get services from database
                        $sql = 'SELECT type FROM type_app';
                        $result = mysqli_query($conexion, $sql);
                  
                          if (mysqli_num_rows($result) > 0) {
                          
                          
                              while($row = mysqli_fetch_assoc($result)) {
                          
                                  echo "<option>" . $row["type"] . "</option>";
                              }   
                          }

                        ?>
                    </select>
                </div>

                <div class="form-group text-left">
                    <label for="comment">Comments</label>
                    <textarea class="form-control" max=200 id="comment" name="comment" ows="4" cols="50"></textarea>
                </div>

                <div class="form-group text-left">
                  <label for="date">
                  Date
                  <span class="asteriskField">
                    *
                  </span>
                  </label>
                  <input class="form-control" id="date" autocomplete="off" name="date" placeholder="dd/mm/yyyy" type="text" required/>
                </div>  

                <div class="form-group text-left">
                  <label for="time">
                  Book available time
                  <span class="asteriskField">
                    *
                  </span>
                  </label>
                  <select type="text" class="form-control" id="time" name="time" placeholder="Book available time" onclick="getShifts()" required>
                    <option selected value="">Book available time (Shift)</option>
                </select>
                </div>  
                <div style="text-align:center;">
                  <button type="submit" class="btn button-login">Make Appointment</button>
                </div>
                <label>
                  <br>
                  <span class="asteriskField">
                    *
                  </span>Required fields
                </label>
              
            </form>

                <hr class="featurette-divider">

                <div class="table-responsive">
                  <table class="table table-striped">
                    <caption>List of your appointments</caption>
                    <thead>
                      <tr>
                        <th scope="col">Booking number</th>
                        <th scope="col">Booking date</th>
                        <th scope="col">Booking type</th>
                        <th scope="col">Time</th>
                        <th scope="col">Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      <?php    

                      // get status
                      $sqlStatus = "SELECT * FROM status";

                      // get type of service
                      $sqlTypeApp = "SELECT * FROM type_app";

                      // build table with appointments
                      $email = $_SESSION['user'];    

                      // get shifts
                      $sqlShifts = "SELECT appoinment_id, shift_id  
                                    FROM appoinment_has_shift 
                                    WHERE appoinment_user_email = '$email'";

                      // get appoinments for a date and their shifts
                      $sql = "SELECT appoinment.id, appoinment.date, appoinment.type_app_id,  
                                     appoinment.status_id
                              FROM user INNER JOIN appoinment
                              ON  user.email = appoinment.user_email  
                              WHERE user.email = '$email'";

                      $result = mysqli_query($conexion, $sql);

                      if (mysqli_num_rows($result) > 0) {

                        // build table with appointment
                        while($row = mysqli_fetch_assoc($result)) {
                      
                          echo "<tr><th scope='row'>" . 
                               $row["id"]  . "</th>";

                          // convert date to dd-mm-yyyy from yyyy-mm-dd
                          $appDate = $row["date"];

                          $appDateAux = str_replace('-', '/', $appDate);

                          $appDateAux2 = date_create($appDateAux, new DateTimeZone('utc'));

                          $AppDateResult = date_format($appDateAux2, 'd-m-Y');

                          echo "<td>" . $AppDateResult . "</td>";

                          $resultTypeApp = mysqli_query($conexion, $sqlTypeApp);

                          while($row2 = mysqli_fetch_assoc($resultTypeApp)) {

                            if ($row2["id"] == $row["type_app_id"]) {

                                echo "<td>" . $row2["type"] . "</td>";
                                break;
                            }
                          
                          }

                          $resultShifts = mysqli_query($conexion, $sqlShifts);

                          echo "<td>";

                          while($rowShift = mysqli_fetch_assoc($resultShifts)) {

                            if ($rowShift["appoinment_id"] == $row["id"]) {

                                  if ($rowShift["shift_id"] == 1) {
                                    echo "08:00:00";
                                  } else if ($rowShift["shift_id"] == 2) {
                                    echo "10:00:00";
                                  } else if ($rowShift["shift_id"] == 3) {
                                    echo "13:00:00";
                                  } if ($rowShift["shift_id"] == 4) {
                                    echo "15:00:00";
                                  }      
                                  break;
                            }
                          
                          }

                          echo "</td>";

                          $resultStatus = mysqli_query($conexion, $sqlStatus);

                          while($row2 = mysqli_fetch_assoc($resultStatus)) {

                            if ($row2["id"] == $row["status_id"]) {

                                echo "<td>" . $row2["status"] . "</td>";
                                break;
                            }
                          
                          }

                          echo "</tr>";

                        } 
                      
                        include 'include/closeDatabase.php';
                        
                      }  

                      ?>
                    </tbody>
                  </table>
                </div>
        </main>    

      <footer class="mt-auto">
      <hr class="featurette-divider" style="background-color: rgba(14, 13, 13, 0.5);">
        <div class="inner" style="text-align:center;">
          <p>2000-2020 Company, <a href="https://twitter.com/mdo">@Jorge</a>.</p>
        </div>
      </footer>

    </div>

    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    <!-- Include Date Range Picker -->
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/js/bootstrap-datepicker.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/css/bootstrap-datepicker3.css"/>
 
    <script>
      $(document).ready(function(){
        var date_input=$('input[name="date"]'); //our date input has the name "date"
        var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
        date_input.datepicker({
          format: 'dd/mm/yyyy',
          container: container,
          todayHighlight: true,
          autoclose: true,
          forceParse: false,
          clearBtn: true,
          daysOfWeekDisabled: "0", // disabled sunday
          startDate: '+1d', // disabled the dates before today and today 
        })
      })
    </script>


  </body>
</html>
