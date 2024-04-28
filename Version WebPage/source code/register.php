<?php
    session_start();

    if (isset($_SESSION['loggedin'])) {

      if ($_SESSION['typeUser'] == 1) {

        header("Location: homeadmin.php");

      } else {

        header("Location: homeuser.php");

      }

    }

    include 'include/database.php';

    if(!empty($_POST)){ 

      $fullName= mysqli_real_escape_string($conexion,$_POST['fullName']);
      $email=mysqli_real_escape_string($conexion,$_POST['email']);
      $phoneNumber=mysqli_real_escape_string($conexion,$_POST['phoneNumber']);
      $password1=mysqli_real_escape_string($conexion,$_POST['password1']);
      $vehiclePlateNumber=mysqli_real_escape_string($conexion,$_POST['vehiclePlateNumber']);
      $typeVehicle=mysqli_real_escape_string($conexion,$_POST['typeVehicle']);
      $makeVehicle=mysqli_real_escape_string($conexion,$_POST['makeVehicle']);
      $engineType=mysqli_real_escape_string($conexion,$_POST['engineType']);
      $encrypted_password=sha1($password1);

      if ($makeVehicle == "Other") {

        $other=mysqli_real_escape_string($conexion,$_POST['other']);
        $makeVehicle = $other;

      }
      // get type of user
      $sqlTypeUser = "SELECT id FROM type_user WHERE type = 'user'";
      $resultTypeUser = mysqli_query($conexion, $sqlTypeUser);
      $rowTypeUser = mysqli_fetch_assoc($resultTypeUser);
      $idTypeUser = $rowTypeUser["id"];

      // get id of engine type
      $sqlEngine = "SELECT id FROM engine_type WHERE type = '$engineType'";
      $resultEngine = mysqli_query($conexion, $sqlEngine);
      $rowEngine = mysqli_fetch_assoc($resultEngine);
      $idEngine = $rowEngine["id"];

      // get id of type of vehicle
      $sqlTypeVehicle = "SELECT id FROM type_car WHERE type = '$typeVehicle'";
      $resultTypeVehicle = mysqli_query($conexion, $sqlTypeVehicle);
      $rowTypeVehicle = mysqli_fetch_assoc($resultTypeVehicle);
      $idTypeVehicle = $rowTypeVehicle["id"];
      $success = false;
      $sqlEmail = "SELECT email FROM user WHERE email= '$email'";
      $resultEmail = mysqli_query($conexion, $sqlEmail);

      if (mysqli_num_rows($resultEmail) > 0) { // user already exist in the database

          echo"<script>alert('the user already exist!');</script>";
          
      } else {  //user does not exist, it will be registered

              $sqlUser = "INSERT INTO user(email, password, name, mobilePhone, type_user_id, plateNumber, make, engine_type_id, type_car_id) VALUES('$email','$encrypted_password','$fullName','$phoneNumber','$idTypeUser','$vehiclePlateNumber', '$makeVehicle', '$idEngine', '$idTypeVehicle')";
              if ((mysqli_query($conexion, $sqlUser))) {

                     include 'include/closeDatabase.php';
                     $success = true;
                     echo"<script> alert('successfully registered. Please log in');
                     window.location='index.php';</script>"; 
                  
              }  else {
                echo "<script> alert('Conexion error!');</script>";
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

  <body style="height:auto">

    <div class="cover-container d-flex p-3 flex-column align-items-stretch">

    <form action="register.php" method="post">
        <h1 class="h3 mb-3 font-weight-normal text-center">Register</h1>
        <div class="form-group">
          <label for="email">Email address
            <span class="asteriskField">
              *
             </span>
          </label>
          <input type="email" name="email" class="form-control" id="email" 
          value="<?php if (!empty($_POST["email"]) && !$success) { echo $_POST["email"]; } ?>"
          aria-describedby="emailHelp" placeholder="Enter email" required> 
          <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
        </div>

        <div class="form-group">
            <label for="fullName">Full name
              <span class="asteriskField">
                *
              </span>
            </label>
            <input type="text" name="fullName" class="form-control" id="fullName" 
            value="<?php if (!empty($_POST["fullName"]) && !$success) { echo $_POST["fullName"]; } ?>"
            aria-describedby="NameHelp" placeholder="Enter full name" required>
          </div>

        <!--
            // at least one number, one lowercase and one uppercase letter
            // at least six characters that are letters, numbers or the underscore
         --> 

        <div class="form-group">
          <label for="password1">Password
            <span class="asteriskField">
              *
            </span>
          </label>
          <input type="password" class="form-control" id="password1" 
          required pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}" name="password1"
          aria-describedby="passwordHelpBlock" placeholder="Password" required>

          <small id="passwordHelpBlock" class="form-text text-muted">
              At least one number, one lowercase and one uppercase letter.
              At least six characters that are letters, numbers or the underscore.
          </small>
        </div>

        <div class="form-group">
            <label for="password2">Password
              <span class="asteriskField">
                *
              </span>
            </label>
            <input type="password" required
            name="password2"
            class="form-control" id="password2" placeholder="Repeat password" 
            required> 
        </div>
  
        <div class="form-group">
            <label for="phoneNumber">Phone Number
               <span class="asteriskField">
                *
               </span>
            </label>
            <input type="text" name="phoneNumber" class="form-control" 
            value="<?php if (!empty($_POST["phoneNumber"]) && !$success) { echo $_POST["phoneNumber"]; } ?>"
            id="phoneNumber" placeholder="Mobile Phone Number" required>
        </div>
  
        <!--

        the first 2 characters are digit, which represent a year, and the third digit must 1 or 2. 
        After, we have the options which represent every city/town
        The las part could be 1 digit or 6
        
        -->  

        <div class="form-group">
            <label for="vehiclePlateNumber">Vehicle Plate Number
              <span class="asteriskField">
                *
              </span>
            </label>
            <input type="text" name="vehiclePlateNumber"
            value="<?php if (!empty($_POST["vehiclePlateNumber"]) && !$success) { echo $_POST["vehiclePlateNumber"]; } ?>"
            aria-describedby="plateNumberHelpBlock"
            required pattern="^([0-9]{2})[12]?-(C|CE|CN|CW|D|DL|G|KE|KK|KY|L|LD|LH|LM|LS|MH|MN|MO|OY|RN|SO|T|W|WH|WW|WX|LK|TN|TS|WD)-([0-9]{1,6})$"
            class="form-control" id="vehiclePlateNumber" placeholder="Plate Number" required>
            
            <small id="plateNumberHelpBlock" class="form-text text-muted">
              YY(1|2)-(C|CE|CN|CW|D|DL|G|KE|KK|KY|L|LD|LH|LM|LS|MH|MN|MO|OY|RN|SO|T|W|WH|WW|WX|LK|TN|TS|WD)-(1 or 6 numbers from 1 to 9}, Y = Year, for example 191-DL-1, year = 2019. Another example, 232-RN-123654, year = 2023. 
            </small>
            
        </div>

        <div class="form-group">
            <label for="typeVehicle">Type of Vehicle
              <span class="asteriskField">
                *
              </span>
            </label>
            <select type="text" name="typeVehicle" class="form-control" 
            id="typeVehicle" placeholder="Plate Number" required>
                <option value="" selected>Choose a type of vehicle
                  <span class="asteriskField">
                    *
                  </span>
                </option>
                <?php
                  // get types of car from database
                  $sql = 'SELECT type FROM type_car';
                  $result = mysqli_query($conexion, $sql);
                  
                  if (mysqli_num_rows($result) > 0) {
                  
                  
                      while($row = mysqli_fetch_assoc($result)) {
                  
                          echo "<option>" . $row["type"] . "</option>";
                      }   
                  }

                ?>
            </select>
        </div>

        <div class="form-row">

            <div class="col-md-8 mb-12">
                <label for="makeVehicle">Make Vehicle
                  <span class="asteriskField">
                    *
                  </span>
                </label>
                <select type="text" name="makeVehicle" 
                onclick="enableOther()" class="form-control" id="makeVehicle" placeholder="Make Vehicle" required>
                    <option value="" selected>Choose a make of vehicle</option>
                    <?php
                      // get makes of car from database
                      $sql = 'SELECT make FROM make';
                      $result = mysqli_query($conexion, $sql);
                      
                      if (mysqli_num_rows($result) > 0) {
                      
                      
                          while($row = mysqli_fetch_assoc($result)) {
                      
                              echo "<option>" . $row["make"] . "</option>";
                          } 
                          
                      }
                    ?>
                </select>
            </div>

            <div class="col-md-4 mb-12">
                <label for="other">Other</label>
                <input type="text" name="other" class="form-control" id="other" 
                placeholder="Enter other" disabled>
            </div>    
        </div>

        <br>
        
        <div class="form-group">
            <label for="engineType">Engine Type
              <span class="asteriskField">
                *
              </span>
            </label>
            <select type="text" name="engineType" class="form-control" 
            id="engineType" placeholder="Engine Type" required>
                <option value="" selected>Choose a type of engine</option>
                <?php
                  // get types of car from database
                  $sql = 'SELECT type FROM engine_type';
                  $result = mysqli_query($conexion, $sql);
                  
                  if (mysqli_num_rows($result) > 0) {
                  
                  
                      while($row = mysqli_fetch_assoc($result)) {
                  
                          echo "<option>" . $row["type"] . "</option>";
                      }   
                  }
                  include 'include/closeDatabase.php';
                ?>
            </select>
        </div>
        <div style="text-align: center;">
          <button type="submit" class="btn button-login" onsubmit="return validateForm()">Submit</button>
        
        </div>
        <br>
        <label>
          <span class="asteriskField">
            *
          </span>Required fields
        </label>
      </form>
    </div>

    <script>
    
      var password = document.getElementById("password1"),
      confirm_password = document.getElementById("password2");

      function validatePassword(){
        if(password.value != confirm_password.value) {
          confirm_password.setCustomValidity("Passwords don't match");
        } else {
          confirm_password.setCustomValidity('');
        }
      }

      password.onchange = validatePassword;
      confirm_password.onkeyup = validatePassword;
      
    </script>


    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
  </body>
</html>
