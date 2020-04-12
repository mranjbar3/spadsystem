<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Page-Title -->
<div class="row">
    <div class="col-sm-12">

        <h4 class="page-title">صندوق پیام</h4>
        <ol class="breadcrumb">
            <li>
                <a href="#">پیشخوان</a>
            </li>
            <li class="active">
                صندوق پیام
            </li>
        </ol>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <div class="card-box" style="float: right; width: 100%;">
            <!-- Left sidebar -->
            <div class="col-lg-3 col-md-4">

                <div class="p-20">
                    <a href="#" onclick="showNewMailView()"
                       class="btn btn-Hazheh btn-rounded btn-custom btn-block waves-effect waves-light">ارسال
                        پیام جدید</a>

                    <div class="list-group mail-list  m-t-20">
                        <a id="receive_btn" href="#" onclick="showInboxMailView()" class="list-group-item b-0 active"><i
                                class="fa fa-download m-r-10"></i>صندوق
                            دریافتی <b></b></a>
                        <a id="star_btn" href="#" onclick="showStarMailView()" class="list-group-item b-0"><i
                                class="fa fa-star-o m-r-10"></i>نشان دار<b></b></a>
                        <a id="sent_btn" href="#" onclick="showSendMailView()" class="list-group-item b-0"><i
                                class="fa fa-paper-plane-o m-r-10"></i>ارسال
                            شده</a>
                        <a id="trash_btn" href="#" onclick="showTrashView()" class="list-group-item b-0"><i
                                class="fa fa-trash-o m-r-10"></i>حذف شده
                            <b></b></a>
                    </div>
                </div>

            </div>
            <!-- End Left sidebar -->
            <!-- Right Sidebar -->
            <div class="col-lg-9 col-md-8">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="btn-toolbar m-t-20" role="toolbar">
                            <div class="btn-group">
                                <button onclick="selectAll()" type="button"
                                        class="btn btn-Hazheh waves-effect waves-light "><i
                                        class="fa fa-check-square-o"></i></button>
                                <button onclick="starSelectedMessages()" type="button"
                                        class="btn btn-Hazheh waves-effect waves-light "><i
                                        class="fa fa-star"></i></button>
                                <button onclick="deleteMail()" type="button"
                                        class="btn btn-danger waves-effect waves-light "><i
                                        class="fa fa-trash-o"></i></button>
                            </div>
                            <div class="btn-group">
                                <button type="button" class="btn btn-primary waves-effect waves-light " title="پاسخ"><i
                                        class="fa fa-reply"></i></button>
                                <button type="button" class="btn btn-primary waves-effect waves-light "
                                        title="ارسال به"><i class="fa fa-mail-forward"></i></button>
                            </div>

                        </div>
                    </div>
                </div> <!-- End row -->


                <div id="mail-inbox" style="display: inline;">
                    <jsp:include page="Inbox.html"/>
                </div>
                <div id="mail-detail" style="display: none;">
                    <jsp:include page="Details.html"/>
                </div>
                <div id="mail-send" style="display: none;">
                    <jsp:include page="Sent.html"/>
                </div>
                <div id="mail-sent" style="display: none;">
                    <jsp:include page="Send.html"/>
                </div>
                <div id="mail-star" style="display: none;">
                    <jsp:include page="Star.html"/>
                </div>
                <div id="mail-trash" style="display: none;">
                    <jsp:include page="Trash.html"/>
                </div>
            </div> <!-- end Col-9 -->

        </div>
    </div>
</div>
<!-- End row -->