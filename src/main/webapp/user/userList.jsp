<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<style>
    th{
        text-align: center;
    }
</style>
<script>

    function update(id, status) {
        let b = confirm("确定修改?");
        if (b == true) {
            $.ajax({
                url: "${path}/user/status",
                data: {'id': id, 'status': status},
                datatype: "json",
                success: function (res) {
                    $('#userId').jqGrid('clearGridData');
                    $('#userId').trigger('reloadGrid');
                }
            });
        }
    }

    $(function () {
        let id = null;
        $('#userId').jqGrid({
            styleUI: 'Bootstrap',
            url: "${path}/user/findAll",
            editurl: '${path}/user/add',
            cellurl: '${path}/user/add',
            datatype: "json",
            colNames: ["编号", "名字","性别", "电话", "头像", "简介", "学分", "状态", "创建时间","城市"],
            colModel: [
                {name: 'id', align: 'center'},
                {name: 'nickname', align: 'center', editable: true},
                {name: 'sex', align: 'center', editable: true},
                {name: 'phone', align: 'center', editable: true},
                {
                    name: 'picImg', align: 'center', editable: true, edittype: "file",
                    formatter: function (value, options, rows) {
                        return "<img src='" + value + "' class='img-circle' width:'60px' height='60px' >";
                    }
                },
                {name: 'brief', align: 'center', editable: true},
                {name: 'score', align: 'center', editable: true},
                {
                    name: 'status', editable: true, align: 'center',
                    formatter: function (value, options, rows) {
                        id = rows.id;
                        if (value == 1) return "<button class='btn btn-success' onclick='update(\"" + rows.id + "\",\"" + value + "\")'>正常</button>";
                        else return "<button class='btn btn-danger' onclick='update(\"" + rows.id + "\",\"" + value + "\")'>冻结</button>";
                    }
                },
                {name: 'createDate', align: 'center', formatter: 'date', formatoptions: {newformat: 'Y-m-d'},},
                {name: 'city', align: 'center', editable:true}
            ],
            pager: '#pager',
            page: 1,
            rowNum: 5,
            rowList: [5, 10, 20, 30],
            viewrecords: true,
            autowidth: true,
            height: "auto",
            rownumbers: true,//开启表格行号
            multiselect: true,//开启多行选择
        }).jqGrid('navGrid', '#pager', {
                edit: false, add: true, del: false, search: false,
                addtext: '添加'
            },
            {},
            /*添加之后的额外操作*/
            {
                closeAfterAdd: true,
                afterSubmit: function (response) {
                    let id = response.responseJSON.rows.id;
                    $.ajaxFileUpload({
                        url: "${path}/user/showUpload",
                        type: "post",
                        data: {"id": id},
                        fileElementId: "picImg",
                        success: function () {
                            $("#userId").trigger("reloadGrid");
                        }
                    });
                    return "true";
                }
            },
            {},
        );
        $("#aliyun").click(function () {
            let val = $("#phone").val();
            $.ajax({
                url:"${path}/user/aliyun",
                data:{"phone":val},
                datatype:"JSON",
                success:function (res) {
                    alert(res.message);
                }
            });
        });
        $('#poi').click(function () {
            $.ajax({
                url:"${path}/user/easyPOI",
                datatype:"JSON",
                success:function (res) {
                    alert(res.message);
                }
            });
        });

    });
</script>
<%--面板--%>
<div class="panel panel-danger">
    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>用户信息</h2>
    </div>
        <%--标签页--%>
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab"
                                                      data-toggle="tab">用户管理</a></li>
        </ul>
    <br>
    <div class="row">
        <div class="col-sm-4" style="margin-left: 10px">
            <button class="btn btn-success" id="poi">导出用户信息</button>
        </div>
        <form class="form-inline col-sm-offset-9">
            <div class="form-group">
                <input type="email" class="form-control" id="phone" placeholder="Photp">
            </div>
            <button type="button" id="aliyun" class="btn btn-default">获取验证码</button>
        </form>
    </div>
    <%--数据--%>
    <br>
    <table id="userId" class="table table-hover table-responsive table-striped"/>
    <%--分页工具栏--%>
    <div id="pager"/>
</div>
