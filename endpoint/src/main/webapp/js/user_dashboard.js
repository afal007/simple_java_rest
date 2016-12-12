var appPrefix = btoa('endpoint');
var login = localStorage.getItem(appPrefix + 'login')
var pass = localStorage.getItem(appPrefix + 'pass')

$(document).ready(function() {
    $.get({
        url: 'rest/get_user_id/' + login,
        headers: {
            'Authorization': 'Basic ' + btoa(login + ':' + pass)
        }
    }).done(function(data) {
        $.get({
            url: 'rest/get_user_data/' + data.id,
            headers: {
                'Authorization': 'Basic ' + btoa(login + ':' + pass)
            }
        }).done(function(data) {
            $("#first_name_id").append(data.data.firstName);
            $("#last_name_id").append(data.data.lastName);
            $("#email_id").append(data.data.login);
            $("#password_id").append(data.data.pass);
            $("#role_id").append(data.data.userRole);
        });
    });
});