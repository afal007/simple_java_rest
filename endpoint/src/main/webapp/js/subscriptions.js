var appPrefix = btoa('endpoint');
var login = localStorage.getItem(appPrefix + 'login');
var pass = localStorage.getItem(appPrefix + 'pass');
var id;

$(document).ready(function(){
    $("#home").click(function() {
        $.redirect('/endpoint/customer_dashboard.html');
    });

    $.get({
        url: 'rest/get_customer_id/' + login,
        headers: {
            'Authorization': 'Basic ' + btoa(login + ':' + pass)
        }
    })
        .done(function (data) {
            $.get({
                url: 'rest/get_subscriptions/' + data.id,
                headers: {
                    'Authorization': 'Basic ' + btoa(login + ':' + pass)
                }
            }).done(function(data) {
                //var json = $.parseJSON(data);

                var json = data;
                var dataSet = [];
                for(var i = 0; i < json.length; i++) {
                    var obj = json[i];
                    dataSet.push([obj.id, obj.planId, obj.data.usedSeats, obj.data.status])
                }

                //$("#customer_list_id").html(data);
                $('#subscriptions_list_id')
                    .DataTable({
                        data: dataSet,
                        columns: [
                            { title: "UUID" },
                            { title: "Plan ID" },
                            { title: "Used seats" },
                            { title: "Status" }
                        ]
                    });
            });
        });


    $('#plan_list_id tbody').on('click', 'tr', function () {
        if( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        } else {
            $("#plan_list_id").$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
    });

    $('#buy_plan').click( function () {
        alert($("#plan_list_id").row('.selected').eq(0).data());
    })
});
