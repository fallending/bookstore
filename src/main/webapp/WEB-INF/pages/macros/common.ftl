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

<#macro sectionEncodingHeaders>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</#macro>

<#macro sectionUserBar>
	<div class="sectionUserBar">
		<#if Session['SPRING_SECURITY_CONTEXT']??>
			<a href="/security/logout">
				Logout "${Session['SPRING_SECURITY_CONTEXT'].authentication.name}"
			</a>
		<#else>
			Not logged in.
		</#if>
	</div>
</#macro>

<#macro sectionTitle>
	<h1 class="sectionTitle">Bookstore</h1>
</#macro>

<#macro sectionFooter>
	<div class="sectionFooter">Copyright (C) 2013-2014 Pawe&#x0142 Jojczyk, GNU GPL v3.0</div>
</#macro>

