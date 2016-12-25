var appPrefix = btoa('endpoint');
var login = localStorage.getItem(appPrefix + 'login')
var pass = localStorage.getItem(appPrefix + 'pass')
var table;

$(document).ready(function(){
    $("#home").click(function() {
        $.redirect('/endpoint/customer_dashboard.html');
    });

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
                    //document.getElementById("cur_balance").value = data.data.money;
                    $("#cur_balance").append(data.data.money);
                })
        });

    $.get({
        url: 'rest/get_plans',
        headers: {
            'Authorization': 'Basic ' + btoa(login + ':' + pass)
        }
    }).done(function(data) {
        //var json = $.parseJSON(data);

        var json = data;
        var dataSet = [];
        for(var i = 0; i < json.length; i++) {
            var obj = json[i];
            dataSet.push([obj.id, obj.data.name, obj.data.details, obj.data.maxSeats, obj.data.feePerUnit, obj.data.cost])
        }

        //$("#customer_list_id").html(data);
        table = $('#plan_list_id')
            .DataTable({
                data: dataSet,
                columns: [
                    { title: "UUID" },
                    { title: "Name" },
                    { title: "Details" },
                    { title: "Max seats" },
                    { title: "Fee per unit" },
                    { title: "Cost"}
                ]
            });
    });

    $('#plan_list_id tbody').on('click', 'tr', function () {
        if( $(this).hasClass('selected') ) {
            $(this).removeClass('selected');
        } else {
            table.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
    });

    $('#buy_plan').click( function () {
        alert($("#plan_list_id").row('.selected').eq(0).data());
    })
});
