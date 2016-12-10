var appPrefix = btoa('endpoint');
var login = localStorage.getItem(appPrefix + 'login')
var pass = localStorage.getItem(appPrefix + 'pass')

$(document).ready( function () {
    $.get( {
        url: 'rest/get_customer_id/' + login,
        headers: {
            'Authorization': 'Basic ' + btoa(login + ':' + pass)
        }
    })
        .done(function (data) {
            $.get( {
                url: 'rest/get_customer_data/' + data.id,
                headers: {
                    'Authorization': 'Basic ' + btoa(login + ':' + pass)
                }
            })
                .done(function (data) {
                    document.getElementById("cur_balance").value = data.data.money;
                })
        });

    $('#accept').click( function () {
        $('input[type="text"]').css({"border":"2px solid #00F5FF","box-shadow":"0 0 5px #00F5FF"});

        var amount = $('#amount').val();

        if(amount <= 0) {
            if ($("#amount").next(".validation").length == 0) {
                $("#amount").after("<div class='validation' style='color:red;margin-bottom: 20px;'>Amount should be greater than 0</div>");
                $('input[type="text"]').css({"border":"2px solid red","box-shadow":"0 0 3px red"});
            }
        }
        else {
            $("#amount").next(".validation").remove();
            $.get( {
                url: 'rest/get_customer_id/' + login,
                headers: {
                    'Authorization': 'Basic ' + btoa(login + ':' + pass)
                }
            })
                .done(function (data) {
                    $.post({
                        url: 'rest/top_up_balance/' + data.id + '/' + amount,
                        headers: {
                            'Authorization': 'Basic ' + btoa(login + ':' + pass),
                            'Content-Type': 'application/json'
                        }
                        })
                            .done(function(data) {
                                //$.redirect('/endpoint/balance.html', {}, 'GET');
                                location.reload();
                            });
                });
        }
    });

    $('#home').click( function () {
        $.redirect('/endpoint/customer_dashboard.html', {}, 'GET');
    })
});