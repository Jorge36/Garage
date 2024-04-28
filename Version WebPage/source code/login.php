<?php
    session_start();
  
    if (isset($_SESSION['loggedin'])) {

      if ($_SESSION['typeUser'] == 1) {

        header("Location: homeadmin.php");

      } else {

        header("Location: homeuser.php");

      }

    }

    require 'include/database.php';

    // bring email and password from input, select data from database
    // if user exists and password is correct, the user will be logged in
    if(!empty($_POST)){
        $email = mysqli_real_escape_string($conexion,$_POST['email']);
        $password = mysqli_real_escape_string($conexion,$_POST['password']);
        $encrypted_password=sha1($password);
        $sql="SELECT email, name, type_user_id FROM user WHERE email = '$email' AND password = '$encrypted_password'";
        $result = mysqli_query($conexion, $sql);

        if (mysqli_num_rows($result) > 0) { // user exists in the database

          // get type of user
          $sqlTypeUser = "SELECT id FROM type_user WHERE type = 'user'";
          $resultTypeUser = mysqli_query($conexion, $sqlTypeUser);
          $rowTypeUser = mysqli_fetch_assoc($resultTypeUser);
          $idTypeUser = $rowTypeUser["id"];

          $row = mysqli_fetch_assoc($result);
          $_SESSION['loggedin'] = true;
          $_SESSION['name'] = $row['name'];
          $_SESSION['typeUser'] = $row['type_user_id'];
          $_SESSION['user']= $row['email']; // sessiom variable: it is global variable to be used across multiple pages.

          if ($row['type_user_id'] == $idTypeUser) { // this is a normal user

            include 'include/closeDatabase.php';
            echo "<script>
                alert('you have successfully logged in');
                window.location.href='homeuser.php';</script>";

          } else { // this an admin user

            include 'include/closeDatabase.php';
            echo "<script>
                alert('you have successfully logged in');
                window.location.href='homeadmin.php';</script>";

          }

        } else {

            echo"<script> alert('email or password incorrect');</script>";

        }

    }

    include 'include/closeDatabase.php';

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

  </head>
  <body class="text-center" style="padding-top: 100px;padding-bottom: 100px;">

    <div class="cover-container d-flex p-3 flex-column align-items-stretch">

    <form action="login.php" method="post">
        <h1 class="h3 mb-3 font-weight-normal">Please Log in</h1>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="email" id="inputEmail" name="email" class="form-control" placeholder="Email address" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" name="password" class="form-control" placeholder="Password" required>
        <br>
        <button class="btn btn-lg btn-block button-login" type="submit">Log in</button>
    </form>

    </div>

    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
  </body>
</html>