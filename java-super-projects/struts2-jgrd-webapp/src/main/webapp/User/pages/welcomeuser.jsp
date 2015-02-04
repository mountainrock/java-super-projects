<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome Page</title>
<sj:head jqueryui="true"  />
</head>
<body>
<form>
<div>
<s:url id="remoteurl" action="Welcome" />
			<sjg:grid id="gridtable" caption="Students" dataType="json"
				href="%{remoteurl}" pager="true" gridModel="listStudentUtils"
				rowNum="10" navigator="true" navigatorAdd="false"
				navigatorDelete="false" navigatorEdit="false"
				navigatorRefresh="true" navigatorSearch="false"
				navigatorView="false" rownumbers="true" rowList="10,20"
				viewrecords="true" autowidth="true">
				<sjg:gridColumn name="studentID" hidden="true" search="false"
					key="true" index="studentID" title="Student ID" sortable="false" />
				<sjg:gridColumn name="studentName" align="left" index="studentName"
					title="Student Name" sortable="false" />
				<sjg:gridColumn name="studentAddress" align="left"
					index="studentAddress" title="Student Address" sortable="false" />
			</sjg:grid>
		</div>
</form>
</body>
</html>