<#--
 Copyright (C) 2013-2014 Paweł Jojczyk

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
-->
<#import "/spring.ftl" as spring/>
<#import "macros/common.ftl" as common>
<#import "macros/messages.ftl" as messages>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
	<@common.sectionEncodingHeaders/>
	<title>Bookstore - Login</title>
	<link rel="stylesheet" type="text/css" href="<@spring.url '/css/public/common.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<@spring.url '/css/public/messages.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<@spring.url '/css/public/loginPage.css'/>"/>
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
	<@common.sectionFooter/>
</body>
</html>

<#macro sectionMessages>
	<#if loggedOut??>
		<@messages.sectionSingleKindMessages ['User logged out.'] 'sectionMessagesInfo' 'info'/>
	</#if>
	<#if Session.SPRING_SECURITY_LAST_EXCEPTION?? && Session.SPRING_SECURITY_LAST_EXCEPTION.message?has_content>
		<@messages.sectionSingleKindMessages [Session.SPRING_SECURITY_LAST_EXCEPTION.message]
												'sectionMessagesError' 'error'/>
	</#if>
</#macro>

<#macro sectionHint>
<div class="hint">
	Sample credentials: admin/admin, user/user, unauthorized/unauthorized
</div>
</#macro>

<#macro sectionLoginTable>
	<form method="POST" action="<@spring.url '/auth/login'/>">
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
