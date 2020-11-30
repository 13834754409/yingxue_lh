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
        $('#videoList').jqGrid({
            styleUI: 'Bootstrap',
            url: "${path}/video/findAll",
            editurl: '${path}/video/edit',
            cellurl: '${path}/video/edit',
            datatype: "json",
            colNames: ["编号", "标题", "简介", "封面", "视频", "上传时间", "级别", "上传人", "所属分组"],
            colModel: [
                {name: 'id', align: 'center'},
                {name: 'title', align: 'center', editable: true, editable: true,},
                {name: 'brief', align: 'center', editable: true, editable: true,},
                {
                    name: 'coverPath', align: 'center', edittype: "file", width: '220px',
                    formatter: function (value, options, rows) {
                        return "<img src='" + value + "' style='height: 150px;width: 100%'/>"
                    }
                },
                {
                    name: 'videoPath', align: 'center', editable: true, edittype: "file", width: '230px',
                    formatter: function (value, options, rows) {
                        return "<video src='" + value + "' poster='" + rows.coverPath + "' controls='controls' style='height: 150px;width:100%'/>"
                    }
                },
                {name: 'uploadTime', align: 'center', width: '95px',},
                {
                    name: 'categoryId',
                    editable: true,
                    align: 'center',
                    width: '50px',
                    edittype: "select",
                    editoptions: {dataUrl: "${path}/category/findAllSecondByAll"},
                },
                {name: 'userId', align: 'center', editable: true,},
                {name: 'groupId', align: 'center', editable: true,}
            ],
            pager: '#pager',
            page: 1,
            rowNum: 5,
            rowList: [5, 10, 20, 30],
            viewrecords: true,
            autowidth: true,
            height: "auto",
            //rownumbers: true,//开启表格行号
            multiselect: true,//开启多行选择
        }).jqGrid('navGrid', '#pager', {
                edit: true, add: true, del: true, search: false,
                edittext: '修改',
                addtext: '添加',
                deltext: '删除',
                searchtext: "刷新"
            },
            {
                closeAfterEdit: true,//关闭面板
                //reloadAfterSubmit: true,
                afterSubmit: function (response) {
                    console.log(response);
                    $.ajaxFileUpload({
                        url: "${path}/video/modfiyvideo",
                        type: "post",
                        data: {"id": response.responseJSON.rows.id},
                        fileElementId: "videoPath",
                        success: function () {
                            $("#videoList").trigger("reloadGrid");
                        }
                    });
                    return "true";
                }

            },
            /*添加之后的额外操作*/
            {
                closeAfterAdd: true,//关闭面板
                //reloadAfterSubmit: true,
                afterSubmit: function (response) {
                    console.log(response);
                    $.ajaxFileUpload({
                        url: "${path}/video/upload",
                        type: "post",
                        data: {"id": response.responseJSON.rows.id},
                        fileElementId: "videoPath",
                        success: function () {
                            $("#videoList").trigger("reloadGrid");
                        }
                    });
                    return "true";
                }
            },
            {
                closeAfterDelete: true,//关闭面板
                reloadAfterSubmit: true,
            },
        );

    });
</script>
<%--面板--%>

<div class="panel panel-warning">
    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>视频信息</h2>
    </div>
    <%--标签页--%>
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab"
                                                  data-toggle="tab">视频管理</a></li>
    </ul>
    <br>
    <%--数据--%>
    <br>
    <table id="videoList" class="table table-hover table-responsive table-striped"/>
    <%--分页工具栏--%>
    <div id="pager"/>
</div>
