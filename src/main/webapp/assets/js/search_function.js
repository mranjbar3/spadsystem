let master_state = [], state = {}, master_city = {}, city = {}, all = {}, search_response;
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
    $('.btn-signin').click(function () {
        $(window).attr('location', 'login.html')
    });
    if (localStorage.id.length > 4) {
        $('.btn-signin').html("خروج");
        $('.btn-signin').off();
        $('.btn-signin').click(function () {
            window.location.search = "";
        })
    }
    if (localStorage.type === "user") {
        $('#search_bar').keydown(function (event) {
            var keyCode = (event.keyCode ? event.keyCode : event.which);
            console.log(keyCode);
            if (keyCode === 13) checkInput();
        });
        user["user_id"] = localStorage.id;
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
});

function hideAll() {
    $("#add_user").hide();
    $(".btn-floating").hide();
    $(".section").hide();
    $(".container").hide();
}

function checkInput() {
    $.ajax({
        url: "/spadsystem/rest/search",
        type: "post",
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify({data: $('#search_bar').val()}),
        success: function (response) {
            if (response === null)
                showToast("پایگاه داده مشکل پیدا کرده است.", "red rounded");
            else {
                $('#results').html('');
                if (response.length > 0) {
                    search_response = response;
                    showSearchResult(response, 0, 12);
                } else {
                    showToast("موردی با این عنوان یافت نشد.", "green rounded");
                }
            }
        },
        error: function () {
            showToast("سرور با مشکل مواجه شده است. لطفا بعدا تلاش نمایید.", 'red rounded');
        }
    });
}

function showSearchResult(results, start, cnt) {
    let str = $("#results").html();
    for (let i = start; i < Math.min(start + cnt, results.length); i++) {
        const result = results[i];
        str += "<div class='search_res col m6 s12 l2'>" +
            "<div class='card'>" +
            "<span class='card-title grey-text text-darken-4' onclick='closeDiv(" + i + ")' style='cursor: pointer;'><i class='material-icons right' style='#2c3e50'>close</i></span>" +
            "<br><div class='card-image waves-effect waves-block waves-light'>" +
            "<img class='activator'  src=" + (result.image === null ? (result.gender === 'مرد' ? "/spadsystem/assets/image/man.png" : "/spadsystem/assets/image/woman.png") : result.image.substring(result.image.indexOf('temp'))) + ">" +
            "</div>" +
            "<div class='card-content'>" +
            "<span class='card-title activator grey-text text-darken-4'><i class='fa fa-user'></i><b> نام و نام خانوادگی: </b>" +
            result.first_name + " " + result.last_name + "<br/><i class='fa fa-map'></i><b> واحد سازمانی: </b>" +
            (result.address === null ? "نامشخص" : result.address) + " <br/><i class='fa fa-hand-pointer-o'></i><b> واحد سازمانی: </b>" +
            (result.service_unit === null ? "نامشخص" : result.service_unit) + " <br/><i class='fa fa-flag'></i><b> میز خدمت: </b>" +
            (result.service_table === null ? '' : result.service_table) + "<br/><i class='fa fa-tty'></i><b> تلفن ثابت:</b><span style='float: left;margin: 6px 10px;'>" +
            (result.telephone === null ? '' : (result.preTel === null ? '' : result.preTel + '-') + result.telephone) + "</span></span>" +
            "</div>" +
            "<div class='card-reveal'>" +
            "<span class='card-title grey-text text-darken-4'><i class='material-icons right' style='color:#ffffff'>close</i></span>" +
            "<h4>" + result.first_name + " " + result.last_name + "</h4>" +
            "<p class='cardtitle'><i class='fa fa-list'></i><b>سمت سازمانی: </b>" + (result.position === null ? '' : result.position) + "</p>" +
            "<p class='cardtitle'><i class='fa fa-map'></i><b>محل خدمت(استان): </b>" + (result.state === null ? '' : result.state) + "</p>" +
            "<p class='cardtitle'><i class='fa fa-road'></i><b>محل خدمت:(شهر): </b>" + (result.city === null ? '' : result.city) + "</p>" +
            "<p class='cardtitle'><i class='fa fa-map-pin'></i><b>نشانی محل خدمت: </b>" + (result.address === null ? '' : result.address) + "</p>" +
            "<p class='cardtitle'><i class='fa fa-flag'></i><b>میز خدمت: </b>" + (result.service_table === null ? '' : result.service_table) + "</p>" +
            "<p class='cardtitle'><i class='fa fa-tty'></i><b>شماره ثابت: </b><span style='float: left;margin: 6px 10px;'>" + (result.telephone === null ? '' : (result.preTel === null ? '' : result.preTel + '-') + result.telephone) + "</span></p>" +
            "<p class='cardtitle'><i class='fa fa-phone'></i><b>شماره داخلی 1: </b>" + (result.internalTel1 === null ? '' : result.internalTel1) + "</p>" +
            "<p class='cardtitle'><i class='fa fa-phone'></i><b>شماره داخلی 2: </b>" + (result.internalTel2 === null ? '' : result.internalTel2) + "</p>" +
            "<p class='cardtitle'><i class='fa fa-list-ol'></i><b>پیش شماره: </b>" + (result.preIntTel === null ? '' : result.preIntTel) + "</p>" +
            "<p class='cardtitle'><i class='fa fa-fax'></i><b>شماره دورنگار: </b>" + (result.fax === null ? '' : result.fax) + "</p>" +
            "</div>" +
            "</div>" +
            "</div >";
    }
    $("#results").html(str);
    $(window).off();
    if (results.length > start + cnt)
        $(window).scroll(function () {
            if ($(window).scrollTop() >= $(document).height() - $(window).height() - 10) {
                showSearchResult(results, start + cnt, cnt);
            }
        });
}

function closeDiv(id) {
    $('.search_res').eq(id).hide();
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

/*Scroll to top when arrow up clicked BEGIN*/
$(window).scroll(function () {
    var height = $(window).scrollTop();
    if (height > 100) {
        $('#back2Top').fadeIn();
    } else {
        $('#back2Top').fadeOut();
    }
});
$(document).ready(function () {
    $("#back2Top").click(function (event) {
        event.preventDefault();
        $("html, body").animate({scrollTop: 0}, "slow");
        return false;
    });

});
/*Scroll to top when arrow up clicked BEGIN*/
$(window).scroll(function () {
    var height = $(window).scrollTop();
    if (height > 100) {
        $('#back2Top').fadeIn();
    } else {
        $('#back2Top').fadeOut();
    }
});
$(document).ready(function () {
    $("#back2Top").click(function (event) {
        event.preventDefault();
        $("html, body").animate({scrollTop: 0}, "slow");
        return false;
    });

});
/*Scroll to top when arrow up clicked END*/