<!DOCTYPE html>
<html>
<head>
	<title>Bookstore - HTTP Error</title>
	<link rel="stylesheet" type="text/css" href="/css/public/exception.css"/>
</head>
<body>
<h1 class="sectionTitle">
	HTTP Error ${httpErrorCommand.id}.
</h1>
<div class="sectionMain">
	<table><tr>
		<td align="center" valign="center" width="50"><img src="/img/public/error.png"/></td>
		<td>
			'${httpErrorCommand.description}' when accessing url:
			<a href="${httpErrorCommand.originalUrl}">${httpErrorCommand.originalUrl}</a>
			<p></p>
			<form>
				<input type="button" value="Back to Previous Page" onClick="history.go(-1)"/>
			</form>
		</td>
	</tr></table>
</div>
</body>
</html>
