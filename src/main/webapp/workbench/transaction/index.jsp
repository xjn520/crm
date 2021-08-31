<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){
		pageList(1,2);

		//为全选复选框添加事件
		$("#qx").click(function (){
			$("input[name=xz]").prop("checked",this.checked);
		})
		//为查询按钮绑定事件
		$("#queryBtn").click(function (){
			//将搜索框里面的值赋值到隐藏域当中
			$("#hidden-name").val($.trim($("#search-name").val()))
			$("#hidden-owner").val($.trim($("#search-owner").val()))
			$("#hidden-source").val($.trim($("#search-source").val()))
			$("#hidden-customerName").val($.trim($("#search-customerName").val()))
			$("#hidden-stage").val($.trim($("#search-stage").val()))
			$("#hidden-contactName").val($.trim($("#search-contactName").val()))
			$("#hidden-type").val($.trim($("#search-type").val()))
			pageList(1,2);
		})
		//为修稿按钮绑定事件
		$("#editBtn").click(function (){
			var $xz=$("input[name=xz]:checked");
			if ($xz.length==0)
			{
				alert("请选择你要修改的数据");
			}else if ($xz.length>1)
			{
				alert("一次只能修改一项");
			}else {
				var tranId=$("input[name=xz]:checked").val();
				window.location.href="workbench/transaction/edit.do?tranId="+tranId;
			}

		})
		//为删除按钮绑定事件
		$("#deleteBtn").click(function (){
			var $xz=$("input[name=xz]:checked");
			var tranId=$xz.val();
			if ($xz.length==0)
			{
				alert("请选择你要删除的数据")
			}else if($xz.length>1)
			{
				alert("一次只能删除一项")
			}else {
				if (confirm("确定要删除吗？"))
				{
					$.ajax({
						url:"workbench/transaction/delete.do",
						data:{
							"tranId":tranId
						},
						type:"post",
						dataType:"json",
						success:function (data){
							if (data.success)
							{
								alert("删除成功");
								pageList(1,2);
							}else {
								alert("删除失败")
							}
						}
					})
				}
			}
		})
	});
	function pageList(pageNo,pageSize)
	{
		//做查询前，将隐藏域中保存的信息取出来重新赋值到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()))
		$("#search-owner").val($.trim($("#hidden-owner").val()))
		$("#search-source").val($.trim($("#hidden-source").val()))
		$("#search-customerName").val($.trim($("#hidden-customerName").val()))
		$("#search-stage").val($.trim($("#hidden-stage").val()))
		$("#search-contactName").val($.trim($("#hidden-contactName").val()))
		$("#search-type").val($.trim($("#hidden-type").val()))
		$.ajax({
			url:"workbench/transaction/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"customerName":$.trim($("#search-customerName").val()),
				"source":$.trim($("#search-source").val()),
				"stage":$.trim($("#search-stage").val()),
				"contactName":$.trim($("#search-contactName").val()),
				"type":$.trim($("#search-type").val())
			},
			type:"get",
			dataType:"json",
			success:function (data){
				var html="";
				$.each(data.dataList,function (i,n){
					html+='<tr>';
					html+='<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
					html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/transaction/detail.do?id='+n.id+'\'">'+n.name+'</a></td>';
					html+='<td>'+n.customerId+'</td>';
					html+='<td>'+n.stage+'</td>';
					html+='<td>'+n.type+'</td>';
					html+='<td>'+n.owner+'</td>';
					html+='<td>'+n.source+'</td>';
					html+='<td>'+n.contactsId+'</td>';
					html+='</tr>';
				})
				$("#tranListBody").html(html);

				//计算总页数
				var totalPages=data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

				//数据处理完毕后，结合分页查询，对前端展现分页信息
				$("#activityPage").bs_pagination({
					currentPage:pageNo,
					rowsPerPage:pageSize,
					maxRowsPerPage: 20,
					totalPages:totalPages,
					totalRows:data.total,

					visiblePageLinks: 3,

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					//该函数点击分页组件的时候触发
					onChangePage:function (event,data){
						pageList(data.currentPage,data.rowsPerPage);
					}
				});
			}
		})
	}
</script>
</head>
<body>
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-customerName">
	<input type="hidden" id="hidden-stage">
	<input type="hidden" id="hidden-type">
	<input type="hidden" id="hidden-source">
	<input type="hidden" id="hidden-contactName">
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" type="text" id="search-customerName">
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="search-stage">
					  	<option></option>
					  	<option>资质审查</option>
					  	<option>需求分析</option>
					  	<option>价值建议</option>
					  	<option>确定决策者</option>
					  	<option>提案/报价</option>
					  	<option>谈判/复审</option>
					  	<option>成交</option>
					  	<option>丢失的线索</option>
					  	<option>因竞争丢失关闭</option>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="search-type">
					  	<option></option>
					  	<option>已有业务</option>
					  	<option>新业务</option>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="search-source">
						  <option></option>
						  <option>广告</option>
						  <option>推销电话</option>
						  <option>员工介绍</option>
						  <option>外部介绍</option>
						  <option>在线商场</option>
						  <option>合作伙伴</option>
						  <option>公开媒介</option>
						  <option>销售邮件</option>
						  <option>合作伙伴研讨会</option>
						  <option>内部研讨会</option>
						  <option>交易会</option>
						  <option>web下载</option>
						  <option>web调研</option>
						  <option>聊天</option>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <input class="form-control" type="text" id="search-contactName">
				    </div>
				  </div>
				  
				  <button type="submit" class="btn btn-default" id="queryBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" onclick="window.location.href='workbench/transaction/add.dao';"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" id="editBtn" class="btn btn-default"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" id="deleteBtn" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="tranListBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/transaction/detail.jsp';">动力节点-交易01</a></td>
							<td>动力节点</td>
							<td>谈判/复审</td>
							<td>新业务</td>
							<td>zhangsan</td>
							<td>广告</td>
							<td>李四</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/transaction/detail.jsp';">动力节点-交易01</a></td>
                            <td>动力节点</td>
                            <td>谈判/复审</td>
                            <td>新业务</td>
                            <td>zhangsan</td>
                            <td>广告</td>
                            <td>李四</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 20px;">
				<div id="activityPage">

				</div>
				<%--<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>--%>
			</div>
			
		</div>
		
	</div>
</body>
</html>