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
        pageInit();
    });

    function del() {
        $.ajax({
            url:"${path}/${path}/category/edit",
            datatype:"json",
            success:function (res) {
                console.log(res.status);
            }
        });
    }

    function pageInit() {
        $("#cateList").jqGrid({
            styleUI: 'Bootstrap',
            url: "${path}/category/findAllFirst",
            datatype: "json",
            editurl: "${path}/category/edit",
            colNames: ['编号', '一级类别名', '所属级别'],
            colModel: [
                {name: 'id', index: 'id', width: 55, align: 'center'},
                {name: 'catename', index: 'catename', width: 90, align: 'center', editable: true},
                {name: 'levels', index: 'levels', width: 100, align: 'center',hidden:true},
            ],
            rowNum: 5,
            rowList: [5, 10, 20],
            pager: '#pager',
            sortname: 'id',
            viewrecords: true,
            rownumbers:true,
            multiselect:true,
            autowidth: true,
            height: "auto",
            subGrid: true,  //开启子表格
            // subgrid_id:是在创建表数据时创建的div标签的ID
            //row_id是该行的ID
            subGridRowExpanded: function (subgrid_id, row_id) {
                addSubGrid(subgrid_id, row_id);
            },
        }).jqGrid('navGrid', '#pager', {
                edit: true, add: true, del: true, search: false,
                edittext: '修改',
                addtext: '添加',
                deltext: '删除',

            },
            {
                closeAfterEdit: true,//关闭面板
                reloadAfterSubmit: true,
                afterSubmit: function (response) {
                    if(response.responseJSON.message){
                        alert(response.responseJSON.message);
                    }
                    return "true";
                }
            },  //修改
            {
                closeAfterAdd: true,
                reloadAfterSubmit: true,
                afterSubmit: function (response) {
                    if(response.responseJSON.message){
                        alert(response.responseJSON.message);
                    }
                    return "true";
                }
            }, //添加
            {
                closeAfterDelete: true,
                reloadAfterSubmit: true,
                afterSubmit: function (response) {
                    if(response.responseJSON.message) {
                        alert(response.responseJSON.message);
                    }
                    return "true";
                }
                //删除成功之后触发的function, 接收删除返回的提示信息, 在页面做展示
            }, //删除

        );
    }

    //开启子表格的样式
    function addSubGrid(subgridId, rowId) {
        console.log(rowId);
        var subgridTableTd = subgridId + "Table";
        var pagerId = subgridId + "Page";
        $("#" + subgridId).html("" +
            "<table id='" + subgridTableTd + "' />" +
            "<div id='" + pagerId + "' />"
        );
        $("#" + subgridTableTd).jqGrid({
            url: "${path}/category/findAllSecond?id=" + rowId,
            datatype: "json",
            editurl: "${path}/category/edit?editid=" + rowId,
            colNames: ['编号', '二级类别名', '所属级别'],
            colModel: [
                {name: "id", index: "num", width: 80, key: true, align: 'center'},
                {name: "catename", index: "item", width: 130, align: 'center', editable: true},
                {name: "levels", index: "qty", width: 70, align: "right", align: 'center', hidden:true},
            ],
            rowNum: 5,
            rowList: [5, 10, 20],
            pager: "#" + pagerId,
            rownumbers:true,
            multiselect:true,
            sortname: 'num',
            sortorder: "asc",
            styleUI: "Bootstrap",
            autowidth: true,
            height: "auto",

        });
        $("#" + subgridTableTd).jqGrid('navGrid', "#" + pagerId, {
                edit: true, add: true, del: true, search: false,
                edittext: '修改',
                addtext: '添加',
                deltext: '删除',
            },
            {
                closeAfterEdit: true,//关闭面板
                reloadAfterSubmit: true,
                afterSubmit: function (response) {
                    if(response.responseJSON.message){
                        alert(response.responseJSON.message);
                    }
                    return "true";
                }
            },
            {
                closeAfterAdd: true,
                reloadAfterSubmit: true,
                afterSubmit: function (response) {
                    if(response.responseJSON.message){
                        alert(response.responseJSON.message);
                    }
                    return "true";
                }
            },
            {
                closeAfterDelete: true,
                reloadAfterSubmit: true,
                afterSubmit: function (response) {
                    if(response.responseJSON.message){
                        alert(response.responseJSON.message);
                    }
                    return "true";
                }
            }
        );
    }
</script>
<%--面板--%>
<div class="panel panel-success">
    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>分类信息</h2>
    </div>
    <%--标签页--%>
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab"
                                                  data-toggle="tab">类别管理</a></li>
    </ul>
    <br>
    <%--数据--%>
    <table id="cateList" class="table table-hover table-responsive table-striped"/>
    <%--分页工具栏--%>
    <div id="pager"/>
</div>
