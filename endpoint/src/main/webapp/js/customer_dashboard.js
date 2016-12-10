var appPrefix = btoa('endpoint');
var login = localStorage.getItem(appPrefix + 'login')
var pass = localStorage.getItem(appPrefix + 'pass')
if(login == '' || pass == ''){
    $.redirect('/endpoint/login.html');
}

$(document).ready(function(){
    $("#buy_plan").click(function() {
        $.redirect('/endpoint/buy_plan.html');
    });
    $("#add_user").click(function() {
        $.redirect('/endpoint/add_user.html');
    });
    $("#subscriptions").click(function() {
        $.redirect('/endpoint/subscriptions.html');
    });
    $("#balance").click(function() {
        $.redirect('/endpoint/balance.html');
    });

    $.get({
        url: 'rest/get_users/' + login,
        headers: {
            'Authorization': 'Basic ' + btoa(login + ':' + pass)
        }
    }).done(function(data) {
        //var json = $.parseJSON(data);
        var json = data;
        var dataSet = [];
        for(var i = 0; i < json.length; i++) {
            var obj = json[i];
            dataSet.push([obj.firstName, obj.lastName, obj.login, obj.pass, obj.userRole])
        }

        $('#user_list_id')
            .DataTable({
                data: dataSet,
                columns: [
                    { title: "Fist Name" },
                    { title: "Last Name" },
                    { title: "Email" },
                    { title: "Pass" },
                    { title: "User Role" }
                ]
            });
    });
});