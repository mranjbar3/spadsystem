var search_chips, master_state = [], state = {}, master_city = {}, city = {}, all = {};
$.ajax({
    url: "/spadsystem/rest/first_data",
    type: "GET",
    success: function (response) {
        $.each(response, function (i, item) {
            if (i < 31) {
                master_state.push(item);
                state[item] = null;
                all['استان ' + item] = null;
                master_city[i + 1] = [];
            } else {
                if (item.indexOf('pos:') === -1) {
                    master_city[item.split(',')[1]].push(item.split(',')[0]);
                    city[item.split(',')[0]] = null;
                    all['شهر ' + item.split(',')[0]] = null;
                } else {
                    all['سمت ' + item.replace('pos:', '')] = null;
                }
            }
        });
        if (localStorage.type === "user")
            $('#search_bar').autocomplete({data: all, limit: 4});
    },
    error: function (response) {
        console.log(response);
    }
});

let user = {};

if (localStorage.type === "admin")
    $.ajax({
        url: "/spadsystem/rest/search",
        type: "post",
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify({data: "all"}),
        success: function (response) {
            if (response === null)
                showToast("پایگاه داده مشکل پیدا کرده است.", "red rounded");
            else {
                let ids = {};
                $.each(response, function (i, result) {
                    user[result.user_id] = result;
                    ids[result.user_id] = null;
                });
                $("#user_id").autocomplete({data: ids, limit: 4});
                $("#user_id").eq(0).on('keyup keydown change', function () {
                    let user0 = user[$("#user_id")[0].value];
                    if (user0 === undefined)
                        clearAddUserItems(false);
                    else {
                        $("#first_name")[0].value = user0.first_name;
                        $("#last_name")[0].value = user0.last_name;
                        $("#position")[0].value = user0.position;
                        $("#master_id")[0].value = user0.master_id;
                        $("#state")[0].value = user0.state;
                        $("#city")[0].value = user0.city;
                        $("#address")[0].value = user0.address;
                        $("#telephone")[0].value = user0.telephone;
                        $("#internalTel1")[0].value = user0.internalTel1;
                        $("#internalTel2")[0].value = user0.internalTel2;
                        $("#preIntTel")[0].value = user0.preIntTel;
                        $("#fax")[0].value = user0.fax;
                    }
                });
            }
        },
        error: function (response) {
            showToast("سرور با مشکل مواجه شده است. لطفا بعدا تلاش نمایید.", 'red rounded');
        }
    });
$(document).ready(function () {
    localStorage.id = new URLSearchParams(window.location.search).get('id');
    if (localStorage.type === "user") {
        $('.chips').chips({
            //placeholder: 'یک عبارت بنویس و بزن روی اینتر',
            onChipDelete: function () {
                checkInput();
            }
        });
        search_chips = M.Chips.getInstance($('.chips'));
        $('#search_bar').keydown(function (event) {
            var keyCode = (event.keyCode ? event.keyCode : event.which);
            if (keyCode === 13) checkInput();
        });
        $(".btn-floating").off();
        $(".btn-floating").click(function () {
            hideAll();
            $("#add_user").show();
        });
        user["user_id"] = localStorage.id;
        $.ajax({
            url: "/spadsystem/rest/get_user",
            type: "post",
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(user),
            success: function (response) {
                if (response === null)
                    showToast("پایگاه داده مشکل پیدا کرده است.", "red rounded");
                else {
                    $("#user_id")[0].value = response.user_id;
                    $("#password")[0].value = response.password;
                    $("#image_view")[0].value = response.image;
                    $("#first_name")[0].value = response.first_name;
                    $("#last_name")[0].value = response.last_name;
                    $("#position")[0].value = response.position;
                    $("#master_id")[0].value = response.master_id;
                    $("#state")[0].value = response.state;
                    $("#city")[0].value = response.city;
                    $("#address")[0].value = response.address;
                    $("#telephone")[0].value = response.telephone;
                    $("#internalTel1")[0].value = response.internalTel1;
                    $("#internalTel2")[0].value = response.internalTel2;
                    $("#preIntTel")[0].value = response.preIntTel;
                    $("#fax")[0].value = response.fax;
                    if (response.image != null)
                        $(".btn-floating img").eq(0).attr("src", '/spadsystem' +
                            response.image.substring(response.image.indexOf('/temp')));
                }
            },
            error: function (response) {
                showToast("سرور با مشکل مواجه شده است. لطفا بعدا تلاش نمایید.", 'red rounded');
            }
        });
    } else {
        $("#add_user").show();
        $("#new_user").click(function () {
            clearAddUserItems(true);
        });
    }
    $('#state').autocomplete({data: state, limit: 4});
    $('#city').focusin(function () {
        var cities = {};
        $.each(master_city[master_state.indexOf($('#state')[0].value) + 1], function (i, city) {
            cities[city] = null;
        });
        $('#city').autocomplete({data: cities, limit: 4});
    });
    $("#cancel_btn").click(function () {
        if (localStorage.type === "user") {
            hideAll();
            showMain();
        } else {
            $.ajax({
                url: ("/spadsystem/rest/delete_user"),
                type: "post",
                contentType: "application/json; charset=UTF-8",
                data: JSON.stringify({"user_id": $("#user_id")[0].value}),
                success: function (response) {
                    if (response) {
                        delete user[$("#user_id")[0].value];
                        clearAddUserItems(true);
                        showToast('اطلاعات کاربری با موفقیت حذف شد.', 'green rounded');
                    } else
                        showToast('حذف اطلاعات با خظا مواجه شده است.', 'red rounded');
                },
                error: function () {
                    showToast('سرور با مشکل مواجه شده است. لطفا بعدا امتحان نمایید.', 'red rounded');
                }
            });
        }
    });
    $('.btn-signin').click(function () {

    });
    // $("#add_user_btn").click(function () {
    //     if (localStorage.type === "admin")
    //         localStorage.id = $("#user_id")[0].value;
    //     data = {
    //         'user_id': localStorage.id,
    //         'password': localStorage.type === "admin" ? localStorage.id : $("#password")[0].value,
    //         'first_name': $("#first_name")[0].value,
    //         'last_name': $("#last_name")[0].value,
    //         'position': $("#position")[0].value,
    //         'master_id': $("#master_id")[0].value !== '' ? $("#master_id")[0].value : '16288831',
    //         'state': $("#state")[0].value,
    //         'city': $("#city")[0].value,
    //         'address': $("#address")[0].value,
    //         'telephone': $("#telephone")[0].value === '' ? '0' : $("#telephone")[0].value,
    //         'internalTel1': $("#internalTel1")[0].value === '' ? '0' : $("#internalTel1")[0].value,
    //         'internalTel2': $("#internalTel2")[0].value === '' ? '0' : $("#internalTel2")[0].value,
    //         'preIntTel': $("#preIntTel")[0].value === '' ? '0' : $("#preIntTel")[0].value,
    //         'fax': $("#fax")[0].value === '' ? '0' : $("#fax")[0].value,
    //     };
    //     if (localStorage.type === "user" && $("#image_view")[0].value !== '')
    //         uploadFile(data.user_id);
    //     $.ajax({
    //         url: ("/spadsystem/rest/add_user"),
    //         type: "post",
    //         contentType: "application/json; charset=UTF-8",
    //         data: JSON.stringify(data),
    //         success: function (response) {
    //             if (response)
    //                 showToast('اطلاعات کاربری با موفقیت ثبت شد.', 'green rounded');
    //             else
    //                 showToast('ثبت اطلاعات با خظا مواجه شده است.', 'red rounded');
    //         },
    //         error: function () {
    //             showToast('سرور با مشکل مواجه شده است. لطفا بعدا امتحان نمایید.', 'red rounded');
    //         }
    //     });
    // });
});

function hideAll() {
    $("#add_user").hide();
    $(".btn-floating").hide();
    $(".section").hide();
    $(".container").hide();
}

function checkInput() {
    $('.chips').css({"-webkit-transform": "translate(0,-14rem)"});
    $('.main_master p').hide();
    $(".main_master img").css('transform', 'scale(0.5) translate(85vw,-7rem)')
    let str = '';
    for (let i = 0; i < $('.chip').length; i++) {
        str += $('.chip').eq(i).html().split('<i')[0] + ',';
    }
    var data = {data: str};
    $.ajax({
        url: "/spadsystem/rest/search",
        type: "post",
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(data),
        success: function (response) {
            if (response === null)
                showToast("پایگاه داده مشکل پیدا کرده است.", "red rounded");
            else {
                var str = "";
                $.each(response, function (i, result) {
                    str += "<div class='col s3'>" +
                        "<div class='card'>" +
                        "<div class='card-image waves-effect waves-block waves-light'>" +
                        "<img class='activator'  src=\"" + (result.image === null ? "/spadsystem/image/avatar.jpg" : result.image.substring(result.image.indexOf('/temp'))) + "\">" +
                        "</div>" +
                        "<div class='card-content'>" +
                        "<span class='card-title activator grey-text text-darken-4'>" + result.first_name + " " + result.last_name + " - استان:" + (result.state === "" ? "نامشخص" : (result.state + " - شهر: " + result.city === "" ? "نامشخص" : result.city)) + " - میز خدمت: " + result.service_table + "<i class='material-icons right'>more_vert</i></span>" +
                        "</div>" +
                        "<div class='card-reveal'>" +
                        "<span class='card-title grey-text text-darken-4'><i class='material-icons right'>close</i></span>" +
                        "<h4>" + result.first_name + " " + result.last_name + "</h4>" +
                        "<p class='cardtitle'><i class='fa fa-circle'></i><b>شناسه کاربری: </b>" + result.user_id + "</p>" +
                        "<p class='cardtitle'><i class='fa fa-circle'></i><b>سمت سازمانی: </b>" + result.position + "</p>" +
                        "<p class='cardtitle'><i class='fa fa-circle'></i><b>محل خدمت(استان): </b>" + result.state + "</p>" +
                        "<p class='cardtitle'><i class='fa fa-circle'></i><b>محل خدمت:(شهر): </b>" + result.city + "</p>" +
                        "<p class='cardtitle'><i class='fa fa-circle'></i><b>نشانی محل خدمت: </b>" + result.address + "</p>" +
                        "<p class='cardtitle'><i class='fa fa-circle'></i><b>میز خدمت: </b>" + result.service_table + "</p>" +
                        "<p class='cardtitle'><i class='fa fa-circle'></i><b>شماره ثابت: </b>" + result.telephone + "</p>" +
                        "<p class='cardtitle'><i class='fa fa-circle'></i><b>شماره داخلی: </b>" + result.internalTel1 + "</p>" +
                        "<p class='cardtitle'><i class='fa fa-circle'></i><b>شماره داخلی 2: </b>" + result.internalTel2 + "</p>" +
                        "<p class='cardtitle'><i class='fa fa-circle'></i><b>پیش شماره: </b>" + result.preIntTel + "</p>" +
                        "<p class='cardtitle'><i class='fa fa-circle'></i><b>شماره دورنگار: </b>" + result.fax + "</p>" +
                        "</div>" +
                        "</div>" +
                        "</div >";


                    //str += "<div class='card row'><div class='col s1'><img class=\"materialboxed\" " +
                    //    "data-caption=\"" + result.first_name + " " + result.last_name + "\" src=\"" +
                    //    (result.image == null ? "/spadsystem/image/avatar.jpg" : result.image.substring(result.image.indexOf('/temp'))) +
                    //    "\" style='margin: 10px auto;" +
                    //    "max-height: 100px;width: auto;'></div><div class='col s2' " +
                    //    "style='text-align: right;'>" + result.first_name + " " + result.last_name +
                    //    "</div><div class='col s2' " +
                    //    "style='text-align: right;'>سمت:<br>" + result.position;
                    //if (result.state !== "")
                    //    str += "</div><div class='col s2' " +
                    //        "style='text-align: right;'>موقعیت مکانی:<br> استان " + result.state + " شهر " + (result.city === "" ? "نامشخص" : result.city);
                    //str += "</div><div class='col s2' " +
                    //    "style='text-align: right;'>محل خدمت:<br>" + result.address + "</div>";
                    //if (result.telephone !== "0")
                    //    str += "<div class='col s2' " +
                    //        "style='text-align: right;'>به شماره تماس: " + (result.state === "" ? "" : result.preTel) + result.telephone + "</div>";
                    //if (result.internalTel1 !== "0")
                    //    str += "<div class='col s2' " +
                    //        "style='text-align: right;'>شماره داخلی: " + result.internalTel1 + (result.internalTel2 === "0" ? "" : " و " +
                    //            result.internalTel2 + "به پیش شماره " + (result.state === "" ? "" : result.preTel) + result.preIntTel) + "</div>";
                    //str += "</div></div>";
                });
                $("#results").html(str);
                //$('.materialboxed').materialbox();
            }
        },
        error: function (response) {
            showToast("سرور با مشکل مواجه شده است. لطفا بعدا تلاش نمایید.", 'red rounded');
        }
    });
}

function showMain() {
    $(".btn-floating").show();
    $(".section").show();
    $(".container").show();
}

function showToast(str, classes) {
    M.toast({html: str, classes: classes});
}

function clearAddUserItems(admin) {
    if (admin)
        $("#user_id")[0].value = "";
    // $("#image_view")[0].value = "";
    $("#first_name")[0].value = "";
    $("#last_name")[0].value = "";
    $("#position")[0].value = "";
    $("#master_id")[0].value = "";
    $("#state")[0].value = "";
    $("#city")[0].value = "";
    $("#address")[0].value = "";
    $("#telephone")[0].value = "";
    $("#internalTel1")[0].value = "";
    $("#internalTel2")[0].value = "";
    $("#preIntTel")[0].value = "";
    $("#fax")[0].value = "";
}

function uploadFile(id) {
    var formData = new FormData();
    formData.append("file", $('#image')[0].files[0]);
    formData.append("id", id);

    $.ajax({
        url: "/spadsystem/rest/image",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            if (response === "Error")
                showToast("بارگذاری تصویر با خطا مواجه شد.", 'red rounded');
        },
        error: function () {
            showToast("سرور با مشکل مواجه شده است. لطفا بعدا تلاش نمایید.", 'red rounded');
        }
    });
}
