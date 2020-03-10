let master_state = [], state = {}, master_city = {}, city = {}, all = {}, users = [], positions = [], addresses = [],
    service_tables = [];
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
            str += '<option value="' + state + '">' + state + '</option>';
        })
        $('#state').html(str);
        $('#state').change(function () {
            str = "<option value='0'>انتخاب</option>";
            $.each(master_city[master_state.indexOf($("#state")[0].value) + 1], function (i, city) {
                str += '<option value="' + city + '">' + city + '</option>';
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
            showToast("دریافت داده از سرور با خطا مواجه شد. \nلطفا صفحه مرورگر را refresh کنید.", 'red rounded');
        else {
            $.each(response, function (i, result) {
                user[result.user_id] = result;
                users.push(result.user_id);
                if (result.position != null && !positions.includes(result.position))
                    positions.push(result.position);
                if (result.address !== null && !addresses.includes(result.address))
                    addresses.push(result.address);
                if (result.service_table != null && !service_tables.includes(result.service_table))
                    service_tables.push(result.service_table);
            });
            $("#user_id").eq(0).on('keyup keydown change click', function () {
                let user0 = user[$("#user_id")[0].value];
                if (user0 === undefined)
                    clearAddUserItems(true);
                else {
                    if (user0.gender === "مرد")
                        $('#radio_male')[0].checked = true;
                    else
                        $('#radio_female')[0].checked = true;
                    $("#first_name")[0].value = user0.first_name;
                    $("#last_name")[0].value = user0.last_name;
                    $("#national_code")[0].value = user0.national_code;
                    $("#position")[0].value = user0.position;
                    $("#master_id")[0].value = user0.master_id;
                    $("#state").val(user0.state).change();
                    $("#city").val(user0.city).change();
                    $("#address")[0].value = user0.address;
                    $("#TableService")[0].value = user0.service_table;
                    $("#telephone")[0].value = user0.telephone;
                    $("#internalTel1")[0].value = user0.internalTel1;
                    $("#internalTel2")[0].value = user0.internalTel2;
                    $("#preIntTel")[0].value = user0.preIntTel;
                    $("#fax")[0].value = user0.fax;
                    $("#mobile")[0].value = user0.mobile;
                    $("#fullAddress")[0].value = user0.fullAddress;
                    document.getElementById('admin').checked = user0.type != null ? user0.type : false;
                }
            });
            pageCnt(12);
        }
    },
    error: function () {
        showToast("دریافت داده از سرور با خطا مواجه شد. \nلطفا صفحه مرورگر را refresh کنید.", 'red rounded');
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
                    removeUser($("#user_id")[0].value);
                    clearAddUserItems();
                    showToast('اطلاعات کاربری با موفقیت حذف شد.', 'green rounded');
                } else
                    showToast('حذف اطلاعات با خظا مواجه شده است.', 'red rounded');
            },
            error: function () {
                showToast('حذف اطلاعات با خظا مواجه شده است.', 'red rounded');
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
            'national_code': $("#national_code")[0].value,
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
            'mobile': $("#mobile")[0].value === '' ? '0' : $("#mobile")[0].value,
            'fullAddress': $("#fullAddress")[0].value,
            'type': document.getElementById('admin').checked,
        };
        if ($("#image_view")[0].value !== '')
            uploadFile(data.user_id);
        $.ajax({
            url: ("/spadsystem/rest/add_user"),
            type: "post",
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(data),
            success: function (response) {
                if (response) {
                    addUser(data);
                    showToast('اطلاعات کاربری با موفقیت ثبت شد.', 'green rounded');
                    clearAddUserItems();
                } else
                    showToast('ثبت اطلاعات با خظا مواجه شده است.', 'red rounded');
            },
            error: function () {
                showToast('ثبت اطلاعات با خظا مواجه شده است.', 'red rounded');
            }
        });
    });
});

function showToast(str, classes) {
    M.toast({html: str, classes: classes});
}

function addUser(data) {
    user[data.user_id] = data;
    if (users.indexOf(data.user_id) === -1) {
        users.push(data.user_id);
    }
    pageCnt(data);
}

function removeUser(id) {
    delete user[$("#user_id")[0].value];
    users = jQuery.grep(users, function (value) {
        return value !== id;
    });
    pageCnt(id);
}

function pageCnt(input) {
    $('#user_cnt').html(Object.keys(user).length);
    let m_cnt = 1, t_cnt = 0, it_cnt = 0;
    $.each(user, function (i, user0) {
        if (user0.type === "true")
            m_cnt++;
        if (user0.telephone.length > 1)
            t_cnt++;
        if (user0.internalTel1.length > 1)
            it_cnt++;
        if (user0.internalTel2.length > 1)
            it_cnt++;
    });
    $('#master_cnt').html(m_cnt);
    $('#tel_cnt').html(t_cnt);
    $('#int_tel_cnt').html(it_cnt);
    updateAutoCompletes(input);
}

function updateAutoCompletes(input) {
    if (typeof input === "object") {
        if (input.position != null && !positions.includes(input.position))
            positions.push(input.position != null ? input.position : "");
        if (input.address != null && !addresses.includes(input.address))
            addresses.push(input.address != null ? input.address : "");
        if (input.service_table != null && !service_tables.includes(input.service_table))
            service_tables.push(input.service_table != null ? input.service_table : "");
    }
    autocomplete(document.getElementById("user_id"), users);
    autocomplete(document.getElementById("position"), positions);
    autocomplete(document.getElementById("address"), addresses);
    autocomplete(document.getElementById("TableService"), service_tables);
}

function clearAddUserItems(type) {
    $('#radio_male')[0].checked = true;
    if (!type)
        $("#user_id")[0].value = "";
    $("#image_view")[0].value = "";
    $(".badge").html("");
    $("#first_name")[0].value = "";
    $("#last_name")[0].value = "";
    $("#national_code")[0].value = "";
    $("#position")[0].value = "";
    $("#master_id")[0].value = "";
    try {
        $("#state").val(0).change();
    } catch (e) {
    }
    try {
        $("#city").val(0).change();
    } catch (e) {
    }
    $("#address")[0].value = "";
    $("#TableService")[0].value = "";
    $("#telephone")[0].value = "";
    $("#internalTel1")[0].value = "";
    $("#internalTel2")[0].value = "";
    $("#preIntTel")[0].value = "";
    $("#fax")[0].value = "";
    $("#mobile")[0].value = "";
    $("#fullAddress")[0].value = "";
    document.getElementById("admin").checked = false;
}

function uploadFile(id) {
    var formData = new FormData();
    formData.append("file", $('#image_view')[0].files[0]);
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
            showToast("بارگذاری تصویر با خطا مواجه شد.", 'red rounded');
        }
    });
}

function autocomplete(inp, arr) {
    /*the autocomplete function takes two arguments,
    the text field element and an array of possible autocompleted values:*/
    var currentFocus;
    /*execute a function when someone writes in the text field:*/
    inp.addEventListener("input", function (e) {
        var a, b, i, val = this.value;
        /*close any already open lists of autocompleted values*/
        closeAllLists();
        if (!val) {
            return false;
        }
        currentFocus = -1;
        /*create a DIV element that will contain the items (values):*/
        a = document.createElement("DIV");
        a.setAttribute("id", this.id + "autocomplete-list");
        a.setAttribute("class", "autocomplete-items");
        a.setAttribute("style", "position: absolute;background-color:white;z-index: 1;width: 94%;");
        /*append the DIV element as a child of the autocomplete container:*/
        this.parentNode.appendChild(a);
        /*for each item in the array...*/
        for (i = 0; i < arr.length; i++) {
            /*check if the item starts with the same letters as the text field value:*/
            if (arr[i].substr(0, val.length) === val) {
                /*create a DIV element for each matching element:*/
                b = document.createElement("DIV");
                b.setAttribute("style", "margin: 5px;cursor: pointer;");
                /*make the matching letters bold:*/
                b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                b.innerHTML += arr[i].substr(val.length);
                /*insert a input field that will hold the current array item's value:*/
                b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
                /*execute a function when someone clicks on the item value (DIV element):*/
                b.addEventListener("click", function (e) {
                    /*insert the value for the autocomplete text field:*/
                    inp.value = this.getElementsByTagName("input")[0].value;
                    inp.click();
                    /*close the list of autocompleted values,
                    (or any other open lists of autocompleted values:*/
                    closeAllLists();
                });
                a.appendChild(b);
            }
        }
    });
    /*execute a function presses a key on the keyboard:*/
    inp.addEventListener("keydown", function (e) {
        var x = document.getElementById(this.id + "autocomplete-list");
        if (x) x = x.getElementsByTagName("div");
        if (e.keyCode == 40) {
            /*If the arrow DOWN key is pressed,
            increase the currentFocus variable:*/
            currentFocus++;
            /*and and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode == 38) { //up
            /*If the arrow UP key is pressed,
            decrease the currentFocus variable:*/
            currentFocus--;
            /*and and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode == 13) {
            /*If the ENTER key is pressed, prevent the form from being submitted,*/
            e.preventDefault();
            if (currentFocus > -1) {
                /*and simulate a click on the "active" item:*/
                if (x) x[currentFocus].click();
            }
        }
    });

    function addActive(x) {
        /*a function to classify an item as "active":*/
        if (!x) return false;
        /*start by removing the "active" class on all items:*/
        removeActive(x);
        if (currentFocus >= x.length) currentFocus = 0;
        if (currentFocus < 0) currentFocus = (x.length - 1);
        /*add class "autocomplete-active":*/
        x[currentFocus].classList.add("autocomplete-active");
    }

    function removeActive(x) {
        /*a function to remove the "active" class from all autocomplete items:*/
        for (var i = 0; i < x.length; i++) {
            x[i].classList.remove("autocomplete-active");
        }
    }

    function closeAllLists(elmnt) {
        /*close all autocomplete lists in the document,
        except the one passed as an argument:*/
        var x = document.getElementsByClassName("autocomplete-items");
        for (var i = 0; i < x.length; i++) {
            if (elmnt != x[i] && elmnt != inp) {
                x[i].parentNode.removeChild(x[i]);
            }
        }
    }

    /*execute a function when someone clicks in the document:*/
    document.addEventListener("click", function (e) {
        closeAllLists(e.target);
    });


}