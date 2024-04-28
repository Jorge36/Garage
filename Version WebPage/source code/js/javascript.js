function enableOther(){
    var selectobject;
    selectobject = document.getElementById("makeVehicle").value;

    if (selectobject == "Other") {
        document.getElementById("other").disabled = false;
        document.getElementById("other").required = true;
    } else {
        document.getElementById("other").value = "";
        document.getElementById("other").disabled = true;
        document.getElementById("other").required = false;
    }
}

function getShifts() {

    var booking = document.getElementById("booking").value;
    //var date = document.getElementById("date").value;
    var time = document.getElementById("time");
    //if ((booking == "") || (date == "")) {
    console.log(booking);
    if (booking == "")  {

        time.setCustomValidity("First, you must select type of booking");
        time.reportValidity();

    } 

}

// fill the select on the screen with the shifts
function fillTimeSelect() {

    var booking = document.getElementById("booking");
    var value =  booking.options[booking.selectedIndex].value;
    var text =  booking.options[booking.selectedIndex].text;
    var time = document.getElementById("time");

    if ((value == "major repair") && (time.options.length == 1)) { // if user select major repair
                                                                  // it has 3 shifts

        var option = document.createElement("option");
        option.value = "1";
        option.text =  "Morning - 08:00:00";
        time.appendChild(option);

        option = document.createElement("option");
        option.value = "2";
        option.text =  "Morning Afternoon - 10:00:00";
        time.appendChild(option);

        option = document.createElement("option");
        option.value = "3";
        option.text =  "Afternoon - 13:00:00";
        time.appendChild(option);

        return;

    } else if (((value == "annual service") || (value == "major service") || (value == "repair"))
    && (time.options.length == 1)) { // if user select othe service, it has 4 shifts 

        var option = document.createElement("option");
        option.value = "1";
        option.text =  "Morning 1 - 08:00:00";
        time.appendChild(option);
    
        option = document.createElement("option");
        option.value = "2";
        option.text =  "Morning 2 - 10:00:00";
        time.appendChild(option);
    
        option = document.createElement("option");
        option.value = "3";
        option.text =  "Afternoon 1 - 13:00:00";
        time.appendChild(option);
    
        option = document.createElement("option");
        option.value = "4";
        option.text =  "Afternoon 2 - 15:00:00";
        time.appendChild(option);    

        return;

    }


    if ((value == "major repair") && (time.options.length == 5)) {

        time.remove(1);
        time.remove(1);
        time.remove(1);
        time.remove(1);
        
        var option = document.createElement("option");
        option.value = "1";
        option.text =  "Morning - 08:00:00";
        time.appendChild(option);

        option = document.createElement("option");
        option.value = "2";
        option.text =  "Morning Afternoon - 10:00:00";
        time.appendChild(option);

        option = document.createElement("option");
        option.value = "3";
        option.text =  "Afternoon - 13:00:00";
        time.appendChild(option);

    } else if (((value == "annual service") || (value == "major service") || (value == "repair"))
            && (time.options.length == 4)) {

        time.remove(1);
        time.remove(1);
        time.remove(1);

        var option = document.createElement("option");
        option.value = "1";
        option.text =  "Morning 1 - 08:00:00";
        time.appendChild(option);
    
        option = document.createElement("option");
        option.value = "2";
        option.text =  "Morning 2 - 10:00:00";
        time.appendChild(option);
    
        option = document.createElement("option");
        option.value = "3";
        option.text =  "Afternoon 1 - 13:00:00";
        time.appendChild(option);
    
        option = document.createElement("option");
        option.value = "4";
        option.text =  "Afternoon 2 - 15:00:00";
        time.appendChild(option);    

    } else if ((text == "Choose a type of booking") && (time.options.length == 4)) {

        time.remove(1);
        time.remove(1);
        time.remove(1);

    } else if ((text == "Choose a type of booking") && (time.options.length == 5)) {

        time.remove(1);
        time.remove(1);
        time.remove(1);
        time.remove(1);

    }

}

function getApp() {

    var date = document.getElementById("date").value;
    if (date == "") {
        //The user doesn't enter data
        return;
    } else {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                document.getElementById("result").innerHTML = this.responseText;
            }
        };
        xmlhttp.open("GET", "getApp.php?date=" + date, true);
        xmlhttp.send();
    }


}

