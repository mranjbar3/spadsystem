let master_state = [], state = {}, master_city = {}, city = {}, all = {};
$.ajax({
    url: "/spadsystem/rest/first_data",
    type: "GET",
    success: function (response) {
        $.each(response, function (i, item) {
            if (i < 31) {
                master_state.push(item);
                master_city[i + 1] = [];
            } else {
                if (item.indexOf('pos:') === -1) {
                    master_city[item.split(',')[1]].push(item.split(',')[0]);
                } else {
                    all['سمت ' + item.replace('pos:', '')] = null;
                }
            }
        });
        let str = "<option value='0'>انتخاب</option>";
        $.each(master_state, function (i, state) {
            str += '<option value="' + (i + 1) + '">' + state + '</option>';
        })
        $('#state').html(str);
        $('#state').change(function () {
            str = "<option value='0'>انتخاب</option>";
            $.each(master_city[$('#state')[0].value], function (i, city) {
                str += '<option value="' + (i + 1) + '">' + city + '</option>';
            })
            $('#city').html(str);
        });
    },
    error: function (response) {
        console.log(response);
    }
});

let user = {};
$.ajax({
    url: "/spadsystem/rest/search",
    type: "post",
    contentType: "application/json; charset=UTF-8",
    data: JSON.stringify({data: "all"}),
    success: function (response) {
        if (response === null)
            alert("پایگاه داده مشکل پیدا کرده است.");
        else {
            $.each(response, function (i, result) {
                user[result.user_id] = result;
            });
            $("#user_id").eq(0).on('keyup keydown change', function () {
                let user0 = user[$("#user_id")[0].value];
                if (user0 === undefined)
                    clearAddUserItems();
                else {
                    if (user0.gender === "مرد")
                        $('#radio_male')[0].checked = true;
                    else
                        $('#radio_female')[0].checked = true;
                    $("#first_name")[0].value = user0.first_name;
                    $("#last_name")[0].value = user0.last_name;
                    $("#position")[0].value = user0.position;
                    $("#master_id")[0].value = user0.master_id;
                    $("#state")[0].value = user0.state;
                    $("#city")[0].value = user0.city;
                    $("#address")[0].value = user0.address;
                    $("#TableService")[0].value = user0.service_table;
                    $("#telephone")[0].value = user0.telephone;
                    $("#internalTel1")[0].value = user0.internalTel1;
                    $("#internalTel2")[0].value = user0.internalTel2;
                    $("#preIntTel")[0].value = user0.preIntTel;
                    $("#fax")[0].value = user0.fax;
                }
            });
            $('#user_cnt').html(Object.keys(user).length);
            let m_cnt = 1, t_cnt = 0, it_cnt = 0;
            $.each(user, function (i, user0) {
                if (user0.type === "admin")
                    m_cnt++;
                if (user0.telephone.length > 1)
                    t_cnt++;
                if (user0.internalTel1.length > 1)
                    it_cnt++;
                if (user0.internalTel2.length > 1)
                    it_cnt++;
            })
            $('#master_cnt').html(m_cnt);
            $('#tel_cnt').html(t_cnt);
            $('#int_tel_cnt').html(it_cnt);
        }
    },
    error: function () {
        alert("سرور با مشکل مواجه شده است. لطفا بعدا تلاش نمایید.");
    }
});
$(document).ready(function () {
    $("#cancel_btn").click(function () {
        clearAddUserItems();
    });
    $("#remove_btn").click(function () {
        $.ajax({
            url: ("/spadsystem/rest/delete_user"),
            type: "post",
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify({"user_id": $("#user_id")[0].value}),
            success: function (response) {
                if (response) {
                    delete user[$("#user_id")[0].value];
                    clearAddUserItems();
                    alert('اطلاعات کاربری با موفقیت حذف شد.');
                } else
                    alert('حذف اطلاعات با خظا مواجه شده است.');
            },
            error: function () {
                alert('سرور با مشکل مواجه شده است. لطفا بعدا امتحان نمایید.');
            }
        });
    });
    $("#add_user_btn").click(function () {
        data = {
            'gender': $('#radio_male')[0].checked === true ? 'مرد' : 'زن',
            'user_id': $("#user_id")[0].value,
            'password': $("#user_id")[0].value,
            'first_name': $("#first_name")[0].value,
            'last_name': $("#last_name")[0].value,
            'position': $("#position")[0].value,
            'master_id': $("#master_id")[0].value !== '' ? $("#master_id")[0].value : '16288831',
            'state': $("#state")[0].value,
            'city': $("#city")[0].value,
            'address': $("#address")[0].value,
            'service_table': $("#TableService")[0].value,
            'telephone': $("#telephone")[0].value === '' ? '0' : $("#telephone")[0].value,
            'internalTel1': $("#internalTel1")[0].value === '' ? '0' : $("#internalTel1")[0].value,
            'internalTel2': $("#internalTel2")[0].value === '' ? '0' : $("#internalTel2")[0].value,
            'preIntTel': $("#preIntTel")[0].value === '' ? '0' : $("#preIntTel")[0].value,
            'fax': $("#fax")[0].value === '' ? '0' : $("#fax")[0].value,
        };
        // if (localStorage.type === "user" && $("#image_view")[0].value !== '')
        //     uploadFile(data.user_id);
        $.ajax({
            url: ("/spadsystem/rest/add_user"),
            type: "post",
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(data),
            success: function (response) {
                if (response)
                    alert('اطلاعات کاربری با موفقیت ثبت شد.');
                else
                    alert('ثبت اطلاعات با خظا مواجه شده است.');
            },
            error: function () {
                alert('سرور با مشکل مواجه شده است. لطفا بعدا امتحان نمایید.');
            }
        });
    });
});

function clearAddUserItems() {
    $('#radio_male')[0].checked = true;
    $("#user_id")[0].value = "";
    // $("#image_view")[0].value = "";
    $("#first_name")[0].value = "";
    $("#last_name")[0].value = "";
    $("#position")[0].value = "";
    $("#master_id")[0].value = "";
    $("#state").val(0).change();
    $("#city").val(0).change();
    $("#address")[0].value = "";
    $("#TableService")[0].value = "";
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
                alert("بارگذاری تصویر با خطا مواجه شد.");
        },
        error: function () {
            alert("سرور با مشکل مواجه شده است. لطفا بعدا تلاش نمایید.");
        }
    });
}
