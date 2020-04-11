<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <!-- Page Description and Author -->
    <meta name="description"
          content="سامانه مدیریت هوشمند ذخیره سازی شماره تماس های ارگان ها، سازمان ها و شرکت های خصوص"/>
    <meta name="author" content="SPADSYSTEM"/>
    <meta name="keywords" content="دفترچه تلفن، تلفن گویا"/>
    <link rel="shortcut icon" href="assets/images/favicon_1.ico">
    <link rel="apple-touch-icon" sizes="48x48" href="assets/images/logo48.png"/>
    <link rel="apple-touch-icon" sizes="64x64" href="assets/images/logo64.png"/>
    <link rel="apple-touch-icon" sizes="128x128" href="assets/images/logo128.png"/>
    <link rel="alternate" href="https://www.spadsystem.com" hreflang="fa-ir"/>

    <title>پنل مدیریت - دفترچه تلفن</title>

    <link href="assets/css/bootstrap-rtl.min.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/core.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/components.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/icons.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/pages.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/responsive.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/font.css" rel="stylesheet"/>

    <script src="assets/js/modernizr.min.js"></script>

</head>

<body class="fixed-left">

<!-- Begin page -->
<div id="wrapper">

    <!-- Top Bar Start -->
    <div class="topbar">

        <!-- LOGO -->
        <div class="topbar-left">
            <div class="text-center">
                <a href="Dashboard" class="logo">
                    <i class="icon-c-logo"> <img src="assets/images/logo_sm_w.png" height="42"/> </i>
                    <span><img src="assets/images/logo-w.png" height="58" title="سامانه جستجوگر هوشمند شماره تلفن هاژه"
                               alt="سامانه جستجوگر هوشمند شماره تلفن هاژه"/></span>
                </a>
            </div>
        </div>

        <!-- Button mobile view to collapse sidebar menu -->
        <div class="navbar navbar-default" role="navigation">
            <div class="container">
                <div class="">
                    <div class="pull-left">
                        <button class="button-menu-mobile open-left waves-effect waves-light">
                            <i class="md md-menu"></i>
                        </button>
                        <span class="clearfix"></span>
                    </div>

                    <form role="search" class="navbar-left app-search pull-left hidden-xs">
                        <input type="text" placeholder="جستجو..." class="form-control">
                        <a href=""><i class="fa fa-search"></i></a>
                    </form>


                    <ul class="nav navbar-nav navbar-right pull-right">
                        <li class="hidden-xs">
                            <a href="#" id="btn-fullscreen" class="waves-effect waves-light"><i
                                    class="icon-size-fullscreen"></i></a>
                        </li>
                        <li class="dropdown top-menu-item-xs">
                            <a href="#" data-target="#" class="dropdown-toggle waves-effect waves-light"
                               data-toggle="dropdown" aria-expanded="true">
                                <i class="icon-bell"></i> <span style="display: none;"
                                                                class="badge badge-xs badge-danger"></span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-lg">
                                <li class="notifi-title"><span class="label label-default pull-right">جدید 3</span>پیام
                                    های خوانده نشده
                                </li>
                                <li class="list-group slimscroll-noti notification-list">
                                    <!-- list item-->

                                </li>
                                <li>
                                    <a href="#" onclick="showInboxMailView()" class="list-group-item text-left">
                                        <small class="font-600">مشاهده پیام های دریافتی</small>
                                    </a>
                                </li>
                            </ul>
                        </li>

                        <li class="hidden-xs">
                            <a href="#" class="right-bar-toggle waves-effect waves-light"><i
                                    class="glyphicon glyphicon-comment"></i></a>
                        </li>
                        <li class="dropdown top-menu-item-xs">
                            <a href="" class="dropdown-toggle profile waves-effect waves-light" data-toggle="dropdown"
                               aria-expanded="true"><img src="assets/images/users/avatar-1.jpg" alt="user-img"
                                                         class="img-circle"> </a>
                            <ul class="dropdown-menu">
                                <li><a href="#" onclick="showProfileView()"><i class="ti-user m-r-10 text-custom"></i>
                                    حساب کاربری</a>
                                </li>
                                <li class="divider"></li>
                                <li><a href="#"
                                       onclick='window.location.replace("http://localhost:8080/spadsystem/login.html");'><i
                                        class="ti-power-off m-r-10 text-danger"></i>
                                    خروج</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
                <!--/.nav-collapse -->
            </div>
        </div>
    </div>
    <!-- Top Bar End -->
    <!-- ========== Left Sidebar Start ========== -->

    <div class="left side-menu">
        <div class="sidebar-inner slimscrollleft">
            <!--- Divider -->
            <div id="sidebar-menu">
                <ul>
                    <li class="has_sub">
                        <a href="#" onclick="showDashboardView()" class="waves-effect"><i class="ti-dashboard"></i>
                            <span> پیش خوان </span></a>
                    </li>

                    <li class="has_sub">
                        <a href="javascript:void(0);" class="waves-effect"><i class="ti-user"></i>
                            <span> کارمندان </span> <span class="menu-arrow"></span> </a>
                        <ul class="list-unstyled">
                            <li><a href="#" onclick="showEmployeeView()">مدیریت کارمندان</a></li>
                        </ul>
                    </li>

                    <li class="has_sub">
                        <a href="javascript:void(0);" class="waves-effect"><i class="ti-email"></i><span
                                class="label label-success pull-right"
                                style="display: none;"></span><span> صندوق پیام </span> </a>
                        <ul class="list-unstyled">
                            <li><a href="#" onclick="showNewMailView()"><i class="ion ion-plus"></i>ارسال پیام جدید</a>
                            </li>
                            <li><a href="#" onclick="showInboxMailView()"><i class="ion ion-android-inbox"></i> پیام های
                                دریافتی</a></li>
                            <li><a href="#" onclick="showSendMailView()"><i class="ion ion-android-send"></i>پیام های
                                ارسالی</a></li>
                        </ul>
                    </li>

                </ul>
                <div class="clearfix"></div>
            </div>
            <div class="clearfix"></div>
        </div>
    </div>
    <!-- Left Sidebar End -->
    <!-- ============================================================== -->
    <!-- Start right Content here -->
    <!-- ============================================================== -->
    <div class="content-page">
        <!-- Start content -->
        <div class="content" style="overflow-y: scroll; height: 92%;">
            <div class="container">
                <div id="admin" style="display: none;">
                    <jsp:include page="Views/Dashboard/Index.html"/>
                </div>
                <div id="employee" style="display: none;">
                    <jsp:include page="Views/Employee/Index.html"/>
                </div>
                <div id="profile" style="display: none;">
                    <jsp:include page="Views/Profile/Index.html"/>
                </div>
                <div id="mail-part" style="display: inline;">
                    <jsp:include page="Views/Mail/index.jsp"/>
                </div>
            </div> <!-- container -->

        </div> <!-- content -->

        <footer class="footer" style="background-color: aliceblue;z-index: 1;">
            © 1399. تمامی حقوق مادی و معنوی برای بانک سپه محفوظ است | طراحی شده توسط هلدینگ <a
                href="http://www.spadsystem.com" style="color:#ea0000">اسپاد سیستم</a>
        </footer>

    </div>
    <!-- ============================================================== -->
    <!-- End Right content here -->
    <!-- ============================================================== -->
    <!-- Right Sidebar -->
    <div class="side-bar right-bar nicescroll">
        <h4 class="text-center">آخرین پیام های ارسال شده</h4>
        <div class="contact-list nicescroll">
            <ul class="list-group contacts-list">
            </ul>
        </div>
    </div>
    <!-- /Right-bar -->

</div>
<!-- END wrapper -->

<script>
    var resizefunc = [];
</script>

<!-- jQuery  -->
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/bootstrap-rtl.min.js"></script>
<script src="assets/js/detect.js"></script>
<script src="assets/js/fastclick.js"></script>
<script src="assets/js/jquery.slimscroll.js"></script>
<script src="assets/js/jquery.blockUI.js"></script>
<script src="assets/js/waves.js"></script>
<script src="assets/js/wow.min.js"></script>
<script src="assets/js/jquery.nicescroll.js"></script>
<script src="assets/js/jquery.scrollTo.min.js"></script>
<script src="assets/js/materialize.min.js"></script>
<%--<script src="assets/plugins/bootstrap-filestyle/js/bootstrap-filestyle.min.js" type="text/javascript"></script>--%>

<script src="assets/js/jquery.core.js"></script>
<script src="assets/js/jquery.app.js"></script>

<script src="assets/js/mainFunction.js"></script>

</body>
</html>
