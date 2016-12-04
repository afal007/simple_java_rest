$(document).ready(function(){
    $("#login").click(function(){
        var email = $("#email").val();
        var password = $("#password").val();
        // Checking for blank fields.
        if(email =='' || password =='') {
            $('input[type="text"],input[type="password"]').css("border","2px solid red");
            $('input[type="text"],input[type="password"]').css("box-shadow","0 0 3px red");
            alert("ASDF or password is empty");
        } else {
            $.get({
                url: 'rest/get_role',
                headers: {
                    'Authorization': 'Basic ' + btoa(email + ':' + password)
                }
            })
                .done(function(data) {
                    var dataObj = $.parseJSON(data);
                    var role = dataObj['role']
                    if(role =='ADMIN') {
                        $.redirect('/endpoint/customers.html', {'login': email, 'pass': password, 'role': 'ADMIN'}, 'GET');
                    } else if (role == 'CUSTOMER'){
                    	$.redirect('/endpoint/customer_dashboard.html', {'login': email, 'pass': password}, 'GET');
                    } else if(role =='UNKNOWN') {
                        $('input[type="text"],input[type="password"]').css({"border":"2px solid red","box-shadow":"0 0 3px red"});
                        alert("Email or password is incorrect");
                    }
                });
        }
    });
});