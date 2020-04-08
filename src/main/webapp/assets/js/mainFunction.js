//define global variable
let all_mails = [], unread_receive_message, unread_star_message, unread_trash_message;
//for ajax calling...
let ajax_url, ajax_method, ajax_content_type, ajax_data;

// hide all view for showing new view
function hideAll() {
    $("#admin").hide();
    $("#employee").hide();
    $("#profile").hide();
    $("#mail-part").hide();
}

//if want to view employee and update them or add new one, call this.
function showEmployeeView() {
    hideAll();
    $("#employee").show();
}

//main dashboard view on site by calling this function
function showDashboardView() {
    hideAll();
    $("#admin").show();
}

//show profile information view
function showProfileView() {
    hideAll();
    $('#profile').show();
}

function mailHide() {
    $("#mail-inbox").hide();
    $("#receive_btn").removeClass("active");
    $("#mail-detail").hide();
    $("#mail-send").hide();
    $("#sent_btn").removeClass("active");
    $("#mail-sent").hide();
    $("#mail-star").hide();
    $("#star_btn").removeClass("active");
    $("#mail-trash").hide();
    $("#trash_btn").removeClass("active");
}

//when you need to send a new mail, call this function.
function showNewMailView(clean) {
    if (clean) {
        $('#new_mail_receiver').val("");
        $('#new_mail_title').val("");
        $('#new_mail_body').val("");
    } else {
        mailHide();
        hideAll();
        $('#mail-part').show();
        $("#mail-sent").show();
    }
}

//when you want to show mail inbox, call this function.
function showInboxMailView() {
    mailHide();
    hideAll();
    $('#mail-part').show();
    $("#mail-inbox").show();
    $("#receive_btn").addClass("active");
}

//when you want to view your send mail, call this function.
function showSendMailView() {
    mailHide();
    hideAll();
    $('#mail-part').show();
    $("#mail-send").show();
    $("#sent_btn").addClass("active");
}

//star mails show in this function.
function showStarMailView() {
    mailHide();
    hideAll();
    $('#mail-part').show();
    $("#mail-star").show();
    $("#star_btn").addClass("active");
}

//this function show mails that in trash.
function showTrashView() {
    mailHide();
    hideAll();
    $('#mail-part').show();
    $('#mail-trash').show();
    $("#trash_btn").addClass("active");
}

//detail of one mail show!
function showMailDetailView() {
    mailHide();
    hideAll();
    $('#mail-part').show();
    $('#mail-detail').show();
}

//when completely load page, this part of code is running.
$(document).ready(function () {
    localStorage.id = new URLSearchParams(window.location.search).get('id');
    getEmails(localStorage.id);
});

function ajaxMasterFunction(type) {
    $.ajax({
        url: ajax_url,
        type: ajax_method,
        contentType: ajax_content_type,
        data: ajax_data,
        success: function (response) {
            switch (type) {
                case "G_Mail":
                    all_mails = response;
                    showAllMail(response);
                    break;
                case "S_Mail":
                    if (response === null) {
                        showToast('لطفا دوباره تلاش نمایید.', 'green rounded');
                    } else if (response.pk === null) {
                        showToast('پیام ارسال نشد. لطفا دوباره تلاش کنید.', 'green rounded');
                    } else {
                        showToast('پیام ارسال شد.', 'green rounded');
                        showNewMailView(true);
                        addSendMail(response);
                    }
                    break;
                case "U_Mail":
                    break;
            }
        },
        error: function () {
            showToast('سرور دچار مشکل شده است.', 'red rounded');
        }
    });
}

function getEmails(id) {
    ajax_url = "/spadsystem/rest/get_mail";
    ajax_method = "POST";
    ajax_content_type = "application/json";
    ajax_data = JSON.stringify({"receiver": {user_id: id}});
    ajaxMasterFunction("G_Mail");
}

function showAllMail(mails) {
    produceMailViewString(mails);
    unread_trash_message = $('#trash_mails .unread').length;
    unread_star_message = $('#star_mails .unread').length;
    checkReceiveMessages();
    if (unread_trash_message > 0) {
        $('#trash_btn b').eq(0).html("(" + unread_trash_message + ")");
    }
    if (unread_star_message > 0) {
        $('#star_btn b').eq(0).html("(" + unread_star_message + ")");
    }
    showLastSendingMail()
}

function checkReceiveMessages(cnt) {
    const rightLabel = $('.label-success').eq(0), topLabel = $('.badge-danger').eq(0),
        topLabelNew = $('.notifi-title .pull-right').eq(0);
    if (cnt === undefined)
        unread_receive_message = $('#receive_mails .unread').length;
    if (unread_receive_message > 0) {
        rightLabel.show();
        rightLabel.html(unread_receive_message);
        topLabel.show();
        topLabel.html(unread_receive_message);
        topLabelNew.show();
        topLabelNew.html("جدید " + unread_receive_message);
        $('#receive_btn b').eq(0).html("(" + unread_receive_message + ")");
    } else {
        rightLabel.hide();
        topLabel.hide();
        topLabelNew.hide();
        $('#receive_btn b').eq(0).html("");
    }
}

function produceMailViewString(mails, type_str) {
    $.each(mails, function (i, mail) {
        let str = '<tr id="' + mail.pk + '"';
        if (!mail.read) {
            str += ' class="unread"';
            if (mail.sender.user_id !== localStorage.id)
                $('.slimscroll-noti').eq(0).prepend("<a href=\"#\" onclick=\"showMailDetail(" + mail.pk + ")\" class=\"list-group-item\">" +
                    " <div class=\"media\">" +
                    "  <div class=\"pull-left p-r-10 profile \">" +
                    "   <img src=\"assets/images/users/avatar-1.jpg\" class=\"img-circle\"/>" +
                    "  </div>" +
                    "  <div class=\"media-body\">" +
                    "   <h5 class=\"media-heading\">" + mail.sender.first_name + " " + mail.sender.last_name + "</h5>" +
                    "   <p class=\"m-0\">" +
                    "    <small>10 ساعت پیش</small>" +
                    "   </p>" +
                    "  </div>" +
                    " </div>" +
                    "</a>");
        }
        str += '><td class="mail-select"><div class="checkbox checkbox-primary m-r-15">' +
            '<input type="checkbox"><label></label>' +
            '</div><i onclick="mailStarChange(this)" class="fa fa-star m-r-15 text-' + (mail.star ? 'warning' : 'muted') + '"></i></td>' +
            '<td><a href="#" onclick="showMailDetail(' + mail.pk + ')" class="email-name">' +
            (mail.sender.user_id === localStorage.id ? (mail.receiver.first_name + ' ' + mail.receiver.last_name) : (mail.sender.first_name + ' ' + mail.sender.last_name)) + '</a></td>' +
            '<td class="hidden-xs"><a href="#" onclick="showMailDetail(' + mail.pk + ',)" class="email-msg">' +
            mail.title + '</a></td>' +
            (mail.attach == null ? '' : '<td style="width: 20px;"><i class="fa fa-paperclip"></i></td>') +
            '<td class="text-right">' + mail.time + '</td></tr>';

        if (mail.trash) {
            $('#trash_mails').prepend(str);
        } else {
            if (mail.receiver.user_id === localStorage.id) {
                $('#receive_mails').prepend(str);
            } else if (mail.sender.user_id === localStorage.id) {
                $('#sent_mails').prepend(str);
            }
            if (mail.star) {
                $('#star_mails').prepend(str);
            }
        }
    });
}


function showToast(str, classes) {
    M.toast({html: str, classes: classes});
}

function showMailDetail(id) {
    showMailDetailView();
    let mail = all_mails.find(element => element.pk === id);
    $('#detail_mail_title').html(mail.title);
    $('#detail_mail_time').html(mail.time);
    $('#detail_mail_name').html(mail.sender.user_id === localStorage.id ? (mail.receiver.first_name + ' ' + mail.receiver.last_name) : (mail.sender.first_name + ' ' + mail.sender.last_name));
    $('#detail_mail_position').html(mail.sender.user_id === localStorage.id ? mail.receiver.position : mail.sender.position);
    $('#detail_mail_body').html(mail.body);
    localStorage.active_object = JSON.stringify(mail);
    if (mail.sender.user_id !== localStorage.id && !mail.read) {
        mail.send = true;
        mail.read = true;
        updateMail(mail);
        $('#' + mail.pk).removeClass("unread");
        if (mail.star) {
            unread_star_message--;
            $("#star_btn b").eq(0).html(unread_star_message === 1 ? "" : "(" + unread_star_message + ")");
        }
        if (mail.trash) {
            unread_trash_message--;
            $("#trash_btn b").eq(0).html(unread_trash_message === 1 ? "" : "(" + unread_trash_message + ")");
        } else if (mail.receiver.user_id === localStorage.id) {
            unread_receive_message--;
            checkReceiveMessages(unread_receive_message);
        }
    }
}

function updateMail(mail) {
    ajax_url = "/spadsystem/rest/update_mail";
    ajax_method = "POST";
    ajax_content_type = "application/json";
    ajax_data = JSON.stringify({
        "pk": mail.pk,
        "title": mail.title,
        "body": mail.body,
        "send": mail.send,
        "read": mail.read,
        "star": mail.star,
        "trash": mail.trash,
        "delete": mail.delete
    });
    ajaxMasterFunction("U_Mail");
}

function sendNewMail(responseMail) {
    ajax_url = "/spadsystem/rest/send_mail";
    ajax_method = "POST";
    ajax_content_type = "application/json";
    if (responseMail) {
        ajax_data = JSON.stringify({
            "sender": {user_id: localStorage.id},
            "receiver": {first_name: $('#detail_mail_name').html()},
            "title": $('#detail_mail_title').html(),
            "time": new Date().toLocaleString("fa"),
            "body": $('#textarea').val()
        });
    } else {
        ajax_data = JSON.stringify({
            "sender": {user_id: localStorage.id},
            "receiver": {user_id: $('#new_mail_receiver').val()},
            "title": $('#new_mail_title').val(),
            "time": new Date().toLocaleString("fa"),
            "body": $('#new_mail_body').val()
        });
    }
    ajaxMasterFunction("S_Mail");
}

function addSendMail(mail) {
    let str = '<tr id="' + mail.pk + '" class="unread">' +
        ' <td class="mail-select">' +
        '  <div class="checkbox checkbox-primary m-r-15">' +
        '   <input type="checkbox">' +
        '   <label></label>' +
        '  </div>' +
        '  <i onclick="mailStarChange(this)" class="fa fa-star m-r-15 text-' + (mail.star ? 'warning' : 'muted') + '"></i>' +
        ' </td>' +
        ' <td><a href="#" onclick="showMailDetail(' + mail.pk + ')" class="email-name">' +
        mail.receiver.first_name + ' ' + mail.receiver.last_name + '</a></td>' +
        '<td class="hidden-xs"><a href="#" onclick="showMailDetail(' + mail.pk + ')" class="email-msg">' +
        mail.title + '</a></td>' +
        (mail.attach == null ? '' : '<td style="width: 20px;"><i class="fa fa-paperclip"></i></td>') +
        '<td class="text-right">' + mail.time + '</td></tr>';
    $('#sent_mails').prepend(str);
}

function mailStarChange(element) {
    element = $(element);
    const parent = element.parent().parent(),
        mail = all_mails.find(elm => elm.pk === Number(parent.attr('id')));
    if (mail.receiver.user_id === localStorage.id) {
        element = $('#mail-inbox #' + parent.attr('id') + ' .mail-select i');
    } else if (mail.sender.user_id === localStorage.id) {
        element = $('#mail-send #' + parent.attr('id') + ' .mail-select i');
    }
    if (element.attr('class').indexOf('text-muted') !== -1) {
        element.addClass('text-warning');
        element.removeClass('text-muted');
        mail.star = true;
        $('#star_mails').prepend(parent.clone());
    } else {
        element.addClass('text-muted');
        element.removeClass('text-warning');
        mail.star = false;
        $('#star_mails #' + parent.attr('id')).detach();
    }
    updateMail(mail);
}

function deleteMail() {
    if ($('#mail-detail').css('display') === "block") {
        let mail = JSON.parse(localStorage.active_object);
        if (mail.trash) {
            mail.delete = true;
            $('#trash_mails').detach();
            showTrashView();
        } else {
            mail.trash = true;
            if ($('#trash_mails').children().length === 0)
                $('#trash_mails').html($('#' + mail.pk));
            else
                $('#trash_mails').prepend($('#' + mail.pk));
            showInboxMailView();
        }
        updateMail(mail);
    }
}

function showLastSendingMail() {
    let str = "", contacts = [];
    $.each(all_mails, function (i, mail) {
        if (mail.receiver.user_id !== localStorage.id) {
            if (!contacts.includes(mail.receiver.user_id)) {
                contacts.push(mail.receiver.user_id);
                str += "<li class=\"list-group-item\">" +
                    " <a href=\"#\">" +
                    "  <div class=\"avatar\">" +
                    "   <img src=\"assets/images/users/avatar-1.jpg\" alt=\"\">" +
                    "  </div>" +
                    "  <span class=\"name\">" + mail.receiver.first_name + " " + mail.receiver.last_name + "</span>" +
                    // "  <i class=\"fa fa-circle online\"></i>" +
                    " </a>" +
                    " <span class=\"clearfix\"></span>" +
                    "</li>";
            }
        }
    });
    $('.contacts-list').eq(0).html(str);
}