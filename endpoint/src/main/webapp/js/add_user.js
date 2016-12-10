var appPrefix = btoa('endpoint');
var login = localStorage.getItem(appPrefix + 'login')
var pass = localStorage.getItem(appPrefix + 'pass')

$(document).ready(function(){
    $('#home').click( function () {
        $.redirect('/endpoint/customer_dashboard.html', {}, 'GET');
    })

    $("#create_user_id").click(
        function() {
            var fName = $("#first_name_id").val();
            var lName = $("#last_name_id").val();
            var email = $("#email_id").val();
            var password = $("#password_id").val();
            var role = $("#role_id").val();

            // check fields
            if(email =='' || password =='') {
                $('input[type="text"],input[type="password"]').css("border","2px solid red");
                $('input[type="text"],input[type="password"]').css("box-shadow","0 0 3px red");
                alert("Email or password is empty");
            } else {
                $.post({
                    url: 'rest/create_user',
                    headers: {
                        'Authorization': 'Basic ' + btoa(login + ':' + pass),
                        'Content-Type': 'application/json'
                    },
                    data: JSON.stringify({
                            "firstName":fName,
                            "lastName":lName,
                            "login":email,
                            "pass":password,
                            "userRole":role
                    })
                }).done(function(data) {
                    $.redirect('/endpoint/customer_dashboard.html', {}, 'GET');
                });
            }
        }
    );
});