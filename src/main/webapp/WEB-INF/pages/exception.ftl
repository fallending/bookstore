<!DOCTYPE html>
<html>
<head>
	<title>Bookstore - Exception</title>
</head>
<body>
	<h1>Something failed.</h1>
	<#if exceptionCommand??>
		<p>${exceptionCommand.message!"(no exception message present)"}</p>
	<#else>
		<p>(no exception object to be presented)</p>
	</#if>
	<p></p>
	<form>
		<input type="button" value="Back to Previous Page" onClick="javascript: history.go(-1)"/>
	</form>
	<!--
	${exceptionCommand.stackTraceAsString}
	-->
</body>
</html>