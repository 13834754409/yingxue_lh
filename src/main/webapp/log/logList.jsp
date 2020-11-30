<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<style>
    th {
        text-align: center;
    }
</style>
<script>


    $(function () {
        $('#logList').jqGrid({
            styleUI: 'Bootstrap',
            url: "${path}/log/findAll",
            datatype: "json",
            colNames: ["编号", "操作人", "操作详情", "操作时间", "操作状态"],
            colModel: [
                {name: 'id', align: 'center'},
                {name: 'logname', align: 'center'},
                {name: 'logOption', align: 'center'},
                {name: 'logTimes', align: 'center'},
                {name: 'status', align: 'center'},
            ],
            pager: '#pager',
            page: 1,
            rowNum: 10,
            rowList: [20, 40, 60],
            viewrecords: true,
            autowidth: true,
            height: "auto",
            rownumbers: true,//开启表格行号
            multiselect: true,//开启多行选择
        }).jqGrid('navGrid', '#pager', {
                edit: false, add: false, del: false, search: false,},
        );
    });
</script>
<%--面板--%>

<div class="panel panel-warning">
    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>日志信息</h2>
    </div>
    <%--标签页--%>
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab"
                                                  data-toggle="tab">查看日志</a></li>
    </ul>
    <br>
    <%--数据--%>
    <br>
    <table id="logList" class="table table-hover table-responsive table-striped"/>
    <%--分页工具栏--%>
    <div id="pager"/>
</div>
