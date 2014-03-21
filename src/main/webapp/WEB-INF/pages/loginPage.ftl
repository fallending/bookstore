<#import "macros/common.ftl" as common>
<#import "macros/messages.ftl" as messages>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
	<meta http-equiv="content-type" content="application/xhtml+xml; charset=utf-8" />
	<title>Bookstore - Login</title>
	<link rel="stylesheet" type="text/css" href="/css/public/common.css"/>
	<link rel="stylesheet" type="text/css" href="/css/public/messages.css"/>
	<link rel="stylesheet" type="text/css" href="/css/public/loginPage.css"/>
</head>
<body>
	<div class="all">
		<@common.sectionUserBar/>
		<@common.sectionTitle/>
		<div class="sectionMain">
			<@sectionMessages/>
			<@sectionHint/>
			<@sectionLoginTable/>
		</div>
	</div>
</body>
</html>

<#macro sectionMessages>
	<#if loggedOut??>
		<@messages.sectionSingleKindMessages ['User logged out.'] 'sectionMessagesInfo' 'info'/>
	</#if>
	<#if Session.SPRING_SECURITY_LAST_EXCEPTION?? && Session.SPRING_SECURITY_LAST_EXCEPTION.message?has_content>
		<@messages.sectionSingleKindMessages [Session.SPRING_SECURITY_LAST_EXCEPTION.message] 'sectionMessagesError' 'error'/>
	</#if>
</#macro>

<#macro sectionHint>
<div class="hint">
	Sample credentials: admin/admin, user/user
</div>
</#macro>

<#macro sectionLoginTable>
	<form method="POST" action="/security/login">
		<div class="sectionTable">
			<table>
				<tr>
					<td>User:</td>
					<td><input type="text" name="username" /></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type="password" name="password" /></td>
				</tr>
			</table>
		</div>
		<div class="sectionBottomButtons">
			<input type="submit" value="Login" />
		</div>
	</form>
</#macro>
