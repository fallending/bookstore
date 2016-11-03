<#import "/spring.ftl" as spring>
<#import "macros/common.ftl" as common>

<!DOCTYPE html>
<html>
<head>
	<title>Bookstore - Exception</title>
	<link rel="stylesheet" type="text/css" href="<@spring.url '/css/public/common.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<@spring.url '/css/public/exception.css'/>"/>
</head>
<body>
	<h1 class="sectionTitle">
		Something failed.
	</h1>
	<div class="sectionMain">
		<table><tr>
			<td align="center" valign="center" width="50"><img src="<@spring.url '/img/public/error.png'/>"/></td>
			<td>
				<#if exceptionCommand??>
					<p>${exceptionCommand.message!"(no exception message present)"}</p>
					<!--
						${exceptionCommand.stackTraceAsString}
					-->
				<#else>
					<p>(no exception object to be presented)</p>
				</#if>
					<p></p>
					<form>
						<input type="button" value="Back to Previous Page" onClick="history.go(-1)"/>
					</form>
			</td>
		</tr></table>
	</div>
	<@common.sectionFooter/>
</body>
</html>
