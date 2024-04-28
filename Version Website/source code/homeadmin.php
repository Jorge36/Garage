<?php
    session_start();
    if (!isset($_SESSION['loggedin'])) {
      header("Location: index.php");
    }

    if (isset($_SESSION['loggedin'])) {

      if ($_SESSION['typeUser'] == 2) {

        header("Location: homeuser.php");

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
              <a class="nav-link active" href="homeadmin.php">Home</a>
              <a class="nav-link" href="logout.php">Log out</a>
            </nav>
          </div>
        </header>

        <main role="main" class="inner cover w-100">

            <hr class="featurette-divider">

            <button type="button" class="btn button-login btn-sm">Allocate Mechanics</button>
            <hr>
            <button type="button" onclick="location.href = 'printInvoice.php';" class="btn button-login btn-sm">Allocate Cost and Print Invoice</button>

            <hr class="featurette-divider">

            <div class="row">
                <div class="col-md-3 col-sm-4 col-5">
                    <div class="form-group">
                        <label for="date">
                        Date
                        </label>
                        <input class="form-control" id="date" autocomplete="off" name="date" placeholder="dd/mm/yyyy" type="text"/>
                    </div>
                    <button type="button" onclick="getApp()" class="btn button-login btn-sm">Search</button>     
                </div>
            </div>

            <hr class="featurette-divider">

            <div class="table-responsive">
                <table class="table table-striped">
                    <caption>Appointments</caption>
                    <thead>
                    <tr>
                        <th scope="col">Booking number</th>
                        <th scope="col">Booking type</th>
                        <th scope="col">Email</th>
                        <th scope="col">Time</th>
                        <th scope="col">Status</th>
                    </tr>
                    </thead>
                    <tbody id="result">

                    </tbody>
                </table>
                <button type="button" class="btn button-login btn-sm">Print Schedule</button>

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
