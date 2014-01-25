<html>
<head>
	<title>Bookstore - Exception</title>
</head>
<body>
	<h1>Something failed.</h1>
	<p>${exceptionCommand.message}</p>
	<form>
		<input type="button" value="Back to Previous Page" onClick="javascript: history.go(-1)"/>
	</form>
	<!--
	${exceptionCommand.stackTraceAsString}
	-->
</body>
</html>