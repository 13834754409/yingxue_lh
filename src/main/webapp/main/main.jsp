<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>应学视频后台管理系统</title>
    <link rel="icon" href="${path}/bootstrap/img/arrow-up.png" type="image/x-icon">
    <link rel="stylesheet" href="${path}/bootstrap/css/bootstrap.css">

    <%--引入jqgrid中主题css--%>
    <link rel="stylesheet" href="${path}/bootstrap/jqgrid/css/css/hot-sneaks/jquery-ui-1.8.16.custom.css">
    <link rel="stylesheet" href="${path}/bootstrap/jqgrid/boot/css/trirand/ui.jqgrid-bootstrap.css">
    <%--引入js文件--%>
    <script src="${path}/bootstrap/js/jquery.min.js"></script>
    <script src="${path}/bootstrap/js/bootstrap.js"></script>
    <script src="${path}/bootstrap/jqgrid/js/i18n/grid.locale-cn.js"></script>
    <script src="${path}/bootstrap/jqgrid/boot/js/trirand/jquery.jqGrid.min.js"></script>
    <script src="${path}/bootstrap/js/ajaxfileupload.js"></script>
</head>
<body>
<!--顶部导航-->
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <a class="navbar-brand" href="${path}/main/main.jsp">应学视频App后台管理系统</a>
        </div>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li><a>欢迎:<font color="red">${sessionScope.admin.nickname}</font></a>
                <li class="dropdown">
                    <a href="${path}/admin/exit" class="dropdown-toggle" role="button" aria-haspopup="true"
                       aria-expanded="false">退出 <span class="glyphicon glyphicon-log-out"></span> </a>
                </li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
<!--栅格系统-->
<div class="container-fluid" style="margin-top: 70px">
    <div class="row">
        <div class="col-sm-2">
            <!--左边手风琴部分-->
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                <div class="panel panel-default">
                    <div class="panel-heading" role="tab" id="headingOne">
                        <h4 class="panel-title text-center">
                            <a class="btn btn-danger btn-group-justified" role="button" data-toggle="collapse"
                               data-parent="#accordion" href="#collapseOne" aria-expanded="true"
                               aria-controls="collapseOne">
                                <span class="glyphicon glyphicon-user"></span>
                                用户管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
                        <div class="panel-body text-center">
                            <div class="list-group">
                                <a href="javascript:$('#content').load('${path}/user/userList.jsp')"
                                   class="list-group-item">
                                    <button class="btn btn-danger btn-group-justified">
                                        用户展示
                                    </button>
                                </a>
                                <a href="javascript:$('#content').load('${path}/user/chinaAll.jsp')"
                                   class="list-group-item">
                                    <button class="btn btn-danger btn-group-justified">
                                        用户分布
                                    </button>
                                </a>
                                <a href="javascript:$('#content').load('${path}/user/userEchartsAll.jsp')"
                                   class="list-group-item">
                                    <button class="btn btn-danger btn-group-justified">
                                        用户统计
                                    </button>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading" role="tab" id="headingTwo">
                        <h4 class="panel-title">
                            <a class="collapsed btn btn-success btn-group-justified" role="button"
                               data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false"
                               aria-controls="collapseTwo">
                                <span class="glyphicon glyphicon-align-justify"></span>
                                分类管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
                        <div class="panel-body">
                            <div class="list-group">
                                <a href="javascript:$('#content').load('${path}/category/cateList.jsp')"
                                   class="list-group-item">
                                    <button class="btn btn-success btn-group-justified">
                                        分类展示
                                    </button>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading" role="tab" id="headingThree">
                        <h4 class="panel-title">
                            <a class="collapsed btn btn-warning btn-group-justified" role="button"
                               data-toggle="collapse" data-parent="#accordion" href="#collapseThree"
                               aria-expanded="false" aria-controls="collapseThree">
                                <span class="glyphicon glyphicon-sound-dolby"></span>
                                视频管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseThree" class="panel-collapse collapse" role="tabpanel"
                         aria-labelledby="headingThree">
                        <div class="panel-body">
                            <div class="list-group">
                                <a href="javascript:$('#content').load('${path}/video/videoList.jsp')"
                                   class="list-group-item">
                                    <button class="btn btn-warning btn-group-justified">
                                        视频展示
                                    </button>
                                </a>

                                <a href="javascript:$('#content').load('${path}/video/searchVideo.jsp')"
                                   class="list-group-item">
                                    <button class="btn btn-warning btn-group-justified">
                                        视频检索
                                    </button>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading" role="tab" id="headingThree1">
                        <h4 class="panel-title">
                            <a class="collapsed btn btn-primary btn-group-justified" role="button"
                               data-toggle="collapse" data-parent="#accordion" href="#collapseThree1"
                               aria-expanded="false" aria-controls="collapseThree">
                                <span class="glyphicon glyphicon-tasks"></span>
                                反馈管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseThree1" class="panel-collapse collapse" role="tabpanel"
                         aria-labelledby="headingThree">
                        <div class="panel-body">
                            <div class="list-group">
                                <a href="#" class="list-group-item">
                                    <button class="btn btn-primary btn-group-justified">
                                        反馈展示
                                    </button>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading" role="tab" id="headingThree2">
                        <h4 class="panel-title">
                            <a class="collapsed btn btn-info btn-group-justified" role="button"
                               data-toggle="collapse" data-parent="#accordion" href="#collapseThree2"
                               aria-expanded="false" aria-controls="collapseThree">
                                <span class="glyphicon glyphicon-tasks"></span>
                                日志管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseThree2" class="panel-collapse collapse" role="tabpanel"
                         aria-labelledby="headingThree">
                        <div class="panel-body">
                            <div class="list-group">
                                <a href="javascript:$('#content').load('${path}/log/logList.jsp')" class="list-group-item">
                                    <button class="btn btn-info btn-group-justified">
                                        日志展示
                                    </button>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>


            </div>
        </div>
        <div class="col-sm-10" id="content">
            <!--右边轮播图部分-->
            <div id="carousel-example-generic" data-interval="2000" class="carousel slide" data-ride="carousel">
                <!-- Indicators -->
                <ol class="carousel-indicators">
                    <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                    <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                    <li data-target="#carousel-example-generic" data-slide-to="2"></li>
                    <li data-target="#carousel-example-generic" data-slide-to="3"></li>
                    <li data-target="#carousel-example-generic" data-slide-to="4"></li>
                </ol>

                <!-- Wrapper for slides -->
                <div class="carousel-inner" role="listbox">
                    <div class="item active">
                        <img src="${path}/bootstrap/img/1.jpg" style="width: 100%;height: 400px">
                        <div class="carousel-caption">
                            <h3>学习使我快乐！</h3>
                            <p>好好学习！天天向上！欢迎来应学App</p>
                        </div>
                    </div>
                    <div class="item">
                        <img src="${path}/bootstrap/img/6.jpg" style="width: 100%;height: 400px">
                        <div class="carousel-caption">
                            <h3>学习使我快乐！</h3>
                            <p>好好学习！天天向上！欢迎来应学App</p>
                        </div>
                    </div>
                    <div class="item">
                        <img src="${path}/bootstrap/img/9.jpg" style="width: 100%;height: 400px">
                        <div class="carousel-caption">
                            <h3>学习使我快乐！</h3>
                            <p>好好学习！天天向上！欢迎来应学App</p>
                        </div>
                    </div>
                    <div class="item">
                        <img src="${path}/bootstrap/img/10.jpg" style="width: 100%;height: 400px">
                        <div class="carousel-caption">
                            <h3>学习使我快乐！</h3>
                            <p>好好学习！天天向上！欢迎来应学App</p>
                        </div>
                    </div>
                    <div class="item">
                        <img src="${path}/bootstrap/img/16.jpg" style="width: 100%;height: 400px">
                        <div class="carousel-caption">
                            <h3>学习使我快乐！</h3>
                            <p>好好学习！天天向上！欢迎来应学App</p>
                        </div>
                    </div>

                </div>

                <!-- Controls -->
                <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
                    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                    <span class="sr-only">Previous</span>
                </a>
                <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
                    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                    <span class="sr-only">Next</span>
                </a>
            </div>
            <!--巨幕开始-->
            <hr>
            <div class="jumbotron text-center">
                <h1>欢迎来到应学视频App后台管理系统</h1>
            </div>
        </div>
    </div>
</div>
<!--页脚-->


<nav class="navbar navbar-inverse navbar-fixed-bottom" style="bottom:-25px;">
    <div class="row footer-bottom">
        <ul class="list-inline text-center" style="margin-top: 2px">
            <li class="text-danger">@应学App yingxuelihui@163.com</li>
        </ul>
    </div>
</nav>
<!--栅格系统-->

</body>
</html>
