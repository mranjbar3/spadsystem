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
        $("#mail-sent").show();
    }
}

//when you want to show mail inbox, call this function.
function showInboxMailView() {
    mailHide();
    $("#mail-inbox").show();
    $("#receive_btn").addClass("active");
}

//when you want to view your send mail, call this function.
function showSendMailView() {
    mailHide();
    $("#mail-send").show();
    $("#sent_btn").addClass("active");
}

//star mails show in this function.
function showStarMailView() {
    mailHide();
    $("#mail-star").show();
    $("#star_btn").addClass("active");
}

//this function show mails that in trash.
function showTrashView() {
    mailHide();
    $('#mail-trash').show();
    $("#trash_btn").addClass("active");
}

//detail of one mail show!
function showMailDetailView() {
    mailHide();
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
                    showToast('پیام ارسال شد.', 'green rounded');
                    showNewMailView(true);
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
    ajax_data = JSON.stringify({"receiver": id});
    ajaxMasterFunction("G_Mail");
}

function showAllMail(mails) {
    produceMailViewString(mails);
    const rightLable = $('.label-success').eq(0);
    unread_receive_message = $('#receive_mails .unread').length;
    unread_trash_message = $('#trash_mails .unread').length;
    unread_star_message = $('#star_mails .unread').length;
    if (unread_receive_message > 0) {
        rightLable.show();
        rightLable.html(unread_receive_message);
        $('#receive_btn b').eq(0).html("(" + unread_receive_message + ")");
    } else {
        rightLable.hide();
    }
    if (unread_trash_message > 0) {
        $('#trash_btn b').eq(0).html("(" + unread_trash_message + ")");
    }
    if (unread_star_message > 0) {
        $('#star_btn b').eq(0).html("(" + unread_star_message + ")");
    }
}

function produceMailViewString(mails, type_str) {
    $.each(mails, function (i, mail) {
        let str = '<tr id="' + mail.pk + '"';
        if (!mail.read) {
            str += ' class="unread"';
        }
        str += '><td class="mail-select"><div class="checkbox checkbox-primary m-r-15">' +
            '<input type="checkbox"><label></label>' +
            '</div><i onclick="mailStarChange(this)" class="fa fa-star m-r-15 text-' + (mail.star ? 'warning' : 'muted') + '"></i></td>' +
            '<td><a href="#" onclick="showMailDetail(' + mail.pk + ',\'' + type_str + '\')" class="email-name">' +
            (mail.sender === localStorage.id ? mail.receiver : mail.sender) + '</a></td>' +
            '<td class="hidden-xs"><a href="#" onclick="showMailDetail(' + mail.pk + ',\'' + type_str +
            '\')" class="email-msg">' + mail.title + '</a></td>' +
            '<td style="width: 20px;"><i class="fa fa-paperclip"></i></td>' +
            '<td class="text-right">' + mail.time + '</td></tr>';

        if (mail.trash) {
            $('#trash_mails').prepend(str);
        } else {
            if (mail.receiver === localStorage.id) {
                $('#receive_mails').prepend(str);
            } else if (mail.sender === localStorage.id) {
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
    $('#detail_mail_name').html(mail.sender === localStorage.id ? mail.receiver : mail.sender);
    $('#detail_mail_body').html(mail.body);
    localStorage.active_object = JSON.stringify(mail);
    if (!mail.read) {
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
        } else if (mail.receiver === localStorage.id) {
            unread_receive_message--;
            $("#receive_btn b").eq(0).html(unread_receive_message === 1 ? "" : "(" + unread_receive_message + ")");
            $('.label-success').eq(0).html(unread_receive_message);
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

function sendNewMail() {
    ajax_url = "/spadsystem/rest/send_mail";
    ajax_method = "POST";
    ajax_content_type = "application/json";
    ajax_data = JSON.stringify({
        "sender": localStorage.id,
        "receiver": $('#new_mail_receiver').val(),
        "title": $('#new_mail_title').val(),
        "time": new Date().toLocaleString(),
        "body": $('#new_mail_body').val()
    });
    ajaxMasterFunction("S_Mail");
}

function mailStarChange(element) {
    element = $(element);
    const mail = all_mails.find(elm => elm.pk === element.parent().parent().attr('id'));
    if (element.attr('class').indexOf('text-muted') !== -1) {
        element.addClass('text-warning');
        element.removeClass('text-muted');
        mail.star = true;
    } else {
        element.addClass('text-muted');
        element.removeClass('text-warning');
        mail.star = false;
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