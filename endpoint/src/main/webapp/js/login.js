var appPrefix = btoa('endpoint'); //very secret
$(document).ready(function(){
    $("#login").click(function(){
        $('input[type="email"],input[type="password"]').css({"border":"2px solid #00F5FF","box-shadow":"0 0 5px #00F5FF"});

        var email = $("#email").val();
        var password = $("#password").val();
        localStorage.setItem(appPrefix + 'login', email);
		localStorage.setItem(appPrefix + 'pass', password);
        // Checking for blank fields.
        if(email == '' || password == '') {
            if ($("#email").parent().next(".validation").length == 0) {
                $("#email").parent().after("<div class='validation' style='color:red;margin-bottom: 20px;'>Empty password or e-mail.</div>");
                $('input[type="email"],input[type="password"]').css({"border":"2px solid red","box-shadow":"0 0 3px red"});
            }
        } else {
            $("#email").parent().next(".validation").remove();

            $.get({
                url: 'rest/get_customer_id/' + email,
                headers: {
                    'Authorization': 'Basic ' + btoa('admin:setup')
                }
            })
                .done(function (data) {
                    if(data['id'] == '00000000-0000-0000-0000-000000000000') {
                        if ($("#email").next(".validation").length == 0) {
                            $("#email").after("<div class='validation' style='color:red;margin-bottom: 20px;'>Wrong e-mail address.</div>");
                            $('input[type="email"]').css({"border":"2px solid red","box-shadow":"0 0 3px red"});
                        }
                        $("#email").focus();
                    } else {
                        $("#email").next(".validation").remove();
                        $('input[type="password"]').css({"border":"2px solid #00F5FF","box-shadow":"0 0 5px #00F5FF"});

                        $.get({
                            url: 'rest/get_role',
                            headers: {
                                'Authorization': 'Basic ' + btoa(email + ':' + password)
                            }
                        })
                            .done(function (data) {
                                var dataObj = $.parseJSON(data);
                                var role = dataObj['role'];
                                if(role == 'ADMIN') {
                                    $.redirect('/endpoint/customers.html',{'login': email, 'pass': password, 'role': 'ADMIN'}, 'GET');
                                } else if (role == 'CUSTOMER'){
                                    $.redirect('/endpoint/customer_dashboard.html', {'login':email, 'pass':password, 'role': 'CUSTOMER'}, 'GET');
                                } else if(role =='UNKNOWN') {
                                    if ($("#password").next(".validation").length == 0)
                                    {
                                        $("#password").after("<div class='validation' style='color:red;margin-bottom: 20px;'>Wrong password.</div>");
                                        $('input[type="password"]').css({"border":"2px solid red","box-shadow":"0 0 3px red"});
                                    }
                                    $("#password").focus();
                                }
                            })
                    }
                })
        }
    });
});