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

<#import "macros/common.ftl" as common>

<!DOCTYPE html>
<html>
<head>
	<@common.sectionEncodingHeaders/>
	<title>Bookstore - HTTP Error</title>
	<link rel="stylesheet" type="text/css" href="/css/public/common.css"/>
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
	<@common.sectionFooter/>
</body>
</html>
